package com.client.vcarecloud;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

import com.client.vcarecloud.Adapters.ExpandableListAdapter;
import com.client.vcarecloud.models.MenuModel;

import java.util.List;

public class ExpandList extends ExpandableListView {
    private int currentSelection = 0;
    private int currentChildSelection = -1;
    ExpandableListAdapter expandableListAdapter;

    private List<MenuModel> listHeader;
    private Context context;

    public ExpandList(Context context) {
        super(context);
    }

    public ExpandList(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpandList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ExpandList(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setSelected(int groupPosition) {
        MenuModel headerModel = listHeader.get(groupPosition);

        if (!headerModel.isHasChild()) {
            MenuModel currentModel = listHeader.get(currentSelection);
            currentModel.setSelected(false);

//            if (currentChildSelection != -1) {
//                ChildModel childModel = listHeader.get(currentSelection)
//                        .getChildModelList().get(currentChildSelection);
//                childModel.setSelected(false);
//
//                currentChildSelection = -1;
//            }

            headerModel.setSelected(true);

            currentSelection = groupPosition;
            expandableListAdapter.notifyDataSetChanged();
        }
    }

    public void setSelected(int groupPosition, int childPosition) {

        MenuModel currentModel = listHeader.get(currentSelection);
        currentModel.setSelected(false);

//        if (currentChildSelection != -1) {
//            ChildModel currentChildModel = listHeader
//                    .get(currentSelection)
//                    .getChildModelList()
//                    .get(currentChildSelection);
//            currentChildModel.setSelected(false);
//        }

        currentSelection = groupPosition;
        currentChildSelection = childPosition;

//        ChildModel childModel = listHeader.get(groupPosition).getChildModelList().get(childPosition);
//        childModel.setSelected(true);
        expandableListAdapter.notifyDataSetChanged();
    }
}
