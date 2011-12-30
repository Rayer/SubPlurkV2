package com.rayer.SubPlurkV2.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rayer.SubPlurkV2.R;
import com.rayer.SubPlurkV2.bean.UserData;
import com.rayer.SubPlurkV2.manager.PlurkController;
import com.rayer.SubPlurkV2.manager.SystemManager;
import com.rayer.util.databridge.DebugBridge;

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
		
		PlurkController pc = SystemManager.getInst().getPlurkCtrl();
		UserData data = pc.getOwnProfile();
		
		tv.setText(data.nick_name);
		
				
		return ll;
	}

}
