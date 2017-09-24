# ip-location-core
IP地理位置信息搜索

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

ipLocationRepository.loadDataFromCsv("/PATH/TO/IPLOCATION_CSV_FILE");
```
### 查询API
```Java
IpLocation ipLocation = ipLocationRepository.findByIp(IP_LONG);
```
## TODOs
1. 增加命令行接口，支持用户导入数据
2. 性能优化：基于单台商用服务器进行性能优化
3. 性能测试：测试单台服务器的查询性能
