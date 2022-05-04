package com.kook.spjpj.command;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.kook.spjpj.dao.Dao;
import com.kook.spjpj.dto.BDto;
import com.kook.spjpj.dto.RDto;
import com.kook.spjpj.util.Constant;

public class MyWageBillCommand implements ICommand {

	@Override
	public void execute(Model model, HttpServletRequest request) {
		Dao dao = Constant.dao;
		String nic = Constant.username;
		ArrayList<RDto> dtos = dao.wageBill(nic);
		request.setAttribute("wageBill", dtos);
		

	}
}
