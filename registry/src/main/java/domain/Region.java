package domain;

// Region值对象，每个服务都唯一属于一个Region
public class Region {
    // Region Name，唯一标识一个Region
    private final String name;
    // Region所属国家
    private final String country;

    private Region(String name, String country) {
        this.name = name;
        this.country = country;
    }

    public static Region of(String name, String country) {
        return new Region(name, country);
    }

    public String name() {
        return name;
    }

    public String country() {
        return country;
    }
}
