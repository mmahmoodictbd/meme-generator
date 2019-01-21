<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Meme generator</title>
<style type="text/css">
#grid[data-columns]::before {
	content: '6 .column.size-1of6';
}
/* These are the classes that are going to be applied: */
.column {
	float: left;
}

.size-1of3 {
	width: 33.333%;
}

.size-1of4 {
	width: 25%;
}

.size-1of6 {
	width: 16.6%;
}
</style>

<link href="css/jquery.nailthumb.1.1.min.css" type="text/css"
  rel="stylesheet" />

</head>

<body>

  <c:if test="${not empty messages}">
    <br>
    <c:forEach var="msg" items="${messages}">
      <p>${msg}</p>
    </c:forEach>
  </c:if>
  <br>

  <form method="POST" enctype="multipart/form-data" action="/upload">
    File to upload: <input type="file" name="file"></input> <input
      type="submit" value="Upload"></input>
  </form>

  <br>

  <div id="grid" data-columns="6">
    <c:forEach var="fileName" items="${fileNames}" varStatus="status">
      <div class="nailthumb-container" data-n="${status.index}">
        <a href="/showImage?imageFileName=${fileName}" target="_blank">
          <img src="/image/${fileName}">
        </a>
      </div>
    </c:forEach>
  </div>

  <script src="//code.jquery.com/jquery-1.11.3.min.js"></script>
  <script src="js/salvattore.min.js"></script>
  <script src="js/jquery.nailthumb.1.1.min.js"></script>
  <script type="text/javascript">
      jQuery(document).ready(function() {
        jQuery('.nailthumb-container').nailthumb({
          width: 180,
          height: 150,
          fitDirection: 'center center'
        });
      });
    </script>
</body>
</html>