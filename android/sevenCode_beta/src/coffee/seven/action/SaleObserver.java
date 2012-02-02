package coffee.seven.action;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import coffee.seven.SysConfig;
import coffee.seven.bean.SaleBean;

/**
 * saleBean的观察者
 * 单例模式 
 * @author wangtao
 */
public class SaleObserver implements Observer{

	private static SaleObserver single;
	
	public static Map<Integer,String> scrollNotifyMap = new HashMap<Integer, String>(); 
	
	private SaleObserver(){
	}
	//
	public static SaleObserver getInstance(){
		if(single == null){
			single = new SaleObserver();
		}
		return single;
	}
	
	@Override
	public void update(Observable observable, Object data) {
		if(observable instanceof SaleBean){
			SaleBean sale = (SaleBean) observable;
			if(sale.getGoodsName() != null && !"".equals(sale.getGoodsName().trim())){
				//已开始
				String info = "";
				if(SysConfig.getNowTime() > sale.getStartTime()){
					info = "[正在抢购] " + sale.getGoodsName();
				}else{
					info = "[即将抢购] " + sale.getGoodsName();
				}
				scrollNotifyMap.put(sale.getId(),info);
			}
			if(sale.isEnd()){
				scrollNotifyMap.remove(sale.getId());
			}
//			SaleNowActivity.context.runOnUiThread(new Runnable() {
//				@Override
//				public void run() {
//					SaleNowActivity.context.refreshScrollText();
//					if(SaleNextActivity.context != null){
//						SaleNextActivity.context.refreshScrollText();
//					}
//				}
//			});
		}
	}

}
