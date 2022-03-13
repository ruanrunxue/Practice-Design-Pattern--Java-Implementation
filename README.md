# 实践GoF的23种设计模式：Java实现

## 文章目录

| 归类       |                 模式                  |                           示例代码                           |                             文章                             |
| ---------- | :-----------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: |
| SOLID原则  |          单一职责原则（SRP）          | [Registry](demo/src/main/java/com/yrunz/designpattern/service/registry/Registry.java) | [实践GoF的23种设计模式: SOLID原则](docs/practice_design_pattern__solid_principle.md) |
|            |            开闭原则（OCP）            | [Pipeline](demo/src/main/java/com/yrunz/designpattern/monitor/pipeline/Pipeline.java) | [实践GoF的23种设计模式: SOLID原则](docs/practice_design_pattern__solid_principle.md) |
|            |          里氏替换原则（LSP）          | [PipelineFactory](demo/src/main/java/com/yrunz/designpattern/monitor/pipeline/PipelineFactory.java) | [实践GoF的23种设计模式: SOLID原则](docs/practice_design_pattern__solid_principle.md) |
|            |          接口隔离原则（ISP）          | [Mq](demo/src/main/java/com/yrunz/designpattern/mq/MemoryMq.java) | [实践GoF的23种设计模式: SOLID原则](docs/practice_design_pattern__solid_principle.md) |
|            |          依赖倒置原则（DIP）          | [Db](demo/src/main/java/com/yrunz/designpattern/db/Db.java)  | [实践GoF的23种设计模式: SOLID原则](docs/practice_design_pattern__solid_principle.md) |
| 创建型模式 |         单例模式（Singleton）         | [Network](demo/src/main/java/com/yrunz/designpattern/network/Network.java) |                                                              |
|            |         建造者模式（Builder）         | [ServiceProfile.Builder](demo/src/main/java/com/yrunz/designpattern/service/registry/model/ServiceProfile.java) |                                                              |
|            |    工厂方法模式（Factory Method）     | [SidecarFactory](demo/src/main/java/com/yrunz/designpattern/sidecar/SidecarFactory.java) |                                                              |
|            |   抽象工厂模式（Abstract Factory）    | [ConfigFactory](demo/src/main/java/com/yrunz/designpattern/monitor/config/ConfigFactory.java) |                                                              |
|            |         原型模式（Prototype）         | [Cloneable](demo/src/main/java/com/yrunz/designpattern/service/registry/model/Cloneable.java) |                                                              |
| 结构型模式 |         适配器模式（Adapter）         | [DslResultRender](demo/src/main/java/com/yrunz/designpattern/db/console/DslResultRender.java) |                                                              |
|            |          桥接模式（Bridge）           | [Pipeline](demo/src/main/java/com/yrunz/designpattern/monitor/pipeline/Pipeline.java) |                                                              |
|            |         组合模式（Composite）         | [Pipeline](demo/src/main/java/com/yrunz/designpattern/monitor/pipeline/Pipeline.java) |                                                              |
|            |        装饰者模式（Decorator）        | [FlowCtrlSidecar](demo/src/main/java/com/yrunz/designpattern/sidecar/FlowCtrlSidecar.java) |                                                              |
|            |          外观模式（Facade）           | [ShoppingCenter](demo/src/main/java/com/yrunz/designpattern/service/shopping/ShoppingCenter.java) |                                                              |
|            |         享元模式（Flyweight）         | [RegionTable](demo/src/main/java/com/yrunz/designpattern/service/registry/model/schema/RegionTable.java) |                                                              |
|            |           代理模式（Proxy）           | [CacheDbProxy](demo/src/main/java/com/yrunz/designpattern/db/cache/CacheDbProxy.java) |                                                              |
| 行为型模式 | 责任链模式（Chain Of Responsibility） | [FilterChain](demo/src/main/java/com/yrunz/designpattern/monitor/filter/FilterChain.java) |                                                              |
|            |          命令模式（Command）          | [Command](demo/src/main/java/com/yrunz/designpattern/db/transaction/Command.java) |                                                              |
|            |        迭代器模式（Iterator）         | [TableIterator](demo/src/main/java/com/yrunz/designpattern/db/TableIterator.java) |                                                              |
|            |        中介者模式（Mediator）         | [Mediator](demo/src/main/java/com/yrunz/designpattern/service/mediator/Mediator.java) |                                                              |
|            |         备忘录模式（Memento）         | [CmdHistory](demo/src/main/java/com/yrunz/designpattern/db/transaction/CmdHistory.java) |                                                              |
|            |        观察者模式（Observer）         | [SocketImpl](demo/src/main/java/com/yrunz/designpattern/network/SocketImpl.java) |                                                              |
|            |           状态模式（State）           | [FcState](demo/src/main/java/com/yrunz/designpattern/sidecar/flowctrl/FcState.java) |                                                              |
|            |         策略模式（Strategy）          | [InputPlugin](demo/src/main/java/com/yrunz/designpattern/monitor/input/InputPlugin.java) |                                                              |
|            |    模板方法模式（Template Method）    | [AbstractFcState](demo/src/main/java/com/yrunz/designpattern/sidecar/flowctrl/AbstractFcState.java) |                                                              |
|            |         访问者模式（Visitor）         | [TableVisitor](demo/src/main/java/com/yrunz/designpattern/db/TableVisitor.java) |                                                              |

