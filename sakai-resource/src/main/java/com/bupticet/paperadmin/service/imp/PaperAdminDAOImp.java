package com.bupticet.paperadmin.service.imp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.Writer;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import javax.sql.DataSource;

import oracle.jdbc.rowset.OracleCachedRowSet;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

import com.bupticet.paperadmin.common.Constant;
import com.bupticet.paperadmin.common.DAOSysException;
import com.bupticet.paperadmin.common.DividedByZeroException;
import com.bupticet.paperadmin.common.ItemInfo;
import com.bupticet.paperadmin.common.JNDINames;
import com.bupticet.paperadmin.common.MaterialTO;
import com.bupticet.paperadmin.common.OracleForBinary;
import com.bupticet.paperadmin.common.Page;
import com.bupticet.paperadmin.common.QuestionTO;
import com.bupticet.paperadmin.common.ServiceLocator;
import com.bupticet.paperadmin.common.XmlParse;
import com.bupticet.paperadmin.common.ZipFile;
import com.bupticet.paperadmin.model.FrameTO;
import com.bupticet.paperadmin.model.FrameXmlTO;
import com.bupticet.paperadmin.model.PaperListTO;
import com.bupticet.paperadmin.model.SchemaListTO;
import com.bupticet.paperadmin.schema.model.SchemaContent;
import com.bupticet.paperadmin.service.PaperAdminDAO;

public class PaperAdminDAOImp implements PaperAdminDAO{
	 public static final String GET_PATH_SQL="SELECT SE_SE_VALUE FROM setting"


	      +" WHERE SE_SE_NAME=?";


	  //==========getcursename SQL command=========


	  public static final String GET_COURSEID_SQL="SELECT PS_BELO_COURSE FROM"


	      +" paperschema WHERE PS_PS_ID=?";





	  public static final String GET_FRAME_SQL=


	  "SELECT PF_PF_ID,PF_FRAM_ORDER,PT_PT_NAME,"


	      +"PF_PRAX_TYPE,PF_PF_COMMENT FROM paperframe,praxistype"


	      +" WHERE PF_BELO_PAPER=? AND praxistype.PT_PT_ID=paperframe.PF_PRAX_TYPE ORDER BY PF_FRAM_ORDER";





	  public static final String GET_DEAL_FLAG_SQL="SELECT PT_DEAL_FLAG FROM"


	      +" praxistype WHERE PT_PT_ID=?";





	  public static final String GET_TYPE_NAME_SQL="SELECT PT_PT_NAME FROM"


	      +" praxistype WHERE PT_PT_ID=?";





	  public static final String GET_QUESTION_SQL="SELECT TQ_TQ_ID,TQ_BELO_FRAME,"


	      +"TQ_QUES_ORDER,TQ_TQ_BODY,TQ_TQ_ANSWER,TQ_TQ_SNAPSHOT FROM testquestion WHERE TQ_BELO_FRAME=? ORDER BY TQ_QUES_ORDER";





	  public static final String GET_NAME_SQL="SELECT PAP_PAP_NAME FROM paper WHERE PAP_PAP_ID=?";





	  //========updatePraxisBody SQL command=========


	  public static final String GET_SCHEMA_SQL=


	  "SELECT ps_ps_id,ps_ps_name,ps_ps_creator,PS_CREA_DATE FROM paperschema "+


	  "WHERE ps_belo_course=? ";


	  //


	  public static final String GET_SCHEMAITEM_SQL=


	  "SELECT PSI_SCHE_KEY,PSI_KEY_VALUE FROM PAPERSCHEMAITEM "+


	  "WHERE PSI_BELO_SCHEMA=? AND PSI_SCHE_TYPE=?";


	  public static final String GET_PAPER_LIST_SQL=


	  "SELECT PAP_PAP_ID,PAP_PAP_NAME,PAP_PAP_CREATOR,PAP_CREA_DATE,PAP_CREA_TYPE,PAP_ZIP_TIMES FROM paper "+


	  "WHERE 1>0 ";


//	========updatePraxisBody SQL command=========


	  public static final String UPDATE_QUESTIONBODY_SQL=


	  "UPDATE TESTQUESTION SET TQ_TQ_BODY=EMPTY_CLOB() WHERE TQ_TQ_ID=?";


	  public static final String UPDATE_QUESTIONANSWER_SQL=


	  "UPDATE TESTQUESTION SET TQ_TQ_ANSWER=EMPTY_CLOB() WHERE TQ_TQ_ID=?";


//	========getPraxisBody SQL command=========


	  public static final String GET_QUESTIONBODY_UPDATE_SQL=


	  "SELECT TQ_TQ_BODY FROM TESTQUESTION WHERE TQ_TQ_ID=? FOR UPDATE";


	  public static final String GET_QUESTIONANSWER_UPDATE_SQL=


	  "SELECT TQ_TQ_ANSWER FROM TESTQUESTION WHERE TQ_TQ_ID=? FOR UPDATE";


	  public static final String GET_QUESTIONBODY_SQL=


	  "SELECT TQ_TQ_BODY FROM TESTQUESTION WHERE TQ_TQ_ID=?";


	  public static final String GET_QUESTIONANSWER_SQL=


	  "SELECT TQ_TQ_ANSWER FROM TESTQUESTION WHERE TQ_TQ_ID=?";





	  //=========getMaterial()==========writen by merven


	  public static final String GET_MATERIAL_SQL=


	  "SELECT questionmaterial.QM_QM_ID as nMaterialID,questionmaterial.QM_BELO_QUESTION as nCourseID,"+


	  "questionmaterial.QM_QM_FORMAT as strFormat,questionmaterial.QM_QM_BODY as cMaterialData,questionmaterial.QM_FILE_SUFFIX as strFileSuffix  "+


	  "FROM heuser.questionmaterial WHERE questionmaterial.QM_QM_ID=?";


	  //========addMaterial SQL command=========


	  public static final String ADD_MATERIAL_SQL=


	  "UPDATE heuser.questionmaterial SET QM_BELO_QUESTION=?,QM_QM_FORMAT=?,QM_FILE_SUFFIX=?"+


	  "WHERE QM_QM_ID=?";





	  //========getFrameList SQL command=========


	  public static final String GET_FRAME_LIST_FORFRAME_SQL=


	  "SELECT paperframe.PF_PF_ID as nFrameID,paperframe.PF_BELO_PAPER as nPaperID,paperframe.PF_FRAM_ORDER as nOrder,"+


	  "praxistype.PT_PT_NAME as strPraxisTypeName,paperframe.PF_PF_COMMENT as strComment "+


	  "FROM heuser.paperframe,heuser.praxistype WHERE PF_BELO_PAPER=? AND paperframe.PF_PRAX_TYPE=praxistype.PT_PT_ID ORDER BY PF_FRAM_ORDER";


	  //========getFrameList SQL command=========


	  public static final String GET_FRAME_LIST_FORSUM_SQL=


	  "SELECT count(testquestion.TQ_TQ_ID) as nQuesSum,sum(testquestion.TQ_TQ_SCORE) as fScoreSum "+


	  "FROM heuser.testquestion where TQ_BELO_FRAME=?";





	  //========getQuestionList SQL command=========


	  public static final String GET_QUESTION_LIST_SQL=


	  "SELECT TQ_TQ_ID as nQuestionID,TQ_QUES_ORDER as nOrder,"+


	  "TQ_TQ_DIFFICULTY as fDifficulty,TQ_TQ_SCORE as fScore,TQ_SUGG_TIME as nSuggTime,"+


	  "TQ_TQ_SNAPSHOT as strSnapShot "+


	  "FROM heuser.testquestion WHERE TQ_BELO_FRAME=? ORDER BY TQ_QUES_ORDER";


	  //GET_QUESTION_LIST=================================writen by merven


	  //========getQuestionList SQL command=========





	  public static final String GET_KNOWLEDGE_NAME_SQL=


	  "SELECT KN_KN_ID as nID,KN_KN_NAME as strName FROM heuser.KNOWLEDGE"+


	  " WHERE KN_KN_ID IN ";

	  //LJW添加
	  public static final String GET_PRAXIATYPE_NAME_SQL=
	      "SELECT PT_PT_ID as nID,PT_PT_NAME as strName FROM heuser.PRAXISTYPE"+


	  " WHERE PT_PT_ID IN ";


	  //GET SCORE OF A BIG PRAXIS


	  public static final String GET_SCORE_SQL=


	  "SELECT SUM(TQ_TQ_SCORE) as score FROM testquestion WHERE TQ_BELO_FRAME=? ";


	  //向试卷表中加入密钥


	  public static final String UPDATE_PAPERPASSWD_SQL=


	  "UPDATE PAPER SET PAP_PAP_PASSWD=? WHERE PAP_PAP_ID=?";

//	由于使用ejb无法向数据库中插入和更新试题评分标准，所以使用JDBC来操作
	  public static final String UPDATE_APPROACH_SQL=
	  "UPDATE TESTQUESTION SET TQ_GRAD_APPROACH=? WHERE TQ_TQ_ID=?";



	  //9.15


	  public static final String INSERT_PAPERSTATICS_SQL="insert into paperstatics(ps_id,ps_sch_id,ps_stat) values(?,?,empty_blob())";





	  //


	  public static final String GET_PAPERSTATICS_UPDATE_SQL="SELECT PS_STAT FROM PAPERSTATICS WHERE PS_ID=? and PS_SCH_ID=? FOR UPDATE";





	  public static final String GET_PAPERSTATICS_SQL="select PS_STAT from PAPERSTATICS WHERE PS_ID=?";








	  public   static DataSource getDataSource() throws DAOSysException {
	    //be useful for testing dao only
	    /*
	    String url = "t3://202.204.15.122:88";
	    String user = null;
	    String password = null;
	    Properties properties = null;
	    try {
	      properties = new Properties();
	      properties.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
	      properties.put(Context.PROVIDER_URL, url);
	      if (user != null) {
	            properties.put(Context.SECURITY_PRINCIPAL, user);
	            properties.put(Context.SECURITY_CREDENTIALS, password == null ? "" : password);
	      }
	       InitialContext ic=new InitialContext(properties);
	       return (DataSource)ic.lookup("HEDataSource");
	    }
	    catch(Exception e) {
	      e.printStackTrace();
	                   throw new DAOSysException(e.getMessage());
	    }
	  */
	    try {


	      return (DataSource) ServiceLocator.getInstance().getDataSource(JNDINames.PRAXISADMIN_DATASOURCE);


	    } catch (Exception sle) {


	      throw new DAOSysException("NamingException while looking up DB context : "


	                                + sle.getMessage(),"未找到数据源");


	    }


	  }





