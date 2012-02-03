package coffee.seven.action;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.droid.util.lang.StringUtils;
import org.droid.util.sqlite.DbHelper;
import org.droid.util.sqlite.TSqliteUtils;

import android.app.Activity;
import android.util.Log;
import coffee.seven.SysConfig;
import coffee.seven.bean.GoodsBean;
import coffee.seven.bean.GoodsImageBean;
import coffee.seven.bean.GoodsInfoBean;
import coffee.seven.bean.OrderBean;
import coffee.seven.bean.SaleBean;
import coffee.seven.bean.Sales;
import coffee.seven.bean.VersionBean;
import coffee.seven.service.SubRemindService;
import coffee.seven.service.remote.IRemoteService;
import coffee.seven.service.remote.impl.MmbRemotelService;

/**
 * 数据库相关的业务层服务类
 * @author wangtao
 */
public class SaleService{
	
	
	public void executeSql(String sql){
		DbHelper helper = new DbHelper();
		helper.execSQL(sql);
		helper.close();
	}
	
	/**
	 * 查询需要通知的sale
	 */
	public List<SaleBean> getSaleNotifyList(){
		DbHelper helper = new DbHelper();
		List<SaleBean> saleList = helper.queryForList(SaleBean.class, null, "startTime");
		for(SaleBean sale : saleList){
			//添加观察者
			this.addSaleToObserver(sale);
		}
		helper.close();
		return saleList;
	}
	
	private void addSaleToObserver(SaleBean sale){
		sale.addObserver(SaleObserver.getInstance());
		sale.setChanged();
		sale.notifyObservers();
	}
	
	/**
	 *  查询所有活动
	 */
	public List<SaleBean> getSaleBaseListAll(){
		DbHelper helper = new DbHelper();
		//
		List<SaleBean> saleList = helper.queryForList(SaleBean.class, null, null);
		for(SaleBean sale : saleList){
			List<GoodsBean> goodsList = helper.queryForList(GoodsBean.class, "saleId=" + sale.getId(), null);
			sale.setGoodsList(goodsList);
			this.addSaleToObserver(sale);
		}
		helper.close();
		return saleList;
	}
	
	/**
	 *  查询当前正在进行的活动 
	 */
	public List<SaleBean> getSaleBaseListNow(){
		DbHelper helper = new DbHelper();
		//
		List<SaleBean> saleList = helper.queryForList(SaleBean.class, "startTime <= " + SysConfig.getNowTime(), "startTime");
		for(SaleBean sale : saleList){
			List<GoodsBean> goodsList = helper.queryForList(GoodsBean.class, "saleId=" + sale.getId(), null);
			sale.setGoodsList(goodsList);
			this.addSaleToObserver(sale);
		}
		helper.close();
		return saleList;
	}
	
	/**
	 *  查询下一期活动
	 *  即：未开启的活动
	 */
	public List<SaleBean> getSaleBaseListNext(){
		DbHelper helper = new DbHelper();
		//
		List<SaleBean> saleList = helper.queryForList(SaleBean.class, "startTime > " + SysConfig.getNowTime(), "startTime");
		for(SaleBean sale : saleList){
			List<GoodsBean> goodsList = helper.queryForList(GoodsBean.class, "saleId=" + sale.getId(), null);
			sale.setGoodsList(goodsList);
			this.addSaleToObserver(sale);
		}
		helper.close();
		return saleList;
	}
	
