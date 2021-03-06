/*
 * AbstractController.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import services.ConfigurationService;

@Controller
public class AbstractController {

	// Panic handler ----------------------------------------------------------

	@Autowired
	private ConfigurationService	configurationService;


	@ExceptionHandler(Throwable.class)
	public ModelAndView panic(final Throwable oops) {
		ModelAndView result;

		result = new ModelAndView("misc/panic");
		result.addObject("name", ClassUtils.getShortName(oops.getClass()));
		result.addObject("exception", oops.getMessage());
		result.addObject("stackTrace", ExceptionUtils.getStackTrace(oops));

		return result;
	}

	@ModelAttribute("banner")
	public String getSysBanner() {
		String res = this.configurationService.findConfiguration().getBannerURL();
		return res;
	}

	@ModelAttribute("isRebranded")
	public boolean getRebranded() {
		boolean res = this.configurationService.findConfiguration().getUsersHaveBeenNotified();
		return res;
	}

	@ModelAttribute("sysName")
	public String getSysName() {
		String res = this.configurationService.findConfiguration().getSysName();
		return res;
	}

}
