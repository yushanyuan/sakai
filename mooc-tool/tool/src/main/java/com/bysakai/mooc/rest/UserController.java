/**
 * 版权所有 北京思开科技有限公司 
 * All Rights Reserved
 */
package com.bysakai.mooc.rest;

import java.io.IOException;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.user.api.UserAlreadyDefinedException;
import org.sakaiproject.user.api.UserDirectoryService;
import org.sakaiproject.user.api.UserIdInvalidException;
import org.sakaiproject.user.api.UserPermissionException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author nesdu
 *
 */
@Controller
@RequestMapping("/user")
public class UserController {

	private static Log logger = LogFactory.getLog(UserController.class);
	
	
	@Resource(name="org.sakaiproject.user.api.UserDirectoryService")
	private UserDirectoryService userDirectoryService;

	/*@Resource(name="org.sakaiproject.tool.api.SessionManager")
	private SessionManager sessionManager;*/
	
/*	private LoginService loginService = (org.sakaiproject.login.api.LoginService) ComponentManager
			.get(org.sakaiproject.login.api.LoginService.class);
	*/
 
	@RequestMapping(value = "/regist")
	public String regist(Model model, @RequestParam(value="url", required=false) String url) throws IOException {
		if(url==null || "".equals(url)){
			url ="/portal";
		}
		model.addAttribute("nextPage", url);
		return "regist_save";
	}
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Model model, @RequestParam(value="nextPage", required=false) String nextPage,@RequestParam(value="username", required=false) String username,
			@RequestParam(value="name", required=false) String name,@RequestParam(value="pwd", required=false) String pwd,@RequestParam(value="email", required=false) String email) throws IOException {
		
		try {
			userDirectoryService.addUser(null, username, name, null, email, pwd, "registered", null);
		} catch (UserIdInvalidException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UserAlreadyDefinedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UserPermissionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(nextPage==null || "".equals(nextPage)){
			nextPage ="/portal";
		}
		
		model.addAttribute("username",username);
		model.addAttribute("password", pwd);
		model.addAttribute("nextPage", nextPage);
		return "regist_success";
	}
	/*@RequestMapping(value = "/json/regist", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, Object> regist(Model model, @RequestParam(value="username") String username, @RequestParam(value="password")  String password, @RequestParam(value="email")  String email, @RequestParam(value="nextPage", required=false) String nextPage, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		MsgVO msg = new MsgVO();
		
		logger.error("---username----:"+username);
		
		User user = null;
		try {
			user = userDirectoryService.getUserByEid(username);
		} catch (UserNotDefinedException e) {
			e.printStackTrace();
		}
		if(user!=null){
			Session sakaiSession = null;//sessionManager.getCurrentSession();
			sakaiSession.setUserId(username);
			session.setAttribute("username", username);
			msg.setData(nextPage);
			msg.setMsg("登陆成功!");
		}else{
			//userDirectoryService.addUser(username, username, username, "学生", email, pw, type, null);
			msg.setData("regist.html");
			msg.setMsg("未注册!");
		}
		Session session = sessionManager.getCurrentSession();
		session.setUserId("admin");
		
		session.setAttribute("username", username);
		
		
		msg.setData(nextPage);
		msg.setMsg("登陆成功!");
		map.put("data", msg);
		return map;
	}
	*/
	 
	 
}
