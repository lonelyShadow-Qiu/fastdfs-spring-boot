# FastDFS SpringBoot Starter入门

> 导语

[FastDFS](https://github.com/happyfish100/fastdfs)是一款开源的分布式文件系统，功能主要包括：文件存储、文件同步、文件访问（文件上传、文件下载）等，解决了文件大容量存储和高性能访问的问题。FastDFS特别适合以文件为载体的在线服务，如图片、视频、文档等等。

***

## 介绍

在原作[tobato项目](https://github.com/tobato/FastDFS_Client)发布的客户端基础上进行学习和细微的改动（springboot-starter的模板改动），方便自己的理解和学习。

为尊重原作者tobato的劳动成果，所以项目路径命名遵循原作com.github.tobato。

想要掌握更多可以学习[tobato项目](https://github.com/tobato/FastDFS_Client).
***

## FastDFS Starter使用方式

### 运行环境
   笔者正在学习SpringBoot，用的是2.3.4版本

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-dependencies</artifactId>
        <version>2.3.4.RELEASE</version>
        <type>pom</type>
        <scope>import</scope>
    </dependency>
    
    
### 1.在项目Pom当中加入依赖

Maven依赖为

    <dependency>
        <groupId>com.github.tobato</groupId>
        <artifactId>fastdfs-spring-boot-starter</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>

### 2.将Fdfs配置引入项目

在Maven当中配置依赖以后，SpringBoot项目将会自动导入FastDFS依赖，但不会启用，必须结合注解@EnableFastdfsConfiguration启动。

```java
@SpringBootApplication
@EnableFastdfsConfiguration
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```




#### 2.1 知识学习

##### 原作tobato的FastDFS-Client的引入方式
```java
@Configuration
@ComponentScan
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
public class FdfsClientConfig {
    // auto import
}

```

使用方式
```java
@SpringBootApplication
@Import(FdfsClientConfig.class)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```
    
>注意

##### 2.2 我弃用FdfsClientConfig @ComponentScan @Component扫描注解，使用com.github.tobato.fastdfs.autoconfiguration.FastdfsAutoConfiguration自动装配

```java
@Configuration
@ConditionalOnBean(
        annotation = {EnableFastdfsConfiguration.class}
)
@EnableConfigurationProperties(FastdfsProperties.class)
public class FastdfsAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(PooledConnectionFactory.class)
    public PooledConnectionFactory pooledConnectionFactory() {
        PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory();
        pooledConnectionFactory.setSoTimeout(properties.getSoTimeout());
        pooledConnectionFactory.setConnectTimeout(properties.getConnectTimeout());
        return pooledConnectionFactory;
    }
    
    ......省略
    
    @Bean
    @ConditionalOnMissingBean(GenerateStorageClient.class)
    @ConditionalOnBean({TrackerClient.class, FdfsConnectionManager.class})
    public GenerateStorageClient generateStorageClient(TrackerClient trackerClient,
                                                       FdfsConnectionManager fdfsConnectionManager) {
        return new DefaultGenerateStorageClient(trackerClient, fdfsConnectionManager);
    }
}
```
##### 2.3 @EnableFastdfsConfiguration
FastdfsAutoConfiguration 注解依赖
```java

@ConditionalOnBean(
        annotation = {EnableFastdfsConfiguration.class}
)

```

### 3.在application.properties当中配置Fdfs相关参数
多台服务器;分隔
```properties
# fastdfs配置 多台 10.17.14.214:22122;10.17.14.214:22122
fdfs.trackerList=10.17.14.214:22122
fdfs.soTimeout=1500
fdfs.connectTimeout=600
# 连接池
fdfs.pool.max-total=-1
fdfs.pool.max-wait-millis=5000
fdfs.pool.max-total-per-key=50
fdfs.pool.max-idle-per-key=10
fdfs.pool.max_idle_per_key=5
# 缩略图
fdfs.thumbImage.width=150
fdfs.thumbImage.height=150
```
>注意 
- 1.) com.github.tobato.fastdfs.config.FastdfsProperties
```java
@ConfigurationProperties(prefix = FdfsClientConstants.ROOT_CONFIG_PREFIX)
public class FastdfsProperties {

    /**
     * 连接地址, formats: `host:port;host:port`
     */
    private String trackerList;

    /**
     * 读取时间
     */
    private int soTimeout = 1000;
    /**
     * 连接超时时间
     */
    private int connectTimeout = 1000;
    
    ......省略
```

- 2.) com.github.tobato.fastdfs.autoconfiguration.FastdfsAutoConfiguration 使用参数

### 4.学习总结

* com.github.tobato.fastdfs.annotation 注解EnableFastdfsConfiguration
* com.github.tobato.fastdfs.autoconfiguration 自动装配FastdfsAutoConfiguration
* com.github.tobato.fastdfs.context.event banar图FastdfsBannerApplicationListener
* spring.factories 
```factories
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
com.github.tobato.fastdfs.autoconfiguration.FastdfsAutoConfiguration
org.springframework.context.ApplicationListener=\
com.github.tobato.fastdfs.context.event.FastdfsBannerApplicationListener
```

@EnableAutoConfiguration(包含在@SpringBootApplication)作用
从classpath中搜索所有META-INF/spring.factories配置文件然后，将其中org.springframework.boot.autoconfigure.EnableAutoConfiguration key对应的配置项加载到spring容器只有spring.boot.enableautoconfiguration为true（默认为true）的时候，才启用自动配置@EnableAutoConfiguration还可以进行排除，排除方式有2中，一是根据class来排除（exclude），二是根据class name（excludeName）来排除其内部实现的关键点有
* 1）ImportSelector该接口的方法的返回值都会被纳入到spring容器管理中
* 2）SpringFactoriesLoader该类可以从classpath中搜索所有META-INF/spring.factories配置文件，并读取配置

### 5.单元测试

#### 参考同Module的fastdfs-spring-boot-web工程 
com.github.tobato.fastdfs.service下有client的单元测试