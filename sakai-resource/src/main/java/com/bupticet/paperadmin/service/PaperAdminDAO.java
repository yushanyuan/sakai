package com.bupticet.paperadmin.service;

import java.util.Hashtable;

import java.util.List;

import java.util.ArrayList;

import java.io.IOException;


import com.bupticet.paperadmin.common.DAOSysException;
import com.bupticet.paperadmin.common.MaterialTO;
import com.bupticet.paperadmin.common.Page;
import com.bupticet.paperadmin.schema.model.SchemaContent;


public interface PaperAdminDAO {

	public List getItemInfos(String sql)

	throws DAOSysException;

	public List getItemInfosLjw(String sql, int nRelativite)
			throws DAOSysException;

	public Page getPaperList(int nCount, int nStart, String strSearchQuery)

	throws DAOSysException;

	public Page getSchemaList(int nCount, int nStart, int nCourseID,
			String strSearchQuery)

	throws DAOSysException;

	public ArrayList getAllSchema(int nCourseID)

	throws DAOSysException;

	public void setQuestionBody(int nQuestionID, String strQuestionBody)

	throws DAOSysException;

	public void setQuestionAnswer(int nQuestionID, String strQuestionAnswer)

	throws DAOSysException;

	public String getQustionBody(int nQuestionID)

	throws DAOSysException;

	public String getQustionAnswer(int nQuestionID)

	throws DAOSysException;

	public void writePaperTree(int nPaperID) throws DAOSysException;

	public void writePaperToXML(int nPaperID, String strPassad)
			throws DAOSysException;

	// ====================writen by merven========================

	/**
	 * 
	 * 
	 * This method add a new info to the designated list
	 * 
	 * 
	 * @param cMaterialTO
	 *            a MaterialTO object
	 * 
	 * 
	 * @return int
	 * 
	 * 
	 * @throws DAOSysException
	 * 
	 * 
	 */

	public int addMaterial(MaterialTO cMaterialTO, boolean isUseByEJB)

	throws DAOSysException;

	/**
	 * 
	 * 
	 * This method add a new info to the designated list
	 * 
	 * 
	 * @param nMaterialID
	 *            a Material id
	 * 
	 * 
	 * @return MaterialTO
	 * 
	 * 
	 * @throws DAOSysException
	 * 
	 * 
	 */

	public MaterialTO getMaterial(int nMaterialID)

	throws DAOSysException;

	/**
	 * 
	 * 
	 * This method get a list of frame
	 * 
	 * 
	 * @param nPaperID
	 *            a paper id
	 * 
	 * 
	 * @return List
	 * 
	 * 
	 * @throws DAOSysException
	 * 
	 * 
	 */

	public List getFrameList(int nPaperID)

	throws DAOSysException;

	/**
	 * 
	 * 
	 * This method get a list of question
	 * 
	 * 
	 * @param nFrameID
	 *            a frame id
	 * 
	 * 
	 * @return List
	 * 
	 * 
	 * @throws DAOSysException
	 * 
	 * 
	 */

	public List getQuestionList(int nFrameID)

	throws DAOSysException;

	public void writeSchemaKnowDist(int nSchemaID)

	throws DAOSysException;

	public SchemaContent getPaperStatics(int paperID)

	throws ClassNotFoundException, IOException;

	// ===============Written by Merven=====================

	public Hashtable getKnowNameByID(String strIDs) throws DAOSysException;

	public String processMaterial(String strProcess, String strFilePath)
			throws DAOSysException;

	public String processMaterial1(String strProcess, String strFilePath)
			throws DAOSysException;

	public int addTestMaterial(MaterialTO cMaterialTO, boolean isUseByEJB)
			throws DAOSysException;

	public void storePaperStatics(int paperID, int schemaID,
			SchemaContent schemaContent);

	public Hashtable getPraxisTypeByID(String strIDs) throws DAOSysException;

	public boolean updateApproach(String strApproach, int nQuestionID)
			throws DAOSysException;
}