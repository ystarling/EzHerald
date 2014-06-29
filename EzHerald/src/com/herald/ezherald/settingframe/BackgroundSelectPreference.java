package com.herald.ezherald.settingframe;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.actionbarsherlock.internal.view.menu.ActionMenuView;
import com.herald.ezherald.R;

/**
 * Created by xie on 6/29/2014.
 */
public class BackgroundSelectPreference extends DialogPreference{

    private ImageView[] images;
    private int choice;
    public BackgroundSelectPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setDialogLayoutResource(R.layout.background_select_preference);
        setPositiveButtonText("确定");
        setNegativeButtonText("取消");
        choice = 0;
    }

    @Override
    protected void onBindDialogView(View view) {

        images = new ImageView[4];
        for(int i=0;i<images.length;i++) {
            images[i] = new ImageView(getContext());
        }
        images[0].setImageResource(R.drawable.menu_background_0);
        images[1].setImageResource(R.drawable.menu_background_1);
        images[2].setImageResource(R.drawable.menu_background_2);
        images[3].setImageResource(R.drawable.menu_background_3);

        TableLayout table = (TableLayout)view.findViewById(R.id.table);
        TableRow tr =null;
        for(int i=0;i<images.length;i++){

            if(i % 3 == 0) {
                tr = new TableRow(getContext());
                TableLayout.LayoutParams param = new TableLayout.LayoutParams();
                param.setMargins(10,0,0,0);
                tr.setLayoutParams(param);
            }
            images[i].setScaleType(ImageView.ScaleType.FIT_XY);
            TableRow.LayoutParams param = new TableRow.LayoutParams(200,200);
            images[i].setLayoutParams(param);
            tr.addView(images[i]);
            if(i % 3 == 2) {
                table.addView(tr);
                tr = null;
            }
            class onClickListener implements View.OnClickListener{
                private int id;
                public onClickListener(int id){
                    this.id = id;
                }
                @Override
                public void onClick(View v) {
                    choice = id;
                }
            }
            images[i].setOnClickListener(new onClickListener(i));
        }
        if( tr!=null){
           table.addView(tr);
        }
        super.onBindDialogView(view);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if(positiveResult) {
            persistInt(choice);
        }
    }
}
