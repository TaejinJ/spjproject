package com.kook.spjpj.command;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.kook.spjpj.dao.Dao;
import com.kook.spjpj.dto.BDto;
import com.kook.spjpj.dto.QDto;
import com.kook.spjpj.util.Constant;

public class QContentCommand implements ICommand {

	@Override
	public void execute(Model model, HttpServletRequest request) {
		Dao dao = Constant.dao;
		String qid = request.getParameter("qId");
		System.out.println(qid);
		QDto qdto = dao.qcontentView(qid);
		if(qdto != null) {
			model.addAttribute("qcontent_view",qdto);
		}

	}

}
