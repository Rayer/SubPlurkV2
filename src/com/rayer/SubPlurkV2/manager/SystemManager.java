package com.rayer.SubPlurkV2.manager;

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
	
	public void init() {
		authManager = new AuthManager();
	}
	
	AuthManager authManager;
	public AuthManager getAuthManager() {
		return authManager;
	}

}
