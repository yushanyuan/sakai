package org.sakaiproject.resource.impl.course.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.adl.parsers.dom.DOMTreeUtility;
import org.adl.samplerte.server.packageimport.ManifestHandler;
import org.adl.samplerte.server.packageimport.parsers.dom.ADLDOMParser;
import org.adl.util.decode.decodeHandler;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.xwork.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.dom4j.io.OutputFormat;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.sakaiproject.component.cover.ComponentManager;
import org.sakaiproject.component.cover.ServerConfigurationService;
import org.sakaiproject.resource.api.course.model.MeleteCourseModel;
import org.sakaiproject.resource.api.course.model.MeleteForumModel;
import org.sakaiproject.resource.api.course.model.MeleteModuleModel;
import org.sakaiproject.resource.api.course.model.MeleteSectionModel;
import org.sakaiproject.resource.api.course.model.MeleteSelfTestModel;
import org.sakaiproject.resource.api.course.model.MeleteTestModel;
import org.sakaiproject.resource.api.course.service.ICourseService;
import org.sakaiproject.resource.api.courseware.model.CoursewareModel;
import org.sakaiproject.resource.api.study.model.MeleteSectionRecordModel;
import org.sakaiproject.resource.api.study.model.MeleteTestAttemptModel;
import org.sakaiproject.resource.util.CacheUtil;
import org.sakaiproject.resource.util.CodeTable;
import org.sakaiproject.resource.util.Constants;
import org.sakaiproject.resource.util.CourseUtil;
import org.sakaiproject.resource.util.HibernateDaoSupport;
import org.sakaiproject.resource.util.QueryString;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.SiteService;
import org.sakaiproject.tool.api.ToolManager;
import org.sakaiproject.user.api.UserDirectoryService;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.schlichtherle.io.ArchiveDetector;
import de.schlichtherle.io.DefaultArchiveDetector;
import de.schlichtherle.io.archive.zip.CheckedZipDriver;

public class CourseServiceImpl extends HibernateDaoSupport implements ICourseService {

