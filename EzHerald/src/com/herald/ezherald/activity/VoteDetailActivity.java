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
		 // 用于 保存点集数据 ，包括每条曲线的X，Y坐标
		 XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();//使用与柱状图
		 RangeCategorySeries series = new RangeCategorySeries("您当月的消费记录");// 括号内为图表底部的文字
		 for (int k = 0; k < minValues.length; k++) {
		  series.add(minValues[k], maxValues[k]);
		 }
		 dataset.addSeries(series.toXYSeries());
		 int[] colors = new int[] { Color.CYAN };// 青色蓝绿色
		 XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
		 setChartSettings(renderer, "标题", "x",
		   "y", 0.5, 12.5, 0, 150, Color.GRAY, Color.LTGRAY);// 画笔的颜色预先定义成浅灰色
		 
		 renderer.setBarSpacing(0.01);//设置间距
		 renderer.setXLabels(0);//设置 X 轴不显示数字（改用我们手动添加的文字标签））;//设置X轴显示的刻度标签的个数
		 renderer.setYLabels(15);// 设置合适的刻度，在轴上显示的数量是 MAX / labels
		 renderer.setMargins(new int[] { 30, 70, 10, 0 });//图形 4 边距 设置4边留白  设置图表的外边框
		 renderer.setYLabelsAlign(Align.RIGHT);//设置y轴显示的分列，默认是 Align.CENTER
		 renderer.setPanEnabled(true, false);//设置x方向可以滑动，y方向不可以滑动
		 renderer.setZoomEnabled(false,false);//设置x，y方向都不可以放大或缩小
		 SimpleSeriesRenderer r = renderer.getSeriesRendererAt(0);
		 r.setDisplayChartValues(true);//设置是否在主题上方显示值
		 r.setChartValuesTextSize(24);//柱体上方字的大小
		 r.setChartValuesSpacing(3);//柱体上方字的与柱体顶部的距离
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
