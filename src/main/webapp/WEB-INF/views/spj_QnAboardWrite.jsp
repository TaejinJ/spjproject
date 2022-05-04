<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta id="_csrf" name="_csrf" content="${_csrf.token}"/>
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css">

<link rel="stylesheet"
	href="https://use.fontawesome.com/releases/v5.6.3/css/all.css"
	integrity="sha384-UHRtZLI+pbxtHCWp1t77Bi1L4ZtiqrqD80Kn4Z8NTSRyMA2Fd33n5dQ8lWUE00s/"
	crossorigin="anonymous">

<!--
<script src="https://cdn.jsdelivr.net/npm/jquery@3.5.1/dist/jquery.slim.min.js"></script>
-->
<!--  
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
-->
<script
	src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script>
<title>main</title>
<style>
* {
	margin: 0;
	padding: 0;
}

ul, li {
	list-style: none;
}

a {
	text-decoration: none;
	color: inherit;
}

.board-table {
	width: 70%;
	height: 90%;
	margin: 0 auto;
	display: flex;
	flex-direction: column;
	font-size: 16px;
}

.board_title {
	margin-bottom: 30px;
}

.board_title strong {
	font-size: 32px;
}

.board_title p {
	margin-top: 5px;
	font-size: 16px;
}

.board_list {
	width: 100%;
	border-top: 2px solid #000;
}

.board_list>div {
	border-bottom: 1px solid #ddd;
	font-size: 0;
}

.board_list>div.top {
	border-bottom: 1px solid #999;
}

.board_list>div:last-child {
	border-bottom: 1px solid #000;
}

.board_list>div>div {
	display: inline-block;
	padding: 15px 0;
	text-align: center;
	font-size: 16px;
}

.board_list>div.top>div {
	font-weight: 600;
}

.board_list .num {
	width: 10%;
}

.board_list .title {
	width: 60%;
	text-align: left;
}

.board_list .top .title {
	text-align: center;
}

.board_list .writer {
	width: 10%;
}

.board_list .date {
	width: 10%;
}

.board_list .count {
	width: 10%;
}

.board_page {
	margin-top: 30px;
	text-align: center;
	font-size: 0;
}

.board_page a {
	display: inline-block;
	width: 32px;
	height: 32px;
	box-sizing: border-box;
	vertical-align: middle;
	border: 1px solid #ddd;
	border-left: 0;
	line-height: 100%;
}

.board_page a.bt {
	padding-top: 10px;
	font-size: 16px;
	letter-spacing: -1px;
}

.board_page a.num {
	padding-top: 9px;
	font-size: 16px;
}

.board_page a.num.on {
	border-color: #000;
	background: #000;
	color: #fff;
}

.board_page a:first-child {
	border-left: 1px solid #ddd;
}

.board_view {
	width: 100%;
	border-top: 2px solid #000;
}

.board_view .title {
	padding: 20px 15px;
	border-bottom: 1px dashed #ddd;
	font-size: 16px;
}

.board_view .info {
	padding: 15px;
	border-bottom: 1px solid #999;
	font-size: 0;
}

.board_view .info dl {
	position: relative;
	display: inline-block;
	padding: 0 20px;
}

.board_view .info dl:first-child {
	padding-left: 0;
}

.board_view .info dl::before {
	content: "";
	position: absolute;
	top: 1px;
	left: 0;
	display: block;
	width: 1px;
	height: 13px;
	background: #ddd;
}

.board_view .info dl:first-child::before {
	display: none;
}

.board_view .info dl dt, .board_view .info dl dd {
	display: inline-block;
	font-size: 16px;
}

.board_view .info dl dt {
	
}

.board_view .info dl dd {
	margin-left: 10px;
	color: #777;
}

.board_view .cont {
	padding: 15px;
	border-bottom: 1px solid #000;
	line-height: 160%;
	font-size: 16px;
}

.board_qwrite {
	border-top: 2px solid #000;
}

.board_qwrite .title, .board_qwrite .info {
	padding: 10px;
}

.board_qwrite .info {
	border-top: 1px dashed #ddd;
	border-bottom: 1px solid #000;
	font-size: 0;
}

.board_qwrite .title dl {
	font-size: 0;
}

.board_qwrite .info dl {
	display: inline-block;
	width: 50%;
	vertical-align: middle;
}

.board_qwrite .title dt, .board_qwrite .title dd, .board_qwrite .info dt,
	.board_qwrite .info dd {
	display: inline-block;
	vertical-align: middle;
	font-size: 16px;
}

.board_qwrite .title dt, .board_qwrite .info dt {
	width: 100px;
}

.board_qwrite .title dd {
	width: calc(100% - 100px);
}

.board_qwrite .title input[type="text"], .board_qwrite .info input[type="text"],
	.board_qwrite .info input[type="password"] {
	padding: 10px;
	box-sizing: border-box;
}

.board_qwrite .title input[type="text"] {
	width: 77%;
}

.board_qwrite .cont {
	border-bottom: 1px solid #000;
}

.board_qwrite .cont textarea {
	display: block;
	width: 100%;
	height: 300px;
	padding: 15px;
	box-sizing: border-box;
	border: 0;
	resize: vertical;
}


.bt_wrap {
    margin-top: 30px;
    text-align: center;
    font-size: 0;
}

.bt_wrap a {
    display: inline-block;
    min-width: 80px;
    margin-left: 10px;
    padding: 10px;
    border: 1px solid #000;
    font-size: 16px;
}

.bt_wrap a:first-child {
    margin-left: 0;
}

.bt_wrap a.on {
    background: #000;
    color: #fff;
}

button {
	display: inline-block;
    min-width: 80px;
    min-height: 46px;
    margin-left: 10px;
    padding: 10px;
    border: 1px solid #000;
    font-size: 16px;
}

</style>
</head>
<body>
	

	<%@ include file="spj_nav.jsp"%>

	<div class="board-table mt-5">
		<div class="board_title">
			<strong>게시글 작성</strong>
		</div>

	<form action="qwrite" method="post">
	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
		<div class="board_qwrite">
			<div class="title">
				<dl>
					<dt>제목</dt>
					<dd>
						<input type="text" class="form-control" id="uboardType" placeholder="Title" 
						name="qTitle" required value="${qTitle}" required>
					</dd>
				</dl>
			</div>
			<div class="info">
				<dl>
					<dt>게시판</dt>
					<dd>
						<input type="text" class="form-control" id="uboardType" placeholder="Board type" 
						name="bboardType" required value="${bBoardType}" readonly>
					</dd>
				</dl>
				<dl>
					<dt>작성자</dt>
					<dd>
						<input type="text" class="form-control" id="uname" placeholder="Enter email" 
						name="qNic" required value="${qNic}" readonly>	
					</dd>
				</dl>
			</div>
			<div class="cont">
				<textarea placeholder="내용 입력"></textarea>
			</div>
		</div>

		<div class="bt_wrap">
			<button type="submit" class="btn btn-dark">등록</button> 
			<a href="spj_qnaboardList" class="btn btn-dark">취소</a>
		</div>
	</form>	
	</div>

	<%@ include file="spj_footer.jsp"%>

</body>
</html>