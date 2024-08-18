<h1 align="center">
  <b>Fofa-Cloud-Server</b>
  <br>
</h1>

![image](https://ice.frostsky.com/2024/08/18/ba6436748b8cf2014bd5f6d43d86a821.jpeg)

<p align="center">
<a href="https://github.com/youki992/Fofa-Cloud-Server/issues"><img src="https://img.shields.io/badge/contributions-welcome-brightgreen.svg?style=flat"></a>
<a href="https://github.com/youki992/Fofa-Cloud-Server"><img alt="Release" src="https://img.shields.io/badge/LICENSE-BSD-important"></a>
<a href="https://github.com/youki992/Fofa-Cloud-Server/releases"><img src="https://img.shields.io/github/release/youki992/VscanPlus"></a>
<a href="https://github.com/youki992/Fofa-Cloud-Server/releases"><img src="https://img.shields.io/github/downloads/youki992/VscanPlus/total?color=blueviolet"></a>
</p>

# 使用说明
基于单FOFA账号部署的多人协作查询B/S模式，后端部署后通过前端网页交互查询数据和导出数据

根据不同情况可以部署在本地局域网中，或是部署在服务器上，使用Apache或Nginx进行配置前端页面

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
查询字符串 - qbase64 (即FOFA查询语法)
页码 - page
每页显示数量 - size
字段列表 - fileds (至少选择两个字段)
```
![image](https://ice.frostsky.com/2024/08/18/81165d00768fe6dca7a22cddefc8ad8d.png)

查询完成后，可以点击导出按钮，导出EXCEL表格
![image](https://ice.frostsky.com/2024/08/18/cd4d5b10acc19eacfb47e2537c7534c5.png)
![image](https://ice.frostsky.com/2024/08/18/01d50e9d085dfde6f69972cab0756cb0.png)

# 附录
- 已支持跨域请求

****************************

**本工具仅提供给安全测试人员进行安全自查使用**

**用户滥用造成的一切后果与作者无关**

**使用者请务必遵守当地法律**

**本程序不得用于商业用途，仅限学习交流**

*********


<p align="center">
  本工具由Code4th安全团队开发维护
  <img src="https://ice.frostsky.com/2024/08/18/5559fc7abc47065e9e5e53a7dba2142b.jpeg">
</p>

**团队公开群**

<p align="center">
  本工具由Code4th安全团队开发维护
  <img src="https://ice.frostsky.com/2024/08/18/266dd331939779e83cb1ffe84a0e7500.png" width="720" height="1280" alt="公开群">>
</p>

# 历史Star

[![Star History Chart](https://api.star-history.com/svg?repos=youki992/Fofa-Cloud-Server&type=Date)](https://star-history.com/#youki992/Fofa-Cloud-Server)
