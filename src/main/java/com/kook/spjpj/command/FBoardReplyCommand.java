package com.kook.spjpj.command;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.kook.spjpj.dao.IDao;
import com.kook.spjpj.util.Constant;



public class FBoardReplyCommand implements ICommand {

	@Override
	public void execute(Model model, HttpServletRequest request) {
		IDao dao = Constant.dao;
		
		String bId = request.getParameter("bId");
		String bName = request.getParameter("bNic");
		String bTitle = request.getParameter("bTitle");
		String bContent = request.getParameter("bContent");
		String bGroup = request.getParameter("bGroup");
		String bStep = request.getParameter("bStep");
		String bIndent = request.getParameter("bIndent");
		
		dao.reply(bId, bName, bTitle, bContent, bGroup, bStep, bIndent);
	}

}
