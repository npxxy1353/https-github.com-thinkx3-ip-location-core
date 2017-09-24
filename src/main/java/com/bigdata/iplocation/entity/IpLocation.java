package com.bigdata.iplocation.entity;

import com.google.gson.Gson;

/**
 * Created on 2017/9/23.
 */
public class IpLocation {
    private Long ipFrom;
    private Long ipTo;
    private String countryCode;
    private String countryName;
    private String regionName;
    private String cityName;
    private Double latitude;
    private Double longitude;
    private String zipCode;
    private String timeZone;

    public IpLocation(Long ipFrom, Long ipTo, String countryCode, String countryName, String regionName, String cityName, Double latitude, Double longitude, String zipCode, String timeZone) {
        this.ipFrom = ipFrom;
        this.ipTo = ipTo;
        this.countryCode = countryCode;
        this.countryName = countryName;
        this.regionName = regionName;
        this.cityName = cityName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.zipCode = zipCode;
        this.timeZone = timeZone;
    }

    public Long getIpFrom() {
        return ipFrom;
    }

    public Long getIpTo() {
        return ipTo;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getRegionName() {
        return regionName;
    }

    public String getCityName() {
        return cityName;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getTimeZone() {
        return timeZone;
    }

    /*
    csv line format : "16777216","16777471","AU","Australia","Queensland","Brisbane","-27.467940","153.028090","4000","+10:00"
    * */
    public static IpLocation fromCsv(String line) {
        String[] record = line.replaceFirst("\"", "").split("\",\"");

        IpLocation ipLocation = new IpLocation(
                Long.valueOf(record[0]), /*ipFrom*/
                Long.valueOf(record[1]), /*ipTo*/
                record[2], /*countryCode*/
                record[3], /*coutryName*/
                record[4], /*regionName*/
                record[5], /*cityName*/
                Double.valueOf(record[6]), /*latitude*/
                Double.valueOf(record[7]), /*longitude*/
                record[8], /*zipCode*/
                record[9].replaceAll("\"", "") /*timeZone*/
        );
        return ipLocation;
    }

    public static IpLocation fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, IpLocation.class);
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    @Override
    public String toString() {
        return "IpLocation{" +
                "ipFrom=" + ipFrom +
                ", ipTo=" + ipTo +
                ", countryCode='" + countryCode + '\'' +
                ", countryName='" + countryName + '\'' +
                ", regionName='" + regionName + '\'' +
                ", cityName='" + cityName + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", zipCode='" + zipCode + '\'' +
                ", timeZone='" + timeZone + '\'' +
                '}';
    }
}
