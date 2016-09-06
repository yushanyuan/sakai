package org.sakaiproject.resource.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.sakaiproject.resource.api.course.model.MeleteCourseModel;
import org.sakaiproject.resource.api.course.model.MeleteForumModel;
import org.sakaiproject.resource.api.course.model.MeleteModuleModel;
import org.sakaiproject.resource.api.course.model.MeleteSectionModel;
import org.sakaiproject.resource.api.course.model.MeleteSelfTestModel;
import org.sakaiproject.resource.api.course.model.MeleteTestModel;
import org.sakaiproject.resource.api.study.model.MeleteForumRecordModel;
import org.sakaiproject.resource.api.study.model.MeleteModuleRecordModel;
import org.sakaiproject.resource.api.study.model.MeleteSectionRecordModel;
import org.sakaiproject.resource.api.study.model.MeleteSelftestRecordModel;
import org.sakaiproject.resource.api.study.model.MeleteStudyRecordModel;
import org.sakaiproject.resource.api.study.model.MeleteTestRecordModel;

public class CacheElement implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -21815697319251027L;

	private MeleteCourseModel course;

	private Map<Long, MeleteModuleModel> modules;

	private Map<Long, MeleteSectionModel> sections;

	private Map<Long, MeleteTestModel> tests;

	private Map<Long, MeleteForumModel> forums;

	private Map<Long, MeleteSelfTestModel> selftests;

	private MeleteStudyRecordModel studyRecord;

	private Map<Long, MeleteModuleRecordModel> moduleRecords;

	private Map<Long, MeleteSectionRecordModel> sectionRecords;

	private Map<Long, MeleteTestRecordModel> testRecords;

	private Map<Long, MeleteForumRecordModel> forumRecords;

	private Map<Long, MeleteSelftestRecordModel> selfTestRecords;

	private Map<Long, Boolean> moduleLeaf;

	private Map<Long, Boolean> sectionLeaf;

	private Map<Long, Boolean> testLeaf;

	private Map<Long, Boolean> forumLeaf;

	private Map<Long, Boolean> selftestLeaf;

	/**
	 * 课程缓存
	 * 
	 * @param course
	 * @param moduleList
	 * @param sectionList
	 * @param testList
	 * @param forumList
	 * @param selfList
	 */
	public CacheElement(MeleteCourseModel course,
			List<MeleteModuleModel> moduleList,
			List<MeleteSectionModel> sectionList,
			List<MeleteTestModel> testList, List<MeleteForumModel> forumList,
			List<MeleteSelfTestModel> selfList) {
		this.course = course;
		this.modules = new HashMap<Long, MeleteModuleModel>();
		this.addModuleList(moduleList);

		this.sections = new HashMap<Long, MeleteSectionModel>();
		this.addSectionList(sectionList);

		this.tests = new HashMap<Long, MeleteTestModel>();
		this.addTestList(testList);

		this.forums = new HashMap<Long, MeleteForumModel>();
		this.addForumList(forumList);

		this.selftests = new HashMap<Long, MeleteSelfTestModel>();
		this.addSelftestList(selfList);

		this.moduleLeaf = new HashMap<Long, Boolean>();
		this.sectionLeaf = new HashMap<Long, Boolean>();
		this.testLeaf = new HashMap<Long, Boolean>();
		this.forumLeaf = new HashMap<Long, Boolean>();
		this.selftestLeaf = new HashMap<Long, Boolean>();
	}

	/**
	 * 学生学习记录缓存
	 * 
	 * @param studyRecord
	 * @param moduleRecordList
	 * @param sectionRecordList
	 * @param testRecordList
	 * @param forumRecordList
	 * @param selfRecordList
	 */
	public CacheElement(MeleteStudyRecordModel studyRecord,
			List<MeleteModuleRecordModel> moduleRecordList,
			List<MeleteSectionRecordModel> sectionRecordList,
			List<MeleteTestRecordModel> testRecordList,
			List<MeleteForumRecordModel> forumRecordList,
			List<MeleteSelftestRecordModel> selfRecordList) {
		this.studyRecord = studyRecord;

		this.moduleRecords = new HashMap<Long, MeleteModuleRecordModel>();
		for (MeleteModuleRecordModel moduleRecord : moduleRecordList) {
			Long id = moduleRecord.getModuleId();
			this.moduleRecords.put(id, moduleRecord);
		}
		this.sectionRecords = new HashMap<Long, MeleteSectionRecordModel>();
		for (MeleteSectionRecordModel sectionRecord : sectionRecordList) {
			this.sectionRecords
					.put(sectionRecord.getSectionId(), sectionRecord);
		}
		this.testRecords = new HashMap<Long, MeleteTestRecordModel>();
		for (MeleteTestRecordModel testRecord : testRecordList) {
			this.testRecords.put(testRecord.getTestId(), testRecord);
		}
		this.forumRecords = new HashMap<Long, MeleteForumRecordModel>();
		for (MeleteForumRecordModel forumRecord : forumRecordList) {
			this.forumRecords.put(forumRecord.getForumId(), forumRecord);
		}
		this.selfTestRecords = new HashMap<Long, MeleteSelftestRecordModel>();
		for (MeleteSelftestRecordModel selftestRecord : selfRecordList) {
			this.selfTestRecords.put(selftestRecord.getSelftestId(),
					selftestRecord);
		}
	}

	public MeleteStudyRecordModel getStudyRecord() {
		return studyRecord;
	}

	public void setStudyRecord(MeleteStudyRecordModel studyRecord) {
		this.studyRecord = studyRecord;
	}

	public MeleteCourseModel getCourse() {
		return course;
	}

	public List<MeleteModuleModel> getModules() {
		List<MeleteModuleModel> list =  new ArrayList<MeleteModuleModel>();
		for (MeleteModuleModel meleteModuleModel : this.modules.values()) {
			list.add(meleteModuleModel);
		}
		return list;
	}

	public List<MeleteSectionModel> getSections() {
		List<MeleteSectionModel> list =  new ArrayList<MeleteSectionModel>();
		for (MeleteSectionModel m : this.sections.values()) {
			list.add(m);
		}
		return list;
	}

	public List<MeleteTestModel> getTests() {
		List<MeleteTestModel> list =  new ArrayList<MeleteTestModel>();
		for (MeleteTestModel m : this.tests.values()) {
			list.add(m);
		}
		return list;
	}

	public List<MeleteForumModel> getForums() {
		List<MeleteForumModel> list =  new ArrayList<MeleteForumModel>();
		for (MeleteForumModel m : this.forums.values()) {
			list.add(m);
		}
		return list;
	}

	public List<MeleteSelfTestModel> getSelfTests() {
		List<MeleteSelfTestModel> list =  new ArrayList<MeleteSelfTestModel>();
		for (MeleteSelfTestModel m : this.selftests.values()) {
			list.add(m);
		}
		return list;
	}

	/**
	 * 根据父节点id获取子节点集合
	 * 
	 * @param parentId
	 *            父节点id，如果为null说明是第一级节点
	 * @return
	 */
	public List<MeleteModuleModel> getModuleListByParentid(String parentId) {
		List<MeleteModuleModel> list = new ArrayList<MeleteModuleModel>();
		Collection<MeleteModuleModel> coll = this.modules.values();
		if (parentId == null) {
			for (MeleteModuleModel model : coll) {
				if (model.getParentId() == null) {
					list.add(model);
				}
			}
		} else {
			for (MeleteModuleModel model : coll) {
				if (model.getParentId() != null
						&& model.getParentId().equals(Long.valueOf(parentId))) {
					list.add(model);
				}
			}
		}
		if (list.size() == 0) {
			return null;
		}
		ComparatorModule comp = new ComparatorModule();
		Collections.sort(list, comp);
		return list;
	}

	/**
	 * 根据节点id获取页集合
	 * 
	 * @param moduleId
	 *            节点id
	 * @return
	 */
	public List<MeleteSectionModel> getSectionListByModuleId(String moduleId) {
		if (!StringUtils.isNotBlank(moduleId)) {
			return null;
		}
		List<MeleteSectionModel> list = new ArrayList<MeleteSectionModel>();
		Collection<MeleteSectionModel> coll = this.sections.values();
		for (MeleteSectionModel model : coll) {
			if (model.getModuleId().equals(Long.valueOf(moduleId))) {
				list.add(model);
			}
		}
		if (list.size() == 0) {
			return null;
		}
		ComparatorSection comp = new ComparatorSection();
		Collections.sort(list, comp);
		return list;
	}

	/**
	 * 根据父id和父类型获取作业集合
	 * 
	 * @param parentId
	 * @param parentType
	 *            如果parentType为节点类型，parentId则为节点id；如果parentType为页类型，
	 *            parentId则为页id
	 * @return
	 */
	public List<MeleteTestModel> getTestListByParentId(String parentId,
			String parentType) {
		List<MeleteTestModel> list = new ArrayList<MeleteTestModel>();
		Collection<MeleteTestModel> coll = this.tests.values();
		if (parentType.equals(CodeTable.module)) {
			for (MeleteTestModel model : coll) {
				if (model.getModuleId() != null
						&& model.getModuleId().toString().equals(parentId)) {
					list.add(model);
				}
			}
		} else {
			for (MeleteTestModel model : coll) {
				if (model.getSectionId() != null
						&& model.getSectionId().toString().equals(parentId)) {
					list.add(model);
				}
			}
		}
		if (list.size() == 0) {
			return null;
		}
		return list;
	}

	/**
	 * 根据父id和父类型获取讨论集合
	 * 
	 * @param parentId
	 * @param parentType
	 *            如果parentType为节点类型，parentId则为节点id；如果parentType为页类型，
	 *            parentId则为页id
	 * @return
	 */
	public List<MeleteForumModel> getForumListByParentId(String parentId,
			String parentType) {
		List<MeleteForumModel> list = new ArrayList<MeleteForumModel>();
		Collection<MeleteForumModel> coll = this.forums.values();
		if (parentType.equals(CodeTable.module)) {
			for (MeleteForumModel model : coll) {
				if (model.getModuleId() != null
						&& model.getModuleId().toString().equals(parentId)) {
					list.add(model);
				}
			}
		} else {
			for (MeleteForumModel model : coll) {
				if (model.getSectionId() != null
						&& model.getSectionId().toString().equals(parentId)) {
					list.add(model);
				}
			}
		}
		if (list.size() == 0) {
			return null;
		}
		return list;
	}

	/**
	 * 根据父id和父类型获取前测集合
	 * 
	 * @param parentId
	 * @param parentType
	 *            如果parentType为节点类型，parentId则为节点id；如果parentType为页类型，
	 *            parentId则为页id
	 * @return
	 */
	public List<MeleteSelfTestModel> getSelftestListByParentId(String parentId,
			String parentType) {
		List<MeleteSelfTestModel> list = new ArrayList<MeleteSelfTestModel>();
		Collection<MeleteSelfTestModel> coll = this.selftests.values();
		if (parentType.equals(CodeTable.module)) {
			for (MeleteSelfTestModel model : coll) {
				if (model.getModuleId() != null
						&& model.getModuleId().toString().equals(parentId)) {
					list.add(model);
				}
			}
		} else {
			for (MeleteSelfTestModel model : coll) {
				if (model.getSectionId() != null
						&& model.getSectionId().toString().equals(parentId)) {
					list.add(model);
				}
			}
		}
		if (list.size() == 0) {
			return null;
		}
		return list;
	}

	/**
	 * 将一个节点集合添加到缓存中
	 * 
	 * @param moduleList
	 *            节点集合
	 */
	public void addModuleList(List<MeleteModuleModel> moduleList) {
		if (!moduleList.isEmpty()) {
			for (MeleteModuleModel model : moduleList) {
				this.modules.put(model.getId(), model);
			}
		}
	}

	/**
	 * 将一个页集合添加到缓存中
	 * 
	 * @param sectionList
	 */
	public void addSectionList(List<MeleteSectionModel> sectionList) {
		if (!sectionList.isEmpty()) {
			for (MeleteSectionModel model : sectionList) {
				this.sections.put(model.getId(), model);
			}
		}
	}

	/**
	 * 将一个作业集合添加到缓存中
	 * 
	 * @param testList
	 */
	public void addTestList(List<MeleteTestModel> testList) {
		if (!testList.isEmpty()) {
			for (MeleteTestModel model : testList) {
				this.tests.put(model.getId(), model);
			}
		}
	}

	/**
	 * 将一个讨论集合添加到缓存中
	 * 
	 * @param forumList
	 */
	public void addForumList(List<MeleteForumModel> forumList) {
		if (!forumList.isEmpty()) {
			for (MeleteForumModel model : forumList) {
				this.forums.put(model.getId(), model);
			}
		}
	}

	/**
	 * 将一个前测集合添加到缓存中
	 * 
	 * @param selftestList
	 */
	public void addSelftestList(List<MeleteSelfTestModel> selftestList) {
		if (!selftestList.isEmpty()) {
			for (MeleteSelfTestModel model : selftestList) {
				this.selftests.put(model.getId(), model);
			}
		}
	}

	/**
	 * 根据节点id获取节点对象
	 * 
	 * @param moduleId
	 * @return
	 */
	public MeleteModuleModel getModule(Long moduleId) {
		return this.modules.get(moduleId);
	}

	/**
	 * 根据页id获取页对象
	 * 
	 * @param sectionId
	 * @return
	 */
	public MeleteSectionModel getSecton(Long sectionId) {
		return this.sections.get(sectionId);
	}

	/**
	 * 根据作业id获取作业对象
	 * 
	 * @param testId
	 * @return
	 */
	public MeleteTestModel getTest(Long testId) {
		return this.tests.get(testId);
	}

	/**
	 * 根据讨论id获取讨论对象
	 * 
	 * @param forumId
	 * @return
	 */
	public MeleteForumModel getForum(Long forumId) {
		return this.forums.get(forumId);
	}

	/**
	 * 根据前测id获取前测对象
	 * 
	 * @param selftestId
	 * @return
	 */
	public MeleteSelfTestModel getSelftest(Long selftestId) {
		return this.selftests.get(selftestId);
	}

	/**
	 * 将一个节点对象添加到缓存中
	 * 
	 * @param module
	 */
	public void updateModule(MeleteModuleModel module) {
		this.modules.put(module.getId(), module);
	}

	/**
	 * 根据节点id删除缓存中的节点和节点记录
	 * 
	 * @param moduleId
	 */
	public void deleteModule(Long moduleId) {
		MeleteModuleModel delModule = this.modules.get(moduleId);
		if(delModule == null){
			return;
		}
		Long parentId = delModule.getParentId();
		this.modules.remove(moduleId);
		if (parentId != null) {// 有上级模块
			List<MeleteModuleModel> moduleList = this
					.getModuleListByParentid(parentId.toString());
			if (moduleList == null || moduleList.isEmpty()) {// 上级模块中无子模块则设置模块是否有子模块判断
				this.addModuleLeaf(parentId, true);
			}
		}
	}

	public void deleteModuleRecord(Long moduleId) {
		this.moduleRecords.remove(moduleId);
	}

	/**
	 * 将一个页对象添加到缓存中
	 * 
	 * @param section
	 */
	public void updateSection(MeleteSectionModel section) {
		this.sections.put(section.getId(), section);
	}

	/**
	 * 根据页id删除缓存中的页和页记录
	 * 
	 * @param sectionId
	 */
	public void deleteSection(Long sectionId) {
		MeleteSectionModel delSection = this.sections.get(sectionId);
		if(delSection == null){
			return;
		}
		Long moduleId = delSection.getModuleId();
		this.sections.remove(sectionId);
		List<MeleteSectionModel> mSectionList = this
				.getSectionListByModuleId(moduleId.toString());
		if (mSectionList == null || mSectionList.isEmpty()) {
			addSectionLeaf(moduleId, true);
		}
	}

	public void deleteSectionRecord(Long sectionId) {
		this.sectionRecords.remove(sectionId);
	}

	/**
	 * 将一个作业对象添加到缓存中
	 * 
	 * @param test
	 */
	public void updateTest(MeleteTestModel test) {
		this.tests.put(test.getId(), test);
	}

	/**
	 * 根据作业id删除缓存中的作业和作业记录
	 * 
	 * @param testId
	 */
	public void deleteTest(Long testId) {
		MeleteTestModel delTest = this.tests.get(testId);
		String parentType = delTest.getBelongType();
		Long parentId = new Long(0);
		if (CodeTable.belongMudole.equals(parentType)) {// 模块测试
			parentId = delTest.getModuleId();
		} else {
			parentId = delTest.getSectionId();
		}
		this.tests.remove(testId);
		List<MeleteTestModel> testList = getTestListByParentId(
				parentId.toString(), parentType);
		if (testList == null || testList.isEmpty()) {
			this.addTestLeaf(parentId, true);
		}
	}

	public void deleteTestRecord(Long testId) {
		this.testRecords.remove(testId);
	}

	/**
	 * 将一个讨论对象添加到缓存中
	 * 
	 * @param forum
	 */
	public void updateForum(MeleteForumModel forum) {
		this.forums.put(forum.getId(), forum);
	}

	/**
	 * 根据讨论id删除缓存中的讨论和讨论记录
	 * 
	 * @param testId
	 */
	public void deleteForum(Long forumId) {
		MeleteForumModel delForum = forums.get(forumId);// 获得要删除的讨论
		String parentType = delForum.getBelongType();
		Long parentId = new Long(0);
		if (CodeTable.belongMudole.equals(parentType)) {// 模块讨论
			parentId = delForum.getModuleId();
		} else {// 页讨论
			parentId = delForum.getSectionId();
		}
		this.forums.remove(forumId);
		List<MeleteForumModel> forumList = this.getForumListByParentId(
				parentId.toString(), parentType);
		if (forumList == null || forumList.isEmpty()) {// 设置父节点下的是否有讨论
			this.addForumLeaf(parentId, true);
		}
	}

	public void deleteForumRecord(Long forumId) {
		this.forumRecords.remove(forumId);
	}

	/**
	 * 将一个前测对象添加到缓存中
	 * 
	 * @param selftest
	 */
	public void updateSelftest(MeleteSelfTestModel selftest) {
		this.selftests.put(selftest.getId(), selftest);
	}

	/**
	 * 根据前测id删除缓存中的前测和前测记录
	 * 
	 * @param testId
	 */
	public void deleteSelftest(Long selftestId) {
		MeleteSelfTestModel selfTest = selftests.get(selftestId);
		Long parentId = new Long(0);
		if (CodeTable.belongMudole.equals(selfTest.getBelongType())) {
			MeleteModuleModel mod = modules.get(selfTest.getModuleId());
			parentId = mod.getId();
		} else {
			MeleteSectionModel sec = sections.get(selfTest.getSectionId());
			parentId = sec.getId();
		}
		this.selftests.remove(selftestId);
		addSelftestLeaf(parentId, true);
	}

	public void deleteSelftestRecord(Long selftestId) {
		this.selfTestRecords.remove(selftestId);
	}

	/**
	 * 根据节点id获取节点记录对象
	 * 
	 * @param moduleId
	 * @return
	 */
	public MeleteModuleRecordModel getModuleRecord(Long moduleId) {
		return this.moduleRecords.get(moduleId);
	}

	/**
	 * 根据页id获取页记录对象
	 * 
	 * @param sectionId
	 * @return
	 */
	public MeleteSectionRecordModel getSectonRecord(Long sectionId) {
		return this.sectionRecords.get(sectionId);
	}

	/**
	 * 根据作业id获取作业记录对象
	 * 
	 * @param testId
	 * @return
	 */
	public MeleteTestRecordModel getTestRecord(Long testId) {
		return this.testRecords.get(testId);
	}

	/**
	 * 根据讨论id获取讨论记录对象
	 * 
	 * @param forumId
	 * @return
	 */
	public MeleteForumRecordModel getForumRecord(Long forumId) {
		return this.forumRecords.get(forumId);
	}

	/**
	 * 根据前测id获取前测记录对象
	 * 
	 * @param selftestId
	 * @return
	 */
	public MeleteSelftestRecordModel getSelftestRecord(Long selftestId) {
		return this.selfTestRecords.get(selftestId);
	}

	/**
	 * 添加或者更新节点记录
	 * 
	 * @param moduleRecord
	 */
	public void editModuleRecord(MeleteModuleRecordModel moduleRecord) {
		Long id = moduleRecord.getModuleId();
		this.moduleRecords.put(id, moduleRecord);
	}

	/**
	 * 添加或者更新页记录
	 * 
	 * @param sectionRecord
	 */
	public void editSectionRecord(MeleteSectionRecordModel sectionRecord) {
		this.sectionRecords.put(sectionRecord.getSectionId(), sectionRecord);
	}

	/**
	 * 添加或者更新作业记录
	 * 
	 * @param testRecord
	 */
	public void editTestRecord(MeleteTestRecordModel testRecord) {
		this.testRecords.put(testRecord.getTestId(), testRecord);
	}

	/**
	 * 添加或者更新讨论记录
	 * 
	 * @param forumRecord
	 */
	public void editForumRecord(MeleteForumRecordModel forumRecord) {
		this.forumRecords.put(forumRecord.getForumId(), forumRecord);
	}

	/**
	 * 添加或者更新前测记录
	 * 
	 * @param selftestRecord
	 */
	public void editSelftestRecord(MeleteSelftestRecordModel selftestRecord) {
		this.selfTestRecords
				.put(selftestRecord.getSelftestId(), selftestRecord);
	}

	/**
	 * 根据节点id判断该节点下是否有子节点
	 * 
	 * @param moduleId
	 *            节点id
	 * @return false 有子节点；true 没有子节点
	 */
	public Boolean getModuleLeaf(Long moduleId) {
		return this.moduleLeaf.get(moduleId);
	}

	/**
	 * 根据节点id判断该节点下是否有页
	 * 
	 * @param moduleId
	 *            节点id
	 * @return false 有页；true 没有页
	 */
	public Boolean getSectionLeaf(Long moduleId) {
		return this.sectionLeaf.get(moduleId);
	}

	/**
	 * 根据节点id或者页id判断其是否有作业
	 * 
	 * @param parentId
	 *            节点id或者页id
	 * @return false 有作业；true 没有作业
	 */
	public Boolean getTestLeaf(Long parentId) {
		return this.testLeaf.get(parentId);
	}

	/**
	 * 根据节点id或者页id判断其是否有讨论
	 * 
	 * @param parentId
	 *            节点id或者页id
	 * @return false 有讨论；true 没有讨论
	 */
	public Boolean getForumLeaf(Long parentId) {
		return this.forumLeaf.get(parentId);
	}

	/**
	 * 根据节点id或者页id判断其是否有前测
	 * 
	 * @param parentId
	 *            节点id或者页id
	 * @return false 有前测；true 没有前测
	 */
	public Boolean getSelftestLeaf(Long parentId) {
		return this.selftestLeaf.get(parentId);
	}

	/**
	 * 设置某一节点下是否有子节点
	 * 
	 * @param moduleId
	 *            节点id
	 * @param leaf
	 *            false 有子节点；true 没有子节点
	 */
	public void addModuleLeaf(Long moduleId, boolean leaf) {
		this.moduleLeaf.put(moduleId, new Boolean(leaf));
	}

	/**
	 * 设置某一节点下是否有页
	 * 
	 * @param moduleId
	 *            节点id
	 * @param leaf
	 *            false 有页；true 没有页
	 */
	public void addSectionLeaf(Long moduleId, boolean leaf) {
		this.sectionLeaf.put(moduleId, new Boolean(leaf));
	}

	/**
	 * 设置某一节点或者页是否包含有作业
	 * 
	 * @param moduleId
	 *            节点id或者页id
	 * @param leaf
	 *            false 有作业；true 没有作业
	 */
	public void addTestLeaf(Long parentId, boolean leaf) {
		this.testLeaf.put(parentId, new Boolean(leaf));
	}

	/**
	 * 设置某一节点或者页是否包含有讨论
	 * 
	 * @param moduleId
	 *            节点id或者页id
	 * @param leaf
	 *            false 有讨论；true 没有讨论
	 */
	public void addForumLeaf(Long parentId, boolean leaf) {
		this.forumLeaf.put(parentId, new Boolean(leaf));
	}

	/**
	 * 设置某一节点或者页是否包含有前测
	 * 
	 * @param moduleId
	 *            节点id或者页id
	 * @param leaf
	 *            false 有前测；true 没有前测
	 */
	public void addSelftestLeaf(Long parentId, boolean leaf) {
		this.selftestLeaf.put(parentId, new Boolean(leaf));
	}

	/**
	 * 获取所有计算平时成绩的作业model及其记录model的集合
	 * 
	 * @return List<Object[]>
	 *         返回一个Object[]集合,其中Object[0]=MeleteTestModel,Object[1
	 *         ]=MeleteTestRecordModel
	 */
	public List<Object[]> getTestAndRecordList() {
		Collection<MeleteTestModel> coll = this.tests.values();// 获取所有的作业集合
		List<Object[]> returnList = new ArrayList<Object[]>();// 定义返回集合
		for (MeleteTestModel test : coll) {
			if (test.getIsCaculateScore().toString()
					.equals(CodeTable.IsCaculateScoreYes)) {// 只获取计算平时成绩的作业
				Object[] objs = new Object[2];
				objs[0] = test;
				MeleteTestRecordModel record = this.getTestRecord(test.getId());
				objs[1] = record;
				returnList.add(objs);
			}
		}
		if (returnList.isEmpty() || returnList.size() == 0) {
			return null;
		}
		return returnList;
	}

	/**
	 * 获取所有计算平时成绩的讨论model及其记录model的集合
	 * 
	 * @return List<Object[]>
	 *         返回一个Object[]集合,其中Object[0]=MeleteForumModel,Object[
	 *         1]=MeleteForumRecordModel
	 */
	public List<Object[]> getForumAndRecordList() {
		Collection<MeleteForumModel> coll = this.forums.values();// 获取所有的讨论集合
		List<Object[]> returnList = new ArrayList<Object[]>();// 定义返回集合
		for (MeleteForumModel forum : coll) {
			if (forum.getIsCaculateScore().toString()
					.equals(CodeTable.IsCaculateScoreYes)) {// 只获取计算平时成绩的讨论
				Object[] objs = new Object[2];
				objs[0] = forum;
				MeleteForumRecordModel record = this.getForumRecord(forum
						.getId());
				objs[1] = record;
				returnList.add(objs);
			}
		}
		if (returnList.isEmpty() || returnList.size() == 0) {
			return null;
		}
		return returnList;
	}

	/**
	 * 获取所有计算平时成绩的前测model及其记录model的集合
	 * 
	 * @return List<Object[]>
	 *         返回一个Object[]集合,其中Object[0]=MeleteSelfTestModel,Object
	 *         [1]=MeleteSelftestRecordModel
	 */
	public List<Object[]> getSelftestAndRecordList() {
		Collection<MeleteSelfTestModel> coll = this.selftests.values();// 获取所有的前测集合
		List<Object[]> returnList = new ArrayList<Object[]>();// 定义返回集合
		for (MeleteSelfTestModel selftest : coll) {
			if (selftest.getIsCaculateScore().toString()
					.equals(CodeTable.IsCaculateScoreYes)) {// 只获取计算平时成绩的前测
				Object[] objs = new Object[2];
				objs[0] = selftest;
				MeleteSelftestRecordModel record = this
						.getSelftestRecord(selftest.getId());
				objs[1] = record;
				returnList.add(objs);
			}
		}
		if (returnList.isEmpty() || returnList.size() == 0) {
			return null;
		}
		return returnList;
	}

	public List<MeleteModuleRecordModel> getModuleRecordList(
			List<MeleteModuleModel> moduleList) {
		if (moduleList != null && !moduleList.isEmpty()) {
			List<MeleteModuleRecordModel> moduleRecordList = new ArrayList<MeleteModuleRecordModel>();
			for (MeleteModuleModel module : moduleList) {
				MeleteModuleRecordModel moduleRecord = this
						.getModuleRecord(module.getId());
				moduleRecordList.add(moduleRecord);
			}
			return moduleRecordList;
		} else {
			return null;
		}
	}

	public List<MeleteSectionRecordModel> getSectionRecordList(
			List<MeleteSectionModel> sectionList) {
		if (sectionList != null && !sectionList.isEmpty()) {
			List<MeleteSectionRecordModel> sectionRecordList = new ArrayList<MeleteSectionRecordModel>();
			for (MeleteSectionModel section : sectionList) {
				MeleteSectionRecordModel sectionRecord = this
						.getSectonRecord(section.getId());
				sectionRecordList.add(sectionRecord);
			}
			return sectionRecordList;
		} else {
			return null;
		}
	}

	public List<MeleteTestRecordModel> getTestRecordList(
			List<MeleteTestModel> testList) {
		if (testList != null && !testList.isEmpty()) {
			List<MeleteTestRecordModel> testRecordList = new ArrayList<MeleteTestRecordModel>();
			for (MeleteTestModel test : testList) {
				MeleteTestRecordModel testRecord = this.getTestRecord(test
						.getId());
				testRecordList.add(testRecord);
			}
			return testRecordList;
		} else {
			return null;
		}
	}

	public List<MeleteForumRecordModel> getForumRecordList(
			List<MeleteForumModel> forumList) {
		if (forumList != null && !forumList.isEmpty()) {
			List<MeleteForumRecordModel> forumRecordList = new ArrayList<MeleteForumRecordModel>();
			for (MeleteForumModel forum : forumList) {
				MeleteForumRecordModel forumRecord = this.getForumRecord(forum
						.getId());
				forumRecordList.add(forumRecord);
			}
			return forumRecordList;
		} else {
			return null;
		}
	}

	public List<MeleteSelftestRecordModel> getSelfRecordList(
			List<MeleteSelfTestModel> selfList) {
		if (selfList != null && !selfList.isEmpty()) {
			List<MeleteSelftestRecordModel> selfRecordList = new ArrayList<MeleteSelftestRecordModel>();
			for (MeleteSelfTestModel self : selfList) {
				MeleteSelftestRecordModel selfRecord = this
						.getSelftestRecord(self.getId());
				selfRecordList.add(selfRecord);
			}
			return selfRecordList;
		} else {
			return null;
		}
	}

	// public List<Node> getNodeListByModuleId(String moduleId){
	// List<Node> returnList = new ArrayList<Node>();
	// if(moduleId == null){
	//
	// }else{
	//
	// }
	// return returnList;
	// }

	private static class ComparatorModule implements Comparator {
		public int compare(Object o1, Object o2) {
			MeleteModuleModel c1 = (MeleteModuleModel) o1;
			MeleteModuleModel c2 = (MeleteModuleModel) o2;

			Long idx1 = c1.getIdx();
			Long idx2 = c2.getIdx();
			if (idx1 > idx2) {
				return 1;
			} else {
				if (idx1 == idx2) {
					return 0;
				} else {
					return -1;
				}
			}
		}
	}

	private static class ComparatorSection implements Comparator {
		public int compare(Object o1, Object o2) {
			MeleteSectionModel c1 = (MeleteSectionModel) o1;
			MeleteSectionModel c2 = (MeleteSectionModel) o2;

			Long idx1 = c1.getIdx();
			Long idx2 = c2.getIdx();
			if (idx1 > idx2) {
				return 1;
			} else {
				if (idx1 == idx2) {
					return 0;
				} else {
					return -1;
				}
			}
		}
	}

	public void setCourse(MeleteCourseModel course) {
		this.course = course;
	}

}
