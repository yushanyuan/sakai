/**
 * 版权所有 北京思开科技有限公司 
 * All Rights Reserved
 */
package com.bysakai.mooc.rest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import org.apache.commons.lang.StringUtils;
import org.sakaiproject.component.api.ServerConfigurationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.bysakai.mooc.api.CategoryService;
import com.bysakai.mooc.model.Course;
import com.bysakai.mooc.model.Categorys;
import com.bysakai.mooc.rest.vo.CategoryVO;
import com.bysakai.mooc.utils.Constant;
import com.bysakai.mooc.utils.UploadUtil;


/**
 * @author llk
 *
 */
@Controller
@RequestMapping("/category")
public class CategoryController {

	@Resource(name="com.bysakai.mooc.api.CategoryService")
	private CategoryService categoryService;
	@Resource(name="org.sakaiproject.component.api.ServerConfigurationService")
	private ServerConfigurationService serverConfigurationService;
	
	
	/**
	 * 专业列表
	 * @return
	 */
	@RequestMapping(value = "/list")
	public String list(Model model) {
		 
		String moocServer = serverConfigurationService.getString(Constant.MOOC_SERVER_URL);
		model.addAttribute("moocServer", moocServer);
		
		model.addAttribute("list", categoryService.list());
		
		return "category_list";
	}
	
	/**
	 * 新增
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/add")
	public String add(Model model) {
		
		return "category_add";
	}
	
	/**
	 * 编辑
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/edit")
	public String edit(Model model, @RequestParam("id") String id) {
		Categorys persist = categoryService.findOne(id);
		model.addAttribute("category", persist);
		return "category_add";
	}
	
	/**
	 * 保存或修改
	 * @param model
	 * @param id
	 * @param name
	 * @param iconFile
	 * @return
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Model model,  @RequestParam(value="id", required=false) String id, @RequestParam("name") String name, @RequestParam("icon") MultipartFile iconFile ) {
		try {
			Categorys persist = new Categorys();
			if(StringUtils.isNotBlank(id)){
				persist = categoryService.findOne(id);
			} 
			
			persist.setName(name);
			
			UploadUtil uploadUtil = new UploadUtil();
			uploadUtil.setApacheServerFilePath(serverConfigurationService.getString(Constant.MOOC_FILE_PATH));
			uploadUtil.setApacheServerImagesPath(serverConfigurationService.getString(Constant.apacheServerImagesPath));
			
			if (iconFile.getOriginalFilename() != null && !iconFile.isEmpty()) {
				 
				String filePath = uploadUtil.uploadIcon(iconFile.getInputStream(), iconFile.getOriginalFilename(), persist.getIcon());
				if (StringUtils.isNotBlank(filePath)) {
					persist.setIcon(filePath); 
				}
			}
			categoryService.save(persist);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list(model);
	}
	
	
	/**
	 * 删除 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/remove")
	public String remove(Model model, @RequestParam("id") String id) {
		Categorys category = categoryService.findOne(id);
		Set<Course> set = category.getCourseList();
		if(set!=null && set.size()>0){
			model.addAttribute("alertMesage", "该专业下绑定了课程信息，请先删除课程！");
		}else{
			categoryService.remove(category);
		}
		
		return list(model);
	}
	
	//-------------------------------------
	
	@RequestMapping(value = "/json/list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, Object> list() {
		Map<String, Object> map = new HashMap<String, Object>();
		List<CategoryVO> speces = new ArrayList<CategoryVO>();
		try{
			List<Categorys> specialitys = categoryService.list();
			for(Categorys s: specialitys){
				CategoryVO sp = new CategoryVO();
				sp.setName(s.getName());
				sp.setIcon(serverConfigurationService.getString(Constant.MOOC_SERVER_URL) + s.getIcon());
				sp.setId(s.getId());
				speces.add(sp);
			}
			map.put("data", speces);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return map;
	}
}
