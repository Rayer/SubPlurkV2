package com.rayer.SubPlurkV2.manager;

import android.content.Context;
import android.location.LocationManager;

//TODO: Consider put SystemManager into Application
//Due it may be access via service, and I don't want to make 'init' be invoked so many times.
public class SystemManager {
	
	private static SystemManager defInst = null;
	//private static boolean isInitialized = false;
	public static SystemManager getInst() {
		if(defInst == null) {
			defInst = new SystemManager();
			//isInitialized = false;
		}
		return defInst;
	}
	
	public SystemManager init(Context context) {
		plurkController = new PlurkController(context);
		//TODO: May not use this directly, wrap it or put it into some sub-controller?
		sharedLocationMgr = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		
		return defInst;
	}
	
	PlurkController plurkController;
	LocationManager sharedLocationMgr;
	
	public PlurkController getPlurkCtrl() {
		return plurkController;
	}

}
