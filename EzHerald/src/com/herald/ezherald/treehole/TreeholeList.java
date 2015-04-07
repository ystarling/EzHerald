package com.herald.ezherald.treehole;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONObject;

/**
 * Created by lenovo on 2015/3/4.
 */
public class TreeholeList extends ListView implements View.OnClickListener,AdapterView.OnItemClickListener {
    public TreeholeList(Context context) {
        super(context);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
    public Boolean addTreeholeList(JSONObject json)
    {

        return true;
    }
}
