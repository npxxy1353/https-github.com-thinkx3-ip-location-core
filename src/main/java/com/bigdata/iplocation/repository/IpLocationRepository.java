package com.bigdata.iplocation.repository;

import com.bigdata.iplocation.entity.IpLocation;

import java.io.IOException;

/**
 * Created on 2017/9/23.
 */
public interface IpLocationRepository {
    IpLocation findByIp(long ip);

    void loadDataFromCsv(String fileName) throws IOException;

    void loadDataFromCsv(String fileName, String encoding) throws IOException;
}
