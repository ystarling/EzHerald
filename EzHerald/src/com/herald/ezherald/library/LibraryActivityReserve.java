package com.herald.ezherald.library;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.herald.ezherald.R;

public class LibraryActivityReserve extends SherlockActivity{
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.library_activity_reserve);
		setTitle("�鼮ԤԼ");
		
		TextView libr_name=(TextView) this.findViewById(R.id.libr_book_Reserve_name);
		libr_name.setText("����/������:  "+"��֪��");
		TextView libr_press=(TextView) this.findViewById(R.id.libr_book_Reserve_press);
		libr_press.setText("���淢����:  "+"߀�ǲ�֪��");
		TextView libr_prize=(TextView) this.findViewById(R.id.libr_book_Reserve_prize);
		libr_prize.setText("ISBN������:  "+"ȷʵ��֪��");
		TextView libr_vector=(TextView) this.findViewById(R.id.libr_book_Reserve_vector);
		libr_vector.setText("����������:  "+"ȷʵ�治֪��");
		TextView libr_personal=(TextView) this.findViewById(R.id.libr_book_Reserve_personal);
		libr_personal.setText("ѧ������:  "+"ȷʵ�治֪��");
		TextView libr_sort=(TextView) this.findViewById(R.id.libr_book_Reserve_sort);
		libr_sort.setText("��ͼ�������:  "+"ȷʵ�治֪��");
		
	}
	
	 public class mAdapter extends BaseAdapter{
		private LayoutInflater inflater;
		public mAdapter(Context c){
			this.inflater=LayoutInflater.from(c);
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if(convertView==null){
				convertView=inflater.inflate(R.layout.library_mine_list_item, null);
				holder=new ViewHolder();
				holder.text1=(TextView) convertView.findViewById(R.id.libr_reserve_suoshuhao);
				holder.text2=(TextView) convertView.findViewById(R.id.libr_reserve_collection);
				holder.text3=(TextView) convertView.findViewById(R.id.libr_reserve_can_borrow);
				holder.text4=(TextView) convertView.findViewById(R.id.libr_reserve_exist);
				holder.text5=(TextView) convertView.findViewById(R.id.libr_reserve_reserved);
				holder.text6=(TextView) convertView.findViewById(R.id.libr_reserve_whether_borrow);
				convertView.setTag(holder);
			}else
			{
				holder=(ViewHolder) convertView.getTag();
			}
			
			holder.text1.setText(get_value().get(position).get("suoshuhao").toString());
			holder.text2.setText(get_value().get(position).get("collection").toString());
			holder.text3.setText(get_value().get(position).get("can_borrow").toString());
			holder.text4.setText(get_value().get(position).get("exist").toString());
			holder.text5.setText(get_value().get(position).get("reserved").toString());
			holder.text6.setText(get_value().get(position).get("whether_borrow").toString());

			
			
			return convertView;
		}
		 
		
		
		 
	 }
	 
		public class ViewHolder{
			public TextView text1;
			public TextView text2;
			public TextView text3;
			public TextView text4;
			public TextView text5;
			public TextView text6;

		}
		
		public List<HashMap<String,Object>> get_value(){
			
			ArrayList<HashMap<String,Object>> list=new ArrayList<HashMap<String,Object>>();
			HashMap<String,Object> map=new HashMap<String,Object>();
			for(int i=0;i<=2;i++){
			map.clear();
			map.put("suoshuhao", "����ţ� "+ "I247.5/2334");
			map.put("collection", "�ݲصأ� "+ "������");
			map.put("can_borrow", "�ɽ踴��: "+"3");
			map.put("exist", "�ڹݸ���: "+"0");
			map.put("reserved", "��ԤԼ��: "+"0");
			map.put("whether_borrow", "�ɷ�ԤԼ: "+"��");
			
			list.add(map);
			}
			return list;
			
		}
}
