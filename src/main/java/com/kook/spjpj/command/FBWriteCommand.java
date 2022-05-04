package com.kook.spjpj.command;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.kook.spjpj.dao.Dao;
import com.kook.spjpj.util.Constant;

public class FBWriteCommand implements ICommand {

	@Override
	public void execute(Model model, HttpServletRequest request) {
		Dao dao = Constant.dao;
		
		String nic = request.getParameter("bNic");
		String title = request.getParameter("bTitle");
		String content = request.getParameter("bContent");
		
		dao.write(nic,title,content);

	}

}
