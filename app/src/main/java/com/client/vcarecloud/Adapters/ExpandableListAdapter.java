package com.client.vcarecloud.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.client.vcarecloud.R;
import com.client.vcarecloud.models.MenuModel;

import java.util.HashMap;
import java.util.List;

/**
 * Created by anupamchugh on 22/12/17.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<MenuModel> listDataHeader;
    private HashMap<MenuModel, List<MenuModel>> listDataChild;

    public ExpandableListAdapter(Context context, List<MenuModel> listDataHeader,
                                 HashMap<MenuModel, List<MenuModel>> listChildData) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
    }

    @Override
    public MenuModel getChild(int groupPosition, int childPosititon) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = getChild(groupPosition, childPosition).menuName;

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChild = convertView
                .findViewById(R.id.lblListItem);

        txtListChild.setText(childText);

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        if (this.listDataChild.get(this.listDataHeader.get(groupPosition)) == null)
            return 0;
        else
            return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                    .size();
    }

    @Override
    public MenuModel getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        final MenuModel header = (MenuModel) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = convertView.findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText((header.menuName));

        ImageView ivGroupIndicator = convertView.findViewById(R.id.ivGroupIndicator);
        ImageView iconMenu = convertView.findViewById(R.id.icon_menu);

        if (header.getResource() != -1) {
            iconMenu.setBackgroundResource(header.getResource());
            iconMenu.setVisibility(View.VISIBLE);
        } else {
            iconMenu.setVisibility(View.GONE);
        }

        if (header.isHasChild()) {
            lblListHeader.setTypeface(null, Typeface.NORMAL);
            ivGroupIndicator.setVisibility(View.VISIBLE);
        } else {
            ivGroupIndicator.setVisibility(View.GONE);
            if (header.isSelected()) {
                lblListHeader.setTypeface(null, Typeface.BOLD);
            } else {
                lblListHeader.setTypeface(null, Typeface.NORMAL);
            }
        }

        if (isExpanded) {
            ivGroupIndicator.setImageResource(R.drawable.ic_remove);
        } else {
            ivGroupIndicator.setImageResource(R.drawable.ic_add);
        }
        return convertView;
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