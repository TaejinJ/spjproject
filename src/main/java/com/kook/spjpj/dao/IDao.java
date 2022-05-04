package com.kook.spjpj.dao;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;

import com.kook.spjpj.dto.BDto;
import com.kook.spjpj.dto.JoinDto;
import com.kook.spjpj.dto.QDto;
import com.kook.spjpj.dto.RDto;
import com.kook.spjpj.dto.ReDto;

public interface IDao {
	//===== join =====
	public String join(JoinDto dto);
	
	//===login====
	public JoinDto login(String bid);
	
	//===== board =====
	public ArrayList<BDto> list();
	public void write(String nic, String title, String content);
	public BDto contentView(String id);
	public void upHit(String bId);
	public void modify(String bId, String bNic, String bTitle, String bContent);
	public void delete(String bId);
	public ArrayList<BDto> pageList(String pageNo);

	// ===== record =====
	public void record(String rcompany, String rnic, String rtime_from, String rtime_to, String rtime_day, String rwage_hour,
			String rwage_day);
	public ArrayList<RDto> wageBill(String rnic);
	//´ñ±Û
	public BDto reply_view(String bId);
	public void reply(String bId, String bName, String bTitle, String bContent, String bGroup, 
			String bStep, String bIndent);
	public void replyShape(String bGroup, String bStep);
	
//	//===== freeboard ´ñ±Û =====
//	public ArrayList<CDto> free_replylist();
//	public void reply_write(String reply_content, String reply_writer);
//	public CDto reply_contentView(String reply_rno);
	
	//===== qnaboard =====
	public ArrayList<QDto> qlist();
	public void qwrite(String qnic, String qtitle, String qcontent);
	public QDto qcontentView(String id);
	public void qupHit(String qId);
	public void qmodify(String qId, String qNic, String qTitle, String qContent);
	public void qdelete(String qId);

	ArrayList<QDto> qpageList(String qpageNo);


	
	//===== reviewboard =====
	public ArrayList<ReDto> review_list();
	public void review_write(String user_id, String review_goods_name, String review_title, String review_content);
	public ReDto review_contentView(String review_num);
//	public void review_upHit(String review_num);
//	public void review_modify(String user_id, String review_goods_name, String review_title, String review_content);
	public void review_delete(String review_num);
	public ArrayList<ReDto> rpageList(String rpageNo);
}
