package org.sakaiproject.resource.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.helper.StringUtil;
import org.sakaiproject.component.cover.ComponentManager;
import org.sakaiproject.memory.api.Cache;
import org.sakaiproject.memory.api.MemoryService;
import org.sakaiproject.resource.api.course.model.MeleteCourseModel;
import org.sakaiproject.resource.api.course.model.MeleteForumModel;
import org.sakaiproject.resource.api.course.model.MeleteModuleModel;
import org.sakaiproject.resource.api.course.model.MeleteSectionModel;
import org.sakaiproject.resource.api.course.model.MeleteSelfTestModel;
import org.sakaiproject.resource.api.course.model.MeleteTestModel;
import org.sakaiproject.resource.api.course.service.ICourseService;
import org.sakaiproject.resource.api.study.model.MeleteForumRecordModel;
import org.sakaiproject.resource.api.study.model.MeleteModuleRecordModel;
import org.sakaiproject.resource.api.study.model.MeleteSectionRecordModel;
import org.sakaiproject.resource.api.study.model.MeleteSelftestRecordModel;
import org.sakaiproject.resource.api.study.model.MeleteStudyRecordModel;
import org.sakaiproject.resource.api.study.model.MeleteTestRecordModel;
import org.sakaiproject.resource.api.study.service.IStudyService;

public class CacheUtil {
	private Log logger = LogFactory.getLog(CacheUtil.class);

	private Cache cache;
	private IStudyService studyService;
	private ICourseService courseService;

	private Cache getCache() {
		if (cache == null) {
			MemoryService cacheService = (MemoryService) ComponentManager.get(MemoryService.class);
			cache = cacheService.newCache(Constants.CACHE_NAME);
		}
		return cache;
	}

	public void setCache(Cache cache) {
		this.cache = cache;
	}

	public IStudyService getStudyService() {
		return studyService;
	}

	public void setStudyService(IStudyService studyService) {
		this.studyService = studyService;
	}

	public ICourseService getCourseService() {
		return courseService;
	}

	public void setCourseService(ICourseService courseService) {
		this.courseService = courseService;
	}

	public static CacheUtil getInstance() {
		CacheUtil util = (CacheUtil) SpringContextUtil.getBean("cacheUtil");
		// 初始化缓存
		util.getCache();
		return util;
	}

