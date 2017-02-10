/**
 * 版权所有 北京思开科技有限公司 
 * All Rights Reserved
 */
package cn.net.sikai.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.sakaiproject.db.api.SqlService;
import cn.net.sikai.model.SimplePage;
/**
 * @Description: TODO
 * @author: yushanyuan
 *
 */
public class LessonBuilderHelperDao {

	private SqlService sqlService;
	

	public SqlService getSqlService() {
		return sqlService;
	}

	public void setSqlService(SqlService sqlService) {
		this.sqlService = sqlService;
	}
	

	public void init(){
		System.out.println(" init helper dao -----");
		
	}
	
	
	public String findPageIdByTooIdInSakaiSiteTool(String toolId) throws SQLException{
		String pageId = null;
		Connection c = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		 
		c = sqlService.borrowConnection();
		
		String sql1 = "select PAGE_ID from  SAKAI_SITE_TOOL where  TOOL_ID=? ";
		pst = c.prepareStatement(sql1);
		pst.setString(1, toolId);
		rs = pst.executeQuery();
		if(rs.next()){
			pageId = rs.getString("PAGE_ID");
		} 
		
		if (rs != null)
			rs.close();

		if (pst != null)
			pst.close();

		if (c != null)
			sqlService.returnConnection(c);
		
		return pageId;
		
	}

	public Long getMainPageIdBySiteId(String siteId) throws SQLException{
		Long pageId = null;
		Connection c = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		 
		c = sqlService.borrowConnection();
		
		String sql1 = "select PAGEID from  LESSON_BUILDER_PAGES where  SITEID=? and  PARENT is NULL and TOPPARENT is NULL ";
		pst = c.prepareStatement(sql1);
		pst.setString(1, siteId);
		rs = pst.executeQuery();
		if(rs.next()){
			pageId = rs.getLong("PAGEID");
		} 
		
		if (rs != null)
			rs.close();

		if (pst != null)
			pst.close();

		if (c != null)
			sqlService.returnConnection(c);
		
		return pageId;
	}
	
	public Long getMainPageIdByToolId(String toolId) throws SQLException{
		Long pageId = null;
		Connection c = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		 
		c = sqlService.borrowConnection();
		
		String sql1 = "select PAGEID from  LESSON_BUILDER_PAGES where  TOOLID=? and  PARENT is NULL and TOPPARENT is NULL ";
		pst = c.prepareStatement(sql1);
		pst.setString(1, toolId);
		rs = pst.executeQuery();
		if(rs.next()){
			pageId = rs.getLong("PAGEID");
		} 
		
		if (rs != null)
			rs.close();

		if (pst != null)
			pst.close();

		if (c != null)
			sqlService.returnConnection(c);
		
		return pageId;
	}
	
	
	public String getToolIdBySiteId(String siteId) throws SQLException{
		String toolId = null;
		Connection c = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		 
		c = sqlService.borrowConnection();
		
		String sql1 = "select TOOLID from  LESSON_BUILDER_PAGES where  SITEID=? and  PARENT is NULL and TOPPARENT is NULL ";
		pst = c.prepareStatement(sql1);
		pst.setString(1, siteId);
		rs = pst.executeQuery();
		if(rs.next()){
			toolId = rs.getString("TOOLID");
		} 
		
		if (rs != null)
			rs.close();

		if (pst != null)
			pst.close();

		if (c != null)
			sqlService.returnConnection(c);
		
		return toolId;
	}
	public String getSiteIdByToolId(String toolId) throws SQLException{
		String siteId = null;
		Connection c = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		 
		c = sqlService.borrowConnection();
		
		String sql1 = "select SITEID from  LESSON_BUILDER_PAGES where  TOOLID=? and  PARENT is NULL and TOPPARENT is NULL ";
		pst = c.prepareStatement(sql1);
		pst.setString(1, toolId);
		rs = pst.executeQuery();
		if(rs.next()){
			siteId = rs.getString("SITEID");
		} 
		
		if (rs != null)
			rs.close();

		if (pst != null)
			pst.close();

		if (c != null)
			sqlService.returnConnection(c);
		
		return siteId;
	}
	public SimplePage savePage(SimplePage page) throws SQLException{
		
		SimplePage exist = queryPageBy(page.getToolId(),page.getSiteId(), page.getTitle(), page.getParent(), page.getTopParent());
		if(exist!=null){
			return exist;
		}
		Connection c = null;
		PreparedStatement pst = null;
		 
		c = sqlService.borrowConnection();
		
		String sql1 = "insert into LESSON_BUILDER_PAGES(TOOLID,SITEID,TITLE,PARENT,TOPPARENT, HIDDEN, GROUPOWNED) values (?,?,?,?,?,false,false)";
		if(page.getParent()==null){
			sql1 = "insert into LESSON_BUILDER_PAGES(TOOLID,SITEID,TITLE,HIDDEN,GROUPOWNED) values (?,?,?,false,false)";	
		}
		
		pst = c.prepareStatement(sql1);
		
		pst.setString(1, page.getToolId());
		pst.setString(2, page.getSiteId());
		pst.setString(3, page.getTitle());
		if(page.getParent()!=null){
			pst.setLong(4, page.getParent());
			pst.setLong(5, page.getTopParent());
		}
		
		int n = pst.executeUpdate();
		c.commit();
		if (pst != null)
			pst.close();

		if (c != null)
			sqlService.returnConnection(c);
		
		return queryPageBy(page.getToolId(),page.getSiteId(), page.getTitle(), page.getParent(), page.getTopParent());
	}
	
