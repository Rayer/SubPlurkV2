package com.rayer.SubPlurkV2.manager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import com.rayer.SubPlurkV2.AuthActivity;
import com.rayer.SubPlurkV2.bean.IAvatarFetchable;
import com.rayer.SubPlurkV2.bean.PlurkScrap;
import com.rayer.SubPlurkV2.bean.UserData;
import com.rayer.util.provisioner.FileSystemResourceProvisioner;
import com.rayer.util.provisioner.InternetResourceProvisioner;
import com.rayer.util.provisioner.MemoryCacheResourceProvisioner;
import com.rayer.util.provisioner.ResourceProxy;
import com.rayer.util.stream.StreamUtil;

public class PlurkController {	
	
	static final String PLURK_REQUEST_URL = "http://www.plurk.com/OAuth/request_token";
	static final String PLURK_AUTHORIZATION_URL = "http://www.plurk.com/m/authorize";
	static final String PLURK_ACCESS_URL = "http://www.plurk.com/OAuth/access_token";
	static final String PLURK_CALLBACK_URL = "subplurkv2:///";
	
	static final String PLURK_CONSUMER_KEY = "I1yKmAKMGkfJ";
	static final String PLURK_CONSUMER_SECRET = "ZliRvOGEXmdgSDP7utOMbSvUZ4CV8CPa";
	
	static final String PLURK_ROOT_PREF = "com.rayer.subplurkv2.pref";
	static final String PLURK_AT_TOKEN_PREF = PLURK_ROOT_PREF + ".accesstoken";
	static final String PLURK_AT_SECRET_PREF = PLURK_ROOT_PREF + ".accesssecret";
	
	static final String PLURK_BASE_URL = "http://www.plurk.com";
	
	String accessToken;
	String accessSecret;
	
	private OAuthConsumer mainConsumer;
	OAuthProvider mainProvider;
	Context context;
	SharedPreferences sp;
	PlurkController(Context inContext) {
		initProvisioners();
		mainConsumer = new CommonsHttpOAuthConsumer(PLURK_CONSUMER_KEY, PLURK_CONSUMER_SECRET); 
		mainProvider = new CommonsHttpOAuthProvider(PLURK_REQUEST_URL, PLURK_ACCESS_URL, PLURK_AUTHORIZATION_URL);
		context = inContext;
		sp = inContext.getSharedPreferences(PLURK_ROOT_PREF, Context.MODE_PRIVATE);
		
		//處理AccessToken
		accessToken = sp.getString(PLURK_AT_TOKEN_PREF, "");
		accessSecret = sp.getString(PLURK_AT_SECRET_PREF, "");
		
		if(accessToken.equals("") == false)
			mainConsumer.setTokenWithSecret(accessToken, accessSecret);
		
	}
	
	public void setAccessToken(String token, String tokenSecret) {
		mainConsumer.setTokenWithSecret(token, tokenSecret);
		
	}
	
	public boolean isLoggedIn() {
		return accessToken.equals("") == false;
	}
	
	public void aquireAccessToken(String verifier) throws Exception {
		mainProvider.retrieveAccessToken(mainConsumer, verifier);
		accessToken = mainConsumer.getToken();
		accessSecret = mainConsumer.getTokenSecret();
		sp.edit().putString(PLURK_AT_TOKEN_PREF, accessToken).putString(PLURK_AT_SECRET_PREF, accessSecret).commit();
	}

