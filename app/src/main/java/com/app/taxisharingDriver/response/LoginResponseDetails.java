package com.app.taxisharingDriver.response;

public class LoginResponseDetails
{
	/*"token": "4131303030303NGxaQ8qUWroeC9UtMOOg337434533424333NGxaQ8qUWroeC9UtMOOg",
    "expiryLocal": "2014-08-27 02:19:43",
    "expiryGMT": "2014-08-27 01:19:43",
    "fname": "Nihar",
    "lname": "Ranjan",
    "profilePic": "DANihar2014200720282008204320578579temp_pic.jpg",
    "medicalLicenseNum": "",
    "flag": "1",
    "joined": "2014-07-28 08:43:34",
    "email": "annu@gmail.com",
    "susbChn": "qd_A1000037CE3BC3",
    "chn": "quiktripz",
    "listner": "qdl_A1000037CE3BC3"*/
	
	private String token;
	private String expiryLocal;
	private String expiryGMT;
	private String profilePic;
	private String flag;
	private String joined;
	private String chn;
	private String apiKey;
	private String email;
	private String fname;
	private String lname;
	private String susbChn;
	private String listner;
	private String vehTypeId;
	private String typeImage;
	

	
	public String getVehTypeId() {
		return vehTypeId;
	}
	public void setVehTypeId(String vehTypeId) {
		this.vehTypeId = vehTypeId;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getLname() {
		return lname;
	}
	public void setLname(String lname) {
		this.lname = lname;
	}
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
	public String getExpiryGMT() {
		return expiryGMT;
	}
	public void setExpiryGMT(String expiryGMT) {
		this.expiryGMT = expiryGMT;
	}
	public String getProfilePic() {
		return profilePic;
	}
	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
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
	public String getApiKey() {
		return apiKey;
	}
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getTypeImage() {
		return typeImage;
	}

	public void setTypeImage(String typeImage) {
		this.typeImage = typeImage;
	}

}
