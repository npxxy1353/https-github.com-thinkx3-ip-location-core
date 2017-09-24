# ip-location-core
IP location search library

## 功能
查找IP地址所属地的信息。

## 使用说明
### 初始化Redis数据
```Java
IpLocationRepository ipLocationRepository = new IpLocationRepositoryRedisImpl.Builder()
                .setRedisIp("127.0.0.1")
                .setRedisPort(6379)
                .setDatabase(3) //the database that store ip2location data
                .setPassword(null)
                .build();

ipLocationRepository2.loadDataFromCsv("/PATH/TO/IPLOCATION_CSV_FILE");
```
### 查询API
```Java
ipLocationRepository2.findByIp(IP_LONG);
```

