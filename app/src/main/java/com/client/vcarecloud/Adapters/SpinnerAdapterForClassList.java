package com.client.vcarecloud.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.client.vcarecloud.R;
import com.client.vcarecloud.models.GetClassList;


import java.util.List;

public class SpinnerAdapterForClassList extends ArrayAdapter<GetClassList> {

    private Context context;
    List<GetClassList> myObjs;
    LayoutInflater inflater;

    public SpinnerAdapterForClassList(Context context, int textViewResourceId, List<GetClassList> myObjs) {
        super(context, textViewResourceId,myObjs);
        this.context = context;
        this.myObjs = myObjs;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount(){
        return myObjs.size();
    }

    public GetClassList getItem(int position){
        return myObjs.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);

    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
        //TextView label = new TextView(context);
        //label.setText(myObjs.get(position).getClassName());
        //return label;
    }

    public View getCustomView(int position, View convertView, ViewGroup parent)
    {
        View view = inflater.inflate(R.layout.spinner_layout, parent, false);
        TextView main_text = view.findViewById(R.id.text);
        main_text.setText(myObjs.get(position).getClassName());

        return view;
    }
}
