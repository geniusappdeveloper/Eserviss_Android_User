package com.app.taxisharingDriver.pojo;

import java.io.Serializable;

public class PubnubResponse implements Serializable
{
   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/* "dt":"2014-07-29 13:46:52",
    "bid":250,
    "a":11,
    "e":"abhishek@mobifyi.com",
    "nt":51*/
	private String dt;
	private String bid;
	private String a;
	private String e;
	private String nt;
	
	
	public String getNt() {
		return nt;
	}
	public void setNt(String nt) {
		this.nt = nt;
	}
	public String getDt() {
		return dt;
	}
	public void setDt(String dt) {
		this.dt = dt;
	}
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public String getA() {
		return a;
	}
	public void setA(String a) {
		this.a = a;
	}
	public String getE() {
		return e;
	}
	public void setE(String e) {
		this.e = e;
	}
	
	
}
