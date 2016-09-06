package com.bupticet.paperadmin.common;

import java.io.Serializable;

import java.io.InputStream;

import com.bupticet.praxisadmin.praxistype.modelConvertor.MaterialTOConvertor;

/**
 * This class represents an individual line item the course detail.
 */

public class MaterialTO implements Serializable {

	public int nMaterialID;

	public int nPraxisID;

	public String strFormat = "";

	public String strFileSuffix = "";

	public InputStream cMaterialData;

	public byte[] content;

	public MaterialTO() {

		nMaterialID = 0;

		nPraxisID = 0;

		strFormat = "";

		strFileSuffix = "";

		cMaterialData = null;

		content = null;

	}

	public MaterialTO(int nMaterialID, int nPraxisID, String strFormat, String strFileSuffix) {

		this.nMaterialID = nMaterialID;

		this.nPraxisID = nPraxisID;

		this.strFormat = strFormat;

		this.strFileSuffix = strFileSuffix;

		content = null;

	}

	public MaterialTO(int nMaterialID, int nPraxisID, String strFormat, InputStream cMaterialData,
			String strFileSuffix, byte[] b) {

		this.nMaterialID = nMaterialID;

		this.nPraxisID = nPraxisID;

		this.strFormat = strFormat;

		this.cMaterialData = cMaterialData;

		this.strFileSuffix = strFileSuffix;

		this.content = b;

		// this.materialData = (CloneINStream)cMaterialData;

	}

	public InputStream getCMaterialData() {
		return cMaterialData;
	}

	public void setCMaterialData(InputStream materialData) {
		cMaterialData = materialData;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public int getNMaterialID() {
		return nMaterialID;
	}

	public void setNMaterialID(int materialID) {
		nMaterialID = materialID;
	}

	public int getNPraxisID() {
		return nPraxisID;
	}

	public void setNPraxisID(int praxisID) {
		nPraxisID = praxisID;
	}

	public String getStrFileSuffix() {
		return strFileSuffix;
	}

	public void setStrFileSuffix(String strFileSuffix) {
		this.strFileSuffix = strFileSuffix;
	}

	public String getStrFormat() {
		return strFormat;
	}

	public void setStrFormat(String strFormat) {
		this.strFormat = strFormat;
	}

	public MaterialTOConvertor convertToMaterialTo() {
		MaterialTOConvertor result = new MaterialTOConvertor();
		result.setNMaterialID(this.nMaterialID);
		result.setNPraxisID(this.nPraxisID);
		result.setStrFileSuffix(this.strFileSuffix);
		result.setStrFormat(this.strFormat);
		return result;
	}
}