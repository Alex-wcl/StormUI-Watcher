#storm集群监控系统#
##README##
	<P>该项目是针对storm集群的监控系统，通过storm的api收集storm的cluster，supervisor，topology，spout，bolt等信息，通过动态图表的形式展示出来。前端显示使用Grafana-2.1.3，本地数据存储使用InfluxDB-0.9.3。</P>
##项目结构##
	<P>该项目目前有三个模块，web模块，service模块，processor模块。</P>
	<P>web模块是与前端做交互使用。</P>
	<P>service模块是核心处理模块。</P>
	<P>processor模块是从storm集群中load数据，并存储到本地influxDB中。</P>
	
##项目部署##
	<P>
	将项目导出之后，需要在web模块上添加Server Runtime。右击stormUI-web-->Build Path-->Configuer Build Path-->Libraries-->Add library-->Server Runtime,选择当前使用的server。
	</P>
##项目运行##
	<P>运行stormUI-web项目即可。</P>
	<P>右击stormUI-web-->Run As-->Run on Server。</P>
		