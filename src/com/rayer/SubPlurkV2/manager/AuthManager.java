package com.rayer.SubPlurkV2.manager;

import java.net.HttpURLConnection;
import java.net.URL;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.rayer.SubPlurkV2.AuthActivity;

public class AuthManager {	
	
	static final String PLURK_REQUEST_URL = "http://www.plurk.com/OAuth/request_token";
	static final String PLURK_AUTHORIZATION_URL = "http://www.plurk.com/m/authorize";
	static final String PLURK_ACCESS_URL = "http://www.plurk.com/OAuth/access_token";
	static final String PLURK_CALLBACK_URL = "subplurkv2:///";
	
	static final String PLURK_CONSUMER_KEY = "I1yKmAKMGkfJ";
	static final String PLURK_CONSUMER_SECRET = "ZliRvOGEXmdgSDP7utOMbSvUZ4CV8CPa";
	
	private OAuthConsumer mainConsumer;
	OAuthProvider mainProvider;
	AuthManager() {
		mainConsumer = new DefaultOAuthConsumer(PLURK_CONSUMER_KEY, PLURK_CONSUMER_SECRET); 
		mainProvider = new DefaultOAuthProvider(PLURK_REQUEST_URL, PLURK_ACCESS_URL, PLURK_AUTHORIZATION_URL);
	}
	
	public void setAccessToken(String token, String tokenSecret) {
		mainConsumer.setTokenWithSecret(token, tokenSecret);
		
	}
	
	public void aquireAccessToken(String verifier) throws Exception {
		Log.d("SubPlurkV2", "Token = " + mainConsumer.getToken() + " and secret = " + mainConsumer.getTokenSecret());
		mainProvider.retrieveAccessToken(mainConsumer, verifier);
		Log.d("SubPlurkV2", "Token = " + mainConsumer.getToken() + " and secret = " + mainConsumer.getTokenSecret());
	}

	public void attemptAuth(Context context) throws OAuthMessageSignerException, OAuthNotAuthorizedException, OAuthExpectationFailedException, OAuthCommunicationException {
		String url = mainProvider.retrieveRequestToken(mainConsumer, PLURK_CALLBACK_URL);
		Bundle bundle = new Bundle();
		bundle.putString("URI", url);
		Intent intent = new Intent();
		intent.putExtras(bundle);
		intent.setClass(context, AuthActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	/**
	 * @deprecated
	 * @return the mainConsumer
	 */
	public OAuthConsumer getMainConsumer() {
		return mainConsumer;
	}

}
