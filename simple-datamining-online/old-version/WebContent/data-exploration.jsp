<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="weka.core.Attribute,weka.core.Instance"%>
<%@ page
	import="weka.core.Instances,weka.core.converters.ConverterUtils"%>
<%@ page import="dm.dao.*,dm.weka.core.*"%>
<%@ page import="java.util.*"%>
<%@ taglib prefix='c' uri="http://java.sun.com/jstl/core_rt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="css/main.css" />
<title>数据探索</title>
</head>
<body>
	<div id="navigation-bar">
		<ul>
			<li><a href="data-selection.jsp">数据选择</a></li>
			<li><a href="data-exploration.jsp" style="background-color: #cc0000">数据探索</a></li>
			<li><a href="model-evaluation-selection.jsp">模型与验证</a></li>
			<li><a href="model-evaluation.jsp">性能度量</a></li>
		</ul>
	</div>
	<div id="data-info">
		<c:choose>
			<c:when test="${ sessionScope.DATA != null }">
				<h3 style="color:blue">当前使用的数据集: ${ sessionScope.DATA.getDataName() }</h3>
			</c:when>
			<c:otherwise>
				<h3 style="color:red">未绑定数据集,&nbsp;请先绑定数据集</h3>
			</c:otherwise>
		</c:choose>
	</div>
	<div id="data-exploration">
	<h1>2. 数据探索</h1>
	<%
		//DataSet iris = Factory.getDataSetDao().getDataSet("iris");
		//session.setAttribute("data", iris);

		//获得数据文件信息
		DataSet data = (DataSet) session.getAttribute("DATA");
		if (data != null) {
			String dataPath = data.getDataPath();
			System.out.println("加载的数据路径是" + dataPath);
			DataExploration explor = new DataExploration(dataPath);
			
			session.setAttribute("Instances", explor.getInstances());
			
			String[] attNames = explor.getAttributeNames();
			List<String[]> dataValue = explor.getValue();
			Map<String, String> dataInfo = explor.getDataInfo();
			Map<String, Map<String, String>> dataDescribe = explor.getDataDescribe();

			request.setAttribute("attNames", attNames);
			request.setAttribute("dataValue", dataValue);
			request.setAttribute("dataInfo", dataInfo);
			request.setAttribute("dataDescribe", dataDescribe);
		} else {
			request.setAttribute("msg", "还没有选择数据,无法显示数据信息");
		}
	%>
	<c:choose>
		<c:when test="${ msg!=null }">
			<h3 style="color: red">${ msg }</h3>
		</c:when>
		<c:otherwise>
			<!-- ------------  main content  ----------------- -->

			<h3>${ DATA.getDataName() }数据集</h3>
			<table id="table-1" border="1">
				<thead>
					<th>数据集名称</th>
					<th>特征数</th>
					<th>样本数</th>
				</thead>
				<tbody>
					<td>${ dataInfo.get("dataSetName") }</td>
					<td>${ dataInfo.get("numAttributes") }</td>
					<td>${ dataInfo.get("numInstances") }</td>
				</tbody>
			</table>
			<h3>数据集特征信息</h3>
			<table id="table-2" border="1">
				<tr>
					<th>特征</th>
					<th>Type</th>
					<th>Missing</th>
					<th>Count</th>
					<th>Mean</th>
					<th>Std</th>
					<th>Min</th>
					<th>Max</th>
				</tr>
				<c:forEach items="${ attNames }" var="att">
					<tr>
						<td>${ dataDescribe[att]["name"] }</td>
						<td>${ dataDescribe[att]["Type"] }</td>
						<td>${ dataDescribe[att]["Missing"] }</td>
						<td>${ dataDescribe[att]["Count"] }</td>
						<td>${ dataDescribe[att]["Mean"] }</td>
						<td>${ dataDescribe[att]["Std"] }</td>
						<td>${ dataDescribe[att]["Min"] }</td>
						<td>${ dataDescribe[att]["Max"] }</td>
					</tr>	
				</c:forEach>
			</table>
			<h3>数据集取值</h3>
			<table id="table-3" border="1">
				<thead>
					<th>序号</th>
					<c:forEach items="${ attNames }" var="att">
						<th>${ att }</th>
					</c:forEach>
				</thead>
				<tbody>
					<c:forEach items="${ dataValue }" var="elem" varStatus="forEach">
						<tr>
							<th>${ forEach.count }</th>
							<c:forEach items="${ elem }" var="value">
								<td>${ value }</td>
							</c:forEach>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<!-- ------------  main content  ----------------- -->
		</c:otherwise>
	</c:choose>
	</div>
</body>
</html>