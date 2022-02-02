package domain;

// Region值对象，每个服务都唯一属于一个Region
public class Region {
    // Region Id，唯一标识一个Region
    private final int id;
    // Region Name
    private String name;
    // Region所属国家
    private String country;

    private Region(int id) {
        this.id = id;
    }

    public static Region of(int id) {
        return new Region(id);
    }

    public int id() {
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
}
