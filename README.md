# yuanlin-rpc-framework
yuanlin-rpc-framework is an RPC framework based on Netty. The framework is mainly divided into three parts: network transmission, service publishing, and service calling. It implements multiple serialization methods and load balancing strategies. The registration center supports ZooKeeper and Nacos.
## Architecture
![RPC架构](https://user-images.githubusercontent.com/52808768/149269295-f324d8e4-61b4-4301-a1a4-096c000380f1.png)
The service provider (server) registers the service with the registry. The service consumer (client) obtains the service address through the registry and then initiates a service call request to the service provider through the network.
## Features
General
- Good interface abstraction, detailed Chinese comments, unit testing
- Implement 4 serialization methods (Hessian, Kryo, Protostuff, Json)
- Implement 3 load balancing strategies (consistent hashing, polling, random)
- Support ZooKepper and Nacos two registration centers

Network transmission module
- Network transmission through Netty (NIO)
- Add Netty heartbeat mechanism
- Custom communication protocol (scalable and backward compatible)
- Consumers can reuse Channel to avoid multiple connections

Service publishing module (Server side)
- Automatic service registration through Spring + annotation
- Graceful start, publish services to the registration center after the spring container is initialized (delayed release)
- Graceful shutdown, clean up registered services when the server is shut down

Service call module (Client side)
- Service consumption through Spring + annotation
- Dynamic proxy shields network communication details

## Project module overview
- demo-api: Example-service interface
- demo-client: Example-client
- demo-server: Example-server
- rpc-common: RPC framework common module, including enumeration, exception, tool class, etc.
- rpc-core: RPC framework core module, including client, server, network communication module implementation

## Transmission protocol
The following transmission protocols are used when using Netty for transmission:
![未命名文件](https://user-images.githubusercontent.com/52808768/149275549-8b4d43ed-9d27-48fc-b3d6-a8ee90c61d67.png)
Field explanation:

| Name | Length (byte) | Description |
| ------------ | ------------ | ------------ |
| Magic number | 4 | Identifies the protocol package, similar to the four bytes at the beginning of the Java bytecode file 0xcafebabe |
| Overall length | 4 | Length of the entire protocol package |
| Header length | 2 | Length of the protocol package header |
| Protocol version | 1 | Version of the current protocol |
| Message type | 1 | Is the current protocol package a request, response, or heartbeat package |
| Serialization method | 1 | Method used to serialize the payload |
| Request id | 4 | ID of the current protocol package |
| Protocol header extension field | Uncertain | If the protocol version is upgraded, new fields may be added, and these fields are here |
| Payload | Uncertain | Protocol package data body |

## Instructions
Current version: v1.0
### Define RPC interface

> See demo-api module

```java
package github.yuanlin;

public interface HiService {

    /**
     * hi method
     * @return hi msg
     */
    String hi(String msg);
}
```

The RPC interface and RPC implementation need to be stored in different modules

### Publish RPC service

> See demo-server module

#### Step 1: Add Maven dependency

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
- demo-api: Dependencies of the module where the RPC interface is located
- rpc-core: Dependencies of the RPC core module

#### Step 2: Implement the service interface
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
If a service interface has multiple implementation classes, they can be distinguished by group and version.

#### Step 3: Configure the RPC server

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
Spring turns on annotation scanning.

#### Step 4: Start the RPC service
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

Run the DemoServerMain02 class to publish the service and register the service at the same time.

### Call RPC service

> See demo-client module

#### Step 1: Add Maven dependency
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

#### Step 2: Configure the RPC client
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

#### Step 3: Call RPC service
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
1. Inject the proxy object through @RpcAutowire
2. Call the RPC proxy interface method just like calling the remote interface method

### Tips: Start the registry

Before running, please make sure the registry is started locally. The default registry uses ZooKeeper and starts port 2181.

If you use Nacos, please change the implementation class in the github.yuanlin.provider.ServiceProvider, github.yuanlin.registry.ServiceDiscovery files in resources/META-INF.extensions in the rpc-core module.

# Worth mentioning

I was able to implement this RPC framework relatively completely, thanks to the RPC framework implementation of many predecessors, and I would like to thank them here
- guide: https://github.com/Snailclimb/guide-rpc-framework
- CN-GuoZiyang: https://github.com/CN-GuoZiyang/My-RPC-Framework
- huangyong: https://gitee.com/huangyong/rpc?_from=gitee_search

At the same time, the Geek Time column is also very helpful
RPC practice and core principles: https://time.geekbang.org/column/intro/100046201?tab=catalog

# Insufficient features

- No server monitoring center implementation
- The configuration of the registration center, serialization of client requests, etc. is not flexible enough. We will try to configure it by reading the configuration file later
- Only Netty is provided for network transmission. We can try to implement transmission through other high-performance network communication frameworks
- Lack of health detection function. If the server hangs up, there is no need to continue sending requests
- Lack of exception retry function. After the service call fails, we can try to call it again
- Graceful shutdown optimization. In the shutdown phase, we reject requests and throw specific exceptions by adding baffles to limit the shutdown time
- Try to provide adaptive load balancing strategy

Adhering to the principle of open source, the complete code of this project can be downloaded from my github. It would be great if we can help friends in need. I think the blogger's sharing is good. You may as well star the blogger on github to encourage him to update more practical functions. github: https://github.com/yuanlin-repository/yuanlin-rpc-framework
