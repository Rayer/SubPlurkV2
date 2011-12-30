package com.rayer.SubPlurkV2.manager;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
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

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.rayer.SubPlurkV2.AuthActivity;
import com.rayer.SubPlurkV2.bean.PlurkScrap;
import com.rayer.SubPlurkV2.bean.UserData;
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
		return null;
	}
	public UserData getOwnProfile() {
		
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
		try {
			//URL url = new URL(PLURK_BASE_URL + "/APP/Timeline/getPlurks"); 
			URI url = new URI(PLURK_BASE_URL + "/APP/Timeline/getPlurks");
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);
			mainConsumer.sign(post);
			HttpResponse res = client.execute(post);
			String context = StreamUtil.InputStreamToString(res.getEntity().getContent());
			
			
//			HttpURLConnection request = (HttpURLConnection) url.openConnection();  
//			request.setDoOutput(true);  
//			request.setRequestMethod("POST");
//			mainConsumer.sign(request);
//			request.connect();
//			String context = StreamUtil.InputStreamToString(request.getInputStream());
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
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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
		try {
			URL url = new URL(PLURK_BASE_URL + "/APP/Realtime/getUserChannel");  
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
