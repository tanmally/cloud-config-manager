package com.bbytes.config.web;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {

	@RequestMapping(value = "/loginfailed", method = RequestMethod.GET)
	public String loginerror(ModelMap model) {
		model.addAttribute("error", "true");
		return "login";

	}

	@RequestMapping(value = {"/", "/login"}, method = RequestMethod.GET)
	public String loginGet(ModelMap model) {
		return "login";

	}


	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String redirectToIndex(ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		model.addAttribute("message", "Spring Security Custom Form example");
		return "index";

	}
}
