package com.client.vcarecloud.models;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anupamchugh on 22/12/17.
 */

public class MenuModel {
    public String menuName, url;
    int resource = -1;
    int indicatorResource = -1;
    public boolean hasChildren, isGroup;
    String title;
    boolean isNew = false;
    boolean isCartBudget = false;
    int cartBudgetTextColor = Color.WHITE;
    boolean hasChild = false;
    boolean isSelected = false;
    List<ChildModel> childModelList = new ArrayList<>();

    public MenuModel(String menuName, boolean isGroup, boolean hasChildren, String url) {
        this.menuName = menuName;
        this.url = url;
        this.isGroup = isGroup;
        this.hasChildren = hasChildren;
    }

    public MenuModel(String menuName, int resource, boolean isGroup,boolean hasChildren,boolean hasChild, boolean isSelected) {
        this.menuName = menuName;
        this.resource = resource;
        this.isGroup = isGroup;
        this.hasChildren = hasChildren;
        this.hasChild=hasChild;
        this.isSelected = isSelected;
    }

    public MenuModel(String menuName, int resource) {
        this.menuName = menuName;
        this.resource = resource;

    }
    public int getResource() {
        return resource;
    }
    public boolean isHasChild() {
        return hasChild;
    }
    public boolean isSelected() {
        return isSelected;
    }
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public List<ChildModel> getChildModelList() {
        return childModelList;
    }

    public void setChildModelList(List<ChildModel> childModelList) {
        this.childModelList = childModelList;
    }
    public class ChildModel {
        String title;
        boolean isSelected;

        public ChildModel(String title){
            this.title = title;
        }

        public ChildModel(String title, boolean isSelected){
            this.title = title;
            this.isSelected = isSelected;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }
    }
}
