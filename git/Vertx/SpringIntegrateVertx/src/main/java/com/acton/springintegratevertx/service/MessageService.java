package com.acton.springintegratevertx.service;

import org.springframework.stereotype.Service;

@Service
public class MessageService {
	public String getMessage(){
		return "this is message from Service..!";
	}
}
