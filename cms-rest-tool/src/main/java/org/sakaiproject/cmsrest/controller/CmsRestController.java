/**   
 *   
 * 版本信息：   
 * Copyright 思开科技    
 * 版权所有   
 */
package org.sakaiproject.cmsrest.controller;
import java.sql.Time;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.sakaiproject.cmsrest.logic.ConfigLogic;
import org.sakaiproject.coursemanagement.api.AcademicSession;
import org.sakaiproject.coursemanagement.api.CanonicalCourse;
import org.sakaiproject.coursemanagement.api.CourseManagementAdministration;
import org.sakaiproject.coursemanagement.api.CourseManagementService;
import org.sakaiproject.coursemanagement.api.CourseOffering;
import org.sakaiproject.coursemanagement.api.CourseSet;
import org.sakaiproject.coursemanagement.api.EnrollmentSet;
import org.sakaiproject.coursemanagement.api.Meeting;
import org.sakaiproject.coursemanagement.api.Section;
import org.sakaiproject.coursemanagement.api.SectionCategory;
import org.sakaiproject.tool.api.Session;
import org.sakaiproject.tool.api.SessionManager;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 * restful interface
 * @Description: TODO
 * @author: derek lee
 *
 */
@Controller
@RequestMapping("/cmsRest")
public class CmsRestController {

	@Resource(name="org.sakaiproject.coursemanagement.api.CourseManagementAdministration")
	private CourseManagementAdministration courseManagementAdministration = null;
	
	
	@Resource(name="org.sakaiproject.coursemanagement.api.CourseManagementService")
	private CourseManagementService cmService;
	 
	@Resource(name="org.sakaiproject.tool.api.SessionManager")
	private SessionManager sessionManager;
	

	@Resource
	private ConfigLogic configLogic;
	
