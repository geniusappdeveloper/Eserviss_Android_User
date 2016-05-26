package com.app.taxisharingDriver.pojo;

import com.google.gson.annotations.SerializedName;

public class CarTypeDetail
{
	@SerializedName("min_fare")
  private String minFare;
	@SerializedName("type_desc")
  private String typeDesc;
	@SerializedName("type_name")
	  private String typeName;
	@SerializedName("basefare")
  private String basefare;
	@SerializedName("price_per_km")
  private String pricePerKm;
	@SerializedName("price_per_min")
  private String pricePerMin;
	@SerializedName("max_size")
  private String maxSize;
	@SerializedName("type_id")
  private String typeId;
  
  
  
public String getMinFare() {
	return minFare;
}
public void setMinFare(String minFare) {
	this.minFare = minFare;
}
public String getTypeDesc() {
	return typeDesc;
}
public void setTypeDesc(String typeDesc) {
	this.typeDesc = typeDesc;
}
public String getTypeName() {
	return typeName;
}
public void setTypeName(String typeName) {
	this.typeName = typeName;
}
public String getBasefare() {
	return basefare;
}
public void setBasefare(String basefare) {
	this.basefare = basefare;
}
public String getPricePerKm() {
	return pricePerKm;
}
public void setPricePerKm(String pricePerKm) {
	this.pricePerKm = pricePerKm;
}
public String getPricePerMin() {
	return pricePerMin;
}
public void setPricePerMin(String pricePerMin) {
	this.pricePerMin = pricePerMin;
}
public String getMaxSize() {
	return maxSize;
}
public void setMaxSize(String maxSize) {
	this.maxSize = maxSize;
}
public String getTypeId() {
	return typeId;
}
public void setTypeId(String typeId) {
	this.typeId = typeId;
}
  
  
  
}