	/**
	 * 重置课程缓存
	 * 
	 * @param siteId
	 */
	public void resetCacheOfCourse(String courseId) {
		try {
			MeleteCourseModel course = courseService.getCourseById(courseId);
//			this.getCache().remove(course.getSiteId());
			this.getCacheOfCourse(course.getSiteId());

			List<Long> list = courseService.getStudyrecordidListByCourseId(course.getId());
//			for (Long studyrecordId : list) {
//				this.getCache().remove(studyrecordId.toString());
//			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 获取课程缓存
	 * 
	 * @param siteId
	 * @return
	 */
	public CacheElement getCacheOfCourse(String siteId) {
		CacheElement ce = null;
		try {
			if (StringUtil.isBlank(siteId)) {
				return null;
			}
//			Object e = this.getCache().get(siteId);
//			if (e != null) {
//				return (CacheElement) e;
//			}
			MeleteCourseModel course = courseService.getCourseBySiteId(siteId);
			String courseId = course.getId();

			List<MeleteModuleModel> moduleList = courseService.getAllModule(courseId);
			List<MeleteSectionModel> sectionList = courseService.getAllSection(courseId);
			List<MeleteTestModel> testList = courseService.getAllTest(courseId);
			List<MeleteForumModel> forumList = courseService.getAllForum(courseId);
			List<MeleteSelfTestModel> selfList = courseService.getAllSelfTest(courseId);

			ce = new CacheElement(course, moduleList, sectionList, testList, forumList, selfList);
//			this.getCache().put(courseId, ce);

			String template = course.getPlayerTemplate();
			if (!CodeTable.template_default.equals(template)) {
				// 遍历模块model列表
				for (MeleteModuleModel module : moduleList) {
					Long id = module.getId();
					Boolean subLeafObj = ce.getModuleLeaf(id);// 是否有子模块
					boolean subLeaf = true;
					if (subLeafObj == null) {
						subLeaf = courseService.leafModuleByParentId(id, false);// 是否有子模块
						ce.addModuleLeaf(id, subLeaf);
					}

					Boolean secLeafObj = ce.getSectionLeaf(id);// 是否有页
					boolean secLeaf = true;
					if (secLeafObj == null) {
						secLeaf = courseService.leafSectionByModuleId(id);// 是否有页
						ce.addSectionLeaf(id, secLeaf);
					}

					Boolean testLeafObj = ce.getTestLeaf(id);// 是否有作业
					boolean testLeaf = true;
					if (testLeafObj == null) {
						testLeaf = courseService.leafTestByModuleId(id, false);
						ce.addTestLeaf(id, testLeaf);
					}

					Boolean forumLeafObj = ce.getForumLeaf(id);// 是否有论坛
					boolean forumLeaf = true;
					if (forumLeafObj == null) {
						forumLeaf = courseService.leafForunByModuleId(id, false);
						ce.addForumLeaf(id, forumLeaf);
					}

					Boolean selftestLeafObj = ce.getSelftestLeaf(id);// 是否有前测
					boolean selftestLeaf = true;
					if (selftestLeafObj == null) {
						selftestLeaf = (module.getModuleSelftest() == null ? true : false);// 是否有前测
						ce.addSelftestLeaf(id, selftestLeaf);
					}
				}

				// 遍历页model列表
				for (MeleteSectionModel section : sectionList) {
					Long id = section.getId();

					Boolean testLeafObj = ce.getTestLeaf(id);// 是否有作业
					boolean testLeaf = true;
					if (testLeafObj == null) {
						testLeaf = courseService.leafTestBySectionId(id, false);// 是否有作业
						ce.addTestLeaf(id, testLeaf);
					}

					Boolean forumLeafObj = ce.getForumLeaf(id);// 是否有讨论
					boolean forumLeaf = true;
					if (forumLeafObj == null) {
						forumLeaf = courseService.leafForumBySectionId(id, false);// 是否有讨论
						ce.addForumLeaf(id, forumLeaf);
					}

					Boolean selfLeafObj = ce.getSelftestLeaf(id);
					boolean selfLeaf = true;
					if (selfLeafObj == null) {
						selfLeaf = section.getSectionSelftest() == null ? true : false;// 是否有前测
						ce.addSelftestLeaf(id, selfLeaf);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		return ce;
	}

	/**
	 * 获取学习记录缓存
	 * 
	 * @param studyRecordId
	 * @return
	 */
	public CacheElement getCacheOfStudyrecord(String studyRecordId) {
		Object obj = null;//this.getCache().get(studyRecordId);
		CacheElement ce = null;
		if (obj == null) {
			MeleteStudyRecordModel studyRecord = studyService.getStudyRecordById(studyRecordId);
			if (studyRecord != null) {
				try {
					ce = getCacheOfStudyrecord(studyRecord.getCourseId(), studyRecord.getStudentId(), studyRecordId);
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			}
		} else {
			ce = (CacheElement) obj;
		}

		return ce;
	}

	/**
	 * 获取学生学习记录缓存
	 * 
	 * @param siteId
	 * @param studentId
	 * @param studyRecordId
	 * @return
	 * @throws Exception
	 */
	public CacheElement getCacheOfStudyrecord(String siteId, String studentId, String studyRecordId) throws Exception {
		CacheElement ce = null;
		try {
			Object e = null;
//			if (!StringUtil.isBlank(studyRecordId)) {
//				e = this.getCache().get(studyRecordId);
//				if (e != null) {
//					return (CacheElement) e;
//				}
//			}

			// 获取课程缓存
			CacheElement cacheCourse = getCacheOfCourse(siteId);
			MeleteCourseModel course = cacheCourse.getCourse();

			// 获取学习记录(用于查询模块记录或节点记录)
			MeleteStudyRecordModel studyRecord = checkStudyRecord(course.getId(), studentId);
			studyRecordId = studyRecord.getStudyrecordId().toString();
//			e = this.getCache().get(studyRecordId);
//			if (e != null) {
//				return (CacheElement) e;
//			}
			// 缓存为空，需创建缓存
			List<MeleteModuleModel> moduleList = cacheCourse.getModules();
			List<MeleteModuleRecordModel> moduleRecordList = new ArrayList<MeleteModuleRecordModel>();
			for (MeleteModuleModel module : moduleList) {
				// 获取模块学习记录
				MeleteModuleRecordModel moduleRecord = studyService.getModuleRecordById(studyRecordId,
						String.valueOf(module.getId()));
				// 数据库中没有记录，说明是第一次加载，需要在数据库中插入一条初始记录
				if (moduleRecord == null) {
					moduleRecord = new MeleteModuleRecordModel();
					moduleRecord.setModuleId(module.getId()); // 对应模块ID
					moduleRecord.setCourseId(module.getCourseId()); // 课程ID
					moduleRecord.setOpenStatus(new Long(CodeTable.openStatusNo));// 开启状态
					moduleRecord.setStudyrecordId(new Long(studyRecordId)); // 学习记录ID
					moduleRecord.setStudyTime(new Long(0));// 学习时长
					if (module.getRequirement() == null || module.getRequirement().trim().equals("")) {
						moduleRecord.setStatus(new Long(CodeTable.passStatusYes));// 通过状态
					} else {
						moduleRecord.setStatus(new Long(CodeTable.passStatusNo));// 通过状态
					}
					studyService.saveModuleRecord(moduleRecord);
				}
				moduleRecordList.add(moduleRecord);
			}

			List<MeleteSectionModel> sectionList = cacheCourse.getSections();
			List<MeleteSectionRecordModel> sectionRecordList = new ArrayList<MeleteSectionRecordModel>();
			for (MeleteSectionModel section : sectionList) {
				// 获取页记录
				MeleteSectionRecordModel sectionRecord = studyService.getSectionRecordById(studyRecordId,
						String.valueOf(section.getId()));
				// 第一次加载课程树时保持相应课程学习记录
				if (sectionRecord == null) {
					sectionRecord = new MeleteSectionRecordModel();
					sectionRecord.setSectionId(section.getId()); // 对应节点ID
					sectionRecord.setCourseId(course.getId()); // 课程ID
					sectionRecord.setStartStudyTime(null); // 学习开始时间
					sectionRecord.setStudyTime(0L); // 学习时长
					sectionRecord.setStudyrecordId(new Long(studyRecordId));// 学习记录ID
					sectionRecord.setOpenStatus(new Long(CodeTable.openStatusNo));// 开启状态
					if (section.getRequirement() == null || section.getRequirement().trim().equals("")) {
						sectionRecord.setStatus(new Long(CodeTable.passStatusYes));
					} else {
						sectionRecord.setStatus(new Long(CodeTable.passStatusNo));
					}

					MeleteModuleModel module = courseService.getModuleBySectionId(section.getId().toString());// 获得section所属的module
					MeleteModuleRecordModel moduleRecord = null;
					for (MeleteModuleRecordModel mRecord : moduleRecordList) {
						if (mRecord.getModuleId().longValue() == module.getId().longValue()) {
							moduleRecord = mRecord;
							break;
						}
					}
					sectionRecord.setMeleteModuleRecordId(moduleRecord.getModulerecordId());// 设置对应的sectionRecord对应的moduleRecordId
					studyService.saveSectionRecord(sectionRecord);
				}
				sectionRecordList.add(sectionRecord);
			}

			List<MeleteTestModel> testList = cacheCourse.getTests();
			List<MeleteTestRecordModel> testRecordList = new ArrayList<MeleteTestRecordModel>();
			for (MeleteTestModel test : testList) {
				// 获取作业记录
				MeleteTestRecordModel testRecord = studyService.getTestRecordById(studyRecordId,
						String.valueOf(test.getId()));
				// 第一次加载作业树时保存相应课程作业记录
				if (testRecord == null) {
					testRecord = new MeleteTestRecordModel();
					testRecord.setTestId(test.getId());
					testRecord.setCourseId(course.getId());
					testRecord.setScore(0F);
					testRecord.setAttemptNumber(0L);
					testRecord.setStatus(Long.valueOf(CodeTable.passStatusNo));
					testRecord.setModuleId(test.getModuleId());
					testRecord.setSectionId(test.getSectionId());
					testRecord.setStudyrecordId(Long.valueOf(studyRecordId));
					testRecord.setTestName(test.getName());
					studyService.saveTestRecord(testRecord);
				}
				testRecordList.add(testRecord);
			}

			List<MeleteForumModel> forumList = cacheCourse.getForums();
			List<MeleteForumRecordModel> forumRecordList = new ArrayList<MeleteForumRecordModel>();
			for (MeleteForumModel forum : forumList) {
				MeleteForumRecordModel forumRecord = studyService.getForumRecordById(studyRecordId,
						String.valueOf(forum.getId()));
				// 第一次加载讨论树时保存相应讨论记录
				if (forumRecord == null) {
					forumRecord = new MeleteForumRecordModel();
					forumRecord.setForumId(forum.getId());
					forumRecord.setStudyrecordId(Long.valueOf(studyRecordId));
					forumRecord.setCourseId(course.getId());
					forumRecord.setAttemptNumber(0L);
					forumRecord.setStatus(Long.valueOf(CodeTable.passStatusNo));
					forumRecord.setModuleId(forum.getModuleId());
					forumRecord.setSectionId(forum.getSectionId());
					studyService.saveForumRecord(forumRecord);
				}
				forumRecordList.add(forumRecord);
			}
			// 第一次加载前测记录
			List<MeleteSelfTestModel> selfList = cacheCourse.getSelfTests();
			List<MeleteSelftestRecordModel> selfRecordList = new ArrayList<MeleteSelftestRecordModel>();
			for (MeleteSelfTestModel self : selfList) {
				MeleteSelftestRecordModel selftestRecord = studyService.getSelftestRecordById(studyRecordId,
						String.valueOf(self.getId()));
				if (selftestRecord == null) {
					selftestRecord = new MeleteSelftestRecordModel();
					selftestRecord.setStudyrecordId(Long.valueOf(studyRecordId));
					selftestRecord.setCourseId(course.getId());
					selftestRecord.setScore(0F);
					selftestRecord.setAttemptNumber(0L);
					selftestRecord.setSelftestId(self.getId());
					selftestRecord.setStatus(Long.valueOf(CodeTable.passStatusNo));
					selftestRecord.setModuleId(self.getModuleId());
					selftestRecord.setSectionId(self.getSectionId());
					selftestRecord.setSelftestName(self.getName());
					studyService.saveSelftestRecord(selftestRecord);
				}
				selfRecordList.add(selftestRecord);
			}// 建缓存存入
			ce = new CacheElement(studyRecord, moduleRecordList, sectionRecordList, testRecordList, forumRecordList,
					selfRecordList);

//			this.getCache().put(studyRecordId, ce);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}

		return ce;
	}

	/**
	 * 检查学生学习记录情况
	 * 
	 * @param courseId
	 * @param studentId
	 * @return
	 * @throws Exception
	 */
	private MeleteStudyRecordModel checkStudyRecord(String courseId, String studentId) throws Exception {
		MeleteStudyRecordModel studyRecord = studyService.getStudyRecordById(courseId, studentId);
		// 第一次加载课程树时保存学习记录信息
		if (studyRecord == null) {
			studyRecord = new MeleteStudyRecordModel();
			studyRecord.setCourseId(courseId); // 课程ID
			studyRecord.setStudentId(studentId); // 学生ID
			studyRecord.setScore(0f); // 课程成绩
			studyRecord.setLessonStatus(Long.valueOf(CodeTable.passStatusNo)); // 课程通过状态
			studyRecord.setStartStudyTime(null); // 学习开始时间
			studyService.saveStudyRecord(studyRecord);
		}

		return studyRecord;
	}

	/**
	 * @param ce
	 * @param self
	 * @return
	 * @throws Exception
	 */
	public MeleteSelftestRecordModel getStuSelftestRecord(CacheElement ce, MeleteSelfTestModel self,
			String studyrecordId, String courseId) throws Exception {
		MeleteSelftestRecordModel selftestRecord = ce.getSelftestRecord(self.getId());
		if (selftestRecord == null) {
			selftestRecord = studyService.getSelftestRecordById(studyrecordId, String.valueOf(self.getId()));
			if (selftestRecord == null) {
				selftestRecord = new MeleteSelftestRecordModel();
				selftestRecord.setStudyrecordId(Long.valueOf(studyrecordId));
				selftestRecord.setCourseId(courseId);
				selftestRecord.setScore(0F);
				selftestRecord.setAttemptNumber(0L);
				selftestRecord.setSelftestId(self.getId());
				selftestRecord.setStatus(Long.valueOf(CodeTable.passStatusNo));
				selftestRecord.setModuleId(self.getModuleId());
				selftestRecord.setSectionId(self.getSectionId());
				selftestRecord.setSelftestName(self.getName());
				studyService.saveSelftestRecord(selftestRecord);
			}
		}
		return selftestRecord;
	}

	/**
	 * @param ce
	 * @param forum
	 * @return
	 * @throws Exception
	 */
	public MeleteForumRecordModel getStuForumRecord(CacheElement ce, MeleteForumModel forum, String studyRecordId,
			String courseId) throws Exception {
		MeleteForumRecordModel forumRecord = ce.getForumRecord(forum.getId());
		if (forumRecord == null) {
			forumRecord = studyService.getForumRecordById(studyRecordId, String.valueOf(forum.getId()));
			// 第一次加载讨论树时保存相应讨论记录
			if (forumRecord == null) {
				forumRecord = new MeleteForumRecordModel();
				forumRecord.setForumId(forum.getId());
				forumRecord.setStudyrecordId(Long.valueOf(studyRecordId));
				forumRecord.setCourseId(courseId);
				forumRecord.setAttemptNumber(0L);
				forumRecord.setStatus(Long.valueOf(CodeTable.passStatusNo));
				forumRecord.setModuleId(forum.getModuleId());
				forumRecord.setSectionId(forum.getSectionId());
				studyService.saveForumRecord(forumRecord);
			}
		}
		return forumRecord;
	}

	/**
	 * @param ce
	 * @param test
	 * @return
	 * @throws Exception
	 */
	public MeleteTestRecordModel getStuTestRecord(CacheElement ce, MeleteTestModel test, String studyrecordId,
			String courseId) throws Exception {
		MeleteTestRecordModel testRecord = ce.getTestRecord(test.getId());
		if (testRecord == null) {
			testRecord = studyService.getTestRecordById(studyrecordId, String.valueOf(test.getId()));
			// 第一次加载作业树时保存相应课程作业记录
			if (testRecord == null) {
				testRecord = new MeleteTestRecordModel();
				testRecord.setTestId(test.getId());
				testRecord.setCourseId(courseId);
				testRecord.setScore(0F);
				testRecord.setAttemptNumber(0L);
				testRecord.setStatus(Long.valueOf(CodeTable.passStatusNo));
				testRecord.setModuleId(test.getModuleId());
				testRecord.setSectionId(test.getSectionId());
				testRecord.setStudyrecordId(Long.valueOf(studyrecordId));
				testRecord.setTestName(test.getName());
				studyService.saveTestRecord(testRecord);
			}
		}
		return testRecord;
	}

	/**
	 * @param ce
	 * @param section
	 * @return
	 * @throws Exception
	 */
	public MeleteSectionRecordModel getStuSecRecord(CacheElement ce, MeleteSectionModel section, String studyrecordId)
			throws Exception {
		MeleteSectionRecordModel sectionRecord = ce.getSectonRecord(section.getId());
		if (sectionRecord == null) {// 若缓存中sectionRecord为空则从数据库中取
			sectionRecord = studyService.getSectionRecordById(studyrecordId, String.valueOf(section.getId()));
			if (sectionRecord == null) {// 若数据库中无页学习记录新建

				sectionRecord = new MeleteSectionRecordModel();
				sectionRecord.setSectionId(section.getId()); // 对应节点ID
				sectionRecord.setCourseId(section.getCourseId()); // 课程ID
				sectionRecord.setStartStudyTime(null); // 学习开始时间
				sectionRecord.setStudyTime(0L); // 学习时长
				sectionRecord.setStudyrecordId(new Long(studyrecordId));// 学习记录ID
				sectionRecord.setOpenStatus(new Long(CodeTable.openStatusNo));// 开启状态

				MeleteModuleModel module = courseService.getModuleBySectionId(section.getId().toString());// 获得section所属的module
				MeleteModuleRecordModel moduleRecord = ce.getModuleRecord(module.getId());
				sectionRecord.setMeleteModuleRecordId(moduleRecord.getModulerecordId());// 设置对应的sectionRecord对应的moduleRecordId
				studyService.saveSectionRecord(sectionRecord);
			}
			ce.editSectionRecord(sectionRecord);// 放入缓存
		}
		return sectionRecord;
	}

	/**
	 * 获得学生的模块学习记录
	 * 
	 * @param ce
	 * @param module
	 * @return 学习模块学习记录
	 * @throws Exception
	 */
	public MeleteModuleRecordModel getStuModuleRecord(CacheElement ce, MeleteModuleModel module, String studyRecordId)
			throws Exception {
		MeleteModuleRecordModel moduleRecord = ce.getModuleRecord(module.getId());// 从缓存中读取
		if (moduleRecord == null) {// 若缓存中moduleRecord为空
			moduleRecord = studyService.getModuleRecordById(studyRecordId, String.valueOf(module.getId()));
			if (moduleRecord == null) {// 若数据库中无模块学习记录则创建
				moduleRecord = new MeleteModuleRecordModel();
				moduleRecord.setModuleId(module.getId()); // 对应模块ID
				moduleRecord.setCourseId(module.getCourseId()); // 课程ID
				moduleRecord.setOpenStatus(new Long(CodeTable.openStatusNo));// 开启状态
				moduleRecord.setStudyrecordId(new Long(studyRecordId)); // 学习记录ID
				moduleRecord.setStudyTime(new Long(0));// 学习时长
				studyService.saveModuleRecord(moduleRecord);
			}
			ce.editModuleRecord(moduleRecord);// 放入缓存
		}
		return moduleRecord;
	}

	/**
	 * 更新所有学生缓存中的节点model
	 * 
	 * @param module
	 */
	public void updateModule(MeleteModuleModel module) {
		String courseId = module.getCourseId();
		CacheElement c = this.getCacheOfCourse(courseId);
		c.updateModule(module);
	}

	/**
	 * 向所有学生缓存中添加一个节点model
	 * 
	 * @param module
	 */
	public void addModule(MeleteModuleModel module) {
		String courseId = module.getCourseId();
		CacheElement c = this.getCacheOfCourse(courseId);
		c.updateModule(module);

		List<Long> list = courseService.getStudyrecordidListByCourseId(courseId);
		for (Long studyrecordId : list) {
			Object e = null;//this.getCache().get(studyrecordId.toString());
			if (e != null) {
				CacheElement ce = (CacheElement) e;
				MeleteModuleRecordModel moduleRecord = new MeleteModuleRecordModel();
				moduleRecord.setModuleId(module.getId()); // 对应模块ID
				moduleRecord.setCourseId(module.getCourseId()); // 课程ID
				moduleRecord.setOpenStatus(new Long(CodeTable.openStatusNo));// 开启状态
				moduleRecord.setStudyrecordId(new Long(studyrecordId)); // 学习记录ID
				moduleRecord.setStudyTime(new Long(0));// 学习时长
				if (module.getRequirement() == null || module.getRequirement().trim().equals("")) {
					moduleRecord.setStatus(new Long(CodeTable.passStatusYes));// 通过状态
				} else {
					moduleRecord.setStatus(new Long(CodeTable.passStatusNo));// 通过状态
				}
				try {
					studyService.saveModuleRecord(moduleRecord);
					ce.editModuleRecord(moduleRecord);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				Long parentId = module.getParentId();
				if (parentId != null) {
					c.addModuleLeaf(parentId, false);
				}
			}
		}
	}

	/**
	 * 删除所有学生缓存中的节点model
	 * 
	 * @param module
	 */
	public void deleteModule(Long moduleId) {
		try {
			MeleteModuleModel module = (MeleteModuleModel) courseService
					.getModelById(MeleteModuleModel.class, moduleId);
			String courseId = module.getCourseId();
			CacheElement c = this.getCacheOfCourse(courseId);
			c.deleteModule(module.getId());

			List<Long> list = courseService.getStudyrecordidListByCourseId(courseId);
//			for (Long studyrecordId : list) {
//				Object e = this.getCache().get(studyrecordId.toString());
//				if (e != null) {
//					CacheElement ce = (CacheElement) e;
//					ce.deleteModuleRecord(moduleId);
//				}
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 更新所有学生缓存中的页model
	 * 
	 * @param section
	 */
	public void updateSection(MeleteSectionModel section) {
		String courseId = section.getCourseId();
		CacheElement c = this.getCacheOfCourse(courseId);
		c.updateSection(section);
	}

	/**
	 * 向所有学生缓存中添加一个页model
	 * 
	 * @param section
	 */
	public void addSection(MeleteSectionModel section) {
		String courseId = section.getCourseId();

		CacheElement c = this.getCacheOfCourse(courseId);
		c.updateSection(section);

		List<Long> list = courseService.getStudyrecordidListByCourseId(courseId);
		for (Long studyrecordId : list) {
			Object e = null;//this.getCache().get(studyrecordId.toString());
			if (e != null) {
				CacheElement ce = (CacheElement) e;
				MeleteSectionRecordModel sectionRecord = new MeleteSectionRecordModel();
				sectionRecord.setSectionId(section.getId()); // 对应节点ID
				sectionRecord.setCourseId(courseId); // 课程ID
				sectionRecord.setStartStudyTime(null); // 学习开始时间
				sectionRecord.setStudyTime(0L); // 学习时长
				sectionRecord.setStudyrecordId(new Long(studyrecordId));// 学习记录ID
				sectionRecord.setOpenStatus(new Long(CodeTable.openStatusNo));// 开启状态
				if (section.getRequirement() == null || section.getRequirement().trim().equals("")) {
					sectionRecord.setStatus(new Long(CodeTable.passStatusYes));
				} else {
					sectionRecord.setStatus(new Long(CodeTable.passStatusNo));
				}
				try {
					MeleteModuleRecordModel moduleRecord = studyService.getModuleRecordById(studyrecordId.toString(),
							section.getModuleId().toString());// 根据学生对应的学习记录和module获得模块记录
					sectionRecord.setMeleteModuleRecordId(moduleRecord.getModulerecordId());
					studyService.saveSectionRecord(sectionRecord);
					ce.editSectionRecord(sectionRecord);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Long moduleId = section.getModuleId();
				c.addSectionLeaf(moduleId, false);
			}
		}
	}

	/**
	 * 删除所有学生缓存中的某个页model
	 * 
	 * @param section
	 */
	public void deleteSection(MeleteSectionModel section) {
		try {
			String courseId = section.getCourseId();
			CacheElement c = this.getCacheOfCourse(courseId);
			c.deleteSection(section.getId());

			List<Long> list = courseService.getStudyrecordidListByCourseId(courseId);
//			for (Long studyrecordId : list) {
//				Object e = this.getCache().get(studyrecordId.toString());
//				if (e != null) {
//					CacheElement ce = (CacheElement) e;
//					ce.deleteSectionRecord(section.getId());
//				}
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 向所有学生缓存中添加一个作业model
	 * 
	 * @param test
	 *            作业model
	 * @param courseId
	 *            课程id
	 */
	public void addTest(MeleteTestModel test) {
		String courseId = courseService.getCourseIdFromModel(test);

		CacheElement c = this.getCacheOfCourse(courseId);
		c.updateTest(test);

		List<Long> list = courseService.getStudyrecordidListByCourseId(courseId);
		for (Long studyrecordId : list) {
			Object e = null;//this.getCache().get(studyrecordId.toString());
			if (e != null) {
				CacheElement ce = (CacheElement) e;
				MeleteTestRecordModel testRecord = new MeleteTestRecordModel();
				testRecord.setTestId(test.getId());
				testRecord.setCourseId(courseId);
				testRecord.setScore(0F);
				testRecord.setAttemptNumber(0L);
				testRecord.setStatus(Long.valueOf(CodeTable.passStatusNo));
				testRecord.setModuleId(test.getModuleId());
				testRecord.setSectionId(test.getSectionId());
				testRecord.setStudyrecordId(Long.valueOf(studyrecordId));
				testRecord.setTestName(test.getName());
				try {
					studyService.saveTestRecord(testRecord);
					ce.editTestRecord(testRecord);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if (test.getBelongType().equals(CodeTable.belongMudole)) {
					c.addTestLeaf(test.getModuleId(), false);
				} else {
					c.addTestLeaf(test.getSectionId(), false);
				}
			}
		}
	}

	/**
	 * 更新所有学生缓存中的作业model
	 * 
	 * @param test
	 *            作业model
	 */
	public void updateTest(MeleteTestModel test) {
		String courseId = courseService.getCourseIdFromModel(test);
		CacheElement c = this.getCacheOfCourse(courseId);
		c.updateTest(test);
	}

	/**
	 * 删除所有学生缓存中的某个作业
	 * 
	 * @param test
	 *            作业model
	 */
	public void deleteTest(MeleteTestModel test) {
		String courseId = courseService.getCourseIdFromModel(test);
		CacheElement c = this.getCacheOfCourse(courseId);
		c.deleteTest(test.getId());

		List<Long> list = courseService.getStudyrecordidListByCourseId(courseId);
//		for (Long studyrecordId : list) {
//			Object e = this.getCache().get(studyrecordId.toString());
//			if (e != null) {
//				CacheElement ce = (CacheElement) e;
//				ce.deleteTestRecord(test.getId());
//			}
//		}
	}

	/**
	 * 向所有学生缓存中添加一个讨论model
	 * 
	 * @param forum
	 *            讨论model
	 * @param courseId
	 *            课程id
	 */
	public void addForum(MeleteForumModel forum) {
		String courseId = courseService.getCourseIdFromModel(forum);

		CacheElement c = this.getCacheOfCourse(courseId);
		c.updateForum(forum);

		List<Long> list = courseService.getStudyrecordidListByCourseId(courseId);
		for (Long studyrecordId : list) {
			Object e = null;//this.getCache().get(studyrecordId.toString());
			if (e != null) {
				CacheElement ce = (CacheElement) e;
				MeleteForumRecordModel forumRecord = new MeleteForumRecordModel();
				forumRecord.setForumId(forum.getId());
				forumRecord.setStudyrecordId(Long.valueOf(studyrecordId));
				forumRecord.setCourseId(courseId);
				forumRecord.setAttemptNumber(0L);
				forumRecord.setStatus(Long.valueOf(CodeTable.passStatusNo));
				forumRecord.setModuleId(forum.getModuleId());
				forumRecord.setSectionId(forum.getSectionId());
				try {
					studyService.saveForumRecord(forumRecord);
					ce.editForumRecord(forumRecord);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if (forum.getBelongType().equals(CodeTable.belongMudole)) {
					c.addForumLeaf(forum.getModuleId(), false);
				} else {
					c.addForumLeaf(forum.getSectionId(), false);
				}
			}
		}
	}

	/**
	 * 更新所有学生缓存中的讨论model
	 * 
	 * @param forum
	 *            讨论model
	 */
	public void updateForum(MeleteForumModel forum) {
		String courseId = courseService.getCourseIdFromModel(forum);
		CacheElement c = this.getCacheOfCourse(courseId);
		c.updateForum(forum);
	}

	/**
	 * 删除所有学生缓存中的某个讨论
	 * 
	 * @param forum
	 *            讨论model
	 */
	public void deleteForum(MeleteForumModel forum) {
		String courseId = courseService.getCourseIdFromModel(forum);

		CacheElement c = this.getCacheOfCourse(courseId);
		c.deleteForum(forum.getId());

		List<Long> list = courseService.getStudyrecordidListByCourseId(courseId);
//		for (Long studyrecordId : list) {
//			Object e = this.getCache().get(studyrecordId.toString());
//			if (e != null) {
//				CacheElement ce = (CacheElement) e;
//				ce.deleteForumRecord(forum.getId());
//			}
//		}
	}

	/**
	 * 向所有学生缓存中添加一个前测model
	 * 
	 * @param selftest
	 *            前测model
	 */
	public void addSelftest(MeleteSelfTestModel selftest) {
		String courseId = courseService.getCourseIdFromModel(selftest);

		CacheElement c = this.getCacheOfCourse(courseId);
		c.updateSelftest(selftest);

		List<Long> list = courseService.getStudyrecordidListByCourseId(courseId);
		for (Long studyrecordId : list) {
			Object e = null;//this.getCache().get(studyrecordId.toString());
			if (e != null) {
				CacheElement ce = (CacheElement) e;
				MeleteSelftestRecordModel selftestRecord = new MeleteSelftestRecordModel();
				selftestRecord.setStudyrecordId(Long.valueOf(studyrecordId));
				selftestRecord.setCourseId(courseId);
				selftestRecord.setScore(0F);
				selftestRecord.setAttemptNumber(0L);
				selftestRecord.setSelftestId(selftest.getId());
				selftestRecord.setStatus(Long.valueOf(CodeTable.passStatusNo));
				selftestRecord.setModuleId(selftest.getModuleId());
				selftestRecord.setSectionId(selftest.getSectionId());
				selftestRecord.setSelftestName(selftest.getName());
				try {
					studyService.saveSelftestRecord(selftestRecord);
					ce.editSelftestRecord(selftestRecord);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				if (selftest.getBelongType().equals(CodeTable.belongMudole)) {
					c.addSelftestLeaf(selftest.getModuleId(), false);
				} else {
					c.addSelftestLeaf(selftest.getSectionId(), false);
				}
			}
		}
	}

	/**
	 * 更新所有学生缓存中的前测model
	 * 
	 * @param selftest
	 *            前测model
	 */
	public void updateSelftest(MeleteSelfTestModel selftest) {
		String courseId = courseService.getCourseIdFromModel(selftest);

		CacheElement c = this.getCacheOfCourse(courseId);
		c.updateSelftest(selftest);
	}

	/**
	 * 删除所有学生缓存中的某个前测
	 * 
	 * @param selftest
	 *            前测model
	 */
	public void deleteSelftest(MeleteSelfTestModel selftest) {
		String courseId = courseService.getCourseIdFromModel(selftest);

		CacheElement c = this.getCacheOfCourse(courseId);
		c.deleteSelftest(selftest.getId());

		List<Long> list = courseService.getStudyrecordidListByCourseId(courseId);
//		for (Long studyrecordId : list) {
//			Object e = this.getCache().get(studyrecordId.toString());
//			if (e != null) {
//				CacheElement ce = (CacheElement) e;
//				ce.deleteSelftestRecord(selftest.getId());
//			}
//		}
	}

	public void updateCourse(MeleteCourseModel course) {
		CacheElement c = this.getCacheOfCourse(course.getId());
		c.setCourse(course);
	}
}
