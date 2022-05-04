package com.kook.spjpj.command;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.kook.spjpj.dao.Dao;
import com.kook.spjpj.util.Constant;

public class QBWriteCommand implements ICommand {

	@Override
	public void execute(Model model, HttpServletRequest request) {
		Dao dao = Constant.dao;
		
		String qnic = request.getParameter("qNic");
		String qtitle = request.getParameter("qTitle");
		String qcontent = request.getParameter("qContent");
		
		dao.qwrite(qnic,qtitle,qcontent);

	}

}
