package com.rayer.SubPlurkV2.fragments;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.rayer.SubPlurkV2.bean.PlurkScrap;
import com.rayer.SubPlurkV2.manager.SystemManager;
import com.rayer.SubPlurkV2.views.PlurkScrapView;

public class EntirePlurkFragment extends Fragment {

	Context context;
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		context = container.getContext();
		ListView lv = new ListView(container.getContext());
		lv.setAdapter(new ScrapAdapter(SystemManager.getInst().getPlurkCtrl().getPlurks()));
		return lv;
	}
	
	public class ScrapAdapter extends BaseAdapter {

		List<PlurkScrap> plurkList;
		public ScrapAdapter(List<PlurkScrap> plurks) {
			plurkList = plurks;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return plurkList.size();
		}

		@Override
		public PlurkScrap getItem(int position) {
			// TODO Auto-generated method stub
			return plurkList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			PlurkScrapView view = (PlurkScrapView) convertView;
			if(view == null)
				view = new PlurkScrapView(context, getItem(position));
			else
				view.initWithData(getItem(position));
			return view;
		}

	}

}