## 示例代码demo介绍

示例代码demo工程实现了一个简单的分布式应用系统（单机版），该系统主要由以下几个模块组成：

- **网络 Network**，网络功能模块，模拟实现了报文转发、socket通信、http通信等功能。
- **数据库 Db**，数据库功能模块，模拟实现了表、事务、dsl等功能。
- **消息队列 Mq**，消息队列模块，模拟实现了基于topic的生产者/消费者的消息队列。
- **监控系统 Monitor**，监控系统模块，模拟实现了服务日志的收集、分析、存储等功能。
- **边车 Sidecar**，边车模块，模拟对网络报文进行拦截，实现access log上报、消息流控等功能。
- **服务 Service**，运行服务，当前模拟实现了服务注册中心、在线商城服务集群、服务消息中介等服务。

![](https://tva1.sinaimg.cn/large/e6c9d24egy1gzn32jkkduj213g0o00xq.jpg)

主要目录结构如下：

```shell
├── db                # 数据库模块，定义Db、Table、TableVisitor等抽象接口 【@单例模式】
│   ├── cache         # 数据库缓存代理，为Db新增缓存功能 【@代理模式】
│   ├── console       # 数据库控制台实现，支持dsl语句查询和结果显示 【@适配器模式】
│   ├── dsl           # 实现数据库dsl语句查询能力，当前只支持select语句查询 【@解释器模式】
│   ├── exception     # 数据库模块相关异常定义
│   ├── iterator      # 遍历表迭代器，包含按序遍历和随机遍历 【@迭代器模式】
│   └── transaction   # 实现数据库的事务功能，包括执行、提交、回滚等 【@命令模式】【@备忘录模式】
├── monitor        # 监控系统模块，采用插件式的架构风格，当前实现access log日志etl功能
│   ├── config     # 监控系统插件配置模块  【@抽象工厂模式】【@组合模式】
│   │   ├── json   # 实现基于json格式文件的配置加载功能
│   │   └── yaml   # 实现基于yaml格式文件的配置加载功能
│   ├── model     # 监控系统模型定义
│   │   └── schema   # 监控系统模型对应相关的数据表定义
│   ├── exception  # 监控系统相关异常
│   ├── filter     # Filter插件的实现定义  【@责任链模式】
│   ├── input      # Input插件的实现定义   【@策略模式】
│   ├── output     # Output插件的实现定义
│   ├── pipeline   # Pipeline插件的实现定义，一个pipeline表示一个ETL处理流程 【@桥接模式】
│   └── plugin     # 插件抽象接口定义
├── mq          # 消息队列模块
├── network        # 网络模块，模拟网络通信，定义了socket、packet等通用类型/接口  【@观察者模式】
│   └── http       # 模拟实现了http通信等服务端、客户端能力
├── service           # 服务模块，定义了服务的基本接口
│   ├── mediator      # 服务消息中介，作为服务通信的中转方，实现了服务发现，消息转发的能力 【@中介者模式】
│   ├── registry      # 服务注册中心，提供服务注册、去注册、更新、 发现、订阅、去订阅、通知等功能
│   │   └── model    # 服务注册/发现相关的模型定义 【@原型模式】【@建造者模式】
│   │        └── schema    # 服务注册中心相关的数据表定义 【@访问者模式】【@享元模式】
│   └── shopping      # 模拟在线商城服务群的定义，包含订单服务、库存服务、支付服务、发货服务 【@外观模式】
└── sidecar        # 边车模块，对socket进行拦截，提供http access log、流控功能 【@装饰者模式】【@工厂模式】
    └── flowctrl   # 流控模块，基于消息速率进行随机流控 【@模板方法模式】【@状态模式】
```
