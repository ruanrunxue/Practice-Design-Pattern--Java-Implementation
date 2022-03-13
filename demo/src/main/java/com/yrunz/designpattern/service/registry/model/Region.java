package com.yrunz.designpattern.service.registry.model;

// Region值对象，每个服务都唯一属于一个Region
public class Region implements Comparable<Region> {
    // Region Id，唯一标识一个Region
    private final String id;
    // Region Name
    private String name;
    // Region所属国家
    private String country;

    private Region(String id) {
        this.id = id;
    }

    public static Region of(String id) {
        return new Region(id);
    }

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    public Region withName(String name) {
        this.name = name;
        return this;
    }

    public String country() {
        return country;
    }

    public Region withCountry(String country) {
        this.country = country;
        return this;
    }

    @Override
    public int compareTo(Region other) {
        return this.id.compareTo(other.id);
    }
}
