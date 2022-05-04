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

<!-- 페이지 처리 js라이브러리 -->
<script src="js/jquery.twbsPagination.js"></script>
<title>freeboard</title>
<style>
html {
    position: relative;
    min-height: 100%;
    margin: 0;
}

body {
    min-height: 100%;
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
	height : 40px;
}

.board_list .date {
	width: 10%;
}

.board_list .count {
	width: 10%;
}

.board-btn {
	justify-content: right;
	width: 140px;
	margin-bottom: 10px;
}

.pagination a {
	color: black;
	border: 0px;
	padding: 8px 16px;
	text-decoration: none;
}

.pagination {
	margin-top: 10px;
	justify-content: center;
}

form {
	width: 80%;
	margin: 0 auto;
}

.search {
	text-align: center;
	margin-top: 30px;
}

.bt_wrap {
	text-align: right;
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

.btn a {
	display: inline-block;
	min-width: 80px;
	margin-left: 10px;
	padding: 10px;
	border: 1px solid #000;
	font-size: 16px;
}
</style>


</head>
<body>
	

	<%@ include file="spj_nav.jsp"%>
	<!-- main은 페이지 리스트 처리 -->
<div id="main" class="board-table mt-5">
	<div class="board_title">
		<strong>게시판</strong>
	</div>
		
	<div class="dropdown">
		<button type="button" class="btn dropdown-toggle" data-toggle="dropdown">게시판 선택</button>
		<ul class="dropdown-menu" aria-labelledby="dropdownMenuButton1">
			<li><a class="dropdown-item" href="spj_FreeboardList">자유 게시판</a></li>
			<li><a class="dropdown-item" href="spj_qnaboardList">질문 게시판</a></li>
			<li><a class="dropdown-item" href="spj_reviewboardList">알바 리뷰 게시판</a></li>
		</ul>
	</div>

	<span class="bt_wrap">
		<a href="spj_freeboardWrite" class="btn btn-dark mb-2">게시글 작성</a>
		<!--<a href="#">수정</a>-->
	</span>
		
	<table id="searchTable" class="board_list">
		<thead class="top">
			<tr>
				<th class="num">번호</th>
				<th class="title">제목</th>
				<th class="writer">작성자</th>
				<th class="date">작성일</th>
				<th class="count">조회</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${listContent}" var="dto">
				<tr>
					<td class="num" id="bid">${dto.bId}</td>
					<td class="title">
						<a class="spj_freeboardView" href="spj_freeboardView?bId=${dto.bId}">${dto.bTitle}</a>
					</td>
					<td class="writer">${dto.bNic}</td>
					<td class="date">${dto.bDate}</td>
					<td class="count">${dto.bHit}</td>
				</tr>
			</c:forEach>
		</tbody>			
	</table>
	<br/><br/>
	<!-- 검색창 -->
	<div class="search-wrap">	
		<form action="" > 
			<div class="input-group">
				<select name="type">
					<option value="title">제목</option>
					<option value="content">내용</option>
					<option value="writer">작성자</option>
				</select>
				<input type="text" class="form-control search-input" id="searchInput" placeholder="검색어를 입력하세요" 
					aria-label="검색어를 입력하세요." aria-describedby="basic-addon2"/>
				<button class="btn btn-outline-dark serch-btn" type="submit"
					style="width: 100px">검색</button>	
					
			</div>
		</form>
	</div>	
</div>		

<!-- 페이징 -->
<!-- #boardOnly는 페이지  -->
<div class="container" id="boardOnly">
	<nav aria-label="Page navigation"> <!-- aria-label은 라벨표시가 안되는것 예방 -->
		<ul class="pagination justify-content-center" id="pagination" style="margin:20px 0">
		</ul>
	</nav>
</div>

<%@ include file="spj_footer.jsp"%>
<script>
$(document).ready(function(){
	//글작성
	$("#write").click(function(event){
		event.preventDefault(); //ajax를 사용하기 위해 글작성이 링크 a로 된것을 막음
		//창이동 없이 board메뉴창안에 글작성창을 로딩
		$.ajax({
			url : "spj_FreeboardWrite",
			type : "get", //링크 a를 ajax시는 get방식이어야 함
			success : function(data) {
				$("#main").html(data); //data는 콘트롤라에서 보내준 jsp
				$("#boardOnly").css("display","none");	//글작성창에서는 페이지와 write버튼 필요 없음
			},
			error : function() {
				alert("write_view시 에러 발생");
			}
		});
	});
	
	//내용보기
	$(".spj_freeboardView").click(function(event){
		$.ajax({
			url : x,
			type : "get", //a의 방식은  get
			success : function(data) {
				$("#main").html(data); //data는 content_view창
				$("#boardOnly").css("display","none");	//페이지표시 엘리먼트
			},
			error : function() {
				alert("spj_freeboardView시 에러 발생");
			}
		});
	});
	//검색창
	$("#searchInput").on("keyup",function(){
		var value = $(this).val().toLowerCase(); //입력창값을 소문자로 변환
		$("#searchTable tr").filter(function(){
			$(this).toggle($(this).text().toLowerCase().indexOf(value) > -1);
			//일치하지 않는 tr은 제거를 하고 일치하는 것이 있는 tr만 남겨두는 것이 toggle메서드임
		});
	});
	
});

</script>


<script>
//pagination만들기
$(function(){
	window.pagObj = $('#pagination').twbsPagination({		
		totalPages: 35, //총 페이지 수
		visiblePages: 10, //가시 페이지 수
		onPageClick : function (event, page) {
			console.info(page + ' (from options)');
			$(".page-link").on("click",function(event){
				event.preventDefault();
				var peo = event.target; //이벤트 발생 자바스크립트 객체
				var pageNo = peo.innerHTML;	//페이지 번호
				var purl;
				if(pageNo != "First" && pageNo != "Previous" && pageNo != "Next" && pageNo != "Last"){
					purl = "plist?pageNo=" + pageNo;
				}
				else {
					return;
				}
				$.ajax({
					url : purl,
					type : "get",
					success : function(data) {
						$("#main").html(data);
					},
					error : function() {
						alert("plist시 에러 발생");
					}
				}); //.ajax
			}); //".page-link"
		} //onPageClick
	}) //window.pagObj
	.on('page', function(event, page){ //chaining
		console.info(page + ' (from event listening)');
	}); //on
}); //function()

</script>
 

</body>
</html>