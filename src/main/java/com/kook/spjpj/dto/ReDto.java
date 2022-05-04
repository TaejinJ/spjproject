package com.kook.spjpj.dto;

import java.util.Date;


public class ReDto {
	private int review_num; // �� ��ȣ
	private String user_id; // �ۼ��� ID
	private String review_goods_name; // ȸ���
	private String review_title; // ���� ����
	private String review_content; // ���� ����
	private int review_cnt; // ��ȸ��
	private int review_star; // ����
	private int review_buy_opt; // �˹ٿ��� 0 : X 1 : ����
	private Date review_regdate; // ���� �ۼ���
	
	public ReDto() {
		super();
		
	}
	
	public ReDto(int review_num, String user_id, String review_goods_name, String review_title, String review_content,
			int review_cnt, int review_star, int review_buy_opt, Date review_regdate) {
		super();
		this.review_num = review_num;
		this.user_id = user_id;
		this.review_goods_name = review_goods_name;
		this.review_title = review_title;
		this.review_content = review_content;
		this.review_cnt = review_cnt;
		this.review_star = review_star;
		this.review_buy_opt = review_buy_opt;
		this.review_regdate = review_regdate;
	}
	
	public int getReview_num() {
		return review_num;
	}

	public void setReview_num(int review_num) {
		this.review_num = review_num;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getReview_goods_name() {
		return review_goods_name;
	}

	public void setReview_goods_name(String review_goods_name) {
		this.review_goods_name = review_goods_name;
	}

	public String getReview_title() {
		return review_title;
	}

	public void setReview_title(String review_title) {
		this.review_title = review_title;
	}

	public String getReview_content() {
		return review_content;
	}

	public void setReview_content(String review_content) {
		this.review_content = review_content;
	}

	public int getReview_cnt() {
		return review_cnt;
	}

	public void setReview_cnt(int review_cnt) {
		this.review_cnt = review_cnt;
	}

	public int getReview_star() {
		return review_star;
	}

	public void setReview_star(int review_star) {
		this.review_star = review_star;
	}

	public int getReview_buy_opt() {
		return review_buy_opt;
	}

	public void setReview_buy_opt(int review_buy_opt) {
		this.review_buy_opt = review_buy_opt;
	}

	public Date getReview_regdate() {
		return review_regdate;
	}

	public void setReview_regdate(Date review_regdate) {
		this.review_regdate = review_regdate;
	}

}
