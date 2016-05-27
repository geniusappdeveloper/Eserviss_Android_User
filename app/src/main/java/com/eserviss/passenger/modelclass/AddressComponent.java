package com.eserviss.passenger.modelclass;

import java.util.ArrayList;

public class AddressComponent
{
	
	/*{
    "results":[
               {
                   "address_components":[
                       {
                           "long_name":"54",
                           "short_name":"54",
                           "types":[
                               "premise"
                           ]
                       },
                       {
                           "long_name":"RBI Colony",
                           "short_name":"RBI Colony",
                           "types":[
                               "sublocality_level_2",
                               "sublocality",
                               "political"
                           ]
                       },
                       {
                           "long_name":"Hebbal",
                           "short_name":"Hebbal",
                           "types":[
                               "sublocality_level_1",
                               "sublocality",
                               "political"
                           ]
                       },
                       {
                           "long_name":"Bengaluru",
                           "short_name":"Bengaluru",
                           "types":[
                               "locality",
                               "political"
                           ]
                       },
                       {
                           "long_name":"Bangalore Urban",
                           "short_name":"Bangalore Urban",
                           "types":[
                               "administrative_area_level_2",
                               "political"
                           ]
                       },
                       {
                           "long_name":"Karnataka",
                           "short_name":"KA",
                           "types":[
                               "administrative_area_level_1",
                               "political"
                           ]
                       },
                       {
                           "long_name":"India",
                           "short_name":"IN",
                           "types":[
                               "country",
                               "political"
                           ]
                       },
                       {
                           "long_name":"560024",
                           "short_name":"560024",
                           "types":[
                               "postal_code"
                           ]
                       }
                   ],
                   "formatted_address":"54, RBI Colony, Hebbal, Bengaluru, Karnataka 560024, India",
                   "geometry":{},
                   "types":[]
               },
           ],
           "status":"OK"
       }*/
	
	ArrayList<ResultData> results;

	public ArrayList<ResultData> getResults() {
		return results;
	}

	public void setResults(ArrayList<ResultData> results) {
		this.results = results;
	}
	
}
