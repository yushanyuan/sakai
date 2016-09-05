package com.bupticet.paperadmin.common;

public class JNDINames {

	private JNDINames() {
	} // prevent instanciation

	//
	// JNDI names of EJB home objects
	//
	public static final String INFOADMIN_REMOTEHOME = "java:comp/env/ejb/remote/infoadmin";

	public static final String RIGHTSETGROUPADMIN_REMOTEHOME = "java:comp/env/ejb/remote/rightsetgroupadmin";

	public static final String USERADMIN_REMOTEHOME = "java:comp/env/ejb/remote/useradmin";

	public static final String LOGADMIN_REMOTEHOME = "java:comp/env/ejb/remote/logadmin";

	public static final String SIGNON_REMOTEHOME = "java:comp/env/ejb/remote/signon";

	// 课程知识点
	public static final String COURSEADMIN_REMOTEHOME = "java:comp/env/ejb/remote/courseadmin";

	public static final String COURSE_EJBLOCALHOME = "java:comp/env/ejb/local/course";

	public static final String COURSE_EJBREMOTEHOME = "java:comp/env/ejb/remote/course";

	public static final String KNOWLEDGE_EJBLOCALHOME = "java:comp/env/ejb/local/knowledge";

	public static final String KNOWLEDGE_EJBREMOTEHOME = "java:comp/env/ejb/remote/knowledge";

	public static final String COURSETYPE_EJBLOCALHOME = "java:comp/env/ejb/local/coursetype";

	public static final String COURSETYPE_EJBREMOTEHOME = "java:comp/env/ejb/remote/coursetype";

	public static final String OPERATEOBJECT_EJBLOCALHOME = "java:comp/env/ejb/local/operateobject";

	public static final String PERMISSIONS_EJBLOCALHOME = "java:comp/env/ejb/local/permissions";

	public static final String OBJECTOPERATION_EJBLOCALHOME = "java:comp/env/ejb/local/objectoperation";

	// 题库
	public static final String PRAXISADMIN_REMOTEHOME = "java:comp/env/ejb/remote/praxisadmin";

	public static final String PRAXIS_EJBLOCALHOME = "java:comp/env/ejb/local/praxis";

	public static final String PRAXIS_EJBREMOTEHOME = "java:comp/env/ejb/remote/praxis";

	public static final String PRAXISMATERIAL_EJBLOCALHOME = "java:comp/env/ejb/local/praxismaterial";

	public static final String PRAXISTEMPLATE_EJBLOCALHOME = "java:comp/env/ejb/local/praxistemplate";

	public static final String PRAXISTEMPLATE_EJBREMOTEHOME = "java:comp/env/ejb/remote/praxistemplate";

	public static final String PRAXISTYPE_EJBLOCALHOME = "java:comp/env/ejb/local/praxistype";

	public static final String PRAXISTYPE_EJBREMOTEHOME = "java:comp/env/ejb/remote/praxistype";

	// 卷库
	// 策略
	public static final String SCHEMA_EJBLOCALHOME = "java:comp/env/ejb/local/schema";

	public static final String SCHEMAITEM_EJBLOCALHOME = "java:comp/env/ejb/local/schemaitem";

	public static final String SCHEMAADMIN_EJBLOCALHOME = "java:comp/env/ejb/local/schemaadmin";

	public static final String SCHEMAADMIN_EJBREMOTEHOME = "java:comp/env/ejb/remote/schemaadmin";

	// 试卷
	public static final String PAPER_EJBLOCALHOME = "java:comp/env/ejb/local/paper";

	public static final String PAPER_EJBREMOTEHOME = "java:comp/env/ejb/remote/paper";

	public static final String PAPERFRAME_EJBLOCALHOME = "java:comp/env/ejb/local/paperframe";

	public static final String PAPERFRAME_EJBREMOTEHOME = "java:comp/env/ejb/remote/paperframe";

	public static final String QUESTION_EJBLOCALHOME = "java:comp/env/ejb/local/question";

	public static final String QUESTION_EJBREMOTEHOME = "java:comp/env/ejb/remote/question";

	public static final String QUESTIONMATERIAL_EJBLOCALHOME = "java:comp/env/ejb/local/questionmaterial";

	public static final String ORIGINALPRAXIS_EJBLOCALHOME = "java:comp/env/ejb/local/originalpraxis";

	public static final String PAPERADMIN_EJBREMOTEHOME = "java:comp/env/ejb/remote/paperadmin";

	// 作业库
	// 策略
	public static final String EXERCISESCHEMA_EJBLOCALHOME = "java:comp/env/ejb/local/exerciseschema";

	public static final String EXERCISESCHEMAITEM_EJBLOCALHOME = "java:comp/env/ejb/local/exerciseschemaitem";

	public static final String EXERCISESCHEMAADMIN_EJBLOCALHOME = "java:comp/env/ejb/local/exerciseschemaadmin";