	public void attemptAuth() throws OAuthMessageSignerException, OAuthNotAuthorizedException, OAuthExpectationFailedException, OAuthCommunicationException {
		String url = mainProvider.retrieveRequestToken(mainConsumer, PLURK_CALLBACK_URL);
		Bundle bundle = new Bundle();
		bundle.putString("URI", url);
		Intent intent = new Intent();
		intent.putExtras(bundle);
		intent.setClass(context, AuthActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
	
	public void logout() {
		accessToken = "";
		accessSecret = "";
		sp.edit().putString(PLURK_AT_TOKEN_PREF, accessToken).putString(PLURK_AT_SECRET_PREF, accessSecret).commit();
	}
	
	protected String createAvatarUrlBig(IAvatarFetchable obj) {
		if(obj.hasProfileImage() == 0)
			return "http://www.plurk.com/static/default_big.gif";
		
		if(obj.getAvatar() <= 0)
			return "http://avatars.plurk.com/" + obj.getUID() + "-big.jpg";
		
		return "http://avatars.plurk.com/" + obj.getUID() + "-big" + obj.getAvatar() + ".jpg";
	}
	
	protected String createAvatarUrlMedium(IAvatarFetchable obj) {
		if(obj.hasProfileImage() == 0)
			return "http://www.plurk.com/static/default_medium.gif";
		
		if(obj.getAvatar() <= 0)
			return "http://avatars.plurk.com/" + obj.getUID() + "-medium.gif";
		
		return "http://avatars.plurk.com/" + obj.getUID() + "-medium" + obj.getAvatar() + ".gif";
	}
	
	protected String createAvatarUrlSmall(IAvatarFetchable obj) {
		if(obj.hasProfileImage() == 0)
			return "http://www.plurk.com/static/default_small.gif";
		
		if(obj.getAvatar() <= 0)
			return "http://avatars.plurk.com/" + obj.getUID() + "-small.gif";
		
		return "http://avatars.plurk.com/" + obj.getUID() + "-small" + obj.getAvatar() + ".gif";
	}
	
	enum AVATAR_SIZE {
		SMALL,
		MEDIUM,
		BIG
	}
	
	//Cache的部分我還在想要怎麼寫....
	ArrayList<MemoryCacheResourceProvisioner<Bitmap, String> > avatarMCRPArray;// = ((MemoryCacheResourceProvisioner<Bitmap, String>[]){null, null, null});
	ArrayList<FileSystemResourceProvisioner<Bitmap, String> > avatarFSRPArray; //= {null, null, null};
	
	private void initProvisioners() {
		avatarMCRPArray = new ArrayList<MemoryCacheResourceProvisioner<Bitmap, String> >();

		String base_path = "/sdcard/.SubPlurkV2/cache/avatar";
		String[] FileCachePath = {base_path + "small", base_path + "medium", base_path + "big"};
		avatarFSRPArray = new ArrayList<FileSystemResourceProvisioner<Bitmap, String> >();
		
		for(int i = 0; i < AVATAR_SIZE.values().length; ++i) {
			avatarMCRPArray.add(new MemoryCacheResourceProvisioner<Bitmap, String>(){

				@Override
				public boolean clearAllCachedResource() {
					return false;
				}

				@Override
				public boolean destroyElement(Bitmap source) {
					source.recycle();
					return true;
				}});
			
			avatarFSRPArray.add(new FileSystemResourceProvisioner<Bitmap, String>(FileCachePath[i]){

				@Override
				public Bitmap formFromStream(InputStream in) {
					return BitmapFactory.decodeStream(in);
				}

				@Override
				public void writeToOutputStream(Bitmap target,
						FileOutputStream fo) {
					target.compress(CompressFormat.PNG, 75, fo);
				}});
		}
			
	};
	
	private MemoryCacheResourceProvisioner<Bitmap, String> getAvatarMCRP(AVATAR_SIZE size) {
		return avatarMCRPArray.get(size.ordinal());
	}
	
	private FileSystemResourceProvisioner<Bitmap, String> getAvatarFSRP(AVATAR_SIZE size) {
		return avatarFSRPArray.get(size.ordinal());
	}
	
	
	public ResourceProxy<Bitmap, String> getAvatarProxy(final IAvatarFetchable obj, final AVATAR_SIZE size) {
		ResourceProxy<Bitmap, String> ret = new ResourceProxy<Bitmap, String>(){

			@Override
			public String getIndentificator() {
				return "" + obj.getUID();
			}};
			
		ret.addProvisioner(getAvatarMCRP(size));
		ret.addProvisioner(getAvatarFSRP(size));
		ret.addProvisioner(new InternetResourceProvisioner<Bitmap, String>(){

			@Override
			public Bitmap formFromStream(InputStream is) {
				return BitmapFactory.decodeStream(is);
			}

			@Override
			public String getUrlAddress(String identificator) {
				//先寫的ugly一點，我懶的全部opt了 orz
				if(size == AVATAR_SIZE.BIG)
					return createAvatarUrlBig(obj);
				else if(size == AVATAR_SIZE.MEDIUM)
					return createAvatarUrlMedium(obj);
				
				return createAvatarUrlSmall(obj);
			}});
		
		return ret;
	}

	/**
	 * @deprecated
	 * @return the mainConsumer
	 */
	public OAuthConsumer getMainConsumer() {
		return mainConsumer;
	}
	
	/*
	 * 目前等待implement的清單
	 * Plurk data
A plurk and it's data
User data
A user and the data
How to render the avatar
Users
/APP/Users/update
/APP/Users/getKarmaStats
Real time notifications
/APP/Realtime/getUserChannel
Comet channel specification
Polling
/APP/Polling/getPlurks
/APP/Polling/getUnreadCount
Timeline
/APP/Timeline/getPlurk
/APP/Timeline/getPlurks
/APP/Timeline/getUnreadPlurks
/APP/Timeline/getPublicPlurks
/APP/Timeline/plurkAdd
/APP/Timeline/plurkDelete
/APP/Timeline/plurkEdit
/APP/Timeline/mutePlurks
/APP/Timeline/unmutePlurks
/APP/Timeline/favoritePlurks
/APP/Timeline/unfavoritePlurks
/APP/Timeline/replurk
/APP/Timeline/unreplurk
/APP/Timeline/markAsRead
/APP/Timeline/uploadPicture
Responses
/APP/Responses/get
/APP/Responses/responseAdd
/APP/Responses/responseDelete
Profile
/APP/Profile/getOwnProfile
/APP/Profile/getPublicProfile
Friends and fans
/APP/FriendsFans/getFriendsByOffset
/APP/FriendsFans/getFansByOffset
/APP/FriendsFans/getFollowingByOffset
/APP/FriendsFans/becomeFriend
/APP/FriendsFans/removeAsFriend
/APP/FriendsFans/becomeFan
/APP/FriendsFans/setFollowing
/APP/FriendsFans/getCompletion
Alerts
General data structures
/APP/Alerts/getActive
/APP/Alerts/getHistory
/APP/Alerts/addAsFan
/APP/Alerts/addAllAsFan
/APP/Alerts/addAllAsFriends
/APP/Alerts/addAsFriend
/APP/Alerts/denyFriendship
/APP/Alerts/removeNotification
Search
/APP/PlurkSearch/search
/APP/UserSearch/search
Emoticons
/APP/Emoticons/get
Blocks
/APP/Blocks/get
/APP/Blocks/block
/APP/Blocks/unblock
Cliques
/APP/Cliques/getCliques
/APP/Cliques/getClique
/APP/Cliques/createClique
/APP/Cliques/renameClique
/APP/Cliques/add
/APP/Cliques/remove
PlurkTop
/APP/PlurkTop/getCollections
/APP/PlurkTop/getPlurks
	 */
	
	//Profile
	public JSONObject getOwnProfileRaw() {
		return fetchData(PLURK_BASE_URL + "/APP/Profile/getOwnProfile");	
	}
	
	public UserData getOwnProfile() {
		try {
			return new UserData(getOwnProfileRaw().getJSONObject("user_info"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	//Timeline
	
	public List<PlurkScrap> getPlurks() {
		ArrayList<PlurkScrap> ret = new ArrayList<PlurkScrap>();
		JSONObject raw = getPlurksRaw();
		try {
			JSONArray array = raw.getJSONArray("plurks");
			for(int i = 0; i < array.length(); ++i) 
				ret.add(new PlurkScrap((JSONObject)array.get(i)));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ret;
	}
	
	public JSONObject getPlurksRaw() {
		return fetchData(PLURK_BASE_URL + "/APP/Timeline/getPlurks");
	}
	
	//Real time notifications
	/**
	 * @category RealTimeNotifications
	 *
	 *	Get instant notifications when there are new plurks and responses on a user's timeline. This is much more efficient and faster than polling so please use it!
	 *	This API works like this:
	 * 	A request is sent to /APP/Realtime/getUserChannel and in it you get an unique channel to the specified user's timeline
	 *	You do requests to this unqiue channel in order to get notifications
	 */
	public JSONObject getUserChannel() {
		return fetchData(PLURK_BASE_URL + "/APP/Realtime/getUserChannel");
	}
	
	protected JSONObject fetchData(String targetURL) {
		try {
			URL url = new URL(targetURL);  
			HttpURLConnection request = (HttpURLConnection) url.openConnection();  
			request.setDoOutput(true);  
			request.setRequestMethod("POST");
			mainConsumer.sign(request);
			request.connect();
			String context = StreamUtil.InputStreamToString(request.getInputStream());
			Log.d("SubPlurkV2", "" + context);
			return new JSONObject(context);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthMessageSignerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthExpectationFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthCommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
