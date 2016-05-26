package com.app.taxisharingDriver.pojo;
import com.google.gson.annotations.SerializedName;
public class CarType 
{
	@SerializedName("types")
    java.util.ArrayList<CarTypeDetail>carTypeDeataiList;

	public java.util.ArrayList<CarTypeDetail> getCarTypeDeataiList() {
		return carTypeDeataiList;
	}

	public void setCarTypeDeataiList(
			java.util.ArrayList<CarTypeDetail> carTypeDeataiList) {
		this.carTypeDeataiList = carTypeDeataiList;
	}
	
	
	private String errFlag;
	private String errNum;
	private String errMsg;

	public String getErrFlag() {
		return errFlag;
	}

	public void setErrFlag(String errFlag) {
		this.errFlag = errFlag;
	}

	public String getErrNum() {
		return errNum;
	}

	public void setErrNum(String errNum) {
		this.errNum = errNum;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	
	
	/*"errFlag":"0",
	"errNum":"21",
	"errMsg":"Got the details!"*/
	
}
