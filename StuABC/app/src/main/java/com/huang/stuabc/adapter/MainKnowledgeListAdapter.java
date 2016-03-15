package com.huang.stuabc.adapter;

import java.util.ArrayList;

import com.huang.stuabc.MathLevel1Activity;
import com.huang.stuabc.R;
import com.huang.stuabc.constants.Constants;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class MainKnowledgeListAdapter extends BaseExpandableListAdapter {
	private LayoutInflater mInflater;
	private ArrayList<String> mGroupList ;
	private ArrayList<ArrayList<String>> mChildrenList ;
	private String mGrade;
	private Context mContext;
	private String mDbPath;
    private String mModule;
	public MainKnowledgeListAdapter(
			Context context,
			ArrayList<String> groupList,
			ArrayList<ArrayList<String>> childrenList,
			String dbPath,
			String module,
			String grade) {
		mInflater = LayoutInflater.from(context);
		mGroupList= groupList;
		mChildrenList=childrenList;
		mContext = context;
		mDbPath = dbPath;
		mModule = module;
		mGrade = grade;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return mChildrenList.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		MainChildExpandableHolder holder = null;

		if (null == convertView) {
			holder = new MainChildExpandableHolder();
			convertView = mInflater.inflate(R.layout.ex_child, null);
			holder.textChildCount= (TextView) convertView
					.findViewById(R.id.count);
			holder.txtChildValue = (TextView) convertView
					.findViewById(R.id.txtChildValue);
			holder.txtBeginTest2 = (TextView) convertView
					.findViewById(R.id.txtBeginTest2);
			
			convertView.setTag(holder);
		} else {
			
			holder = (MainChildExpandableHolder) convertView.getTag();
		}
		
		buildChildView(holder, groupPosition, childPosition);
		return convertView;
	}

	public void buildChildView(MainChildExpandableHolder holder,
			final int groupPosition, final int childPosition) {
		holder.textChildCount.setText(String.valueOf(childPosition+1));
		holder.txtChildValue.setText(mChildrenList.get(groupPosition).get(
				childPosition));
		holder.txtBeginTest2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mContext.startActivity(
						new Intent(mContext,MathLevel1Activity.class)
						.putExtra(Constants.EXTRA_DBFILEPATH, mDbPath)
						.putExtra(Constants.EXTRA_KEY3, mGroupList.get(groupPosition))
						.putExtra(Constants.EXTRA_KEY4, mChildrenList.get(groupPosition)
								.get(childPosition))
						.putExtra(Constants.EXTRA_GRADE, mGrade)	
						.putExtra(Constants.EXTRA_MODULE, mModule)
						);
			}
		});

	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return mChildrenList.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mGroupList.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return mGroupList.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		MainGroupExpandableHolder holder = null;
		if (null == convertView) {
			holder = new MainGroupExpandableHolder();
			convertView = mInflater.inflate(R.layout.ex_group, null);
			holder.showLevel2 = (TextView) convertView
					.findViewById(R.id.showLevel2);
			holder.txtGroupValue = (TextView) convertView
					.findViewById(R.id.txtGroupValue);
			holder.txtBeginTest1 = (TextView) convertView
					.findViewById(R.id.txtBeginTest1);
			convertView.setTag(holder);
		} else {
			holder = (MainGroupExpandableHolder) convertView.getTag();
		}
		
		buildGroupView(holder, groupPosition,isExpanded);

		return convertView;
	}

	public void buildGroupView(MainGroupExpandableHolder holder,
			final int groupPosition, boolean isExpanded) {
		if(isExpanded)
			holder.showLevel2.setBackgroundResource(R.drawable.list_close_arrow);
		else
			holder.showLevel2.setBackgroundResource(R.drawable.list_open_arrow);
		String text = mGroupList.get(groupPosition);
		holder.txtGroupValue.setText(text);

		holder.txtBeginTest1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mContext.startActivity(
						new Intent(mContext,MathLevel1Activity.class)
						.putExtra(Constants.EXTRA_DBFILEPATH, mDbPath)
						.putExtra(Constants.EXTRA_KEY3, mGroupList.get(groupPosition))
						.putExtra(Constants.EXTRA_KEY4,"")
						.putExtra(Constants.EXTRA_GRADE, mGrade)	
						.putExtra(Constants.EXTRA_MODULE, mModule)
						);
			}
		});
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
