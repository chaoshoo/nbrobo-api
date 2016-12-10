package com.net.nky.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/reload")
public class ReloadController {

	private static Logger log = LoggerFactory.getLogger(ReloadController.class);

	@RequestMapping(value = "/load.do")
	@ResponseBody
	public void reloadDeviceAttr() {
	}
}
