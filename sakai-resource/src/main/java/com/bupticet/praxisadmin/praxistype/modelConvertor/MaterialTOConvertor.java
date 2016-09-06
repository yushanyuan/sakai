package com.bupticet.praxisadmin.praxistype.modelConvertor;

import com.bupticet.paperadmin.common.MaterialTO;

public class MaterialTOConvertor {
	public int nMaterialID;

	public int nPraxisID;

	public String strFormat = "";

	public String strFileSuffix = "";

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
	public MaterialTO convertToMaterialTo()
	{
		//System.out.println("nMaterialID=="+this.nMaterialID);
		//System.out.println("nPraxisID=="+this.nPraxisID);
		//System.out.println("strFileSuffix=="+this.strFileSuffix);
		//System.out.println("strFormat=="+this.strFormat);
		MaterialTO result=new MaterialTO();
		result.setNMaterialID(this.nMaterialID);
		result.setNPraxisID(this.nPraxisID);
		result.setStrFileSuffix(this.strFileSuffix);
		result.setStrFormat(this.strFormat);
		return result;
	}
	
}
