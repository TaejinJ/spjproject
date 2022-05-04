package com.kook.spjpj.command;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.kook.spjpj.dao.Dao;
import com.kook.spjpj.dto.ReDto;
import com.kook.spjpj.util.Constant;

public class RBPageListCommand implements ICommand {

	@Override
	public void execute(Model model, HttpServletRequest request) {
		Dao dao = Constant.dao;
		String rpageNo = request.getParameter("rpageNo");
		ArrayList<ReDto> rdtos = dao.rpageList(rpageNo);
		request.setAttribute("rlistContent", rdtos);

	}

}
