package com.kook.spjpj.dao;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import com.kook.spjpj.dto.BDto;
import com.kook.spjpj.dto.JoinDto;
import com.kook.spjpj.dto.QDto;
import com.kook.spjpj.dto.RDto;
import com.kook.spjpj.dto.ReDto;

public class Dao implements IDao {
//	Mybatis를 이용하여 DAO를 구현하려면 SqlSession 객체가 필요 (참고: https://heewon26.tistory.com/2)	
	@Autowired 
	private SqlSession sqlSession;
	
	@Override
	public String join(JoinDto dto) {
		int res = sqlSession.insert("join", dto); //mapper.xml호출
		System.out.println(res);
		String result = null;
		if(res > 0)
			result = "success";
		else
			result = "failed";
		
		return result;
	}

	//======login======
	@Override
	public JoinDto login(String bid) {
		System.out.println(bid);
		JoinDto result = sqlSession.selectOne("login",bid);
		return result;
	}
	
	//===== board =====
		@Override
		public ArrayList<BDto> list() {
			ArrayList<BDto> result = (ArrayList)sqlSession.selectList("list");
			return result;
		}
		
		@Override
		public void write(String nic, String title, String content) {
			BDto dto = new BDto(0,nic, title, content,null,0,0,0,0);
			sqlSession.insert("write",dto);
		}
		
		@Override
		public BDto contentView(String id) {
			upHit(id);	//hit수 처리, 내용 조기에 hit수가 더해져야함
			int idNum = Integer.parseInt(id);	//id는 주키인 게시판 번호로 db에서는 number
			BDto result = sqlSession.selectOne("contentView",idNum);
			return result;
		}
		
		@Override
		public void upHit(String bId) {
			int idNum = Integer.parseInt(bId);
			sqlSession.update("upHit",idNum);
		}
				

		@Override
		public void modify(String id, String bNic, String bTitle, String bContent) {
			int idNum = Integer.parseInt(id);
			BDto dto = new BDto(idNum, bNic, bTitle, bContent, null, 0, 0, 0, 0);
			sqlSession.update("modify",dto);	//수정이므로 sqlSession의 update메서드를 사용
		}
		
		@Override
		public void delete(String bId) {
			int idNum = Integer.parseInt(bId);
			sqlSession.delete("delete",idNum); //삭제이므로 sqlSession의 delete메서드 사용
		}
		
		@Override
		public ArrayList<BDto> pageList(String pageNo) {
			int page = Integer.parseInt(pageNo);
			int start = (page -1) * 10 + 1 ;
			System.out.println("start : " + start);
			ArrayList<BDto> result = (ArrayList)sqlSession.selectList("pageList",start);
			System.out.println(result);
			return result;
		}


		@Override
		public void record(String company, String nic, String time_from, String time_to, String time_day, String wage_hour, String wage_day) {
			//SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
			//java.sql.Date date = java.sql.Date.valueOf(rDate);
			DateTimeFormatter formatDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
	        LocalDateTime localTime1 = LocalDateTime.from(formatDateTime.parse(time_from));
	        Timestamp rtime_from = Timestamp.valueOf(localTime1);
	        
	        LocalDateTime localTime2 = LocalDateTime.from(formatDateTime.parse(time_to));
	        Timestamp rtime_to = Timestamp.valueOf(localTime2);
	        
	        float rtime_day = Float.parseFloat(time_day);
	        float rwage_hour = Float.parseFloat(wage_hour);
	        float rwage_day = Float.parseFloat(wage_day);
			
			RDto dto = new RDto(company, nic, rtime_from, rtime_to, rtime_day, 0, rwage_hour, rwage_day);
			sqlSession.insert("record", dto);
		}

		@Override
		public ArrayList<RDto> wageBill(String rnic) {
			System.out.println(rnic);
			ArrayList<RDto> result = (ArrayList)sqlSession.selectList("wageBill", rnic);
			return result;
		}
		@Override
		public BDto reply_view(String bId) {
			int idNum = Integer.parseInt(bId);
			BDto result = sqlSession.selectOne("reply_view",idNum);
			//게시판 목록번호에 해당하는 레코드
			return result;
		}
		
		@Override
		public void reply(String bId, String bNic, String bTitle, String bContent, String bGroup, 
				String bStep, String bIndent) {
			replyShape(bGroup, bStep);
			//db컬럼이 number인것은 모두 int로 변환(dto와도 일치)
			int idNum = Integer.parseInt(bId);
			int iGroup = Integer.parseInt(bGroup);
			int iStep = Integer.parseInt(bStep);
			int iIndent = Integer.parseInt(bIndent);
			
			BDto dto = new BDto(idNum, bNic, bTitle, bContent, null, 0, 
					iGroup, iStep, iIndent);
			
			sqlSession.insert("reply",dto);
		}
		
