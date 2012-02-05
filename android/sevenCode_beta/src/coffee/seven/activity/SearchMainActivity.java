package coffee.seven.activity;

import android.os.Bundle;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import coffee.seven.R;
import coffee.seven.activity.adapter.KeywordsGridAdapter;
import coffee.seven.activity.adapter.SaleGridAdapter;
import coffee.seven.activity.adapter.VoucherListAdapter;
import coffee.seven.activity.base.BaseActivity;
import coffee.seven.activity.base.IActivity;
import coffee.seven.service.remote.impl.RemoteService;

/**
 * 搜索 
 * @author wangtao
 */
public class SearchMainActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		Bundle localBundle = new Bundle();
		localBundle.putInt(IActivity.KEY_LAYOUT_RES, R.layout.search_main);
		super.onCreate(localBundle);
		RemoteService remoteService = new RemoteService();
		
		remoteService.queryKindAll();
		
		//类别
		GridView saleGridView = (GridView) this.findViewById(R.id.search_main_sale_grid);
		BaseAdapter saleGridAdapter = new SaleGridAdapter(this);
		saleGridView.setAdapter(saleGridAdapter);
		//热门搜索词
		GridView keywordsGridView = (GridView) this.findViewById(R.id.search_main_keywords_grid);
		BaseAdapter keywordsGridAdapter = new KeywordsGridAdapter(this);
		keywordsGridView.setAdapter(keywordsGridAdapter);
		//优惠券列表
		ListView voucherListView = (ListView) this.findViewById(R.id.search_main_voucher_list);
		BaseAdapter voucherListAdapter = new VoucherListAdapter(this);
		voucherListView.setAdapter(voucherListAdapter);
	}
	
}
