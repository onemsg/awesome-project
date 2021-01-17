<%@page import="dm.dao.Factory"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="dm.dao.*,java.util.*"%>
<%@ taglib prefix='c' uri="http://java.sun.com/jstl/core_rt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>数据选择</title>
<link rel="stylesheet" type="text/css" href="css/main.css" />
<style type="text/css">

</style>
</head>
<body>
	<div id="navigation-bar">
		<ul>
			<li><a href="data-selection.jsp" style="background-color: #cc0000">数据选择</a></li>
			<li><a href="data-exploration.jsp">数据探索</a></li>
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
	<div id="data-selection">
		<%
			List<DataSet> dataList = Factory.getDataSetDao().getAllDataSet();
			pageContext.setAttribute("dataList", dataList);
		%>
		<h1>1. 数据选择</h1>
		<h3 style="color: #C0C0C0">选择上传自定义数据集</h3>
		<form id='upload-file' action='DataSelectServlet' method="post"
			enctype="multipart/form-data">
			<input type='file' name='upload-data' required /> <input
				type='submit' value='上传' />
		</form>
		<h3 style="color: #C0C0C0">选择提供的数据集</h3>
		<table border="1">
			<thead>
				<th>序号</th>
				<th>数据集</th>
				<th></th>
			</thead>
			<tbody>
				<c:forEach items="${ dataList }" var="elem" varStatus="forEach">
					<tr>
						<td><c:out value='${ forEach.count }' /></td>
						<td>${ elem.getDataName() }</td>
						<td><a href='DataSelectServlet?id=${ elem.getId() }'>选择</a></td>						
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</body>
</html>