	/**
	 * 查询指定ID的详细信息 
	 * @param saleId
	 * @return ：仅包含 imageList 和 infoList
	 * 	如果没查询结果， 则返回null
	 */
	public SaleBean getSaleDetail(int saleId){
		//先从本地
		DbHelper helper = new DbHelper();
		//
		String sql = "select * from " + TSqliteUtils.getTableName(GoodsImageBean.class)
					+ " where saleId = " + saleId;
		List<GoodsImageBean> imageList =helper.queryForList(sql, GoodsImageBean.class);
		//
		sql = "select * from " + TSqliteUtils.getTableName(GoodsInfoBean.class)
				+ " where saleId = " + saleId;
		List<GoodsInfoBean> infoList = helper.queryForList(sql, GoodsInfoBean.class);
		SaleBean saleDetail = null;
		if(imageList.size() > 0 || infoList.size() > 0){
			saleDetail = new SaleBean();
			saleDetail.setId(saleId);
			if(imageList.size() > 0){
				saleDetail.setImageList(imageList);
			}
			if(infoList.size() > 0){
				saleDetail.setInfoList(infoList);
			}
		}
		//从远程
		if(saleDetail == null || saleDetail.getImageList().size() == 0
				|| saleDetail.getInfoList().size() == 0){
			//远程获取
			IRemoteService remoteService = new MmbRemotelService();
			saleDetail = remoteService.getSaleDetail(saleId);
			if(saleDetail != null){
				for(GoodsImageBean image : saleDetail.getImageList()){
					image.setSaleId(saleId);
					helper.insert(image);
				}
				//
				for(GoodsInfoBean info : saleDetail.getInfoList()){
					info.setSaleId(saleId);
					helper.insert(info);
				}
			}
		}
		helper.close();
		return saleDetail;
	}
	
	/**
	 * 更新商品剩余数量
	 */
	public void updateSaleRemainCount(SaleBean sale){
		if(sale == null){
			return;
		}
		String tableName = TSqliteUtils.getTableName(GoodsBean.class);
		DbHelper helper = new DbHelper();
		for(GoodsBean goods : sale.getGoodsList()){
			String sql = "update " + tableName + " set remainCount = " 
				+ goods.getRemainCount() + " where id = " + goods.getId();
			helper.execSQL(sql);
		}
		helper.close();
	}
	
	/**
	 * 【远程】获取最新的商品剩余量
	 */
	public List<SaleBean> getLastSaleRemainCount() {
		IRemoteService remoteService = new MmbRemotelService();
		List<SaleBean> saleList = remoteService.getSaleRemainCount();
		if (saleList == null) {
			saleList = new ArrayList<SaleBean>();
		}
		for (SaleBean sale : saleList) {
			this.addSaleToObserver(sale);
		}
		return saleList;
	}
	
	/**
	 * 	获取指定活动ID的全部剩余商品总数 
	 */
	public String getRemainCountText(SaleBean sale){
		int remainCount = 0;
		for(GoodsBean goods : sale.getGoodsList()){
			remainCount += goods.getRemainCount();
		}
		return remainCount + "";
	}
	
	//通过code获取goods信息
	public String getLinkNameByCode(String code){
		DbHelper helper = new DbHelper();
		//String sql = "select * from " + TSqliteUtils.getTableName(GoodsBean.class);
		int codeInt = StringUtils.toInt(code, -1);
		String result = null;
		if(codeInt != -1){
			GoodsBean goods = helper.queryForObject(GoodsBean.class, codeInt);
			if(goods != null){
				result = goods.getLinkName();
			}
		}else{
			Log.w("MMB", "不能将code" + code + "转化为int类型");
		}
		helper.close();
		return result;
	}
	 
	/**
	 * 该方法被SaleDetailActivity一秒钟调用一次
	 */
	private long[] arr = new long[4];
	// k:时间的字符串格式[yyyy-MM-dd HH:mm:ss]表示
	private static Map<Long, Date> timeMap = new HashMap<Long, Date>();
	
