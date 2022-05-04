<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta id="_csrf" name="_csrf" content="${_csrf.token}" />
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css">

<link rel="stylesheet"
	href="https://use.fontawesome.com/releases/v5.6.3/css/all.css"
	integrity="sha384-UHRtZLI+pbxtHCWp1t77Bi1L4ZtiqrqD80Kn4Z8NTSRyMA2Fd33n5dQ8lWUE00s/"
	crossorigin="anonymous">

<!--
<script src="https://cdn.jsdelivr.net/npm/jquery@3.5.1/dist/jquery.slim.min.js"></script>
-->
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script>
<style>
* {
	visibility:hidden;
}

html {
	position: relative;
	min-height: 100%;
	margin: 0;
}

body {
	min-height: 100%;
}

h1 {
	font-weight: 900;
}

.social-logo {
	width: 26px;
	height: 26px;
}

span {
	font-size: 14px;
	text-aling: center;
}

.login-text1 {
	font-weight: bolder;
	font-size: 26px;
}

form {
	width: 60%;
	margin: 0 auto;
}
</style>

</head>
<body>
	<title>logout</title>

</head>
<body>
	
	<div id="main" class="container mt-5">
		<h3 class="text-center text-info">로그아웃</h3>
		<!-- security는 다음 form을 이용해야만 하고 action="logout"은 security시 처리 경로로 controller
	     사용 경로가 아님  -->
		<form id="frm1" name="frm1" method="post" action="logout">
			<!-- csrf를 방지하기 위한 헤더 부분 추가 -->
			<input type="hidden" name="${_csrf.parameterName}"
				value="${_csrf.token}" /> <br />
			<button type="submit" class="btn btn-success" id="logout-btn">로그아웃</button>
		</form>
	</div>

	<div class="mb-5"></div>


</body>
</html>