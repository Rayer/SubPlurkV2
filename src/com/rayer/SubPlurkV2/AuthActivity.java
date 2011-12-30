package com.rayer.SubPlurkV2;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.rayer.SubPlurkV2.manager.SystemManager;

public class AuthActivity extends Activity {
	WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String uri = getIntent().getStringExtra("URI");
		WebView webView = new WebView(this);
		webView.setWebViewClient(new AuthClient());
		
		webView.loadUrl(uri);
		setContentView(webView);
		webView.getSettings().setJavaScriptEnabled(true);
		

		//Log.d("GShare", "entered WeiboAuthActivity");
		
	}
	
	private class AuthClient extends WebViewClient {
		
		

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {


			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onPageFinished(WebView view, String url) {


			super.onPageFinished(view, url);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String urlString) {
			
			Log.d("SubPlurkV2", "url : " + urlString);
			if(urlString.contains("subplurkv2")) {
				Uri url = Uri.parse(urlString);
				String verifier = url.getQueryParameter("oauth_verifier");
				try {
					SystemManager.getInst().getPlurkCtrl().aquireAccessToken(verifier);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				finish();
			}


			return super.shouldOverrideUrlLoading(view, urlString);
		}
		
		
	}
	

}
