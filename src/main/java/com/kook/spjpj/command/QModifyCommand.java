package com.kook.spjpj.command;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.kook.spjpj.dao.Dao;
import com.kook.spjpj.util.Constant;

public class QModifyCommand implements ICommand {

	@Override
	public void execute(Model model, HttpServletRequest request) {
		Dao dao = Constant.dao;
		
		String qId = request.getParameter("qId");
		String qNic = request.getParameter("qNic");
		String qTitle = request.getParameter("qTitle");
		String qContent = request.getParameter("qContent");
		
		dao.qmodify(qId, qNic, qTitle, qContent);

	}

}
