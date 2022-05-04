package com.kook.spjpj.command;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.kook.spjpj.dao.Dao;
import com.kook.spjpj.dto.ReDto;
import com.kook.spjpj.util.Constant;

public class RListCommend implements ICommand {

	@Override
	public void execute(Model model, HttpServletRequest request) {
		Dao dao = Constant.dao;
		ArrayList<ReDto> rdtos = dao.review_list();
		request.setAttribute("rlistContent", rdtos);
	}

}
