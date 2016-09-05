package org.sakaiproject.resource.util;

public class AsyncGridModel {
	private StringBuilder data;
	
	public String getData(){
        return data.toString();
    }
	
	public AsyncGridModel(){
        data = new StringBuilder("{");
    }

	public void setDataIndex(String dataIndex,Object value){
        if(value==null){
        	value = "";
        }
        data.append(dataIndex+":");
        String sn = value.getClass().getSimpleName();//获取数据类型
        if(sn.equals("String")){
            data.append("'"+value.toString().replace("'", "\\\'").replace("\"", "\\\"")+"',");
        }
        else if(sn.equals("Integer")){
            data.append("'"+value+"',");
        }
        else if(sn.equals("Boolean")){
            data.append(value+",");
        }
        else if(sn.equals("Double")){
            data.append("'"+value+"',");
        }
        else if(sn.equals("Long")){
            data.append("'"+value+"',");
        }
        else if(sn.equals("Short")){
            data.append("'"+value+"',");
        }
        else if(sn.equals("Date")){
        	data.append("'"+value.toString().substring(0, 10).replace("'", "\\\'").replace("\"", "\\\"")+"',");
        }
        else if(sn.equals("Timestamp")){
        	data.append("'"+value.toString().substring(0, 19).replace("'", "\\\'").replace("\"", "\\\"")+"',");
        }
        else {
            data.append("'"+value+"',");
        }
    }
}