	private UserDirectoryService userDirectoryService = (UserDirectoryService) ComponentManager
			.get(UserDirectoryService.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sakaiproject.resource.api.course.service.ICourseService#getCourseBySiteId
	 * (java.lang.String)
	 */
	public MeleteCourseModel getCourseBySiteId(String siteId) throws Exception {
		try {
			// 先取siteid，如果取不到则取courseId
			String hql = "from MeleteCourseModel course where course.siteId = ?";
			Object[] parameters = { siteId };
			List<MeleteCourseModel> courseList = this.findEntity(hql, parameters);
			if (!courseList.isEmpty() && courseList.size() > 0) {
				return courseList.get(0);
			} else {
				return this.getCourseById(siteId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sakaiproject.resource.api.course.service.ICourseService#saveCourse
	 * (org.sakaiproject.resource.api.course.model.MeleteCourseModel)
	 */

	// 保存课程信息
	public void saveCourse(MeleteCourseModel course) throws Exception {
		try {
			this.createEntity(course);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.sakaiproject.resource.api.course.service.ICourseService#
	 * leafModuleByCourseId(java.lang.String)
	 */
	// 根据课程id查询是否有下级模块，用于判断课程节点是否为叶子
	public boolean leafModuleByCourseId(String courseId, boolean showHide) throws Exception {
		try {
			if (showHide) {
				String hql = "select count(*) from MeleteModuleModel where courseId=? and status in (?,?) and parentId is null ";
				Object[] parameters = { courseId, new Long(CodeTable.hide), new Long(CodeTable.normal) };
				List<Object> countList = this.findEntity(hql, parameters);
				if (!countList.isEmpty() && countList.size() != 0 && !countList.get(0).toString().equals("0")) {
					return false;
				} else {
					return true;
				}
			} else {
				String hql = "select count(*) from MeleteModuleModel where courseId=? and status=? and parentId is null ";
				Object[] parameters = { courseId, new Long(CodeTable.normal) };
				List<Object> countList = this.findEntity(hql, parameters);
				if (!countList.isEmpty() && countList.size() != 0 && !countList.get(0).toString().equals("0")) {
					return false;
				} else {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.sakaiproject.resource.api.course.service.ICourseService#
	 * getModuleByCourseId(java.lang.String)
	 */
	// 根据课程id查询模块集合，展开课程节点时调用，用于加载下级模块
	public List<MeleteModuleModel> getModuleByCourseId(String courseId, boolean showHide) throws Exception {
		try {
			if (showHide) {
				String hql = "from MeleteModuleModel where courseId=? and status in (?,?) and parentId is null order by idx ";
				Object[] parameters = { courseId, new Long(CodeTable.normal), new Long(CodeTable.hide) };
				List<MeleteModuleModel> list = this.findEntity(hql, parameters);
				return list;
			} else {
				String hql = "from MeleteModuleModel where courseId=? and status=? and parentId is null order by idx ";// idx//讨论序号
				Object[] parameters = { courseId, new Long(CodeTable.normal) };
				List<MeleteModuleModel> list = this.findEntity(hql, parameters);
				return list;
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.sakaiproject.resource.api.course.service.ICourseService#
	 * leafModuleByParentId(java.lang.String)
	 */
	// 根据上级模块id查询是否有下级模块，用于判断模块节点是否为叶子
	public boolean leafModuleByParentId(Long parentId, boolean showHide) throws Exception {
		try {
			if (showHide) {
				String hql = "select count(*) from MeleteModuleModel module where module.parentId = ? and module.status in (?,?) ";
				Object[] parameters = { parentId, new Long(CodeTable.hide), new Long(CodeTable.normal) };
				List<Object> countList = this.findEntity(hql, parameters);
				if (!countList.isEmpty() && countList.size() != 0 && !countList.get(0).toString().equals("0")) {
					return false;
				} else {
					return true;
				}
			} else {
				String hql = "select count(*) from MeleteModuleModel module where module.parentId = ? and module.status=? ";
				Object[] parameters = { parentId, new Long(CodeTable.normal) };
				List<Object> countList = this.findEntity(hql, parameters);
				if (!countList.isEmpty() && countList.size() != 0 && !countList.get(0).toString().equals("0")) {
					return false;
				} else {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.sakaiproject.resource.api.course.service.ICourseService#
	 * leafSectionByModuleId(java.lang.String)
	 */
	// 根据模块id查询是否有页，用于判断模块节点是否为叶子
	public boolean leafSectionByModuleId(Long moduleId) throws Exception {
		try {
			String hql = "select count(*) from MeleteSectionModel section where section.moduleId = ? and section.status=?";
			Object[] parameters = { moduleId, new Long(CodeTable.normal) };
			List<Object> countList = this.findEntity(hql, parameters);
			if (!countList.isEmpty() && countList.size() != 0 && !countList.get(0).toString().equals("0")) {
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.sakaiproject.resource.api.course.service.ICourseService#
	 * countElectiveSecByModuleId(java.lang.String)
	 */
	// 根据模块id查询选修页总个数
	public Long countElectiveSecByModuleId(Long moduleId) throws Exception {
		try {
			String hql = "select count(*) from MeleteSectionModel section where section.moduleId = ? and section.status=? and section.required=?";// required选修OR必修
			Object[] parameters = { moduleId, new Long(CodeTable.normal), new Long(CodeTable.elective) };// 选修
			List<Object> countList = this.findEntity(hql, parameters);
			if (countList.isEmpty() || countList.size() == 0) {
				return new Long(0);
			} else {
				return new Long(countList.get(0).toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.sakaiproject.resource.api.course.service.ICourseService#
	 * countRequiredSecByModuleId(java.lang.Long)
	 */
	// 根据模块id查询必修页总个数
	public Long countRequiredSecByModuleId(Long moduleId) throws Exception {
		try {
			String hql = "select count(*) from MeleteSectionModel where moduleId = ? and status=? and required=?";
			Object[] parameters = { moduleId, new Long(CodeTable.normal), new Long(CodeTable.required) };// 必修
			List<Object> countList = this.findEntity(hql, parameters);
			if (countList.isEmpty() || countList.size() == 0) {
				return new Long(0);
			} else {
				return new Long(countList.get(0).toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.sakaiproject.resource.api.course.service.ICourseService#
	 * leafTestByModuleId(java.lang.String)
	 */
	// 根据模块id查询是否有作业，用于判断模块节点是否为叶子
	public boolean leafTestByModuleId(Long moduleId, boolean showScore) throws Exception {
		try {
			if (showScore) {
				String hql = "select count(*) from MeleteTestModel where moduleId = ? and status=? and isCaculateScore=?";
				Object[] parameters = { moduleId, new Long(CodeTable.normal), new Long(CodeTable.IsCaculateScoreYes) };// 是否计算平时成绩
				List<Object> countList = this.findEntity(hql, parameters);
				if (!countList.isEmpty() && countList.size() != 0 && !countList.get(0).toString().equals("0")) {
					return false;
				} else {
					return true;
				}
			} else {
				String hql = "select count(*) from MeleteTestModel where moduleId = ? and status=?";
				Object[] parameters = { moduleId, new Long(CodeTable.normal) };
				List<Object> countList = this.findEntity(hql, parameters);
				if (!countList.isEmpty() && countList.size() != 0 && !countList.get(0).toString().equals("0")) {
					return false;
				} else {
					return true;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.sakaiproject.resource.api.course.service.ICourseService#
	 * leafForunByModuleId(java.lang.String)
	 */
	// 根据模块id查询是否有讨论，用于判断模块节点是否为叶子
	public boolean leafForunByModuleId(Long moduleId, boolean showScore) throws Exception {
		try {
			if (showScore) {
				String hql = "select count(*) from MeleteForumModel where moduleId = ? and status=? and isCaculateScore=?";
				Object[] parameters = { moduleId, new Long(CodeTable.normal), new Long(CodeTable.IsCaculateScoreYes) };
				List<Object> countList = this.findEntity(hql, parameters);
				if (!countList.isEmpty() && countList.size() != 0 && !countList.get(0).toString().equals("0")) {
					return false;
				} else {
					return true;
				}
			} else {
				String hql = "select count(*) from MeleteForumModel forum where forum.moduleId = ? and forum.status=?";
				Object[] parameters = { moduleId, new Long(CodeTable.normal) };
				List<Object> countList = this.findEntity(hql, parameters);
				if (!countList.isEmpty() && countList.size() != 0 && !countList.get(0).toString().equals("0")) {
					return false;
				} else {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.sakaiproject.resource.api.course.service.ICourseService#
	 * leafSelftestByModuleId(java.lang.String)
	 */
	// 根据模块id查询是否有前测，用于判断模块节点是否为叶子
	public boolean leafSelftestByModuleId(Long moduleId, boolean showScore) throws Exception {
		try {
			if (showScore) {
				String hql = "select count(*) from MeleteSelfTestModel where moduleId = ? and status=? and isCaculateScore=?";
				Object[] parameters = { moduleId, new Long(CodeTable.normal), new Long(CodeTable.IsCaculateScoreYes) };// 计算成绩
				List<Object> countList = this.findEntity(hql, parameters);
				if (!countList.isEmpty() && countList.size() != 0 && !countList.get(0).toString().equals("0")) {
					return false;
				} else {
					return true;
				}
			} else {
				String hql = "select count(*) from MeleteSelfTestModel where moduleId = ? and status=?";
				Object[] parameters = { moduleId, new Long(CodeTable.normal) };
				List<Object> countList = this.findEntity(hql, parameters);
				if (!countList.isEmpty() && countList.size() != 0 && !countList.get(0).toString().equals("0")) {
					return false;
				} else {
					return true;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.sakaiproject.resource.api.course.service.ICourseService#
	 * getModuleByParentId(java.lang.String)
	 */
	// 根据上级模块id查询下级模块集合，展开模块节点时调用，用于加载下级模块
	public List<MeleteModuleModel> getModuleByParentId(Long parentId, boolean showHide) throws Exception {
		try {
			if (showHide) {
				String hql = "from MeleteModuleModel where parentId=? and status in (?,?) order by idx";
				Object[] parameters = { parentId, new Long(CodeTable.hide), new Long(CodeTable.normal) };
				List<MeleteModuleModel> list = this.findEntity(hql, parameters);
				return list;
			} else {
				String hql = "from MeleteModuleModel where parentId=? and status=? order by idx";
				Object[] parameters = { parentId, new Long(CodeTable.normal) };
				List<MeleteModuleModel> list = this.findEntity(hql, parameters);
				return list;
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.sakaiproject.resource.api.course.service.ICourseService#
	 * getSectionByModuleId(java.lang.String)
	 */
	// 根据模块id查询页集合，展开模块节点时调用，用于加载页
	public List<MeleteSectionModel> getSectionByModuleId(Long moduleId, Long status) throws Exception {
		try {
			String hql = "from MeleteSectionModel where moduleId=? and status=? order by idx";
			Object[] parameters = { moduleId, status };
			List<MeleteSectionModel> list = this.findEntity(hql, parameters);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sakaiproject.resource.api.course.service.ICourseService#getTestByModuleId
	 * (java.lang.String)
	 */
	// 根据模块id查询作业集合，展开模块节点时调用，用于加载作业
	public List<MeleteTestModel> getTestByModuleId(Long moduleId, boolean showScore) throws Exception {
		try {
			if (showScore) {
				String hql = "from MeleteTestModel where moduleId=? and status=? and isCaculateScore=? order by idx";
				Object[] parameters = { moduleId, new Long(CodeTable.normal), new Long(CodeTable.IsCaculateScoreYes) };
				List<MeleteTestModel> list = this.findEntity(hql, parameters);
				return list;
			} else {
				String hql = "from MeleteTestModel where moduleId=? and status=? order by idx";
				Object[] parameters = { moduleId, new Long(CodeTable.normal) };
				List<MeleteTestModel> list = this.findEntity(hql, parameters);
				return list;
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.sakaiproject.resource.api.course.service.ICourseService#
	 * getForumByModuleId(java.lang.String)
	 */
	// 根据模块id查询讨论集合，展开模块节点时调用，用于加载讨论
	public List<MeleteForumModel> getForumByModuleId(Long moduleId, boolean showScore) throws Exception {
		try {
			if (showScore) {
				String hql = "from MeleteForumModel where moduleId=? and status=? and isCaculateScore=? order by idx";
				Object[] parameters = { moduleId, new Long(CodeTable.normal), new Long(CodeTable.IsCaculateScoreYes) };
				List<MeleteForumModel> list = this.findEntity(hql, parameters);
				return list;
			} else {
				String hql = "from MeleteForumModel where moduleId=? and status=? order by idx";
				Object[] parameters = { moduleId, new Long(CodeTable.normal) };
				List<MeleteForumModel> list = this.findEntity(hql, parameters);
				return list;
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.sakaiproject.resource.api.course.service.ICourseService#
	 * getSelftestByModuleId(java.lang.String)
	 */
	// 根据模块id查询前测集合，展开模块节点时调用，用于加载前测
	public List<MeleteSelfTestModel> getSelftestByModuleId(Long moduleId, boolean showScore) throws Exception {
		try {
			if (showScore) {
				String hql = "from MeleteSelfTestModel where moduleId=? and status=? and isCaculateScore=? order by idx";
				Object[] parameters = { moduleId, new Long(CodeTable.normal), new Long(CodeTable.IsCaculateScoreYes) };
				List<MeleteSelfTestModel> list = this.findEntity(hql, parameters);
				return list;
			} else {
				String hql = "from MeleteSelfTestModel where moduleId=? and status=? order by idx";
				Object[] parameters = { moduleId, new Long(CodeTable.normal) };
				List<MeleteSelfTestModel> list = this.findEntity(hql, parameters);
				return list;
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.sakaiproject.resource.api.course.service.ICourseService#
	 * leafTestBySectionId(java.lang.String)
	 */
	// 根据页id查询是否有作业，用于判断页节点是否为叶子
	public boolean leafTestBySectionId(Long sectionId, boolean showScore) throws Exception {
		try {
			if (showScore) {
				String hql = "select count(*) from MeleteTestModel where sectionId = ? and status=? and isCaculateScore=?";
				Object[] parameters = { sectionId, new Long(CodeTable.normal), new Long(CodeTable.IsCaculateScoreYes) };
				List<Object> countList = this.findEntity(hql, parameters);
				if (!countList.isEmpty() && countList.size() != 0 && !countList.get(0).toString().equals("0")) {
					return false;
				} else {
					return true;
				}
			} else {
				String hql = "select count(*) from MeleteTestModel where sectionId = ? and status=?";
				Object[] parameters = { sectionId, new Long(CodeTable.normal) };
				List<Object> countList = this.findEntity(hql, parameters);
				if (!countList.isEmpty() && countList.size() != 0 && !countList.get(0).toString().equals("0")) {
					return false;
				} else {
					return true;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.sakaiproject.resource.api.course.service.ICourseService#
	 * leafForumBySectionId(java.lang.String)
	 */
	// 根据页id查询是否有讨论，用于判断页节点是否为叶子
	public boolean leafForumBySectionId(Long sectionId, boolean showScore) throws Exception {
		try {
			if (showScore) {
				String hql = "select count(*) from MeleteForumModel  where sectionId = ? and status=? and isCaculateScore=?";
				Object[] parameters = { sectionId, new Long(CodeTable.normal), new Long(CodeTable.IsCaculateScoreYes) };
				List<Object> countList = this.findEntity(hql, parameters);
				if (!countList.isEmpty() && countList.size() != 0 && !countList.get(0).toString().equals("0")) {
					return false;
				} else {
					return true;
				}
			} else {
				String hql = "select count(*) from MeleteForumModel forum where forum.sectionId = ? and forum.status=?";
				Object[] parameters = { sectionId, new Long(CodeTable.normal) };
				List<Object> countList = this.findEntity(hql, parameters);
				if (!countList.isEmpty() && countList.size() != 0 && !countList.get(0).toString().equals("0")) {
					return false;
				} else {
					return true;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.sakaiproject.resource.api.course.service.ICourseService#
	 * leafSelftestBySectionId(java.lang.String)
	 */
	// 根据页id查询是否有前测，用于判断页节点是否为叶子
	public boolean leafSelftestBySectionId(Long sectionId, boolean showScore) throws Exception {
		try {
			if (showScore) {
				String hql = "select count(*) from MeleteSelfTestModel where sectionId = ? and status=? and isCaculateScore=?";
				Object[] parameters = { sectionId, new Long(CodeTable.normal), new Long(CodeTable.IsCaculateScoreYes) };
				List<Object> countList = this.findEntity(hql, parameters);
				if (!countList.isEmpty() && countList.size() != 0 && !countList.get(0).toString().equals("0")) {
					return false;
				} else {
					return true;
				}
			} else {
				String hql = "select count(*) from MeleteSelfTestModel where sectionId = ? and status=?";
				Object[] parameters = { sectionId, new Long(CodeTable.normal) };
				List<Object> countList = this.findEntity(hql, parameters);
				if (!countList.isEmpty() && countList.size() != 0 && !countList.get(0).toString().equals("0")) {
					return false;
				} else {
					return true;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.sakaiproject.resource.api.course.service.ICourseService#
	 * getTestBySectionId(java.lang.String)
	 */
	// 根据页id查询作业集合，展开页节点时调用，用于加载作业
	public List<MeleteTestModel> getTestBySectionId(Long sectionId, boolean showScore) throws Exception {
		try {
			if (showScore) {
				String hql = "from MeleteTestModel where sectionId=? and status=? and isCaculateScore=?";
				Object[] parameters = { sectionId, new Long(CodeTable.normal), new Long(CodeTable.IsCaculateScoreYes) };
				List<MeleteTestModel> list = this.findEntity(hql, parameters);
				return list;
			} else {
				String hql = "from MeleteTestModel where sectionId=? and status=?";
				Object[] parameters = { sectionId, new Long(CodeTable.normal) };
				List<MeleteTestModel> list = this.findEntity(hql, parameters);
				return list;
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.sakaiproject.resource.api.course.service.ICourseService#
	 * getForumBySectionId(java.lang.String)
	 */
	// 根据页id查询讨论集合，展开页节点时调用，用于加载讨论
	public List<MeleteForumModel> getForumBySectionId(Long sectionId, boolean showScore) throws Exception {
		try {
			if (showScore) {
				String hql = "from MeleteForumModel where sectionId=? and status=? and isCaculateScore=?";
				Object[] parameters = { sectionId, new Long(CodeTable.normal), new Long(CodeTable.IsCaculateScoreYes) };
				List<MeleteForumModel> list = this.findEntity(hql, parameters);
				return list;
			} else {
				String hql = "from MeleteForumModel where sectionId=? and status=?";
				Object[] parameters = { sectionId, new Long(CodeTable.normal) };
				List<MeleteForumModel> list = this.findEntity(hql, parameters);
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.sakaiproject.resource.api.course.service.ICourseService#
	 * getSelftestBySectionId(java.lang.String)
	 */
	// 根据页id查询前测集合，展开页节点时调用，用于加载前测
	public List<MeleteSelfTestModel> getSelftestBySectionId(Long sectionId, boolean showScore) throws Exception {
		try {
			if (showScore) {
				String hql = "from MeleteSelfTestModel where sectionId=? and status=? and isCaculateScore=?";
				Object[] parameters = { sectionId, new Long(CodeTable.normal), new Long(CodeTable.IsCaculateScoreYes) };
				List<MeleteSelfTestModel> list = this.findEntity(hql, parameters);
				return list;
			} else {
				String hql = "from MeleteSelfTestModel where sectionId=? and status=?";
				Object[] parameters = { sectionId, new Long(CodeTable.normal) };
				List<MeleteSelfTestModel> list = this.findEntity(hql, parameters);
				return list;
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sakaiproject.resource.api.course.service.ICourseService#getCourseById
	 * (java.lang.String)
	 */
	// 根据课程id获取课程持久类
	public MeleteCourseModel getCourseById(String courseId) throws Exception {
		try {
			MeleteCourseModel course = (MeleteCourseModel) this.findEntityById(MeleteCourseModel.class, courseId);
			return course;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sakaiproject.resource.api.course.service.ICourseService#updateCourse
	 * (org.sakaiproject.resource.api.course.model.MeleteCourseModel)
	 */
	// 更新课程信息
	public void updateCourse(MeleteCourseModel course) throws Exception {
		try {
			this.updateEntity(course);
			// 在缓存中更新课程信息
			CacheUtil.getInstance().updateCourse(course);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sakaiproject.resource.api.course.service.ICourseService#getCoursewareBox
	 * ()
	 */
	// 获取课件资源json串，用于前台加载下拉列表的数据源
	public String getCoursewareBox() throws Exception {
		try {
			StringBuffer sb = new StringBuffer("[");
			String hql = "from CoursewareModel ware where ware.status=1 order by ware.courseName";
			List<CoursewareModel> list = this.findEntity(hql);
			for (CoursewareModel ware : list) {
				sb.append("['").append(ware.getId()).append("','").append(ware.getCourseName()).append("'],");
			}
			sb = sb.deleteCharAt(sb.length() - 1).append("]");
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sakaiproject.resource.api.course.service.ICourseService#editModule
	 * (org.sakaiproject.resource.api.course.model.MeleteModuleModel)
	 */
	// 保存更新模块
	public String editModule(MeleteModuleModel module) throws Exception {
		try {
			if (module.getId() == null) {
				this.createEntity(module);
				CacheUtil.getInstance().addModule(module);// 向所有学生缓存中增加模块
			} else {
				this.updateEntity(module);
				CacheUtil.getInstance().updateModule(module);// 更改所有学生缓存中对应的模块信息
			}
			return module.getId().toString();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sakaiproject.resource.api.course.service.ICourseService#editSection
	 * (org.sakaiproject.resource.api.course.model.MeleteSectionModel)
	 */
	// 保存更新页信息
	public String editSection(MeleteSectionModel section) throws Exception {
		try {
			if (section.getId() == null) {
				String id = this.createEntity(section);
				CacheUtil.getInstance().addSection(section);// 向所有学生缓存中增加页
				return id;
			} else {
				this.updateEntity(section);
				CacheUtil.getInstance().updateSection(section);// 更改所有学生缓存中的对应的页信息
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sakaiproject.resource.api.course.service.ICourseService#editTest(
	 * org.sakaiproject.resource.api.course.model.MeleteTestModel)
	 */
	// 编辑保存作业
	public String editTest(MeleteTestModel test, String editType) throws Exception {
		try {
			// 判断同一级是否有重名
			String name = test.getName();
			Long moduleId = test.getModuleId();

			if (moduleId != null) {
				String hql = "select id from MeleteTestModel where moduleId=? and name=? and status=?";
				Object[] parameters = { moduleId, name, new Long(CodeTable.normal) };
				List countList = this.findEntity(hql, parameters);
				if (CodeTable.addType.equals(editType) && !countList.isEmpty() && countList.size() > 0) {// 做添加操作时
					throw new Exception("重名");
				} else if (CodeTable.updateType.equals(editType) && !countList.isEmpty() && countList.size() > 0) {// 做更改操作时
					if (test.getId().longValue() != new Long(countList.get(0).toString()).longValue()) {
						// 当查到的ID 不为 test ID 是重名
						throw new Exception("重名");
					}
				}

			} else {
				Long sectionId = test.getSectionId();
				String hql = "select id from MeleteTestModel where sectionId=? and name=? and status=?";
				Object[] parameters = { sectionId, name, new Long(CodeTable.normal) };
				List countList = this.findEntity(hql, parameters);
				if (CodeTable.addType.equals(editType) && !countList.isEmpty() && countList.size() > 0) {
					throw new Exception("重名");
				} else if (CodeTable.updateType.equals(editType) && !countList.isEmpty() && countList.size() > 0) {// 做更改操作时
					if (test.getId().longValue() != new Long(countList.get(0).toString()).longValue()) {
						// 当查到的ID 不为 test ID 是重名
						throw new Exception("重名");
					}
				}
			}
			if (test.getId() == null) {
				test.setRatio(new Float(0));
				this.createEntity(test);
				CacheUtil.getInstance().addTest(test);// 向所有学生缓存中增加作业
			} else {
				this.updateEntity(test);
				CacheUtil.getInstance().updateTest(test);// 更改所有学生缓存中对应作业信息
			}
			return test.getId().toString();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sakaiproject.resource.api.course.service.ICourseService#editSelfTest
	 * (org.sakaiproject.resource.api.course.model.MeleteSelfTestModel)
	 */
	// 保存修改前侧
	public String editSelfTest(MeleteSelfTestModel test, String editType) throws Exception {
		try {
			// 判断同一级是否有重名
			String name = test.getName();
			Long moduleId = test.getModuleId();
			// 前测所属节点类型为"模块"
			if (moduleId != null) {
				String hql = "select id from MeleteSelfTestModel where moduleId=? and name=? and status=?";
				Object[] parameters = { moduleId, name, new Long(CodeTable.normal) };
				List countList = this.findEntity(hql, parameters);
				if (CodeTable.addType.equals(editType) && !countList.isEmpty() && countList.size() > 0) {// 做添加操作时
					throw new Exception("重名");
				} else if (CodeTable.updateType.equals(editType) && !countList.isEmpty() && countList.size() > 0) {// 做更改操作时
					if (test.getId().longValue() != new Long(countList.get(0).toString()).longValue()) {
						// 当查到的ID 不为 test ID 是重名
						throw new Exception("重名");
					}
				}
				// 前测所属节点类型为"页"
			} else {
				Long sectionId = test.getSectionId();
				String hql = "select id from MeleteSelfTestModel where sectionId=? and name=? and status=?";
				Object[] parameters = { sectionId, name, new Long(CodeTable.normal) };
				List countList = this.findEntity(hql, parameters);
				if (CodeTable.addType.equals(editType) && !countList.isEmpty() && countList.size() > 0) {// 做添加操作时
					throw new Exception("重名");
				} else if (CodeTable.updateType.equals(editType) && !countList.isEmpty() && countList.size() > 0) {// 做更改操作时
					if (test.getId().longValue() != new Long(countList.get(0).toString()).longValue()) {
						// 当查到的ID 不为 test ID 是重名
						throw new Exception("重名");
					}
				}
			}
			if (test.getId() == null) {
				String id = this.createEntity(test);// 向所有学生缓存中增加作业
				CacheUtil.getInstance().addSelftest(test);
				if (moduleId != null) {
					String hql = "update MeleteModuleModel set moduleSelftest=? where id=? ";
					Object[] param = { id, moduleId };
					this.updateEntity(hql, param);
					MeleteModuleModel module = (MeleteModuleModel) this.findEntityById(MeleteModuleModel.class,
							moduleId);
					module.setModuleSelftest(id);
					CacheUtil.getInstance().updateModule(module);// 更新所有学生缓存中对应的模块信息
				} else {
					String hql = "update MeleteSectionModel set sectionSelftest=? where id=? ";
					Object[] param = { id, test.getSectionId() };
					this.updateEntity(hql, param);
					MeleteSectionModel section = (MeleteSectionModel) this.findEntityById(MeleteSectionModel.class,
							test.getSectionId());
					section.setSectionSelftest(id);
					CacheUtil.getInstance().updateSection(section);// 更新所有学生缓存中对应的页信息
				}
			} else {
				this.updateEntity(test);
				CacheUtil.getInstance().updateSelftest(test);// 更新所有学生缓存中对应的前测信息
			}
			return test.getId().toString();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sakaiproject.resource.api.course.service.ICourseService#editForum
	 * (org.sakaiproject.resource.api.course.model.MeleteForumModel)
	 */
	// 保存编辑讨论
	public String editForum(MeleteForumModel forum) throws Exception {
		try {
			// 判断同一级是否有重名
			String topicId = forum.getTopicId();
			Long moduleId = forum.getModuleId();
			if (moduleId != null) {
				String hql = "select id from MeleteForumModel where moduleId=? and topicId=? and status=?";
				Object[] parameters = { moduleId, topicId, new Long(CodeTable.normal) };
				List countList = this.findEntity(hql, parameters);
				if (!countList.isEmpty() && countList.size() > 0
						&& !countList.get(0).toString().equals(forum.getId().toString())) {
					throw new Exception("重名");
				}
			} else {
				Long sectionId = forum.getSectionId();
				String hql = "select id from MeleteForumModel where sectionId=? and topicId=?  and status=?";
				Object[] parameters = { sectionId, topicId, new Long(CodeTable.normal) };
				List countList = this.findEntity(hql, parameters);
				if (!countList.isEmpty() && countList.size() > 0
						&& !countList.get(0).toString().equals(forum.getId().toString())) {
					throw new Exception("重名");
				}
			}
			if (forum.getId() == null) {
				forum.setRatio(new Float(0));
				this.createEntity(forum);
				CacheUtil.getInstance().addForum(forum);// 向所有学生缓存中增加讨论
			} else {
				this.updateEntity(forum);
				CacheUtil.getInstance().updateForum(forum);// 更改所有学生缓存对应的讨论信息
			}
			return forum.getId().toString();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sakaiproject.resource.api.course.service.ICourseService#countScoreActNum
	 * (java.lang.String)
	 */
	// 根据课程id查询所有需要计算平时成绩的活动的总个数
	public Long countScoreActNum(String courseId) throws Exception {
		try {
			Object[] parameters = { courseId, new Long(CodeTable.IsCaculateScoreYes), new Long(CodeTable.normal) };
			Long sum = new Long(0);
			String hql = "select count(*) from MeleteModuleModel module,MeleteTestModel test "
					+ "where module.courseId=? and module.id=test.moduleId "
					+ "and test.isCaculateScore=? and test.status=? ";
			List countList = this.findEntity(hql, parameters);
			if (!countList.isEmpty() && countList.size() > 0) {
				sum += new Long(countList.get(0).toString());
			}
			hql = "select count(*) from MeleteModuleModel module,MeleteSectionModel section,MeleteTestModel test "
					+ "where module.courseId=? and module.id=section.moduleId and section.id=test.sectionId "
					+ "and test.isCaculateScore=? and test.status=? ";
			countList = this.findEntity(hql, parameters);
			if (!countList.isEmpty() && countList.size() > 0) {
				sum += new Long(countList.get(0).toString());
			}
			hql = "select count(*) from MeleteModuleModel module,MeleteSelfTestModel test "
					+ "where module.courseId=? and module.id=test.moduleId "
					+ "and test.isCaculateScore=? and test.status=? ";
			countList = this.findEntity(hql, parameters);
			if (!countList.isEmpty() && countList.size() > 0) {
				sum += new Long(countList.get(0).toString());
			}
			hql = "select count(*) from MeleteModuleModel module,MeleteSectionModel section,MeleteSelfTestModel test "
					+ "where module.courseId=? and module.id=section.moduleId and section.id=test.sectionId "
					+ "and test.isCaculateScore=? and test.status=? ";
			countList = this.findEntity(hql, parameters);
			if (!countList.isEmpty() && countList.size() > 0) {
				sum += new Long(countList.get(0).toString());
			}
			hql = "select count(*) from MeleteModuleModel module,MeleteForumModel test "
					+ "where module.courseId=? and module.id=test.moduleId "
					+ "and test.isCaculateScore=? and test.status=? ";
			countList = this.findEntity(hql, parameters);
			if (!countList.isEmpty() && countList.size() > 0) {
				sum += new Long(countList.get(0).toString());
			}
			hql = "select count(*) from MeleteModuleModel module,MeleteSectionModel section,MeleteForumModel test "
					+ "where module.courseId=? and module.id=section.moduleId and section.id=test.sectionId "
					+ "and test.isCaculateScore=? and test.status=? ";
			countList = this.findEntity(hql, parameters);
			if (!countList.isEmpty() && countList.size() > 0) {
				sum += new Long(countList.get(0).toString());
			}
			return sum;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sakaiproject.resource.api.course.service.ICourseService#getActList
	 * (java.lang.String)
	 */
	// 根据课程id获取所有计算平时成绩的活动集合
	public List<Object> getActList(String courseId) throws Exception {
		try {
			List<Object> returnList = new ArrayList<Object>();
			Object[] parameters = { courseId, new Long(CodeTable.IsCaculateScoreYes), new Long(CodeTable.normal) };
			String hql = "select test from MeleteModuleModel module,MeleteTestModel test "
					+ "where module.courseId=? and module.id=test.moduleId "
					+ "and test.isCaculateScore=? and test.status=?";
			returnList.addAll(this.findEntity(hql, parameters));

			hql = "select test from MeleteModuleModel module,MeleteSectionModel section,MeleteTestModel test "
					+ "where module.courseId=? and module.id=section.moduleId and section.id=test.sectionId "
					+ "and test.isCaculateScore=? and test.status=?";
			returnList.addAll(this.findEntity(hql, parameters));

			hql = "select test from MeleteModuleModel module,MeleteSelfTestModel test "
					+ "where module.courseId=? and module.id=test.moduleId "
					+ "and test.isCaculateScore=? and test.status=?";
			returnList.addAll(this.findEntity(hql, parameters));
			hql = "select test from MeleteModuleModel module,MeleteSectionModel section,MeleteSelfTestModel test "
					+ "where module.courseId=? and module.id=section.moduleId and section.id=test.sectionId "
					+ "and test.isCaculateScore=? and test.status=?";
			returnList.addAll(this.findEntity(hql, parameters));
			hql = "select test from MeleteModuleModel module,MeleteForumModel test "
					+ "where module.courseId=? and module.id=test.moduleId "
					+ "and test.isCaculateScore=? and test.status=?";
			returnList.addAll(this.findEntity(hql, parameters));
			hql = "select test from MeleteModuleModel module,MeleteSectionModel section,MeleteForumModel test "
					+ "where module.courseId=? and module.id=section.moduleId and section.id=test.sectionId "
					+ "and test.isCaculateScore=? and test.status=?";
			returnList.addAll(this.findEntity(hql, parameters));
			return returnList;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sakaiproject.resource.api.course.service.ICourseService#saveRatio
	 * (java.lang.String[], java.lang.String[], java.lang.String[],
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	// 保存活动所占成绩百分比
	public void saveRatio(String[] id, String[] type, String[] ratio, String impressionScore, String impressionType,
			String courseId) throws Exception {
		try {
			StringBuffer courseHql = new StringBuffer("");
			// 如果分数为Null或者为空字符串就不更新
			if (StringUtils.isNotBlank(impressionScore)) {
				courseHql.append("update MeleteCourseModel set impressionScore=?, impressionType=? where id=? ");
				Object[] courseParam = { new Float(impressionScore), new Long(impressionType), courseId };
				this.updateEntity(courseHql.toString(), courseParam);
			}

			for (int i = 0; i < id.length; i++) {
				String hql = "";
				if (type[i].equals(CodeTable.test)) {// 作业
					hql = "update MeleteTestModel set ratio=? where id=? ";
					Object[] param = { new Float(ratio[i]), new Long(id[i]) };
					this.updateEntity(hql, param);
					MeleteTestModel test = (MeleteTestModel) this
							.findEntityById(MeleteTestModel.class, new Long(id[i]));
					test.setRatio(new Float(ratio[i]));
					CacheUtil.getInstance().updateTest(test);// 更新所有学生缓存对应的作业信息
				} else if (type[i].equals(CodeTable.selftest)) {// 前测
					hql = "update MeleteSelfTestModel set ratio=? where id=? ";
					Object[] param = { new Float(ratio[i]), new Long(id[i]) };
					this.updateEntity(hql, param);
					MeleteSelfTestModel selfTest = (MeleteSelfTestModel) findEntityById(MeleteSelfTestModel.class,
							new Long(id[i]));
					selfTest.setRatio(new Float(ratio[i]));
					CacheUtil.getInstance().updateSelftest(selfTest);// 更新所有学生缓存对应的前测信息
				} else if (type[i].equals(CodeTable.forum)) {// 论坛
					hql = "update MeleteForumModel set ratio=? where id=? ";
					Object[] param = { new Float(ratio[i]), new Long(id[i]) };
					this.updateEntity(hql, param);
					MeleteForumModel forum = (MeleteForumModel) this.findEntityById(MeleteForumModel.class, new Long(
							id[i]));
					forum.setRatio(new Float(ratio[i]));
					CacheUtil.getInstance().updateForum(forum);// 更新所有学生缓存对应的讨论信息
				} else if (type[i].equals(CodeTable.file)) {// 资源
					hql = "update ResourceFileModel set ratio=? where id=? ";
					Object[] param = { new Float(ratio[i]), id[i] };
					this.updateEntity(hql, param);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sakaiproject.resource.api.course.service.ICourseService#changeModelStatus
	 * (java.lang.Class, java.lang.String, java.lang.String)
	 */
	// 修改某一持久化类的状态
	public String changeModelStatus(Class clazz, Long id, Long status) throws Exception {
		try {
			Object obj = this.findEntityById(clazz, id);

			String clazzName = clazz.getName();
			String hql = "update " + clazzName + " set status=? where id=? ";
			Object[] param = { status, id };
			this.updateEntity(hql, param);

			// 更新所有学生缓存中的相应信息
			if (obj.getClass() == MeleteModuleModel.class) {
				MeleteModuleModel module = (MeleteModuleModel) obj;
				module.setStatus(status);
				CacheUtil.getInstance().updateModule(module);
			} else if (obj.getClass() == MeleteForumModel.class) {
				MeleteForumModel forum = (MeleteForumModel) obj;
				forum.setStatus(status);
				CacheUtil.getInstance().updateForum(forum);
			} else if (obj.getClass() == MeleteSelfTestModel.class) {
				MeleteSelfTestModel selfTest = (MeleteSelfTestModel) obj;
				selfTest.setStatus(status);
				CacheUtil.getInstance().updateSelftest(selfTest);
			} else if (obj.getClass() == MeleteTestModel.class) {
				MeleteTestModel test = (MeleteTestModel) obj;
				test.setStatus(status);
				CacheUtil.getInstance().updateTest(test);
			}
			return "{success: true}";
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sakaiproject.resource.api.course.service.ICourseService#getModelById
	 * (java.lang.Class, java.lang.String)
	 */
	// 根据类名和id获取某个持久化类
	public Object getModelById(Class clazz, Long id) throws Exception {
		try {
			return this.findEntityById(clazz, id);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.sakaiproject.resource.api.course.service.ICourseService#
	 * countRequiredByCourseId(java.lang.String)
	 */
	// 根据课程id查询选修模块的总个数
	public Long countRequiredByCourseId(String courseId) throws Exception {
		try {
			String hql = "select count(*) from MeleteModuleModel where "
					+ "status=? and courseId=? and required=? and parentId is null ";
			Object[] param = { new Long(CodeTable.normal), courseId, new Long(CodeTable.required) };
			List countList = this.findEntity(hql, param);
			if (countList.isEmpty() || countList.size() == 0) {
				return new Long("0");
			} else {
				return new Long(countList.get(0).toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.sakaiproject.resource.api.course.service.ICourseService#
	 * countElectiveByCourseId(java.lang.String)
	 */
	// 根据课程id查询必修模块的总个数
	public Long countElectiveByCourseId(String courseId) throws Exception {
		try {
			String hql = "select count(*) from MeleteModuleModel where "
					+ "status=? and courseId=? and required=? and parentId is null ";
			Object[] param = { new Long(CodeTable.normal), courseId, new Long(CodeTable.elective) };
			List countList = this.findEntity(hql, param);
			if (countList.isEmpty() || countList.size() == 0) {
				return new Long("0");
			} else {
				return new Long(countList.get(0).toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sakaiproject.resource.api.course.service.ICourseService#dragNode(
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	// 保存拖拽结果
	public String dragNode(String point, String origId, String origType, String destId, String destType)
			throws Exception {
		try {
			String res = "{success:true}";
			if (point.equals("append")) {// 追加子节点
				if (origType.equals("2") && destType.equals("2")) {// 拖拽的模块节点，目标为模块节点
					// 被拖拽的节点
					MeleteModuleModel origNode = (MeleteModuleModel) this.getModelById(MeleteModuleModel.class,
							new Long(origId));
					Long oldParentId = origNode.getParentId();// 拖拽前的父节点
					Long oldIdx = origNode.getIdx();// 拖拽前的序号
					origNode.setParentId(new Long(destId));// 拖拽后的父节点
					Long maxIdx = this.getMaxModuleIdxByParentId(new Long(destId));
					origNode.setIdx(maxIdx + 1);
					this.editModule(origNode);
					MeleteModuleModel destNode = (MeleteModuleModel) this.getModelById(MeleteModuleModel.class,
							new Long(destId));
					destNode.setChildType(CodeTable.module);// 设置destNode的子节点类型为节点
					this.editModule(destNode);// 更新 destNode

					if (oldParentId != null) {
						String hql = "update MeleteModuleModel set idx=idx-1 where idx>? and parentId=?";
						Object[] param = { oldIdx, oldParentId };
						this.updateEntity(hql, param);
						List<MeleteModuleModel> moduleList = this.getModuleByParentId(oldParentId, true);
						if (moduleList != null && !moduleList.isEmpty()) {
							for (int i = 0; i < moduleList.size(); i++) {
								if (moduleList.get(i).getIdx().longValue() > oldIdx) {
									moduleList.get(i).setIdx(new Long(moduleList.get(i).getIdx().longValue() - 1));
									CacheUtil.getInstance().updateModule(moduleList.get(i));// 更新所有学生的缓存中对应的模块
								}
							}
						} else {// 若父节点下无子节点则将父节点的childType属性设为null
							MeleteModuleModel oldPModule = (MeleteModuleModel) this.getModelById(
									MeleteModuleModel.class, new Long(oldParentId));
							oldPModule.setChildType(null);
							this.editModule(oldPModule);
						}
					} else {
						String hql = "update MeleteModuleModel set idx=idx-1 where idx>? and parentId is null and courseId=?";
						Object[] param = { oldIdx, origNode.getCourseId() };
						this.updateEntity(hql, param);
						List<MeleteModuleModel> moduleList = this.getModuleByCourseId(origNode.getCourseId(), true);
						for (int i = 0; i < moduleList.size(); i++) {
							if (moduleList.get(i).getIdx().longValue() > oldIdx) {
								moduleList.get(i).setIdx(new Long(moduleList.get(i).getIdx().longValue() - 1));
								CacheUtil.getInstance().updateModule(moduleList.get(i));// 更新所有学生的缓存中对应的模块
							}
						}
					}
					if (oldParentId != new Long(destId)) {// 父节点发生改变，修改拖拽前后的父节点通过条件
						this.updateModuleCondition(destId, CodeTable.module);
						if (oldParentId != null) {
							this.updateModuleCondition(oldParentId.toString(), CodeTable.module);
						} else {
							this.updateCourseCondition(origNode.getCourseId());
						}
					}

					this.adjustXML(origNode, destNode, point);// 调整对应的课件描述文件
				} else if (origType.equals("2") && destType.equals("1")) {// 拖拽的模块节点，目标为课程节点
					MeleteModuleModel origNode = (MeleteModuleModel) this.getModelById(MeleteModuleModel.class,
							new Long(origId));
					Long oldParentId = origNode.getParentId();
					Long oldIdx = origNode.getIdx();
					origNode.setParentId(null);
					Long maxIdx = this.getMaxModuleIdxByCourseId(origNode.getCourseId());
					origNode.setIdx(maxIdx + 1);
					this.editModule(origNode);

					String hql = "update MeleteModuleModel set idx=idx-1 where idx>? and parentId=?";
					Object[] param = { oldIdx, oldParentId };
					this.updateEntity(hql, param);
					List<MeleteModuleModel> moduleList = this.getModuleByParentId(oldParentId, true);
					if (moduleList != null && !moduleList.isEmpty()) {
						for (int i = 0; i < moduleList.size(); i++) {
							if (moduleList.get(i).getIdx().longValue() > oldIdx) {
								moduleList.get(i).setIdx(moduleList.get(i).getIdx().longValue() - 1);
								CacheUtil.getInstance().updateModule(moduleList.get(i));// 更新所有学生的缓存中对应的模块
							}
						}
					} else {// 若父节点下无子节点则将父节点的childType属性设为null
						MeleteModuleModel oldPModule = (MeleteModuleModel) this.getModelById(MeleteModuleModel.class,
								new Long(oldParentId));
						oldPModule.setChildType(null);
						this.editModule(oldPModule);
					}

					if (oldParentId != null) {
						this.updateModuleCondition(oldParentId.toString(), CodeTable.module);
						this.updateCourseCondition(origNode.getCourseId());
					}

					this.adjustXML(origNode, null, point);// 调整对应的课件描述文件
				} else if (origType.equals("3") && destType.equals("2")) {// 拖拽的页节点，目标为模块节点
					MeleteSectionModel origNode = (MeleteSectionModel) this.getModelById(MeleteSectionModel.class,
							new Long(origId));
					Long oldModuleId = origNode.getModuleId();
					Long oldIdx = origNode.getIdx();
					origNode.setModuleId(new Long(destId));
					Long maxIdx = this.getMaxSectionIdxByModuleId(new Long(destId));
					origNode.setIdx(maxIdx + 1);
					this.editSection(origNode);
					MeleteModuleModel destNode = (MeleteModuleModel) this.getModelById(MeleteModuleModel.class,
							new Long(destId));
					destNode.setChildType(CodeTable.section);// 将ChildType设置为页
					this.editModule(destNode);
					String hql = "update MeleteSectionModel set idx=idx-1 where idx>? and moduleId=?";
					Object[] param = { oldIdx, oldModuleId };
					this.updateEntity(hql, param);
					List<MeleteSectionModel> sectionList = this.getSectionByModuleId(oldModuleId,
							Long.valueOf(CodeTable.normal));
					if (sectionList != null && !sectionList.isEmpty()) {
						for (int i = 0; i < sectionList.size(); i++) {
							if (sectionList.get(i).getIdx().longValue() > oldIdx) {
								sectionList.get(i).setIdx(sectionList.get(i).getIdx().longValue() - 1);
								CacheUtil.getInstance().updateSection(sectionList.get(i));// 更新所有学生的缓存中对应的页
							}
						}
					} else {// 若父节点下无子节点则将父节点的childType属性设为null
						MeleteModuleModel oldPModule = (MeleteModuleModel) this.getModelById(MeleteModuleModel.class,
								oldModuleId);
						oldPModule.setChildType(null);
						this.editModule(oldPModule);
					}
					if (oldModuleId != new Long(destId)) {
						this.updateModuleCondition(oldModuleId.toString(), CodeTable.section);
						this.updateModuleCondition(destId, CodeTable.section);
					}
					this.adjustXML(origNode, destNode, point);// 调整对应的课件描述文件
				} else {
					return "{success:false}";
				}
			} else if (point.equals("above")) {// 插入上一个兄弟节点
				if (origType.equals("2") && destType.equals("2")) {// 拖拽的模块节点，目标为模块节点
					MeleteModuleModel origNode = (MeleteModuleModel) this.getModelById(MeleteModuleModel.class,
							new Long(origId));
					Long oldParentId = origNode.getParentId();
					Long oldIdx = origNode.getIdx();

					MeleteModuleModel destNode = (MeleteModuleModel) this.getModelById(MeleteModuleModel.class,
							new Long(destId));
					Long destIdx = destNode.getIdx();
					if (destNode.getParentId() != null) {
						String hql = "update MeleteModuleModel set idx=idx+1 where idx>=? and parentId=?";
						Object[] param = { destIdx, destNode.getParentId() };
						this.updateEntity(hql, param);
						List<MeleteModuleModel> moduleList = this.getModuleByParentId(destNode.getParentId(), true);
						for (int i = 0; i < moduleList.size(); i++) {
							if (moduleList.get(i).getIdx().longValue() >= destIdx) {
								moduleList.get(i).setIdx(moduleList.get(i).getIdx().longValue() + 1);
								CacheUtil.getInstance().updateModule(moduleList.get(i));// 更新所有学生的缓存中对应的模块
							}
						}

					} else {
						String hql = "update MeleteModuleModel set idx=idx+1 where idx>=? and parentId is null and courseId=?";
						Object[] param = { destIdx, destNode.getCourseId() };
						this.updateEntity(hql, param);
						List<MeleteModuleModel> moduleList = this.getModuleByCourseId(destNode.getCourseId(), true);
						for (int i = 0; i < moduleList.size(); i++) {
							if (moduleList.get(i).getIdx().longValue() >= destIdx) {
								moduleList.get(i).setIdx(moduleList.get(i).getIdx().longValue() + 1);
								CacheUtil.getInstance().updateModule(moduleList.get(i));// 更新所有学生的缓存中对应的模块
							}
						}
					}
					origNode.setIdx(destIdx);
					origNode.setParentId(destNode.getParentId());
					this.editModule(origNode);

					if (oldParentId != null) {
						String hql = "update MeleteModuleModel set idx=idx-1 where idx>? and parentId=?";
						Object[] param = { oldIdx, oldParentId };
						this.updateEntity(hql, param);
						List<MeleteModuleModel> moduleList = this.getModuleByParentId(oldParentId, true);
						if (moduleList != null && !moduleList.isEmpty()) {// 若父节点下还有子节点
							for (int i = 0; i < moduleList.size(); i++) {
								if (moduleList.get(i).getIdx().longValue() > oldIdx) {
									moduleList.get(i).setIdx(moduleList.get(i).getIdx().longValue() - 1);
									CacheUtil.getInstance().updateModule(moduleList.get(i));// 更新所有学生的缓存中对应的模块
								}
							}
						} else {// 若父节点下无子节点则将父节点的childType属性设为null
							MeleteModuleModel oldPModule = (MeleteModuleModel) this.getModelById(
									MeleteModuleModel.class, new Long(oldParentId));
							oldPModule.setChildType(null);
							this.updateEntity(oldPModule);
							CacheUtil.getInstance().updateModule(oldPModule);// 更新所有学生的缓存中对应的模块
						}

					} else {
						String hql = "update MeleteModuleModel set idx=idx-1 where idx>? and parentId is null and courseId=?";
						Object[] param = { oldIdx, origNode.getCourseId() };
						this.updateEntity(hql, param);
						List<MeleteModuleModel> moduleList = this.getModuleByCourseId(origNode.getCourseId(), true);
						for (int i = 0; i < moduleList.size(); i++) {
							if (moduleList.get(i).getIdx().longValue() > oldIdx) {
								moduleList.get(i).setIdx(moduleList.get(i).getIdx().longValue() - 1);
								CacheUtil.getInstance().updateModule(moduleList.get(i));// 更新所有学生的缓存中对应的模块
							}
						}
					}
					if (oldParentId != destNode.getParentId()) {
						if (destNode.getParentId() != null) {
							this.updateModuleCondition(destNode.getParentId().toString(), CodeTable.module);
						}

						if (oldParentId != null) {
							this.updateModuleCondition(oldParentId.toString(), CodeTable.module);
						} else {
							this.updateCourseCondition(origNode.getCourseId());
						}
					}
					this.adjustXML(origNode, destNode, point);// 调整课件描述文件中的顺序
				} else if (origType.equals("3") && destType.equals("3")) {// 拖拽的页节点，目标为页节点
					MeleteSectionModel origNode = (MeleteSectionModel) this.getModelById(MeleteSectionModel.class,
							new Long(origId));
					MeleteSectionModel destNode = (MeleteSectionModel) this.getModelById(MeleteSectionModel.class,
							new Long(destId));
					Long oldModuleId = origNode.getModuleId();
					Long oldIdx = origNode.getIdx();
					Long destIdx = destNode.getIdx();

					String hql = "update MeleteSectionModel set idx=idx+1 where idx>=? and moduleId=?";
					Object[] param = { destIdx, destNode.getModuleId() };
					this.updateEntity(hql, param);
					List<MeleteSectionModel> sectionList = this.getSectionByModuleId(destNode.getModuleId(),
							Long.valueOf(CodeTable.normal));
					for (int i = 0; i < sectionList.size(); i++) {
						if (sectionList.get(i).getIdx().longValue() >= destIdx) {
							sectionList.get(i).setIdx(sectionList.get(i).getIdx().longValue() + 1);
							CacheUtil.getInstance().updateSection(sectionList.get(i));// 更新所有学生的缓存中对应的模块
						}
					}
					origNode.setModuleId(destNode.getModuleId());
					origNode.setIdx(destIdx);
					this.editSection(origNode);

					hql = "update MeleteSectionModel set idx=idx-1 where idx>? and moduleId=?";
					Object[] oparam = { oldIdx, oldModuleId };
					List<MeleteSectionModel> oldSectionList = this.getSectionByModuleId(oldModuleId,
							Long.valueOf(CodeTable.normal));
					this.updateEntity(hql, oparam);
					if (oldSectionList != null && !oldSectionList.isEmpty()) {// 若拖拽前的父节点下还有子节点
						for (int i = 0; i < oldSectionList.size(); i++) {
							if (oldSectionList.get(i).getIdx().longValue() > oldIdx) {
								oldSectionList.get(i).setIdx(oldSectionList.get(i).getIdx().longValue() - 1);
								CacheUtil.getInstance().updateSection(oldSectionList.get(i));// 更新所有学生的缓存中对应的模块
							}
						}
					} else {// 若拖拽之前的模块无子节点则将父节点的childType设为null
						MeleteModuleModel oldPModule = (MeleteModuleModel) this.getModelById(MeleteModuleModel.class,
								new Long(oldModuleId));
						oldPModule.setChildType(null);
						this.editModule(oldPModule);
					}

					if (oldModuleId != destNode.getModuleId()) {
						this.updateModuleCondition(oldModuleId.toString(), CodeTable.section);
						this.updateModuleCondition(destNode.getModuleId().toString(), CodeTable.section);
					}
					this.adjustXML(origNode, destNode, point);
				} else {
					return "{success:false}";
				}
			} else if (point.equals("below")) {// 插入下一个兄弟节点
				if (origType.equals("2") && destType.equals("2")) {// 拖拽的模块节点，目标为模块节点
					MeleteModuleModel origNode = (MeleteModuleModel) this.getModelById(MeleteModuleModel.class,
							new Long(origId));
					Long oldParentId = origNode.getParentId();
					Long oldIdx = origNode.getIdx();

					MeleteModuleModel destNode = (MeleteModuleModel) this.getModelById(MeleteModuleModel.class,
							new Long(destId));
					Long destIdx = destNode.getIdx();
					if (destNode.getParentId() != null) {
						String hql = "update MeleteModuleModel set idx=idx+1 where idx>? and parentId=?";
						Object[] param = { destIdx, destNode.getParentId() };
						this.updateEntity(hql, param);
						List<MeleteModuleModel> moduleList = this.getModuleByParentId(destNode.getParentId(), true);
						for (int i = 0; i < moduleList.size(); i++) {
							if (moduleList.get(i).getIdx().longValue() > destIdx) {
								moduleList.get(i).setIdx(moduleList.get(i).getIdx().longValue() + 1);
								CacheUtil.getInstance().updateModule(moduleList.get(i));// 更新所有学生的缓存中对应的模块
							}
						}
					} else {
						String hql = "update MeleteModuleModel set idx=idx+1 where idx>? and parentId is null and courseId=?";
						Object[] param = { destIdx, destNode.getCourseId() };
						this.updateEntity(hql, param);
						List<MeleteModuleModel> moduleList = this.getModuleByCourseId(destNode.getCourseId(), true);
						for (int i = 0; i < moduleList.size(); i++) {
							if (moduleList.get(i).getIdx().longValue() > destIdx) {
								moduleList.get(i).setIdx(moduleList.get(i).getIdx().longValue() + 1);
								CacheUtil.getInstance().updateModule(moduleList.get(i));// 更新所有学生的缓存中对应的模块
							}
						}
					}
					origNode.setIdx(destIdx + 1);
					origNode.setParentId(destNode.getParentId());
					this.editModule(origNode);

					if (oldParentId != null) {
						String hql = "update MeleteModuleModel set idx=idx-1 where idx>? and parentId=?";
						Object[] param = { oldIdx, oldParentId };
						this.updateEntity(hql, param);

						List<MeleteModuleModel> moduleList = this.getModuleByParentId(oldParentId, true);

						if (moduleList != null && !moduleList.isEmpty()) {// 若父节点下还有子节点
							for (int i = 0; i < moduleList.size(); i++) {
								if (moduleList.get(i).getIdx().longValue() > oldIdx) {
									moduleList.get(i).setIdx(moduleList.get(i).getIdx().longValue() - 1);
									CacheUtil.getInstance().updateModule(moduleList.get(i));// 更新所有学生的缓存中对应的模块
								}
							}
						} else {// 若父节点下无子节点则将父节点的childType属性设为null
							MeleteModuleModel oldPModule = (MeleteModuleModel) this.getModelById(
									MeleteModuleModel.class, new Long(oldParentId));
							oldPModule.setChildType(null);
							this.editModule(oldPModule);
						}

					} else {
						String hql = "update MeleteModuleModel set idx=idx-1 where idx>? and parentId is null and courseId=?";
						Object[] param = { oldIdx, origNode.getCourseId() };
						this.updateEntity(hql, param);

						List<MeleteModuleModel> moduleList = this.getModuleByCourseId(origNode.getCourseId(), true);
						for (int i = 0; i < moduleList.size(); i++) {
							if (moduleList.get(i).getIdx().longValue() > oldIdx) {
								moduleList.get(i).setIdx(moduleList.get(i).getIdx().longValue() - 1);
								CacheUtil.getInstance().updateModule(moduleList.get(i));// 更新所有学生的缓存中对应的模块
							}
						}

					}
					if (oldParentId != destNode.getParentId()) {
						if (destNode.getParentId() != null) {
							this.updateModuleCondition(destNode.getParentId().toString(), CodeTable.module);
						}

						if (oldParentId != null) {
							this.updateModuleCondition(oldParentId.toString(), CodeTable.module);
						} else {
							this.updateCourseCondition(origNode.getCourseId());
						}
					}
					this.adjustXML(origNode, destNode, point);
				} else if (origType.equals("3") && destType.equals("3")) {// 拖拽的页节点，目标为页节点
					MeleteSectionModel origNode = (MeleteSectionModel) this.getModelById(MeleteSectionModel.class,
							new Long(origId));
					MeleteSectionModel destNode = (MeleteSectionModel) this.getModelById(MeleteSectionModel.class,
							new Long(destId));
					Long oldModuleId = origNode.getModuleId();
					Long oldIdx = origNode.getIdx();
					Long destIdx = destNode.getIdx();

					String hql = "update MeleteSectionModel set idx=idx+1 where idx>? and moduleId=?";
					Object[] param = { destIdx, destNode.getModuleId() };
					this.updateEntity(hql, param);
					List<MeleteSectionModel> sectionList = this.getSectionByModuleId(destNode.getModuleId(),
							Long.valueOf(CodeTable.normal));
					for (int i = 0; i < sectionList.size(); i++) {
						if (sectionList.get(i).getIdx().longValue() > destIdx) {
							sectionList.get(i).setIdx(sectionList.get(i).getIdx().longValue() + 1);
							CacheUtil.getInstance().updateSection(sectionList.get(i));// 更新所有学生的缓存中对应的页
						}
					}

					origNode.setModuleId(destNode.getModuleId());
					origNode.setIdx(destIdx + 1);
					this.editSection(origNode);

					hql = "update MeleteSectionModel set idx=idx-1 where idx>? and moduleId=?";
					Object[] oparam = { oldIdx, oldModuleId };
					this.updateEntity(hql, oparam);
					List<MeleteSectionModel> oldSectionList = this.getSectionByModuleId(oldModuleId,
							Long.valueOf(CodeTable.normal));

					if (oldSectionList != null && !oldSectionList.isEmpty()) {// 若拖拽前的父节点下还有子节点
						for (int i = 0; i < oldSectionList.size(); i++) {
							if (oldSectionList.get(i).getIdx().longValue() > oldIdx) {
								oldSectionList.get(i).setIdx(oldSectionList.get(i).getIdx().longValue() - 1);
								CacheUtil.getInstance().updateSection(oldSectionList.get(i));// 更新所有学生的缓存中对应的页
							}
						}
					} else {// 若拖拽之前的模块无子节点则将父节点的childType设为null
						MeleteModuleModel oldPModule = (MeleteModuleModel) this.getModelById(MeleteModuleModel.class,
								new Long(oldModuleId));
						oldPModule.setChildType(null);
						this.editModule(oldPModule);
					}

					if (oldModuleId != destNode.getModuleId()) {
						this.updateModuleCondition(oldModuleId.toString(), CodeTable.section);
						this.updateModuleCondition(destNode.getModuleId().toString(), CodeTable.section);
					}
					this.adjustXML(origNode, destNode, point);
				} else {
					return "{success:false}";
				}
			}
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.sakaiproject.resource.api.course.service.ICourseService#
	 * changeModuleStatus(java.lang.String, java.lang.Long)
	 */
	// 修改模块的状态，同时修改子模块页活动的状态
	public void changeModuleStatus(Long moduleId, Long status) throws Exception {
		try {
			this.changeModelStatus(MeleteModuleModel.class, moduleId, status);// 修改该模块的状态
			CacheUtil.getInstance().updateModule(
					(MeleteModuleModel) this.getModelById(MeleteModuleModel.class, moduleId));
			Object[] param = { status, moduleId };

			String testHql = "update MeleteTestModel set status=? where moduleId=?";
			this.updateEntity(testHql, param);// 修改模块下作业的状态

			List<MeleteTestModel> testList = this.getTestByModuleId(moduleId, false);
			for (int i = 0; i < testList.size(); i++) {
				testList.get(i).setStatus(status);
				CacheUtil.getInstance().updateTest(testList.get(i));// 更新所有学生缓存中的对应作业信息
			}

			String selftestHql = "update MeleteSelfTestModel set status=? where moduleId=?";
			this.updateEntity(selftestHql, param);// 修改模块下前测的状态

			List<MeleteSelfTestModel> selfTestList = this.getSelftestByModuleId(moduleId, false);
			for (int i = 0; i < selfTestList.size(); i++) {
				selfTestList.get(i).setStatus(status);
				CacheUtil.getInstance().updateSelftest(selfTestList.get(i));// 更新所有学生缓存中的对应前测信息
			}

			String forumHql = "update MeleteForumModel set status=? where moduleId=?";
			this.updateEntity(forumHql, param);// 修改模块下讨论的状态
			List<MeleteForumModel> forumList = this.getForumByModuleId(moduleId, false);
			for (int i = 0; i < forumList.size(); i++) {
				forumList.get(i).setStatus(status);
				CacheUtil.getInstance().updateForum(forumList.get(i));// 更新所有学生缓存中的对应讨论信息
			}

			Long selSta = new Long(0);
			if (status.toString().equals(CodeTable.hide)) {// 隐藏操作
				selSta = new Long(CodeTable.normal);
			} else if (status.toString().equals(CodeTable.normal)) {// 恢复操作
				selSta = new Long(CodeTable.hide);
			} else if (status.toString().equals(CodeTable.del)) {// 删除操作
				selSta = new Long(CodeTable.normal);
			}

			List<MeleteSectionModel> sectionList = this.getSectionByModuleId(moduleId, selSta);// 获取页集合
			for (MeleteSectionModel section : sectionList) {// 遍历页集合
				this.changeSectionStatus(section.getId(), status);// 修改页的状态
			}

			List<MeleteModuleModel> subList = this.getModuleByParentId(moduleId, true);// 获取下级模块集合
			for (MeleteModuleModel sub : subList) {// 遍历模块集合
				this.changeModuleStatus(sub.getId(), status);// 修改下级模块的状态
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.sakaiproject.resource.api.course.service.ICourseService#
	 * changeSectionStatus(java.lang.String, java.lang.Long)
	 */
	// 修改页的状态，同时修改所包含的活动的状态
	public void changeSectionStatus(Long sectionId, Long status) throws Exception {
		try {
			String sectionHql = "update MeleteSectionModel set status=? where id=?";
			Object[] param = { status, sectionId };
			this.updateEntity(sectionHql, param);// 修改页的状态
			MeleteSectionModel section = (MeleteSectionModel) this.findEntityById(MeleteSectionModel.class, sectionId);
			section.setStatus(status);
			CacheUtil.getInstance().updateSection(section);// 更改所有学生缓存中的对应的页信息

			String testHql = "update MeleteTestModel set status=? where sectionId=?";
			this.updateEntity(testHql, param);// 修改作业的状态
			List<MeleteTestModel> testList = this.getTestBySectionId(sectionId, false);
			for (int i = 0; i < testList.size(); i++) {
				testList.get(i).setStatus(status);
				CacheUtil.getInstance().updateTest(testList.get(i));// 更新所有学生缓存中对应的作业信息
			}

			String selftestHql = "update MeleteSelfTestModel set status=? where sectionId=?";
			this.updateEntity(selftestHql, param);// 修改前测的状态
			List<MeleteSelfTestModel> selfTestList = this.getSelftestBySectionId(sectionId, false);
			for (int i = 0; i < selfTestList.size(); i++) {
				selfTestList.get(i).setStatus(status);
				CacheUtil.getInstance().updateSelftest(selfTestList.get(i));// 更新所有学生缓存中对应的前测信息
			}

			String forumHql = "update MeleteForumModel set status=? where sectionId=?";
			this.updateEntity(forumHql, param);// 修改讨论的状态
			List<MeleteForumModel> froumList = this.getForumBySectionId(sectionId, false);
			for (int i = 0; i < froumList.size(); i++) {
				froumList.get(i).setStatus(status);
				CacheUtil.getInstance().updateForum(froumList.get(i));// 更新所有学生缓存中对应的讨论信息
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.sakaiproject.resource.api.course.service.ICourseService#
	 * getMaxModuleIdxByCourseId(java.lang.String)
	 */
	// 查询课程一级节点的最大序号
	public Long getMaxModuleIdxByCourseId(String courseId) throws Exception {
		try {
			String hql = "select max(idx) from MeleteModuleModel where courseId=? and parentId is null";
			Object[] param = { courseId };
			List maxList = this.findEntity(hql, param);
			if (maxList.isEmpty() || maxList.size() == 0 || maxList.get(0) == null) {
				return new Long(0);
			} else {
				return new Long(maxList.get(0).toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.sakaiproject.resource.api.course.service.ICourseService#
	 * getMaxModuleIdxByParentId(java.lang.String)
	 */
	// 查询兄弟节点最大序号
	public Long getMaxModuleIdxByParentId(Long parentId) throws Exception {
		try {
			String hql = "select max(idx) from MeleteModuleModel where parentId=?";
			Object[] param = { parentId };
			List maxList = this.findEntity(hql, param);
			if (maxList.isEmpty() || maxList.size() == 0 || maxList.get(0) == null) {
				return new Long(0);
			} else {
				return new Long(maxList.get(0).toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.sakaiproject.resource.api.course.service.ICourseService#
	 * getMaxSectionIdxByModuleId(java.lang.String)
	 */
	// 根据模块id查询兄弟页中的最大序号
	public Long getMaxSectionIdxByModuleId(Long moduleId) throws Exception {
		try {
			String hql = "select max(idx) from MeleteSectionModel where moduleId=?";
			Object[] param = { moduleId };
			List maxList = this.findEntity(hql, param);
			if (maxList.isEmpty() || maxList.size() == 0 || maxList.get(0) == null) {
				return new Long(0);
			} else {
				return new Long(maxList.get(0).toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	private boolean copyFile(File descFile, File sourceFile, String courseId) throws Exception {
		boolean result = true;
		try {
			String path = ServletActionContext.getRequest().getContextPath();
			String fileHref = sourceFile.getName();
			if (fileHref.toLowerCase().indexOf(".htm") > 0 || fileHref.toLowerCase().indexOf(".html") > 0) {// 是页面文件
				String fileContent = this.replaceImportHtmlResPath(sourceFile, courseId);// 替换html文件中引用资源的路径
				fileContent += "\r\n<script>\r\n";
				fileContent += "var domainUrl = window.location.hash.toString().substring(1);\r\n";
				fileContent += "var script = document.createElement(\"script\");\r\n";
				fileContent += "script.src = domainUrl + \"/resource/scripts/APIWrapper.js\";\r\n";
				fileContent += "script.charset = \"utf-8\";\r\n";
				fileContent += "document.getElementsByTagName(\"head\")[0].appendChild(script);\r\n";
				fileContent += "</script>";
				FileUtils.writeStringToFile(descFile, fileContent, "UTF-8");
			} else {
				FileUtils.copyFile(sourceFile, descFile);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
			logger.error(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 替换导入的html文件中引用资源（图片 flash link script 等）标签中的路径
	 * 
	 * @param htmlFile
	 * @param courseId
	 * @return 更改路径后的html文本
	 */
	private String replaceImportHtmlResPath(File htmlFile, String courseId) throws Exception {
		org.jsoup.nodes.Document document = Jsoup.parse(htmlFile, "UTF-8", ServerConfigurationService.getServerUrl());
		String courseReplaceURI = Constants.COURSE_PATH_URI + courseId + "/";
		// 替换img标签的路径
		this.replaceImportImgPath(courseReplaceURI, document);
		// 替换link标签路径
		this.replaceImportLinkPath(courseReplaceURI, document);
		// 替换param标签路径
		this.replaceImportParamPath(courseReplaceURI, document);
		// 替换object标签路径
		this.replaceImportObjectPath(courseReplaceURI, document);
		// 替换script标签路径
		this.replaceImportScriptPath(courseReplaceURI, document);
		// 替换embed标签路径
		this.replaceImportEmbedPath(courseReplaceURI, document);
		return document.outerHtml();
	}

	/**
	 * 导入课件时替换embed标签的路径
	 * 
	 * @param courseId
	 * @param document
	 */
	private void replaceImportEmbedPath(String courseReplaceURI, org.jsoup.nodes.Document document) {
		Elements embedElements = document.getElementsByTag("embed");
		if (!embedElements.isEmpty()) {
			for (org.jsoup.nodes.Element embed : embedElements) {
				String src = embed.attr("src");
				if (src != null && !"".equals(src) && !(src.startsWith("http://") || src.startsWith("https://"))) {
					logger.debug("导入前路径：" + src);
					// 如果是以"/"的表示使用绝对路径,不进行替换, 不是这个开头的再进行替换
					if (!src.startsWith("/")) {
						src = courseReplaceURI + src;
					} else {
						String http = null;
						if (courseReplaceURI.indexOf("://") > -1) {// 查看域名中是否存在
																	// http
							int httpIndex = courseReplaceURI.indexOf("://") + 3;
							http = courseReplaceURI.substring(0, httpIndex); // 域名前的http段

							String str = courseReplaceURI.substring(httpIndex);// http后面的

							if (str.indexOf("/") > -1) {// 第一个/开始的地方既是 域名+端口号的位置
								String serverName = str.substring(0, str.indexOf("/"));
								src = http + serverName + src;// http + 域名和端口号 +
																// 路径
							}
						}
					}
					logger.debug("导入替换后路径：" + src);
					embed.attr("src", src);// 替换script标签中的src为应用根路径开始的绝对路径
				}
			}
		}
	}

	/**
	 * 导入课件时替换script标签的路径
	 * 
	 * @param courseId
	 * @param document
	 * @return
	 */
	private void replaceImportScriptPath(String courseReplaceURI, org.jsoup.nodes.Document document) {
		Elements scriptElements = document.getElementsByTag("script");
		if (!scriptElements.isEmpty()) {
			for (org.jsoup.nodes.Element script : scriptElements) {
				String src = script.attr("src");
				if (src != null && !"".equals(src) && !(src.startsWith("http://") || src.startsWith("https://"))) {
					logger.debug("导入前script标签路径：" + src);
					// 如果是以"/"的表示使用绝对路径,不进行替换, 不是这个开头的再进行替换
					if (!src.startsWith("/")) {
						src = courseReplaceURI + src;
					} else {
						String http = null;
						if (courseReplaceURI.indexOf("://") > -1) {// 查看域名中是否存在
																	// http
							int httpIndex = courseReplaceURI.indexOf("://") + 3;
							http = courseReplaceURI.substring(0, httpIndex); // 域名前的http段

							String str = courseReplaceURI.substring(httpIndex);// http后面的

							if (str.indexOf("/") > -1) {// 第一个/开始的地方既是 域名+端口号的位置
								String serverName = str.substring(0, str.indexOf("/"));
								src = http + serverName + src;// http + 域名和端口号 +
																// 路径
							}
						}
					}
					logger.debug("导入替换后script标签路径：" + src);
					script.attr("src", src);// 替换script标签中的src为应用根路径开始的绝对路径
				}
			}
		}
	}

	/**
	 * 导入课件时替换object标签的路径
	 * 
	 * @param courseId
	 * @param document
	 */
	private void replaceImportObjectPath(String courseReplaceURI, org.jsoup.nodes.Document document) {
		Elements objectElements = document.getElementsByTag("object");
		if (!objectElements.isEmpty()) {
			for (org.jsoup.nodes.Element object : objectElements) {
				String type = object.attr("type");
				if (type != null && type.equalsIgnoreCase("application/x-shockwave-flash")) {
					String data = object.attr("data");
					if (data != null && !"".equals(data)
							&& !(data.startsWith("http://") || data.startsWith("https://"))) {
						logger.debug("导入前object标签路径：" + data);
						// 如果是以"/"的表示使用绝对路径,不进行替换, 不是这个开头的再进行替换
						if (!data.startsWith("/")) {
							data = courseReplaceURI + data;
						} else {
							String http = null;
							if (courseReplaceURI.indexOf("://") > -1) {// 查看域名中是否存在
																		// http
								int httpIndex = courseReplaceURI.indexOf("://") + 3;
								http = courseReplaceURI.substring(0, httpIndex); // 域名前的http段

								String str = courseReplaceURI.substring(httpIndex);// http后面的

								if (str.indexOf("/") > -1) {// 第一个/开始的地方既是
															// 域名+端口号的位置
									String serverName = str.substring(0, str.indexOf("/"));
									data = http + serverName + data;// http +
																	// 域名和端口号 +
																	// 路径
								}
							}
						}
						logger.debug("导入替换后object标签路径：" + data);
						object.attr("data", data);// 替换object标签中的data为应用根路径开始的绝对路径
					}
				}
			}
		}
	}

	/**
	 * 导入课件时替换param标签的路径
	 * 
	 * @param courseId
	 * @param document
	 */
	private void replaceImportParamPath(String courseReplaceURI, org.jsoup.nodes.Document document) {
		Elements paramElements = document.getElementsByTag("param");
		if (!paramElements.isEmpty()) {
			for (org.jsoup.nodes.Element param : paramElements) {
				String name = param.attr("name");
				if (name != null && (name.equalsIgnoreCase("movie") || name.equalsIgnoreCase("expressInstall"))) {
					String value = param.attr("value");
					if (value != null && !"".equals(value)
							&& !(value.startsWith("http://") || value.startsWith("https://"))) {
						logger.debug("导入前param标签路径：" + value);
						// 如果是以"/"的表示使用绝对路径,不进行替换, 不是这个开头的再进行替换
						if (!value.startsWith("/")) {
							value = courseReplaceURI + value;
						} else {
							String http = null;
							if (courseReplaceURI.indexOf("://") > -1) {// 查看域名中是否存在
																		// http
								int httpIndex = courseReplaceURI.indexOf("://") + 3;
								http = courseReplaceURI.substring(0, httpIndex); // 域名前的http段

								String str = courseReplaceURI.substring(httpIndex);// http后面的

								if (str.indexOf("/") > -1) {// 第一个/开始的地方既是
															// 域名+端口号的位置
									String serverName = str.substring(0, str.indexOf("/"));
									value = http + serverName + value;// http +
																		// 域名和端口号
																		// + 路径
								}
							}
						}
						logger.debug("导入替换后param标签路径：" + value);
						param.attr("value", value);// 替换param标签中的value为应用根路径开始的绝对路径
					}
				}
			}
		}
	}

	/**
	 * 导入课件时替换link标签的路径
	 * 
	 * @param courseId
	 * @param document
	 */
	private void replaceImportLinkPath(String courseReplaceURI, org.jsoup.nodes.Document document) {
		Elements linkElements = document.getElementsByTag("link");
		if (!linkElements.isEmpty()) {
			for (org.jsoup.nodes.Element link : linkElements) {
				String href = link.attr("href");
				if (href != null && !"".equals(href) && !(href.startsWith("http://") || href.startsWith("https://"))) {
					logger.debug("导入前link标签路径：" + href);
					// 如果是以"/"的表示使用绝对路径,不进行替换, 不是这个开头的再进行替换
					if (!href.startsWith("/")) {
						href = courseReplaceURI + href;
					} else {
						String http = null;
						if (courseReplaceURI.indexOf("://") > -1) {// 查看域名中是否存在
																	// http
							int httpIndex = courseReplaceURI.indexOf("://") + 3;
							http = courseReplaceURI.substring(0, httpIndex); // 域名前的http段

							String str = courseReplaceURI.substring(httpIndex);// http后面的

							if (str.indexOf("/") > -1) {// 第一个/开始的地方既是 域名+端口号的位置
								String serverName = str.substring(0, str.indexOf("/"));
								href = http + serverName + href;// http + 域名和端口号
																// + 路径
							}
						}
					}
					logger.debug("导入替换后link标签路径：" + href);
					link.attr("href", href);// 替换link标签中的href为应用根路径开始的绝对路径
				}
			}
		}
	}

	/**
	 * 导入课件时替换img标签的路径
	 * 
	 * @param courseId
	 * @param document
	 */
	private void replaceImportImgPath(String courseReplaceURI, org.jsoup.nodes.Document document) {
		Elements imgElements = document.getElementsByTag("img");
		if (!imgElements.isEmpty()) {
			for (org.jsoup.nodes.Element img : imgElements) {
				String src = img.attr("src");
				if (src != null && !"".equals(src) && !(src.startsWith("http://") || src.startsWith("https://"))) {
					logger.debug("导入前图片路径：" + src);
					// 如果是以"/"的表示使用绝对路径,不进行替换, 不是这个开头的再进行替换
					if (!src.startsWith("/")) {
						src = courseReplaceURI + src;
					} else {
						String http = null;
						if (courseReplaceURI.indexOf("://") > -1) {// 查看域名中是否存在
																	// http
							int httpIndex = courseReplaceURI.indexOf("://") + 3;
							http = courseReplaceURI.substring(0, httpIndex); // 域名前的http段

							String str = courseReplaceURI.substring(httpIndex);// http后面的

							if (str.indexOf("/") > -1) {// 第一个/开始的地方既是 域名+端口号的位置
								String serverName = str.substring(0, str.indexOf("/"));
								src = http + serverName + src;// http + 域名和端口号 +
																// 路径
							}
						}
					}
					logger.debug("替换路径后图片路径：" + src);
					img.attr("src", src);// 替换img标签中的src为应用根路径开始的绝对路径
				}
			}
		}
	}

	/**
	 * 根据文件名称或相对路径（在课件描述文件中的file元素的href的属性值）获得文件的存放的绝对路径
	 * 
	 * @param fileName
	 * @return
	 */
	private String createFilePath(String fileName) {
		String filePath = Constants.COURSEWARE_RESOURCE_PATH;
		if (fileName.lastIndexOf(".") > 0) {
			String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1);// 获得文件名的扩展名
			if (Constants.COURSEWARE_RESOURCE_IMAGE_EXT.indexOf(fileExt) > 0) {// 图片文件
				filePath += Constants.COURSEWARE_RESOURCE_IMAGE_FOLDER + fileName;
			} else if (Constants.COURSEWARE_RESOURCE_FLASH_EXT.indexOf(fileExt) > 0) {// flash文件
				filePath += Constants.COURSEWARE_RESOURCE_FLASH_FOLDER + fileName;
			} else {// 其他类型文件
				filePath += Constants.COURSEWARE_RESOURCE_FILE_FOLDER + fileName;
			}
		} else {// 无扩展名文件将绝对路径设为一般文件存放路径
			filePath += Constants.COURSEWARE_RESOURCE_FILE_FOLDER + fileName;
		}
		return filePath;
	}

	private boolean createFile(File file) throws Exception {
		boolean result = true;
		try {
			if (!file.exists()) {
				File parentFile = file.getParentFile();
				if (!parentFile.exists()) {
					parentFile.mkdirs();
				}
				file.createNewFile();
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	private List<Object> importSubScorm(Vector resources, String courseId, Node parentNode, String warePath,
			String coursePath, String parentId, String relaPath, List<String> nodeRefList) throws Exception {
		List<Object> resultList = new ArrayList<Object>();
		try {
			Vector childList = DOMTreeUtility.getNodes(parentNode, "item");// 下级模块集合
			for (int j = 0; j < childList.size(); j++) {
				Node childNode = (Node) childList.elementAt(j);
				String nodeTitle = DOMTreeUtility.getNodeValue(DOMTreeUtility.getNode(childNode, "title"));// 标题
				String nodeRef = DOMTreeUtility.getAttributeValue(childNode, "identifierref");
				String identifier = DOMTreeUtility.getAttributeValue(childNode, "identifier");
				if (nodeRef == null || nodeRef.equals("")) { // 是模块节点
					MeleteModuleModel module = new MeleteModuleModel();
					module.setParentId(new Long(parentId));
					module.setCourseId(courseId);
					// 查询兄弟节点的最大序号并生成新的序号
					Long idx = getMaxModuleIdxByParentId(new Long(parentId)) + 1;
					module.setIdx(idx);// 设置序号
					module.setTitle(nodeTitle);// 设置模块标题
					module.setKeywords(nodeTitle);// 将keywords设置为模块标题
					// 因为原有的melete数据库中该字段为非空所以暂将该字段的值设置为title的值
					String username = (String) ServletActionContext.getRequest().getSession().getAttribute("userName");
					module.setCreatedByFname(username);// 设置创建人
					module.setCreatedByLname(username);
					module.setUserId(userDirectoryService.getCurrentUser().getId());// 设置创建人id
					module.setCreationDate(new Date());// 设置创建时间
					module.setPrerequids(CodeTable.prerequisiteNo);// 设置开启条件（默认无条件）
					module.setRequired(new Long(CodeTable.required));// 设置必修选修（默认必修）
					module.setStatus(new Long(CodeTable.normal));// 设置状态
					module.setStudyDay(new Long(0));// 设置学习时间
					module.setAvgPassTime(0L);
					Vector childrenList = DOMTreeUtility.getNodes(childNode, "item");// 下级模块集合
					String ment = "必修" + childrenList.size() + "个";
					if (childrenList.size() > 0) {
						Node childrenNode = (Node) childrenList.elementAt(0);
						String childRef = DOMTreeUtility.getAttributeValue(childrenNode, "identifierref");
						if (childRef == null || childRef.equals("")) {
							ment += "节点";
							module.setChildType(CodeTable.module);
						} else {
							ment += "页";
							module.setChildType(CodeTable.section);
						}
					} else
						module.setRequirement(ment);// 通过条件中文说明
					module.setModuleItemID(DOMTreeUtility.getAttributeValue(childNode, "identifier"));// 将模块与课件描述文件中的module的item元素中相关联
					String moduleId = this.createEntity(module);// 保存模块信息
					logger.debug("----创建模块---id----" + moduleId + "---title" + module.getTitle()
							+ "---moduleItemId----" + module.getModuleItemID());
					// 将必修总数暂存在最少选修属性中，便于返回前台
					resultList.add(module);
					List<Object> subList = importSubScorm(resources, courseId, childNode, warePath, coursePath,
							moduleId, relaPath, nodeRefList);
					resultList.addAll(subList);
				} else {// 是页节点
					nodeRefList.add(nodeRef);// 将引用到的资源节点id放入list中
					MeleteSectionModel section = this.importSection(resources, nodeRef, identifier, coursePath,
							warePath, parentId, nodeTitle, relaPath, courseId);
					resultList.add(section);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}

	/**
	 * 复制section对应的页
	 * 
	 * @param resources
	 * @param nodeRef
	 * @param coursePath
	 * @param warePath
	 * @param parentId
	 * @param title
	 * @param relaPath
	 * @return
	 * @throws Exception
	 */
	private MeleteSectionModel importSection(Vector resources, String nodeRef, String identifier, String coursePath,
			String warePath, String parentId, String title, String relaPath, String courseId) throws Exception {
		MeleteSectionModel section = new MeleteSectionModel();
		for (int i = 0; i < resources.size(); i++) { // 遍历资源标签
			Node resourceNode = (Node) resources.elementAt(i); // 获取资源节点
			String resourceID = DOMTreeUtility.getAttributeValue(resourceNode, "identifier"); // 获取节点的identifier属性
			if (nodeRef.equals(resourceID)) { // 页所对应的资源节点
				String nodeHref = DOMTreeUtility.getAttributeValue(resourceNode, "href");// htm路径
				decodeHandler decoder = new decodeHandler(nodeHref, "UTF-8");
				decoder.decodeName();
				String descHref = relaPath + decoder.getDecodedFileName();

				Vector fileList = DOMTreeUtility.getNodes(resourceNode, "file");// 获取file集合

				for (int k = 0; k < fileList.size(); k++) {// 遍历文件标签
					Node fileNode = (Node) fileList.elementAt(k);// 获取文件节点
					String fileHref = DOMTreeUtility.getAttributeValue(fileNode, "href");// 文件的路径
					decodeHandler decodeFile = new decodeHandler(fileHref, "UTF-8");
					decodeFile.decodeName();
					fileHref = warePath + decodeFile.getDecodedFileName();// 文件资源的绝对路径
					File source = new File(fileHref);// 获取源文件
					if (source.exists()) {// 若源文件存在
						File desc = new File(coursePath + decodeFile.getDecodedFileName());// 获取目标文件
						createFile(desc); // 创建目标文件
						copyFile(desc, source, courseId); // 复制文件
					} else {
						logger.info("从课件资源目录向课程目录复制文件时出错, 原因:" + source.getAbsolutePath()
								+ "文件不存在, 该文件在在课件描述文件中的resource标签中的file标签中已配置但实际该资源不存在");
					}
				}

				section.setCourseId(courseId);
				section.setModuleId(new Long(parentId));// 节点id
				section.setTitle(title);// 页标题
				section.setAudioContent(new Long(0));// 是否包含音频
				String username = (String) ServletActionContext.getRequest().getSession().getAttribute("userName");
				section.setCreatedByFname(username);// 创建人
				section.setCreatedByLname(username);// createdByLname的值与createdByFname相同
				section.setCreationDate(new Date());// 创建时间
				section.setModificationDate(new Date());// 修改时间
				section.setPrerequids(CodeTable.prerequisiteNo);// 开启条件（默认无条件）
				section.setRequired(new Long(CodeTable.required));// 必修
				section.setStatus(new Long(CodeTable.normal));// 页状态
				section.setStudyTime(new Long(0));// 学习时长
				section.setAvgPassTime(0L);
				section.setTextualContent(new Long(1));// 是否包含文本
				section.setVideoContent(new Long(0));// 是否包含视频
				section.setIdx(getMaxSectionIdxByModuleId(new Long(parentId)) + 1);// 页序号
				section.setPath(descHref.replace("\\", "/"));// 存储路径
				section.setRequirement(null);// 通过条件中文说明
				section.setContentType("notype");// 为兼容原有melete中的非空字段contentType
				// 无实际作用
				section.setSectionItemId(identifier);// 关联课件描述文件中的section的item的identifier属性值
				this.createEntity(section);
				logger.debug("---新建页---id--" + section.getId() + "----title" + section.getTitle()
						+ "---sectionItemId---" + section.getSectionItemId());
			}
		}
		return section;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sakaiproject.resource.api.course.service.ICourseService#importScorm
	 * (java.lang.String, java.lang.String, java.lang.String)
	 */
	// 保存从资源包中引入的页
	public List<Object> importScorm(String courseId, String mods, String wareId) throws Exception {
		List<Object> resultList = new ArrayList<Object>();
		try {
			String webPath = Constants.WEB_PATH;
			String securePath = Constants.SECURE_PATH;
			String warePath = new File(securePath) + "/" + Constants.COURSE_RESOURCE_PACK_PATH + "/" + wareId + "/";// 课件目录
			String coursePath = new File(webPath) + "/" + Constants.SECTION_PATH + "/" + courseId + "/";// 课程目录
			String relaPath = Constants.SECTION_PATH + "/" + courseId + "/";
			File courseRootFile = new File(coursePath);
			if (!courseRootFile.exists()) { // 文件夹不存在则创建
				courseRootFile.mkdirs();
			}
			String manifestFile = warePath + "imsmanifest.xml";
			ADLDOMParser domParser = new ADLDOMParser();
			domParser.createDocument(manifestFile, true, false);
			Document mDocument = domParser.getDocument(); // 解析imsmanifest.xml文件
			Node mManifest = mDocument.getDocumentElement();
			Node tempOrg = getOrgNode(mDocument);
			Vector moduleList = DOMTreeUtility.getNodes(tempOrg, "item");// 一级模块集合

			Node resourcesNode = DOMTreeUtility.getNode(mManifest, "resources");
			Vector resources = DOMTreeUtility.getNodes(resourcesNode, "resource");
			List<String> nodeRefList = new ArrayList<String>();// 存放资源引用id集合

			for (int j = 0; j < moduleList.size(); j++) {
				Node tempModule = (Node) moduleList.elementAt(j);
				String tempModuleIdentifier = DOMTreeUtility.getAttributeValue(tempModule, "identifier");// 模块id
				if (mods.indexOf("," + tempModuleIdentifier + ",") >= 0) {// 该模块被选中
					if (logger.isDebugEnabled()) {
						logger.debug("选中模块在课件描述文件中的identifier为" + tempModuleIdentifier);
					}
					Node tempModuleTitleNode = DOMTreeUtility.getNode(tempModule, "title");
					String tempModuleTitle = DOMTreeUtility.getNodeValue(tempModuleTitleNode);// 模块标题

					MeleteModuleModel module = new MeleteModuleModel();
					module.setCourseId(courseId);
					Long idx = this.getMaxModuleIdxByCourseId(courseId) + 1;
					module.setIdx(idx);// 设置序号
					module.setTitle(tempModuleTitle);// 设置模块标题
					module.setKeywords(tempModuleTitle);// 将keywords设置为模块标题
					// 因为原有的melete数据库中该字段为非空所以暂将该字段的值设置为title的值
					String username = (String) ServletActionContext.getRequest().getSession().getAttribute("userName");
					module.setCreatedByFname(username);// 设置创建人
					module.setCreatedByLname(username);// 为兼容原有melete中的createdByLname字段非空限制在此处设置与createdByFname相同
					module.setUserId(userDirectoryService.getCurrentUser().getId());// 设置创建人id
					module.setCreationDate(new Date());// 设置创建时间
					module.setPrerequids(CodeTable.prerequisiteNo);// 设置开启条件（默认没有开启条件）
					module.setRequired(new Long(CodeTable.required));// 设置必修选修（默认必修）
					module.setStatus(new Long(CodeTable.normal));// 设置状态
					module.setStudyDay(new Long(0));// 设置学习时间
					module.setAvgPassTime(0L);
					Vector childList = DOMTreeUtility.getNodes(tempModule, "item");// 下级模块集合
					String ment = "必修" + childList.size() + "个";
					Node childNode = (Node) childList.elementAt(0);
					String nodeRef = DOMTreeUtility.getAttributeValue(childNode, "identifierref");
					if (nodeRef == null || nodeRef.equals("")) {
						ment += "节点";
						module.setChildType(CodeTable.module);
					} else {
						ment += "页";
						module.setChildType(CodeTable.section);
					}
					module.setRequirement(ment);// 通过条件中文说明
					module.setModuleItemID(DOMTreeUtility.getAttributeValue(tempModule, "identifier"));// 将模块与课件描述文件中的module的item元素中相关联
					String moduleId = this.editModule(module);// 保存节点
					logger.debug("----新建节点----id---" + moduleId + "----title---" + module.getTitle()
							+ "---在课件描述文件对应的identifier为---" + module.getModuleItemID());
					// 将必修总数暂存在最少选修属性中，便于返回前台
					resultList.add(module);
					List<Object> sublist = importSubScorm(resources, courseId, tempModule, warePath, coursePath,
							moduleId, relaPath, nodeRefList);
					resultList.addAll(sublist);
				} else {
					tempOrg.removeChild(tempModule);// 移除未选中节点
				}
			}

			this.removeResource(resources, nodeRefList, false);// 遍历resources
			// 移除资源节点的identifier不在nodeRefList中的资源节点
			String courseIdAndimportItemIds = courseId + ";" + mods;
			this.toCourseManifest(coursePath, mDocument, tempOrg, resourcesNode, CodeTable.course,
					courseIdAndimportItemIds);// 将修改过得mDocument写入到coursePath中的课件描述文件中

			this.updateCourseCondition(courseId);// 修改课程节点的通过条件
			CacheUtil.getInstance().resetCacheOfCourse(courseId);
			return resultList;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sakaiproject.resource.api.course.service.ICourseService#setScormAttr
	 * (java.lang.String[], java.lang.String[], java.lang.String[],
	 * java.lang.String[], java.lang.String[], java.lang.String[])
	 */
	// 初始化导入资源的属性
	public void setScormAttr(String[] nodeTypes, String[] ids, String[] requireds, String[] studyTimes,
			String[] prerequidses, String[] minSecNums, String[] reqNums, String[] childTypes, String[] titles)
			throws Exception {
		try {
			for (int i = 0; i < ids.length; i++) {
				String nodeType = nodeTypes[i];
				if (nodeType.equals(CodeTable.module)) { // 是模块
					MeleteModuleModel module = (MeleteModuleModel) getModelById(MeleteModuleModel.class, new Long(
							ids[i]));
					module.setRequired(new Long(requireds[i]));
					module.setStudyDay(new Long(studyTimes[i]));
					module.setTitle(titles[i]);
					String requirement = "";
					if (!studyTimes[i].equals("0")) {
						requirement += "学习时长≥" + studyTimes[i] + " and ";
					}
					if (!requirement.equals("")) {
						requirement = requirement.substring(0, requirement.length() - 5);
					}
					module.setRequirement(requirement.equals("") ? null : requirement);
					editModule(module);
				} else { // 是页
					MeleteSectionModel section = (MeleteSectionModel) getModelById(MeleteSectionModel.class, new Long(
							ids[i]));

					section.setRequired(new Long(requireds[i]));
					section.setStudyTime(new Long(studyTimes[i]));
					section.setTitle(titles[i]);
					if (!studyTimes[i].equals("0")) {
						section.setRequirement("学习时长≥" + studyTimes[i]);
					}
					editSection(section);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sakaiproject.resource.api.course.service.ICourseService#initModuleScorm
	 * (org.w3c.dom.Node)
	 */
	// 根据课件资源的根节点获取模块列表树
	public String initModuleScorm(Node parentNode) {
		StringBuffer sb = new StringBuffer("");
		try {
			Vector childList = DOMTreeUtility.getNodes(parentNode, "item");// 下级模块集合
			for (int i = 0; i < childList.size(); i++) {
				Node childNode = (Node) childList.elementAt(i);
				String identifierref = DOMTreeUtility.getAttributeValue(childNode, "identifierref");
				if (identifierref == null || identifierref.equals("")) { // 是模块节点
					String identifier = DOMTreeUtility.getAttributeValue(childNode, "identifier");
					String title = DOMTreeUtility.getNodeValue(DOMTreeUtility.getNode(childNode, "title"));
					String subString = initModuleScorm(childNode);
					sb.append("{text:'" + title + "',");
					sb.append("expanded:true,");
					sb.append("icon:'" + CodeTable.icoModule + "',");
					sb.append("id:'" + identifier + "',");
					sb.append("checked:false,");
					if (subString.equals("")) { // 没有下级节点
						sb.append("leaf:true},");
					} else { // 有下级节点
						sb.append("children:[" + subString + "]},");
					}
				}
			}
			if (!sb.toString().equals("")) {
				sb.deleteCharAt(sb.length() - 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sakaiproject.resource.api.course.service.ICourseService#importModuleScorm
	 * (java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, org.w3c.dom.Node, java.util.Vector)
	 */
	// 导入选择的课件模块
	public List importModuleScorm(String courseId, String parentId, String parentType, String mods, String warePath,
			String coursePath, Node parentNode, Vector resources, String relaPath, List<String> resourceIdList)
			throws Exception {
		List<Object> resultList = new ArrayList<Object>();
		try {
			String[] mod = mods.split(";");
			Vector childList = DOMTreeUtility.getNodes(parentNode, "item");// 下级模块集合

			for (int i = 0; i < childList.size(); i++) {
				Node childNode = (Node) childList.elementAt(i);
				String identifierref = DOMTreeUtility.getAttributeValue(childNode, "identifierref");
				String identifier = DOMTreeUtility.getAttributeValue(childNode, "identifier");
				if (identifierref == null || identifierref.equals("")) { // 是模块节点
					if (mod[0].indexOf("," + identifier + ",") >= 0) { // 该模块选中
						String title = DOMTreeUtility.getNodeValue(DOMTreeUtility.getNode(childNode, "title"));
						MeleteModuleModel module = new MeleteModuleModel();
						Long idx = null;
						if (parentType.equals(CodeTable.course)) {// 父节点是课程节点
							// 查询兄弟节点的最大序号并生成新的序号
							idx = getMaxModuleIdxByCourseId(courseId) + 1;
						} else if (parentType.equals(CodeTable.module)) {// 父节点是模块节点
							module.setParentId(new Long(parentId));
							// 查询兄弟节点的最大序号并生成新的序号
							idx = getMaxModuleIdxByParentId(new Long(parentId)) + 1;
						}
						module.setCourseId(courseId);
						module.setIdx(idx);// 设置序号
						module.setTitle(title);// 设置模块标题
						String username = (String) ServletActionContext.getRequest().getSession()
								.getAttribute("userName");
						module.setCreatedByFname(username);// 设置创建人
						module.setCreatedByLname(username);// 为兼容原有melete中的createdByLname字段非空限制在此处设置与createdByFname相同
						module.setUserId(userDirectoryService.getCurrentUser().getId());// 设置创建人id
						module.setCreationDate(new Date());// 设置创建时间
						module.setPrerequids(CodeTable.prerequisiteNo);// 设置开启条件
						// module.setMinSecNum(new Long(0));// 设置最少选修页个数
						module.setRequired(new Long(CodeTable.required));// 设置必修选修
						module.setStatus(new Long(CodeTable.normal));// 设置状态
						module.setStudyDay(new Long(0));// 设置学习时间
						module.setAvgPassTime(0L);
						module.setRequirement(null);// 通过条件中文说明
						module.setKeywords(title);// 为兼容melete中Keywords的非空约束此处将该字段值设为title无实际用处
						Vector childrenList = DOMTreeUtility.getNodes(childNode, "item");// 下级模块集合
						if (childrenList.isEmpty()) {
							throw new Exception("模块\"" + module.getTitle() + "\"下页不存在, 引入失败");
						}
						Node childrenNode = (Node) childrenList.elementAt(0);
						String childRef = DOMTreeUtility.getAttributeValue(childrenNode, "identifierref");
						if (childRef == null || childRef.equals("")) {
							module.setChildType(CodeTable.module);
						} else {
							module.setChildType(CodeTable.section);
						}
						module.setModuleItemID(identifier);// 将模块与课件描述文件中的module的item元素中相关联
						String moduleId = this.editModule(module);
						resultList.add(module);

						if (parentType.equals(CodeTable.course)) {// 父节点是课程节点
							this.updateCourseCondition(courseId);
						} else if (parentType.equals(CodeTable.module)) {// 父节点是模块节点
							this.updateModuleCondition(parentId, CodeTable.module);
						}
						List sublist = this.importModuleScorm(courseId, moduleId, CodeTable.module, mods, warePath,
								coursePath, childNode, resources, relaPath, resourceIdList);
						resultList.addAll(sublist);
					} else if (mod[1].indexOf("," + identifier + ",") >= 0) { // 该模块没选中，但是它的下级模块有选中，遍历下级模块，判断是否选
						List sublist = this.importModuleScorm(courseId, parentId, parentType, mods, warePath,
								coursePath, childNode, resources, relaPath, resourceIdList);
						resultList.addAll(sublist);
					} else { // 若对应的模块即未被选中并且其子模块也未被选中则从parentNode中移除
						parentNode.removeChild(childNode);
					}
				} else { // 是页节点
					resourceIdList.add(identifierref);// 将包含的资源id加入到集合中
					String title = DOMTreeUtility.getNodeValue(DOMTreeUtility.getNode(childNode, "title"));
					MeleteSectionModel section = this.importSection(resources, identifierref, identifier, coursePath,
							warePath, parentId, title, relaPath, courseId);
					resultList.add(section);
					this.updateModuleCondition(parentId, CodeTable.section);
				}
			}
			return resultList;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 移除identifier属性值包含或者不包含(具体由isResourceIdListContain参数确定)
	 * 在resourceIdList中的resources中的元素
	 * 
	 * @param resources
	 *            resource元素集合
	 * @param resourceIdList
	 *            存放resource元素的identifier属性值的集合
	 * @param isResourceIdListContain
	 *            若值为true则移除identifier属性值包含在resourceIdList中的resource元素,
	 *            若为false则移除不包含在resourceIdList中的resource元素
	 */
	public void removeResource(Vector resources, List<String> resourceIdList, boolean isResourceIdListContain) {
		for (Iterator iterator = resources.iterator(); iterator.hasNext();) {
			Node resource = (Node) iterator.next();
			String resId = DOMTreeUtility.getAttributeValue(resource, "identifier");
			Node parentNode = resource.getParentNode();
			if (isResourceIdListContain) {
				if (resourceIdList.contains(resId)) {
					parentNode.removeChild(resource);
				}
			} else {
				if (!resourceIdList.contains(resId)) {
					parentNode.removeChild(resource);
				}
			}
		}
	}

	private void delResourceFile(Vector fileVector) {
		logger.debug("----进入方法delResourceFile----");
		try {
			Site site = this.getCurSite();
			logger.debug("---站点Id---" + site.getId());
			MeleteCourseModel course = this.getCourseBySiteId(site.getId());
			String coursePath = Constants.WEB_PATH + "/" + Constants.SECTION_PATH + "/" + course.getId() + "/";
			logger.debug("---coursePath---" + coursePath);
			for (Iterator iterator = fileVector.iterator(); iterator.hasNext();) {
				Node fileNode = (Node) iterator.next();
				String fileName = DOMTreeUtility.getAttributeValue(fileNode, "href");
				File resFile = new File(coursePath + fileName);
				if (resFile.exists()) {
					boolean delRes = resFile.delete();// 删除资源文件
					if (delRes) {
						logger.debug("---删除课件资源文件---" + resFile.getAbsolutePath() + "--=成功=--");
					} else {
						logger.debug("---删除课件资源文件---" + resFile.getAbsolutePath() + "--=失败=--");
					}
				} else {
					logger.info("---资源文件文件---" + resFile.getAbsolutePath() + "---不存在无法删除---");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("---方法delResourceFile出现异常未正确结束---");
			logger.error(e.getMessage(), e);
		}
		logger.debug("---结束方法delResourceFile---");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sakaiproject.resource.api.course.service.ICourseService#initSectionScorm
	 * (org.w3c.dom.Node)
	 */
	// 根据课件资源的根节点获取页列表树
	public String initSectionScorm(Node parentNode) throws Exception {
		StringBuffer sb = new StringBuffer("");
		try {
			Vector childList = DOMTreeUtility.getNodes(parentNode, "item");// 下级模块集合
			for (int i = 0; i < childList.size(); i++) {
				Node childNode = (Node) childList.elementAt(i);
				String identifierref = DOMTreeUtility.getAttributeValue(childNode, "identifierref");
				String identifier = DOMTreeUtility.getAttributeValue(childNode, "identifier");
				String title = DOMTreeUtility.getNodeValue(DOMTreeUtility.getNode(childNode, "title"));
				if (identifierref == null || identifierref.equals("")) { // 是模块节点
					String subString = initSectionScorm(childNode);
					sb.append("{text:'" + title + "',");
					sb.append("expanded:false,");// 模块节点不展开
					sb.append("icon:'" + CodeTable.icoModule + "',");
					sb.append("id:'',");
					sb.append("children:[" + subString + "]},");
				} else {
					sb.append("{text:'" + title + "',");
					sb.append("expanded:false,");
					sb.append("icon:'" + CodeTable.icoSection + "',");
					/**
					 * 因为要将导入的section的信息写入课程目录中的描述文件中
					 * 所以将id更改为{identifierref;identifier}的形式
					 * 
					 * @author zhangxin
					 */
					sb.append("id:'" + identifierref + ";" + identifier + "',");
					sb.append("checked:false,");
					sb.append("leaf:true},");
				}
			}
			if (!sb.toString().equals("")) {
				sb.deleteCharAt(sb.length() - 1);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	/**
	 * 根据课件资源的根节点获取课件预览页列表树
	 * 
	 * @param wardId
	 * @return
	 * @throws Exception
	 */
	public String initSectionScormPreview(String wareId,Node parentNode,Vector resources) throws Exception{
		StringBuffer sb = new StringBuffer("");
		try {
			Vector orgs = DOMTreeUtility.getNodes(parentNode, "item");// 下级模块集合
			for (int i = 0; i < orgs.size(); i++) {
				Node childNode = (Node) orgs.elementAt(i);
				String identifierref = DOMTreeUtility.getAttributeValue(childNode, "identifierref");
				String identifier = DOMTreeUtility.getAttributeValue(childNode, "identifier");
				String title = DOMTreeUtility.getNodeValue(DOMTreeUtility.getNode(childNode, "title"));
				
				if (identifierref == null || identifierref.equals("")) { // 是模块节点
					String subString = initSectionScormPreview(wareId,childNode,resources);
					sb.append("{text:'" + title + "',");
					sb.append("expanded:false,");// 模块节点不展开
					sb.append("icon:'" + CodeTable.icoModule + "',");
					sb.append("id:'',");
					sb.append("children:[" + subString + "]},");
				} else {
					sb.append("{text:'" + title + "',");
					sb.append("expanded:false,");
					sb.append("icon:'" + CodeTable.icoSection + "',");
					sb.append("id:'" + identifierref + ";" + identifier + "',");
					String sectionPath = new File(Constants.COURSEWARE_URL) + "/" + wareId + "/";
					   
					for (int j = 0; j < resources.size(); j++) { // 遍历资源标签
						Node resourceNode = (Node) resources.elementAt(j); // 获取资源节点
						String resourceID = DOMTreeUtility.getAttributeValue(resourceNode, "identifier"); // 获取节点的identifier属性
						if (identifierref.equals(resourceID)) { // 页所对应的资源节点
							String nodeHref = DOMTreeUtility.getAttributeValue(resourceNode, "href");// htm路径
							decodeHandler decoder = new decodeHandler(nodeHref, "UTF-8");
							decoder.decodeName();
							sb.append("url:'"+sectionPath.replace("\\", "/")+decoder.getDecodedFileName()+ "',");
							break;
						}
					}
					
					sb.append("checked:false,");
					sb.append("leaf:true},");
				}
			}
			if (!sb.toString().equals("")) {
				sb.deleteCharAt(sb.length() - 1);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.sakaiproject.resource.api.course.service.ICourseService#
	 * importSectionScorm(java.lang.String[], java.lang.String[],
	 * java.util.Vector, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	// 保存从资源包中引入的页
	public List<String> importSectionScorm(String[] idAndIdRef, String[] titles, Vector resources, String moduleId,
			String warePath, String coursePath, String relaPath, String courseId) throws Exception {
		List<String> resultList = new ArrayList<String>();
		try {
			for (int i = 0; i < idAndIdRef.length; i++) {
				String[] splitIdAndIdRef = idAndIdRef[i].split(";");// 将idAndIdRef中存放的identifierref和identifer分离并分别放入refs与ids中
				String refs = splitIdAndIdRef[0];
				String ids = splitIdAndIdRef[1];
				this.importSection(resources, refs, ids, coursePath, warePath, moduleId, titles[i], relaPath, courseId);
				resultList.add(refs);
			}
			MeleteModuleModel parentModel = (MeleteModuleModel) this.getModelById(MeleteModuleModel.class, new Long(
					moduleId));
			parentModel.setChildType(CodeTable.section);
			this.editModule(parentModel);

			return resultList;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 复制scrom里的htm文件时调用
	 * 
	 * @param iURL
	 * @param iParameters
	 * @return
	 */
	private String addParameters(String iURL, String iParameters) {
		if ((iURL.length() == 0) || (iParameters.length() == 0)) {
			return iURL;
		}
		while ((iParameters.charAt(0) == '?') || (iParameters.charAt(0) == '&')) {
			iParameters = iParameters.substring(1);
		}
		if (iParameters.charAt(0) == '#') {
			if ((iURL.indexOf('#') != -1) || (iURL.indexOf('?') != -1)) {
				return iURL;
			}
		}
		if (iURL.indexOf('?') != -1) {
			iURL = iURL + '&';
		} else {
			iURL = iURL + '?';
		}
		iURL = iURL + iParameters;

		return iURL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sakaiproject.resource.api.course.service.ICourseService#getNextModule
	 * (java.lang.String)
	 */
	// 根据模块id查询下一个模块
	public MeleteModuleModel getNextModule(String moduleId) throws Exception {
		try {
			MeleteModuleModel module = (MeleteModuleModel) this.getModelById(MeleteModuleModel.class,
					new Long(moduleId));
			Long parentId = module.getParentId();
			Long idx = module.getIdx();
			if (parentId != null) {
				String hql = "from MeleteModuleModel where parentId=? and idx>? and status=? order by idx";
				Object[] param = { parentId, idx, new Long(CodeTable.normal) };
				List<MeleteModuleModel> list = this.findEntity(hql, param);
				if (list.isEmpty() || list.size() == 0) {
					return null;
				} else {
					return list.get(0);
				}
			} else {
				String courseId = module.getCourseId();
				String hql = "from MeleteModuleModel where courseId=? and parentId is null and idx>? and status=? order by idx";
				Object[] param = { courseId, idx, new Long(CodeTable.normal) };
				List<MeleteModuleModel> list = this.findEntity(hql, param);
				if (list.isEmpty() || list.size() == 0) {
					return null;
				} else {
					return list.get(0);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sakaiproject.resource.api.course.service.ICourseService#getNextSection
	 * (java.lang.String)
	 */
	// 根据页id查询下一页
	public MeleteSectionModel getNextSection(String sectionId) throws Exception {
		try {
			MeleteSectionModel section = (MeleteSectionModel) this.getModelById(MeleteSectionModel.class, new Long(
					sectionId));
			Long moduleId = section.getModuleId();
			Long idx = section.getIdx();
			String hql = "from MeleteSectionModel where moduleId=? and idx>? and status=? order by idx";
			Object[] param = { moduleId, idx, new Long(CodeTable.normal) };
			List<MeleteSectionModel> list = this.findEntity(hql, param);
			if (list.isEmpty() || list.size() == 0) {
				return null;
			} else {
				return list.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sakaiproject.resource.api.course.service.ICourseService#getPreviousModule
	 * (java.lang.String)
	 */
	// 根据模块id查询上一个模块
	public MeleteModuleModel getPreviousModule(String moduleId) throws Exception {
		try {
			MeleteModuleModel module = (MeleteModuleModel) this.getModelById(MeleteModuleModel.class,
					new Long(moduleId));
			Long parentId = module.getParentId();
			Long idx = module.getIdx();
			if (parentId != null) {
				String hql = "from MeleteModuleModel where parentId=? and idx<? and status=? order by idx desc";
				Object[] param = { parentId, idx, new Long(CodeTable.normal) };
				List<MeleteModuleModel> list = this.findEntity(hql, param);
				if (list.isEmpty() || list.size() == 0) {
					return null;
				} else {
					return list.get(0);
				}
			} else {
				String courseId = module.getCourseId();
				String hql = "from MeleteModuleModel where courseId=? and parentId is null and idx<? and status=? order by idx desc";
				Object[] param = { courseId, idx, new Long(CodeTable.normal) };
				List<MeleteModuleModel> list = this.findEntity(hql, param);
				if (list.isEmpty() || list.size() == 0) {
					return null;
				} else {
					return list.get(0);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.sakaiproject.resource.api.course.service.ICourseService#
	 * getPreviousSection(java.lang.String)
	 */
	// 根据页id查询上一页
	public MeleteSectionModel getPreviousSection(String sectionId) throws Exception {
		try {
			MeleteSectionModel section = (MeleteSectionModel) this.getModelById(MeleteSectionModel.class, new Long(
					sectionId));
			Long moduleId = section.getModuleId();
			Long idx = section.getIdx();
			String hql = "from MeleteSectionModel where moduleId=? and idx<? and status=? order by idx desc";
			Object[] param = { moduleId, idx, new Long(CodeTable.normal) };
			List<MeleteSectionModel> list = this.findEntity(hql, param);
			if (list.isEmpty() || list.size() == 0) {
				return null;
			} else {
				return list.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sakaiproject.resource.api.course.service.ICourseService#getFirstModule
	 * (java.lang.String)
	 */
	// 根据模块id查询第一个模块
	public MeleteModuleModel getFirstModule(String moduleId) throws Exception {
		try {
			MeleteModuleModel module = (MeleteModuleModel) this.getModelById(MeleteModuleModel.class,
					new Long(moduleId));
			Long parentId = module.getParentId();
			Long idx = module.getIdx();
			if (parentId != null) {
				String hql = "from MeleteModuleModel where parentId=? and idx<=? and status=? order by idx";
				Object[] param = { parentId, idx, new Long(CodeTable.normal) };
				List<MeleteModuleModel> list = this.findEntity(hql, param);
				if (list.isEmpty() || list.size() == 0) {
					return null;
				} else {
					return list.get(0);
				}
			} else {
				String courseId = module.getCourseId();
				String hql = "from MeleteModuleModel where courseId=? and parentId is null and idx<=? and status=? order by idx";
				Object[] param = { courseId, idx, new Long(CodeTable.normal) };
				List<MeleteModuleModel> list = this.findEntity(hql, param);
				if (list.isEmpty() || list.size() == 0) {
					return null;
				} else {
					return list.get(0);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sakaiproject.resource.api.course.service.ICourseService#getFirstSection
	 * (java.lang.String)
	 */
	// 根据页id查询第一页
	public MeleteSectionModel getFirstSection(String sectionId) throws Exception {
		try {
			MeleteSectionModel section = (MeleteSectionModel) this.getModelById(MeleteSectionModel.class, new Long(
					sectionId));
			Long moduleId = section.getModuleId();
			Long idx = section.getIdx();
			String hql = "from MeleteSectionModel where moduleId=? and idx<=? and status=? order by idx ";
			Object[] param = { moduleId, idx, new Long(CodeTable.normal) };
			List<MeleteSectionModel> list = this.findEntity(hql, param);
			if (list.isEmpty() || list.size() == 0) {
				return null;
			} else {
				return list.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sakaiproject.resource.api.course.service.ICourseService#getLastModule
	 * (java.lang.String)
	 */
	// 根据模块id查询最后一个模块
	public MeleteModuleModel getLastModule(String moduleId) throws Exception {
		try {
			MeleteModuleModel module = (MeleteModuleModel) this.getModelById(MeleteModuleModel.class,
					new Long(moduleId));
			Long parentId = module.getParentId();
			Long idx = module.getIdx();
			if (parentId != null) {
				String hql = "from MeleteModuleModel where parentId=? and idx>=? and status=? order by idx desc";
				Object[] param = { parentId, idx, new Long(CodeTable.normal) };
				List<MeleteModuleModel> list = this.findEntity(hql, param);
				if (list.isEmpty() || list.size() == 0) {
					return null;
				} else {
					return list.get(0);
				}
			} else {
				String courseId = module.getCourseId();
				String hql = "from MeleteModuleModel where courseId=? and parentId is null and idx>=? and status=? order by idx desc";
				Object[] param = { courseId, idx, new Long(CodeTable.normal) };
				List<MeleteModuleModel> list = this.findEntity(hql, param);
				if (list.isEmpty() || list.size() == 0) {
					return null;
				} else {
					return list.get(0);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sakaiproject.resource.api.course.service.ICourseService#getLastSection
	 * (java.lang.String)
	 */
	// 根据页id查询最后一页
	public MeleteSectionModel getLastSection(String sectionId) throws Exception {
		try {
			MeleteSectionModel section = (MeleteSectionModel) this.getModelById(MeleteSectionModel.class, new Long(
					sectionId));
			Long moduleId = section.getModuleId();
			Long idx = section.getIdx();
			String hql = "from MeleteSectionModel where moduleId=? and idx>=? and status=? order by idx desc";
			Object[] param = { moduleId, idx, new Long(CodeTable.normal) };
			List<MeleteSectionModel> list = this.findEntity(hql, param);
			if (list.isEmpty() || list.size() == 0) {
				return null;
			} else {
				return list.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.sakaiproject.resource.api.course.service.ICourseService#
	 * countElectiveByParentId(java.lang.Long)
	 */
	// 根据上级模块id查询下级选修模块总个数
	public Long countElectiveByParentId(Long parentId) throws Exception {
		try {
			String hql = "select count(*) from MeleteModuleModel where parentId = ? and status=? and required=?";
			Object[] parameters = { parentId, new Long(CodeTable.normal), new Long(CodeTable.elective) };
			List<Object> countList = this.findEntity(hql, parameters);
			if (countList.isEmpty() || countList.size() == 0) {
				return new Long(0);
			} else {
				return new Long(countList.get(0).toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.sakaiproject.resource.api.course.service.ICourseService#
	 * countRequiredByParentId(java.lang.Long)
	 */
	// 根据上级模块id查询下级必修模块总个数
	public Long countRequiredByParentId(Long parentId) throws Exception {
		try {
			String hql = "select count(*) from MeleteModuleModel where parentId = ? and status=? and required=?";
			Object[] parameters = { parentId, new Long(CodeTable.normal), new Long(CodeTable.required) };
			List<Object> countList = this.findEntity(hql, parameters);
			if (countList.isEmpty() || countList.size() == 0) {
				return new Long(0);
			} else {
				return new Long(countList.get(0).toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.sakaiproject.resource.api.course.service.ICourseService#
	 * updateModuleCondition(java.lang.String, java.lang.String)
	 */
	// 更新模块的通过条件
	public void updateModuleCondition(String id, String childType) throws Exception {
		// TODO Empty Method
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.sakaiproject.resource.api.course.service.ICourseService#
	 * updateCourseCondition(java.lang.String)
	 */
	// 更新课程的通过条件
	public void updateCourseCondition(String id) throws Exception {
		// TODO Empty Method
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.sakaiproject.resource.api.course.service.ICourseService#
	 * updateSectionCondition(java.lang.String)
	 */
	// 更新页的通过条件
	public void updateSectionCondition(String id, String studyTime) throws Exception {
		// TODO Empty Method
	}

	/**
	 * 根据站点ID所有的作业和前测
	 * 
	 * @return
	 * @throws Exception
	 */
	// 根据站点ID所有的作业和前测的ID与名称
	public List<Object> getTestsAndSelfTestsBySiteId(String siteId) throws Exception {
		try {
			MeleteCourseModel course = this.getCourseBySiteId(siteId);
			String courseId = course.getId();
			List<Object> returnList = new ArrayList<Object>();

			returnList.addAll(this.getAllTest(courseId));
			returnList.addAll(this.getAllSelfTest(courseId));

			return returnList;

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 根据 作业和前测的ID 批改状态 截止时间 查询要批改的试卷
	 * 
	 * @param testId
	 *            作业和前测的ID
	 * @param checkStatus
	 *            批改状态
	 * @param endTime
	 *            截止时间
	 * @param testType
	 *            试卷类型 1 为作业 2 为前测
	 * @return
	 * @throws Exception
	 */
	// 根据 作业和前测的ID 批改状态 截止时间 查询要批改的试卷
	public Object[] findCheckWorkList(String testId, String checkStatus, String endTime, String testType, int start)
			throws Exception {
		Object[] results = null;
		List<Object> resultList = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = sdf.parse(endTime);
		Map parameters = new HashMap();
		parameters.put("testId", Long.parseLong(testId));
		parameters.put("checkStatus", checkStatus);
		parameters.put("endTime", date);
		QueryString hql = new QueryString();
		if ("1".equals(testType)) {
			// 如果是作业
			hql.setSelect("select new Map(sakaiuser.userId as userid,sakaiuser.firstName as studentName, sakaiuser.publicstunum as stuNum,"
					+ "sakaiuser.organizationName as eduCenter,testattempt.score as score,testattempt.checkstatus as checkstatus,testattempt.testattemptId as id,"
					+ "testattempt.testPaperid as paperid,testattempt.courseId as courseid, testrecord.studyrecordId as studyrecordId) ");
			hql.setFrom("from MeleteSakaiUserModel sakaiuser, MeleteStudyRecordModel studyrecord, MeleteTestRecordModel testrecord,MeleteTestAttemptModel testattempt ");
			hql.setWhere("where sakaiuser.userId=studyrecord.studentId and studyrecord.studyrecordId=testrecord.studyrecordId and testrecord.testrecordId=testattempt.meleteTestRecordId"
					+ " and testrecord.testId=:testId and testattempt.pagerstatus=:checkStatus and testattempt.endTime<=:endTime");//
			results = this.findEntity(hql, parameters, LIMIT, start);// 分页查询，返回一个对象数组

		} else if ("2".equals(testType)) {
			// 如果是前测
			hql.setSelect("select new Map(sakaiuser.userId as userid,sakaiuser.firstName as studentName, sakaiuser.publicstunum as stuNum,"
					+ "sakaiuser.organizationName as eduCenter,selftestattempt.score as score,selftestattempt.checkstatus as checkstatus,"
					+ "selftestattempt.selftestattemptId as id,selftestattempt.selftestPaperid as paperid,selftestattempt.courseId as courseid,selftestrecord.studyrecordId as studyrecordId) ");

			hql.setFrom("from MeleteSakaiUserModel sakaiuser, MeleteStudyRecordModel studyrecord, MeleteSelftestRecordModel selftestrecord,MeleteSelftestAttemptModel selftestattempt ");
			hql.setWhere("where sakaiuser.userId=studyrecord.studentId and studyrecord.studyrecordId=selftestrecord.studyrecordId and selftestrecord.selftestrecordId=selftestattempt.meleteSelftestRecordId"
					+ " and selftestrecord.selftestId=:testId and selftestattempt.pagerstatus=:checkStatus and selftestattempt.endTime<=:endTime");
			results = this.findEntity(hql, parameters, LIMIT, start);// 分页查询，返回一个对象数组
		}

		return results;
	}

	/**
	 * 通过学习记录ID 和 页节点ID 获得节点记录
	 * 
	 * @param studyRecordId
	 * @param sectionId
	 * @return
	 * @throws Exception
	 */
	// 通过学习记录ID 和 页节点ID 获得节点记录
	public MeleteSectionRecordModel findSectionRecord(Long studyRecordId, String sectionId) throws Exception {
		String hql = " from MeleteSectionRecordModel sectionRecord where sectionRecord.studyrecordId=? and sectionRecord.sectionId=?";
		Object[] params = { studyRecordId, new Long(sectionId) };
		List resultList = this.findEntity(hql, params);
		if (resultList != null && !resultList.isEmpty() && resultList.size() > 0) {
			return (MeleteSectionRecordModel) resultList.get(0);
		}
		return null;
	}

	/**
	 * 通过页ID 获得模块
	 * 
	 * @param sectionId
	 *            页ID
	 * @return
	 * @throws Exception
	 */
	// 通过页ID 获得模块
	public MeleteModuleModel getModuleBySectionId(String sectionId) throws Exception {

		String hql = "select module from MeleteModuleModel module, MeleteSectionModel section where section.moduleId=module.id and section.id=?";
		Object[] params = { new Long(sectionId) };
		List resultList = findEntity(hql, params);
		if (resultList != null && !resultList.isEmpty() && resultList.size() > 0) {
			return (MeleteModuleModel) resultList.get(0);
		}
		return null;
	}

	/**
	 * 通过页ID 获得页
	 * 
	 * @param sectionId
	 *            页ID
	 * @return
	 * @throws Exception
	 */
	// 通过页ID 获得页
	public MeleteSectionModel getSectionById(String sectionId) throws Exception {
		MeleteSectionModel section = (MeleteSectionModel) this.findEntityById(MeleteSectionModel.class, new Long(
				sectionId));

		return section;
	}

	// 获得所有讨论
	public List<MeleteForumModel> getAllForum(String courseId) throws Exception {
		List<MeleteForumModel> returnList = new ArrayList<MeleteForumModel>();
		Object[] parameters = { courseId, new Long(CodeTable.normal) };
		String hql = "select forum from MeleteModuleModel module,MeleteForumModel forum "
				+ "where module.courseId=? and module.id=forum.moduleId and forum.status=?";
		returnList.addAll(this.findEntity(hql, parameters));

		hql = "select forum from MeleteSectionModel section,MeleteForumModel forum "
				+ "where section.courseId=? and section.id=forum.sectionId and forum.status=?";
		returnList.addAll(this.findEntity(hql, parameters));
		return returnList;
	}

	// 获得所有模块
	public List<MeleteModuleModel> getAllModule(String courseId) throws Exception {
		String hql = "from MeleteModuleModel where courseId=? and status=?";
		Object[] param = { courseId, Long.valueOf(CodeTable.normal) };
		List list = this.getHibernateTemplate().find(hql, param);
		return list;
	}

	// 获得所有页
	public List<MeleteSectionModel> getAllSection(String courseId) throws Exception {
		String hql = "from MeleteSectionModel where courseId=? and status=?";
		Object[] param = { courseId, Long.valueOf(CodeTable.normal) };
		List list = this.getHibernateTemplate().find(hql, param);
		return list;
	}

	// 获得所有前测
	public List<MeleteSelfTestModel> getAllSelfTest(String courseId) throws Exception {
		List<MeleteSelfTestModel> returnList = new ArrayList<MeleteSelfTestModel>();
		Object[] parameters = { courseId, new Long(CodeTable.normal) };
		String hql = "select test from MeleteModuleModel module,MeleteSelfTestModel test "
				+ "where module.courseId=? and module.id=test.moduleId and test.status=?";
		returnList.addAll(this.findEntity(hql, parameters));

		hql = "select test from MeleteSectionModel section,MeleteSelfTestModel test "
				+ "where section.courseId=? and section.id=test.sectionId and test.status=?";
		returnList.addAll(this.findEntity(hql, parameters));
		return returnList;
	}

	// 获得所有考试
	public List<MeleteTestModel> getAllTest(String courseId) throws Exception {
		List<MeleteTestModel> returnList = new ArrayList<MeleteTestModel>();

		Object[] parameters = { courseId, new Long(CodeTable.normal) };
		String hql = "select test from MeleteModuleModel module,MeleteTestModel test "
				+ "where module.courseId=? and module.id=test.moduleId " + "and test.status=?";
		returnList.addAll(this.findEntity(hql, parameters));

		hql = "select test from MeleteSectionModel section,MeleteTestModel test "
				+ "where section.courseId=? and section.id=test.sectionId " + "and test.status=?";
		returnList.addAll(this.findEntity(hql, parameters));
		return returnList;
	}
	
	/**
	 * 得到test
	 * @param testId
	 * @return
	 * @throws Exception
	 */
	public MeleteTestModel getTest(Long testId) throws Exception{
		return (MeleteTestModel)this.findEntityById(MeleteTestModel.class, testId);
	}
	
	/**
	 * 获得旧课程空间的作业
	 */
	public List<MeleteTestModel> getAllTestByOldMelete(String courseId) throws Exception {
		List<MeleteTestModel> returnList = new ArrayList<MeleteTestModel>();
		
		String sql = "select test_id from melete_test moduletest0_ left outer join melete_module module2_ " +
				"on moduletest0_.MODULE_ID=module2_.MODULE_ID, melete_course_module" +
				" coursemodu1_ where moduletest0_.BELONG_TYPE='MODULETEST'" +
				" and coursemodu1_.DELETE_FLAG=0 " +
				"and coursemodu1_.COURSE_ID=? and moduletest0_.MODULE_ID=coursemodu1_.MODULE_ID and moduletest0_.COUNT=1";
		
		List list = this.getSession().createSQLQuery(sql).setString(0, courseId).list();

		if(list != null && !list.isEmpty()){
			StringBuffer hql = new StringBuffer();
			hql.append("from MeleteTestModel test where test.id in (");
			for(Object o : list){
				hql.append(o.toString()+",");
			}
			hql.deleteCharAt(hql.length()-1);
			hql.append(")");
			returnList.addAll(this.findEntity(hql.toString()));
		}
        sql = "select test_id from melete_test sectiontes0_ left outer join melete_section section2_ on sectiontes0_.SECTION_ID=section2_.SECTION_ID," +
        		"melete_course_module coursemodu1_ where sectiontes0_.BELONG_TYPE='SECTIONTEST' " +
        		"and section2_.DELETE_FLAG=0 and coursemodu1_.DELETE_FLAG=0 and " +
        		"coursemodu1_.COURSE_ID=? and section2_.MODULE_ID=coursemodu1_.MODULE_ID and " +
        		"section2_.SECTION_ID=sectiontes0_.SECTION_ID and sectiontes0_.COUNT=1";
		
		list = this.getSession().createSQLQuery(sql).setString(0, courseId).list();
		
		if(list != null && !list.isEmpty()){
			StringBuffer hql = new StringBuffer();
			hql.append("from MeleteTestModel test where test.id in (");
			for(Object o : list){
				hql.append(o.toString()+",");
			}
			hql.deleteCharAt(hql.length()-1);
			hql.append(")");
			returnList.addAll(this.findEntity(hql.toString()));
		}
		return returnList;
	}

	/**
	 * 根据条件查询一门课程下要增加印象分的学生相关信息
	 * 
	 * @param eduCenterId
	 * @param stuName
	 * @param stuNum
	 * @param courseId
	 * @param start
	 * @return
	 * @throws Exception
	 */
	// 根据条件查询一门课程下要增加印象分的学生相关信息
	public Object[] findImpScoreStudents(String eduCenterId, String stuName, String stuNum, String courseId, int start)
			throws Exception {
		Object[] results = null;
		try {
			Map parameters = new HashMap();
			StringBuffer hqlWhere = new StringBuffer(
					"where sakaiUser.userId=studyRecord.studentId and studyRecord.courseId=:courseId and sakaiUser.stunum is not null");
			parameters.put("courseId", courseId);

			if (eduCenterId != null && !"".equals(eduCenterId)) {
				hqlWhere.append(" and sakaiUser.organizationId=:eduCenterId");
				parameters.put("eduCenterId", eduCenterId);
			}
			if (stuName != null && !"".equals(stuName)) {
				hqlWhere.append(" and sakaiUser.firstName like :stuName");
				parameters.put("stuName", "%" + stuName + "%");
			}
			if (stuNum != null && !"".equals(stuNum)) {
				hqlWhere.append(" and sakaiUser.stunum like :stuNum");
				parameters.put("stuNum", "%" + stuNum + "%");
			}
			QueryString hql = new QueryString();
			hql.setSelect("select new Map(studyRecord.studyrecordId as id,sakaiUser.firstName as stuName,sakaiUser.stunum as stuNum,studyRecord.score as score,sakaiUser.organizationName as eduCenter,sakaiUser.userId as userId,studyRecord.studyrecordId as studyrecordId) ");
			hql.setFrom("from MeleteSakaiUserModel sakaiUser, MeleteStudyRecordModel studyRecord ");
			hql.setWhere(hqlWhere.toString());
			results = this.findEntity(hql, parameters, LIMIT, start);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
		return results;
	}

	/**
	 * 获得课程目录下的课件描述文件imsmanifest.xml的document对象
	 * 
	 * @param coursePath
	 *            课程目录
	 */
	public Document getXmlDocument(String coursePath) throws Exception {
		ADLDOMParser xmlParser = new ADLDOMParser();
		xmlParser.createDocument(coursePath + "imsmanifest.xml", true, false);
		return xmlParser.getDocument();
	}

	/**
	 * 将添加的页放入课程清单文件的中
	 * 
	 * @param fileName
	 * @param servicePath
	 * @param moduleId
	 * @param courseId
	 * @param section
	 * @throws Exception
	 */
	public void addSectionToManifest(String fileName, String servicePath, String moduleId, String courseId,
			MeleteSectionModel section) throws Exception {
		// 将新加的section写入课程目录中的imsmanifest.xml文件中
		String coursePath = servicePath + "/" + Constants.SECTION_PATH + "/" + courseId + "/";// 课程课件存放目录
		Document xmlDoc = this.getXmlDocument(coursePath);// 获得课件包描述清单文件imsmanifest.xml
		// 的document
		Node orgNode = getOrgNode(xmlDoc);
		Vector items = DOMTreeUtility.getNodes(orgNode, "item");
		MeleteModuleModel module = (MeleteModuleModel) this.getModelById(MeleteModuleModel.class, new Long(moduleId));
		Node pItemNode = this.getItemNode(items, module.getModuleItemID());
		Element sectionElement = this.createItem(section.getSectionItemId(), xmlDoc, section.getTitle(), pItemNode);
		this.createRescource(fileName, xmlDoc, sectionElement);
		// 将seciton的item子元素加入到对应module的item元素中
		pItemNode.appendChild(sectionElement);
		// 将文档写入课件清单文件imsmanifest.xml中
		this.writeManifest(new File(coursePath + "imsmanifest.xml"), xmlDoc);
	}

	/**
	 * 将页资源添加到课件描述文件文档对象中
	 * 
	 * @param fileName
	 * @param xmlDoc
	 * @param sectionItem
	 * @return
	 */
	private void createRescource(String fileName, Document xmlDoc, Element sectionItem) {
		// 插入子节点时，如果子节点没有设置namespace，会自动插入一个空的xmlns=""
		// 将子节点的namespace设置成和父节点一样，xmlns这一项就不会在xml文件中显示
		Element sectionResource = xmlDoc.createElementNS(sectionItem.getNamespaceURI(), "resource");
		String resourceId = CourseUtil.getResId();
		Element manifest = xmlDoc.getDocumentElement();
		String attrNS = manifest.getAttribute("xmlns:adlcp");
		// 添加resource元素的属性
		sectionResource.setPrefix(sectionItem.getPrefix());
		sectionResource.setAttribute("identifier", resourceId);
		sectionResource.setAttribute("type", "webcontent");
		sectionResource.setAttribute("href", fileName);
		Attr att = xmlDoc.createAttributeNS(attrNS, "scormType");// 创建属性scormType
		att.setPrefix("adlcp");// 增加该属性前缀
		att.setValue("asset");// 设置属性值
		sectionResource.setAttributeNode(att);
		sectionItem.setAttribute("identifierref", resourceId);// 设置item元素的identifierref属性值为resource元素的identifier值

		// 插入子节点时，如果子节点没有设置namespace，会自动插入一个空的xmlns=""
		// 将子节点的namespace设置成和父节点一样，xmlns这一项就不会在xml文件中显示
		Element sectionFile = xmlDoc.createElementNS(sectionItem.getNamespaceURI(), "file");// 创建file子元素
		sectionFile.setAttribute("href", fileName);
		// 将file子元素添加到resource元素中
		sectionResource.appendChild(sectionFile);
		Node manifestElement = xmlDoc.getDocumentElement();
		// 将resource子元素加入到resources元素中
		Node resourcesNode = DOMTreeUtility.getNode(manifestElement, "resources");
		resourcesNode.appendChild(sectionResource);
	}

	/**
	 * 将选择的模块添加到相应的课程模块中
	 * 
	 * @param tempOrg
	 *            处理后的课件目录下的描述文件中的课程组织结果
	 * @param resourcesNode
	 *            处理后的课件目录下的资源元素
	 * @param courseDocument
	 * @param pIdAndImportItemId
	 *            要导入到的父模块节点的数据库记录id和所选择的模块的课件描述文件中的item元素的属性identifier的值
	 */
	private void toModuleItem(Node tempOrg, Node resourcesNode, Document courseDocument, String pIdAndImportItemId)
			throws Exception {
		String[] ids = pIdAndImportItemId.split(";");
		String parentId = ids[0];// 要导入的父模块
		String[] selectedItemIds = ids[1].split(",");// 所选择的模块或页在课件描述文件中的item中的identifier属性值
		MeleteModuleModel module = (MeleteModuleModel) getModelById(MeleteModuleModel.class, new Long(parentId));
		Element manifestElement = courseDocument.getDocumentElement();
		Vector orgVector = ManifestHandler.getOrganizationNodes(manifestElement, false);
		Node orgNode = (Node) orgVector.elementAt(0);
		Vector courseItems = DOMTreeUtility.getNodes(orgNode, "item");
		// 获得要导入到课程中的模块的父节点
		Node moduleNode = this.getItemNode(courseItems, module.getModuleItemID());
		Vector items = DOMTreeUtility.getNodes(tempOrg, "item");
		for (int i = 0; i < selectedItemIds.length; i++) {
			if ("".equals(selectedItemIds[i])) {// 若为空串不进行下面操作
				continue;
			}
			Node selectItemNode = this.getItemNode(items, selectedItemIds[i]);// 获得已选课件模块或页节点
			moduleNode.appendChild(courseDocument.importNode(selectItemNode, true));// 将获得所选课件模块item元素加入到所属的课程模块中
		}
		// 将所选模块包含的页的资源添加到课程描述文件的文档对象中
		this.toCourseResources(resourcesNode, courseDocument);
	}

	/**
	 * 将处理过的课件目录的课件描述文件的document对象写入到课程目录中的imsmanifest.xml文件
	 * 
	 * @param coursePath
	 * @param mDocument
	 * @param tempOrg
	 * @param resourcesNode
	 * @param pIdAndImportItemId
	 *            针对父节点为模块的情况要导入的父节点ID与要导入的模块或页item元素的identifier属性值用";"隔开
	 * @param parentType
	 *            父节点类型 课程 模块
	 * @throws Exception
	 */
	public void toCourseManifest(String coursePath, Document mDocument, Node tempOrg, Node resourcesNode,
			String parentType, String pIdAndImportItemId) throws Exception {
		logger.debug("------进入toCourseManifest方法-------");
		// 课程目录下是否存在课件描述文件imsmanifest.xml
		File imsmanifestFile = new File(coursePath + "imsmanifest.xml");
		logger.debug("---要写入的imsmanifest路径----" + imsmanifestFile.getAbsolutePath());
		Document courseDocument = null;
		if (imsmanifestFile.exists()) {// 若存在则创建对应的document对象
			ADLDOMParser courseDocParser = new ADLDOMParser();
			courseDocParser.createDocument(imsmanifestFile.getAbsolutePath(), true, false);
			courseDocument = courseDocParser.getDocument();
		}

		// 若courseDocument为空则值为mDocument
		// 若courseDocument不为空则将处理过的mDocument中的模块几点集合moduleList与资源节点集合resources
		// 整合到courseDocument中
		if (courseDocument == null) {
			courseDocument = mDocument;
		} else if (CodeTable.course.equals(parentType)) {// 向课程根节点中导入
			String importItemIds = pIdAndImportItemId.split(";")[1];// 获得导入的模块item的identifier值
			this.toCourseRoot(tempOrg, resourcesNode, courseDocument, importItemIds);
		} else if (CodeTable.module.equals(parentType)) {// 若向模块节点中导入
			this.toModuleItem(tempOrg, resourcesNode, courseDocument, pIdAndImportItemId);
		}
		this.writeManifest(imsmanifestFile, courseDocument);
	}

	/**
	 * 向课程目录中的课件描述文件中的课程根节点(即organization元素中直接导入)导入课件信息
	 * 
	 * @param tempOrg
	 *            课件资源库中的课件描述文件中的课程根节点
	 * @param resourcesNode
	 *            课件资源库中的课件描述文件中的资源(resources)节点
	 * @param courseDocument
	 *            课程目录中的课件描述文件的文档对象
	 * @param importItemIds
	 *            要导入选择的模块的课件描述文件中的item元素的属性identifier的值用"," 分割
	 */
	private void toCourseRoot(Node tempOrg, Node resourcesNode, Document courseDocument, String importItemIds) {
		String[] itemIds = importItemIds.split(",");
		Node courseOrganization = getOrgNode(courseDocument);
		tempOrg.removeChild(DOMTreeUtility.getNode(tempOrg, "title"));

		List<Node> modifyModuleList = new ArrayList<Node>();
		Vector items = DOMTreeUtility.getNodes(tempOrg, "item");
		for (int i = 0; i < itemIds.length; i++) {
			if (itemIds[i] != null && !"".equals(itemIds[i])) {
				modifyModuleList.add(this.getItemNode(items, itemIds[i]));
			}
		}

		for (int i = 0; i < modifyModuleList.size(); i++) {// 将课件目录的模块节点加入到课程目录中的organizations节点下
			Node moduleNode = modifyModuleList.get(i);
			Node courseModule = courseDocument.importNode(moduleNode, true);// 不同文档对象下的节点不能相互添加,若添加不同文档下的节点必须先导入import
			courseOrganization.appendChild(courseModule);
		}

		// 将处理后的课件中的resources元素添加到课程目录中的课件xml文档中
		this.toCourseResources(resourcesNode, courseDocument);
	}

	/**
	 * 将resourcesNode中的元素添加到courseDocument中
	 * 
	 * @param resourcesNode
	 * @param courseDocument
	 */
	private void toCourseResources(Node resourcesNode, Document courseDocument) {
		Node courseManifest = courseDocument.getDocumentElement();
		Node courseRecourses = DOMTreeUtility.getNode(courseManifest, "resources");
		NodeList modifyResourceList = resourcesNode.getChildNodes();
		for (int i = 0; i < modifyResourceList.getLength(); i++) {// 将课件目录的资源节点加入到课程目录中的resources节点下
			Node resourceNode = modifyResourceList.item(i);
			Node courseResourceNode = courseDocument.importNode(resourceNode, true);
			courseRecourses.appendChild(courseResourceNode);
		}
	}

	/**
	 * 获得根据item元素的id获得文档对象的对应的item节点
	 * 
	 * @param courseDocument
	 * @param itemId
	 * @return
	 */
	private Node getItemNode(Vector items, String itemId) {
		Node result = null;
		for (int i = 0; i < items.size(); i++) {
			Node item = (Node) items.elementAt(i);
			String identifier = DOMTreeUtility.getAttributeValue(item, "identifier");
			// System.out.println("--------identifier-------"+identifier);
			if (itemId.equalsIgnoreCase(identifier)) {
				// System.out.println("--------找到要查找的item----identifier---"+itemId);
				result = item;
				break;
			} else {
				Vector childVector = DOMTreeUtility.getNodes(item, "item");
				result = getItemNode(childVector, itemId);
				if (result != null) {
					break;
				}
			}
		}
		return result;
	}

	/**
	 * 将对应的document对象写入到对应的课件描述文件中
	 * 
	 * @param imsmanifestFile
	 * @param courseDocument
	 * @throws Exception
	 */
	public void writeManifest(File imsmanifestFile, Document courseDocument) throws Exception {
		logger.debug("----进入writeManifest方法---");
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("UTF-8");
		// 将更改的document写入到课件存储目录中的imsmanifest.xml文件
		org.dom4j.io.XMLWriter xmlWriter = new org.dom4j.io.XMLWriter(new FileOutputStream(imsmanifestFile), format);
		org.dom4j.io.DOMReader domReader = new org.dom4j.io.DOMReader();
		org.dom4j.Document document = domReader.read(courseDocument);
		xmlWriter.write(document);
		xmlWriter.close();
		logger.debug("写入文件：" + imsmanifestFile.getAbsolutePath() + "成功, writeManifest方法结束");
	}

	/**
	 * 将节点添加到课程根节点中
	 * 
	 * @param itemId
	 *            添加的模块在课件描述文件中的item元素中的identifier属性值
	 * @param courseId
	 *            课程Id
	 */
	public void addModuleToCourse(String itemId, String courseId, MeleteModuleModel module) throws Exception {
		String coursePath = CourseUtil.getCoursePathByCourseId(courseId);// 获得coursePath
		Document document = this.getXmlDocument(coursePath);// 获得课程目录中的课件描述文件的文档对象
		Node orgNode = this.getOrgNode(document);
		Element moduleElement = createItem(itemId, document, module.getTitle(), orgNode);
		orgNode.appendChild(moduleElement);// 将item元素添加到organization元素中
		this.writeManifest(new File(coursePath + "imsmanifest.xml"), document);
	}

	/**
	 * 创建课件描述文件中的 item元素
	 * 
	 * @param itemId
	 * @param document
	 * @param title
	 * @param pNode
	 * @return 新创建的item元素
	 */
	private Element createItem(String itemId, Document document, String title, Node pNode) {
		// 插入子节点时，如果子节点没有设置namespace，会自动插入一个空的xmlns=""
		// 将子节点的namespace设置成和父节点一样，xmlns这一项就不会在xml文件中显示
		Element itemElement = document.createElementNS(pNode.getNamespaceURI(), "item");// 新建item元素
		Element titleElement = document.createElementNS(pNode.getNamespaceURI(), "title");
		itemElement.setAttribute("identifier", itemId);
		itemElement.setAttribute("isvisible", "true");
		titleElement.setTextContent(title);
		itemElement.appendChild(titleElement);
		return itemElement;
	}

	/**
	 * 获得文档对象中的organization元素
	 * 
	 * @param document
	 * @return
	 */
	private Node getOrgNode(Document document) {
		Node manifestNode = document.getDocumentElement();
		Vector orgVector = ManifestHandler.getOrganizationNodes(manifestNode, false);
		Node orgNode = (Node) orgVector.elementAt(0);// 获得organization元素
		return orgNode;
	}

	/**
	 * 将module添加到课件描述文件中的父module中
	 * 
	 * @param itemId
	 * @param pModuleId
	 */
	public void addModuleToPModule(String itemId, MeleteModuleModel pModule, MeleteModuleModel module) throws Exception {
		String coursePath = CourseUtil.getCoursePathByCourseId(pModule.getCourseId());// 获得coursePath
		Document document = this.getXmlDocument(coursePath);// 获得课程目录中的课件描述文件的文档对象
		Node orgNode = this.getOrgNode(document);
		Vector items = DOMTreeUtility.getNodes(orgNode, "item");// 获得organization元素下的item元素集合
		Node pItemNode = this.getItemNode(items, pModule.getModuleItemID());
		Element moduleItem = this.createItem(itemId, document, module.getTitle(), pItemNode);// 创建新item元素
		pItemNode.appendChild(moduleItem);// 将新创建的item添加的父item元素中
		this.writeManifest(new File(coursePath + "imsmanifest.xml"), document);
	}

	/**
	 * 获得课件描述文件中的resources节点
	 * 
	 * @param document
	 * @return
	 */
	public Node getResourcesNode(Document document) {
		Node manifestNode = document.getDocumentElement();
		Node resourcesNode = DOMTreeUtility.getNode(manifestNode, "resources");
		return resourcesNode;
	}

	/**
	 * 预处理课件资源库中的课件描述文件文档对象就是将原有的item和rescource元素中的identifier属性值替换为新生成的值
	 * 
	 * @param cwareDocument
	 */
	public void preProcess(Document cwareDocument) throws Exception {
		Node orgNode = this.getOrgNode(cwareDocument);
		Vector itemElements = DOMTreeUtility.getNodes(orgNode, "item");
		Node resourcesNode = this.getResourcesNode(cwareDocument);
		Vector resElements = DOMTreeUtility.getNodes(resourcesNode, "resource");
		this.recursiveProcess(itemElements, resElements);// 递归处理item节点集合与rescource节点集合
	}

	/**
	 * 递归处理 itemElements 与 resElements中的各元素的identifier属性
	 * 
	 * @param itemElements
	 * @param resElements
	 */
	private void recursiveProcess(Vector itemElements, Vector resElements) {
		for (int i = 0; i < itemElements.size(); i++) {
			Element itemElement = (Element) itemElements.elementAt(i);
			String idref = DOMTreeUtility.getAttributeValue(itemElement, "identifierref");
			itemElement.setAttribute("identifier", CourseUtil.getItemId());// item的identifier值更改为新生成的值
			if (idref == null || "".equals(idref)) {// 模块节点
				Vector childNodes = DOMTreeUtility.getNodes(itemElement, "item");
				recursiveProcess(childNodes, resElements);
			} else { // 页节点
				String resId = CourseUtil.getResId();
				itemElement.setAttribute("identifierref", resId);
				Element resElement = (Element) this.getResElementByIdref(resElements, idref);
				logger.debug("----idref----" + idref + "-----对应的资源节点---" + resElement);
				resElement.setAttribute("identifier", resId);// 将resource元素的identifier值更改为新生成的值
			}
		}
	}
	
	/**
	 * 检查课件包结构是否符合要求
	 * 
	 * @param cwareDocument
	 */
	public boolean checkDocument(Document cwareDocument) throws Exception {
		Node orgNode = this.getOrgNode(cwareDocument);
		Vector itemElements = DOMTreeUtility.getNodes(orgNode, "item");
		return this.recursiveCheckDocument(itemElements);// 递归  
	}

	/**
	 * 递归检查课件包结构是否符合要求
	 * 
	 * @param itemElements
	 */
	private boolean recursiveCheckDocument(Vector itemElements) {
		boolean module = false;
		boolean section = false;
		for (int i = 0; i < itemElements.size(); i++) {
			Element itemElement = (Element) itemElements.elementAt(i);
			String idref = DOMTreeUtility.getAttributeValue(itemElement, "identifierref");
			if (idref == null || "".equals(idref)) {// 模块节点
				Vector childNodes = DOMTreeUtility.getNodes(itemElement, "item");
				if(!recursiveCheckDocument(childNodes)){
					return false;
				}
				
				module = true;
			} else { // 页节点
				section = true;
			}
		}
		
		if(module != section){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 通过item元素中的identifierref属性值来定位resElements中的resource元素并返回
	 * 
	 * @param resElements
	 * @param idref
	 * @return 已定位resource元素
	 */
	private Node getResElementByIdref(Vector resElements, String idref) {
		Node result = null;
		for (int i = 0; i < resElements.size(); i++) {
			Node resElement = (Node) resElements.elementAt(i);
			String resId = DOMTreeUtility.getAttributeValue(resElement, "identifier");
			if (resId.equalsIgnoreCase(idref)) {
				result = resElement;
				break;
			}
		}
		return result;
	}

	/**
	 * 调整拖拽排序后的课件描述文件
	 * 
	 * @param origNode
	 * @param destNode
	 * @param point
	 * @throws Exception
	 */
	private void adjustXML(Object origNodeObj, Object destNodeObj, String point) throws Exception {
		logger.debug("-----进入adjustXML方法-------");
		if ("append".equals(point)) {// 若为追加节点
			if (destNodeObj == null) {// 若追加的节点为课程节点
				this.appendItemToCourse(origNodeObj);
			} else {// 若追加节点为模块节点
				this.appendItemToDestItem(origNodeObj, destNodeObj);
			}
		} else {// 若为节点间相互拖拽
			if (MeleteModuleModel.class == origNodeObj.getClass()) {// 若模块间互相拖拽
				adjustModuleItem(origNodeObj, destNodeObj, point);
			} else if (MeleteSectionModel.class == origNodeObj.getClass()) {// 若是页之间相互拖拽
				adjustSection(origNodeObj, destNodeObj, point);
			}
		}

	}

	/**
	 * 向课程节点追加模块节点
	 * 
	 * @param origNodeObj
	 */
	private void appendItemToCourse(Object origNodeObj) throws Exception {
		logger.debug("--------进入appendItemToCourse方法------");
		MeleteModuleModel origNode = (MeleteModuleModel) origNodeObj;
		String coursePath = CourseUtil.getCoursePathByCourseId(origNode.getCourseId());
		Document document = this.getXmlDocument(coursePath);
		Node orgNode = this.getOrgNode(document);// 获得文档对象中的organization元素
		Vector items = DOMTreeUtility.getNodes(orgNode, "item");
		String origItemId = origNode.getModuleItemID();// 通过获得module在描述文件相关连的ModuleItemID
		Node origModuleItem = this.getItemNode(items, origItemId);// 获得拖拽的item元素
		Node parentNode = origModuleItem.getParentNode();// 获得origModuleItem拖拽前的父节点
		parentNode.removeChild(origModuleItem);// 从原来的父节点中移除拖拽节点
		orgNode.appendChild(origModuleItem);// 将拖拽节点追加到organization元素
		this.writeManifest(new File(coursePath + "imsmanifest.xml"), document);// 将修改的文档对象写回
	}

	/**
	 * 向目标模块追加模块或页节点
	 * 
	 * @param origNodeObj
	 * @param destNodeObj
	 */
	private void appendItemToDestItem(Object origNodeObj, Object destNodeObj) throws Exception {
		logger.debug("-----进入appendItemToDestItem方法-------");
		MeleteModuleModel destNode = (MeleteModuleModel) destNodeObj;
		String coursePath = CourseUtil.getCoursePathByCourseId(destNode.getCourseId());
		Document document = this.getXmlDocument(coursePath);
		Node orgNode = this.getOrgNode(document);// 获得文档对象中的organization元素
		Vector items = DOMTreeUtility.getNodes(orgNode, "item");
		Node destItem = this.getItemNode(items, destNode.getModuleItemID());// 获得目标模块item节点
		Node origItem = null;
		if (MeleteModuleModel.class == origNodeObj.getClass()) {// 若拖拽的节点为模块节点
			MeleteModuleModel origNode = (MeleteModuleModel) origNodeObj;
			origItem = this.getItemNode(items, origNode.getModuleItemID());// 获得拖拽节点的item
		} else if (MeleteSectionModel.class == origNodeObj.getClass()) {// 若拖拽节点为页节点
			MeleteSectionModel origNode = (MeleteSectionModel) origNodeObj;
			origItem = this.getItemNode(items, origNode.getSectionItemId());// 获得拖拽节点的item
		} else {
			throw new Exception("origNodeObj类型错误, 传入的origNodeObj类型为" + origNodeObj.getClass());
		}
		Node pOrigItemNode = origItem.getParentNode();// 获得拖拽前的父节点
		pOrigItemNode.removeChild(origItem);// 从父节点移除拖拽节点
		destItem.appendChild(origItem);// 将拖拽节点追加到目标节点
		this.writeManifest(new File(coursePath + "imsmanifest.xml"), document);// 将修改后的文档对象写回到课程目录中的课件描述文件中
	}

	/**
	 * 页之间拖拽后调整课件描述文件
	 * 
	 * @param origNodeObj
	 * @param destNodeObj
	 * @param point
	 * @throws Exception
	 */
	private void adjustSection(Object origNodeObj, Object destNodeObj, String point) throws Exception {
		logger.debug("------进入adjustModuleItem方法------");
		MeleteSectionModel origNode = (MeleteSectionModel) origNodeObj;
		MeleteSectionModel destNode = (MeleteSectionModel) destNodeObj;
		String coursePath = CourseUtil.getCoursePathByCourseId(origNode.getCourseId());
		Document cDocument = this.getXmlDocument(coursePath);
		Node orgNode = this.getOrgNode(cDocument);
		Vector items = DOMTreeUtility.getNodes(orgNode, "item");
		Node origSectionItem = this.getItemNode(items, origNode.getSectionItemId());
		Node destSectionItem = this.getItemNode(items, destNode.getSectionItemId());
		Node origPNode = origSectionItem.getParentNode();
		Node destPNode = destSectionItem.getParentNode();
		Vector destNodeVector = DOMTreeUtility.getNodes(destPNode, "item");// 获得与destNode同级别的item元素集合
		int destSectionItemIndex = destNodeVector.lastIndexOf(destSectionItem);// 返回destSectionItem在destNodeVector的位置
		origPNode.removeChild(origSectionItem);// 移除origPNode中的origModuleItem
		if (point.equals("above")) {
			destPNode.insertBefore(origSectionItem, destSectionItem);
		} else if (point.equals("below")) {
			if (destSectionItemIndex + 1 < destNodeVector.size()) {// 若destSectionItem下还有节点
				Node nextDestNode = (Node) destNodeVector.elementAt(destSectionItemIndex + 1);// destSectionItem的下一个节点
				destPNode.insertBefore(origSectionItem, nextDestNode);
			} else {// 若destSectionItem下无节点将origSectionItem插入到destPNode的尾部
				destPNode.insertBefore(origSectionItem, null);
			}
		}
		this.writeManifest(new File(coursePath + "imsmanifest.xml"), cDocument);// 将调整后的文档对象写入到课件描述文件中
	}

	/**
	 * 模块之间拖拽后调整课件描述文件
	 * 
	 * @param origNodeObj
	 * @param destNodeObj
	 * @param point
	 * @throws Exception
	 */
	private void adjustModuleItem(Object origNodeObj, Object destNodeObj, String point) throws Exception {
		logger.debug("--------进入adjustModuleItem方法------");
		MeleteModuleModel origNode = (MeleteModuleModel) origNodeObj;
		MeleteModuleModel destNode = (MeleteModuleModel) destNodeObj;
		String coursePath = CourseUtil.getCoursePathByCourseId(origNode.getCourseId());
		Document cDocument = this.getXmlDocument(coursePath);
		Node orgNode = this.getOrgNode(cDocument);
		Vector items = DOMTreeUtility.getNodes(orgNode, "item");
		Node origModuleItem = this.getItemNode(items, origNode.getModuleItemID());
		Node destModuleItem = this.getItemNode(items, destNode.getModuleItemID());
		Node origPNode = origModuleItem.getParentNode();
		Node destPNode = destModuleItem.getParentNode();
		Vector destNodeVector = DOMTreeUtility.getNodes(destPNode, "item");// 获得与destNode同级别的item元素集合
		int destModuleItemIndex = destNodeVector.lastIndexOf(destModuleItem);// 返回destModuleItem在destNodeVector的位置
		origPNode.removeChild(origModuleItem);// 移除origPNode中的origModuleItem
		if (point.equals("above")) {
			destPNode.insertBefore(origModuleItem, destModuleItem);// 将origModuleItem插入destModuleItem的前面
		} else if (point.equals("below")) {
			if (destModuleItemIndex + 1 < destNodeVector.size()) {// 若destModuleItem下还有节点
				Node nextDestNode = (Node) destNodeVector.elementAt(destModuleItemIndex + 1);// destModuleItem后面的节点
			} else {// 若destModuleItem下无节点
				destPNode.insertBefore(origModuleItem, null);// 将origModuleItem插入到destPNode的尾部
			}
		}
		this.writeManifest(new File(coursePath + "imsmanifest.xml"), cDocument);// 将调整后的文档对象写入到课件描述文件中
	}

	/**
	 * 根据courseId, itemId 删除对应的课程的课件描述文件中的item以及对应的resource元素
	 * 
	 * @param moduleId
	 * @throws Exception
	 */
	public void delItem(String courseId, String itemId) throws Exception {
		String coursePath = CourseUtil.getCoursePathByCourseId(courseId);
		Document document = this.getXmlDocument(coursePath);
		if (document == null) {
			return;
		}
		Node orgNode = this.getOrgNode(document);// 获得课件描述文件文档对象的organiazation元素节点

		Vector items = DOMTreeUtility.getNodes(orgNode, "item");
		Node delItem = this.getItemNode(items, itemId);// 获得要删除的item节点
		if (delItem != null) {
			Node parentNode = delItem.getParentNode();// 获得delItem父节点
			List<String> resourceIdList = this.getDelRecourseList(delItem);
			Node resourcesNode = this.getResourcesNode(document);
			Vector resVector = DOMTreeUtility.getNodes(resourcesNode, "resource");// 获得resource节点集合
			this.removeResource(resVector, resourceIdList, true);// 移除页节点所引用的资源节点即identifier属性值包含在resourceIdList中的resource元素
			parentNode.removeChild(delItem);// 移除delItem节点本身
			this.writeManifest(new File(coursePath + "imsmanifest.xml"), document);// 将更改的文档对象写回
		}
	}

	/**
	 * 获得要删除的资源id集合
	 * 
	 * @param delItem
	 */
	private List<String> getDelRecourseList(Node delItem) {
		String identifierref = DOMTreeUtility.getAttributeValue(delItem, "identifierref");
		List<String> resourceIdList = new ArrayList<String>();
		if (identifierref != null && !"".equals(identifierref)) {// delItem本身是页节点
			resourceIdList.add(identifierref);
		} else {// delItem本身是模块节点
			// 获得所有子节点中的页节点的identifierref属性值放入resourceIdList中
			Vector<Node> sectionItemsVector = this.getSectionItemsByPNode(delItem);
			for (int i = 0; i < sectionItemsVector.size(); i++) {
				String idref = DOMTreeUtility.getAttributeValue(sectionItemsVector.elementAt(i), "identifierref");
				resourceIdList.add(idref);
			}
		}
		return resourceIdList;
	}

	/**
	 * 通过父模块节点的Item找到所有的子节点的页节点item集合
	 * 
	 * @param pItem
	 *            父模块节点的item
	 * @return
	 */
	private Vector<Node> getSectionItemsByPNode(Node pItem) {
		Vector<Node> sectionItemsVector = new Vector<Node>();
		this.findSectionItems(pItem, sectionItemsVector);
		return sectionItemsVector;
	}

	/**
	 * 将父模块节点下的section的节点放入sectionItemsVector中
	 * 
	 * @param pItem
	 * @param sectionItemsVector
	 */
	private void findSectionItems(Node pItem, Vector<Node> sectionItemsVector) {
		Vector childrenNodes = DOMTreeUtility.getNodes(pItem, "item");
		for (int i = 0; i < childrenNodes.size(); i++) {
			Node childNode = (Node) childrenNodes.elementAt(i);
			String identifierref = DOMTreeUtility.getAttributeValue(childNode, "identifierref");
			if (identifierref != null && !"".equals(identifierref)) {// 是页节点
				sectionItemsVector.add(childNode);
			} else {
				findSectionItems(childNode, sectionItemsVector);
			}
		}
	}

	/**
	 * 打包课程目录下课件资源
	 * 
	 * @param courseId
	 *            课程ID
	 * @param exportFileName
	 *            导出课件包文件名
	 */
	public void packCourseWare(String courseId, String exportFileName) throws Exception {
		String zipFileName = null;
		String coursePath = CourseUtil.getCoursePathByCourseId(courseId);
		File exportDir = new File(Constants.COURSEWARE_EXPORT_PATH + courseId);
		try {
			Document courseDoc = this.getXmlDocument(coursePath);
			Node resources = this.getResourcesNode(courseDoc);
			Vector resVector = DOMTreeUtility.getNodes(resources, "resource");

			for (Iterator iter = resVector.iterator(); iter.hasNext();) {
				Node res = (Node) iter.next();
				String htmFileName = DOMTreeUtility.getAttributeValue(res, "href");
				Vector fileVector = DOMTreeUtility.getNodes(res, "file");
				// 将fileVector中的文件复制到课件导出路径
				copyFilesToExportDir(courseId, coursePath, htmFileName, fileVector);
				this.modifyExportHtm(coursePath, courseId, htmFileName, res, courseDoc);
			}
			this.writeManifest(new File(exportDir, "imsmanifest.xml"), courseDoc);// 将更改过的课件描述文件的文档对象写入到导出目录
			ArchiveDetector detector = new DefaultArchiveDetector(ArchiveDetector.ALL, new Object[] { "zip",
					new CheckedZipDriver("UTF-8") });
			de.schlichtherle.io.File archive = new de.schlichtherle.io.File(Constants.COURSEWARE_EXPORT_PATH
					+ exportFileName, detector);
			new de.schlichtherle.io.File(exportDir, detector).copyAllTo(archive);// 将导出目录中的文件放入压缩包文件
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		} finally {
			try {
				FileUtils.deleteDirectory(exportDir);// 将导出目录删除
				de.schlichtherle.io.File.umount();// J2EE环境下防止在文件操作冲突或数据丢失
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error(e.getMessage(), e);
				throw e;
			}
		}
	}

	/**
	 * 将fileVector中file元素中的href属性值的所引用的文件复制到导出目录中
	 * 
	 * @param courseId
	 * @param coursePath
	 * @param htmFileName
	 * @param fileVector
	 * @throws IOException
	 */
	private void copyFilesToExportDir(String courseId, String coursePath, String htmFileName, Vector fileVector)
			throws IOException {
		for (Iterator fileIter = fileVector.iterator(); fileIter.hasNext();) {
			Node fileNode = (Node) fileIter.next();
			String fileName = DOMTreeUtility.getAttributeValue(fileNode, "href");
			decodeHandler decodeFile = new decodeHandler(fileName, "UTF-8");
			decodeFile.decodeName();

			if (!fileName.equalsIgnoreCase(htmFileName)) {// 若文件不为已处理过的htm文件
				File srcFile = new File(coursePath + decodeFile.getDecodedFileName());
				if (!srcFile.exists()) {
					logger.info("课件导出时将源文件复制到导出目录时出错, 原因是源文件" + srcFile.getAbsolutePath() + "不存在");
					continue;
				}
				File destFile = new File(Constants.COURSEWARE_EXPORT_PATH + courseId + "/"
						+ decodeFile.getDecodedFileName());
				FileUtils.copyFile(srcFile, destFile);
				logger.debug("将文件：" + srcFile.getAbsolutePath() + " 复制到：" + destFile.getAbsolutePath());
			}
		}
	}

	/**
	 * 导出时处理resource元素的href属性中的htm文件中导入时添加的js代码以及操作课件描述文件中的resource元素
	 * 
	 * @param coursePath
	 *            课程中课件存放目录
	 * @param courseId
	 *            课程ID
	 * @param htmFileName
	 *            htm文件名
	 * @param resource
	 *            课件描述文件中的资源节点
	 * @param courseDoc
	 *            要修改的课件描述文件的文档对象
	 * @throws Exception
	 */
	private void modifyExportHtm(String coursePath, String courseId, String htmFileName, Node resource,
			Document courseDoc) throws Exception {
		decodeHandler decodeFile = new decodeHandler(htmFileName, "UTF-8");
		decodeFile.decodeName();
		String fileHref = coursePath + decodeFile.getDecodedFileName();// 文件资源的绝对路径
		File source = new File(fileHref);// 获取源文件
		NodeList childrenList = resource.getChildNodes();

		// 移除resource节点的所有file子节点
		for (int i = 0; i < childrenList.getLength(); i++) {
			resource.removeChild(childrenList.item(i));
		}
		// 替换html文件中的相关引用资源的路径
		List<String> fileList = this.replaceExportHtmlPath(source, courseId);
		// 重新生成课件描述文件中的resource元素的file子元素节点并添加到resource元素中
		for (String file : fileList) {
			Element fileElement = courseDoc.createElementNS(resource.getNamespaceURI(), "file");// 新建文件子节点
			fileElement.setAttribute("href", file);
			resource.appendChild(fileElement);
		}
	}

	/**
	 * 导出课件包时修改课件包中html文件中的引用资源路径
	 * 
	 * @param htmlFile
	 * @param courseId
	 * @return
	 */
	private List<String> replaceExportHtmlPath(File htmlFile, String courseId) throws Exception {
		org.jsoup.nodes.Document htmlDocument = Jsoup.parse(htmlFile, "UTF-8",
				ServerConfigurationService.getServerUrl());
		List<String> fileList = new ArrayList<String>();
		String coursePath = Constants.COURSE_PATH_URI + courseId + "/";
		String ckfinderPath = Constants.COURSEWARE_RESOURCE_URI;
		String courseWareExportPath = Constants.COURSEWARE_EXPORT_PATH + courseId + "/";
		// 替换img标签的路径
		replaceExportImgPath(htmlDocument, fileList, coursePath, ckfinderPath, courseWareExportPath, courseId);
		// 替换link标签路径
		replaceExportLinkPath(htmlDocument, fileList, coursePath, ckfinderPath, courseWareExportPath, courseId);
		// 替换param标签路径
		replaceExportParamPath(htmlDocument, fileList, coursePath, ckfinderPath, courseWareExportPath, courseId);
		// 替换object标签路径
		replaceExportObjectPath(htmlDocument, fileList, coursePath, ckfinderPath, courseWareExportPath, courseId);
		// 替换script标签路径
		replaceExportScriptPath(htmlDocument, fileList, coursePath, ckfinderPath, courseWareExportPath, courseId);
		// 替换embed标签路径
		replaceExportEmbedPath(htmlDocument, fileList, coursePath, ckfinderPath, courseWareExportPath, courseId);
		// 修改后的html文档对象的文本
		String modifiedHtml = htmlDocument.outerHtml();

		// 导入课件增加的js代码导出时去掉
		/*
		 * String fileContent = "<script>\\s*" +
		 * "var\\s*domainUrl\\s*=\\s*window\\.location\\.hash\\.toString\\(\\)\\.substring\\(1\\);\\s*"
		 * +
		 * "var\\s*script\\s*=\\s*document\\.createElement\\(\"script\"\\);\\s*"
		 * ; fileContent +=
		 * "script\\.src\\s*=\\s*domainUrl\\s*\\+\\s*\"/resource/scripts/APIWrapper\\.js\";\\s*"
		 * ; fileContent += "script\\.charset\\s*=\\s*\"utf-8\";\\s*";
		 * fileContent +=
		 * "document\\.getElementsByTagName\\(\"head\"\\)\\[0\\]\\.appendChild\\(script\\);\\s*"
		 * ; fileContent += "</script>"; modifiedHtml =
		 * modifiedHtml.replaceAll(fileContent, "");
		 */

		File desc = new File(Constants.COURSEWARE_EXPORT_PATH + courseId + "/" + htmlFile.getName());// 获取目标文件
		this.createFile(desc); // 创建目标文件
		// logger.debug("替换完路径和去掉导入时增加的js代码后的html文件内容：\n" + modifiedHtml);
		FileUtils.writeStringToFile(desc, modifiedHtml, "UTF-8");// 将替换路径后的html文本写入导出目录中的文件
		logger.debug("将html内容写入到导出目标文件：" + desc.getAbsolutePath());
		fileList.add(0, htmlFile.getName());
		return fileList;
	}

	/**
	 * 替换导出课件时embed标签的路径
	 * 
	 * @param htmlDocument
	 * @param fileList
	 * @param coursePath
	 * @param ckfinderPath
	 * @param courseWareExportPath
	 * @throws Exception
	 * @throws IOException
	 */
	private void replaceExportEmbedPath(org.jsoup.nodes.Document htmlDocument, List<String> fileList,
			String coursePath, String ckfinderPath, String courseWareExportPath, String courseId) throws Exception,
			IOException {
		logger.debug("---进入方法replaceExportEmbedPath---");
		Elements embedElements = htmlDocument.getElementsByTag("embed");
		if (!embedElements.isEmpty()) {
			for (org.jsoup.nodes.Element embed : embedElements) {
				String src = embed.attr("src");
				logger.debug("---要替换的embed标签的src的URL---" + src);
				if (src != null && !"".equals(src) && src.startsWith(Constants.SECTION_URL)) {
					String replacePath = "";
					if (src.startsWith(coursePath)) {// 若路径为课程目录路径
						replacePath = coursePath;
					} else if (src.startsWith(ckfinderPath)) {// 若路径为ckfinder配置目录路径
						replacePath = this.getCkResPath(ckfinderPath, src, courseId);
						// 将ckfinder路径下的资源复制到导出目录下
						this.fileCopyCheck(src, courseWareExportPath);
						logger.debug("--------将课件页中引用的ckfinder目录下的文件" + src + " copy到导出目录" + courseWareExportPath);
					}
					logger.debug("替换前的文件路径：" + src);
					src = src.replace(replacePath, "");
					logger.debug("替换后的文件路径：" + src);
					embed.attr("src", src);// 替换script标签中的src为应用根路径开始的绝对路径
					fileList.add(src);
				}
			}
		}
		logger.debug("---结束方法replaceExportEmbedPath---");
	}

	/**
	 * 替换导出时的script标签的路径
	 * 
	 * @param htmlDocument
	 * @param fileList
	 * @param coursePath
	 * @param ckfinderPath
	 * @param courseWareExportPath
	 * @throws Exception
	 * @throws IOException
	 */
	private void replaceExportScriptPath(org.jsoup.nodes.Document htmlDocument, List<String> fileList,
			String coursePath, String ckfinderPath, String courseWareExportPath, String courseId) throws Exception,
			IOException {
		logger.debug("---进入方法replaceExportScriptPath---");
		Elements scriptElements = htmlDocument.getElementsByTag("script");
		if (!scriptElements.isEmpty()) {
			for (org.jsoup.nodes.Element script : scriptElements) {
				String src = script.attr("src");
				logger.debug("---要替换的script标签的src属性URL---" + src);
				if (src != null && !"".equals(src) && src.startsWith(Constants.SECTION_URL)) {
					// 除配置的URL以外的外部链接不做替换
					String replacePath = "";
					if (src.startsWith(coursePath)) {// 若路径为课程目录路径
						replacePath = coursePath;
					} else if (src.startsWith(ckfinderPath)) {// 若路径为ckfinder配置目录路径
						replacePath = this.getCkResPath(ckfinderPath, src, courseId);
						// 将ckfinder路径下的资源复制到导出目录下
						this.fileCopyCheck(src, courseWareExportPath);
						logger.debug("--------将课件页中引用的ckfinder目录下的文件" + src + " copy到导出目录" + courseWareExportPath);
					}
					logger.debug("替换前的文件路径：" + src);
					src = src.replace(replacePath, "");
					logger.debug("替换后的文件路径：" + src);
					script.attr("src", src);// 替换script标签中的src为应用根路径开始的绝对路径
					fileList.add(src);
				}
			}
		}
		logger.debug("---结束方法replaceExportScriptPath---");
	}

	/**
	 * 替换导出课件时的object标签的路径
	 * 
	 * @param htmlDocument
	 * @param fileList
	 * @param coursePath
	 * @param ckfinderPath
	 * @param courseWareExportPath
	 * @throws Exception
	 * @throws IOException
	 */
	private void replaceExportObjectPath(org.jsoup.nodes.Document htmlDocument, List<String> fileList,
			String coursePath, String ckfinderPath, String courseWareExportPath, String courseId) throws Exception,
			IOException {
		logger.debug("---进入方法replaceExportObjectPath---");
		Elements objectElements = htmlDocument.getElementsByTag("object");
		if (!objectElements.isEmpty()) {
			for (org.jsoup.nodes.Element object : objectElements) {
				String type = object.attr("type");
				if (type != null && type.equalsIgnoreCase("application/x-shockwave-flash")) {
					String data = object.attr("data");
					logger.debug("---要替换的object标签的data属性的URL---" + data);
					if (data != null && !"".equals(data) && data.startsWith(Constants.SECTION_URL)) {
						String replacePath = "";
						if (data.startsWith(coursePath)) {// 若路径为课程目录路径
							replacePath = coursePath;
						} else if (data.startsWith(ckfinderPath)) {// 若路径为ckfinder配置目录路径
							replacePath = this.getCkResPath(ckfinderPath, data, courseId);
							// 将ckfinder路径下的资源复制到导出目录下
							this.fileCopyCheck(data, courseWareExportPath);
							logger.debug("--------将课件页中引用的ckfinder目录下的文件" + data + " copy到导出目录" + courseWareExportPath);
						}
						logger.debug("替换前的文件路径：" + data);
						data = data.replace(replacePath, "");
						logger.debug("替换后的文件路径：" + data);
						object.attr("data", data);// 替换object标签中的data应用根路径开始的绝对路径为相对路径
						fileList.add(data);
					}
				}
			}
		}
		logger.debug("---结束方法replaceExportObjectPath---");
	}

	/**
	 * 替换课件导出时param标签中的路径
	 * 
	 * @param htmlDocument
	 * @param fileList
	 * @param coursePath
	 * @param ckfinderPath
	 * @param courseWareExportPath
	 * @throws Exception
	 * @throws IOException
	 */
	private void replaceExportParamPath(org.jsoup.nodes.Document htmlDocument, List<String> fileList,
			String coursePath, String ckfinderPath, String courseWareExportPath, String courseId) throws Exception,
			IOException {
		logger.debug("---进入replaceExportParamPath方法---");
		Elements paramElements = htmlDocument.getElementsByTag("param");
		if (!paramElements.isEmpty()) {
			for (org.jsoup.nodes.Element param : paramElements) {
				String name = param.attr("name");
				if (name != null && (name.equalsIgnoreCase("movie") || name.equalsIgnoreCase("expressInstall"))) {
					String value = param.attr("value");
					logger.debug("---要替换的---param标签的value的URL---" + value);
					if (value != null && !"".equals(value) && value.startsWith(Constants.SECTION_URL)) {
						String replacePath = "";
						if (value.startsWith(coursePath)) {// 若路径为课程目录路径
							replacePath = coursePath;
						} else if (value.startsWith(ckfinderPath)) {// 若路径为ckfinder配置目录路径
							replacePath = this.getCkResPath(ckfinderPath, value, courseId);
							// 将ckfinder路径下的资源复制到导出目录下
							this.fileCopyCheck(value, courseWareExportPath);
							logger.debug("--------将课件页中引用的ckfinder目录下的文件" + value + " copy到导出目录" + courseWareExportPath);
						}
						logger.debug("替换前的文件路径：" + value);
						value = value.replace(replacePath, "");
						logger.debug("替换后的文件路径：" + value);

						param.attr("value", value);// 替换param标签中的value应用根路径开始的绝对路径为相对路径
						fileList.add(value);
					}
				}
			}
		}
		logger.debug("---结束replaceExportParamPath方法---");
	}

	/**
	 * 替换导出课件时link标签的路径
	 * 
	 * @param htmlDocument
	 * @param fileList
	 * @param coursePath
	 * @param ckfinderPath
	 * @param courseWareExportPath
	 * @throws Exception
	 * @throws IOException
	 */
	private void replaceExportLinkPath(org.jsoup.nodes.Document htmlDocument, List<String> fileList, String coursePath,
			String ckfinderPath, String courseWareExportPath, String courseId) throws Exception, IOException {
		logger.debug("---进入replaceExportLinkPath方法---");
		Elements linkElements = htmlDocument.getElementsByTag("link");
		if (!linkElements.isEmpty()) {
			for (org.jsoup.nodes.Element link : linkElements) {
				String href = link.attr("href");
				logger.debug("---要替换的link标签的href的URL---" + href);
				if (href != null && !"".equals(href) && href.startsWith(Constants.SECTION_URL)) {
					String replacePath = "";
					if (href.startsWith(coursePath)) {// 若路径为课程目录路径
						replacePath = coursePath;
					} else if (href.startsWith(ckfinderPath)) {// 若路径为ckfinder配置目录路径
						replacePath = this.getCkResPath(ckfinderPath, href, courseId);
						// 将ckfinder路径下的资源复制到导出目录下
						this.fileCopyCheck(href, courseWareExportPath);
						logger.debug("--------将ckfinder目录下的文件" + href + " copy到导出目录" + courseWareExportPath);
					}
					logger.debug("替换前的文件路径：" + href);
					href = href.replace(replacePath, "");
					logger.debug("替换后的文件路径：" + href);

					link.attr("href", href);// 替换link标签中的href应用根路径开始的绝对路径为相对路径
					fileList.add(href);
				}

			}
		}
		logger.debug("---结束replaceExportLinkPath方法---");
	}

	/**
	 * 替换课件导出时的img标签的路径
	 * 
	 * @param htmlDocument
	 * @param fileList
	 * @param coursePath
	 * @param ckfinderPath
	 * @param courseWareExportPath
	 * @throws Exception
	 * @throws IOException
	 */
	private void replaceExportImgPath(org.jsoup.nodes.Document htmlDocument, List<String> fileList, String coursePath,
			String ckfinderPath, String courseWareExportPath, String courseId) throws Exception, IOException {
		logger.debug("---进入replaceExportImgPath方法---");
		Elements imgElements = htmlDocument.getElementsByTag("img");
		if (!imgElements.isEmpty()) {
			for (org.jsoup.nodes.Element img : imgElements) {
				boolean isFromCkDir = false;
				String src = img.attr("src");
				logger.debug("---要替换的图片标签的路径---" + src);
				if (src != null && !"".equals(src) && src.startsWith(Constants.SECTION_URL)) {
					String replacePath = "";
					if (src.startsWith(coursePath)) {// 若路径为课程目录路径
						isFromCkDir = false;
						replacePath = coursePath;
					} else if (src.startsWith(ckfinderPath)) {
						isFromCkDir = true;
						// 若路径为ckfinder配置目录路径
						replacePath = this.getCkResPath(ckfinderPath, src, courseId);
						logger.debug("替换前的文件路径：" + src);
						String srcFilePath = src.replace(ckfinderPath, Constants.COURSEWARE_RESOURCE_PATH);// 将uri替换为具体路径
						// 将ckfinder路径下的资源复制到导出目录下
						this.fileCopyCheck(srcFilePath, courseWareExportPath);
						logger.debug("--------将ckfinder目录下的文件" + src + " copy到导出目录" + courseWareExportPath + "files/");

					}
					if (isFromCkDir) {
						src = "files/" + src.replace(replacePath, "");
					} else {
						src = src.replace(replacePath, "");
					}

					logger.debug("替换后的文件路径：" + src);
					img.attr("src", src);// 替换img标签中的src应用根路径开始的绝对路径为相对路径
					fileList.add(src);
				}
			}
		}
		logger.debug("---结束replaceExportImgPath方法---");
	}

	/**
	 * 获得放在ckfinder路径下的资源的路径
	 * 
	 * @param ckfinderPath
	 * @param src
	 * @return
	 */
	private String getCkResPath(String ckfinderPath, String src, String courseId) throws Exception {
		String replacePath;
		String fileExt = CourseUtil.getExtName(src);
		boolean flag = src.contains(courseId);
		if (Constants.COURSEWARE_RESOURCE_IMAGE_EXT.contains(fileExt.toLowerCase())) {
			if (flag) {
				replacePath = ckfinderPath + courseId + "/" + Constants.COURSEWARE_RESOURCE_IMAGE_FOLDER;
			} else {
				replacePath = ckfinderPath + Constants.COURSEWARE_RESOURCE_IMAGE_FOLDER;
			}
		} else if (Constants.COURSEWARE_RESOURCE_FLASH_EXT.contains(fileExt.toLowerCase())) {
			if (flag) {
				replacePath = ckfinderPath + courseId + "/" + Constants.COURSEWARE_RESOURCE_FLASH_FOLDER;
			} else {
				replacePath = ckfinderPath + Constants.COURSEWARE_RESOURCE_FLASH_FOLDER;
			}
		} else {
			if (flag) {
				replacePath = ckfinderPath + courseId + "/" + Constants.COURSEWARE_RESOURCE_FILE_FOLDER;
			} else {
				replacePath = ckfinderPath + Constants.COURSEWARE_RESOURCE_FILE_FOLDER;
			}
		}
		return replacePath;
	}

	/**
	 * 删除指定目录中 最后修改日期距当前时间一天前的所有文件
	 * 
	 * @param export
	 *            要删除的文件所在目录
	 */
	public void deleteFormerFile(String dir) throws Exception {
		File dirFile = new File(dir);
		Date twoDaysAgo = new Date(new Date().getTime() - 24 * 3600 * 1000);// 距当前时间两天前
		if (!dirFile.isDirectory()) {
			throw new Exception(dir + "不是目录!");
		} else {
			// 若dirFile是目录
			java.io.File[] fileList = dirFile.listFiles();// 获得export所有文件
			try {
				for (int i = 0; i < fileList.length; i++) {
					if (fileList[i].lastModified() <= twoDaysAgo.getTime()) {
						// 若文件最后修改日期 为两天前的文件则删除
						if (fileList[i].isDirectory()) {// 若有目录则删除目录
							FileUtils.deleteDirectory(fileList[i]);
						} else {
							fileList[i].delete();
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * 通过父节点id获得对应类型的子节点数目
	 * 
	 * @param parentId
	 * @param childType
	 *            查询子节点的类型 模块 或 页
	 * @return
	 */
	public int findChildrenCountByParentId(Long parentId, String childType) throws Exception {
		int result = 0;
		if (CodeTable.module.equals(childType)) {// 若查询子节点为模块节点
			String findHql = "select count(*) from MeleteModuleModel where parentId=? and status in (?,?)";
			Object[] findParams = new Object[] { parentId, new Long(CodeTable.normal), new Long(CodeTable.hide) };
			List resultList = findEntity(findHql, findParams);
			if (resultList != null && !resultList.isEmpty()) {
				result = (Integer) resultList.get(0);
			}
		} else if (CodeTable.section.equals(childType)) {// 若查询子节点为页
			String findHql = "select count(*) from MeleteSectionModel where moduleId=? and status in (?,?)";
			Object[] findParams = new Object[] { parentId, new Long(CodeTable.normal), new Long(CodeTable.hide) };
			List resultList = findEntity(findHql, findParams);
			if (resultList != null && !resultList.isEmpty()) {
				result = (Integer) resultList.get(0);
			}
		} else {
			throw new Exception("子节点类型错误,传递子节点类型为" + childType);
		}

		return result;
	}

	/**
	 * 获得当前站点
	 * 
	 * @return
	 */
	private Site getCurSite() throws Exception {
		SiteService siteService = (SiteService) ComponentManager.get(SiteService.class);
		ToolManager toolManager = (ToolManager) ComponentManager.get(ToolManager.class);
		Site site = siteService.getSite(toolManager.getCurrentPlacement().getContext());
		return site;
	}

	/**
	 * 删除模块记录
	 * 
	 * @param moduleId
	 * @throws Exception
	 */
	public void delModule(Long moduleId) throws Exception {
		try {
			// 删除模块下的活动
			this.delModOrSecTest(moduleId, MeleteModuleModel.class);
			this.delModOrSecSelfTest(moduleId, MeleteModuleModel.class);
			this.delModOrSecForum(moduleId, MeleteModuleModel.class);

			List<MeleteSectionModel> sectionList = this.getAllSectionsByModuleId(moduleId);// 获取页集合
			for (MeleteSectionModel section : sectionList) {// 遍历页集合
				logger.debug("---删除模块" + moduleId + "下的页操作----页---" + section.getTitle() + "---" + section.getId());
				this.delSection(section.getId());
			}

			List<MeleteModuleModel> subList = this.getModuleByParentId(moduleId, true);// 获取下级模块集合
			for (MeleteModuleModel sub : subList) {// 遍历子模块集合
				this.delModule(sub.getId());
			}
			// 删除模块本身
			MeleteModuleModel module = (MeleteModuleModel) this.getModelById(MeleteModuleModel.class,
					new Long(moduleId));
			// 操作课件描述文件imsmanifest.xml将对应的模块节点item以及相关的页节点item和相关资源节点rescource一并删除移除
			this.delItem(module.getCourseId(), module.getModuleItemID());

			try {
				CacheUtil.getInstance().deleteModule(module.getId());// 删除学生缓存中的该模块缓存
			} catch (Exception e) {
				e.printStackTrace();
				logger.warn("删除缓存中的模块:" + module.getTitle() + "---id:" + module.getId() + "--=失败=--");
				logger.error(e.getMessage(), e);
			}
			logger.debug("---成功--删除缓存中的模块---" + module.getTitle() + "--id--" + module.getId());
			try {
				this.deleteEntity(module);// 删除数据库中模块记录
			} catch (Exception e) {
				e.printStackTrace();
				logger.warn("删除数据库中的模块:" + module.getTitle() + "---id:" + module.getId() + "--=失败=--");
				logger.error(e.getMessage(), e);
			}
			logger.debug("---成功--删除数据库的模块---" + module.getTitle());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 删除模块或页的论坛
	 * 
	 * @param modOrSecId
	 * @throws Exception
	 */
	private void delModOrSecForum(Long modOrSecId, Class clazz) throws Exception {
		Object[] param1 = { modOrSecId };
		List<MeleteForumModel> forumList = null;
		String forumDelHql = "";
		if (MeleteModuleModel.class == clazz) {
			forumList = this.getForumByModuleId(modOrSecId, false);
			forumDelHql = "delete from MeleteForumModel where moduleId=?";
		} else if (MeleteSectionModel.class == clazz) {
			forumList = this.getForumBySectionId(modOrSecId, false);
			forumDelHql = "delete from MeleteForumModel where sectionId=?";
		}
		this.delCacheForum(forumList);
		this.updateEntity(forumDelHql, param1);// 删除数据库中的对应模块下的讨论
		logger.debug("----删除数据库中--" + clazz.getName() + "--" + modOrSecId + "---中的讨论完毕");
	}

	/**
	 * 删除缓存中forumList包含的元素
	 * 
	 * @param forumList
	 */
	private void delCacheForum(List<MeleteForumModel> forumList) {
		for (MeleteForumModel forum : forumList) {
			logger.debug("---准备删除学生缓存中的讨论----讨论名称---" + forum.getName() + "---id---" + forum.getId());
			try {
				CacheUtil.getInstance().deleteForum(forum);// 删除所有学生缓存中的对应讨论
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.warn("删除缓存中的讨论:" + forum.getName() + "---id:" + forum.getId() + "--=失败=--");
				logger.error(e.getMessage(), e);
				continue;
			}
			logger.debug("---成功删除学生缓存中的讨论----" + forum.getName() + "---" + forum.getId());
		}
	}

	/**
	 * 删除模块或页中的前测
	 * 
	 * @param modOrSecId
	 * @param clazz
	 */
	private void delModOrSecSelfTest(Long modOrSecId, Class clazz) throws Exception {
		Object[] params = { modOrSecId };
		List<MeleteSelfTestModel> selfTestList = null;
		String delSelftestHql = "";
		if (MeleteModuleModel.class == clazz) {// 模块
			selfTestList = this.getSelftestByModuleId(modOrSecId, false);
			delSelftestHql = "delete from MeleteSelfTestModel where moduleId=?";
		} else if (MeleteSectionModel.class == clazz) {
			selfTestList = this.getSelftestBySectionId(modOrSecId, false);
			delSelftestHql = "delete from MeleteSelfTestModel where sectionId=?";
		}
		this.delCacheSelfTest(selfTestList);// 删除缓存中的前测
		this.updateEntity(delSelftestHql, params);// 删除数据库中的前测
		logger.debug("----删除数据库中--" + clazz.getName() + "---中的前测完毕");
	}

	/**
	 * 删除selfTestList中在缓存中的前测
	 * 
	 * @param selfTestList
	 */
	private void delCacheSelfTest(List<MeleteSelfTestModel> selfTestList) {
		for (MeleteSelfTestModel selfTest : selfTestList) {
			logger.debug("---准备删除学生缓存中的前测---名称---" + selfTest.getName() + "---id---" + selfTest.getId());
			try {
				CacheUtil.getInstance().deleteSelftest(selfTest);// 删除学生缓存中的前测
			} catch (Exception e) {
				e.printStackTrace();
				logger.warn("---删除缓存中的前测:" + selfTest.getName() + "---id:" + selfTest.getId() + "--=失败=--");
				logger.error(e.getMessage(), e);
				continue;
			}
			logger.debug("---成功删除学生缓存中的前测----" + selfTest.getName() + "---" + selfTest.getId());
		}
	}

	/**
	 * 删除模块或页中的缓存和数据库中的作业
	 * 
	 * @param modOrSecId
	 * @param testList
	 * @return
	 */
	private void delModOrSecTest(Long modOrSecId, Class clazz) throws Exception {
		List<MeleteTestModel> testList = null;
		Object[] param = { modOrSecId };
		String delTestHql = "";
		if (MeleteModuleModel.class == clazz) {// 删除数据库中该模块下的作业
			testList = this.getTestByModuleId(modOrSecId, false);
			delTestHql = "delete from MeleteTestModel where moduleId=?";
		} else if (MeleteSectionModel.class == clazz) {// 删除数据库中该页下的作业
			testList = this.getTestBySectionId(modOrSecId, false);
			delTestHql = "delete from MeleteTestModel where sectionId=?";
		}
		this.delCacheTest(testList);// 删除缓存中的作业
		try {
			this.updateEntity(delTestHql, param);// 删除数据库中作业记录
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("从数据库中删除作业----失败----");
			logger.error(e.getMessage(), e);
		}
		logger.debug("----删除数据库中--" + clazz.getName() + "---" + modOrSecId + "---中的作业完毕");
	}

	/**
	 * 删除缓存中testList中包含元素
	 * 
	 * @param testList
	 */
	private void delCacheTest(List<MeleteTestModel> testList) {
		for (MeleteTestModel test : testList) {
			logger.debug("---准备删除学生缓存中的作业----作业名称---" + test.getName() + "---id---" + test.getId());
			try {
				CacheUtil.getInstance().deleteTest(test);// 删除所有学生缓存中的对应作业
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.warn("--删除缓存中的作业:" + test.getName() + "---id:" + test.getId() + "--=失败=--");
				logger.error(e.getMessage(), e);
				continue;
			}
			logger.debug("---成功删除学生缓存中的作业----" + test.getName() + "---" + test.getId());
		}
	}

	/**
	 * 删除页操作
	 * 
	 * @param sectionId
	 */
	public void delSection(Long sectionId) throws Exception {
		try {
			// 删除测试
			this.delModOrSecTest(sectionId, MeleteSectionModel.class);
			// 删除前测
			this.delModOrSecSelfTest(sectionId, MeleteSectionModel.class);
			// 删除讨论
			this.delModOrSecForum(sectionId, MeleteSectionModel.class);
			// 删除页本身
			MeleteSectionModel section = (MeleteSectionModel) this.getModelById(MeleteSectionModel.class, new Long(
					sectionId));
			if (section == null) {
				return;
			}
			// 操作课件描述文件imsmanifest.xml将对应的模块节点item以及相关的页节点item和相关资源节点rescource一并删除移除
			this.delItem(section.getCourseId(), section.getSectionItemId());
			try {
				CacheUtil.getInstance().deleteSection(section);// 删除该页缓存
			} catch (Exception e) {
				e.printStackTrace();
				logger.warn("删除缓存中的页:" + section.getTitle() + "---id:" + section.getId() + "--=失败=--");
				logger.error(e.getMessage(), e);
			}
			logger.debug("-----删除缓存中的页---" + section.getTitle() + "--id--" + section.getId());
			try {
				this.deleteEntity(section);// 删除数据库中模块记录
			} catch (Exception e) {
				e.printStackTrace();
				logger.warn("删除数据库中的页:" + section.getTitle() + "---id:" + section.getId() + "--=失败=--");
				logger.error(e.getMessage(), e);
			}
			logger.debug("-----删除数据库的页---" + section.getTitle());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 获得模块下的所有页(隐藏, 正常)
	 * 
	 * @param moduleId
	 */
	private List<MeleteSectionModel> getAllSectionsByModuleId(Long moduleId) throws Exception {
		String hql = "from MeleteSectionModel where moduleId=?";
		Object[] parameters = { moduleId };
		List<MeleteSectionModel> list = this.findEntity(hql, parameters);
		return list;
	}

	/**
	 * 根据活动类型和id删除对应活动 (作业 前测 论坛)
	 * 
	 * @param clazz
	 * @param id
	 */
	public void delActivity(Class clazz, Long id) throws Exception {
		try {
			Object obj = this.findEntityById(clazz, id);
			if (obj == null) {
				return;
			}
			if (obj.getClass() == MeleteForumModel.class) {
				MeleteForumModel forum = (MeleteForumModel) obj;
				try {
					CacheUtil.getInstance().deleteForum(forum);// 删除缓存中的学生缓存中讨论
				} catch (Exception e) {
					logger.warn("从缓存中删除讨论:" + forum.getName() + "---id:" + forum.getId() + "--=失败=-");
					logger.error(e.getMessage(), e);
				}
				try {
					this.deleteEntity(forum);// 删除数据库中的讨论
				} catch (Exception e) {
					logger.warn("从数据库中删除讨论:" + forum.getName() + "---id:" + forum.getId() + "--=失败=-");
					logger.error(e.getMessage(), e);
				}
				logger.debug("---从缓存和数据库中删除讨论---成功---");
			} else if (obj.getClass() == MeleteSelfTestModel.class) {
				MeleteSelfTestModel selfTest = (MeleteSelfTestModel) obj;// 删除缓存中的学生缓存中前测
				try {
					CacheUtil.getInstance().deleteSelftest(selfTest);// 删除数据库中的前测
				} catch (Exception e) {
					logger.warn("从缓存中删除前测:" + selfTest.getName() + "---id:" + selfTest.getId() + "--=失败=-");
					logger.error(e.getMessage(), e);
				}
				try {
					this.deleteEntity(selfTest);
				} catch (Exception e) {
					logger.warn("从数据库中删除前测:" + selfTest.getName() + "---id:" + selfTest.getId() + "--=失败=-");
					logger.error(e.getMessage(), e);
				}
				logger.debug("---从缓存和数据库中删除前测---成功---");
			} else if (obj.getClass() == MeleteTestModel.class) {
				MeleteTestModel test = (MeleteTestModel) obj;
				try {
					CacheUtil.getInstance().deleteTest(test);// 删除缓存中的学生缓存中作业
				} catch (Exception e) {
					logger.warn("从缓存中删除作业:" + test.getName() + "---id:" + test.getId() + "--=失败=-");
					logger.error(e.getMessage(), e);
				}
				try {
					this.deleteEntity(test);// 删除数据库中的作业
				} catch (Exception e) {
					logger.warn("从数据库中删除作业:" + test.getName() + "---id:" + test.getId() + "--=失败=-");
					logger.error(e.getMessage(), e);
				}
				logger.debug("---从缓存和数据库中删除作业---成功---");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 文件拷贝检查
	 * 
	 * @param src
	 */
	private void fileCopyCheck(String src, String target) {
		File file = new File(src);
		if (file.exists()) {
			try {
				File targetFile = new File(target + "files/");
				if (!targetFile.exists()) {
					targetFile.mkdirs();
					logger.debug("---创建目录---" + targetFile.getAbsolutePath());
				}
				FileUtils.copyFileToDirectory(file, targetFile);
			} catch (IOException e) {
				logger.error("---复制文件出错---");
				logger.error(e.getMessage(), e);
			}
		} else {
			logger.info(src + "----文件不存在!");
		}
	}

	/**
	 * 
	 * 根据课程id得到所有课程下的作业（MeleteTestModel）
	 * 
	 * @author zihongyan 2013-8-16
	 * @param courseId
	 * @return
	 */
	public List<MeleteTestModel> getAllMeleteTestModelByCourseId(String courseId) {
		// 获得所有ModuleIds
		String hql1 = "select id from MeleteModuleModel where courseId=?";
		Object[] parameters = { courseId };
		List<Long> moduleIdList = this.findEntity(hql1, parameters);

		// 获得所有SectionIds
		String hql2 = "select id from MeleteSectionModel where courseId=?";
		List<Long> sectionIdList = this.findEntity(hql2, parameters);

		String moduleId = "";
		String sectionId = "";
		if (moduleIdList != null && !moduleIdList.isEmpty()) {
			for (int j = 0; j < moduleIdList.size() - 1; j++) {
				moduleId = moduleId + moduleIdList.get(j).toString() + ",";
			}
			moduleId = moduleId + moduleIdList.get(moduleIdList.size() - 1).toString();
		}
		if (sectionIdList != null && !sectionIdList.isEmpty()) {
			for (int i = 0; i < sectionIdList.size() - 1; i++) {
				sectionId = sectionId + sectionIdList.get(i).toString() + ",";
			}
			sectionId = sectionId + sectionIdList.get(sectionIdList.size() - 1).toString();
		}
		// 根据ModulesId和SectionId查询得到MeleteTestModel
		// 根据ModulesId和SectionId查询得到MeleteForumModel
		// 只查询正常状态的作业
		String hql = "from MeleteTestModel where status=" + CodeTable.normal + " and courseId = '" + courseId + "'";
		if (!moduleId.equals("") && sectionId.equals("")) {
			hql = hql + " and moduleId in(" + moduleId + ")";
		}
		if (!sectionId.equals("") && moduleId.equals("")) {
			hql = hql + " and sectionId in(" + sectionId + ")";
		}
		if (!sectionId.equals("") && !moduleId.equals("")) {
			hql = hql + " and ( moduleId in(" + moduleId + ") or sectionId in(" + sectionId + ") )";
		}
		hql = hql + " order by startOpenDate,id";
		List<MeleteTestModel> testList = this.findEntity(hql);
		return testList;
	}

	/**
	 * 根据课程id得到所有课程下的论坛帖子(MeleteForumModel)
	 * 
	 * @author zihongyan 2013-8-22
	 * @param courseId
	 * @param modelId
	 * @return
	 */
	public List<MeleteForumModel> getAllMeleteForumModelByCourseId(String courseId,Long modelId) {
		// 获得所有ModuleIds,如果有传入modelId,则只查询该model下的数据
		Object[] parameters = { courseId };
		List<Long> moduleIdList = null;
		if(modelId==null){
			String hql1 = "select id from MeleteModuleModel where courseId=?";
			moduleIdList = this.findEntity(hql1, parameters);
		}else{
			moduleIdList = new ArrayList<Long>();
			moduleIdList.add(modelId);  
		}

		// 获得所有SectionIds
		String hql2 = "select id from MeleteSectionModel where courseId=?";
		List<Long> sectionIdList = this.findEntity(hql2, parameters);

		String moduleId = "";
		String sectionId = "";
		if (moduleIdList != null && !moduleIdList.isEmpty()) {
			for (int j = 0; j < moduleIdList.size() - 1; j++) {
				moduleId = moduleId + moduleIdList.get(j).toString() + ",";
			}
			moduleId = moduleId + moduleIdList.get(moduleIdList.size() - 1).toString();
		}
		if (sectionIdList != null && !sectionIdList.isEmpty()) {
			for (int i = 0; i < sectionIdList.size() - 1; i++) {
				sectionId = sectionId + sectionIdList.get(i).toString() + ",";
			}
			sectionId = sectionId + sectionIdList.get(sectionIdList.size() - 1).toString();
		}
		// 根据ModulesId和SectionId查询得到MeleteForumModel
		String hql = "from MeleteForumModel where courseId = '" + courseId + "'";
		if (!moduleId.equals("") && sectionId.equals("")) {
			hql = hql + " and moduleId in(" + moduleId + ")";
		}
		if (!sectionId.equals("") && moduleId.equals("")) {
			hql = hql + " and sectionId in(" + sectionId + ")";
		}
		if (!sectionId.equals("") && !moduleId.equals("")) {
			hql = hql + " and ( moduleId in(" + moduleId + ") or sectionId in(" + sectionId + ") )";
		}
		List<MeleteForumModel> forumList = this.findEntity(hql);
		return forumList;
	}

	/**
	 * 根据记录id得到所有的尝试记录id
	 * 
	 * @author zihongyan 2013-8-25
	 * @param testRecordId
	 * @return
	 */
	public List<MeleteTestAttemptModel> getAllMeleteTestAttemptModelByTestRecordId(String testRecordId) {
		String hql = "from MeleteTestAttemptModel where meleteTestRecordId=? order by startTime desc";
		Object[] parameters = { Long.parseLong(testRecordId) };
		List<MeleteTestAttemptModel> meleteTestAttempList = this.findEntity(hql, parameters);
		return meleteTestAttempList;
	}

	/**
	 * 获取课程学习记录
	 * 
	 * @param courseId
	 * @return
	 */
	public List<Long> getStudyrecordidListByCourseId(String courseId) {
		String hql = "select studyrecordId from MeleteStudyRecordModel where courseId=?";
		List list = this.getHibernateTemplate().find(hql, courseId);
		return list;
	}

	/**
	 * 根据业务对象获取课程id
	 */
	public String getCourseIdFromModel(Object obj) {
		String courseId = null;
		try {
			if (obj.getClass() == MeleteTestModel.class) {// 是作业
				MeleteTestModel testModel = (MeleteTestModel) obj;

				String belongType = testModel.getBelongType();
				if (belongType.equals(CodeTable.belongMudole)) {
					Long moduleId = testModel.getModuleId();
					MeleteModuleModel module = (MeleteModuleModel) this.getHibernateTemplate().get(
							MeleteModuleModel.class, moduleId);
					courseId = module.getCourseId();
				} else {
					Long sectionId = testModel.getSectionId();
					MeleteSectionModel section = (MeleteSectionModel) this.getHibernateTemplate().get(
							MeleteSectionModel.class, sectionId);
					courseId = section.getCourseId();
				}
			} else if (obj.getClass() == MeleteForumModel.class) {// 是讨论
				MeleteForumModel model = (MeleteForumModel) obj;

				String belongType = model.getBelongType();
				if (belongType.equals(CodeTable.belongMudole)) {
					Long moduleId = model.getModuleId();
					MeleteModuleModel module = (MeleteModuleModel) this.getHibernateTemplate().get(
							MeleteModuleModel.class, moduleId);
					courseId = module.getCourseId();
				} else {
					Long sectionId = model.getSectionId();
					MeleteSectionModel section = (MeleteSectionModel) this.getHibernateTemplate().get(
							MeleteSectionModel.class, sectionId);
					courseId = section.getCourseId();
				}
			} else {// 是前测
				MeleteSelfTestModel model = (MeleteSelfTestModel) obj;

				String belongType = model.getBelongType();
				if (belongType.equals(CodeTable.belongMudole)) {
					Long moduleId = model.getModuleId();
					MeleteModuleModel module = (MeleteModuleModel) this.getHibernateTemplate().get(
							MeleteModuleModel.class, moduleId);
					courseId = module.getCourseId();
				} else {
					Long sectionId = model.getSectionId();
					MeleteSectionModel section = (MeleteSectionModel) this.getHibernateTemplate().get(
							MeleteSectionModel.class, sectionId);
					courseId = section.getCourseId();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return courseId;
	}

	/**
	 * 使用指定模板的课程数量
	 * 
	 * @param templateName
	 * @return
	 * @throws Exception
	 */
	public Integer countCourseUseTemplate(String templateName) throws Exception {
		try {
			String hql = "select count(*) from MeleteCourseModel where playerTemplate=?";
			Object[] parameters = { templateName };
			List<Object> countList = this.findEntity(hql, parameters);
			if (countList.isEmpty() || countList.size() == 0) {
				return 0;
			} else {
				return new Integer(countList.get(0).toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	// 生成课件预览文件
	public List<Object> previewScorm(String wareId) throws Exception {
		List<Object> resultList = new ArrayList<Object>();
		try {
			String securePath = Constants.SECURE_PATH;
			String warePath = new File(securePath) + "/" + Constants.COURSE_RESOURCE_PACK_PATH + "/" + wareId + "/";
			String coursePath = new File(Constants.COURSEWARE_PATH) + "/" + wareId + "/";
			File courseRootFile = new File(coursePath);
			if (!courseRootFile.exists()) { // 文件夹不存在则创建
				courseRootFile.mkdirs();
			}
			String manifestFile = warePath + "imsmanifest.xml";
			ADLDOMParser domParser = new ADLDOMParser();
			domParser.createDocument(manifestFile, true, false);
			Document mDocument = domParser.getDocument(); // 解析imsmanifest.xml文件
			Node mManifest = mDocument.getDocumentElement();
			Node tempOrg = getOrgNode(mDocument);
			Vector moduleList = DOMTreeUtility.getNodes(tempOrg, "item");// 一级模块集合

			Node resourcesNode = DOMTreeUtility.getNode(mManifest, "resources");
			Vector resources = DOMTreeUtility.getNodes(resourcesNode, "resource");
			List<String> nodeRefList = new ArrayList<String>();// 存放资源引用id集合
			String mods = "";
			for (int j = 0; j < moduleList.size(); j++) {
				Node tempModule = (Node) moduleList.elementAt(j);
				String tempModuleIdentifier = DOMTreeUtility.getAttributeValue(tempModule, "identifier");// 模块id
				if (StringUtils.isNotBlank(mods)) {
					mods += "," + tempModuleIdentifier;
				} else {
					mods += tempModuleIdentifier;
				}
				Vector childList = DOMTreeUtility.getNodes(tempModule, "item");// 下级模块集合
				Node childNode = (Node) childList.elementAt(0);
				String nodeRef = DOMTreeUtility.getAttributeValue(childNode, "identifierref");
				previewSubScorm(resources, tempModule, warePath, coursePath, nodeRefList, wareId);
			}

			this.removeResource(resources, nodeRefList, false);// 遍历resources
			// 移除资源节点的identifier不在nodeRefList中的资源节点
			String courseIdAndimportItemIds = wareId + ";" + mods;
			File imsmanifestFile = new File(coursePath + "imsmanifest.xml");
			if (imsmanifestFile.exists()) {// 若存在则先删除文件
				imsmanifestFile.delete();
			}
			this.toCourseManifest(coursePath, mDocument, tempOrg, resourcesNode, CodeTable.course, courseIdAndimportItemIds);// 将修改过得mDocument写入到coursePath中的课件描述文件中

			return resultList;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	private void previewSubScorm(Vector resources, Node parentNode, String warePath, String coursePath, List<String> nodeRefList,
			String wareId) throws Exception {
		List<Object> resultList = new ArrayList<Object>();
		try {
			Vector childList = DOMTreeUtility.getNodes(parentNode, "item");// 下级模块集合
			for (int j = 0; j < childList.size(); j++) {
				Node childNode = (Node) childList.elementAt(j);
				String nodeTitle = DOMTreeUtility.getNodeValue(DOMTreeUtility.getNode(childNode, "title"));// 标题
				String nodeRef = DOMTreeUtility.getAttributeValue(childNode, "identifierref");
				String identifier = DOMTreeUtility.getAttributeValue(childNode, "identifier");
				if (nodeRef == null || nodeRef.equals("")) { // 是模块节点
					Vector childrenList = DOMTreeUtility.getNodes(childNode, "item");// 下级模块集合
					previewSubScorm(resources, childNode, warePath, coursePath, nodeRefList, wareId);
				} else {// 是页节点
					nodeRefList.add(nodeRef);// 将引用到的资源节点id放入list中
					this.previewSection(resources, nodeRef, identifier, coursePath, warePath, wareId);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void previewSection(Vector resources, String nodeRef, String identifier, String coursePath, String warePath, 
			String wareId) throws Exception {
		for (int i = 0; i < resources.size(); i++) { // 遍历资源标签
			Node resourceNode = (Node) resources.elementAt(i); // 获取资源节点
			String resourceID = DOMTreeUtility.getAttributeValue(resourceNode, "identifier"); // 获取节点的identifier属性
			if (nodeRef.equals(resourceID)) { // 页所对应的资源节点
				String nodeHref = DOMTreeUtility.getAttributeValue(resourceNode, "href");// htm路径
				decodeHandler decoder = new decodeHandler(nodeHref, "UTF-8");
				decoder.decodeName();
				Vector fileList = DOMTreeUtility.getNodes(resourceNode, "file");// 获取file集合
				for (int k = 0; k < fileList.size(); k++) {// 遍历文件标签
					Node fileNode = (Node) fileList.elementAt(k);// 获取文件节点
					String fileHref = DOMTreeUtility.getAttributeValue(fileNode, "href");// 文件的路径
					decodeHandler decodeFile = new decodeHandler(fileHref, "UTF-8");
					decodeFile.decodeName();
					fileHref = warePath + decodeFile.getDecodedFileName();// 文件资源的绝对路径
					File source = new File(fileHref);// 获取源文件
					if (source.exists()) {// 若源文件存在
						File desc = new File(coursePath + decodeFile.getDecodedFileName());// 获取目标文件
						createFile(desc); // 创建目标文件
						copyFile(desc, source, wareId); // 复制文件
					} else {
						logger.info("从课件资源目录向课件预览目录复制文件时出错, 原因:" + source.getAbsolutePath() + "文件不存在, 该文件在在课件描述文件中的resource标签中的file标签中已配置但实际该资源不存在");
					}
				}

			}
		}
	}
	
	/**
	 * 根据id得到作业对象
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public MeleteTestModel getMeleteTestModelById(Long id) throws Exception{
		return this.getHibernateTemplate().get(MeleteTestModel.class, id);
	}
}
