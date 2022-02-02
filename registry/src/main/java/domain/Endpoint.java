package domain;

/**
 * 静态工厂方法
 * 将对象创建的逻辑封装起来，为使用者提供一个简单易用的对象创建接口
 * 使用静态工厂方法来创建对象主要有两个好处
 * （1）代码可读性更好
 * （2）与使用者代码解耦，很多情况下，对象的创建往往是一个容易变化的点，通过工厂方法来封装对象的创建过程，
 *  可以在创建逻辑变更时，避免霰弹式修改
 * 使用静态工厂方法对2个关键点：
 * （1）将构造函数设计为私有，防止使用者使用new实例化对象
 * （2）为使用者提供一个或多个静态工厂方法，用于实例化对象
 */

// Endpoint值对象，其中ip和port属性为不可变，如果需要变更，需要整对象替换
public class Endpoint {
    private final String ip;
    private final int port;

    // 关键点1：将构造函数设计为私有，防止使用者使用new实例化对象
    private Endpoint(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    // 关键点2：为使用者提供一个静态工厂方法，用于实例化对象
    public static Endpoint of(String ip, int port) {
        return new Endpoint(ip, port);
    }

    // 默认端口为80的工厂方法
    public static Endpoint ofDefaultPort(String ip) {
        return new Endpoint(ip, 80);
    }

    public String ip() {
        return ip;
    }

    public int port() {
        return port;
    }

}
