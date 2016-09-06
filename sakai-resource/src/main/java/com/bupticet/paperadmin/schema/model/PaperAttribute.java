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





public class PaperAttribute implements Serializable{


  public int nItemID=0;


  public int nAccording=0;//满意度


  public int nImport=0;//重要度


  public int nDemand=0; //需求


  public int nFact=0; //实际


  /**


  @roseuid 3DFFDDBB0232


   */


  public PaperAttribute()


  {


   }


   public PaperAttribute(int nItemID,int nAccording,int nImport,int nDemand)


   {


	 this.nItemID=nItemID;


	 this.nAccording=nAccording;


	 this.nImport=nImport;


	 this.nDemand=nDemand;


   }


}