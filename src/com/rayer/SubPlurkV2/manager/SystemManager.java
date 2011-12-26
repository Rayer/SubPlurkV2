package com.rayer.SubPlurkV2.manager;

import android.content.Context;

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
	
	public void init(Context context) {
		plurkController = new PlurkController(context);
	}
	
	PlurkController plurkController;
	
	public PlurkController getPlurkCtrl() {
		return plurkController;
	}

}
