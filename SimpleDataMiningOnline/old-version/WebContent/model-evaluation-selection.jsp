<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix='c' uri="http://java.sun.com/jstl/core_rt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>模型与验证方法选择</title>
<link rel="stylesheet" type="text/css" href="css/main.css" />
</head>
<body>
	<div id="navigation-bar">
		<ul>
			<li><a href="data-selection.jsp">数据选择</a></li>
			<li><a href="data-exploration.jsp">数据探索</a></li>
			<li><a href="model-evaluation-selection.jsp" style="background-color: #cc0000">模型与验证</a></li>
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
	
<div id="model-Selection">
	<h1>3. 模型选择</h1>
	<table border="0">
		<tr><th>
		<select name="modelName" form="form-1">
			<option value="LinearRegression">多元线性回归</option>
			<option value="Logistic">洛吉斯蒂回归</option>
			<option value="KNN" selected>K近邻</option>
			<option value="NaiveBayes">朴素贝叶斯</option>
			<option value="C45">C4.5</option>
			<option value="RandomForest">随机森林</option>
			<option value="SVM">支持向量机</option>
		</select>
		</th></tr>
		<tr><th>模型参数<input type="text" name="options" form="form-1"/></th></tr>
	</table>
</div>
<div id="evaluation-Selection">
	<h1>4. 测试方法选择</h1>
	<form action="ModelSelectEvaluateServlet" method="post" id="form-1">
	<table border="0">
		<tr>
			<td align="left"><input type="radio" name="evaluate-mothod" value="use-training-test" />
			使用测试机验证</td>
			<td align="left">&nbsp;</td>
		</tr>		
		<tr>
			<td align="left"><input type="radio" name="evaluate-mothod" value="cross-validation" checked/>交叉验证</td>
			<td align="right">K-Folds<input type="number" name="k-folds" value=10 min=1 max=100 checked /></td>
		</tr>
		<tr>
			<td align="left"><input type="radio" name="evaluate-mothod" value="split-validation" />
			分割数据集验证</td>
			<td align="right">%<input type="number" name="split-ratio" value=75 min=1 max=99  checked /></td>
		</tr>				
	</table>
	<h3><input type="submit" value="确定" class="button"/></h3>
	</form>
</div>		
</div>
</body>
</html>