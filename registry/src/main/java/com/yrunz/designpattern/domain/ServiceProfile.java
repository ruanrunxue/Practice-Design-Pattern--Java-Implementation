package com.yrunz.designpattern.domain;

/**
 * 建造者模式
 * 建造者模式适用于对象成员较多，创建对象逻辑较为繁琐的场景，主要都优点有如下2个：
 * （1）封装复杂对象的创建过程，使对象使用者不感知复杂的创建逻辑
 * （2）可以一步步按照顺序对成员进行赋值，或者创建嵌套对象，并最终完成目标对象的创建
 * 实现建造者模式的几个关键点：
 * 1、在目标对象内创建一个静态内部Builder类
 * 2、Builder持有需要创建的目标对象作为成员属性
 * 3、定义Builder私有构造函数，在构造函数中实例化目标对象
 * 4、在Builder类中定义目标对象的成员属性设置方法
 * 5、在成员属性设置方法中返回Builder的this指针，支持链式调用
 * 6、为Builder类定义Build方法，返回目标对象实例
 * 7、目标对象定义私有构造函数，防止使用者直接实例化对象
 * 8、目标对象定义Builder静态工厂方法，返回Builder实例，通过Builder实例进行对象的创建
 */

// 服务档案，其中服务ID唯一标识一个服务实例，一种服务类型可以有多个服务实例
public class ServiceProfile implements Cloneable<ServiceProfile>, Comparable<ServiceProfile> {
    private final String id;
    private String type;
    private ServiceStatus status;
    private Endpoint endpoint;
    private Region region;
    private int priority; // 服务优先级，范围0～100，值越低，优先级越高
    private int load; // 服务负载，负载越高标识服务处理的业务压力越大

    // 建造者模式关 键点7：目标对象定义私有构造函数，防止使用者直接实例化对象
    private ServiceProfile(String id) {
        this.id = id;
    }

    // 建造者模式 关键点8：目标对象定义Builder静态工厂方法，返回Builder实例，通过Builder实例进行对象的创建
    public static Builder Builder(String serviceId) {
        return new Builder(serviceId);
    }

    public String id() {
        return id;
    }

    public String type() {
        return type;
    }

    public ServiceStatus status() {
        return status;
    }

    public Endpoint endpoint() {
        return endpoint;
    }

    public Region region() {
        return region;
    }

    public int priority() {
        return priority;
    }

    public int load() {
        return load;
    }

    // 原型模式 关键点2：clone方法中实现对象复制逻辑
    @Override
    public ServiceProfile clone() {
        ServiceProfile newProfile = new ServiceProfile(this.id);
        newProfile.type = this.type;
        newProfile.endpoint = this.endpoint;
        newProfile.region = this.region;
        newProfile.priority = this.priority;
        newProfile.load = this.load;
        return newProfile;
    }

    @Override
    public int compareTo(ServiceProfile other) {
        return this.id.compareTo(other.id);
    }

    // 建造者模式 关键点1：在目标对象内创建一个静态内部Builder类
    public static class Builder {
        // 建造者模式 关键点2：Builder持有需要创建的目标对象作为成员属性
        private final ServiceProfile profile;

        // 建造者模式 关键点3：定义Builder私有构造函数，在构造函数中实例化目标对象
        private Builder(String id) {
            profile = new ServiceProfile(id);
        }

        // 建造者模式 关键点4：在Builder类中定义目标对象的成员属性设置方法
        public Builder withType(String type) {
            profile.type = type;
            // 关键点5：在成员属性设置方法中返回Builder的this指针，支持链式调用
            return this;
        }

        public Builder withStatus(ServiceStatus status) {
            profile.status = status;
            return this;
        }

        public Builder withEndpoint(String ip, int port) {
            profile.endpoint = Endpoint.of(ip, port);
            return this;
        }

        public Builder withRegionId(String regionId) {
            profile.region = Region.of(regionId);
            return this;
        }

        public Builder withRegion(String regionId, String regionName, String country) {
            profile.region = Region.of(regionId).withName(regionName).withCountry(country);
            return this;
        }

        public Builder withPriority(int priority) {
            profile.priority = priority;
            return this;
        }

        public Builder withLoad(int load) {
            profile.load = load;
            return this;
        }

        // 建造者模式 关键点6：为Builder类定义Build方法，返回目标对象实例
        public ServiceProfile Build() {
            return profile;
        }
    }
}
