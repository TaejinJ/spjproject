package com.kook.spjpj.command;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.kook.spjpj.dao.Dao;
import com.kook.spjpj.util.Constant;

public class RBWriteCommand implements ICommand {

	@Override
	public void execute(Model model, HttpServletRequest request) {
		Dao dao = Constant.dao;
		
		String user_id = request.getParameter("user_id");
		String review_goods_name = request.getParameter("review_goods_name");
		String review_title = request.getParameter("review_title");
		String review_content = request.getParameter("review_content");
		int review_buy_opt;
			if(request.getParameter("review_buy_opt") != null){
				
				review_buy_opt =  Integer.parseInt(request.getParameter("review_buy_opt"));
					
				} else{
				
				review_buy_opt = 0;
					
				}
			
		int review_star = Integer.parseInt(request.getParameter("review_star"));
		
		dao.review_write(user_id, review_goods_name, review_title, review_content);
	}

}
