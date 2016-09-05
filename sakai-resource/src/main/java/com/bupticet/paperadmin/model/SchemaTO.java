package com.bupticet.paperadmin.model;





import java.io.Serializable;


import java.sql.Date;





import com.bupticet.paperadmin.schema.model.SchemaContent;


/**


 * <p>Title: </p>


 * <p>Description: </p>


 * <p>Copyright: Copyright (c) 2002</p>


 * <p>Company: </p>


 * @author unascribed


 * @version 1.0


 */





public class SchemaTO  implements Serializable{


  public int nSchemaID=0;


  public String strSchemaName="";


  public String strCreator="";


  public Date cCreaDate=null;


  public Date cExpoTime=null;


  public int nCourseID=0;


  public int nUsedTimes=0;


  public SchemaContent cSchemaCont;


  public int nCreatorID=0;


  public SchemaTO()


  {


  }


}