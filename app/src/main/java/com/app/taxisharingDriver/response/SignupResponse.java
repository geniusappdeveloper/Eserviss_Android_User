package com.app.taxisharingDriver.response;

public class SignupResponse
{
	/*{

	    "errNum":"12",
	    "errFlag":"0",
	    "errMsg":"Thank you for providing your details! One of our agents will contact you in the next 24 hours to complete your registration!",
	    "data":{
	        "token":"333536353535303539373039353533ChBprChBprjYMTHsxcnT4FXUujYMTHsxcnT4FXUu",
	        "expiryLocal":"2014-08-28 12:26:18",
	        "expiryGMT":"2014-08-28 11:26:18",
	        "flag":"1",
	        "joined":"2014-07-29 12:26:18",
	        "chn":"quiktripz",
	        "email":"kk@gmail.com",
	        "mFlg":"0",
	        "susbChn":"qd_356555059709553",
	        "listner":"qdl_356555059709553"
	    }

	}*/
	
	private String token ;
	private String expiryLocal;
	private String flag;
	private String joined;
	private String chn;
	private String email;
	private String mFlg;
	private String t,susbChn,listner;
	
	
	
	public String getSusbChn() {
		return susbChn;
	}
	public void setSusbChn(String susbChn) {
		this.susbChn = susbChn;
	}
	public String getListner() {
		return listner;
	}
	public void setListner(String listner) {
		this.listner = listner;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getExpiryLocal() {
		return expiryLocal;
	}
	public void setExpiryLocal(String expiryLocal) {
		this.expiryLocal = expiryLocal;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getJoined() {
		return joined;
	}
	public void setJoined(String joined) {
		this.joined = joined;
	}
	public String getChn() {
		return chn;
	}
	public void setChn(String chn) {
		this.chn = chn;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getmFlg() {
		return mFlg;
	}
	public void setmFlg(String mFlg) {
		this.mFlg = mFlg;
	}
	public String getT() {
		return t;
	}
	public void setT(String t) {
		this.t = t;
	}
	
}