	public SimplePage queryPageBy(String toolId, String siteId, String title, Long parent, Long topParent) throws SQLException{
		SimplePage page = null;
		Connection c = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		 
		c = sqlService.borrowConnection();
		
		String sql1 = "select PAGEID from  LESSON_BUILDER_PAGES where TOOLID=? and SITEID=? and TITLE=? and PARENT=? and TOPPARENT=? ";
		if(parent == null){
			sql1 = "select PAGEID from  LESSON_BUILDER_PAGES where TOOLID=? and SITEID=? and TITLE=? and PARENT is NULL and TOPPARENT is NULL";
		}
		pst = c.prepareStatement(sql1);
		pst.setString(1, toolId);
		pst.setString(2, siteId);
		pst.setString(3, title);
		if(parent != null){
			pst.setLong(4, parent);
			pst.setLong(5, topParent);
		}
		rs = pst.executeQuery();
		if(rs.next()){
			page = new SimplePage(toolId, siteId, title, parent, topParent);
			page.setPageId(rs.getLong("PAGEID"));
		} 
		
		if (rs != null)
			rs.close();

		if (pst != null)
			pst.close();

		if (c != null)
			sqlService.returnConnection(c);
		
		return page;
	}
	
	public void insertItemByType2(Long pageId, String sakaiId, String name) throws SQLException{
		Long id = findItemByPageIdAndTypeAndSakaiId(pageId, 2, sakaiId);
		if(id>0){
			return;
		}
		
		int seqence = getMaxSequence(pageId);
		seqence ++;
		
		Connection c = null;
		PreparedStatement pst = null;
		 
		c = sqlService.borrowConnection();
		
		String sql1 = "insert into  LESSON_BUILDER_ITEMS(PAGEID, SEQUENCE, TYPE, SAKAIID,"
				+ "NAME, DESCRIPTION,HEIGHT, WIDTH, ALT, NEXTPAGE, FORMAT,REQUIRED, ALTERNATE, "
				+ "PREREQUISITE, SUBREQUIREMENT, REQUIREMENTTEXT, SAMEWINDOW, ANONYMOUS, SHOWCOMMENTS,"
				+ "FORCEDCOMMENTSAnonymous, SHOWPEEREVAL, GROUPOWNED, OWNERGROUPS, ATTRIBUTESTRING) "
				+ "values (?,?,2,?,?,'','300px','100%','',false, '',false,false,"
				+ "false,false,'',false,false, false,false,false,false,'','{}')";
		pst = c.prepareStatement(sql1);
		pst.setLong(1, pageId);
		pst.setInt(2, seqence);
		pst.setString(3, sakaiId);
		pst.setString(4, maxlength(name, 100));
		
		int n = pst.executeUpdate();
		 c.commit();
		if (pst != null)
			pst.close();

		if (c != null)
			sqlService.returnConnection(c);
	}
	
	
	public void insertItemByType5(Long pageId, String html) throws SQLException{
		Long id = findItemByPageIdAndTypeAndSakaiId(pageId, 5, "");
		if(id>0){
			updateItemByType5(id, html);
			return;
		}
		
		int seqence = getMaxSequence(pageId);
		seqence ++;
		
		Connection c = null;
		PreparedStatement pst = null;
		 
		c = sqlService.borrowConnection();
		
		String sql1 = "insert into  LESSON_BUILDER_ITEMS(PAGEID, SEQUENCE, TYPE, SAKAIID,"
				+ "NAME, HTML,DESCRIPTION,HEIGHT, WIDTH, ALT, NEXTPAGE,REQUIRED, ALTERNATE, "
				+ "PREREQUISITE, SUBREQUIREMENT, REQUIREMENTTEXT, SAMEWINDOW, ANONYMOUS, SHOWCOMMENTS,"
				+ "FORCEDCOMMENTSAnonymous, SHOWPEEREVAL, GROUPOWNED, OWNERGROUPS, ATTRIBUTESTRING) "
				+ "values (?,?,5,'','',?,'','300px','100%','',false, false,false,"
				+ "false,false,'',false,false,false, false,false,false,'','{}')";
		pst = c.prepareStatement(sql1);
		pst.setLong(1, pageId);
		pst.setInt(2, seqence);
		pst.setString(3, html);
		
		int n =pst.executeUpdate();
		 c.commit();

		if (pst != null)
			pst.close();

		if (c != null)
			sqlService.returnConnection(c);
	}
	

