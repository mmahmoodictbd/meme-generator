<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Meme generator</title>
</head>

<body>

	<c:choose>
		<c:when test="${not param.tmp or empty b64}">
			<img style="width: 50%; height: 50%" src="/image/${imgName}">
		</c:when>
		<c:otherwise>
			<img style="width: 50%; height: 50%"
				src="data:image/jpg;base64, ${b64}">
		</c:otherwise>
	</c:choose>

	<form action="writeTextOnImage" method="POST"
		style="float: right; padding-right: 100px;">
		<br> Top Text 1: <input type="text" name="toptext1"><br>
		Top Text 2: <input type="text" name="toptext2"><br> <br>
		Bottom Text 1: <input type="text" name="bottomtext1"><br>Bottom
		Text 2: <input type="text" name="bottomtext2"><br> <br>
		<br> Font: <input type="text" name="font" value="Impact">
		<br>Font Size: <input type="text" name="fontSize" value="30">
		<br> <input type="hidden" name="imgname" value="${imgName}">
		<br> <input type="submit" value="Submit">
	</form>

</body>
</html>