	public synchronized long[] getRemainTimeArray(SaleBean sale){
		//活动未开始
		if(sale.getStartTime() > SysConfig.getNowTime()){
			return getRemainStartTimeArray(sale);
		}else{
			return getRemainEndTimeArray(sale);
		}
	}
	//getRemainStartTimeArray 与 getRemainEndTimeArray 只能同时执行一个
	private Date startDate;
	private Date endDate;
	private long startTime;
	private long timeAbs; //时间差
	/**
	 * @return ： 获取距离活动【开始】的时间
	 */
	private long[] getRemainStartTimeArray(SaleBean sale){
		///////  \\\\\\\\\
		startTime= sale.getStartTime();
		
		startDate =  timeMap.get(startTime);
		if(startDate == null){
			startDate = new Date(startTime);
			timeMap.put(startTime, startDate);
		}
		//结束当前活动
		if(startDate.getTime() - SysConfig.getNowTime() <= 0){
			return null;
		}
		if(startDate != null){
			timeAbs = startDate.getTime() - SysConfig.getNowTime();
			//天
			arr[0] = timeAbs / (24*60*60*1000);
			//小时
			arr[1] = timeAbs/(60*60*1000) - arr[0]*24;
			//分钟
			arr[2] = (timeAbs/(60*1000)) - arr[0]*24*60 - arr[1]*60;
			//秒
			arr[3] = timeAbs/1000 - arr[0]*24*60*60 - arr[1]*60*60 - arr[2]*60; 
		}
		return arr;
	}
	
	/**
	 * @return ： 获取距离活动结束的时间
	 */
	private long[] getRemainEndTimeArray(SaleBean sale){
		endDate =  timeMap.get(sale.getEndTime());
		if(endDate == null){
 			endDate = new Date(sale.getEndTime());
			timeMap.put(sale.getEndTime(), endDate);
		}
		if(endDate != null){
			//结束当前活动
			if(endDate.getTime() - SysConfig.getNowTime() <= 0){
				return null;
			}
			timeAbs = endDate.getTime() - SysConfig.getNowTime();
			//天
			arr[0] = timeAbs / (24*60*60*1000);
			//小时
			arr[1] = timeAbs/(60*60*1000) - arr[0]*24;
			//分钟
			arr[2] = (timeAbs/(60*1000)) - arr[0]*24*60 - arr[1]*60;
			//秒
			arr[3] = timeAbs/1000 - arr[0]*24*60*60 - arr[1]*60*60 - arr[2]*60; 
		}
		return arr;
	}
	
	/**
	 * 获取表单提交时候 三个文本框的数据
	 * @ruturn : 如果没有数据则返回new String[]{null,null,null} 
	 */
	public String[] getOrderInputInfo(){
		DbHelper helper = new DbHelper();
		String sql = "select * from " + TSqliteUtils.getTableName(OrderBean.class) + " limit 1";
		OrderBean order = helper.queryForObject(sql, OrderBean.class);
		String[] str = new String[3];
		if(order != null){
			str[0] = order.getCustomerPhone();
			str[1] = order.getCustomerName();
			str[2] = order.getCustomerAddress();
		}
		helper.close();
		return str;
	}
	
	/**
	 * 校验
	 * 每个活动只能参加一次
	 * @return true : 没参加
	 * 		   false : 已经参加过一次了
	 */
	public boolean checkSaleUniqueInOrder(int saleId){
		DbHelper helper = new DbHelper();
		String sql = "select count(id) from " + TSqliteUtils.getTableName(OrderBean.class)
					+" where orderId is not null and saleId=" + saleId;
		String count = helper.queryForColumn(sql, String.class);
		helper.close();
		if(StringUtils.toInt(count, 0) >= 1){
			return false; //已经抢购过了
		}
		return true;
	}

	public List<OrderBean> getSaleOrdeList() {
		DbHelper helper = new DbHelper();
		List<OrderBean> lst = helper.queryForList(OrderBean.class, null, "orderCreateTime desc");
		helper.close();
		return lst;
	}
	
