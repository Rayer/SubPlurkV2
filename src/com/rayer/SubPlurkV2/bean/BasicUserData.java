package com.rayer.SubPlurkV2.bean;

public class BasicUserData implements IAvatarFetchable {

	public String display_name;
	public int gender;
	public String nick_name;
	public int has_profile_image;
	public int id;
	public int avatar;
	@Override
	public int hasProfileImage() {
		return has_profile_image;
	}
	@Override
	public int getUID() {
		return id;
	}
	@Override
	public int getAvatar() {
		return avatar;
	}
	
}
