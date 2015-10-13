# storm集群监控系统
## README
该项目是针对storm集群的监控系统，通过storm的api收集storm的cluster，supervisor，topology，spout，bolt等信息，通过动态图表的形式展示出来。前端显示使用Grafana-2.1.3，本地数据存储使用InfluxDB-0.9.3。
## 项目结构
该项目目前有三个模块，web模块，service模块，processor模块。
web模块是与前端做交互使用。service模块是核心处理模块。processor模块是从storm集群中load数据，并存储到本地influxDB中。
## 项目部署
* cmd进入项目主目录，使用maven命令:mvn package打包
## 项目运行
* 将war包导入到服务器软件
* 输入用户名密码：stormwatcher，可对系统运行参数进行修改
* 浏览器输入http://10.77.108.126:3000/login，用户名和密码admin
* 注：如果仅想进入监控页面，可以直接操作上面的url，不需要运行该项目
		