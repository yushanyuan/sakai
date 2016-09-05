package org.sakaiproject.cmsrest.controller;


import javax.annotation.Resource;

import org.sakaiproject.user.api.UserDirectoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {

	@Resource(name="org.sakaiproject.user.api.UserDirectoryService")
	private UserDirectoryService userDirectoryService;

	
	@RequestMapping(value = "/addUser", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String addUser(@RequestParam("id") String id,@RequestParam("eid") String eid, @RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName
			, @RequestParam("email") String email, @RequestParam("pw") String pw, @RequestParam("type") String type) {
		 
		if (id == null) {
			return "{\"error\":\"参数为空\"}";
		}
		try{
			userDirectoryService.addUser(id, eid, firstName, lastName, email, pw, type, null);	
		}catch(Exception e){
			return "{\"error\":\"保存失败\"}";
		}
		return "{\"error\":\"\"}";
	}
	
}
