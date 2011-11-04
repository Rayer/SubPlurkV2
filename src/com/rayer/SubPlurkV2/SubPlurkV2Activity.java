package com.rayer.SubPlurkV2;

import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SubPlurkV2Activity extends Activity {
	
	Button mBtn;
	static final String PLURK_REQUEST_URL = "http://www.plurk.com/OAuth/request_token";
	static final String PLURK_AUTHORIZATION_URL = "http://www.plurk.com/m/authorize";
	static final String PLURK_ACCESS_URL = "http://www.plurk.com/OAuth/access_token";
	static final String PLURK_CALLBACK_URL = "subplurkv2:///";
	
	static final String PLURK_CONSUMER_KEY = "I1yKmAKMGkfJ";
	static final String PLURK_CONSUMER_SECRET = "ZliRvOGEXmdgSDP7utOMbSvUZ4CV8CPa";
	

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mBtn = (Button) findViewById(R.id.auth_button);
        mBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				try {
					processButtonPushed();
				} catch (OAuthMessageSignerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OAuthNotAuthorizedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OAuthExpectationFailedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OAuthCommunicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}});
        
    }
    
	protected void processButtonPushed() throws OAuthMessageSignerException, OAuthNotAuthorizedException, OAuthExpectationFailedException, OAuthCommunicationException {
		CommonsHttpOAuthConsumer consumer = new CommonsHttpOAuthConsumer(  
				PLURK_CONSUMER_KEY, PLURK_CONSUMER_SECRET);  
		  
		OAuthProvider provider = new DefaultOAuthProvider(PLURK_REQUEST_URL, PLURK_ACCESS_URL, PLURK_AUTHORIZATION_URL);
		
		String url = provider.retrieveRequestToken(consumer, PLURK_CALLBACK_URL);
		Log.d("SubPlurkV2", "url : " + url);
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		startActivity(i);
		
		
		//HttpClient client = new DefaultHttpClient();  

	}

    

}