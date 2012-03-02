package coffee.code.activity;



import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import coffee.seven.R;
import coffee.seven.activity.adapter.KeywordsGridAdapter;
import coffee.seven.activity.adapter.SaleGridAdapter;
import coffee.seven.activity.adapter.VoucherListAdapter;
import coffee.seven.activity.base.BaseActivity;
import coffee.seven.activity.base.IActivity;
import coffee.seven.service.remote.impl.RemoteService;
import coffee.util.view.ViewUtils;

/**
 * 搜索 
 * @author wangtao
 */
public class SearchMainActivity extends BaseActivity {
	private  SearchMainActivity context;
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		context = this;
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
		//查询
		Button searchBtn = (Button) this.findViewById(R.id.search_main_action);
		searchBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String searchText = ViewUtils.getText(context, R.id.search_main_input);
				System.out.println(searchText);
			}
		});
	}
	
}