	public static final String EXERCISESCHEMAADMIN_EJBREMOTEHOME = "java:comp/env/ejb/remote/exerciseschemaadmin";

	// 用户
	public static final String USERADMIN_EJBREMOTEHOME = "java:comp/env/ejb/remote/useradmin";

	public static final String USER_EJBLOCALHOME = "java:comp/env/ejb/local/user";

	public static final String USER_EJBREMOTEHOME = "java:comp/env/ejb/remote/user";

	public static final String STUDENTCLASS_EJBLOCALHOME = "java:comp/env/ejb/local/studentclass";

	public static final String STUDENTCLASS_EJBREMOTEHOME = "java:comp/env/ejb/remote/studentclass";

	public static final String ROLE_EJBLOCALHOME = "java:comp/env/ejb/local/role";

	public static final String PERMISSION_EJBLOCALHOME = "java:comp/env/ejb/local/permission";

	public static final String OBJECT_EJBLOCALHOME = "java:comp/env/ejb/local/object";

	public static final String OPERATION_EJBLOCALHOME = "java:comp/env/ejb/local/operation";

	// 考试考务

	public static final String EXAMADMIN_EJBREMOTEHOME = "java:comp/env/ejb/remote/examadmin";

	public static final String EXAMZONE_EJBLOCALHOME = "java:comp/env/ejb/local/examzone";

	public static final String EXAMPAPERLOG_EJBLOCALHOME = "java:comp/env/ejb/local/exampaperlog";

	public static final String EXAMPLAN_EJBLOCALHOME = "java:comp/env/ejb/local/examplan";

	public static final String EXAMPLAN_EJBREMOTEHOME = "java:comp/env/ejb/remote/examplan";

	public static final String EXAMROOM_EJBLOCALHOME = "java:comp/env/ejb/local/examroom";

	public static final String EXAMROOMLOG_EJBLOCALHOME = "java:comp/env/ejb/local/examroomlog";

	public static final String EXAMROOMLOG_EJBREMOTEHOME = "java:comp/env/ejb/remote/examroomlog";

	public static final String EXAMPAPERLOG_EJBREMOTEHOME = "java:comp/env/ejb/remote/exampaperlog";

	public static final String INVIGILATERLOG_EJBLOCALHOME = "java:comp/env/ejb/local/invigilaterlog";

	public static final String STUDENTEXAMLOG_EJBLOCALHOME = "java:comp/env/ejb/local/studentexamlog";

	public static final String STUDENTEXAMLOG_EJBREMOTEHOME = "java:comp/env/ejb/remote/studentexamlog";

	public static final String INPERMISSION_EJBLOCALHOME = "java:comp/env/ejb/local/inpermission";

	// ExamAdmin考试安排
	public static final String EXAMADMIN_REMOTEHOME = "java:comp/env/ejb/romote/examadmin";

	public static final String ANSWERSLOG_EJBLOCALHOME = "java:comp/env/ejb/local/answerslog";

	public static final String ANSWERSLOG_EJBREMOTEHOME = "java:comp/env/ejb/remote/answerslog";

	public static final String LOADLOG_EJBLOCALHOME = "java:comp/env/ejb/local/loadlog";

	// 批改答卷

	public static final String CHECKADMIN_REMOTEHOME = "java:comp/env/ejb/remote/checkadmin";

	public static final String ARRANGECHECK_EJBLOCALHOME = "java:comp/env/ejb/local/arrangecheck";

	public static final String TASKITEM_EJBLOCALHOME = "java:comp/env/ejb/local/taskitem";

	public static final String STUDENTRESULT_EJBLOCALHOME = "java:comp/env/ejb/local/studentresult";

	public static final String ANSWERSEQ_EJBLOCALHOME = "java:comp/env/ejb/local/answerseq";

	public static final String TASKNUMBER_EJBLOCALHOME = "java:comp/env/ejb/local/tasknumber";

	public static final String TESTSTATISTIC_EJBLOCALHOME = "java:comp/env/ejb/local/teststatistic";

	// 出作业

	public static final String EXERCISEADMIN_REMOTEHOME =

	"java:comp/env/ejb/remote/exerciseadmin";

	public static final String EXERCISE_EJBREMOTEHOME = "java:comp/env/ejb/remote/exercise";

	public static final String EXERCISE_EJBLOCALHOME = "java:comp/env/ejb/local/exercise";

	public static final String EXERCISEFRAME_EJBLOCALHOME = "java:comp/env/ejb/local/exerciseframe";

	public static final String EXERCISEFRAME_EJBREMOTEHOME = "java:comp/env/ejb/remote/exerciseframe";

	public static final String EXERCISEQUESTION_EJBLOCALHOME = "java:comp/env/ejb/local/exercisequestion";

	public static final String EXERCISEQUESTION_EJBREMOTEHOME = "java:comp/env/ejb/remote/exercisequestion";

