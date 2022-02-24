<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ taglib prefix='c' uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
	body {
		text-align: center;
	}
</style>
</head>
<body>
	<%
		String s = "Python\tJava\tJavaScript\tGo\n";
		s += "坚持住自己的底线，自律才是真正的自由\n";
		s += "每一点进步，都是以后大事业不可或缺的一砖一瓦";
		session.setAttribute("s", s);
		System.out.println(s);
	%>
	<% pageContext.setAttribute("newLineChar", "|||"); %>

	<h2>${fn:replace(s, newLineChar, "\\n")}</h2>
	<h3><%= s %></h3>
	<textarea rows="100" cols="100">
	 <%= s %>
	 
	</textarea> 
</body>
</html>