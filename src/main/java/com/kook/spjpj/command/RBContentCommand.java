package com.kook.spjpj.command;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.kook.spjpj.dao.Dao;
import com.kook.spjpj.dto.ReDto;
import com.kook.spjpj.util.Constant;

public class RBContentCommand implements ICommand {

	@Override
	public void execute(Model model, HttpServletRequest request) {
		Dao dao = Constant.dao;
		String review_num = request.getParameter("review_num");
		System.out.println(review_num);
		ReDto rdto = dao.review_contentView(review_num);
		if(rdto != null) {
			model.addAttribute("review_content_view",rdto);

		}

	}
}
