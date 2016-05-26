package com.app.taxisharingDriver.pojo;

public class ContactsAdapPojo  implements Comparable<ContactsAdapPojo>
{
	
	String contactName;
	String ph_numb;
	String email_addr;
	public ContactsAdapPojo(String contactName, String ph_numb,
			String email_addr) {
		super();
		this.contactName = contactName;
		this.ph_numb = ph_numb;
		this.email_addr = email_addr;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getPh_numb() {
		return ph_numb;
	}
	public void setPh_numb(String ph_numb) {
		this.ph_numb = ph_numb;
	}
	public String getEmail_addr() {
		return email_addr;
	}
	public void setEmail_addr(String email_addr) {
		this.email_addr = email_addr;
	}
	@Override
	public int compareTo(ContactsAdapPojo another) 
	{
		return contactName.compareTo(another.contactName);
	}
	
	

}
