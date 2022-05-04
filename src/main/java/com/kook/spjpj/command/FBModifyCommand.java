package com.kook.spjpj.command;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.kook.spjpj.dao.Dao;
import com.kook.spjpj.util.Constant;

public class FBModifyCommand implements ICommand {

	@Override
	public void execute(Model model, HttpServletRequest request) {
		Dao dao = Constant.dao;
		
		String id = request.getParameter("bId");
		String nic = request.getParameter("bNic");
		String title = request.getParameter("bTitle");
		String content = request.getParameter("bContent");
		
		dao.modify(id, nic, title, content);

	}

}
