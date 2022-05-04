package com.kook.spjpj.dto;

import java.sql.Date;

import org.springframework.format.annotation.DateTimeFormat;

//게시판 DB MVC_BOARD 테이블과 매핑되는 DTO클래스
public class BDto {
	//멤버변수 이름은 MVC_BOARD 테이블의 컬럼과 데이터형이 매핑
		//대소문자를 무시하나 이름은 같아야 하고 데이터형도 서로 상응하는 형이어야 함
		private int bId;
		private String bNic;
		private String bTitle;
		private String bContent;
	 @DateTimeFormat(pattern = "yyyy-MM-dd")
	 	private Date bDate;
		private int bHit;
		private int bGroup;
		private int bStep;
		private int bIndent;
		
		public BDto() {
			super();
			
		}

		public BDto(int bId, String bNic, String bTitle, String bContent, Date bDate, int bHit, int bGroup,
				int bStep, int bIndent) {
			super();
			this.bId = bId;
			this.bNic = bNic;
			this.bTitle = bTitle;
			this.bContent = bContent;
			this.bDate = bDate;
			this.bHit = bHit;
			this.bGroup = bGroup;
			this.bStep = bStep;
			this.bIndent = bIndent;
		}

		public int getbId() {
			return bId;
		}

		public void setbId(int bId) {
			this.bId = bId;
		}

		public String getbNic() {
			return bNic;
		}

		public void setbNic(String bNic) {
			this.bNic = bNic;
		}

		public String getbTitle() {
			return bTitle;
		}

		public void setbTitle(String bTitle) {
			this.bTitle = bTitle;
		}

		public String getbContent() {
			return bContent;
		}

		public void setbContent(String bContent) {
			this.bContent = bContent;
		}

		public Date getbDate() {
			return bDate;
		}

		public void setbDate(Date bDate) {
			this.bDate = bDate;
		}

		public int getbHit() {
			return bHit;
		}

		public void setbHit(int bHit) {
			this.bHit = bHit;
		}

		public int getbGroup() {
			return bGroup;
		}

		public void setbGroup(int bGroup) {
			this.bGroup = bGroup;
		}

		public int getbStep() {
			return bStep;
		}

		public void setbStep(int bStep) {
			this.bStep = bStep;
		}

		public int getbIndent() {
			return bIndent;
		}

		public void setbIndent(int bIndent) {
			this.bIndent = bIndent;
		}
}
