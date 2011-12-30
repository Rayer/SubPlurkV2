package com.rayer.SubPlurkV2.bean;

import org.json.JSONObject;

import android.util.Log;

import com.rayer.util.databridge.JSONConverter;

/*
 * id: The unique user id.
nick_name: The unique nick_name of the user, for example amix.
display_name: The non-unique display name of the user, for example Amir S. Only set if it's non empty.
has_profile_image: If 1 then the user has a profile picture, otherwise the user should use the default.
avatar: Specifies what the latest avatar (profile picture) version is.
location: The user's location, a text string, for example Aarhus Denmark.
default_lang: The user's profile language.
date_of_birth: The user's birthday.
full_name: The user's full name, like Amir Salihefendic.
gender: 1 is male, 0 is female, 2 is not stating/other.
page_title: The profile title of the user.
karma: User's karma value.
recruited: How many friends has the user recruited.
relationship: Can be not_saying, single, married, divorced, engaged, in_relationship, complicated, widowed, unstable_relationship, open_relationship

 */
public class UserData {
	public UserData(JSONObject jsobj) {
		//Log.d("SubPlurkV2", "UserData = " + jsobj.toString());
		JSONConverter.extractFromJSON(UserData.class, this, jsobj);
	}
	
	public int uid;
	public String nick_name;
	public String display_name;
	public int has_profile_image;
	public int avatar;
	public String location;
	public String default_lang;
	public String date_of_birth;
	public String full_name;
	public int gender;
	public String page_title;
	public float karma;
	public int recuited;
	public String relationship;

	
	/**
	 * placeholder, will use more flexible method
	 * @return
	 */
	public String createAvatarUrlBig() {
		if(has_profile_image == 0)
			return "http://www.plurk.com/static/default_big.gif";
		
		if(avatar == -1 || avatar == 0)
			return "http://avatars.plurk.com/" + uid + "-big.jpg";
		
		return "http://avatars.plurk.com/" + uid + "-big" + avatar + ".jpg";
	}
	
	public String createAvatarUrlMedium() {
		if(has_profile_image == 0)
			return "http://www.plurk.com/static/default_medium.gif";
		
		if(avatar == -1 || avatar == 0)
			return "http://avatars.plurk.com/" + uid + "-medium.gif";
		
		return "http://avatars.plurk.com/" + uid + "-medium" + avatar + ".gif";
	}
	
	public String createAvatarUrlSmall() {
		if(has_profile_image == 0)
			return "http://www.plurk.com/static/default_small.gif";
		
		if(avatar <= 0)
			return "http://avatars.plurk.com/" + uid + "-small.gif";
		
		return "http://avatars.plurk.com/" + uid + "-small" + avatar + ".gif";
	}
}
