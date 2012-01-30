package coffee.seven.activity;

import static coffee.seven.action.SaleUtils.saleService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.ViewSwitcher;
import coffee.seven.R;
import coffee.seven.SysConfig;
import coffee.seven.action.Alert;
import coffee.seven.action.BitmapManager;
import coffee.seven.action.SaleUtils;
import coffee.seven.activity.adapter.SaleDetailsGalleryBigAdapter;
import coffee.seven.activity.adapter.SaleDetailsGallerySmallAdapter;
import coffee.seven.activity.base.BaseActivity;
import coffee.seven.activity.base.TabFlipperActivity;
import coffee.seven.bean.GoodsBean;
import coffee.seven.bean.GoodsInfoBean;
import coffee.seven.bean.SaleBean;

/**
 * @author wangtao
 */
public class SaleDetailsActivity extends BaseActivity{
	public final String TAG = SaleDetailsActivity.class.getSimpleName();
	
	private SaleDetailsActivity context = this;
	
	//gallery小图
	private SaleDetailsGallerySmallAdapter gallerySmallAdapter;
	private ViewFlipper imageFilpperSmall;
	//大图
	private SaleDetailsGalleryBigAdapter galleryBigAdapter;
	private ViewFlipper imageFilpperBig;
	//大小图切换器
	private ViewSwitcher imageSwitcher;
	//gallery下面的图片状态
	private LinearLayout imageStatusLayout;
	//当前位置 [图片的当前]
	private int currentPosition = 0;
	
	private SaleBean saleDetail;
	
	private SaleBean saleBase;
	private Spinner goodsSizeSpinner;
	private Map<String,String> goodsCodeMap;
	
	@Override
	protected void onCreate(Bundle mBundle) {
		//sale的基本信息
		saleBase = this.getIntent().getParcelableExtra(KEY_EXTRA_SALE);
		final String remainCount = this.getIntent().getExtras().getString("REMAIN_COUNT");
		
		Bundle bundle = new Bundle();
		bundle.putInt(KEY_LAYOUT_RES, R.layout.sale_detail);
		bundle.putString(KEY_TITLE_TEXT, saleBase.getGoodsName());
		super.onCreate(bundle);
		
		//Sale详细信息
		saleDetail = saleService.getSaleDetail(saleBase.getId());
		
		if(saleDetail == null || saleDetail.getImageList().size() == 0
				|| saleDetail.getInfoList().size() == 0){
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setCancelable(false);
			builder.setMessage("获取信息失败，请重试...");
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					TabFlipperActivity.saleNowActivity.show(null);				
				}
			});
			builder.show();
			return;
		}
		
		//设置剩余时间
		createSaleRemainTime(saleBase);
		
		//创建图片状态以及图片列表
		imageSwitcher = (ViewSwitcher) this.findViewById(R.id.sale_detail_switcher);
		this.createGoodsImageGallery();
		//价格
		TextView priceView = (TextView) this.findViewById(R.id.sale_price);
		priceView.setText(SaleUtils.getPrice(saleBase.getPrice()));
		//原始价格
