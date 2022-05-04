package com.kook.spjpj.command;


import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.kook.spjpj.dao.IDao;
import com.kook.spjpj.dto.BDto;
import com.kook.spjpj.util.Constant;



public class FBoardReplyViewCommand implements ICommand {

	@Override
	public void execute(Model model, HttpServletRequest request) {
		IDao dao = Constant.dao;
		
		String bid = request.getParameter("bId");
		
		System.out.println(bid);
		
			BDto dto = dao.reply_view(bid);
		
		//String bName = dto.setbName(Constant.username);
		model.addAttribute("reply_view", dto);
	}
}


