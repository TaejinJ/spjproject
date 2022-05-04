package com.kook.spjpj.command;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.kook.spjpj.dao.Dao;
import com.kook.spjpj.dto.BDto;
import com.kook.spjpj.dto.QDto;
import com.kook.spjpj.util.Constant;

public class QListCommand implements ICommand {

	@Override
	public void execute(Model model, HttpServletRequest request) {
		Dao dao = Constant.dao;
		ArrayList<QDto> qdtos = dao.qlist();
		request.setAttribute("qlistContent", qdtos);
		

	}

}
