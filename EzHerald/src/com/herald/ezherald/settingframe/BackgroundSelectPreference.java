package com.herald.ezherald.settingframe;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.herald.ezherald.R;

import java.lang.reflect.Field;

/**
 * Created by xie on 6/29/2014.
 */
public class BackgroundSelectPreference extends DialogPreference{

    private ImageView[] images;
    private int choice;
    private TableLayout table;
    private int numberOfBackground=6;

    public BackgroundSelectPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setDialogLayoutResource(R.layout.background_select_preference);
        setPositiveButtonText("确定");
        setNegativeButtonText("取消");
        choice = 0;
    }



    @Override
    protected void onBindDialogView(View view) {
        Log.v("lllllll", "onBindDialogView");


        images = new ImageView[numberOfBackground];
        for(int i=0;i<images.length;i++) {
            images[i] = new ImageView(getContext());
            try {
                Field field = R.drawable.class.getField("menu_background_" + i);
                int resId = field.getInt(field);
                images[i].setImageResource(resId);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        table = (TableLayout)view.findViewById(R.id.table);
        //int imgSize= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,200,view.getDisplay().getMetrics(null));
        TableRow tr =null;
        for(int i=0;i<images.length;i++){

            if(i % 3 == 0) {
                tr = new TableRow(getContext());
                tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,0,1.0f));
            }
            images[i].setScaleType(ImageView.ScaleType.FIT_XY);


            TableRow.LayoutParams param = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT,1.0f);


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
                    persistInt(choice);
                    Toast.makeText(getContext(),"选择成功",Toast.LENGTH_SHORT).show();
                    getDialog().cancel();
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

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if(restorePersistedValue) {
            choice = this.getPersistedInt(0);
        } else {
            choice = (Integer)defaultValue;

        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInteger(index,0);
    }



}
