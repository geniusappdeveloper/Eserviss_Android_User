package com.app.taxisharingDriver.response;


public class AppointmentDetailPassenger 
{
	/*
	{
	    "errNum": "31",
	    "errFlag": "0",
	    "errMsg": "Got Bookings!",
	    "penCount": "0",
	    "refIndex": [
	        "15",
	        "18"
	    ],
	    "appointments": [
	        {
	            "date": "2014-07-01",
	            "appt": []
	        },
	        {
	            "date": "2014-07-02",
	            "appt": []
	        },
	        {
	            "date": "2014-07-03",
	            "appt": []
	        },
	        {
	            "date": "2014-07-04",
	            "appt": []
	        },
	        {
	            "date": "2014-07-05",
	            "appt": []
	        },
	        {
	            "date": "2014-07-06",
	            "appt": []
	        },
	        {
	            "date": "2014-07-07",
	            "appt": []
	        },
	        {
	            "date": "2014-07-08",
	            "appt": []
	        },
	        {
	            "date": "2014-07-09",
	            "appt": []
	        },
	        {
	            "date": "2014-07-10",
	            "appt": []
	        },
	        {
	            "date": "2014-07-11",
	            "appt": []
	        },
	        {
	            "date": "2014-07-12",
	            "appt": []
	        },
	        {
	            "date": "2014-07-13",
	            "appt": []
	        },
	        {
	            "date": "2014-07-14",
	            "appt": []
	        },
	        {
	            "date": "2014-07-15",
	            "appt": [
	                {
	                    "pPic": "imageThu07032014125142.jpeg",
	                    "email": "Test@roadyo.net",
	                    "statCode": "5",
	                    "status": "Driver is on the way.",
	                    "fname": "Test",
	                    "apntTime": "05:50 pm",
	                    "addrLine1": "49, Bellary Rd, Lakshmayya Layout, Vishveshvaraiah Nagar, Hebbal",
	                    "dropLine1": "730, 6th B Cross Rd, Koramangala 3 Block, Koramangala",
	                    "duration": "0",
	                    "distance": "0",
	                    "amount": "7"
	                },
	                {
	                    "pPic": "imageThu07032014125142.jpeg",
	                    "email": "Test@roadyo.net",
	                    "statCode": "5",
	                    "status": "Driver is on the way.",
	                    "fname": "Test",
	                    "apntTime": "05:50 pm",
	                    "addrLine1": "49, Bellary Rd, Lakshmayya Layout, Vishveshvaraiah Nagar, Hebbal",
	                    "dropLine1": "730, 6th B Cross Rd, Koramangala 3 Block, Koramangala",
	                    "duration": "0",
	                    "distance": "0",
	                    "amount": "7"
	                },
	                {
	                    "pPic": "imageThu07032014125142.jpeg",
	                    "email": "Test@roadyo.net",
	                    "statCode": "5",
	                    "status": "Driver is on the way.",
	                    "fname": "Test",
	                    "apntTime": "05:50 pm",
	                    "addrLine1": "49, Bellary Rd, Lakshmayya Layout, Vishveshvaraiah Nagar, Hebbal",
	                    "dropLine1": "730, 6th B Cross Rd, Koramangala 3 Block, Koramangala",
	                    "duration": "0",
	                    "distance": "0",
	                    "amount": "7"
	                },
	                {
	                    "pPic": "imageThu07032014125142.jpeg",
	                    "email": "Test@roadyo.net",
	                    "statCode": "5",
	                    "status": "Driver is on the way.",
	                    "fname": "Test",
	                    "apntTime": "05:50 pm",
	                    "addrLine1": "49, Bellary Rd, Lakshmayya Layout, Vishveshvaraiah Nagar, Hebbal",
	                    "dropLine1": "730, 6th B Cross Rd, Koramangala 3 Block, Koramangala",
	                    "duration": "0",
	                    "distance": "0",
	                    "amount": "7"
	                }
	            ]
	        },
	        {
	            "date": "2014-07-16",
	            "appt": []
	        },
	        {
	            "date": "2014-07-17",
	            "appt": []
	        },
	        {
	            "date": "2014-07-18",
	            "appt": [
	                {
	                    "pPic": "imageWed07162014152923.jpeg",
	                    "email": "nihar@mobifyi.com",
	                    "statCode": "5",
	                    "status": "Driver is on the way.",
	                    "fname": "nihar",
	                    "apntTime": "01:41 pm",
	                    "addrLine1": "100 Feet Rd, Indira Nagar III Stage, Binnamangala, Hoysala Nagar, Indira Nagar,Bangalore, Karnataka 560008",
	                    "dropLine1": "Old Airport Exit Rd, HAL Bangalore International Airport, HAL Airport Area, HAL,Bangalore, Karnataka 560017",
	                    "duration": "0",
	                    "distance": "0",
	                    "amount": "7"
	                }
	            ]
	        },
	        {
	            "date": "2014-07-19",
	            "appt": []
	        },
	        {
	            "date": "2014-07-20",
	            "appt": []
	        },
	        {
	            "date": "2014-07-21",
	            "appt": []
	        },
	        {
	            "date": "2014-07-22",
	            "appt": []
	        },
	        {
	            "date": "2014-07-23",
	            "appt": []
	        },
	        {
	            "date": "2014-07-24",
	            "appt": []
	        },
	        {
	            "date": "2014-07-25",
	            "appt": []
	        },
	        {
	            "date": "2014-07-26",
	            "appt": []
	        },
	        {
	            "date": "2014-07-27",
	            "appt": []
	        },
	        {
	            "date": "2014-07-28",
	            "appt": []
	        },
	        {
	            "date": "2014-07-29",
	            "appt": []
	        },
	        {
	            "date": "2014-07-30",
	            "appt": []
	        },
	        {
	            "date": "2014-07-31",
	            "appt": []
	        }
	    ]
	}*/
}
