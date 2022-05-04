<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>   
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>  
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css">

<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.6.3/css/all.css" 
 integrity="sha384-UHRtZLI+pbxtHCWp1t77Bi1L4ZtiqrqD80Kn4Z8NTSRyMA2Fd33n5dQ8lWUE00s/" 
 crossorigin="anonymous">

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script>
<title>Insert title here</title>
</head>

<body>

<div class="container">
		<div class="jumbotron">
			<h2>알바 Review</h2>
			<p>* 별점 및 리뷰 작성 후 작성하기 버튼을 클릭해 주세요.</p>
		</div>
	
	<div class="dropdown">
			<button type="button" class="btn dropdown-toggle" data-toggle="dropdown">게시판 선택</button>
			<ul class="dropdown-menu" aria-labelledby="dropdownMenuButton1">
				<li><a class="dropdown-item" href="spj_FreeboardList">자유 게시판</a></li>
				<li><a class="dropdown-item" href="spj_qnaboardList">질문 게시판</a></li>
				<li><a class="dropdown-item" href="spj_reviewboardList">알바 리뷰 게시판</a></li>
			</ul>
	</div>
	<div class="board_title">
		<table class="table">
			<thead>
				<tr>
					<th>#</th>
					<th>ID</th>
					<th>Title</th>
					<th>회사명</th>
					<th>별점</th>
					<th>작성일</th>
				</tr>
			</thead>

			<tbody>
				
				<c:forEach var="ReDto" items="${rlistContent}">
				
				<tr class="info">
					<td class="num" id="review_num">${ReDto.review_num}</td>
					<td>${ReDto.user_id}</td>
					<td><a data-toggle="modal" data-target="#myModal2" onclick="review_read(${ReDto.review_num})">${ReDto.review_title}</a></td>
					<td>${ReDto.review_goods_name}</td>
					
					<%-- 알바여부 1 : 알바O  0 : 알바X --%>
					<c:choose>
						
						<%-- if(a == 1){ --%>
						<c:when test="${ReDto.review_buy_opt == 1}">
							<td>O</td>
						</c:when>
						<%-- } else if(a == 0){ --%>
						<c:when test="${ReDto.review_buy_opt == 0}">
							<td>X</td>
						</c:when>
						
					</c:choose>
					
					<%-- 별점 --%>
					<c:choose>
						
						<%-- if(a == 1){ --%>
						<c:when test="${ReDto.review_star == 1}">
							<td>★☆☆☆☆</td>
						</c:when>
						<%-- if(a == 2){ --%>
						<c:when test="${ReDto.review_star == 2}">
							<td>★★☆☆☆</td>
						</c:when>
						<%-- if(a == 3){ --%>
						<c:when test="${ReDto.review_star == 3}">
							<td>★★★☆☆</td>
						</c:when>
						<%-- if(a == 4){ --%>
						<c:when test="${ReDto.review_star == 4}">
							<td>★★★★☆</td>
						</c:when>
						<%-- if(a == 5){ --%>
						<c:when test="${ReDto.review_star == 5}">
							<td>★★★★★</td>
						</c:when>
						
						
					</c:choose>
					
					<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${ReDto.review_regdate}"/></td>
				</tr>
				
				</c:forEach>
			
			</tbody>
		</table>
	</div>
		<div class="row">
			<div class="col-sm-6">
			</div>
			<div class="col-sm-2"></div>
			<div class="col-sm-4 text-black" style="text-align: right;"> 
			<button type="button" class="btn btn-black btn-lg" data-toggle="modal" data-target="#myModal">Write</button>
			</div>
		</div>
	</div>
</body>
</html>