	public static final String EXERCISEQUESTIONMATERIAL_EJBLOCALHOME = "java:comp/env/ejb/local/exercisequestionmaterial";

	public static final String EXERCISEORIGINALPRAXIS_EJBLOCALHOME = "java:comp/env/ejb/local/exerciseoriginalpraxis";

	// 做作业
	public static final String DOEXERCISEADMIN_REMOTEHOME =

	"java:comp/env/ejb/remote/doexerciseadmin";

	public static final String EXERCISERECORD_EJBLOCALHOME = "java:comp/env/ejb/local/exerciserecord";

	public static final String EXERCISERECORD_EJBREMOTEHOME = "java:comp/env/ejb/remote/exerciserecord";

	public static final String RELEASE_EJBLOCALHOME = "java:comp/env/ejb/local/release";

	public static final String RELEASE_EJBREMOTEHOME = "java:comp/env/ejb/remote/release";

	public static final String SUBMITRECORD_EJBLOCALHOME = "java:comp/env/ejb/local/submitrecord";

	public static final String SUBMITRECORD_EJBREMOTEHOME = "java:comp/env/ejb/remote/submitrecord";

	public static final String CORRECTSTATISTIC_EJBLOCALHOME = "java:comp/env/ejb/local/correctstatistic";

	// 批改作业
	public static final String CHECKEXERCISE_EJBLOCALHOME = "java:comp/env/ejb/local/checkexercise";

	public static final String CHECKSTATISTICS_EJBLOCALHOME = "java:comp/env/ejb/local/checkstatistics";

	public static final String CHECKEXERCISE_EJBREMOTEHOME = "java:comp/env/ejb/remote/checkexercise";

	public static final String CHECKEXERCISEADMIN_REMOTEHOME = "java:comp/env/ejb/remote/checkexerciseadmin";

	/*
	 * 在批改模块增加了两个本地的实体bean：ExerciseAnswerslog和BasePraxis来替代原先的远程调用ExerciseRecord
	 * 和ExerciseQuestion两个远程实体Bean
	 */
	public static final String EXERCISEANSWERSLOG_EJBLOCALHOME = "java:comp/env/ejb/local/exerciseanswerslog";

	public static final String BASEPRAXIS_EJBLOCALHOME = "java:comp/env/ejb/local/basepraxis";

	// JNDI Names of DAO.
	// GetInfoAdmin DAO
	public static final String GETINFOADMIN_DAO_CLASS = "java:comp/env/dao/getinfoadmindao";

	//
	// InfoAdmin DAO
	public static final String INFOADMIN_DAO_CLASS = "java:comp/env/dao/infoadmindao";

	// RightSetGroupAdmin DAO
	public static final String RIGHTSETGROUPADMIN_DAO_CLASS = "java:comp/env/dao/rightsetgroupadmindao";

	// LogAdmin DAO
	public static final String LOGADMIN_DAO_CLASS = "java:comp/env/dao/logadmindao";

	// UserAdmin DAO
	public static final String USERADMIN_DAO_CLASS = "java:comp/env/dao/useradmindao";

	// UserAdmin DAO
	public static final String SIGNON_DAO_CLASS = "java:comp/env/dao/signondao";

	public static final String COURSEADMIN_DAO_CLASS = "java:comp/env/dao/courseadmindao";

	// PraxisAdmin DAO
	public static final String PRAXISADMIN_DAO_CLASS = "java:comp/env/dao/praxisadmindao";

	// PaperAdmin DAO
	public static final String PAPERADMIN_DAO_CLASS = "java:comp/env/dao/paperadmindao";

	// ExamAdmin DAO
	public static final String EXAMADMIN_DAO_CLASS = "java:comp/env/dao/examadmindao";

	// Cert DAO
	public static final String CERT_DAO_CLASS = "java:comp/env/dao/certdao";

	// CheckAdmin DAO
	public static final String CHECKADMIN_DAO_CLASS = "java:comp/env/dao/checkadmindao";

	// MessageBoardAdmin DAO
	public static final String MBADMIN_DAO_CLASS = "java:comp/env/dao/mbadmindao";

	// ExerciseAdmin DAO
	public static final String EXERCISEADMIN_DAO_CLASS = "com.bupticet.exerciseadmin.dao.OracleExerciseAdminDAOImpl";

	public static final String DOEXERCISEADMIN_DAO_CLASS = "java:comp/env/dao/doexerciseadmindao";

	// CheckExerciseAdmin DAO
	public static final String CHECKEXERCISEADMIN_DAO_CLASS = "java:comp/env/dao/checkexerciseadmindao";

	//
	// JNDI Names of data sources.
	//

	public static final String SAKAI_DATASOURCE = "java:comp/env/jdbc/sakaidatasource";

	public static final String PRAXISADMIN_DATASOURCE = "java:comp/env/jdbc/praxisadmindatasource";

	public static final String SETTING_DATASOURCE = "java:comp/env/jdbc/settingdatasource";

}
