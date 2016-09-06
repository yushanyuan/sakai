package org.sakaiproject.resource.util;

import org.sakaiproject.authz.api.FunctionManager;
import org.sakaiproject.component.cover.ComponentManager;

public class FunctionRegister {
	// 教师的文件资源操作权限
	public final static String FILE_PERM_NAME = "file.maintain.perm";

	//学生的文件资源访问权限
	public final static String FILE_ACCESS_PERM = "file.access.perm";
	
	//课件资源管理权限
	public final static String COURSEWARE_PERM_MAINTAIN = "courseware.perm.maintain";
	
	//课件模板管理权限
	public final static String TEMPLATE_PERM_MAINTAIN = "template.perm.maintain";

	//进入学习空间权限 学生
	public final static String STUDY_SPACE_PERM = "course.space.access";
	
	//进入课程空间权限 老师
	public final static String COURSE_SPACE_PERM = "course.space.maintain";

	public FunctionRegister() {
		FunctionManager functionManager = (FunctionManager) ComponentManager.get(FunctionManager.class);

		functionManager.registerFunction(FILE_PERM_NAME);
		functionManager.registerFunction(FILE_ACCESS_PERM);
		functionManager.registerFunction(COURSEWARE_PERM_MAINTAIN);
		functionManager.registerFunction(STUDY_SPACE_PERM);
		functionManager.registerFunction(COURSE_SPACE_PERM);
		functionManager.registerFunction(TEMPLATE_PERM_MAINTAIN);
	}
}
