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
<meta id="_csrf" name="_csrf" content="${_csrf.token}"/>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css">

<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.6.3/css/all.css" 
 integrity="sha384-UHRtZLI+pbxtHCWp1t77Bi1L4ZtiqrqD80Kn4Z8NTSRyMA2Fd33n5dQ8lWUE00s/" 
 crossorigin="anonymous">


<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script>

<!-- 페이지 처리 js라이브러리 -->
<script src="js/jquery.twbsPagination.js"></script>
<title>review board</title>

<%-- JavaScript review_read 이벤트 처리 --%>
<script type="text/javascript">

	//글 번호를 가지고 있을 전역변수
	var G_review_num;

	function review_read(num) {
		
		<%-- review_read 서블릿 호출 --%>
		
			G_review_num = num;
		
			//파라미터
			var param = {
					
					review_num : num
			}
			
			
			//요청
			$.ajax({
				
				url : "review_read.credu",
				dataType : "text",
				data : param,
				type : "post",
				success : function(data) {
															
					$("#content").text(data);
										
				}
						
			});
			
			
	}
	
	
	function review_delete() {
		
		<%-- review_delete 서블릿 호출 --%>
		
			//파라미터
			var param = {
					
					review_num : G_review_num
			}
			
			
			//요청
			$.ajax({
				
				url : "review_delete.credu",
				dataType : "text",
				data : param,
				type : "post",
				success : function(data) {
									
					alert("삭제 되었습니다.");
					G_review_num = 0; //전역변수 초기화
					location.href = "review.credu";
						
				}
						
			});
			
	}

</script>


</head>

<body>
	<%-- review 리스트 서블릿 호출 --%>
	<c:url var="review_list" value="review_list.credu"></c:url>
	<%-- review 작성 서블릿 호출 --%>
	<c:url var="review_write" value="review_write.credu"></c:url>
	<%-- review 삭제 서블릿 호출 --%>
	<c:url var="review_delete" value="review_delete.credu"></c:url>
	
	
	
	  
<%@ include file="spj_nav.jsp"%>

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
		<button type="button" class="btn btn-black btn-lg" data-toggle="modal" data-target="#myModal" >글쓰기</button>
		</div>
	</div>
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
	
<!-- 페이징 -->			
<!-- #boardOnly는 페이지  -->
<div class="container" id="boardOnly">
	<nav aria-label="Page navigation"> <!-- aria-label은 라벨표시가 안되는것 예방 -->
		<ul class="pagination justify-content-center" id="pagination" style="margin:20px 0">
		</ul>
	</nav>
</div>		
		
	</div>
	<%@ include file="spj_footer.jsp"%>	 
		
		 <!-- Modal -->
  	<div class="modal fade" id="myModal" role="dialog">
    	<div class="modal-dialog modal-lg">
    
      	<!-- Modal content-->
      	<div class="modal-content">
	        <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal">&times;</button>
	        </div>
        
        <div class="modal-body">
          
			<div class="panel-group">
			<div class="panel panel-success" style="margin-top: 10px;">
				<div class="panel-heading">Goods Review</div>
				<div class="panel-body">
					<%-- form --%>
					<form class="form-horizontal" role="form" action="${review_write}" method="post">
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
						<div class="form-group">
							<label class="control-label col-sm-2">작성자(ID):</label>
							<div class="col-sm-10">
								<input type="text" class="form-control" id="user_id"
									name="user_id" placeholder="ID">	<!-- 아이디 불러오기 적용하기 -->
							</div>
						</div>
						<div class="form-group">
							<label class="control-label col-sm-2" for="pwd">제목:</label>
							<div class="col-sm-10">
								<input type="text" class="form-control" id="review_title"
									name="review_title" placeholder="Title">
							</div>
						</div>
						<div class="form-group">
							<label class="control-label col-sm-2" for="pwd">회사명:</label>
							<div class="col-sm-10">
								<input type="text" class="form-control" id="review_goods_name"
									name="review_goods_name" placeholder="회사명">
							</div>
						</div>
						<div class="form-group">
							<label class="control-label col-sm-2" for="pwd">내용:</label>
							<div class="col-sm-10">
								<textarea class="form-control" rows="5"	placeholder="내용을 작성하세요" name="review_content" id="review_content"></textarea>
							</div>
						</div>
						<div class="form-group">
							<div class="col-sm-offset-2 col-sm-10">
								<div class="checkbox">
									<label><input type="checkbox" value="1"
										id="review_buy_opt" name="review_buy_opt" checked="checked">알바여부</label>	<!-- 알바여부 적용하기 -->
								</div>
							</div>
						</div>
						<div class="form-group">
							<div class="col-sm-offset-2 col-sm-10">
								<div class="radio">
									<!-- 별점버튼 적용안됨 X로 표시됨 -->
									<label class="radio-inline"> <input type="radio" name="review_star" id="review_star" value="1" checked="checked">★☆☆☆☆</label>
									<label class="radio-inline"> <input type="radio" name="review_star" id="review_star" value="2">★★☆☆☆</label>
									<label class="radio-inline"> <input type="radio" name="review_star" id="review_star" value="3">★★★☆☆</label>
									<label class="radio-inline"> <input type="radio" name="review_star" id="review_star" value="4">★★★★☆</label>
									<label class="radio-inline"> <input type="radio" name="review_star" id="review_star" value="5">★★★★★</label>
									
								</div>
							</div>
						</div>
						<div class="form-group">
							<div class="col-sm-offset-2 col-sm-10">
								<button type="submit" class="btn btn-black">작 성</button>
								<button type="reset" class="btn btn-black">초기화</button>
							</div>
						</div>
					</form>


				</div>

			</div>
		</div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-info" data-dismiss="modal">Close</button>
        </div>
      </div>
      
    </div>
  </div>
  
  
  <!-- Modal2 _ read review -->
  <div class="modal fade" id="myModal2" role="dialog">
    <div class="modal-dialog">
    
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title">Review Detail</h4>
        </div>
        <div class="modal-body">
                   	
          <textarea rows="10" class="form-control" readonly="readonly" id="content"></textarea>	
                    
        </div>
        <div class="modal-footer">
        						
          <button type="button" class="btn btn-danger" onclick="review_delete()">Delete</button>
          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        	
        </div>
      </div>
      
    </div>
  </div>

<script>
$(document).ready(function(){
	//검색창
	$("#searchInput").on("keyup",function(){
		var value = $(this).val().toLowerCase(); //입력창값을 소문자로 변환
		$("#searchTable tr").filter(function(){
			$(this).toggle($(this).text().toLowerCase().indexOf(value) > -1);
			//일치하지 않는 tr은 제거를 하고 일치하는 것이 있는 tr만 남겨두는 것이 toggle메서드임
		});
	});
	
});
}
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
					purl = "rplist?pageNo=" + pageNo;
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
						alert("rplist시 에러 발생");
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