		@Override
		public void replyShape(String bGroup, String bStep) {
			int iGroup = Integer.parseInt(bGroup);
			int iStep = Integer.parseInt(bStep);
			BDto dto = new BDto(0, null, null, null, null, 0, iGroup, iStep, 0);
			sqlSession.update("replyShape",dto);
		}
		
		
//		// ====== freeboard 댓글=======
//		
//	//댓글목록
//	@Override
//	public ArrayList<CDto> free_replylist() {
//		ArrayList<CDto> result = (ArrayList)sqlSession.selectList("free_replylist");
//		return result;
//	}
//	
//	@Override
//	public void reply_write(String reply_content, String reply_writer) {
//		CDto cdto = new CDto(0,0, reply_content, reply_writer, null);
//		sqlSession.insert("free_replywrite",cdto);
//	}
//	
//	@Override
//	public CDto reply_contentView(String reply_rno) {
//		upHit(reply_rno);	//hit수 처리, 내용 조기에 hit수가 더해져야함
//		int cidNum = Integer.parseInt(reply_rno);	//id는 주키인 게시판 번호로 db에서는 number
//		CDto result = sqlSession.selectOne("reply_contentView",cidNum);
//		return result;
//	}
	
//======= reviewboard ========
	
	@Override
	public ArrayList<ReDto> review_list() {
		ArrayList<ReDto> result = (ArrayList)sqlSession.selectList("review_list");
		return result;
		
	}
	
	@Override
	public void review_write(String user_id, String review_goods_name, String review_title, String review_content) {
		ReDto rdto = new ReDto(0,user_id, review_goods_name, review_title, review_content,0,0,0,null);
		sqlSession.insert("review_write",rdto);
	}
	
	@Override
	public ReDto review_contentView(String review_num) {
		upHit(review_num);	//hit수 처리, 내용 조기에 hit수가 더해져야함
		int ridNum = Integer.parseInt(review_num);	//id는 주키인 게시판 번호로 db에서는 number
		ReDto result = sqlSession.selectOne("review_contentView",ridNum);
		return result;
	}
//	
//	@Override
//	public void review_upHit(String review_num) {
//		int ridNum = Integer.parseInt(review_num);
//		sqlSession.update("review_upHit",ridNum);
//	}
//	
//	@Override
//	public void review_modify(String user_id, String review_goods_name, String review_title, String review_content) {
//		int ridNum = Integer.parseInt(review_num);	//db와 BoardDto에서는 int이므로 형변환
//		ReDto rdto = new ReDto(ridNum, user_id, review_goods_name, review_title, review_content, 0, 0, 0, null);
//		sqlSession.update("review_modify",rdto);	//수정이므로 sqlSession의 update메서드를 사용
//	}
//	
	@Override
	public void review_delete(String review_num) {
		int ridNum = Integer.parseInt(review_num);
		sqlSession.delete("review_delete",ridNum); //삭제이므로 sqlSession의 delete메서드 사용
	}
	
	@Override
	public ArrayList<ReDto> rpageList(String rpageNo) {
		int rpage = Integer.parseInt(rpageNo);
		int start = (rpage -1) * 10 + 1 ;
		System.out.println("start : " + start);
		ArrayList<ReDto> result = (ArrayList)sqlSession.selectList("rpageList",start);
		System.out.println(result);
		return result;
	}
	//=====qna board =====
	@Override
	public ArrayList<QDto> qlist() {
		ArrayList<QDto> result = (ArrayList)sqlSession.selectList("qlist");
		return result;
	}
	
	@Override
	public void qwrite(String qnic, String qtitle, String qcontent) {
		QDto dto = new QDto(0,qnic, qtitle, qcontent,null,0,0,0,0);
		sqlSession.insert("qwrite",dto);
	}
	
	@Override
	public QDto qcontentView(String id) {
		qupHit(id);	//hit수 처리, 내용 조기에 hit수가 더해져야함
		int idNum = Integer.parseInt(id);	//id는 주키인 게시판 번호로 db에서는 number
		QDto result = sqlSession.selectOne("qcontentView",idNum);
		return result;
	}
	
	@Override
	public void qupHit(String qId) {
		int idNum = Integer.parseInt(qId);
		sqlSession.update("qupHit",idNum);
	}
	
	@Override
	public void qmodify(String qId, String qNic, String qTitle, String qContent) {
		int idNum = Integer.parseInt(qId);	//db와 BoardDto에서는 int이므로 형변환
		QDto qdto = new QDto(idNum, qNic, qTitle, qContent, null, 0, 0, 0, 0);
		sqlSession.update("qmodify",qdto);	//수정이므로 sqlSession의 update메서드를 사용
	}
	@Override
	public void qdelete(String qId) {
		int idNum = Integer.parseInt(qId);
		sqlSession.delete("qdelete",idNum); //삭제이므로 sqlSession의 delete메서드 사용
	}
	
	
	@Override
	public ArrayList<QDto> qpageList(String qpageNo) {
		int page = Integer.parseInt(qpageNo);
		int start = (page -1) * 10 + 1 ;
		System.out.println("start : " + start);
		ArrayList<QDto> result = (ArrayList)sqlSession.selectList("qpageList",start);
		System.out.println(result);
		return result;
	}

	


}
