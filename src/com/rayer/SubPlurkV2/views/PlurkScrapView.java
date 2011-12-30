package com.rayer.SubPlurkV2.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rayer.SubPlurkV2.R;
import com.rayer.SubPlurkV2.bean.PlurkScrap;

public class PlurkScrapView extends RelativeLayout {

	PlurkScrap targetScrap;
	TextView nameView;
	WebView webView;
	
	public PlurkScrapView(Context context, PlurkScrap scrap) {
		super(context);
		
		LayoutInflater li = LayoutInflater.from(context);
		li.inflate(R.layout.plurk_scrap, this);
		
		nameView = (TextView) findViewById(R.id.avatar_dummy);
		webView = (WebView) findViewById(R.id.plurk_content);
		
		initWithData(scrap);

	}
	
	public void initWithData(PlurkScrap scrap) {
		targetScrap = scrap;
		nameView.setText("" + scrap.user_id);
		//webView.loadData(scrap.content, "text/html", "UTF-8");
		webView.loadDataWithBaseURL(null, scrap.content, "text/html", "UTF-8", null);
	}

}