	  public List getItemInfos(String sql)
	      throws DAOSysException{
	    List items=null;
	    Connection conn=null;
	    Statement st=null;
	    ResultSet rs=null;
	    try {
	      //======Make a connection to the specific database======
	      //Create a connection object
	      conn=getDataSource().getConnection();
	      //Create a Statement object
	      st=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
	                              ResultSet.CONCUR_READ_ONLY);
	      //Execute some SQL statement
	      //Get a ResultSet object
	      rs=st.executeQuery(sql);
	      //obtain the data that you want to get
	      items=new ArrayList();
	      int nSum=0;
	      while(rs.next()){
	        items.add(new ItemInfo(rs.getInt("pp_pp_id"),
	                               rs.getInt("pk_know_id"),
	                               rs.getString("pp_pp_difficulty"),
	                               rs.getInt("pp_pp_type"),
	                               rs.getInt("pp_sugg_time"),
	                               rs.getFloat("pp_sugg_score"),
	                               rs.getString("pp_pp_cognizetype"),
	                               rs.getInt("pp_rela_modulus")));
	        nSum++;
	      }
//		 System.out.println("选取的题目数：　"+nSum);
	    }catch(SQLException se){
	      throw new DAOSysException("error occuring:"+se.getMessage(),"数据库操作失败：查找题目信息时出错！");
	    }finally{
	      try{
	        if(rs!=null)rs.close();
	        if(st!=null)st.close();
	        if(conn!=null)conn.close();
	      }catch(SQLException se){
	        throw new DAOSysException("erroe occuring:"+se.getMessage(),"数据库关闭失败！");
	      }
	    }
	    return items;
	  };
	  /*
	  ljw添加，根据过滤条件筛选出题目，用于组卷，这个方法与上一个过滤方法的不同，就是增加了相关系数的处理。
	            */

	      public List getItemInfosLjw(String sql,int nRelativite)
	          throws DAOSysException{
	        Connection conn=null;
	        Statement st=null;
	        ResultSet rs=null;
	        //ResultSet rs1=null;
	        String sql2="";
	        String sql1="";
	        int nSum=0;
	        List items=new ArrayList();
	        List items1=new ArrayList();
	        try {
//	System.out.println("nRelativite:::"+nRelativite);
	          //======Make a connection to the specific database======
	          //Create a connection object
	          conn=getDataSource().getConnection();
	          //Create a Statement object
	          st=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
	                                  ResultSet.CONCUR_READ_ONLY);
	          if(nRelativite==0){
	            sql1=sql;
	            //Execute some SQL statement
	            //Get a ResultSet object
	            rs=st.executeQuery(sql1);
	            //obtain the data that you want to get
	            while(rs.next()){
	              items.add(new ItemInfo(rs.getInt("pp_pp_id"),
	                                     rs.getInt("pk_know_id"),
	                                     rs.getString("pp_pp_difficulty"),
	                                     rs.getInt("pp_pp_type"),
	                                     rs.getInt("pp_sugg_time"),
	                                     rs.getFloat("pp_sugg_score"),
	                                     rs.getString("pp_pp_cognizetype"),
	                                     rs.getInt("pp_rela_modulus")));
	              //  nSum++;
	            }
	          }else{
	            sql1=sql+" AND pp_rela_modulus=0";
	            sql2=sql+" AND pp_rela_modulus>0 ORDER BY pp_rela_modulus";
	            //Execute some SQL statement
	            //Get a ResultSet object
	            // System.out.println("sql1:::::"+sql1);
	            rs=st.executeQuery(sql1);

	            //obtain the data that you want to get
	            while(rs.next()){
	              items.add(new ItemInfo(rs.getInt("pp_pp_id"),
	                                     rs.getInt("pk_know_id"),
	                                     rs.getString("pp_pp_difficulty"),
	                                     rs.getInt("pp_pp_type"),
	                                     rs.getInt("pp_sugg_time"),
	                                     rs.getFloat("pp_sugg_score"),
	                                     rs.getString("pp_pp_cognizetype"),
	                                     rs.getInt("pp_rela_modulus")));
	              // nSum++;
	            }
	            //  System.out.println("sql2:::::"+sql2);
	            rs=st.executeQuery(sql2);
	            //  System.out.println("333333333333");
	            //obtain the data that you want to get
	            HashSet hs=new HashSet();

	            OracleCachedRowSet rowset = new OracleCachedRowSet ();
	            rowset.populate(rs);
	            //   System.out.println("4444444444444444");
	            try{
	              if(rs!=null)rs.close();
	              if(st!=null)st.close();
	              if(conn!=null)conn.close();
	              rs=null;
	              st=null;
	              conn=null;
	            }catch(SQLException se){
	              throw new DAOSysException("erroe occuring:"+se.getMessage(),"数据库关闭失败！");
	            }
	            while(rowset.next()){
	              hs.add(new Integer(rowset.getInt("pp_rela_modulus")));
	            }
	            //  System.out.println("hs::::"+hs);
	            rowset.beforeFirst();
	            Iterator it=hs.iterator();
	            int i=0;
	            while(it.hasNext()){
	              int b=((Integer)it.next()).intValue();
	              //     System.out.println("5555555555"+b);

	              while(rowset.next()){
	                //  System.out.println("66666666666666"+rowset.getInt("pp_rela_modulus"));

	                if(b==rowset.getInt("pp_rela_modulus")){
	                  items1.add(new ItemInfo(rowset.getInt("pp_pp_id"),
	                      rowset.getInt("pk_know_id"),
	                      rowset.getString("pp_pp_difficulty"),
	                      rowset.getInt("pp_pp_type"),
	                      rowset.getInt("pp_sugg_time"),
	                      rowset.getFloat("pp_sugg_score"),
	                      rowset.getString("pp_pp_cognizetype"),
	                      rowset.getInt("pp_rela_modulus")));

	                }

	                i++;
	              }
	              //  System.out.println("items1::::"+items1);
	              //   System.out.println("KKKKKKKKKKK::::"+i);

	              Random ad=new Random();
	              int a=ad.nextInt(items1.size());
	              //   System.out.println("aaaaaaaaaaaaaaa::::"+a);
	              items.add(items1.get(a));
	              items1.clear();

	              rowset.beforeFirst();
	            }
	          }
//		 System.out.println("选取的题目数：　"+nSum);
	        }catch(SQLException se){
	          se.printStackTrace();
	          throw new DAOSysException("error occuring:"+se.getMessage(),"数据库操作失败：查找题目信息时出错！");
	        }finally{
	          try{
	            if(rs!=null)rs.close();
	            if(st!=null)st.close();
	            if(conn!=null)conn.close();
	          }catch(SQLException se){
	            se.printStackTrace();
	            throw new DAOSysException("erroe occuring:"+se.getMessage(),"数据库关闭失败！");
	          }
	        }
	        return items;
	      }

	      public SchemaContent getPaperStatics(int paperID)


	      throws ClassNotFoundException, IOException{


	        Connection conn=null;


	        PreparedStatement ps=null;


	        ResultSet rs=null;


	        SchemaContent sc=null;


	        try{


	          conn=getDataSource().getConnection();


	          ps= conn.prepareStatement(GET_PAPERSTATICS_SQL,


	                                    ResultSet.TYPE_SCROLL_INSENSITIVE,


	                                    ResultSet.CONCUR_UPDATABLE);


	          ps.setInt(1,paperID);


	          rs=ps.executeQuery();


	          if(rs.next()) {


	            ObjectInputStream in = new ObjectInputStream(rs.getBlob(1).getBinaryStream());


	            sc = (SchemaContent)in.readObject();


	          }


	        }catch(SQLException se){


	          throw new DAOSysException("SQL Exception occurring: "


	                                    +se.getMessage(),"数据库操作失败：获取试卷信息时出错！");


	        }


	        finally{


	          try{


	            if(ps!=null) ps.close();


	            if(conn!=null) conn.close();


	          }catch(SQLException se){


	            throw new DAOSysException("Exception occurring during close().."


	                                      +se.getMessage(),"数据库关闭失败！");


	          }


	        }


	        return  sc;


	      }





	      public ArrayList getAllSchema(int nCourseID)


	      throws DAOSysException{


	        ArrayList schemaList = new ArrayList();


	        Connection conn=null;


	        PreparedStatement ps=null;


	        ResultSet rs=null;


	        String str=GET_SCHEMA_SQL+" ORDER BY ps_ps_id";


	        try{


//	=========make connetion to the database===========


	          conn=getDataSource().getConnection();


	          ps=conn.prepareStatement(str,


	                                   ResultSet.TYPE_SCROLL_INSENSITIVE,


	                                   ResultSet.CONCUR_READ_ONLY);


	          ps.setInt(1,nCourseID);


	          rs=ps.executeQuery();


	          while(rs.next()){


	            schemaList.add(new SchemaListTO(rs.getInt(1),


	                rs.getString(2),


	                rs.getString(3),


	                rs.getDate(4)));


	          }


	        }catch(SQLException se){


	          throw new DAOSysException("error:"+se.getMessage(),"数据库操作失败：查找试卷列表时出错！");


	        }finally{


	          try{


	            if(rs!=null)rs.close();


	            if(ps!=null)ps.close();


	            if(conn!=null)conn.close();


	          }catch(SQLException se){


	            throw new DAOSysException("error:"+se.getMessage(),"数据库关闭失败！");


	          }


	        }


	        return schemaList;


	      }





	      public void storePaperStatics(int paperID,int schemaID,SchemaContent schemaContent){


	        PreparedStatement ps=null;


	        ResultSet rs=null;


	        Connection conn=null;


	        try{


	          conn=getDataSource().getConnection();


	          // conn.setAutoCommit(false);


	          ps = conn.prepareStatement(INSERT_PAPERSTATICS_SQL);


	          ps.setInt(1,paperID);


	          ps.setInt(2,schemaID);


	          ps.executeUpdate();


	          ps.clearParameters();


	          ps=conn.prepareStatement(GET_PAPERSTATICS_UPDATE_SQL);


	          ps.setInt(1,paperID);


	          ps.setInt(2,schemaID);


	          rs=ps.executeQuery();


	          if(rs.next()){


	            java.sql.Blob javabb=rs.getBlob(1);


	            OutputStream os = ((oracle.sql.BLOB)javabb).getBinaryOutputStream();


	            ObjectOutputStream output = new ObjectOutputStream(os);


	            output.writeObject(schemaContent);


	            output.flush();


	            output.close();


	            os.close();


	          }


	          // conn.commit();





	        }


	        catch(Exception e){


	          e.printStackTrace();


	          throw new DAOSysException(e.getMessage(),"数据库操作失败：设置试题内容出错！");





	        }


	        finally{


	          try{


	            if(rs!=null) rs.close();


	            if(ps!=null) ps.close();


	            if(conn!=null)conn.close();


	          }catch(SQLException se){


	            throw new DAOSysException(se.getMessage(),"数据库关闭失败！");


	          }


	        }


	      }





	      public Page getPaperList(int nCount,int nStart,String strSearchQuery)


	      throws DAOSysException{


	        Page PaperList=null;


	        Connection conn=null;


	        PreparedStatement ps=null;


	        ResultSet rs=null;


	        int reSum=0;


	        int pageSum=0;


	        int currentPageNO=0;


	        String str="";


	        if(strSearchQuery==null)


	          str=GET_PAPER_LIST_SQL+" ORDER BY PAP_PAP_ID";


	        else


	          str=GET_PAPER_LIST_SQL+strSearchQuery+" ORDER BY PAP_PAP_ID";


	        try{


	          //==========make connection to the database===========


	          conn=getDataSource().getConnection();


	          ps=conn.prepareStatement(str,


	                                   ResultSet.TYPE_SCROLL_INSENSITIVE,


	                                   ResultSet.CONCUR_READ_ONLY);


	          rs=ps.executeQuery();


	          if(rs.last()){


	            reSum=rs.getRow();


	          }


	          double avPage=0;


	          double av=0;


	          if(nCount!=0){


	            avPage=(double)reSum/(double)nCount;


	            av=(double)nStart/(double)nCount;


	          }


	          else{


	            throw new  DividedByZeroException("error occuring may be the number divided by zero");


	          }


	          int avPageInt=(int)(avPage);


	          int avInt=(int)(av);


	          //get pagesum


	          if(avPage==0){


	            pageSum=0;


	          }else if((avPage-avPageInt)>0){


	            pageSum=avPageInt+1;


	          }else{


	            pageSum=avPageInt;


	          }


	          //Get currentPageNO


	          currentPageNO=avInt+1;


	          if(nStart>=0&&rs.absolute(nStart+1)){


	            boolean hasNext=false;


	            List items=new ArrayList();


	            do{





	              items.add(new PaperListTO(rs.getDate(4),


	                                        rs.getInt(1),


	                                        rs.getString(2),


	                                        rs.getString(3),


	                                        rs.getInt(5),


	                                        rs.getInt(6)));


	            }while((hasNext=rs.next())&&(--nCount>0));


	            PaperList=new Page(pageSum,nStart,currentPageNO,hasNext,items);


	          }


	          else{


	            PaperList=Page.EMPTY_PAGE;


	          }





	        }catch(SQLException se){


	          throw new DAOSysException("error:"+se.getMessage(),"数据库操作失败：查找试题列表时出错！");


	        }finally{


	          try{


	            if(rs!=null)rs.close();


	            if(ps!=null)ps.close();


	            if(conn!=null)conn.close();


	          }catch(SQLException se){


	            throw new DAOSysException("error:"+se.getMessage(),"数据库关闭失败！");


	          }


	        }


	        return PaperList;


	      };








	      public Page getSchemaList(int nCount,int nStart,int nCourseID,String strSearchQuery)


	      throws DAOSysException{


	        Page SchemaList=null;


	        Connection conn=null;


	        PreparedStatement ps=null;


	        ResultSet rs=null;


	        int reSum=0;


	        int pageSum=0;


	        int currentPageNO=0;


	        String str="";


	        if(strSearchQuery==null)


	          str=GET_SCHEMA_SQL+" ORDER BY ps_ps_id";


	        else


	          str=GET_SCHEMA_SQL+strSearchQuery+" ORDER BY ps_ps_id";


	        try{


//	=========make connetion to the database===========


	          conn=getDataSource().getConnection();





	          ps=conn.prepareStatement(str,


	                                   ResultSet.TYPE_SCROLL_INSENSITIVE,


	                                   ResultSet.CONCUR_READ_ONLY);


	          ps.setInt(1,nCourseID);


	          rs=ps.executeQuery();


	          if(rs.last()){


	            reSum=rs.getRow();


	          }


	          double avPage=0;


	          double av=0;


	          if(nCount!=0){


	            avPage=(double)reSum/(double)nCount;


	            av=(double)nStart/(double)nCount;


	          }else{


	            throw new DividedByZeroException("error occuring may be the number divided by zero");


	          }


	          int avPageInt=(int)(avPage);


	          int avInt=(int)(av);





	          if(avPage==0){


	            pageSum=0;


	          }else if((avPage-avPageInt)>0){


	            pageSum=avPageInt+1;


	          }else{


	            pageSum=avPageInt;


	          }


	          //Get currentPageNO


	          currentPageNO=avInt+1;


	          if(nStart>=0&&rs.absolute(nStart+1)){


	            boolean hasNext=false;


	            List items=new ArrayList();


	            do{


	              items.add(new SchemaListTO(rs.getInt(1),


	                  rs.getString(2),


	                  rs.getString(3),


	                  rs.getDate(4)));


	            }while((hasNext=rs.next())&&(--nCount>0));


	            SchemaList=new Page(pageSum,nStart,currentPageNO,hasNext,items);


	          }else{


	            SchemaList=Page.EMPTY_PAGE;


	          }


	        }catch(SQLException se){


	          throw new DAOSysException("error:"+se.getMessage(),"数据库操作失败：查找试卷列表时出错！");


	        }finally{


	          try{


	            if(rs!=null)rs.close();


	            if(ps!=null)ps.close();


	            if(conn!=null)conn.close();


	          }catch(SQLException se){


	            throw new DAOSysException("error:"+se.getMessage(),"数据库关闭失败！");


	          }


	        }


	        return SchemaList;


	      };


	      //清除指定目录下的所有文件，清除不成功返回false


	      private boolean cleanDirFile(String directory)


	      {


	        boolean flag = true;


	        String fileName = "";





	        File folderPack=new File(directory);


	        File[] packFiles = folderPack.listFiles();


	        for(int i=0;i<packFiles.length;i++)


	        {


	          fileName=packFiles[i].getName();





	          //删除directory下考生个人目录


	          File dirName = new File(directory + fileName);





	          if(dirName.isDirectory())


	          {


	            File[] subPackFiles = dirName.listFiles();


	            for(int j=0;j<subPackFiles.length;j++)


	            {


	              subPackFiles[j].delete();    //删除目录下的所有文件


	            }


	            if(!dirName.delete())  //删除目录


	            {


	              flag = false;


	            }





	            continue;


	          }





	          if(!packFiles[i].delete())


	            flag = false;


	        }





	        return flag;


	      }


	      public void writePaperToXML(int nPaperID,String strPassad)throws DAOSysException{


	        byte[] SDBIkey=null;


	        Connection conn=null;


	        PreparedStatement ps=null;


	        ResultSet rs=null;


	        Document d1=null;


	        Document d2=null;


	        List items=null;








//	  get the path that the paper and answer xml files saved in


	        XmlParse xp=new XmlParse();





	        String filePaperdir=null;


	        String filePapername=null;


	        String filePaperpath=null;


	        //


	        String fileAnswerdir=null;


	        String fileAnswername=null;


	        String fileAnswerpath=null;


	        String zipFilePath=null;


	        String secretFilePath=null;





	        // String strPaperName=getName(nPaperID);


	        String strPaperName=""+nPaperID;


	        // get the dir of filePaper saved in








	        filePaperdir=getPath("paperdir")+strPaperName+"\\";


	        zipFilePath=getPath("paperdir")+strPaperName+".zip";


	        // secretFilePath=getPath("paperdir")+strPaperName+".tmp";





	        // create the directory


	        File dir=new File(filePaperdir);


	        if(dir.exists()){


	          cleanDirFile(filePaperdir);


	        }else{


	          dir.mkdirs();


	        }


	        try{


	          PrintWriter writeFile= new PrintWriter(new FileWriter(filePaperdir+"discription.txt",true));


	          writeFile.println("");


	          writeFile.close();


	        }catch(Exception e){


	          e.printStackTrace();
	          throw new RuntimeException(e);

	        }


	        //define the filePaper name


	        filePapername="paper.xml";


	        filePaperpath=filePaperdir+filePapername;





	        //get the dir of fileAnswer saved in


	        fileAnswerdir=getPath("answerdir")+strPaperName+"\\";


	        // create the directory


	        File dir1=new File(fileAnswerdir);


	        if(!dir1.exists()){


	          dir1.mkdirs();


	        }





	        //define the filePaper name


	        fileAnswername="answer.xml";


	        fileAnswerpath=fileAnswerdir+fileAnswername;





//	=========get connection to datasource==========


	        try{


	          //Create a connection object


	          conn=getDataSource().getConnection();


	          ps=conn.prepareStatement(GET_FRAME_SQL,


	                                   ResultSet.TYPE_SCROLL_INSENSITIVE,


	                                   ResultSet.CONCUR_READ_ONLY);


	          ps.setInt(1,nPaperID);


	          // Execute some SQL statement


	          //Get a ResultSet object


	          rs=ps.executeQuery();


	          items=new ArrayList();





	          while(rs.next()){


	            //to use the function fo getDealflag to get the praxis's type


	            String strDealflag=getDealflag(rs.getInt("PF_PRAX_TYPE"));


	            String strComment=rs.getString("PF_PF_COMMENT");


	            if(strComment==null)strComment="";


	            //construct a new object items that maded of the FrameXmlTo object


	            items.add(new FrameXmlTO(rs.getInt("PF_PF_ID"),


	                                     rs.getInt("PF_FRAM_ORDER"),


	                                     rs.getString("PT_PT_NAME")+" "+strComment,


	                                     strDealflag));





	          }


	        }catch(SQLException se){

	se.printStackTrace();
	          throw new DAOSysException("error:"+se.getMessage(),"数据库操作失败：添加试卷时出错！");


	        }finally{


	          try{


	            if(rs!=null)rs.close();


	            if(ps!=null)ps.close();


	          }catch(SQLException se){


	            throw new DAOSysException("error:"+se.getMessage(),"数据库关闭失败！");


	          }


	        }


	        StringBuffer temp=new StringBuffer(200);


	        //to write the paperfile's head to temp at first


	        temp.append("<?xml version=\"1.0\" encoding=\"gb2312\" standalone"+


	                    "=\"no\"?><试卷><试卷组成>");





	        StringBuffer temp1=new StringBuffer(200);


	        //to write the answerfile's head to temp1 at first


	        temp1.append("<?xml version=\"1.0\" encoding=\"gb2312\" standalone"+


	                     "=\"no\"?><考卷答案>");





	        //to append the detail of the Frame in paper from items one bye one


	        Iterator it=items.iterator();


	        for(int i=0;i<items.size();i++){


	          FrameXmlTO fxt=(FrameXmlTO)it.next();


	          //the order of frame and the headline and the dealflag append to the rignt place


	          int tempscore;


	          tempscore=getscore(fxt.nFrameID);


	          // temp.append("<大题 序号=\""+fxt.nOrder+"\" 标题=\""+fxt.strComment+"\" 模板=\""


	          //       +fxt.strDealflag+"\""+" 分值=\""+tempscore +"\">");


	          temp.append("<大题 序号=\""+fxt.nOrder+"\" 标题=\""+fxt.strComment+"\" 模板=\""


	                      +fxt.strDealflag+"\""+" 分值=\""+tempscore +"\">");


	          try{


	            ps=conn.prepareStatement(GET_QUESTION_SQL,


	                                     ResultSet.TYPE_SCROLL_INSENSITIVE,


	                                     ResultSet.CONCUR_READ_ONLY);


	            //    items2=new ArrayList();


	            Clob lob=null;


	            String strBody=null;


	            //to find all the question of the Frame depended by nFramID


	            ps.setInt(1,fxt.nFrameID);


	            rs=ps.executeQuery();


	            while(rs.next()){


	              //to use the tempstr to contain the quesitonbody ,use temstr1 to contain


	              //the questionanswer


	              StringBuffer tempstr=new StringBuffer(200);


	              StringBuffer tempstr1=new StringBuffer(200);


	              //to get the questionbody


	              lob=rs.getClob("TQ_TQ_BODY");





	              if(lob!=null){


	                strBody=lob.getSubString(1,(int)lob.length());


	                lob=null;


	              }


	              //append the strBody to tempstr


	              tempstr.append(processMaterial(strBody,filePaperdir));





	              //append the order to tempstr


	              if(strBody==""){


	                //        System.out.println("sjp");


	                tempstr.append("        空");


	              }


	              tempstr.insert(8,rs.getInt("TQ_QUES_ORDER"));


	              //append the tempstr to temp


	              temp.append(tempstr);





	              //append the answerbody to tempstr1


	              tempstr1.append(processMaterial(rs.getString("TQ_TQ_ANSWER"),fileAnswerdir));


	              //judge the answerbody if the char at 8 is"<" then do the right operation


	              if(rs.getString("TQ_TQ_ANSWER")==null){


	                tempstr1.append("        空");


	              }





	              if(tempstr1.charAt(8)=='<'){


	                tempstr1.insert(8,fxt.nOrder+"_"+rs.getInt("TQ_QUES_ORDER"));


	              }else{


	                //  System.out.println(tempstr1.toString());


	                String[] tempstrg1=splitAnswer(tempstr1.toString(),"</答案>");


	                tempstr1.delete(0,tempstr1.length());





	                for(int ig=0;ig<tempstrg1.length;ig++){





	                  StringBuffer tempstrgb1=new StringBuffer(200);


//	                  tempstrg1[ig]="<答案>"+tempstrg1[ig];


	                  tempstrgb1.append(tempstrg1[ig]);


	                  tempstrgb1.insert(8,fxt.nOrder+"_"+rs.getInt("TQ_QUES_ORDER")+"_");


	                  tempstr1.append(tempstrgb1);


	                }


	              }


	              //append tempstr1 to temp1


	              temp1.append(tempstr1);


	            }





	          }catch(SQLException se){

	se.printStackTrace();
	            throw new DAOSysException(se.getMessage(),"数据库操作失败：添加试题答案时出错！");


	          }


	          finally{


	            try{


	              if(rs!=null)rs.close();


	              if(ps!=null)ps.close();


	              if(conn!=null&&i==(items.size()-1))conn.close();


	            }catch(SQLException se){


	              throw new DAOSysException("error:"+se.getMessage(),"数据库关闭失败！");


	            }


	          }


	          //append the end of Frame label


	          temp.append("</大题>");





	        }


	        //append the end of paperfile stringbuffer


	        temp.append("</试卷组成></试卷>");


	        //append the end of answerfile stringbuffer


	        temp1.append("</考卷答案>");


	        //parse the string of paperfile and answerfile


	        StringReader sr=null;


	        InputSource iSrc=null;


	        Document doc=null;


	        StringReader sr1=null;


	        InputSource iSrc1=null;


	        Document doc1=null;


	        String pa=null;


	        String an=null;


	        StringBuffer temp3=new StringBuffer(200);


	        StringBuffer temp4=new StringBuffer(200);


	        try{


	          sr = new StringReader(temp.toString());


	          sr1 = new StringReader(temp1.toString());





	          iSrc = new InputSource(sr);


	          iSrc1 = new InputSource(sr1);


	          DOMParser parser = new DOMParser();


	          DOMParser parser1 = new DOMParser();


	          //parse the paperfile string


	          parser.parse(iSrc);


	          //create a Document of paperfile


	          doc = parser.getDocument();


	          //parse the answerfile string


	          parser1.parse(iSrc1);


	          //create a Document of answerfile


	          doc1 = parser1.getDocument();





	        }


	        catch (Exception e)

	        {

	e.printStackTrace();
	          System.err.println("Sorry, an error occurred: " + e);
	          throw new RuntimeException(e);

	        }


	        //to use function of the Class of XmlParse to write Document to the path that


	        //the file saved in


	        xp.writeXml(filePaperpath,doc);


	        // System.out.println(filePaperpath);


	        xp.writeXml(fileAnswerpath,doc1);


	        //将paper和answer分别打包


	        try{





	          ZipFile.makeZip(filePaperdir);


	          //ZipFile.makeZip(fileAnswerdir);


//	对压缩文件进行对称加密




	/*
	          String strFileName="paper.tmp";


	          String sourceFile=zipFilePath;


	          String destFile=filePaperdir+strFileName;


	          String passwd=strPassad;


	          //System.out.println("asasasa"+passwd);


	          encrypt(sourceFile,destFile,passwd);


	          //向试卷中插入密钥


	          conn=getDataSource().getConnection();


	          ps=conn.prepareStatement(UPDATE_PAPERPASSWD_SQL);


	          ps.setString(1,passwd);


	          ps.setInt(2,nPaperID);


	          ps.executeUpdate();


	          ps.clearParameters();

	*/
	        }catch(IOException ioe){
	ioe.printStackTrace();

	          throw new DAOSysException("将paper和answer分别打包时出错"+ioe.getMessage(),"试卷和答案打包时出错");


	          }catch (Exception e)


	          {


	            System.err.println("向试卷中插入密钥时出错" + e.getMessage());
	            throw new RuntimeException(e);

	          }finally{


	            try{


	              if(ps!=null)ps.close();


	              if(conn!=null)conn.close();


	            }catch(SQLException se){


	              throw new DAOSysException("erroe occuring:"+se.getMessage());


	            }


	          }


	      }


	      // 把文件读取为字节流


	      private byte[] getByteFile(String filename) throws Exception


	      {


	        FileInputStream fis2 = new FileInputStream(filename);


	        int num2 = fis2.available();


	        byte[] indata = new byte[num2];


	        fis2.read(indata);


	        fis2.close();


	        return indata;


	      }


	      //根据输入的密码字符串，输出密码字节流


	      private byte[] getPWD(String passwd,int length)throws java.lang.Exception


	      {


	       /* SoftLib softlib = new SoftLib();


	        byte[] SDBIkey = softlib.GenerateKey(passwd,length);





	        return SDBIkey;*/
	    	  return null;


	      }


	      //把字节流写为文件


	      static final int BUFFER = 1024;


	      private void putByteFile(byte[] byteFile,String filename)


	      {





	        try


	        {


	          FileOutputStream fos = new FileOutputStream(filename);


	          BufferedOutputStream dest = new BufferedOutputStream(fos,BUFFER);


	          dest.write(byteFile);


	          fos.close();


	          dest.flush();


	          dest.close();


	          }catch(Exception e)


	          {


	            System.err.println(e.getMessage());
	            throw new RuntimeException(e);

	          }


	      }


	      //加密文件.用passwd加密sourceFile文件，输出destFile文件


	      private void encrypt(String sourceFile,String destFile,String passwd) throws java.lang.Exception


	      {


	      /*  byte[] SDBIkey = getPWD(passwd,16);





	        //加密源文件为一个临时文件


	        byte[] FileBuf = getByteFile(sourceFile);





	        byte[] addp = SoftLib.AddDataPad(FileBuf);


	        byte[] en = SoftLib.SDBI_Encrypt(SDBIkey,addp);


	        putByteFile(en,destFile);*/


	      }





	      //jiayueying write


	      private int getscore (int nPraxframe){


	        int score=0;


	        Connection conn=null;


	        PreparedStatement ps=null;


	        ResultSet rs=null;


	        try{


	          //========get connection to the database=========


	          conn=getDataSource().getConnection();





	          //create a prepareStatement


	          ps=conn.prepareStatement(GET_SCORE_SQL,


	                                   ResultSet.TYPE_SCROLL_INSENSITIVE,


	                                   ResultSet.CONCUR_READ_ONLY);


	          ps.setInt(1,nPraxframe);


	          //execute some sql statement to get a resultset


	          rs=ps.executeQuery();


	          //obtain the data you want to access


	          //


	          //   System.out.println(score);


	          if(rs.next())


	          {


	            score=rs.getInt("score");


	          }


	        }catch(SQLException se){


	          throw new DAOSysException("errortest:"+se.getMessage(),"数据库操作失败：查找成绩时出错！");


	        }


	        finally{


	          try{


	            if(rs!=null) rs.close();


	            if(ps!=null)ps.close();


	            if(conn!=null)conn.close();


	          }catch(SQLException se){


	            throw new DAOSysException("Excetion occur when closing"+se.getMessage(),"数据库关闭失败！");


	          }


	        }





	        return score;


	      }








	      public void writePaperTree(int nPaperID)throws DAOSysException{


	        //System.out.println("make tree ----------------------");


	        XmlParse xp=new XmlParse();


	        String filename=null;


	        String filedir=null;





	        Connection conn=null;


	        PreparedStatement ps=null;


	        ResultSet rs=null;


	        PreparedStatement ps1=null;


	        ResultSet rs1=null;





	        filedir=getPath("papertreedir");


	        File dir=new File(filedir);


	        if(!dir.exists()){


	          dir.mkdirs();


	        }


	        String papername=getName(nPaperID);


	        filename=nPaperID+".xml";


	        String filepath=filedir+filename;





	        //System.out.println("file is: "+filepath);





	        //create a docment file object


	        Document document=null;





	        //write a xml file head because if don't write head first the xml file can't be read!


	        xp.writehead(filepath);


	        // parse a xml file the file's name is xmlname





	        try{


	          document=xp.parseXml(filepath);


	        }


	        catch(Exception e)


	        {


	          System.err.println(e.getMessage());
	          throw new RuntimeException(e);

	        }


	        //get the root Element of the xml file


	        Element root=document.getDocumentElement();





	        Element temp=document.createElement("CH0");


	        Attr attr0=document.createAttribute("NODENAME");


	        Attr attr1=document.createAttribute("SRC");


	        Attr attr2=document.createAttribute("IDVALUE");


	        Attr attr3=document.createAttribute("CLICK");





	        Text text0=document.createTextNode("试卷预览");


	        Text text1=document.createTextNode("inSysadmin.jpg");


	        Text text2=document.createTextNode(""+nPaperID);


	        Text text3=document.createTextNode("top.navQustionList("


	            +nPaperID+",'../paperadmin/PaperPreview.jsp')");


	        attr0.appendChild(text0);


	        attr1.appendChild(text1);


	        attr2.appendChild(text2);


	        attr3.appendChild(text3);


	        temp.setAttributeNode(attr0);


	        temp.setAttributeNode(attr1);


	        temp.setAttributeNode(attr2);


	        temp.setAttributeNode(attr3);


	        root.appendChild(temp);


//	              System.out.println(temp.getAttribute("OSRC"));





	        Element temp0=document.createElement("CH0");


	        //append attribute to the node temp0


	        Attr attr0_0=document.createAttribute("NODENAME");


	        Attr attr0_1=document.createAttribute("SRC");


	        Attr attr0_2=document.createAttribute("IDVALUE");


	        Attr attr0_3=document.createAttribute("CLICK");





	        Text text0_0=document.createTextNode(papername);


	        Text text0_1=document.createTextNode("booktop1.gif");


	        Text text0_2=document.createTextNode(""+nPaperID);


	        Text text0_3=document.createTextNode("top.navQustionList("


	            +nPaperID+",'../paperadmin/FrameList.jsp')");





	        attr0_0.appendChild(text0_0);


	        attr0_1.appendChild(text0_1);


	        attr0_2.appendChild(text0_2);


	        attr0_3.appendChild(text0_3);


	        temp0.setAttributeNode(attr0_0);


	        temp0.setAttributeNode(attr0_1);


	        temp0.setAttributeNode(attr0_2);


	        temp0.setAttributeNode(attr0_3);


	        //append the  node temp to the root


	        root.appendChild(temp0);


	        try{


	          String strSnapShot="";


	          //Create a connection object


	          conn=getDataSource().getConnection();


	          ps=conn.prepareStatement(GET_FRAME_SQL,


	                                   ResultSet.TYPE_SCROLL_INSENSITIVE,


	                                   ResultSet.CONCUR_READ_ONLY);


	          ps.setInt(1,nPaperID);


	          //Execute some SQL statement


	          //Get a ResultSet object


	          rs=ps.executeQuery();


	          while(rs.next()){


	            //create a node temp0 named CH1


	            Element temp1=document.createElement("CH1");


	            //create temp0's three attributes





	            Attr attr1_0=document.createAttribute("NODENAME");


	            Attr attr1_1=document.createAttribute("IDVALUE");


	            Attr attr1_2=document.createAttribute("CLICK");


	            Attr attr1_3=document.createAttribute("SRC");


	            Attr attr1_4=document.createAttribute("OSRC");


	            Attr attr1_5=document.createAttribute("SEQ");





	            //append the attribute's value,the value is got from resultset!


	            Text text1_0=document.createTextNode(rs.getInt("PF_FRAM_ORDER")+"、"+getTypeName(rs.getInt("PF_PRAX_TYPE")));


	            Text text1_1=document.createTextNode(rs.getString("PF_PF_ID"));


	            Text text1_2=document.createTextNode("top.navQustionList("


	                +rs.getInt("PF_PF_ID")+",'../paperadmin/FrameDetail.jsp')");


	            Text text1_3=document.createTextNode("book0.gif");


	            Text text1_4=document.createTextNode("book1.gif");


	            Text text1_5=document.createTextNode(""+rs.getInt("PF_FRAM_ORDER"));





	            attr1_0.appendChild(text1_0);


	            attr1_1.appendChild(text1_1);


	            attr1_2.appendChild(text1_2);


	            attr1_3.appendChild(text1_3);


	            attr1_4.appendChild(text1_4);


	            attr1_5.appendChild(text1_5);





	            temp1.setAttributeNode(attr1_0);


	            temp1.setAttributeNode(attr1_1);


	            temp1.setAttributeNode(attr1_2);


	            temp1.setAttributeNode(attr1_3);


	            temp1.setAttributeNode(attr1_4);


	            temp1.setAttributeNode(attr1_5);





	            temp0.appendChild(temp1);





	            try{


	              ps1=conn.prepareStatement(GET_QUESTION_SQL,


	                                        ResultSet.TYPE_SCROLL_INSENSITIVE,


	                                        ResultSet.CONCUR_READ_ONLY);


	              ps1.setInt(1,rs.getInt("PF_PF_ID"));


	              //execute some SQL statment


	              rs1=ps1.executeQuery();


	              while(rs1.next()){


	                //create a node temp1 named CH2


	                Element temp2=document.createElement("CH2");


	                //create temp0's three attributes


	                Attr attr2_0=document.createAttribute("NODENAME");


	                Attr attr2_1=document.createAttribute("IDVALUE");


	                Attr attr2_2=document.createAttribute("CLICK");


	                Attr attr2_3=document.createAttribute("SRC");


	                Attr attr2_4=document.createAttribute("SEQ");





	                strSnapShot=rs1.getString("TQ_TQ_SNAPSHOT");
	                if (strSnapShot == null || strSnapShot.length() == 0) {

	                  strSnapShot = "";

	                }else{

	                  strSnapShot=strSnapShot.substring(0,


	                      strSnapShot.length()>15?15:strSnapShot.length());

	                }
	                Text text2_0=document.createTextNode(rs1.getInt("TQ_QUES_ORDER")+"、"+strSnapShot);


	                Text text2_1=document.createTextNode(rs1.getString("TQ_TQ_ID"));


	                Text text2_2=document.createTextNode("top.navQustionList("


	                    +rs1.getInt("TQ_TQ_ID")+",'../paperadmin/QuestionDetail.jsp')");


	                Text text2_3=document.createTextNode("leaf2.gif");


	                Text text2_4=document.createTextNode(""+rs1.getInt("TQ_QUES_ORDER"));





	                attr2_0.appendChild(text2_0);


	                attr2_1.appendChild(text2_1);


	                attr2_2.appendChild(text2_2);


	                attr2_3.appendChild(text2_3);


	                attr2_4.appendChild(text2_4);





	                temp2.setAttributeNode(attr2_0);


	                temp2.setAttributeNode(attr2_1);


	                temp2.setAttributeNode(attr2_2);


	                temp2.setAttributeNode(attr2_3);


	                temp2.setAttributeNode(attr2_4);


	                if(rs.isLast()){


	                  Attr attr2_5=document.createAttribute("S1");


	                  Text text2_5=document.createTextNode("1");


	                  attr2_5.appendChild(text2_5);


	                  temp2.setAttributeNode(attr2_5);


	                }


	                temp1.appendChild(temp2);


	              }


	            }catch(SQLException se){


	              throw new DAOSysException("error:"+se.getMessage(),"数据库操作失败：设置试卷属性时出错！");


	            }finally{


	              try{


	                if(rs1!=null)rs1.close();


	                if(ps1!=null)ps1.close();


	              }catch(SQLException se){


	                throw new DAOSysException("error:"+se.getMessage(),"数据库关闭失败！");


	              }


	            }





	            temp0.appendChild(temp1);


	          }


	        }catch(SQLException se){


	          throw new DAOSysException("error:"+se.getMessage(),"数据库操作失败：添加试卷导航树时出错！");


	        }finally{


	          try{


	            if(rs!=null)rs.close();


	            if(ps!=null)ps.close();


	            if(conn!=null)conn.close();


	          }catch(SQLException se){


	            throw new DAOSysException("error:"+se.getMessage(),"数据库关闭失败！");


	          }


	        }


	        xp.writeXml(filepath,document);


	      }





	      private String getPath(String str){


	        // String dir="c:\\bea\\user_projects\\applications\\buptdomain\\tested\\WEB-INF\\XML\\"+


	        //           str+"\\";


	        // return dir;


	        String filedir=null;


	        Connection conn=null;


	        PreparedStatement ps=null;


	        ResultSet rs=null;


	        //get a directory from db that xml file save at


	        try{


	          //========get connection to the database=========


	          conn=getDataSource().getConnection();


	          //create a prepareStatement


	          ps=conn.prepareStatement(GET_PATH_SQL,


	                                   ResultSet.TYPE_SCROLL_INSENSITIVE,


	                                   ResultSet.CONCUR_READ_ONLY);





	          ps.setString(1,str);


	          //execute some sql statement


	          //get a resultset


	          rs=ps.executeQuery();


	          //obtain the data you want to access


	          if(rs.next()){


	            filedir=rs.getString(1);


	          }


	        }catch(SQLException se){


	          throw new DAOSysException("error:"+se.getMessage(),"数据库操作失败：文件路径错误！");


	        }


	        finally{


	          try{


	            if(rs!=null) rs.close();


	            if(ps!=null)ps.close();


	            if(conn!=null)conn.close();


	          }catch(SQLException se){


	            throw new DAOSysException("Excetion occur when closing"+se.getMessage(),"数据库关闭失败！");


	          }


	        }


	        return filedir;


	      }


	      //


	      public void writeSchemaKnowDist(int nSchemaID)


	      throws DAOSysException{





	        Hashtable hSche=new Hashtable();


	        Vector vSche=new Vector();





	        String filedir=null;


	        String filedir1=null;


	        String filename=null;


	        String filepath=null;


	        String filepath1=null;


	        Connection conn=null;


	        PreparedStatement ps=null;


	        ResultSet rs=null;


	        PreparedStatement ps1=null;


	        ResultSet rs1=null;





	        XmlParse xp=new XmlParse();


	        Document document=null;


	        try{


	          //========get connection to the database=========


	          conn=getDataSource().getConnection();


	          //create a prepareStatement


	          ps=conn.prepareStatement(GET_SCHEMAITEM_SQL,


	                                   ResultSet.TYPE_SCROLL_INSENSITIVE,


	                                   ResultSet.CONCUR_READ_ONLY);





	          ps.setInt(1,nSchemaID);


	          ps.setInt(2,Constant.STATISTIC_KNOWLEDGE);


	          //execute some sql statement


	          //get a resultset


	          rs=ps.executeQuery();


	          //obtain the data you want to access


	          while(rs.next()){


	            //construct a hashtabe ,the key is the field "PSI_SCHE_KEY",the value is the field "PSI_KEY_VALUE"


	            hSche.put(new Integer(rs.getString("PSI_SCHE_KEY")),new Integer(rs.getInt("PSI_KEY_VALUE")));


	            //add the field PSI_SCHE_KEY to vSche


	            vSche.add(rs.getString("PSI_SCHE_KEY"));


	          }


	        }catch(SQLException se){


	          throw new DAOSysException("error:"+se.getMessage(),"数据库操作失败：获取试卷密钥时出错");


	        }


	        finally{


	          try{


	            if(rs!=null) rs.close();


	            if(ps!=null)ps.close();


	          }catch(SQLException se){


	            throw new DAOSysException("Excetion occur when closing"+se.getMessage(),"数据库关闭失败！");


	          }


	        }





	        try{


	          //create a prepareStatement


	          ps1=conn.prepareStatement(GET_COURSEID_SQL,


	                                    ResultSet.TYPE_SCROLL_INSENSITIVE,


	                                    ResultSet.CONCUR_READ_ONLY);





	          ps1.setInt(1,nSchemaID);


	          //execute some sql statement


	          //get a resultset


	          rs1=ps1.executeQuery();


	          //obtain the courseid and construct the filename


	          if(rs1.next()){


	            filename=rs1.getInt(1)+".xml";


	          }


	        }catch(SQLException se){


	          throw new DAOSysException("error:"+se.getMessage(),"数据库操作失败：创建文件时出错！");


	        }


	        finally{


	          try{


	            if(rs1!=null) rs1.close();


	            if(ps1!=null)ps1.close();


	            if(conn!=null)conn.close();


	          }catch(SQLException se){


	            throw new DAOSysException("Excetion occur when closing"+se.getMessage(),"数据库关闭失败！");


	          }


	        }


	        // abtain the dir of statisticpraxisdir saved in


	        filedir=getPath("statisticpraxisdir");


	        File dir=new File(filedir);


	        if(!dir.exists()){


	          dir.mkdirs();


	        }


	        //obtain the dir of schemaknowdist  saved in


	        filedir1=getPath("schemaknowdist");


	        File dir1=new File(filedir1);


	        if(!dir1.exists()){


	          dir1.mkdirs();


	        }


	        filepath=filedir+filename;


	        filepath1=filedir1+filename;


	        try{


	          //to parse the file saved in filepath


	          document=xp.parseXml(filepath);


	        }catch(Exception e){


	          System.out.println(e.getMessage());
	          throw new RuntimeException(e);

	        }


	        for(int i=0;i<vSche.size();i++){


	          //depend the vSche's data to obtain the element in the document


	          if(xp.goElement((String)(vSche.get(i)),document)!=null){


	            Element temp=xp.goElement((String)(vSche.get(i)),document);


	            // set the attribute "amount" value


	            temp.getAttributes().getNamedItem("AMOUNT").setNodeValue(""+hSche.get(new Integer(vSche.get(i).toString())));





	            //          System.out.println(hSche.get(new Integer(vSche.get(i).toString()))+"sjp");


	          }


	          else{


	            throw new DAOSysException("error occuring:maybe the knowledgenode does not exist!","所查找的知识点不存在！");


	          }


	        }


	        // write document to the filepath1


	        xp.writeXml(filepath1,document);


	      };


	      //


	      private String getName(int nPaperID){


	        String filename="";


	        String tempstr=null;


	        Connection conn=null;


	        PreparedStatement ps=null;


	        ResultSet rs=null;


	        //get name of file from db


	        try{


	          //========get connection to the database=========


	          conn=getDataSource().getConnection();


	          //create a prepareStatement


	          ps=conn.prepareStatement(GET_NAME_SQL,


	                                   ResultSet.TYPE_SCROLL_INSENSITIVE,


	                                   ResultSet.CONCUR_READ_ONLY);





	          ps.setInt(1,nPaperID);


	          //execute some sql statement


	          //get a resultset


	          rs=ps.executeQuery();


	          //obtain the data you want to access


	          if(rs.next()){


	            tempstr=rs.getString(1);


	          }


	        }catch(SQLException se){


	          throw new DAOSysException("error:"+se.getMessage(),"数据库操作失败：查找试卷时出错！");


	        }


	        finally{


	          try{


	            if(rs!=null) rs.close();


	            if(ps!=null)ps.close();


	            if(conn!=null)conn.close();


	          }catch(SQLException se){


	            throw new DAOSysException("Excetion occur when closing"+se.getMessage(),"数据库关闭失败！");


	          }


	        }


	        filename=filename+tempstr;


	        return filename;


	      }


	      private String getTypeName(int nPraxtype){


	        String typename=null;


	        Connection conn=null;


	        PreparedStatement ps=null;


	        ResultSet rs=null;





	        try{


	          //========get connection to the database=========


	          conn=getDataSource().getConnection();


	          //create a prepareStatement


	          ps=conn.prepareStatement(GET_TYPE_NAME_SQL,


	                                   ResultSet.TYPE_SCROLL_INSENSITIVE,


	                                   ResultSet.CONCUR_READ_ONLY);





	          ps.setInt(1,nPraxtype);


	          //execute some sql statement


	          //get a resultset


	          rs=ps.executeQuery();


	          //obtain the data you want to access


	          if(rs.next()){


	            typename=rs.getString(1);


	          }


	        }catch(SQLException se){


	          throw new DAOSysException("error:"+se.getMessage(),"数据库操作失败：查找试卷类型时出错！");


	        }


	        finally{


	          try{


	            if(rs!=null) rs.close();


	            if(ps!=null)ps.close();


	            if(conn!=null)conn.close();


	          }catch(SQLException se){


	            throw new DAOSysException("Excetion occur when closing"+se.getMessage(),"数据库关闭失败！");


	          }


	        }











	        return typename;





	      }


	      private String getDealflag(int nPraxtype){


	        String strDealflag=null;


	        Connection conn=null;


	        PreparedStatement ps=null;


	        ResultSet rs=null;


	        try{


	          //========get connection to the database=========


	          conn=getDataSource().getConnection();


	          //create a prepareStatement


	          ps=conn.prepareStatement(GET_DEAL_FLAG_SQL,


	                                   ResultSet.TYPE_SCROLL_INSENSITIVE,


	                                   ResultSet.CONCUR_READ_ONLY);





	          ps.setInt(1,nPraxtype);


	          //execute some sql statement


	          //get a resultset


	          rs=ps.executeQuery();


	          //obtain the data you want to access


	          if(rs.next()){


	            strDealflag=rs.getString(1);


	          }


	        }catch(SQLException se){


	          throw new DAOSysException("error:"+se.getMessage(),"数据库操作失败：查找试卷状态时出错");


	        }


	        finally{


	          try{


	            if(rs!=null) rs.close();


	            if(ps!=null)ps.close();


	            if(conn!=null)conn.close();


	          }catch(SQLException se){


	            throw new DAOSysException("Excetion occur when closing"+se.getMessage(),"数据库关闭失败！");


	          }


	        }





	        return strDealflag;


	      }





	      public void setQuestionBody(int nQuestionID,String strQuestionBody)


	      throws DAOSysException{


	        Connection conn=null;


	        PreparedStatement ps=null;


	        ResultSet rs=null;


	        Clob lob=null;


	        Writer clobWriter=null;





	        try{


	          conn=getDataSource().getConnection();


	          ps=conn.prepareStatement(UPDATE_QUESTIONBODY_SQL);


	          ps.setInt(1,nQuestionID);


	          ps.executeUpdate();


	          ps.clearParameters();





	          ps=conn.prepareStatement(GET_QUESTIONBODY_UPDATE_SQL);


	          ps.setInt(1,nQuestionID);


	          rs=ps.executeQuery();


	          ps.clearParameters();








	          if(rs.next()){


	            lob=rs.getClob(1);


	            clobWriter=((oracle.sql.CLOB)lob).getCharacterOutputStream();


	            clobWriter.write(strQuestionBody);


	            clobWriter.flush();


	            clobWriter.close();


	            lob=null;


	            clobWriter=null;


	          }


	        }


	        catch(Exception e){


	          e.printStackTrace();


	          throw new DAOSysException(e.getMessage(),"数据库操作失败：修改试题内容时出错");





	        }


	        finally{


	          try{


	            if(rs!=null)rs.close();


	            if(ps!=null)ps.close();


	            if(conn!=null)conn.close();


	          }catch(SQLException se){


	            throw new DAOSysException(se.getMessage(),"数据库关闭失败！");


	          }


	        }





	      }


	      public void setQuestionAnswer(int nQuestionID,String strQuestionAnswer)
	      throws DAOSysException{
	        Connection conn=null;
	        PreparedStatement ps=null;
	        ResultSet rs=null;
	        Clob lob=null;
	        Writer clobWriter=null;
	        try{
	          conn=getDataSource().getConnection();
	          ps=conn.prepareStatement(UPDATE_QUESTIONANSWER_SQL);
	          ps.setInt(1,nQuestionID);
	          ps.executeUpdate();
	          ps.clearParameters();
	          ps=conn.prepareStatement(GET_QUESTIONANSWER_UPDATE_SQL);
	          ps.setInt(1,nQuestionID);
	          rs=ps.executeQuery();
	          ps.clearParameters();
	          if(rs.next()){
	            lob=rs.getClob(1);
	            clobWriter=((oracle.sql.CLOB)lob).getCharacterOutputStream();
	            clobWriter.write(strQuestionAnswer);
	            clobWriter.flush();
	            clobWriter.close();
	            lob=null;
	            clobWriter=null;
	          }
	        }
	        catch(Exception e){
	          e.printStackTrace();
	          throw new DAOSysException(e.getMessage(),"数据库操作失败：修改试题内容时出错");
	        }
	        finally{
	          try{
	            if(rs!=null)rs.close();
	            if(ps!=null)ps.close();
	            if(conn!=null)conn.close();
	          }catch(SQLException se){
	            throw new DAOSysException(se.getMessage(),"数据库关闭失败！");
	          }
	        }
	      }
	      public String getQustionBody(int nQuestionID)


	      throws DAOSysException{


	        Connection conn=null;


	        PreparedStatement ps=null;


	        ResultSet rs=null;


	        Clob lob=null;


	        String strBody="";


	        try{


	          conn=getDataSource().getConnection();





	          ps=conn.prepareStatement(GET_QUESTIONBODY_SQL);


	          ps.setInt(1,nQuestionID);


	          rs=ps.executeQuery();





	          if(rs.next()){


	            lob=rs.getClob(1);


	            if(lob!=null){


	              strBody=lob.getSubString(1,(int)lob.length());


	              lob=null;


	            }


	          }


	        }


	        catch(Exception e){


	          e.printStackTrace();


	          throw new DAOSysException(e.getMessage(),"数据库操作失败：查找试题内容时出错！");


	        }


	        finally{


	          try{


	            if(rs!=null)rs.close();


	            if(ps!=null)ps.close();


	            if(conn!=null)conn.close();


	          }catch(SQLException se){


	            throw new DAOSysException(se.getMessage(),"数据库关闭失败！");


	          }


	        }





	        return strBody;


	      }





	      public String getQustionAnswer(int nQuestionID)


	      throws DAOSysException{


	        Connection conn=null;


	        PreparedStatement ps=null;


	        ResultSet rs=null;


	        Clob lob=null;


	        String strBody="";


	        try{


	          conn=getDataSource().getConnection();





	          ps=conn.prepareStatement(GET_QUESTIONANSWER_SQL);


	          ps.setInt(1,nQuestionID);


	          rs=ps.executeQuery();





	          if(rs.next()){


	            lob=rs.getClob(1);


	            if(lob!=null){


	              strBody=lob.getSubString(1,(int)lob.length());


	              lob=null;


	            }


	          }


	        }


	        catch(Exception e){


	          e.printStackTrace();


	          throw new DAOSysException(e.getMessage(),"数据库操作失败：查找试题内容时出错！");


	        }


	        finally{


	          try{


	            if(rs!=null)rs.close();


	            if(ps!=null)ps.close();


	            if(conn!=null)conn.close();


	          }catch(SQLException se){


	            throw new DAOSysException(se.getMessage(),"数据库关闭失败！");


	          }


	        }


	        return strBody;


	      }











	      //====================writen by merven========================


	      /**


	       * This method add a new info to the designated list


	       * @param cMaterialTO   a MaterialTO object


	       * @return int


	       * @throws DAOSysException


	       * @throws WriteBinaryException


	       */


	      public int addMaterial(MaterialTO cMaterialTO,boolean isUseByEJB)


	      throws DAOSysException{


	        Connection conn=null;


	        PreparedStatement ps=null;


	        ResultSet rs=null;


	        int nForeSeq=0;


	        String strQueryEmpty=


	        "INSERT INTO questionmaterial(QM_QM_ID,QM_QM_BODY) values(?,EMPTY_BLOB())";


	        String strQueryOperate=


	        "SELECT questionmaterial.QM_QM_BODY FROM questionmaterial WHERE QM_QM_ID=? for update";





	        try{


	          //======Make a connection to the specific database======





	          //Create a connection object


	          conn=getDataSource().getConnection();





	          //Get ahead the squence number autoincreasing in the database


	          String strQuerySeq="SELECT questionmaterial_seq.NEXTVAL FROM dual";


	          ps=conn.prepareStatement(strQuerySeq);


	          rs=ps.executeQuery();


	          if(rs.next()){


	            nForeSeq=rs.getInt(1);


	          }


	          //Insert the BLOB type material


	          //Firstly, instantiate the com.bupticet.util.OracleForBinary class


	          OracleForBinary oracleforbinary=


	          new  OracleForBinary(strQueryEmpty,strQueryOperate,nForeSeq);


	          oracleforbinary.writeForBlob(conn,cMaterialTO.cMaterialData,isUseByEJB);


	          //Create a PreparedStatement


	          ps = conn.prepareStatement(ADD_MATERIAL_SQL,


	                                     ResultSet.TYPE_SCROLL_INSENSITIVE,


	                                     ResultSet.CONCUR_UPDATABLE);


	          ps.setInt(1,cMaterialTO.nPraxisID);


	          ps.setString(2,cMaterialTO.strFormat);


	          ps.setString(3,cMaterialTO.strFileSuffix);


	          ps.setInt(4,nForeSeq);





	          //Execute some SQL statement


	          ps.executeUpdate();


	        }catch(SQLException se){


	          throw new DAOSysException("SQL Exception occurring: "


	                                    +se.getMessage(),"数据库操作失败：添加试卷说明时出错！");


	        }catch(Exception wbe){

	        	throw new RuntimeException(wbe);


	        }





	        //Free resource for other process


	        finally{


	          try{


	            if(rs!=null) rs.close();


	            if(ps!=null) ps.close();


	            if(conn!=null) conn.close();


	          }catch(SQLException se){


	            throw new DAOSysException("Exception occurring during close().."


	                                      +se.getMessage(),"数据库关闭失败！");


	          }


	        }


	        return nForeSeq;





	      }





	      /**


	       * This method get the info to the designated list


	       * @param cMaterialID   a id number


	       * @return MaterialTO


	       * @throws DAOSysException


	       */


	      public MaterialTO getMaterial(int nMaterialID)


	      throws DAOSysException{


	        Connection conn=null;


	        PreparedStatement ps=null;


	        ResultSet rs=null;


	        MaterialTO mto=null;


	        Blob blob = null;


	        try{


	          //======Make a connection to the specific database======





	          //Create a connection object


	          conn=getDataSource().getConnection();


	          //Create a PreparedStatement


	          ps= conn.prepareStatement(GET_MATERIAL_SQL,


	                                    ResultSet.TYPE_SCROLL_INSENSITIVE,


	                                    ResultSet.CONCUR_UPDATABLE);


	          ps.setInt(1,nMaterialID);





	          //Execute some SQL statement


	          //Get a ResultSet object


	          rs=ps.executeQuery();





	          //Obtain the data you wish to access


	          if(rs.next()) {


	            blob = rs.getBlob("cMaterialData");


	            mto=new MaterialTO(


	                               rs.getInt("nMaterialID"),


	                               rs.getInt("nCourseID"),


	                               rs.getString("strFormat"),


	                               rs.getBlob("cMaterialData").getBinaryStream(),


	                               rs.getString("strFileSuffix"),


	                               blob.getBytes(1,(int)blob.length()));


	          }


	        }catch(SQLException se){


	          throw new DAOSysException("SQL Exception occurring: "


	                                    +se.getMessage(),"数据库操作失败：获取试卷信息时出错！");


	        }





	        //Free resource for other process


	        finally{


	          try{


	            if(ps!=null) ps.close();


	            if(conn!=null) conn.close();


	          }catch(SQLException se){


	            throw new DAOSysException("Exception occurring during close().."


	                                      +se.getMessage(),"数据库关闭失败！");


	          }


	        }


	        //return infoTO


	        return  mto;





	      }





	      public String writeMaterialToFile(int nMaterialID,String strFilePath)throws DAOSysException{


	        try{


	          File path=new File(strFilePath+"material\\");


	          if(!path.exists()){


	            path.mkdirs();


	          }


	          MaterialTO materialTo=getMaterial(nMaterialID);


	          String strFileName=nMaterialID+"."+materialTo.strFileSuffix;


	          File materialFile=new File(path,strFileName);


	          FileOutputStream out=new FileOutputStream(materialFile);


	          InputStream in=materialTo.cMaterialData;


	          byte[] buf=new byte[1024];


	          int len=0;


	          byte[] en3=null;


	          len=in.read(buf);


	          while(len>0){


	            out.write(buf,0,len);


	            len=in.read(buf);


	          }


	          in.close();


	          out.flush();


	          out.close();


	          return strFileName;





	        }catch(FileNotFoundException fnfe){


	          throw new DAOSysException("Exception occurring during get FileOutputStream"


	                                    +fnfe.getMessage(),"文件未找到！");


	        }catch(IOException ioe){


	          throw new DAOSysException("Exception occurring during write data"


	                                    +ioe.getMessage(),"写数据时失败！");


	        }





	      }


	      //取出并保存素材


	      public String processMaterial(String strProcess,String strFilePath){


	        int indexBegin=0,indexEnd=0;


	        MaterialTO materialTo=null;


	        String strTemp=strProcess;


	        String strFileName="";


	        if(strProcess==null)strTemp="";


	        StringBuffer strTempBuf=new StringBuffer(strTemp);





	        indexBegin=strTemp.indexOf("nMaterialID");


	        while(indexBegin>0){


	          indexEnd=strTemp.indexOf('&',indexBegin);


	          if(indexEnd<indexBegin+24){


	            strFileName=


	            writeMaterialToFile(Integer.parseInt(strTemp.substring(indexBegin+12,indexEnd)),


	            strFilePath);


	            indexBegin=indexBegin-"../servlet/GetMaterialServlet?".length();


	            indexEnd=strTemp.indexOf('>',indexEnd);


	            if(strTemp.charAt(indexEnd-1)=='"'){


	              indexEnd-=1;


	            }


	            strTempBuf.replace(indexBegin,indexEnd,"./material/"+strFileName);


	            strTemp=strTempBuf.toString();


	            indexBegin=strTemp.indexOf("nMaterialID",indexEnd);


	          }


	        }


	        return strTemp;


	      }





	      //=========================================


	      /**


	       * This method get a list of frame


	       * @param nPaperID a paper id


	       * @return List


	       * @throws DAOSysException


	       */


	      public List getFrameList(int nPaperID)


	      throws DAOSysException{


	        Connection conn=null;


	        PreparedStatement ps=null;


	        PreparedStatement psSum=null;


	        ResultSet rs=null;


	        ResultSet rsSum=null;


	        List items=null;


	        int nQuesSum=0;


	        float fScoreSum=0;


	        int nFrameID=0;


	        try{


	          //======Make a connection to the specific database======


	          //Create a connection object


	          conn=getDataSource().getConnection();


	          //Create a PreparedStatemen


	          psSum= conn.prepareStatement(GET_FRAME_LIST_FORSUM_SQL,


	                                       ResultSet.TYPE_SCROLL_INSENSITIVE,


	                                       ResultSet.CONCUR_READ_ONLY);


	          ps= conn.prepareStatement(GET_FRAME_LIST_FORFRAME_SQL,


	                                    ResultSet.TYPE_SCROLL_INSENSITIVE,


	                                    ResultSet.CONCUR_READ_ONLY);


	          ps.setInt(1,nPaperID);


	          //Execute some SQL statement


	          //Get a ResultSet object


	          rs=ps.executeQuery();


	          //Obtain the data you wish to access and encapsulate it to the List structure


	          items=new ArrayList();


	          //Obtain items


	          //FrameTO(Hashtable cQuestions,int nFrameID,int nPaperID,int nOrder,


	          //int nPraxisTypeID,String strPraxisTypeName,String strComment,int nQuesSum,


	          //int nScoreSum)


	          while(rs.next()){


	            nFrameID=rs.getInt("nFrameID");


	            psSum.setInt(1,nFrameID);


	            //Execute some SQL statement


	            //Get a ResultSet object


	            rsSum=psSum.executeQuery();


	            if(rsSum.next()){


	              nQuesSum=rsSum.getInt("nQuesSum");


	              fScoreSum=rsSum.getFloat("fScoreSum");


	            }


	            items.add(new FrameTO(null,//Hashtable cQuestions


	                                  nFrameID,//int nFrameID


	                                  nPaperID,//int nPaperID


	                                  rs.getInt("nOrder"),//int nOrder


	                                  0,//nPraxisTypeID


	                                  rs.getString("strPraxisTypeName"),//string strParxisTypeName


	                                  rs.getString("strComment"),//string strComment


	                                  nQuesSum,//int nQuesSum


	                                  fScoreSum//float nScoreSum


	                                  ));





	          }


	        }catch(SQLException se){


	          throw new DAOSysException("SQL Exception occurring: "


	                                    +se.getMessage(),"查找帧列表时出错！");


	        }


	        //Free resource for other process


	        finally{


	          try{


	            if(rs!=null) rs.close();


	            if(ps!=null) ps.close();


	            if(conn!=null) conn.close();


	          }catch(SQLException se){


	            throw new DAOSysException("Exception occurring during close().."


	                                      +se.getMessage(),"数据库关闭失败！");


	          }


	        }


	        //return CourseList


	        return items;


	      }





	      /**


	       * This method get a list of question


	       * @param nFrameID   a frame id


	       * @return List


	       * @throws DAOSysException


	       */


	      public List getQuestionList(int nFrameID)


	      throws DAOSysException{


	        Connection conn=null;


	        PreparedStatement ps=null;


	        ResultSet rs=null;


	        List items=null;





	        try{


	          //======Make a connection to the specific database======


	          //Create a connection object


	          conn=getDataSource().getConnection();


	          //Create a PreparedStatemen


	          ps= conn.prepareStatement(GET_QUESTION_LIST_SQL,


	                                    ResultSet.TYPE_SCROLL_INSENSITIVE,


	                                    ResultSet.CONCUR_READ_ONLY);


	          ps.setInt(1,nFrameID);


	          //Execute some SQL statement


	          //Get a ResultSet object


	          rs=ps.executeQuery();


	          //Obtain the data you wish to access and encapsulate it to the List structure





	          items=new ArrayList();


	          //Obtain items


	          //QuestionTO(int nQuestionID,int nOriginalPraxisID,int nPraxisTypeID,int nFrameID,


	          //int nOrder,float fScore,PraxisContent cQuestionCont,String strGradApproach,


	          //String strSnapShot,float fDifficulty,String strCognizetypeString,int nSuggTime,


	          //Collection cMaterialIDs)


	          while(rs.next()){


	            items.add(new QuestionTO(rs.getInt("nQuestionID"),//nQuestionID


	                                     0,//nOriginalPraxisID


	                                     0,//nPraxisTypeID


	                                     nFrameID,//nFrameID


	                                     rs.getInt("nOrder"),//nOrder


	                                     rs.getFloat("fScore"),//fScore


	                                     null ,//cQuestionCont


	                                     null , //strGradApproach


	                                     rs.getString("strSnapShot"),//strSnapShot


	                                     rs.getFloat("fDifficulty"),//fDifficulty


	                                     null ,//strCognizetypeString


	                                     rs.getInt("nSuggTime"),//nSuggTime


	                                     null ));


	          }


	        }catch(SQLException se){


	          throw new DAOSysException("SQL Exception occurring: "


	                                    +se.getMessage(),"数据库操作失败：获取试题列表时出错！");


	        }


	        //Free resource for other process


	        finally{


	          try{


	            if(rs!=null) rs.close();


	            if(ps!=null) ps.close();


	            if(conn!=null) conn.close();


	          }catch(SQLException se){


	            throw new DAOSysException("Exception occurring during close().."


	                                      +se.getMessage(),"数据库关闭失败！");


	          }


	        }


	        //return CourseList


	        return items;


	      }


	      //===================Written by Merven======================


	      public String[] splitAnswer(String str,String delim){





	        Vector strVec=new Vector();


	        int len=delim.length();


	        int indexBegin=0;


	        int indexEnd=str.indexOf(delim);


	        while(indexEnd>-1){


	          indexEnd+=len;


	          strVec.add(str.substring(indexBegin,indexEnd));


	          indexBegin=indexEnd;


	          indexEnd=str.indexOf(delim,indexBegin);


	        }


	        String[] strArray=new String[strVec.size()];


	        for(int i=0;i<strVec.size();i++){


	          strArray[i]=(String)strVec.get(i);


	        }


	        return strArray;


	      }


	      //


	      //strIDs格式:('3','77')


	      public Hashtable getKnowNameByID(String strIDs) throws DAOSysException {
	        Hashtable ht=new Hashtable();
	        Connection conn=null;
	        Statement st=null;
	        ResultSet rs=null;
	        try{
	          //======Make a connection to the specific database======
	          //Create a connection object
	          conn=getDataSource().getConnection();
	          //Create a Statement object
	          st=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
	                                  ResultSet.CONCUR_READ_ONLY);
	          //Execute some SQL statement
	          //Get a ResultSet object
	          rs=st.executeQuery(GET_KNOWLEDGE_NAME_SQL+strIDs+" ORDER BY KN_KN_ID");
	          //obtain the data that you want to get
	          while(rs.next()){
	            ht.put(String.valueOf(rs.getInt("nID")),
	                   rs.getString("strName"));
	          }
	        }catch(SQLException se){
	          throw new DAOSysException("error occuring:"+se.getMessage(),"数据库操作失败：获取知识点试题时出错！");
	        }finally{
	          try{
	            if(rs!=null)rs.close();
	            if(st!=null)st.close();
	            if(conn!=null)conn.close();
	          }catch(SQLException se){
	            throw new DAOSysException("erroe occuring:"+se.getMessage(),"数据库关闭失败！");
	          }
	        }
	        return ht;
	      }

	      //liw添加strIDs格式:('3','77')


	      public Hashtable getPraxisTypeByID(String strIDs) throws DAOSysException {
	        Hashtable ht=new Hashtable();
	        Connection conn=null;
	        Statement st=null;
	        ResultSet rs=null;
	        try{
	          //======Make a connection to the specific database======
	          //Create a connection object
	          conn=getDataSource().getConnection();
	          //Create a Statement object
	          st=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
	                                  ResultSet.CONCUR_READ_ONLY);
	          //Execute some SQL statement
	          //Get a ResultSet object
	          rs=st.executeQuery(GET_PRAXIATYPE_NAME_SQL+strIDs+" ORDER BY PT_PT_ID");
	          //obtain the data that you want to get
	          while(rs.next()){
	            ht.put(String.valueOf(rs.getInt("nID")),
	                   rs.getString("strName"));
	          }
	        }catch(SQLException se){
	          throw new DAOSysException("error occuring:"+se.getMessage(),"数据库操作失败：获取知识点试题时出错！");
	        }finally{
	          try{
	            if(rs!=null)rs.close();
	            if(st!=null)st.close();
	            if(conn!=null)conn.close();
	          }catch(SQLException se){
	            throw new DAOSysException("erroe occuring:"+se.getMessage(),"数据库关闭失败！");
	          }
	        }
	        return ht;
	      }


