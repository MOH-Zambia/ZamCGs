package com.algorelpublic.zambia.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.algorelpublic.zambia.Interface.ICheckListener;
import com.algorelpublic.zambia.Interface.IGuidelineCheckListener;
import com.algorelpublic.zambia.Method.Favourite;
import com.algorelpublic.zambia.R;
import com.algorelpublic.zambia.Zambia;
import com.algorelpublic.zambia.activities.ZambiaMain;
import com.algorelpublic.zambia.fragments.GuidelineDetailsFragment;
import com.algorelpublic.zambia.model.GuidelineModel;
import com.algorelpublic.zambia.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by creater on 6/19/2017.
 */

public class AdapterGuideLine extends BaseExpandableListAdapter {

    Context context;
    ArrayList<GuidelineModel.Guideline> headerList;
    HashMap<GuidelineModel.Guideline, ArrayList<GuidelineModel.Guideline>> childList;

    private IGuidelineCheckListener mListener;

    public AdapterGuideLine(ArrayList<GuidelineModel.Guideline> mheaderList, HashMap<GuidelineModel.Guideline, ArrayList<GuidelineModel.Guideline>> mChilList, Context mContext) {
        this.headerList = mheaderList;
        this.childList = mChilList;
        this.context = mContext;
    }

    @Override
    public int getGroupCount() {
        return this.headerList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.childList.get(this.headerList.get(groupPosition)) == null ? 0 : this.childList.get(this.headerList.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.headerList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.childList.get(this.headerList.get(groupPosition))
                .get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }


    class ViewHolder {
        public CheckBox groupCheckBox;
        public CheckBox childCheckBox;
        public TextView tvName;

        public ViewHolder(View view) {
            groupCheckBox = (CheckBox) view.findViewById(R.id.group_chk_box);
            childCheckBox = (CheckBox) view.findViewById(R.id.group_chk_box);
            tvName = (TextView) view.findViewById(R.id.tvName);

        }
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.parent_layout, null);
        ViewHolder holder = new ViewHolder(convertView);
        holder.tvName.setText(headerList.get(groupPosition).contentHeading);
        holder.groupCheckBox.setChecked(headerList.get(groupPosition).isFavourite);

        holder.groupCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isExpanded)
                    mListener.expandGroupEvent(groupPosition, isExpanded);
                boolean state = headerList.get(groupPosition).isFavourite;

                headerList.get(groupPosition).isFavourite = (state ? false : true);
                if (headerList.get(groupPosition).isFavourite) {
                    mListener.onChangeListener(headerList.get(groupPosition), true);
                } else {
                    mListener.onChangeListener(headerList.get(groupPosition), false);
                }
                notifyDataSetChanged();

            }
        });

        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (headerList.get(groupPosition).subCategoryId.equalsIgnoreCase(""))
                    ((ZambiaMain) context).callFragmentWithReplace(R.id.container, GuidelineDetailsFragment.newInstance(headerList.get(groupPosition)), "");
                else
                    mListener.expandGroupEvent(groupPosition, isExpanded);

            }
        });
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.child_layout, null);
        ViewHolder holder = new ViewHolder(convertView);
        holder.tvName.setText(childList.get(headerList.get(groupPosition)).get(childPosition).subCategoryName);
//        holder.childCheckBox.setChecked(childList.get(headerList.get(groupPosition)).get(childPosition).isFavourite);
        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ZambiaMain) context).callFragmentWithReplace(R.id.container, GuidelineDetailsFragment.newInstance(headerList.get(groupPosition)), "");
            }
        });
//        holder.childCheckBox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                boolean state = childList.get(headerList.get(groupPosition)).get(childPosition).isFavourite;
//                childList.get(headerList.get(groupPosition)).get(childPosition).isFavourite = (state ? false : true);
//            }
//        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void setmListener(IGuidelineCheckListener listner) {
        this.mListener = listner;
    }
}