	public void insertItemByType1(Long pageId, String sakaiId, String name,String html) throws SQLException{
		
		Long id = findItemByPageIdAndTypeAndSakaiId(pageId, 1, sakaiId);
		if(id>0){
			return;
		}
		int seqence = getMaxSequence(pageId);
		seqence ++;
		
		Connection c = null;
		PreparedStatement pst = null;
		 
		c = sqlService.borrowConnection();
		
		String sql1 = "insert into  LESSON_BUILDER_ITEMS(PAGEID, SEQUENCE, TYPE, SAKAIID,"
				+ "NAME, HTML,DESCRIPTION,HEIGHT, WIDTH, ALT, NEXTPAGE,REQUIRED, ALTERNATE, "
				+ "PREREQUISITE, SUBREQUIREMENT, REQUIREMENTTEXT, SAMEWINDOW, ANONYMOUS, SHOWCOMMENTS,"
				+ "FORCEDCOMMENTSAnonymous, SHOWPEEREVAL, GROUPOWNED, OWNERGROUPS, ATTRIBUTESTRING) "
				+ "values (?,?,1,?,?,?,'','300px','100%','',false, false,false,"
				+ "false,false,'',false,false, false,false, false,false,'','{}')";
		pst = c.prepareStatement(sql1);
		pst.setLong(1, pageId);
		pst.setInt(2, seqence);
		pst.setString(3, sakaiId);
		pst.setString(4, maxlength(name, 100));
		pst.setString(5, html);
		
		int n = pst.executeUpdate();
		c.commit();

		if (pst != null)
			pst.close();

		if (c != null)
			sqlService.returnConnection(c);
	}

	public void updateItemByType5(Long id, String html) throws SQLException{
		Connection c = null;
		PreparedStatement pst = null;
		 
		c = sqlService.borrowConnection();
		
		String sql1 = "update LESSON_BUILDER_ITEMS set HTML=? where ID=?";
		pst = c.prepareStatement(sql1);
		pst.setString(1, html);
		pst.setLong(2, id);
		
		
		int n = pst.executeUpdate();
		c.commit();

		if (pst != null)
			pst.close();

		if (c != null)
			sqlService.returnConnection(c);
	}

	private int getMaxSequence(Long pageId) throws SQLException{
		int max = 0;
		Connection c = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		 
		c = sqlService.borrowConnection();
		
		String sql1 = "select SEQUENCE from  LESSON_BUILDER_ITEMS where PAGEID=? ";
		pst = c.prepareStatement(sql1);
		pst.setLong(1, pageId);
		rs = pst.executeQuery();
		if(rs.next()){
			max = rs.getInt("SEQUENCE");
		} 
		
		if (rs != null)
			rs.close();

		if (pst != null)
			pst.close();

		if (c != null)
			sqlService.returnConnection(c);
		
		return max;		
	}
	private Long findItemByPageIdAndTypeAndSakaiId(Long pageId, int type, String sakaiId) throws SQLException{
		long id = 0;
		Connection c = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		 
		c = sqlService.borrowConnection();
		
		String sql1 = "select ID from  LESSON_BUILDER_ITEMS where PAGEID=? and TYPE=? and SAKAIID=?";
		pst = c.prepareStatement(sql1);
		pst.setLong(1, pageId);
		pst.setInt(2, type);
		pst.setString(3, sakaiId);
		rs = pst.executeQuery();
		while(rs.next()){
			id = rs.getLong("ID");
		}
		
		if (rs != null)
			rs.close();

		if (pst != null)
			pst.close();

		if (c != null)
			sqlService.returnConnection(c);
		
		return id;		
	}
	private String maxlength(String s, int maxlen) {
	    if (s == null)
		s = "";  // oracle turns "" into null
	    int len = s.length();
	    if (s == null || len <= maxlen)
		return s;
	    int toremove = len - maxlen;
	    int i = s.lastIndexOf(".");

	    // 6 is sort of arbitrary. need a few characters before the .
	    // to do useful abbreviation. The issue here is that in theory
	    // so much of the name might be after the dot that we have to
	    // the abbreviation there.
	
	    if (i > 0 && toremove < (i - 6)) {
		String prefix = s.substring(0, i);
		String suffix = s.substring(i+1);
		int startcut = (i / 2) - (toremove / 2);
		int endcut = startcut + toremove;
		prefix = prefix.substring(0, startcut) + prefix.substring(endcut);
		return prefix + "." + suffix;
	    }

	    // not enough space to cut from prefix, or no prefix. 
	    // for now, just cut the whole string.
	    int startcut = (len / 2) - (toremove / 2);
	    int endcut = startcut + toremove;
	    return s.substring(0, startcut) + s.substring(endcut);
	}
}