	/**
	 * 更新所有sale相关的信息
	 * saleBean
	 * goodsBean
	 * 但是不包括goodsImage goodsInfo
	 */
	public void updateSaleAll(Activity context){
		final IRemoteService remoteService = new MmbRemotelService();
		//查询当前正在进行的活动
		Sales sales = remoteService.getSaleUpdateTimeList(3);
		if(sales == null){ //设置活动时间
			return;
		}else{
			SubRemindService.nowTime = sales.getNowTime();
		}
		List<SaleBean> lst = sales.getSaleList();
		
		/////////////
		//lst.get(4).setLastUpdateTime(DateUtils.format(new Date()));
		
		DbHelper helper = new DbHelper();
		StringBuilder ids = new StringBuilder();
		StringBuilder idsToUpdate = new StringBuilder();
		//比较最后更新时间，判断需要更新的记录
		String saleTable =  TSqliteUtils.getTableName(SaleBean.class);
		for(SaleBean sale : lst){
			SaleBean tmp = helper.queryForObject(SaleBean.class, sale.getId());
			//如果不存在该ID的sale,  //或者最后更新时间不匹配，则sale数据不是最新的， 需要从服务器获取最新数据
			if(tmp == null ||
					tmp.getLastUpdateTime().compareTo(sale.getLastUpdateTime()) < 0){
				helper.delete(SaleBean.class, sale.getId());
				helper.insert(sale);
				idsToUpdate.append(sale.getId()).append(",");
			}
			ids.append(sale.getId()).append(",");
		}
		//从sale表中删除服务器中已经不存在的saleID
		if(ids.length() > 0){
			ids.deleteCharAt(ids.length() - 1);
			String sql = "delete from " + saleTable	+ " where id not in(" + ids + ")";
			helper.execSQL(sql); 
		}
		//更新idsToUpdate的记实录
		if(idsToUpdate.length() > 0){
			idsToUpdate.deleteCharAt(idsToUpdate.length() - 1);
			//【从远程】查询SaleBaseList信息
			List<SaleBean> refreshSaleList = remoteService.getSaleBaseList(idsToUpdate.toString());
			//////////////////////////////
			//refreshSaleList.get(0).setStartTime(SysConfig.getNowTime() + 1000 * 60 * 3);
			
			for(SaleBean sale : refreshSaleList){
				helper.update(sale);	//更新sale的基本信息
				//先删除, 该sale的相关商品信息
				this.deleteGoodsAll(sale.getId(), helper);
				for(GoodsBean goods : sale.getGoodsList()){
					goods.setSaleId(sale.getId());
					helper.insert(goods);//插入活动商品基本信息
					helper.execSQL("update " + TSqliteUtils.getTableName(SaleBean.class) 
							+ " set refresh = 1 where id = " + sale.getId());
				}
			}
		}
		helper.close();
	}
	
	/**
	 * 删除所有goods相关的
	 */
	private void deleteGoodsAll(int saleId, DbHelper helper){
		String sql = "select picLocal from " + TSqliteUtils.getTableName(SaleBean.class) 
					+ " where id = " + saleId;
		//删除【SaleBean】图片
		String picLocal = helper.queryForColumn(sql , String.class);
		deleteFile(picLocal);
		//删除【GoodsImageBean】图片
		sql = "select * from " + TSqliteUtils.getTableName(GoodsImageBean.class) + " where saleId = " + saleId;
		List<GoodsImageBean> goodsImageList = helper.queryForList(sql, GoodsImageBean.class);
		for(GoodsImageBean image : goodsImageList){
			//删除小图
			deleteFile(image.getUrlLocal());
			//删除大图
			deleteFile(image.getUrlBigLocal());
		}
		//从数据库中删除记录
		sql = "delete from " + TSqliteUtils.getTableName(GoodsBean.class) + " where saleId = " + saleId;
		helper.execSQL(sql);
		sql = "delete from " + TSqliteUtils.getTableName(GoodsImageBean.class) + " where saleId = " + saleId;
		helper.execSQL(sql);
		sql = "delete from " + TSqliteUtils.getTableName(GoodsInfoBean.class) + " where saleId = " + saleId;
		helper.execSQL(sql);
	}
	
	private void deleteFile(String filePath){
		if(filePath == null){
			return;
		}
		File file = new File(filePath);
		if(file != null && file.exists()){
			file.delete();
		}
	}
	
