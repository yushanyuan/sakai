/**
 * 版权所有 北京思开科技有限公司 
 * All Rights Reserved
 */
package cn.net.sikai.web;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

	
	private static final Logger logger = Logger.getLogger(UserController.class);

	/*@Resource(name="cn.net.sikai.service.userService")
	private UserService userService;
	
	//-----------------------------
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(Model model) throws IOException  {
		return "users";
	}
	
	
	@RequestMapping(value = "/syncStudent", method = RequestMethod.GET)
	public String syncStudent(Model model) throws IOException  {
		logger.warn("---start sync student----");
		int newStu = 0;
		try{
			newStu = userService.syncStudent();
		}catch(Exception e){
			e.printStackTrace();
		}
		model.addAttribute("newStu",newStu);
		return "sync_success";
	}
 
	@RequestMapping(value = "/syncTeacher", method = RequestMethod.GET)
	public String syncTeacher(Model model) throws IOException  {
		logger.warn("---start sync syncTeacher----");
		int newStu = 0;
		try{
			newStu = userService.syncTeacher();
		}catch(Exception e){
			e.printStackTrace();
		}
		model.addAttribute("newStu",newStu);
		return "sync_success";
	}*/
}
