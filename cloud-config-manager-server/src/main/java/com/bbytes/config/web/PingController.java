package com.bbytes.config.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PingController {

	@RequestMapping("/ping")
	@ResponseBody
	public String greeting() {
		return "Config manager, it works !!";
	}

}