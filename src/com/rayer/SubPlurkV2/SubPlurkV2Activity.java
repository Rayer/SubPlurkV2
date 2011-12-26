package com.rayer.SubPlurkV2;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.rayer.SubPlurkV2.manager.PlurkController;
import com.rayer.SubPlurkV2.manager.SystemManager;

public class SubPlurkV2Activity extends Activity {
	
	Button mBtn;
	PlurkController mPC;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        SystemManager.getInst().init(this);
        
        mBtn = (Button) findViewById(R.id.auth_button);
        mPC = SystemManager.getInst().getPlurkCtrl();
        boolean isLoggin = mPC.isLoggedIn();
        mBtn.setText(isLoggin ? "Get Timeline" : "Login");
        mBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				processButtonPushed();

			}});
    }
    
	protected void processButtonPushed() {
		if(mPC.isLoggedIn() == false)
			try {
				mPC.attemptAuth();
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
		else
			mPC.getPlurks();
	}

    

}