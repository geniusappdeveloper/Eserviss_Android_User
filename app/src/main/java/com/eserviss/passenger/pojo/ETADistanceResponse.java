package com.eserviss.passenger.pojo;

import java.util.ArrayList;

public class ETADistanceResponse
{
	
	ArrayList<ETAResponse> routes;
	String status;

	public String getStatus() {
		return status;
	}

	public ArrayList<ETAResponse> getRoutes() {
		return routes;
	}
	

}
