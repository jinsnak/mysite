<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%> 
<%@page import="java.util.List"%>
<%@page import="kr.co.dhflour.mysite.vo.GuestbookVo"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
	pageContext.setAttribute("newLine", "\n");
%>

<!doctype html>
<html>
<head>
<title>mysite</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link href="/mysite/assets/css/guestbook.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div id="container">
		<c:import url="/WEB-INF/views/include/header.jsp" />
		<div id="content">
			<div id="guestbook">
				<form action="/mysite/guestbook?a=add" method="post">
					<input type="hidden" name="a" value="insert">
					<table>
						<tr>
							<td>이름</td><td><input type="text" name="name"></td>
							<td>비밀번호</td><td><input type="password" name="password"></td>
						</tr>
						<tr>
							<td colspan=4><textarea name="contents" id="content"></textarea></td>
						</tr>
						<tr>
							<td colspan=4 align=right><input type="submit" VALUE=" 확인 "></td>
						</tr>
					</table>
				</form>
				<br>
				
				<c:set var="count" value="${fn:length(list) }"></c:set>
				
				<c:forEach var="vo" items="${list }" varStatus="status">
					<table width="510" border="1">
						<tr>
							<td>[${ count - status.index }]</td>
							<td>${vo.name }</td>
							<td>${vo.regDate }</td>
							<td><a href="/mysite/guestbook?a=deleteform&no=${vo.no }">삭제</a></td>
						</tr>
						<tr>
							<td colspan="4"> ${ fn:replace(vo.contents, newLine, "<br>")} </td>
						</tr>
						
					</table>
					<br>
				</c:forEach>
				
			</div>
		</div>
		<c:import url="/WEB-INF/views/include/navigation.jsp" />
		<c:import url="/WEB-INF/views/include/footer.jsp" />
	</div>
</body>
</html>