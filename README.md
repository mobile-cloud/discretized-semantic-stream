# 基于Spark的物联网语义流数据处理的研究与实现
## ——源代码使用说明
标签： onem2m
---
[TOC]
## 1. 运行环境

### 硬件环境
| 主机名 | CPU | 内存 | 磁盘 | 网络地址 |
| -------  | ----:  | ----:  | -----:  | ---: |
| HDN1 | Intel(R) Xeon(R) E5606@ 2.13GHz | 16GB | 1TB | 192.168.13.200  |
| HDN2 | Intel(R) Xeon(R) E5606@ 2.13GHz | 16GB | 2TB | 192.168.13.201  |
| HDN3 | Intel(R) Xeon(R) E5606@ 2.13GHz | 16GB | 1.8TB | 192.168.13.202  |
这只是集群的某种可能配置之一，单台一般配置的PC机也可以搭建Spark集群。
### 软件环境
| 操作系统 | JDK | Scala | Spark |
| :---- | :--- | :---- | :---- |
| CentOS 6.4 x64 | JDK1.7.0_25 | Scala-2.10.1 | Spark-0.9.0 |
其中，JDK、Scala、Spark需要配置好环境变量。
## 2. Sensor
该目录下是语义流模拟生成程序的服务端源代码，将该工程导入Eclipse后，通过Eclipse的fatjar插件将该工程打包成名字为rdfserver.jar的包后通过命令行运行，命令行运行方式如下：
``` 
$ java -jar rdfserver.jar <each_num> <listening_port>
```
例如：
```
$ java -jar rdfserver.jar 10 30001
```
参数含义：
`<each_num>`  模拟每类房间的个数
`<listening_port>`  服务端监听的端口号
在运行Spark Streaming程序之前，需要提前将该服务端程序启动。
## 3. stream-processing
该目录下是用于语义流处理的stream-processing程序。
### 3.1 单机运行
单机运行主要是为了方便代码的编写和调试，可以直接在Eclipse环境下运行，运行前需要启动RDFServer服务端。
运行前，需要设置Eclipse运行的命令行参数如下：
#### 运行AQI推理
```sh
com.stream.reasoning.AQIProcessing <master> <hostname> <port> <room_uri>
```
参数含义：
`<master>`  master节点位置，单机运行时设置为“local[2]”
`<hostname>`  RDFServer的IP地址
`<port>`  RDFServer的监听端口
`<room_uri>`  目标房间的URI
#### 运行人体舒适度指数推理
```sh
com.stream.reasoning.BodyComfortProcessing <master> <hostname> <port> <room_uri>
```
`<master>`  master节点位置，单机运行时设置为“local[2]”
`<hostname>`  RDFServer的IP地址
`<port>`  RDFServer的监听端口
`<room_uri>`  目标房间的URI
#### 运行SPARQL查询
```sh
com.stream.reasoning.QueryBySPARQL <master> <hostname> <port> <sparql_ip> <sparql_port>
```
`<master>`  master节点位置，单机运行时设置为“local[2]”
`<hostname>`  RDFServer的IP地址
`<port>`  RDFServer的监听端口
`<sparql_ip>`  SPARQL语句导入程序的IP
`<sparql_port>`  SPARQL语句导入程序的端口号
#### 运行窗口查询
```sh
com.stream.reasoning.QueryWindow <master> <hostname> <port> <sparql_ip> <sparql_port>
```
`<master>`  master节点位置，单机运行时设置为“local[2]”
`<hostname>`  RDFServer的IP地址
`<port>`  RDFServer的监听端口
`<sparql_ip>`  SPARQL语句导入程序的IP
`<sparql_port>`  SPARQL语句导入程序的端口号
### 3.2 集群运行
#### 3.2.1 启动Spark集群
先用jps命令查看Master和Worker进程是否存在
若不存在，先执行
```
$SPARK_HOME/sbin/start-all.sh
```
#### 3.2.2 运行App
集群运行主要是为了进行语义流处理的吞吐量实验。将stream-processing工程打包后生成stream-processing.jar包，放在discretized-semantic-stream根目录下。打开stream-processing.jar，删除/META-INF下除maven, *.RSA, *.MF以外的所有文件，然后通过两个脚本运行。
##### **运行AQI推理**
```
$ ./run-aqi stream-processing.jar com.stream.reasoning.AQIProcessing
```
##### **运行人体舒适度指数推理**
```
$ ./run-aqi stream-processing.jar com.stream.reasoning.BodyComfortProcessing
```
##### **运行语义流查询**
先启动sparql-client
```
$ java -jar sparql-client.jar 30002
```
在该终端中输入SPARQL查询语句，例如
```
PREFIX p:<http://hem.org/predicate#>
SELECT ?value ?time WHERE {
<http://hem.org/room#bedroom_1> p:hasSensor ?sensor .
?sensor p:hasValue ?value .
?sensor p:valueType 'temp' .
?sensor p:samplingTime ?time . }
```
然后在另一终端里执行
```
$ ./run-sparql stream-processing.jar com.stream.reasoning.QueryBySPARQL
```
```
$ ./run-sparql stream-processing.jar com.stream.reasoning.QueryWindow
```
## 4. SparklogsAnalyzer
该工程下是对Spark运行日志进行分析的程序，运行后会生成包含分析结果的.xls文件。
命令行运行方式：
```
$ SparklogsAnalyzer <dir> <file_name>
```
参数含义：
`<dir>` 日志所在目录
`<file_name>` 日志文件文件名
