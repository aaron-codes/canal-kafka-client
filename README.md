# Data collection canal channal

[toc]

## 简介

Canal 通道数据收集程序是 Canal Client 的实现方案，它主要实现了监听 Canal Server 分发的 binlog 日志，将其解析为数据收集端通用数据协议 EventEntry， 发送至 Kafka 消息队列。

## 前提条件

* [x] Canal-Server 端已经配置相应的数据源

> 举个栗子：开发环境 Canal Server 安装目录 `/opt/tools/canal` 其 conf 目录配置了*BusinessDataSource*、*example*、*RealStatDataSource* 数据源

```
.
├── BusinessDataSource
│   ├── h2.mv.db
│   ├── h2.trace.db
│   ├── instance.properties
│   └── rds_instance.properties
├── canal.properties
├── example
│   ├── h2.mv.db
│   ├── instance.properties
│   ├── meta.dat
│   └── rds_instance.properties
├── logback.xml
├── RealStatDataSource
│   ├── h2.mv.db
│   ├── h2.trace.db
│   ├── instance.properties
│   ├── meta.dat
│   └── rds_instance.properties
└── spring
```



## 快速开始

### 打包

#### 使用 Maven 打包

```
mvn clean package -P dev/release
```
> -P release  会打包为  data-channel-canal-0.0.1-SNAPSHOT.tar.gz 文件，方便发布


#### 解压目录结构说明

.
├── README.md          `文档`
├── bin                `启动脚本`
│   ├── start.sh
│   └── stop.sh
├── conf               `配置文件`
│   ├── application.properties
│   └── logback.xml
├── lib                `依赖 jar 包`
│   ├── aopalliance-1.0.jar
│   ├── canal.client-1.0.25.jar
│   ├── canal.common-1.0.25.jar
│   ├── canal.protocol-1.0.25.jar
│   ├── commons-codec-1.9.jar
│   ├── commons-io-2.4.jar
│   ├── commons-lang-2.6.jar
│   ├── commons-logging-1.1.3.jar
│   ├── data-channel-canal-0.0.1-SNAPSHOT.jar
│   ├── fastjson-1.2.28.jar
│   ├── guava-18.0.jar
│   ├── jcl-over-slf4j-1.7.12.jar
│   ├── kafka-clients-1.0.0.jar
│   ├── logback-classic-1.1.3.jar
│   ├── logback-core-1.1.3.jar
│   ├── lz4-java-1.4.jar
│   ├── netty-3.2.2.Final.jar
│   ├── netty-all-4.1.6.Final.jar
│   ├── protobuf-java-2.6.1.jar
│   ├── slf4j-api-1.7.25.jar
│   ├── snappy-java-1.1.4.jar
│   ├── spring-aop-3.2.9.RELEASE.jar
│   ├── spring-beans-3.2.9.RELEASE.jar
│   ├── spring-context-3.2.9.RELEASE.jar
│   ├── spring-core-3.2.9.RELEASE.jar
│   ├── spring-expression-3.2.9.RELEASE.jar
│   ├── spring-jdbc-3.2.9.RELEASE.jar
│   ├── spring-orm-3.2.9.RELEASE.jar
│   ├── spring-tx-3.2.9.RELEASE.jar
│   ├── zkclient-0.10.jar
│   └── zookeeper-3.4.5.jar
└── logs         `日志目录`


### 运行

启动

```
./bin/start.sh
```
停止

```
./bin/stop.sh
```

## 如何配置

### application.properties

启动程序时，会读取项目目录 `conf/application.properties`, 如需要配置 cacal destination(即canal的数据源名称)，则修改该文件。

举个栗子：datasource1 消费进程配置如下：

```
#
# /**
# *
# *    Created by OuYangX.
# *    Copyright (c) 2018, ouyangxian@gmail.com All Rights Reserved.
# *
# */
#数据源名称，支持多个 datasource1,datasource2...
destination=BusinessDataSource

#BusinessDataSource Conf

#[cluster|simple]
BusinessDataSource.client.model=cluster

#BusinessDataSource client HA 模式下 zookeeper 地址
BusinessDataSource.zk.address=hadoop02:2181,hadoop01:2181,hadoop03:2181
BusinessDataSource.username=
BusinessDataSource.password=

#datasource1 kafka 地址
BusinessDataSource.kafka.address=hadoop02:9092

```

## 源码说明

data-channel-canal 源码地址：http://git.zyxr.com/hadoop/data-collection-zyxrcanal

└── main
    ├── assembly
    │   ├── bin
    │   │   ├── start.sh
    │   │   └── stop.sh
    │   ├── conf
    │   │   └── logback.xml
    │   ├── dev.xml
    │   └── release.xml
    ├── java
    │   └── com
    │       └── zyxr
    │           └── bi
    │               └── business
    │                   ├── Application.java
    │                   ├── model
    │                   │   └── event
    │                   │       ├── EventChannel.java
    │                   │       ├── EventEntry.java
    │                   │       └── EventType.java
    │                   ├── process
    │                   │   ├── KafkaCanalClient.java
    │                   │   └── base
    │                   │       ├── AbstractCanalClient.java
    │                   │       └── ConsoleCanalClient.java
    │                   └── util
    │                       └── SystemConfig.java
    └── resources
        ├── application.properties
        └── logback.xml

## EventEntry 协议对象

![](http://7xigvj.com1.z0.glb.clouddn.com/15211929181966.jpg)
 
## CanalClient Kafka 实现类

![](http://7xigvj.com1.z0.glb.clouddn.com/15211924985710.jpg)

## FAQ




