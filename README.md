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
- 优雅启动，当 spring 容器初始化完成之后再向注册中心发布服务（延迟发布）
- 优雅关闭，当服务器关闭时清理注册的服务

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

| 名称  | 长度 (byte)   | 描述  |
| ------------ | ------------ | ------------ |
| 魔数            | 4       |  标识协议包，类似与 Java 字节码文件开头的四个字节 0xcafebabe |
| 整体长度        |  4      | 整个协议包的长度  |
| 头长度          |  2      | 协议包头部长度  |
| 协议版本        |  1      |  当前协议的版本 |
| 消息类型        |  1      | 当前协议包是一个请求、响应还是心跳包  |
| 序列化方式      |  1      | 序列化 payload 采用的方式  |
| 请求id          | 4       | 当前协议包的id  |
| 协议头扩展字段  |  不确定  | 如果协议版本升级，可能会新增字段，这些字段就在此处  |
| payload         |  不确定 | 协议包数据主体  |

## 使用说明
当前版本: v1.0
### 定义 RPC 接口

> 参见 demo-api 模块

```java
package github.yuanlin;

public interface HiService {

    /**
     * hi方法
     * @return hi msg
     */
    String hi(String msg);
}
```

需要将 RPC 接口与 RPC 实现分别存放在不同的模块中

### 发布 RPC 服务

> 参见 demo-server 模块

#### 第一步：添加 Maven 依赖

```xml
<!-- rpc-core -->
<dependency>
    <groupId>github.yuanlin</groupId>
    <artifactId>rpc-core</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
<!-- demo-api -->
<dependency>
    <groupId>github.yuanlin</groupId>
    <artifactId>demo-api</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
<!-- spring -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>${version.spring}</version>
</dependency>
```
- demo-api: RPC 接口所在模块的依赖
- rpc-core: RPC 核心模块的依赖

#### 第二步: 实现服务接口
```java
package github.yuanlin.service;

import github.yuanlin.HiService;
import github.yuanlin.annotation.RpcService;

@RpcService(group = "test", version = "01")
public class HiServiceImpl implements HiService {

    @Override
    public String hi(String msg) {
        return "hi " + msg;
    }
}
```
如果服务接口有多个实现类，可以通过 group 和 version 加以区分。

#### 第三步: 配置 RPC 服务端

##### spring.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd">

    <context:component-scan base-package="github.yuanlin"/>

</beans>
```
spring 开启注解扫描。

#### 第四步: 启动 RPC 服务
```java
package github.yuanlin;

import github.yuanlin.transport.netty.server.NettyServer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DemoServerMain02 {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        NettyServer server = applicationContext.getBean(NettyServer.class);
        server.start();
    }
}
```

运行 DemoServerMain02 类，将对外发布服务，同时进行服务注册。

### 调用 RPC 服务

> 参见 demo-client 模块

#### 第一步: 添加 Maven 依赖
```xml
<!-- rpc-core -->
<dependency>
    <groupId>github.yuanlin</groupId>
    <artifactId>rpc-core</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
<!-- demo-api -->
<dependency>
    <groupId>github.yuanlin</groupId>
    <artifactId>demo-api</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
<!-- spring -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>${version.spring}</version>
</dependency>
```

#### 第二步: 配置 RPC 客户端
##### spring.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd">

    <context:component-scan base-package="github.yuanlin"/>

</beans>
```

#### 第三步: 调用 RPC 服务
```java
package github.yuanlin;

import github.yuanlin.annotation.RpcAutowire;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

public class DemoClientMain02 {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        Test test = applicationContext.getBean(Test.class);
        test.test();
    }
}

@Component
class Test {

    @RpcAutowire(group = "test", version = "01")
    private HiService hiService;

    public String test() {
        return hiService.hi("帅哥~");
    }
}
```
1. 通过 @RpcAutowire 注入代理对象
2. 调用 RPC 代理接口的方法，就像调用远程接口方法一样

### 提示: 启动注册中心

在运行前请先确保注册中心在本地启动。默认注册中心使用 ZooKeeper，启动端口 2181。

如果使用 Nacos，请更改 rpc-core 模块中 resources/META-INF.extensions 中 github.yuanlin.provider.ServiceProvider, github.yuanlin.registry.ServiceDiscovery 文件中的实现类。

# 值得一提

自己能够较完整地实现这个 RPC 框架，得益于多位前辈的 RPC 框架实现，在此特地感谢他们
- guide哥: https://github.com/Snailclimb/guide-rpc-framework
- CN-GuoZiyang: https://github.com/CN-GuoZiyang/My-RPC-Framework
- huangyong: https://gitee.com/huangyong/rpc?_from=gitee_search

同时极客时间的专栏也有很大帮助
RPC 实战与核心原理: https://time.geekbang.org/column/intro/100046201?tab=catalog

# 不足之处

- 无服务监控中心实现
- 对注册中心，客户端请求的序列化方式等的配置不够灵活，后续尝试通过读取配置文件来进行配置
- 网络传输只提供了 Netty 实现，可以尝试通过其他高性能网络通信框架来实现传输
- 缺少健康检测功能，服务端挂了不需要再继续发送请求
- 缺少异常重试功能，服务调用失败后可以尝试重新调用
- 优雅关闭优化，在关闭阶段通过增加挡板来拒绝请求并抛出特定异常，限制关闭时间
- 尝试提供自适应的负载均衡策略

秉承开源原则，该项目完整代码均能在我的github上面下载得到。能够帮到有需要的朋友那是再好不过。 觉得博主的分享还不错，不妨在github上star一下博主，激励博主更新更多实用的功能。 github: https://github.com/yuanlin-repository/yuanlin-rpc-framework
