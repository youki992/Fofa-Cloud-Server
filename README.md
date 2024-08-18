<h1 align="center">
  <b>Fofa-Cloud-Server</b>
  <br>
</h1>

![image](https://ice.frostsky.com/2024/08/18/ba6436748b8cf2014bd5f6d43d86a821.jpeg)

<p align="center">
<a href="https://github.com/youki992/VscanPlus/issues"><img src="https://img.shields.io/badge/contributions-welcome-brightgreen.svg?style=flat"></a>
<a href="https://github.com/youki992/VscanPlus"><img alt="Release" src="https://img.shields.io/badge/LICENSE-BSD-important"></a>
<a href="https://github.com/youki992/VscanPlus/releases"><img src="https://img.shields.io/github/release/youki992/VscanPlus"></a>
<a href="https://github.com/youki992/VscanPlus/releases"><img src="https://img.shields.io/github/downloads/youki992/VscanPlus/total?color=blueviolet"></a>
</p>

# 使用说明
基于单FOFA账号部署的多人协作查询B/S模式，后端部署后通过前端网页交互查询数据和导出数据

## 一、配置settings.ini文件
在当前目录下配置settings.ini文件中的fofa.email、fofa.key以及secretKey
```
fofa.email=xxxx
fofa.key=xxxx
secretKey=code4thsec
```
其中fofa.email、fofa.key从FOFA个人页中获取
secretKey配置任意字符即可（用于接口密钥ak验证，防止外部人员调用查询接口）

## 二、启动jar运行文件
在jar运行文件目录下配置好settings.ini后，使用如下命令运行
```
java -jar fofamap-server-0.0.1-SNAPSHOT.jar
```
![image](https://ice.frostsky.com/2024/08/18/068ab46361225c3ca86b2b96f9cb789f.png)
后端服务运行在本地**http://127.0.0.1:83**

运行后会在目录下生成ak.txt，共生成30个用于接口调用传参的接口密钥ak，示例：
![image](https://ice.frostsky.com/2024/08/18/d07950e6d7772abc19754f29fc3058b0.png)

## 三、访问前端页面查询
访问index.html文件（发布版本index.html文件、css文件和js文件都在html.zip压缩包中）
输入上一步生成的任意一个ak值用于验证身份，查询参数对应如下
```
查询字符串 - qbase64
页码 - page
每页显示数量 - size
字段列表 - fileds
```
![image](https://ice.frostsky.com/2024/08/18/81165d00768fe6dca7a22cddefc8ad8d.png)

