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
<title>comment</title>

<!-- 댓글관련 script -->
<script type="text/javascript">

</script>
</head>

<body>

<!-- 댓글 입력 -->
<div class="row">
	<div class="col-sm-12" style="margin-top:20px;">
	<div class="comment-write" id="free_replywrite">
		<form class="comment_form" role="form" method="post">
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
			<div class="input-group">
				<textarea name="comment" id="reply_content" class="form-control p-10" placeholder="댓글입력">
				</textarea>
				<span class="input-group-btn">
					<button class="btn btn-white btn-lg" id="reply_write" type="button" style="background-color: #ddd">
						<i class="far fa-edit"></i>
					</button>
				</span>
			</div>
			
		</form>
	</div>
	</div>
</div>
	
	<!-- 댓글 목록 -->
<div class="container">
	<ul class="list-group list-group-flush">
	<c:forEach var="free_replylist" items="${free_replylist}">
		<li class="list-group-item">
			작성자 : ${free_replylist.reply_writer} &nbsp;&nbsp;&nbsp; 작성 날짜 : <fmt:formatDate value="${free_replylist.reply_regdate}" pattern="yyyy-MM-dd"/><br/>
			댓글 내용 : <span>${free_replylist.reply_content}</span><br/>
		</li>
		<br/>
		<div>
			<button type="button" class="btn btn-outline-success" id="replyupdateBtn" data-rno="${free_replylist.reply_rno}">댓글 수정</button>
			<button type="button" class="btn btn-outline-success" id="replydeleteBtn" data-rno="${free_replylist.reply_rno}">댓글 삭제</button>
		</div>
		<br/>
	</c:forEach>
	</ul>
</div>
</body>
</html>