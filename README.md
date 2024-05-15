## CoolGateway

### 项目介绍：

简易网关项目，提供网关的基本功能，如路由转发，用户鉴权，ip限流，灰度测试，负载均衡。

### 相关知识：

项目使SpringBoot框架进行开发，nacos做配置中心，netty框架搭建网络服务。

学习本项目者需要掌握：

- spring bean生命周期中各钩子函数的使用。

- nacos 服务发现，监听服务变更的sdk使用。

- 使用netty构建网络服务，包括怎么处理读写请求。

![image-20240117151023283](C:\Users\nn\AppData\Roaming\Typora\typora-user-images\image-20240117151023283.png)

### 快速开始：

1、解压项目源码，使用idea打开，使用maven构建工具。源码工程结构如下：

```xml
CoolGateWay ---- 网关项目
CoolGateway-Core ---- 客户端调用网关服务工具包   
CoolGateWay-sample ---- 使用样例
	- AService
	- BService
	- UserService
```

项目使用nacos做服务注册中心。

启动项目前 先启动nacos服务。开发和测试环境都使用的是nacos的单实例部署方式。

2、配置项目

（1）网关服务：

配置文件地址：CoolGateWay/src/main/resources/application.yml

```yml
# 开发环境
spring:
  profiles:
    active: dev
# 日志配置
logging:
  config: classpath:logback-spring-${spring.profiles.active}.xml
# 网关具体配置
cool:
  gateway:
    port: 9000    #网关服务部署端口
    routes:       #路由配置
      - id: user  #路由id
        instanceName: userService-sample   #服务实例名，需跟注册nacos的服务实例名一样
        loadType: round  #负载均衡类型  目前有两种：round（轮询） random（随机）
        predicates:   #断言列表，请求路径与配置列表相匹配则走上面配置的服务实例。
          - /user/** 
      - id: serviceA
        instanceName: AService-sample
        loadType: round
        predicates:
          - /serviceA/**
      - id: serviceB
        instanceName: BService-sample
        loadType: round
        predicates:
          - /serviceB/**
    whites:  #白名单，可以不用鉴权。
      - /user/**
      - /serviceB/**
    limit:   
      open: false       #限流是否开启
      limitType: slide  #限流类型  1、固定窗口 2、滑动窗口   限流是根据ip地址做限流
      qps: 5     #限流qps
      timeWindow: 5000 #限流窗口  5s内同一个ip地址不能超过5次
    authority:
      accessKey: coolGateway    #鉴权密钥，使用jwt做鉴权。
  registerCenter:
    adders: 127.0.0.1:8848      #注册中心地址
    group: DEFAULT_GROUP        #注册中心组
  server:    #处理任务线程池
    coreThreadNumber: 10     #核心线程数    补充此刻的任务应该是cpu密集性
    maxThreadNumber: 10     #最大线程数
```

配置完毕后，启动网关服务。

（2）服务提供端 和 服务调用端：

配置文件地址：UserService/src/main/resources/application.properties

配置说明：

```xml
server.port=8082	#客户端部署端口
spring.application.name=UserService-sample	#注册nacos中 的服务实例名。后续需要填如网关的配置中心
spring.cloud.nacos.discovery.server-addr=127.0.0.1:8848	#nacos注册中心地址

cool.gateway.domain=http://localhost:9000	#网关自定义注解，网关地址
```

在pom文件中依赖开发好的core模块，已实现自定义start，引入即可，无需相关配置。（客户端调用网关服务工具包 ）

```xml
<dependency>
    <groupId>com.master</groupId>
    <artifactId>CoolGateWay-Core</artifactId>
    <version>${project.parent.version}</version>
</dependency>
```

3、功能使用

（1）下游服务获取鉴权信息

前端如果请求需要鉴权的服务，则需要将token 塞入请求头中。name为coolToken字段value为token。网关会自动从请求头中获取token并解析出userId存入请求头中，供下游服务使用。

```java
@GetMapping("getUserId")
public String getUser(HttpServletRequest request) throws InterruptedException {
    String userId = request.getHeader("userId"); //网关服务默认 将用户信息存入请求头中 key为userId
    System.out.println("从请求头中获取的userId "+ userId);
    return "测试token，请求成功！";
}
```

（2）服务与服务之间的调用

**上游服务**需要定义下游服务接口，并配置好参数。参考sample / AService模块的用法

```java
public interface BService {
    
    //path 为请求路径，method 默认为 get

    @CoolMethod(path = "/serviceB/aaa",method = "get")
    public UserDto getAAA(String name,int age);

    @CoolMethod(path = "/serviceB/bbb",method = "post")
    public UserDto postBBB(UserDto dto);

}
```

在需要调用下游服务的类中。新增Bservice的变量，并使用@CoolReference注解。后续通过bservice调用方法即可。

```java
//通过注解实现 自动注入代理类。
@CoolReference
private BService bService;
```

**下游服务**接口定义，可以参考sample / BService的写法

```java
//get方法获取参数。
@GetMapping("aaa")
public UserDto getAAA(@PathParam("name") String name, @PathParam("age")String age) 
    throws InterruptedException 
{
    System.out.println("aaa get");
    System.out.println("请求参数===> name="+name + " age="+age);
    UserDto data = new UserDto( name, age);
    return data;
}
```

```java
//post方法获取参数。
@PostMapping("bbb")
public UserDto postBBB(@RequestBody UserDto dto){
    System.out.println("bbb post");
    System.out.println("请求参数===> name="+dto.getName() + " age="+dto.getAge());
    UserDto data = new UserDto( "bbb",19);
    return data;
}
```

### TODO：

1、网关配置移到配置中心，并动态更新。

### 项目参考：

该项目实现主要参考：

https://github.com/ZhangBlossom/BlossomGateway

https://gitee.com/xuxueli0323/xxl-rpc