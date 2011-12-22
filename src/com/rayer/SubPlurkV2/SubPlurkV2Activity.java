package com.rayer.SubPlurkV2;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.rayer.SubPlurkV2.manager.SystemManager;
import com.rayer.util.stream.StreamUtil;

public class SubPlurkV2Activity extends Activity {
	
	Button mBtn;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        SystemManager.getInst().init();
        
        mBtn = (Button) findViewById(R.id.auth_button);
        mBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				processButtonPushed();

			}});
        
        //12-22 17:51:06.377: D/SubPlurkV2(7707): Token = U5GNYyH6wVGS and secret = aCtJ9XVgNTeIpigq3qxsLi70Sv0HbA6h

        SystemManager.getInst().getAuthManager().setAccessToken("U5GNYyH6wVGS", "aCtJ9XVgNTeIpigq3qxsLi70Sv0HbA6h");
        
        try {
			URL url = new URL("http://www.plurk.com/APP/Timeline/getPlurks");  
			HttpURLConnection request = (HttpURLConnection) url.openConnection();  
			request.setDoOutput(true);  
			request.setRequestMethod("POST");
			SystemManager.getInst().getAuthManager().getMainConsumer().sign(request);
			request.connect();
			String context = StreamUtil.InputStreamToString(request.getInputStream());
			Log.d("SubPlurkV2", "" + context);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
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
		}  

        
    }
    
	protected void processButtonPushed() {
		try {
			SystemManager.getInst().getAuthManager().attemptAuth(this);
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

	}

    

}