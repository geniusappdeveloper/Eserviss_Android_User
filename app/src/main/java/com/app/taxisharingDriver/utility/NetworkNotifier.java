package com.app.taxisharingDriver.utility;

import android.location.Location;

public interface NetworkNotifier {
	
	void updatedInfo(String info);
	void locationUpdates(Location location);
	void locationFailed(String message);

}