	@InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }
	//--------------------------------------------
	/**
	 * 创建学院
	 * @param eid
	 * @param title
	 * @param description
	 * @param category
	 * @param parentCourseSetEid
	 * @return
	 */
	@RequestMapping(value = "/createCourseSet", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String createCourseSet(@RequestParam("eid") String eid, @RequestParam("title") String title
									, @RequestParam("description") String description, @RequestParam("category") String category
									, @RequestParam("parentCourseSetEid") String parentCourseSetEid) {
		CourseSet cs = null;
		eid = StringUtils.trimToNull(eid);
		parentCourseSetEid = StringUtils.trimToNull(parentCourseSetEid);
		category= StringUtils.trimToNull(category);
		if(null == eid){
			return "{\"error\":\"eid为必须项\"}";
		}
		try {
			openSession();
			cs = courseManagementAdministration.createCourseSet(eid, title, description, category, parentCourseSetEid);
			closeSession();
		} catch (Exception e) {
			return "{\"error\":\"学院创建失败\"}";
		}
		return "{\"eid\":\"" + cs.getEid() + "\", \"error\":\"\"}";
	}
	
	
	/**
	 * 创建学期
	 * @param eId
	 * @param title
	 * @param description
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@RequestMapping(value = "/createAcademicSession", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String createAcademicSession(@RequestParam("eid") String eid, @RequestParam("title") String title
			, @RequestParam("description") String description, @RequestParam("startDate") Date startDate
			, @RequestParam("endDate") Date endDate) {
		AcademicSession as = null;

		eid = StringUtils.trimToNull(eid);
		if(null == eid){
			return "{\"error\":\"eid为必须项\"}";
		}

		try {
			openSession();
			as = courseManagementAdministration.createAcademicSession(eid, title, description, startDate, endDate);
			closeSession();
		} catch (Exception e) {
			return "{\"error\":\"学期创建失败\"}";
		}
		
		return "{\"eid\":\"" + as.getEid() + "\", \"error\":\"\"}";
	}

	/**
	 * 设置当前学期失败
	 * @param academicSessionEid
	 * @return
	 */
	@RequestMapping(value = "/setCurrentAcademicSessions", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String setCurrentAcademicSessions(@RequestParam("academicSessionEid") String academicSessionEid) {
		
		academicSessionEid = StringUtils.trimToNull(academicSessionEid);

		if(null == academicSessionEid){
			return "{\"error\":\"academicSessionEid为必须项\"}";
		}
		List<String> eids =  new ArrayList<String>();
		eids.add(academicSessionEid);
		
		try {
			openSession();
			courseManagementAdministration.setCurrentAcademicSessions(eids);
			closeSession();
		} catch (Exception e) {
			return "{\"error\":\"设置当前学期失败\"}";
		}
		
		return "{\"error\":\"\"}";
	}
	
	
	/**
	 * 创建课程
	 * @param eid
	 * @param title
	 * @param description
	 * @return
	 */
	@RequestMapping(value = "/createCanonicalCourse", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String createCanonicalCourse(@RequestParam("eid") String eid, @RequestParam("title") String title
										,  @RequestParam("description") String description) {
		
		eid = StringUtils.trimToNull(eid);
		if(null == eid){
			return "{\"error\":\"eid为必须项\"}";
		}
		
		CanonicalCourse cc = null;
		try {
			openSession();
			cc = courseManagementAdministration.createCanonicalCourse(eid, title, description);
			closeSession();
		} catch (Exception e) {
			return "{\"error\":\"课程创建失败\"}";
		}
		return "{\"eid\":\"" + cc.getEid() + "\", \"error\":\"\"}";
	}
	
	/**
	 * 课程加入学院
	 * @param courseSetEid
	 * @param canonicalCourseEid
	 * @return
	 */
	@RequestMapping(value = "/addCanonicalCourseToCourseSet", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String addCanonicalCourseToCourseSet(@RequestParam("courseSetEid")  String courseSetEid, @RequestParam("canonicalCourseEid") String canonicalCourseEid) {
		courseSetEid = StringUtils.trimToNull(courseSetEid);
		canonicalCourseEid = StringUtils.trimToNull(canonicalCourseEid);
		try {
			openSession();
			courseManagementAdministration.addCanonicalCourseToCourseSet(courseSetEid, canonicalCourseEid);
			closeSession();
		} catch (Exception e) {
			return "{\"error\":\"课程加入学院失败\"}";
		}
		return "{\", \"error\":\"\"}";
	}
	/**
	 * 创建课程实例
	 * @param eid
	 * @param title
	 * @param description
	 * @param status
	 * @param academicSessionEid
	 * @param canonicalCourseEid
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@RequestMapping(value = "/createCourseOffering", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String createCourseOffering(@RequestParam("eid") String eid, @RequestParam("title") String title
			, @RequestParam("description") String description, @RequestParam("status") String status
			, @RequestParam("academicSessionEid") String academicSessionEid, @RequestParam("canonicalCourseEid") String canonicalCourseEid
			, @RequestParam("startDate") Date startDate, @RequestParam("endDate") Date endDate) {
		
		eid = StringUtils.trimToNull(eid);
		if(null == eid){
			return "{\"error\":\"eid为必须项\"}";
		}
		CourseOffering co = null;
		try {
			openSession();
			co = courseManagementAdministration.createCourseOffering(eid, title, description, status, academicSessionEid, canonicalCourseEid, startDate, endDate);
			closeSession();
		} catch (Exception e) {
			return "{\"error\":\"课程实例创建失败\"}";
		}
		return "{\"eid\":\"" + co.getEid() + "\", \"error\":\"\"}";
	}
	
	/**
	 * 课程实例加入学院
	 * @param courseSetEid
	 * @param courseOfferingEid
	 * @return
	 */
	@RequestMapping(value = "/addCourseOfferingToCourseSet", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String addCourseOfferingToCourseSet(@RequestParam("courseSetEid") String courseSetEid, @RequestParam("courseOfferingEid") String courseOfferingEid) {
		courseSetEid = StringUtils.trimToNull(courseSetEid);
		courseOfferingEid = StringUtils.trimToNull(courseOfferingEid);
		try {
			openSession();
			courseManagementAdministration.addCourseOfferingToCourseSet(courseSetEid, courseOfferingEid);
			closeSession();
		} catch (Exception e) {
			return "{\"error\":\"课程实例加入学院失败\"}";
		}
		return "{\"error\":\"\"}";
	}
	
	
	/**
	 * 创建注册集
	 * @param eid
	 * @param title
	 * @param description
	 * @param category
	 * @param defaultEnrollmentCredits
	 * @param courseOfferingEid
	 * @param officialInstructors
	 * @return
	 */
	@RequestMapping(value = "/createEnrollmentSet", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String createEnrollmentSet(@RequestParam("eid") String eid, @RequestParam("title") String title
									, @RequestParam("description") String description, @RequestParam("category") String category
									, @RequestParam("defaultEnrollmentCredits") String defaultEnrollmentCredits, @RequestParam("courseOfferingEid") String courseOfferingEid
									, @RequestParam("officialInstructors") String officialInstructors){
		
		eid = StringUtils.trimToNull(eid);
		EnrollmentSet es = null;
		try {
			Set ins = new HashSet();
			ins.add(officialInstructors);
			openSession();
			es = courseManagementAdministration.createEnrollmentSet(eid, title, description, category, defaultEnrollmentCredits, courseOfferingEid, ins);
			closeSession();
		} catch (Exception e) {
			return "{\"error\":\"注册集创建失败\"}";
		}
		return "{\"eid\":\"" + es.getEid() + "\", \"error\":\"\"}";
	} 
	
	
	/**
	 * 创建班级
	 * @param eid
	 * @param title
	 * @param description
	 * @param category
	 * @param parentSectionEid
	 * @param courseOfferingEid
	 * @param enrollmentSetEid
	 * @return
	 */
	@RequestMapping(value = "/createSection", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String createSection(@RequestParam("eid") String eid, @RequestParam("title") String title
								, @RequestParam("description") String description, @RequestParam("category") String category
								, @RequestParam("parentSectionEid") String parentSectionEid, @RequestParam("courseOfferingEid") String courseOfferingEid
								, @RequestParam("enrollmentSetEid") String enrollmentSetEid) {
		
		eid = StringUtils.trimToNull(eid);
		if(null == eid){
			return "{\"error\":\"eid为必须项\"}";
		}
		parentSectionEid = StringUtils.trimToNull(parentSectionEid);
		enrollmentSetEid = StringUtils.trimToNull(enrollmentSetEid);
		Section co = null;
		try {
			openSession();
			co = courseManagementAdministration.createSection(eid, title, description, category, parentSectionEid, courseOfferingEid, enrollmentSetEid);
			closeSession();
		} catch (Exception e) {
			return "{\"error\":\"班级创建失败\"}";
		}
		return "{\"eid\":\"" + co.getEid() + "\", \"error\":\"\"}";
	}
	
	/**
	 * 将用户加入到课程实例
	 * @param userId
	 * @param role
	 * @param courseOfferingEid
	 * @param status
	 * @return
	 */
	@RequestMapping(value = "/addOrUpdateCourseOfferingMembership", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String addOrUpdateCourseOfferingMembership(@RequestParam("userId") String userId, @RequestParam("role") String role
										,@RequestParam("courseOfferingEid") String courseOfferingEid, @RequestParam("status") String status) {
		userId = StringUtils.trimToNull(userId);
		if(null == userId){
			return "{\"error\":\"userId为必须项\"}";
		}
		try {
			openSession();
			courseManagementAdministration.addOrUpdateCourseOfferingMembership(userId, role, courseOfferingEid, status);
			closeSession();
		} catch (Exception e) {
			return "{\"error\":\"用户加入课程实例失败\"}";
		}
		return "{\"error\":\"\"}";
	}
	
	
	/**
	 * 用户加入课程
	 * @param userId
	 * @param role
	 * @param courseSetEid
	 * @param status
	 * @return
	 */
	@RequestMapping(value = "/addOrUpdateCourseSetMembership", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String addOrUpdateCourseSetMembership(@RequestParam("userId") String userId, @RequestParam("role") String role
												,@RequestParam("courseSetEid") String courseSetEid, @RequestParam("status") String status) {
		
		userId = StringUtils.trimToNull(userId);
		try {
			openSession();
			courseManagementAdministration.addOrUpdateCourseSetMembership(userId, role, courseSetEid, status);
			closeSession();
		} catch (Exception e) {
			return "{\"error\":\"用户加入课程失败\"}";
		}
		return "{\"error\":\"\"}";
	}
	
	/**
	 * 将注册信息加入到注册集中
	 * @param userId
	 * @param enrollmentSetEid
	 * @param enrollmentStatus
	 * @param credits
	 * @param gradingScheme
	 * @return
	 */
	@RequestMapping(value = "/addOrUpdateEnrollment", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String addOrUpdateEnrollment(@RequestParam("userId") String userId, @RequestParam("enrollmentSetEid")  String enrollmentSetEid
										, @RequestParam("enrollmentStatus") String enrollmentStatus, @RequestParam("credits") String credits, 
										@RequestParam("gradingScheme") String gradingScheme) {
		
		userId = StringUtils.trimToNull(userId);
		try {
			openSession();
			courseManagementAdministration.addOrUpdateEnrollment(userId, enrollmentSetEid, enrollmentStatus, credits, gradingScheme);
			closeSession();
		} catch (Exception e) {
			return "{\"error\":\"将注册信息加入到注册集失败\"}";
		}
		return "{\"error\":\"\"}";
		
	}
	
	/**
	 * 用户加入到班级
	 * @param userId
	 * @param role
	 * @param sectionEid
	 * @param status
	 * @return
	 */
	@RequestMapping(value = "/addOrUpdateSectionMembership", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String addOrUpdateSectionMembership(@RequestParam("userId") String userId, @RequestParam("role") String role
												,@RequestParam("sectionEid") String sectionEid, @RequestParam("status") String status) {
		
		userId = StringUtils.trimToNull(userId);
		try {
			openSession();
			courseManagementAdministration.addOrUpdateSectionMembership(userId, role, sectionEid, status);
			closeSession();
		} catch (Exception e) {
			return "{\"error\":\"将用户加入到班级失败\"}";
		}
		return "{\"error\":\"\"}";
		
	}

	
	//-----------------for test ----------------------------------
	
	protected static final int ACADEMIC_SESSION_YEAR;
	protected static final String[] ACADEMIC_SESSION_EIDS = new String[4];
	protected static final Date[] ACADEMIC_SESSION_START_DATES = new Date[4];
	protected static final Date[] ACADEMIC_SESSION_END_DATES = new Date[4];

	protected static final String CS = "SMPL";

	protected static final String CC1 = "SMPL101";
	protected static final String CC2 = "SMPL202";

	protected static final String CO1_PREFIX = CC1 + " ";
	protected static final String CO2_PREFIX = CC2 + " ";

	protected static final String ENROLLMENT_SET_SUFFIX = "es";

	protected static final int ENROLLMENT_SETS_PER_ACADEMIC_SESSION = 2;
	protected static final int ENROLLMENTS_PER_SET = 180;

	protected static final String[] AMPM;

	protected static final SimpleDateFormat sdf()
	{
		return new SimpleDateFormat("hh:mma");
	}
	protected static DecimalFormat df;
	static {
		GregorianCalendar startCal = new GregorianCalendar();
		GregorianCalendar endCal = new GregorianCalendar();
		ACADEMIC_SESSION_YEAR = startCal.get(Calendar.YEAR);
		
		ACADEMIC_SESSION_EIDS[0] = "Winter " + ACADEMIC_SESSION_YEAR;
		ACADEMIC_SESSION_EIDS[1] = "Spring " + ACADEMIC_SESSION_YEAR;
		ACADEMIC_SESSION_EIDS[2] = "Summer " + ACADEMIC_SESSION_YEAR;
		ACADEMIC_SESSION_EIDS[3] = "Fall " + ACADEMIC_SESSION_YEAR;

		startCal.set(ACADEMIC_SESSION_YEAR, 0, 1);
		endCal.set(ACADEMIC_SESSION_YEAR, 3, 1);
		ACADEMIC_SESSION_START_DATES[0] = startCal.getTime();
		ACADEMIC_SESSION_END_DATES[0] = endCal.getTime();

		startCal.set(ACADEMIC_SESSION_YEAR, 3, 1);
		endCal.set(ACADEMIC_SESSION_YEAR, 5, 1);
		ACADEMIC_SESSION_START_DATES[1] = startCal.getTime();
		ACADEMIC_SESSION_END_DATES[1] = endCal.getTime();

		startCal.set(ACADEMIC_SESSION_YEAR, 5, 1);
		endCal.set(ACADEMIC_SESSION_YEAR, 8, 1);
		ACADEMIC_SESSION_START_DATES[2] = startCal.getTime();
		ACADEMIC_SESSION_END_DATES[2] = endCal.getTime();

		startCal.set(ACADEMIC_SESSION_YEAR, 8, 1);
		endCal.set(ACADEMIC_SESSION_YEAR + 1, 0, 1);
		ACADEMIC_SESSION_START_DATES[3] = startCal.getTime();
		ACADEMIC_SESSION_END_DATES[3] = endCal.getTime();

		AMPM = new DateFormatSymbols().getAmPmStrings();

		df = new DecimalFormat("0000");
	}

	protected int studentMemberCount;

	@RequestMapping(value = "/loadData", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String loadData(){
		openSession();
		CourseManagementAdministration cmAdmin = courseManagementAdministration;
		
			// Don't do anything if we've got data already.  The existence of an
			// AcademicSession for the first legacy term will be our indicator for existing
			// data.
			List<AcademicSession> existingAcademicSessions = cmService.getAcademicSessions();
 
			// Academic Sessions
			List<AcademicSession> academicSessions = new ArrayList<AcademicSession>();
			for(int i = 0; i < ACADEMIC_SESSION_EIDS.length; i++) {
				String academicSessionEid = ACADEMIC_SESSION_EIDS[i];
				academicSessions.add(cmAdmin.createAcademicSession(academicSessionEid,academicSessionEid,
						academicSessionEid, ACADEMIC_SESSION_START_DATES[i], ACADEMIC_SESSION_END_DATES[i]));
			}
			
			// Current Academic Sessions
			// 4 sample academic sessions have been created. Make the middle 2 "current".
			cmAdmin.setCurrentAcademicSessions(Arrays.asList(new String[] {ACADEMIC_SESSION_EIDS[1], ACADEMIC_SESSION_EIDS[2]}));

			// Course Sets
			cmAdmin.createCourseSet(CS, "Sample Department",
					"We study wet things in the Sample Dept", "DEPT", null);
			cmAdmin.addOrUpdateCourseSetMembership("da1","DeptAdmin", CS, "active");

			// Cross-listed Canonical Courses
			Set<CanonicalCourse> cc = new HashSet<CanonicalCourse>();
			cc.add(cmAdmin.createCanonicalCourse(CC1, "Sample 101", "A survey of samples"));
			cc.add(cmAdmin.createCanonicalCourse(CC2, "Sample 202", "An in depth study of samples"));
			cmAdmin.setEquivalentCanonicalCourses(cc);

			// Keep an ordered list of COs for use in building enrollment sets & adding enrollments
			List<CourseOffering> courseOfferingsList = new ArrayList<CourseOffering>();

			for(Iterator<AcademicSession> iter = academicSessions.iterator(); iter.hasNext();) {
				AcademicSession as = iter.next();
				CourseOffering co1 = cmAdmin.createCourseOffering(CO1_PREFIX + as.getEid(),
						CC1, "Sample course offering #1, " + as.getEid(), "open", as.getEid(),
						CC1, as.getStartDate(), as.getEndDate());
				CourseOffering co2 = cmAdmin.createCourseOffering(CO2_PREFIX + as.getEid(),
						CC2, "Sample course offering #2, " + as.getEid(), "open", as.getEid(),
						CC2, as.getStartDate(), as.getEndDate());

				courseOfferingsList.add(co1);
				courseOfferingsList.add(co2);

				Set<CourseOffering> courseOfferingSet = new HashSet<CourseOffering>();
				courseOfferingSet.add(co1);
				courseOfferingSet.add(co2);

				// Cross list these course offerings
				cmAdmin.setEquivalentCourseOfferings(courseOfferingSet);

				cmAdmin.addCourseOfferingToCourseSet(CS, co1.getEid());
				cmAdmin.addCourseOfferingToCourseSet(CS, co2.getEid());

				// And add some other instructors at the offering level (this should help with testing cross listings)
				cmAdmin.addOrUpdateCourseOfferingMembership("instructor1","I", co1.getEid(), null);
				cmAdmin.addOrUpdateCourseOfferingMembership("instructor2","I", co2.getEid(), null);
			}

			Map<String, String> enrollmentStatuses = cmService.getEnrollmentStatusDescriptions(Locale.US);
			Map<String, String> gradingSchemes = cmService.getGradingSchemeDescriptions(Locale.US);

			List<String> enrollmentEntries = new ArrayList<String>(enrollmentStatuses.keySet());
			List<String> gradingEntries = new ArrayList<String>(gradingSchemes.keySet());
			int enrollmentIndex = 0;
			int gradingIndex = 0;

			// Enrollment sets and sections
			Set<String> instructors = new HashSet<String>();
			instructors.add("admin");
			instructors.add("instructor");


			int enrollmentOffset = 1;
			for(Iterator<CourseOffering> iter = courseOfferingsList.iterator(); iter.hasNext();) {
				if(enrollmentOffset > (ENROLLMENT_SETS_PER_ACADEMIC_SESSION * ENROLLMENTS_PER_SET )) {
					enrollmentOffset = 1;
				}

				CourseOffering co = iter.next();
				EnrollmentSet es = cmAdmin.createEnrollmentSet(co.getEid() + ENROLLMENT_SET_SUFFIX,
						co.getTitle() + " Enrollment Set", co.getDescription() + " Enrollment Set",
						"lecture", "3", co.getEid(), instructors);

				// Enrollments
				for(int enrollmentCounter = enrollmentOffset; enrollmentCounter < (enrollmentOffset + ENROLLMENTS_PER_SET ); enrollmentCounter++) {
					if(++gradingIndex == gradingEntries.size()) {
						gradingIndex = 0;
					}
					String gradingScheme = gradingEntries.get(gradingIndex);

					if(++enrollmentIndex == enrollmentEntries.size()) {
						enrollmentIndex = 0;
					}
					String enrollmentStatus = enrollmentEntries.get(enrollmentIndex);

					cmAdmin.addOrUpdateEnrollment("student" + df.format(enrollmentCounter), es.getEid(), enrollmentStatus, "3", gradingScheme);
				}
				enrollmentOffset += ENROLLMENTS_PER_SET;
			}

			// Don't load the sections in a loop, since we need to define specific data for each
			// Section Categories (these are returned in alpha order, so we can control the order here)
			SectionCategory lectureCategory = cmAdmin.addSectionCategory("01.lct", "Lecture");
			SectionCategory discussionCategory = cmAdmin.addSectionCategory("03.dsc", "Discussion");
			cmAdmin.addSectionCategory("02.lab", "Lab");
			cmAdmin.addSectionCategory("04.rec", "Recitation");
			cmAdmin.addSectionCategory("05.sto", "Studio");

			for(Iterator<AcademicSession> iter = cmService.getAcademicSessions().iterator(); iter.hasNext();) {
				AcademicSession as = iter.next();

				// Clear the student count for this academic session
				resetStudentMemberCount();

				// Lecture Sections
				String co1Eid = CO1_PREFIX + as.getEid();
				String lec1Eid = co1Eid;
				Section lec1 = cmAdmin.createSection(lec1Eid, lec1Eid, lec1Eid + " Lecture",
					lectureCategory.getCategoryCode(), null, co1Eid, co1Eid + ENROLLMENT_SET_SUFFIX);
				Set<Meeting> lec1Meetings = new HashSet<Meeting>();
				Meeting mtg1 = cmAdmin.newSectionMeeting(lec1.getEid(), "A Building 11", getTime("10:30" + AMPM[0]), getTime("11:00" + AMPM[0]), null);
				mtg1.setMonday(true);
				mtg1.setWednesday(true);
				mtg1.setFriday(true);
				lec1Meetings.add(mtg1);
				lec1.setMeetings(lec1Meetings);
				cmAdmin.updateSection(lec1);

				String co2Eid = CO2_PREFIX + as.getEid();
				String lec2Eid = co2Eid;
				Section lec2 = cmAdmin.createSection(lec2Eid, lec2Eid, lec2Eid + " Lecture",
					lectureCategory.getCategoryCode(), null, co2Eid, co2Eid + ENROLLMENT_SET_SUFFIX);
				Set<Meeting> lec2Meetings = new HashSet<Meeting>();
				Meeting mtg2 = cmAdmin.newSectionMeeting(lec2.getEid(), "A Building 11", getTime("10:30" + AMPM[0]), getTime("11:00" + AMPM[0]), null);
				mtg2.setMonday(true);
				mtg2.setWednesday(true);
				mtg2.setFriday(true);
				lec2Meetings.add(mtg2);
				lec2.setMeetings(lec2Meetings);
				cmAdmin.updateSection(lec2);

				// Discussion sections, first Course Offering

				loadDiscussionSection("Discussion 1 " + CC1, as.getEid(), co1Eid,
						discussionCategory.getCategoryCode(), null, null, null,
						new boolean[]{false, false, false, false, false, false, false}, studentMemberCount, incrementStudentCount());

				loadDiscussionSection("Discussion 2 " + CC1, as.getEid(), co1Eid,
						discussionCategory.getCategoryCode(), "B Building 202",
						getTime("10:00" + AMPM[0]), getTime("11:30" + AMPM[0]),
						new boolean[]{false, true, false, true, false, false, false}, studentMemberCount, incrementStudentCount());

				loadDiscussionSection("Discussion 3 " + CC1, as.getEid(), co1Eid,
						discussionCategory.getCategoryCode(), "B Hall 11",
						getTime("9:00" + AMPM[0]), getTime("10:30" + AMPM[0]),
						new boolean[]{false, true, false, true, false, false, false}, studentMemberCount, incrementStudentCount());

				loadDiscussionSection("Discussion 4 " + CC1, as.getEid(), co1Eid,
						discussionCategory.getCategoryCode(), "C Building 100",
						getTime("1:30" + AMPM[1]), getTime("3:00" + AMPM[1]),
						new boolean[]{false, true, false, true, false, false, false}, studentMemberCount, incrementStudentCount());

				loadDiscussionSection("Discussion 5 " + CC1, as.getEid(), co1Eid,
						discussionCategory.getCategoryCode(), "Building 10",
						getTime("9:00" + AMPM[0]), getTime("10:00" + AMPM[0]),
						new boolean[]{true, false, true, false, true, false, false}, studentMemberCount, incrementStudentCount());

				loadDiscussionSection("Discussion 6 " + CC1, as.getEid(), co1Eid,
						discussionCategory.getCategoryCode(), "Hall 200",
						getTime("4:00" + AMPM[1]), getTime("5:00" + AMPM[1]),
						new boolean[]{true, false, true, false, true, false, false}, studentMemberCount, incrementStudentCount());

				// Discussion sections, second Course Offering

				loadDiscussionSection("Discussion 1 " + CC2, as.getEid(), co2Eid,
						discussionCategory.getCategoryCode(), null, null, null,
						new boolean[]{false, false, false, false, false, false, false}, studentMemberCount, incrementStudentCount());

				loadDiscussionSection("Discussion 2 " + CC2, as.getEid(), co2Eid,
						discussionCategory.getCategoryCode(), "2 Building A",
						getTime("11:30" + AMPM[0]), getTime("1:00" + AMPM[1]),
						new boolean[]{false, true, false, true, false, false, false}, studentMemberCount, incrementStudentCount());

				loadDiscussionSection("Discussion 3 " + CC2, as.getEid(), co2Eid,
						discussionCategory.getCategoryCode(), "101 Hall A",
						getTime("10:00" + AMPM[0]), getTime("11:00" + AMPM[0]),
						new boolean[]{true, false, true, false, true, false, false}, studentMemberCount, incrementStudentCount());

				loadDiscussionSection("Discussion 4 " + CC2, as.getEid(), co2Eid,
						discussionCategory.getCategoryCode(), "202 Building",
						getTime("8:00" + AMPM[0]), getTime("9:00" + AMPM[0]),
						new boolean[]{true, false, true, false, true, false, false}, studentMemberCount, incrementStudentCount());

				loadDiscussionSection("Discussion 5 " + CC2, as.getEid(), co2Eid,
						discussionCategory.getCategoryCode(), "11 Hall B",
						getTime("2:00" + AMPM[1]), getTime("3:30" + AMPM[1]),
						new boolean[]{false, true, false, true, false, false, false}, studentMemberCount, incrementStudentCount());

				loadDiscussionSection("Discussion 6 " + CC2, as.getEid(), co2Eid,
						discussionCategory.getCategoryCode(), "100 Building C",
						getTime("3:00" + AMPM[1]), getTime("4:00" + AMPM[1]),
						new boolean[]{true, false, true, false, true, false, false}, studentMemberCount, incrementStudentCount());
			}
			closeSession();
			return "";
		}

	protected Time getTime(String timeString) {
		Date date = null;
		try {
			date = sdf().parse(timeString);
		} catch (ParseException pe) {
			date = new Date();
		}
		return new Time(date.getTime());
	}

	protected void loadDiscussionSection(String secEidPrefix, String asEid, String coEid, String categoryCode,
			String location, Time startTime, Time endTime, boolean[] days, int studentStart, int studentEnd) {

		CourseManagementAdministration cmAdmin = courseManagementAdministration;
		String secEid = secEidPrefix + " " + asEid;
		Section sec = cmAdmin.createSection(secEid, secEidPrefix, secEid,
				categoryCode, null, coEid, null);
		for(int studentCounter = studentStart; studentCounter < studentEnd ; studentCounter++) {
			String zeroPaddedId = df.format(studentCounter);
			cmAdmin.addOrUpdateSectionMembership("student" + zeroPaddedId, "S", secEid, "member");
		}
		cmAdmin.addOrUpdateSectionMembership("instructor", "I", secEid, "section_leader");
		cmAdmin.addOrUpdateSectionMembership("admin", "I", secEid, "section_leader");

		//SAK-25394 add ta's for testing purposes
		int sectionNum = Integer.parseInt(secEidPrefix.substring("Discussion ".length(),"Discussion ".length()+1));
		switch (sectionNum) {
			case 1: cmAdmin.addOrUpdateSectionMembership("ta1", "GSI", secEid, "section_leader"); break;
			case 2: cmAdmin.addOrUpdateSectionMembership("ta2", "GSI", secEid, "section_leader"); break;
			case 3: cmAdmin.addOrUpdateSectionMembership("ta3", "GSI", secEid, "section_leader"); break;
			case 4: cmAdmin.addOrUpdateSectionMembership("ta", "GSI", secEid, "section_leader");
					cmAdmin.addOrUpdateSectionMembership("ta1", "GSI", secEid, "section_leader");
					break;
			case 5: cmAdmin.addOrUpdateSectionMembership("ta", "GSI", secEid, "section_leader");
					cmAdmin.addOrUpdateSectionMembership("ta2", "GSI", secEid, "section_leader");
					break;
			default: cmAdmin.addOrUpdateSectionMembership("ta", "GSI", secEid, "section_leader"); break;
		}

		Set<Meeting> meetings = new HashSet<Meeting>();
		Meeting mtg = cmAdmin.newSectionMeeting(secEid, location, startTime, endTime, null);
		mtg.setMonday(days[0]);
		mtg.setTuesday(days[1]);
		mtg.setWednesday(days[2]);
		mtg.setThursday(days[3]);
		mtg.setFriday(days[4]);
		mtg.setSaturday(days[5]);
		mtg.setSunday(days[6]);
		meetings.add(mtg);
		sec.setMeetings(meetings);
		cmAdmin.updateSection(sec);

	}

	protected int incrementStudentCount() {
		studentMemberCount += 30;
		return studentMemberCount;
	}
	
	protected void resetStudentMemberCount() {
		studentMemberCount = 1;
	}
	//------------------------------------------------------
	private void openSession(){
		Session session = sessionManager.getCurrentSession();
		session.setUserId(configLogic.getAccount());
	}
	
	private void closeSession(){
		sessionManager.getCurrentSession().invalidate();
	}
 }