	/**
	 * 删除指定saleId的sale的相关的记录
	 * saleBean 
	 * 以及goodsBean - goodsImageBean - goodsInfoBean
	 */
	public void deleteSaleAll(int saleId){
		DbHelper helper = new DbHelper();
		this.deleteGoodsAll(saleId, helper);
		helper.delete(SaleBean.class, saleId);
		helper.close();
	}
	
	
	public enum ImageType{
		SALE_BASE_AD,			// 首页广告图
		SALE_DETAIL_SMALL,		// 详情页小图
		SALE_DETAIL_BIG,		// 详情页大图
		NONE
	}
	
	///////////////////////////////////////////////////////////////////////////
	/**
	 * 获取远程图片在本地的缓存文件
	 * @param netImageUrl
	 * @return :  如果不存在，则返回null
	 */
	public String getLocalImage( String netImageUrl, ImageType type){
		DbHelper helper = new DbHelper();
		String sql = null;
		//首页--广告图
		if(type == ImageType.SALE_BASE_AD){
			sql = "select picLocal from " + TSqliteUtils.getTableName(SaleBean.class)
				+ " where pic = '" + netImageUrl +"'";
		}
		//详情页--小图
		if(type == ImageType.SALE_DETAIL_SMALL){
			sql = "select urlLocal from " + TSqliteUtils.getTableName(GoodsImageBean.class)
					+ " where url = '" + netImageUrl +"'";
		}
		//详情页--大图
		if(type == ImageType.SALE_DETAIL_BIG){
			sql = "select urlBigLocal from " + TSqliteUtils.getTableName(GoodsImageBean.class)
					+ " where urlBig = '" + netImageUrl +"'";
		}
		String imgLocal = helper.queryForColumn(sql, String.class);
		helper.close();
		return imgLocal;
	}
	/**
	 * 保存sale详情页面  图片列表
	 * 保存本地图片
	 */
	public void setLocalImage(String netImageUrl, String localImageUrl, ImageType type){
		if(localImageUrl == null){
			return;
		}
		String sql = null;
		//广告图
		if(type == ImageType.SALE_BASE_AD){
			sql = "update " + TSqliteUtils.getTableName(SaleBean.class)
				+ " set piclocal = '"+ localImageUrl +"' where pic = '" + netImageUrl + "'";
		}
		//详情页--小图
		if(type == ImageType.SALE_DETAIL_SMALL){
			sql = "update " + TSqliteUtils.getTableName(GoodsImageBean.class)
				+ " set urlLocal = '"+ localImageUrl +"' where url = '" + netImageUrl + "'";
		}
		//大图
		if(type == ImageType.SALE_DETAIL_BIG){
			sql = "update " + TSqliteUtils.getTableName(GoodsImageBean.class)
				+ " set urlBigLocal = '" + localImageUrl + "' where urlBig = '" + netImageUrl + "'";
		}
		//
		DbHelper helper = new DbHelper();
		helper.execSQL(sql);
		helper.close();
	}
	
	/**
	 * 更新表单状态 
	 * @param orderId
	 * @param status
	 */
	public void updateOrderStatus(String orderId, String status){
		String tableName = TSqliteUtils.getTableName(OrderBean.class);
		String sql = "update " + tableName + 
				" set orderStatus = '"+status+"' " +
				" where orderId = '" + orderId +"'";
		DbHelper helper = new DbHelper();
		helper.execSQL(sql);
		helper.close();
	}

	/**
	 * 检测指定saleId的saleDetail是否需要重新加载
	 * @param saleId
	 * @return
	 */
	public int isRefreshSaleDetail(int saleId){
		String tableName = TSqliteUtils.getTableName(SaleBean.class);
		String sql = "select refresh from " + tableName + " where saleId = " + saleId;
		DbHelper helper = new DbHelper();
		Integer refresh = helper.queryForColumn(sql, Integer.class);
		helper.close();
		if(refresh == null){
			return 0;
		}
		return refresh;
	}
	
