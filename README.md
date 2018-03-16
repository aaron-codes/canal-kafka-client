# Data collection canal channal

[toc]

## 简介

Canal 通道数据收集程序是 Canal Client 的实现方案，它主要实现了监听 Canal Server 分发的 binlog 日志，将其解析为数据收集端通用数据协议 EventEntry， 发送至 Kafka 消息队列。

## 前提条件

* [x] Canal-Server 端已经配置相应的数据源

> 举个栗子：开发环境 Canal Server 安装目录 `/opt/tools/canal` 其 conf 目录配置了*BusinessDataSource*、*example*、*RealStatDataSource* 数据源

![](http://7xigvj.com1.z0.glb.clouddn.com/15211933432574.jpg)

## 快速开始

### 打包

#### 使用 Maven 打包

```
mvn clean package -P dev/release
```
> -P release  会打包为  data-channel-canal-0.0.1-SNAPSHOT.tar.gz 文件，方便发布


#### 解压目录结构说明

![](http://7xigvj.com1.z0.glb.clouddn.com/15211933905398.jpg)

### 运行

#### 启动

```
./bin/start.sh
```
#### 停止

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

![](http://7xigvj.com1.z0.glb.clouddn.com/15211932749261.jpg)

### EventEntry 协议对象

![](http://7xigvj.com1.z0.glb.clouddn.com/15211929181966.jpg)
 
### CanalClient Kafka 实现类

![](http://7xigvj.com1.z0.glb.clouddn.com/15211924985710.jpg)

## FAQ