/*//	下面的两个方法用于标准测试。
	      private TestMaterialTO writeMaterialToFile1(int nMaterialID,String strFilePath)throws DAOSysException{


	        try{


	          TestMaterialTO TestM=new TestMaterialTO();


	          File path=new File(strFilePath);


	          if(!path.exists()){


	            path.mkdirs();


	          }


	          MaterialTO materialTo=getMaterial(nMaterialID);


	          String strFileName=nMaterialID+"."+materialTo.strFileSuffix;


	          TestM.strFormat=materialTo.strFormat;


	          //TestM.url="./material/"+strFileName;


	          TestM.strFileName=strFileName;


	          File materialFile=new File(path,strFileName);


	          FileOutputStream out=new FileOutputStream(materialFile);


	          InputStream in=materialTo.cMaterialData;


	          byte[] buf=new byte[1024];


	          int len=0;


	          byte[] en3=null;


	          len=in.read(buf);


	          while(len>0){


	            out.write(buf,0,len);


	            len=in.read(buf);


	          }


	          in.close();


	          out.flush();


	          out.close();


	          return TestM;





	        }catch(FileNotFoundException fnfe){


	          throw new DAOSysException("Exception occurring during get FileOutputStream"


	                                    +fnfe.getMessage(),"文件未找到！");


	        }catch(IOException ioe){


	          throw new DAOSysException("Exception occurring during write data"


	                                    +ioe.getMessage(),"写数据时失败！");


	        }





	      }


	      //取出并保存素材


	      public String processMaterial1(String strProcess,String strFilePath){


	        int indexBegin=0,indexEnd=0;


	        MaterialTO materialTo=null;


	        //Hashtable tempHash=new Hashtable();


	        // int number=1;


	        String strTemp=strProcess;


	        String strFileName="";


	        if(strProcess==null)strTemp="";


	        StringBuffer strTempBuf=new StringBuffer(strTemp);


	        TestMaterialTO TestM=new TestMaterialTO();


	        indexBegin=strTemp.indexOf("nMaterialID");


	        while(indexBegin>0){


	          //number++;


	          indexEnd=strTemp.indexOf('&',indexBegin);


	          if(indexEnd<indexBegin+24){


	            TestM=


	            (TestMaterialTO)writeMaterialToFile1(Integer.parseInt(strTemp.substring(indexBegin+12,indexEnd)),


	            strFilePath);





	            indexBegin=indexBegin-"../servlet/GetMaterialServlet?".length();


	            indexEnd=strTemp.indexOf('>',indexEnd);


	            if(strTemp.charAt(indexEnd-1)=='"'){


	              indexEnd-=1;


	            }


	            strTempBuf.replace(indexBegin,indexEnd,"./material/"+TestM.strFileName+"&Format="+TestM.strFormat);


	            strTemp=strTempBuf.toString();


	            indexBegin=strTemp.indexOf("nMaterialID",indexEnd);


	            // tempHash.put(new Integer(number),TestM);


	          }


	        }


	        //System.out.println(" tigan  :"+strTemp);


	        // tempHash.put(new Integer(1),strTemp);


	        return strTemp;


	      }
*/




	      //把用于标准测试的xml文件存入数据库，把素材入库


	      public int addTestMaterial(MaterialTO cMaterialTO,boolean isUseByEJB)


	      throws DAOSysException{


	        Connection conn=null;


	        PreparedStatement ps=null;


	        ResultSet rs=null;


	        int nForeSeq=0;


	        String strQueryEmpty=


	        "INSERT INTO questionmaterial(QM_QM_ID,QM_QM_BODY) values(?,EMPTY_BLOB())";


	        String strQueryOperate=


	        "SELECT questionmaterial.QM_QM_BODY FROM questionmaterial WHERE QM_QM_ID=? for update";





	        try{


	          //======Make a connection to the specific database======





	          //Create a connection object


	          conn=getDataSource().getConnection();





	          //Get ahead the squence number autoincreasing in the database


	          String strQuerySeq="SELECT questionmaterial_seq.NEXTVAL FROM dual";


	          ps=conn.prepareStatement(strQuerySeq);


	          rs=ps.executeQuery();


	          if(rs.next()){


	            nForeSeq=rs.getInt(1);


	          }


	          //Insert the BLOB type material


	          //Firstly, instantiate the com.bupticet.util.OracleForBinary class


	         OracleForBinary oracleforbinary=


	          new  OracleForBinary(strQueryEmpty,strQueryOperate,nForeSeq);


	          oracleforbinary.writeForBlob(conn,cMaterialTO.cMaterialData,isUseByEJB);


	          //Create a PreparedStatement


	          ps = conn.prepareStatement(ADD_MATERIAL_SQL,


	                                     ResultSet.TYPE_SCROLL_INSENSITIVE,


	                                     ResultSet.CONCUR_UPDATABLE);


	          ps.setInt(1,cMaterialTO.nPraxisID);


	          ps.setString(2,cMaterialTO.strFormat);


	          ps.setString(3,cMaterialTO.strFileSuffix);


	          ps.setInt(4,nForeSeq);





	          //Execute some SQL statement


	          ps.executeUpdate();


	        }catch(SQLException se){


	          throw new DAOSysException("SQL Exception occurring: "


	                                    +se.getMessage(),"数据库操作失败：添加试卷说明时出错！");


	        }catch(Exception wbe){

	        	throw new RuntimeException(wbe);



	        }





	        //Free resource for other process


	        finally{


	          try{


	            if(rs!=null) rs.close();


	            if(ps!=null) ps.close();


	            if(conn!=null) conn.close();


	          }catch(SQLException se){


	            throw new DAOSysException("Exception occurring during close().."


	                                      +se.getMessage(),"数据库关闭失败！");


	          }


	        }


	        return nForeSeq;





	      }
	      public boolean updateApproach(String strApproach,int nQuestionID)
	      throws DAOSysException{
	        Connection conn=null;
	        PreparedStatement ps=null;
	        boolean fanhui=false;
	        try{
	          //======Make a connection to the specific database======
	          //Create a connection object
	          conn=getDataSource().getConnection();
	          ps=conn.prepareStatement(UPDATE_APPROACH_SQL);
	         ps.setString(1,strApproach);
	         ps.setInt(2,nQuestionID);
	         ps.executeUpdate();
	         fanhui=true;
	        }catch(SQLException se){
	          throw new DAOSysException("SQL Exception occurring: "
	                                    +se.getMessage(),"数据库操作失败：添加试卷说明时出错！");
	        }
	        //Free resource for other process
	        finally{
	          try{
	            if(ps!=null) ps.close();
	            if(conn!=null) conn.close();
	          }catch(SQLException se){
	            throw new DAOSysException("Exception occurring during close().."
	                                      +se.getMessage(),"数据库关闭失败！");
	          }
	        }
	        return fanhui;
	      }