	/**
	 * 设置指定saleId的saleDetail[image,info]的是否需要重新加载
	 * @param saleId : saleId 
	 * @param refresh : 0 代表重新加载完成 
	 * @return
	 */
	public void setRefreshSaleDetail(int saleId, int refresh){
		String saleTable = TSqliteUtils.getTableName(SaleBean.class);
		String sql = "update " + saleTable + 
				" set refresh = " + refresh + " where saleId = " + saleId;
		DbHelper helper = new DbHelper();
		helper.execSQL(sql);
		helper.close();
	}

	/**
	 * 查询sale基本数据：
	 * 只包含saleBean 跟 goodsList
	 * @param saleId
	 * @return
	 */
	public SaleBean getSaleBase(int saleId) {
		SaleBean sale = null;
		DbHelper helper = new DbHelper();
		sale = helper.queryForObject(SaleBean.class, saleId);
		String sql = "select * from " + TSqliteUtils.getTableName(GoodsBean.class) + " where saleId="+saleId;
		List<GoodsBean> goodsList = helper.queryForList(sql, GoodsBean.class);
		sale.setGoodsList(goodsList);
		helper.close();
		return sale;
	}
	
	/**
	 * 是否需要去server端检测版本更新
	 * @return false ：  不需要。
	 * 		   true  : 需要
	 */
	public boolean requireCheckVersion(){
		String tableName = TSqliteUtils.getTableName(VersionBean.class);
		String sql = "select lastTime from " + tableName 
			+ " order by id desc limit 1";
		DbHelper helper = new DbHelper();
		Long lastTime = helper.queryForColumn(sql, Long.class);
		if(lastTime == null){
			lastTime = 0L;
		}//	1319183617390	1319183657531
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(sdf.format(new Date(lastTime)).equals(sdf.format(new Date(SysConfig.getNowTime())))){
			helper.close();
			return false;
		}
		//
		helper.execSQL("delete from " +tableName);
		helper.insert(new VersionBean(SysConfig.getNowTime()));
		helper.close();
		return true;
	}
	
	/**
	 * 查询该用户的订阅列表 
	 * k:活动的开始时间。 即 : 提醒时间
	 * v:sale列表
	 */
	public Map<Long,List<SaleBean>> getSubMap(){
		DbHelper helper = new DbHelper();
		List<SaleBean> subList = helper.queryForList(SaleBean.class, "isSub=1", null);
		Map<Long,List<SaleBean>> subMap = new HashMap<Long, List<SaleBean>>();
		for(SaleBean sub : subList){
			if(subMap.get(sub.getStartTime()) == null){
				subMap.put(sub.getStartTime(), new ArrayList<SaleBean>());
			}
			SaleBean sale = helper.queryForObject(SaleBean.class, sub.getId());
			subMap.get(sub.getStartTime()).add(sale);
		}
		helper.close();
		return subMap;
	}
	
	/**
	 * 查询该订阅列表活动列表 
	 */
	public List<SaleBean> getSubSaleList() {
		DbHelper helper = new DbHelper();
		List<SaleBean> subs = helper.queryForList(SaleBean.class,"isSub = 1", null);
		helper.close();
		return subs;
	}
	
	//添加订阅记录
	public void addSub(int saleId) {
		DbHelper helper = new DbHelper();
		String sql = "update " + TSqliteUtils.getTableName(SaleBean.class) 
		+" set isSub = 1 where id = " + saleId;
		helper.execSQL(sql);
		helper.close();
	}
	//取消订阅
	public void cancelSub(int saleId) {
		DbHelper helper = new DbHelper();
		//先删除该记录， 确保该记录的数据是最新的
		String sql = "update " + TSqliteUtils.getTableName(SaleBean.class) 
			+" set isSub = 0 where id = " + saleId;
		helper.execSQL(sql);
		helper.close();
	}

	public void deleteSub(List<SaleBean> sales) {
		DbHelper helper = new DbHelper();
		for(SaleBean sale : sales){
			String sql = "update " + TSqliteUtils.getTableName(SaleBean.class) 
					+ " set isSub = 0  where id = " + sale.getId(); 
			helper.execSQL(sql);
		}
		helper.close();
	}
}
