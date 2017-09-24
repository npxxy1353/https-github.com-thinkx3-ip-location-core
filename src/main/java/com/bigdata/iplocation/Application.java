package com.bigdata.iplocation;

import com.bigdata.iplocation.entity.IpLocation;
import com.bigdata.iplocation.repository.IpLocationRepository;
import com.bigdata.iplocation.repository.IpLocationRepositoryRedisImpl;

import java.io.IOException;

/**
 * Show how to use iplocation client
 * Created on 2017/9/23.
 */
public class Application {
    public static void main(String[] args) throws IOException {
        final IpLocationRepository ipLocationRepository = new IpLocationRepositoryRedisImpl.Builder()
                .setRedisIp("127.0.0.1")
                .setRedisPort(6379)
                .setDatabase(3) //the database that store ip2location data
                .setPassword(null)
                .build();

        final IpLocationRepository ipLocationRepository2 = new IpLocationRepositoryRedisImpl.Builder()
                .setRedisIp("127.0.0.1")
                .setRedisPort(6381)
                .setDatabase(3) //the database that store ip2location data
                .setPassword(null)
                .build();

        long start = System.currentTimeMillis();
        ipLocationRepository2.loadDataFromCsv("/Users/leo/Downloads/IP2LOCATION-LITE-DB11.CSV");
        System.err.println("data loaded.");

        Thread t1 = new Thread() {
            public void run() {
                long start = System.currentTimeMillis();
                //perform test
                final int TEST_ROUND = 200000;
                for (int i = 0; i < TEST_ROUND; i++) {
                    long random = (long) (Math.random() * 0xFFFFFFFF);
                    ipLocationRepository.findByIp(random);
                }

                long cost = (System.currentTimeMillis() - start);
                System.err.println("1query " + TEST_ROUND + " times, total takes : [" + cost / 1000.0 + "] s, per query : [ " + (double) cost / TEST_ROUND + "] ms");
                System.err.println("1query " + (double) TEST_ROUND / cost + " times/ms");
            }
        };

        Thread t2 = new Thread() {
            public void run() {
                long start = System.currentTimeMillis();
                //perform test
                final int TEST_ROUND = 200000;
                for (int i = 0; i < TEST_ROUND; i++) {
                    long random = (long) (Math.random() * 0xFFFFFFFF);
                    IpLocation ipLocation = ipLocationRepository2.findByIp(random);
                }

                long cost = (System.currentTimeMillis() - start);
                System.err.println("2query " + TEST_ROUND + " times, total takes : [" + cost / 1000.0 + "] s, per query : [ " + (double) cost / TEST_ROUND + "] ms");
                System.err.println("2query " + (double) TEST_ROUND / cost + " times/ms");
            }
        };

        t1.start();
        t2.start();

        try {
            Thread.currentThread().join(20000);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