//	end


	      public static void main(String[] args){


	       // OraclePaperAdminDAOImpl op=new OraclePaperAdminDAOImpl();
	/*
	     String dd="1;2;3;4;";
	     dd=dd.replaceAll("1","A");
	     dd=dd.replaceAll("2","B");
	     dd=dd.replaceAll("3","B");
	     System.out.println("fff::"+dd);
	        */
	/*
	        Hashtable ht=new Hashtable();
	        int i=0;
	        while(i<=3){
	          i++;
	        ht.put("a",new Integer(0));
	      }
	        */
	//System.out.println("ht:::"+(Float)(new Float(1)));
	        Hashtable htAllKnowIDs=new Hashtable();
	        Hashtable htKnowDist=new Hashtable();
	          Hashtable htKnowDistNew=new Hashtable();
	          Vector v11=new Vector();
	          Vector v12=new Vector();
	          Vector v13=new Vector();
	          Vector v14=new Vector();
	          Vector v15=new Vector();
	          Vector v16=new Vector();
	          v11.add(new Integer(1));
	          v12.add(new Integer(2));
	          v13.add(new Integer(3));
	          v14.add(new Integer(4));
	          v11.add(new Integer(2));
	          v11.add(new Integer(3));
	          v11.add(new Integer(4));
	          v12.add(new Integer(3));
	            v12.add(new Integer(4));



	htAllKnowIDs.put(new Integer(1),v11);
	htAllKnowIDs.put(new Integer(2),v12);
	htAllKnowIDs.put(new Integer(3),v13);
	htAllKnowIDs.put(new Integer(4),v14);

	htKnowDist.put("1",new Integer(12));
	htKnowDist.put("2",new Integer(18));

	  //System.out.println("htKnowDist:::"+htKnowDist.toString());
	     Enumeration enKeys=htKnowDist.keys();
	     String key="";
	     Integer key1=null;


	     HashSet allV=new HashSet();
	     HashSet allV2=new HashSet();
	  while(enKeys.hasMoreElements()){
	       key=(String)enKeys.nextElement();
	     //  System.out.println("key:::"+key);
	    //   System.out.println("key:::"+htAllKnowIDs.get(new Integer(key)));
	       Vector v18=(Vector)htAllKnowIDs.get(new Integer(key));
	    //   System.out.println("v18:::"+v18);
	        Iterator it3=v18.iterator();
	       while(it3.hasNext()){
	       allV.add(it3.next());
	      //  System.out.println("allV:::"+allV);
	     }
	  }


	  //enKeys=htKnowDist.keys();
	  Iterator it=allV.iterator();
	    while(it.hasNext()){
	       key1=(Integer)it.next();
	    //   System.out.println("key1::"+key1);
	       enKeys=htKnowDist.keys();
	    while(enKeys.hasMoreElements()){
	       key=(String)enKeys.nextElement();
	   //      System.out.println("key::"+key);
	   if(key.equals(key1.toString())){
	    allV2.add(key1);
	    Vector v4=new Vector();
	    v4.add(new Integer(key));
	    htKnowDistNew.put(v4,htKnowDist.get(key));
	   }
	   }
	     }
	      //System.out.println("htKnowDistNew::"+htKnowDistNew);
	     //System.out.println("allV::"+allV);
	     //System.out.println("allV2::"+allV2);
	   //  System.out.println("htKnowDistNew1111111::"+htKnowDistNew);
	     allV.removeAll(allV2);
	      //System.out.println("allV::"+allV);
	     Iterator it1=allV.iterator();
	     while(it1.hasNext()){
	       Vector v2=new Vector();
	       v2.add(it1.next());
	     htKnowDistNew.put(v2,new Integer(-1));
	     }

	/*
	  enKeys=htKnowDist.keys();
	     while(enKeys.hasMoreElements()){
	       key=(String)enKeys.nextElement();
	       htKnowDistNew.put(htAllKnowIDs.get(new Integer(key)),htKnowDist.get(key));
	     }
	     */
		//System.out.println("htKnowDistNew:::"+htKnowDistNew);
	      }





		public String processMaterial1(String strProcess, String strFilePath) throws DAOSysException {
			// TODO 自动生成方法存根
			return null;
		}

}
