package com.bupticet.paperadmin.schema.model;





import java.io.Serializable;


/**


 * <p>Title: </p>


 * <p>Description: </p>


 * <p>Copyright: Copyright (c) 2002</p>


 * <p>Company: </p>


 * @author unascribed


 * @version 1.0


 */





public class SchemaContent implements Serializable{


  public PaperAttribute cTotalTime=null;//总的时间


  public PaperAttribute cFullMark=null;//总分





  public PraxisDistribute cTypeDist=null;//类型分布


  public PraxisDistribute cDiffDist=null;//难度分布


  public PraxisDistribute cKnowDist=null;//知识点分布


  public PraxisDistribute cCognDist=null;//认知程度分布





  public int nMaxUsedTimes=0;//最大使用次数


  public int nMinTimeSlot=0;//最小使用日期间隔，天数


  public int nRelativite=0;//相关系数


  public int nPraxisUse=0;//题目用途


  public int nAuditStatus=0;//题目审核情况


  public int nCourseID=0;//所属课程ID


  public int nSchemaID=0;//策略ID


  public int nTotalAccording=0;//总满意度





  public SchemaContent()


  {





   }


}