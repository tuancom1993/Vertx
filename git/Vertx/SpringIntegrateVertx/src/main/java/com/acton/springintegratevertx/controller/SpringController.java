package com.acton.springintegratevertx.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import com.acton.springintegratevertx.model.ValueObjectStatic;
import com.acton.springintegratevertx.service.MessageService;

@Controller
public class SpringController {
	@Autowired MessageService messageService; 
	
	@RequestMapping(value="/cal", method=RequestMethod.GET)
	public String hello(){
		return "hello";
	}
	
	@RequestMapping(value="/cal", method=RequestMethod.POST)
	public String doHello(HttpServletRequest request){

		String value = request.getParameter("value");
		
		RestTemplate restTemplate = new RestTemplate();
		
		String reply = restTemplate.postForObject("http://localhost:8081/springtovertx", value, String.class);
		ValueObjectStatic.setValue(reply);
		return "redirect:/show";
	}
	
	@RequestMapping(value="/show")
	public String show(Model model){
		model.addAttribute("val", ValueObjectStatic.getValue());
		return "show";
	}
}
