package com.bupticet.paperadmin.common;

public class Constant {

	private Constant() {
	}

	// 题库统计方式状态

	// 0：自上次题库统计后题库内容已经改变，所有原来的统计数据失效

	public static final int STATISTIC_INVALIDATE = 0;

	// 1:题库已按题目类型方式统计且数据仍有效

	public static final int STATISTIC_TYPE = 1;

	// 2、题库已按题目难度方式统计且数据仍有效

	public static final int STATISTIC_DIFFICULTY = 2;

	// 4、题库已按题目用途方式统计且数据仍有效

	public static final int STATISTIC_USE = 4;

	// 8、题库已按知识点方式统计且数据仍有效

	public static final int STATISTIC_KNOWLEDGE = 8;

	// 16、题库已按认知分类方式统计且数据仍有效

	public static final int STATISTIC_COGNTYPE = 16;

	// 题目用途

	// 1:考试

	public static final int PRAXIS_USE_EXAM = 1;

	// 2、作业

	public static final int PRAXIS_USE_HOMEWORK = 2;

	// 4、自测

	public static final int PRAXIS_USE_SELFTEST = 4;

	// 8、题目未开放，禁止使用

	public static final int PRAXIS_USE_EXCLUDE = 8;

	// 题目审核标识

	// 1:未审核

	public static final int PRAXIS_AUDIT_WAIT = 1;

	// 2、已审核但未通过

	public static final int PRAXIS_AUDIT_FAILED = 2;

	// 4、已通过审核

	public static final int PRAXIS_AUDIT_PASS = 4;

	// 试卷生成方式

	// 1:自动生成

	public static final int PAPER_GEN_AUTO = 1;

	// 2、手动生成

	public static final int PAPER_GEN_MANUAL = 2;

	// 4、整张录入

	public static final int PAPER_GEN_INPUT = 4;

	// 组卷策略

	// 策略类别

	// 1:试卷属性

	public static final int SCHEMATYPE_PAPER_ATTRI = 1;

	// 2、题目过滤条件

	public static final int SCHEMATYPE_PRAXIS_FILTER = 2;

	// 4、按题目类型分布

	public static final int SCHEMATYPE_DISTRI_TYPE = 4;

	// 8、按知识点分布

	public static final int SCHEMATYPE_DISTRI_KNOWLEDGE = 8;

	// 16、按题目难度分布

	public static final int SCHEMATYPE_DISTRI_DIFFICULTY = 16;

	// 32、按题目认知分类分布

	public static final int SCHEMATYPE_DISTRI_COGNTYPE = 32;

	// 策略条目键

	// 考试时间

	public static final String SCHEMAKEY_TOTAL_TIME = "TOTAL_TIME";

	// 试卷总分

	public static final String SCHEMAKEY_FULL_MARK = "FULL_MARK";

	// 候选题目允许的最大使用次数

	public static final String SCHEMAKEY_MAX_USED_TIMES = "MAX_TIMES";

	// 候选题目允许的曝光时间离出卷时的最小时间间隔

	public static final String SCHEMAKEY_MIN_TIME_SLOT = "MIN_SLOT";

	// 题目相关性

	public static final String SCHEMAKEY_PRAXIS_RELATIVITY = "PRAX_RELAT";

	// 题目用途

	public static final String SCHEMAKEY_PRAXIS_USE = "PRAX_USE";

	// 题目审核标识

	public static final String SCHEMAKEY_PRAXIS_AUDIT = "PRAX_AUDIT";

	// 策略条目值类型

	// 无

	public static final int SCHEMA_VALUETYPE_NONE = -1;

	// 分值

	public static final int SCHEMA_VALUETYPE_MARK = 0;

	// 题目数

	public static final int SCHEMA_VALUETYPE_AMOUNT = 1;

	// 系统登陆验证

	public static final String FORM_SIGNON_URL = "/servlet/AuthSignIn";

	public static final String SIGNED_ON = "globalSession";

	public static final String ORIGINAL_URL = "s_signon_original_url";

	public static final String SIGNON_ERROR_PAGE = "/login_failed.jsp";

	public static final String SIGNON_PAGE = "/tested/login.jsp";

	public static final String ADMITURL1 = "/servlet/GetMaterialServlet";

	public static final String ADMITURL2 = "/praxisadmin/upfile.jsp";

	public static final String ADMITURL3 = "/servlet/GetPhoto";

}