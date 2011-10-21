package com.rayer.SubPlurkV2;

import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthServiceProvider;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SubPlurkV2Activity extends Activity {
	
	Button mBtn;
	//static OAuthServiceProvider mOAuth;
	static OAuthServiceProvider defaultProvider() {
		
		return new OAuthServiceProvider("http://www.plurk.com/OAuth/request_token", "http://www.plurk.com/m/authorize", "http://www.plurk.com/OAuth/access_token");
	}
	
	public OAuthAccessor defaultAccessor() {
	    String callbackUrl = "icecondor-android-app:///";
	    OAuthServiceProvider provider =  defaultProvider();
	    OAuthConsumer consumer = new OAuthConsumer(callbackUrl, 
	                            "I1yKmAKMGkfJ",
	                            "ZliRvOGEXmdgSDP7utOMbSvUZ4CV8CPa", 
	                            provider);
	    OAuthAccessor accessor = new OAuthAccessor(consumer);
	    return accessor;
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mBtn = (Button) findViewById(R.id.auth_button);
        mBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				processButtonPushed();
			}});
        
    }
    
    protected void processButtonPushed() {
    	
//    	DefaultHttpClient client = new DefaultHttpClient();
//    	HttpPost post = new HttpPost("http://www.plurk.com/OAuth/request_token");
//    	//HttpEntity entity = new HttpEntity();
//    	
//    	/*要塞這堆東西進去
//    	 	oauth_consumer_key
//			oauth_nonce
//			oauth_signature_method
//			oauth_signature
//			oauth_timestamp
//			oauth_version
//			oauth_callback
//    	 */
//    	HttpEntity entity;
//    	ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
//    	nvp.add(new BasicNameValuePair("oauth_consumer_key", "I1yKmAKMGkfJ"));
//    	nvp.add(new BasicNameValuePair("oauth_nonce", ""));
//    	nvp.add(new BasicNameValuePair("oauth_signature_method", "HMAC-SHA1"));
//    	nvp.add(new BasicNameValuePair("oauth_signature", ""));
//    	nvp.add(new BasicNameValuePair("oauth_timestamp", "" + System.currentTimeMillis()));
//    	nvp.add(new BasicNameValuePair("oauth_version", "1.0"));
//    	nvp.add(new BasicNameValuePair("oauth_callback", ""));
//    	try {
//			entity = new UrlEncodedFormEntity(nvp);
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
// 
    	
    	OAuthAccessor client = defaultAccessor();
    	Intent intent = new Intent(Intent.ACTION_VIEW);
    	intent.setData(
    	      Uri.parse(
    	           client.consumer.serviceProvider.userAuthorizationURL+
    	           "?oauth_token="+client.requestToken+
    	           "&oauth_callback="+client.consumer.callbackURL));
    	startActivity(intent);

    }
}