//		PriceView oriPriceView = (PriceView) this.findViewById(R.id.sale_detail_oriprice);
//		oriPriceView.setText(SaleUtils.getOriPrice(saleBase.getOriPrice()));
		//剩余数量：
		TextView remainCountView = (TextView) this.findViewById(R.id.sale_detail_remainCount);
		remainCountView.setText(SaleUtils.getRemainCount(remainCount));
		//设置商品规格下拉列表
		goodsSizeSpinner= (Spinner) this.findViewById(R.id.sale_detail_goods_code);
	
		goodsCodeMap = this.createGoodsSizeSpinner(saleBase, goodsSizeSpinner);
		//立即抢购
		Button buyAction = (Button) this.findViewById(R.id.sale_detail_order_action_1);
		buyAction.setOnClickListener(onClickListener);
		//创建产品规格选项卡
		this.createSpceTabHost(saleDetail);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				((ScrollView)findViewById(R.id.scroll_layout)).scrollTo(0, 0);
			}
		}, 1000 * 1/100);
	}
	//立即抢购
	private View.OnClickListener onClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int saleCount = Integer.valueOf(saleService.getRemainCountText(saleBase));
			if(saleCount == 0){
				Alert.show(context, "抢购提示", new String[]{"这件商品已经被抢光了哦！\n下次早点来吧！\n欢迎您抢购其他商品"},
						new View.OnClickListener[]{null,null,null},
						 new Integer[]{null, R.drawable.alert_dialog_sure_selector, null});
				return;
			}
			Intent intent = new Intent();
			intent.setClass(context, OrderSubmitActivity.class);
			intent.putExtra(KEY_EXTRA_SALE, saleBase);
			if(goodsCodeMap.size() > 0){
				String linkName = goodsSizeSpinner.getSelectedItem().toString();
				String code = goodsCodeMap.get(linkName);
				intent.putExtra(KEY_EXTRA_GOODS_CODE, code);
			}
			TabFlipperActivity.saleNowActivity.show(LEVEL_3, intent, LEVEL_2, true);
		}
	};
	
	
	private Timer timer = new Timer();
	/**
	 *  设置时间 
	 *  每秒钟刷新一次
	 */
	private  void createSaleRemainTime(final SaleBean saleBase){
		// 天
		final TextView time0 = (TextView) this.findViewById(R.id.sale_remain_time_0);
		// 时
		final TextView time1 = (TextView) this.findViewById(R.id.sale_remain_time_1);
		// 分
		final TextView time2 = (TextView) this.findViewById(R.id.sale_remain_time_2);
		// 秒
		final TextView time3 = (TextView) this.findViewById(R.id.sale_remain_time_3);
		
		final Handler timerHandler = new Handler(){
			private long[] times;
			@Override
			public void handleMessage(Message msg) {
				times = (long[]) msg.obj;
				if(times != null){
					time0.setText(times[0] + "");
					time1.setText(times[1] + "");
					time2.setText(times[2] + "");
					time3.setText(times[3] + "");
				}
			}
		};
		//
		TimerTask task = new TimerTask() {
			private long[] times;
			@Override
			public void run() {
				if(SysConfig.ENABLE_TIMER){
					times = saleService.getRemainTimeArray(saleBase);
					Message msg = timerHandler.obtainMessage();
					msg.obj = times;
					timerHandler.sendMessage(msg);
				}
			}
		};
		timer.schedule(task, 0, 1000 * 1);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		BitmapManager.recycleAll();//释放内存
		if(this.timer != null){
			this.timer.cancel();
		}
	}
	
	/**
	 * 创建图片状态以及图片列表
	 */
	private void createGoodsImageGallery(){
		if(saleDetail == null){
			return;
		}
		BitmapManager.recycleAll();//先释放内存
		//显示图片状态
		this.createGalleryImageStuaus();
		//创建小图gallery
		this.createGallerySmall();
		//创建大图gallery
		this.createGalleryBig();
		//更新图片状态
		this.updateImageStatus();
		//设置图像高度
		imageSwitcher.getLayoutParams().height = getPx(SysConfig.GALLERY_SMALL_HEIGHT);
		//设置上下图 单击事件
		this.setNavConclick();
	}
	private void setNavConclick(){
		final ImageButton next = (ImageButton) this.findViewById(R.id.sale_detail_btn_next);
		final ImageButton pre = (ImageButton) this.findViewById(R.id.sale_detail_btn_pre);
		final ImageButton nextBig = (ImageButton) this.findViewById(R.id.sale_detail_btn_next_big);
		final ImageButton preBig = (ImageButton) this.findViewById(R.id.sale_detail_btn_pre_big);
		//
		View.OnClickListener clickListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(v == next || v == nextBig){
					currentPosition ++;
					//小图
					if(imageSwitcher.getDisplayedChild() == 0){
						imageFilpperSmall.showNext();
					}else{
						imageFilpperBig.showNext();
					}
				}
				if(v == pre || v == preBig){
					currentPosition --;
					//小图
					if(imageSwitcher.getDisplayedChild() == 0){
						imageFilpperSmall.showPrevious();
					}else{
						imageFilpperBig.showPrevious();
					}
				}
				updateImageStatus();
			}
		};
		next.setOnClickListener(clickListener);
		pre.setOnClickListener(clickListener);
		nextBig.setOnClickListener(clickListener);
		preBig.setOnClickListener(clickListener);
	}
	
	private void createGalleryImageStuaus(){
		int size = saleDetail.getImageList().size();
		//图片选中状态
		imageStatusLayout = (LinearLayout) this.findViewById(R.id.sale_detail_img_status_rlayout); 
		for(int i=0; i<size; i++){
			TextView view = new TextView(context);
			view.setBackgroundResource(R.drawable.sale_detail_img_status_0);
			imageStatusLayout.addView(view);
		}
	}
	
	/**
	 * 更新图片状态
	 * @param currentPosition: 当前位置， 也就是下一个要显示的位置
	 * @return : 
	 */
	private void updateImageStatus(){
		if(currentPosition < 0){
			currentPosition = this.gallerySmallAdapter.getCount() - 1;
		}else if(currentPosition >= this.gallerySmallAdapter.getCount()){
			currentPosition = 0;
		}
		//初始化
		for(int i=0; i<this.gallerySmallAdapter.getCount(); i++){
			imageStatusLayout.getChildAt(i).setBackgroundResource(R.drawable.sale_detail_img_status_0);
		}
		//设置为选中状态
		imageStatusLayout.getChildAt(currentPosition).setBackgroundResource(R.drawable.sale_detail_img_status_1);
	}
	
	//小图gallery
	private void createGallerySmall(){
		//图片列表画廊
		imageFilpperSmall = (ViewFlipper) this.findViewById(R.id.sale_detail_image_small);
		gallerySmallAdapter = new SaleDetailsGallerySmallAdapter(this, saleDetail);
		//添加
		for(int i=0; i<gallerySmallAdapter.getCount(); i++){
			imageFilpperSmall.addView(getImageSmallView(i), i);
		}
	}
	private View getImageSmallView(int position){
		View view = gallerySmallAdapter.getView(position, null, null);
		view.setOnClickListener(iamgeSmallOnclickListener);
		return view;
	}
	//小图单击事件
	private View.OnClickListener iamgeSmallOnclickListener =  new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			//放大
			imageSwitcher.setDisplayedChild(1);
			//设置大图高度
			imageSwitcher.getLayoutParams().height = getPx(SysConfig.GALLERY_BIG_HEIGHT);
			//设置显示的大图
			imageFilpperBig.setDisplayedChild(currentPosition);
		}
	};
	
	//大图 gallery
	private void createGalleryBig(){
		//图片列表画廊
		imageFilpperBig = (ViewFlipper) this.findViewById(R.id.sale_detail_image_big);
		galleryBigAdapter = new SaleDetailsGalleryBigAdapter(this, saleDetail);
		//添加
		for(int i=0; i<galleryBigAdapter.getCount(); i++){
			imageFilpperBig.addView(getImageBigView(i), i);
		}
	}
	private View getImageBigView(int position){
		View view = galleryBigAdapter.getView(position, null, null);
		view.setOnClickListener(iamgeBigOnclickListener);
		return view;
	}
	private View.OnClickListener iamgeBigOnclickListener =  new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			//返回到小图状态
			imageSwitcher.setDisplayedChild(0);
			//设置大图高度
			imageSwitcher.getLayoutParams().height = getPx(SysConfig.GALLERY_SMALL_HEIGHT);
			//设置显示的小图
			imageFilpperSmall.setDisplayedChild(currentPosition);
		}
	};
	
	
	/**
	 * 设置商品型号型号下拉列表
	 * @return 
	 * 返回一个k-v(商品linkName与code)的键值对; 详见 {@link GoodsBean}
	 */
	private Map<String,String> createGoodsSizeSpinner(SaleBean saleBase, Spinner goodsSizeSpinner){
		Map<String,String> goodsCodeMap = new HashMap<String, String>(); 
		//只有一种商品
		if(saleBase.getGoodsList().size() == 1){
//			this.findViewById(R.id.sale_detail_goods_code_label).setVisibility(View.GONE);
//			this.findViewById(R.id.sale_detail_goods_code).setVisibility(View.GONE);
//			ImageButton imageBtn = (ImageButton) this.findViewById(R.id.sale_detail_order_action);
//			TableRow.LayoutParams params = (TableRow.LayoutParams)imageBtn.getLayoutParams();
//			params.span = 3;
//			params.gravity = Gravity.CENTER_HORIZONTAL;
//			imageBtn.setLayoutParams(params);
			this.findViewById(R.id.sale_detail_row_action_1).setVisibility(View.GONE);
			this.findViewById(R.id.sale_detail_row_action_2).setVisibility(View.VISIBLE);
			//注册单击事件
			this.findViewById(R.id.sale_detail_order_action_2).setOnClickListener(onClickListener);
		}
		List<String> codeList = new ArrayList<String>();
		for(GoodsBean goods : saleBase.getGoodsList()){
			//如果改商品的剩余数量>0，则显示
			if(goods.getRemainCount() > 0){
				goodsCodeMap.put(goods.getLinkName(), goods.getId() + "");
				codeList.add(goods.getLinkName());
			}
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getApplicationContext(),android.R.layout.simple_spinner_item, codeList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		goodsSizeSpinner.setPrompt("商品尺码");
		goodsSizeSpinner.setAdapter(adapter);
		return goodsCodeMap;
	}
	/**
	 * 创建产品规格选项卡
	 */
	private void createSpceTabHost(SaleBean saleDetail){
		if(saleDetail == null){
			return;
		}
		//产品规格
		final TabHost tabHost = (TabHost) this.findViewById(android.R.id.tabhost);
		if(tabHost == null){
			return;
		}
		tabHost.setup(); //必须setup
		//如果不存在info，则添加一个空的tab, 否则会莫名其妙的报错
		//java.lang.NullPointerException at android.widget.TabHost.dispatchWindowFocusChanged(TabHost.java:321)
		if(saleDetail.getInfoList().size() == 0){
			TextView view = new TextView(context);
			tabHost.addTab(tabHost.newTabSpec("")
					.setContent(new TabFacroty(""))// 该ID必须位于layout里面
					.setIndicator(view));
			tabHost.getTabWidget().setVisibility(View.GONE);
			return;
		}
		for(GoodsInfoBean info : saleDetail.getInfoList()){
			TextView view = new TextView(context);
			view.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
			view.setText(info.getTitle());
			view.setGravity(Gravity.CENTER);
			view.setTextColor(context.getResources().getColor(R.color.sale_tab_default));
			view.setBackgroundResource(R.drawable.sale_detail_spec_selector);
			tabHost.addTab(tabHost.newTabSpec("")
					.setContent(new TabFacroty(info.getAlt()))// 该ID必须位于layout里面
					.setIndicator(view));
		}
		if(tabHost.getTabWidget().getChildCount() > 0){
			//设置默认选中
			TextView view = ((TextView) tabHost.getTabWidget().getChildAt(0));
			view.setTextColor(context.getResources().getColor(R.color.sale_tab_selected));
			//
			tabHost.setOnTabChangedListener(new OnTabChangeListener() {
				@Override
				public void onTabChanged(String tabId) {
					//初始化颜色
					for(int i = 0; i<tabHost.getTabWidget().getChildCount(); i++){
						TextView view = (TextView) tabHost.getTabWidget().getChildAt(i);
						view.setTextColor(context.getResources().getColor(R.color.sale_tab_default));
						view.setBackgroundResource(R.drawable.sale_detail_spec_selector);
					}
					//设置选中状态后的颜色
					TextView view = ((TextView)tabHost.getCurrentTabView());
					view.setTextColor(context.getResources().getColor(R.color.sale_tab_selected));
					view.setSelected(true);
				}
			});
		}
		tabHost.setCurrentTab(0);// 设置默认的TAB
	}
	
	/**
	 *  动态创建Tab内容 
	 * @author wangtao
	 */
	class TabFacroty  implements TabContentFactory{
		private String content;
		public TabFacroty(String content){
			this.content = content;
		}
		/**
		 * 动态创建TabContent
		 */
		@Override
		public View createTabContent(String tag) {
			WebView tabContent = new WebView(context);
			tabContent.loadDataWithBaseURL(null, content, "text/html", "UTF-8", null);
			//设置WebView背景
			tabContent.setBackgroundColor(Color.parseColor("#00000000"));
			//设置字体
			//tabContent.getSettings().setTextSize(WebSettings.TextSize.valueOf("90%"));
			tabContent.getSettings().setDefaultFontSize(14);
			
			tabContent.setWebViewClient(new WebViewClient(){
				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					//
					return true;
				}
			});
			return tabContent;
		}
	}
}
