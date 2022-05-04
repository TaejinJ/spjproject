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

<!-- fullcalendar css -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/fullcalendar@5.10.1/main.css" rel='stylesheet'>

<script src="https://cdn.jsdelivr.net/npm/fullcalendar@5.10.1/main.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.18.1/moment.min.js"></script>
<!-- fullcalendar 언어 설정관련 script -->
<script src="https://cdn.jsdelivr.net/npm/fullcalendar@5.10.1/locales-all.js"></script>

<title>Calculate</title>

<style>
html {
    position: relative;
    min-height: 100%;
    margin: 0;
}

body {
    min-height: 100%;
}


/* body 스타일 */
html, body {
font-family: Arial, Helvetica Neue, Helvetica, sans-serif;
font-size: 14px;
}

/* 캘린더 위의 해더 스타일(날짜가 있는 부분) */
.fc-header-toolbar {
padding-top: 1em;
padding-left: 1em;
padding-right: 1em;
}

.calendar-container{
	width:100%;
	height:100%;
	display:flex;
	justify-content:center;
}

.fc-media-screen{
	width: 800px;
	height: 600px;
	position: relative;
}
</style>

</head>

<body>

<%@ include file="spj_nav.jsp"%>

<div class="calendar-container">
	<div id='calendar'></div>
</div>


<%@ include file="spj_footer.jsp"%>

<script>
document.addEventListener('DOMContentLoaded', function() { var calendarEl = document.getElementById('calendar'); 
// new FullCalendar.Calendar(대상 DOM객체, {속성:속성값, 속성2:속성값2..}) 

	var calendar = new FullCalendar.Calendar(calendarEl, { 
		headerToolbar: { 
			left: 'prev,next today', 
			center: 'title', 
			right: 'dayGridMonth,timeGridWeek,timeGridDay' 
			}, 
			
			initialDate: '2022-04-08', // 초기 로딩 날짜. 
			local : "ko",
			navLinks: true, // can click day/week names to navigate views 
			selectable: true, 
			selectMirror: true, // 이벤트명 : function(){} : 각 날짜에 대한 이벤트를 통해 처리할 내용.. 
			select: function(arg) { 
				console.log(arg); 
				
				var title = prompt('입력할 일정:'); // title 값이 있을때, 화면에 calendar.addEvent() json형식으로 일정을 추가 
				if (title) { calendar.addEvent({ 
					title: title, 
					start: arg.start, 
					end: arg.end, 
					allDay: arg.allDay, 
					backgroundColor:"yellow", 
					textColor:"blue" 
					}) 
					} 
				calendar.unselect() 
				}, 
				eventClick: function(arg) { 
					// 있는 일정 클릭시, 
					console.log("#등록된 일정 클릭#"); 
					console.log(arg.event); 
					
					if (confirm('일정을 삭제하시겠습니까?')) { 
						arg.event.remove() 
						} 
					}, 
					editable: true, 
					dayMaxEvents: true, // allow "more" link when too many events 
					events: //================ ajax데이터 불러올 부분 =====================// 
						//json형태로 출력되는 데이터
					{"list":[{ 
						"id":1, 
						"groupId":null, 
						"title":"일정title", 
						"writer":null, 
						"content":null, 
						"start":"2021-05-01T00:00:00", 
						"end":"2021-05-03T00:00:00", 
						"allday":true,"textColor":"yellow", 
						"backgroundColor":"navy", "borderColor":"navy" 
					}]}
					

				}); 
				
				calendar.render(); 
				});




</script>


</body>
</html>