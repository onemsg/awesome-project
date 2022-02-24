<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix='c' uri="http://java.sun.com/jstl/core_rt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>模型评估</title>
<link rel="stylesheet" type="text/css" href="css/main.css" />
</head>
<body>
	<div id="navigation-bar">
		<ul>
			<li><a href="data-selection.jsp">数据选择</a></li>
			<li><a href="data-exploration.jsp">数据探索</a></li>
			<li><a href="model-evaluation-selection.jsp">模型与验证</a></li>
			<li><a href="model-evaluation.jsp"
				style="background-color: #cc0000">性能度量</a></li>
		</ul>
	</div>
	<div id="data-info">
		<c:choose>
			<c:when test="${ sessionScope.DATA != null }">
				<h3 style="color: blue">当前使用的数据集: ${ sessionScope.DATA.getDataName() }</h3>
			</c:when>
			<c:otherwise>
				<h3 style="color: red">未绑定数据集,&nbsp;请先绑定数据集</h3>
			</c:otherwise>
		</c:choose>
	</div>

	<div id="model-evaluation">
		<h1>5. 性能度量</h1>
		<c:choose>
			<c:when test="${ sessionScope.error != null }">
				<p style="color: red">${ sessionScope.error }</p>
			</c:when>
		</c:choose>
		<table border="0">
			<thead>
				<th>模型名称</th>
				<th>模型参数</th>
				<th>数据集</th>
				<th>验证方法</th>
			</thead>
			<tbody>
				<tr>
					<td>${ sessionScope.modelName }&nbsp;</td>
					<td>${ sessionScope.options }&nbsp;</td>
					<td>${ sessionScope.dataName }&nbsp;</td>
					<td>${ sessionScope.evaluate_method }&nbsp;</td>
				</tr>
			</tbody>
		</table>
		<h3>性能度量信息</h3>
		<textarea rows="25" cols="110"><c:forEach items="${ sessionScope.summary }" var="item">
		${ item }</c:forEach></textarea>								
	</div>
</body>
</html>