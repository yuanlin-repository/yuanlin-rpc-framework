# yuanlin-rpc-framework
yuanlin-rpc-framework 是一款基于 Netty 实现的 RPC 框架，框架主要分为网络传输、服务发布、服务调用三个部分，实现了多种序列化方法和负载均衡策略，注册中心支持 ZooKeeper 和 Nacos。
## 架构
![RPC架构](https://user-images.githubusercontent.com/52808768/149269295-f324d8e4-61b4-4301-a1a4-096c000380f1.png)
服务提供者（server）向注册中心（registry）注册服务，服务消费者（client）通过注册中心获取服务地址，再通过网络向服务提供者发起服务调用请求。
## 特性
通用
- 良好的接口抽象，详细的中文注释，单元测试
- 实现 4 种序列化方式（Hessian、Kryo、Protostuff、Json）
- 实现 3 种负载均衡策略（一致性哈希、轮询、随机）
- 支持 ZooKepper 和 Nacos 两种注册中心

网络传输模块
- 通过 Netty（NIO）实现网络传输
- 添加 Netty 心跳机制
- 自定义通信协议（可扩展且向后兼容）
- 消费端可以复用 Channel，避免多次连接

服务发布模块（Server 端）
- 通过 Spring + 注解的方式实现服务自动注册

服务调用模块（Client 端）
- 通过 Spring + 注解的方式实现服务消费
- 动态代理屏蔽网络通信细节

## 项目模块概览
- demo-api: 示例-服务接口
- demo-client: 示例-客户端
- demo-server: 示例-服务端
- rpc-common: RPC 框架通用模块，包含枚举，异常，工具类等
- rpc-core: RPC 框架核心模块，包含客户端，服务端，网络通信模块实现

## 传输协议
使用 Netty 进行传输时使用了如下传输协议：
![未命名文件](https://user-images.githubusercontent.com/52808768/149275549-8b4d43ed-9d27-48fc-b3d6-a8ee90c61d67.png)
字段解释：
- 魔数: 标识协议包，类似与 Java 字节码文件开头的四个字节 0xcafebabe
- 整体长度: 整个协议包的长度
- 头长度: 协议包头部长度
- 协议版本: 当前协议的版本
- 消息类型: 当前协议包是一个请求、响应还是心跳包
- 序列化方式: payload 采用的序列化方式
- 请求id: 当前协议包的id
- 协议头扩展字段: 如果协议版本升级，可能会新增字段，这些字段就在此处
- payload: 协议包数据主体

## 使用
