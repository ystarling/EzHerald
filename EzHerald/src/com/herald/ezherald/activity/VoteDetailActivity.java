package com.herald.ezherald.activity;

import org.achartengine.ChartFactory;
import org.achartengine.model.RangeCategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.graphics.Color;
import android.graphics.Paint.Align;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.view.View;
import org.achartengine.chart.BarChart.Type;

import com.actionbarsherlock.app.SherlockActivity;
import com.herald.ezherald.R;

public class VoteDetailActivity extends SherlockActivity {

	public VoteDetailActivity() {
		// TODO Auto-generated constructor stub
	}
	
	@Override 
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acti_vote_detail);
		
		 double[] minValues = new double[]{55,50,40,30,20,20,30,40,50,55};
		 double[] maxValues =new double[]{85,95,100,95,85,85,95,100,95,85};
		 // ���� ����㼯���� ������ÿ�����ߵ�X��Y����
		 XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();//ʹ������״ͼ
		 RangeCategorySeries series = new RangeCategorySeries("�����µ����Ѽ�¼");// ������Ϊͼ��ײ�������
		 for (int k = 0; k < minValues.length; k++) {
		  series.add(minValues[k], maxValues[k]);
		 }
		 dataset.addSeries(series.toXYSeries());
		 int[] colors = new int[] { Color.CYAN };// ��ɫ����ɫ
		 XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
		 setChartSettings(renderer, "����", "x",
		   "y", 0.5, 12.5, 0, 150, Color.GRAY, Color.LTGRAY);// ���ʵ���ɫԤ�ȶ����ǳ��ɫ
		 
		 renderer.setBarSpacing(0.01);//���ü��
		 renderer.setXLabels(0);//���� X �᲻��ʾ���֣����������ֶ���ӵ����ֱ�ǩ����;//����X����ʾ�Ŀ̶ȱ�ǩ�ĸ���
		 renderer.setYLabels(15);// ���ú��ʵĿ̶ȣ���������ʾ�������� MAX / labels
		 renderer.setMargins(new int[] { 30, 70, 10, 0 });//ͼ�� 4 �߾� ����4������  ����ͼ�����߿�
		 renderer.setYLabelsAlign(Align.RIGHT);//����y����ʾ�ķ��У�Ĭ���� Align.CENTER
		 renderer.setPanEnabled(true, false);//����x������Ի�����y���򲻿��Ի���
		 renderer.setZoomEnabled(false,false);//����x��y���򶼲����ԷŴ����С
		 SimpleSeriesRenderer r = renderer.getSeriesRendererAt(0);
		 r.setDisplayChartValues(true);//�����Ƿ��������Ϸ���ʾֵ
		 r.setChartValuesTextSize(24);//�����Ϸ��ֵĴ�С
		 r.setChartValuesSpacing(3);//�����Ϸ��ֵ������嶥���ľ���
		 r.setGradientEnabled(false);
		 r.setGradientStart(0, Color.BLUE);
		 r.setGradientStop(100, Color.GREEN);
		 //View mchartView = this.findViewById(R.id.acti_vote_detail_chart);
		 View mchartView =ChartFactory.getRangeBarChartView(VoteDetailActivity.this, dataset, renderer,
		   Type.DEFAULT);
		 setContentView(mchartView);
		 //setContentView(R.layout.acti_vote_detail);
	}
	
	  protected XYMultipleSeriesRenderer buildBarRenderer(int[] colors) {
	      XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
	      renderer.setAxisTitleTextSize(16);
	      renderer.setChartTitleTextSize(20);
	      renderer.setLabelsTextSize(15);
	      renderer.setLegendTextSize(15);
	      renderer.setOrientation(XYMultipleSeriesRenderer.Orientation.VERTICAL);
	      int length = colors.length;
	      for (int i = 0; i < length; i++) {
	        SimpleSeriesRenderer r = new SimpleSeriesRenderer();
	        r.setColor(colors[i]);
	        renderer.addSeriesRenderer(r);
	      }
	      return renderer;
	    }
	  
	  
	  protected void setChartSettings(XYMultipleSeriesRenderer renderer, String title, String xTitle,
	        String yTitle, double xMin, double xMax, double yMin, double yMax, int axesColor,
	        int labelsColor) {
	      renderer.setChartTitle(title);
	      renderer.setXTitle(xTitle);
	      renderer.setYTitle(yTitle);
	      renderer.setXAxisMin(xMin);
	      renderer.setXAxisMax(xMax);
	      renderer.setYAxisMin(yMin);
	      renderer.setYAxisMax(yMax);
	      renderer.setAxesColor(axesColor);
	      renderer.setLabelsColor(labelsColor);
	    }
	
	
	
	
	
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
