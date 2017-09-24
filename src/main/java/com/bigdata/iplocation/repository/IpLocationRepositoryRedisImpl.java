package com.bigdata.iplocation.repository;

import com.bigdata.iplocation.entity.IpLocation;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;

/**
 * Created on 2017/9/23.
 */
public class IpLocationRepositoryRedisImpl implements IpLocationRepository {
    private JedisPool jedisPool;

    private IpLocationRepositoryRedisImpl(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public IpLocation findByIp(long ip) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Set<String> ipLocations = jedis.zrevrangeByScore("ip2loc", ip, 0, 0, 1);

            if (ipLocations == null || ipLocations.size() == 0) {
                return null;
            }

            for (String each : ipLocations) {
                return IpLocation.fromCsv(each);
            }
        } finally {
            //not really closed, only return jedis to pool
            jedis.close();
        }

        return null;
    }

    public void loadDataFromCsv(String csvFileName) throws IOException {
        loadDataFromCsv(csvFileName, "utf-8");
    }

    public void loadDataFromCsv(String csvFileName, String encoding) throws IOException {
        File file = new File(csvFileName);

        if (!file.exists()) {
            throw new FileNotFoundException("fileName : " + csvFileName);
        }

        Jedis jedis = null;
/*
        try{
            Reader in = new InputStreamReader(new FileInputStream(file), encoding);
            Iterable<CSVRecord> records = CSVFormat.RFC4180.withHeader("ipFrom", "ipTo", "countryCode","countryName","regionName","cityName","latitude","longitude","zipCode","timeZone").parse(in);
            for (CSVRecord record : records) {
                IpLocation ipLocation = IpLocation.fromCsvRecord(record);
                jedis.zadd("ip2loc", ipLocation.getIpFrom(), ipLocation.toJson());
            }
        }finally {
            jedis.close();
        }*/

        LineIterator it = FileUtils.lineIterator(file, encoding);
        try {
            jedis = jedisPool.getResource();
            while (it.hasNext()) {
                String line = it.nextLine();

                IpLocation ipLocation = IpLocation.fromCsv(line);
                jedis.zadd("ip2loc", ipLocation.getIpFrom(), line);
            }
        } finally {
            LineIterator.closeQuietly(it);
            jedis.close();
        }
    }


    public static class Builder {

        private String ip = "127.0.0.1";
        private int port = 6379;
        private int database = 0;
        private String password;

        // 可用连接实例的最大数目，默认值为8；
        // 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
        private int maxActive = 8;
        // 控制一个pool最多/最少有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
        private int maxIdle = 3;
        private int minIdel = 1;
        // 等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
        private int maxWait = 200;
        private int timeOut = 1000;
        // 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
        private boolean testOnBorrow = true;
        private JedisPool jedisPool = null;

        public Builder setRedisIp(String ip) {
            this.ip = ip;
            return this;
        }

        public Builder setRedisPort(int port) {
            this.port = port;
            return this;
        }

        public Builder setDatabase(int database) {
            this.database = database;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setJedisPool(JedisPool jedisPool) {
            this.jedisPool = jedisPool;
            return this;
        }

        public IpLocationRepositoryRedisImpl build() {
            if (jedisPool == null) {
                JedisPoolConfig config = new JedisPoolConfig();
                config.setMaxTotal(maxActive);
                config.setMaxIdle(maxIdle);
                config.setMinIdle(minIdel);
                config.setMaxWaitMillis(maxWait);
                config.setTestOnBorrow(testOnBorrow);
                jedisPool = new JedisPool(config, ip, port, timeOut, password, database);
            }

            IpLocationRepositoryRedisImpl ipLocationRepositoryRedis = new IpLocationRepositoryRedisImpl(jedisPool);
            return ipLocationRepositoryRedis;
        }


    }
}
