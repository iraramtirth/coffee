package coffee.seven.activity.adapter;

import java.util.ArrayList;
import java.util.List;

import org.droid.browser.RUtils;
import org.droid.util.sqlite.DbHelper;
import org.droid.util.view.ViewUtils;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import coffee.seven.R;
import coffee.seven.bean.VoucherBean;
/*
 * @author coffee
 */
public class VoucherListAdapter extends BaseAdapter{
	private List<VoucherBean> voucherList;
	private Activity context;
	public VoucherListAdapter(Activity context){
		voucherList = new ArrayList<VoucherBean>();
		this.context = context;
		DbHelper db = new DbHelper();
		//查询顶级Voucher
		voucherList = db.queryForList(VoucherBean.class, null, "pid=0", null);
		for(VoucherBean voucher : voucherList){
			List<VoucherBean> children = new ArrayList<VoucherBean>();
			children = db.queryForList(VoucherBean.class, null, "pid="+voucher.getId(), null);
			voucher.setChildren(children);
		}
		db.close();
	}
	
	@Override
	public int getCount() {
		return voucherList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			convertView = context.getLayoutInflater()
					.inflate(R.layout.search_voucher_list_item, parent, false);
			holder = new ViewHolder();
			holder.id = (TextView) convertView.findViewById(R.id.voucher_id);
			holder.name = (TextView) convertView.findViewById(R.id.voucher_name);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		VoucherBean voucher = voucherList.get(position);
		holder.id.setText(voucher.getId() + "");
		holder.name.setText(voucher.getName());//购物券类别【美食】
		int i = 0;
		for(VoucherBean child : voucher.getChildren()){ 
			int resid = RUtils.getResId(context.getPackageName()+".R", "id", "voucher_name_"+i);
			//名称
			ViewUtils.setText(convertView, resid, child.getName());
			resid = RUtils.getResId(context.getPackageName()+".R", "id", "voucher_image_"+i);
			//ImageView image = (ImageView) convertView.findViewById(resid);
			//image.setImageURI(Uri.parse("file:///data/data/" + context.getPackageName() + "/" + child.getImgae()));
			i++;
		}
		return convertView;
	}
	
	static class ViewHolder{
		TextView id;			// 
		TextView name;			// 优惠券名称
		ImageView image;		// 优惠券图像
	}
}
