package com.rayer.SubPlurkV2.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rayer.SubPlurkV2.R;
import com.rayer.SubPlurkV2.SubPlurkV2Activity;
import com.rayer.SubPlurkV2.bean.UserData;
import com.rayer.SubPlurkV2.manager.PlurkController;
import com.rayer.SubPlurkV2.manager.SystemManager;

public class UserDataFragment extends Fragment {

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LinearLayout ll = new LinearLayout(container.getContext());
		inflater.inflate(R.layout.user_view, ll);
		ImageView iv = (ImageView) ll.findViewById(R.id.avatar);
		TextView tv = (TextView) ll.findViewById(R.id.name);
		Button btn = (Button) ll.findViewById(R.id.logout);
		
		final PlurkController pc = SystemManager.getInst().getPlurkCtrl();
		UserData data = pc.getOwnProfile();
		
		tv.setText(data.nick_name);
		
		btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				pc.logout();
		       	Intent i = new Intent();
	        	i.setClass(UserDataFragment.this.getActivity(), SubPlurkV2Activity.class);
	        	i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
	        	startActivity(i);				
				UserDataFragment.this.getActivity().finish();
			}});
				
		return ll;
	}

}
