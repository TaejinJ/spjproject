package com.kook.spjpj.dto;

import java.security.Timestamp;
import java.sql.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class QDto {
	private int qId;
	private String qNic;
	private String qTitle;
	private String qContent;
   private Timestamp qDate;
 	
	private int qHit;
	private int qGroup;
	private int qStep;
	private int qIndent;
	public QDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public QDto(int qId, String qNic, String qTitle, String qContent, Timestamp qDate, int qHit, int qGroup, int qStep,
			int qIndent) {
		super();
		this.qId = qId;
		this.qNic = qNic;
		this.qTitle = qTitle;
		this.qContent = qContent;
		this.qDate = qDate;
		this.qHit = qHit;
		this.qGroup = qGroup;
		this.qStep = qStep;
		this.qIndent = qIndent;
	}
	public int getqId() {
		return qId;
	}
	public void setqId(int qId) {
		this.qId = qId;
	}
	public String getqNic() {
		return qNic;
	}
	public void setqNic(String qNic) {
		this.qNic = qNic;
	}
	public String getqTitle() {
		return qTitle;
	}
	public void setqTitle(String qTitle) {
		this.qTitle = qTitle;
	}
	public String getqContent() {
		return qContent;
	}
	public void setqContent(String qContent) {
		this.qContent = qContent;
	}
	public Timestamp getqDate() {
		return qDate;
	}
	public void setqDate(Timestamp qDate) {
		this.qDate = qDate;
	}
	public int getqHit() {
		return qHit;
	}
	public void setqHit(int qHit) {
		this.qHit = qHit;
	}
	public int getqGroup() {
		return qGroup;
	}
	public void setqGroup(int qGroup) {
		this.qGroup = qGroup;
	}
	public int getqStep() {
		return qStep;
	}
	public void setqStep(int qStep) {
		this.qStep = qStep;
	}
	public int getqIndent() {
		return qIndent;
	}
	public void setqIndent(int qIndent) {
		this.qIndent = qIndent;
	}
	
	
	
}
