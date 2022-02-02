package domain;

// Region值对象，每个服务都唯一属于一个Region
public class Region {
    // Region Id，唯一标识一个Region
    private final int id;
    // Region Name
    private String name;
    // Region所属国家
    private String country;

    private Region(int id, String name, String country) {
        this.id = id;
        this.name = name;
        this.country = country;
    }

    public static Region of(int id) {
        return new Region(id, "", "");
    }

    public static Region of(int id, String name, String country) {
        return new Region(id, name, country);
    }

    public int id() {
        return id;
    }

    public String name() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String country() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
