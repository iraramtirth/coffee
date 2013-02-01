package coffee.im.bluetooth.adapter;

import java.util.List;

import org.bluetooth.R;

import coffee.im.bluetooth.adapter.bean.DeviceInfoBean;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DeviceInfoAdapter extends BaseAdapter{

	private List<DeviceInfoBean> infoList;
	
	private Activity context;
	
	public DeviceInfoAdapter(Activity context, List<DeviceInfoBean> infoList){
		this.infoList = infoList;
		this.context = context;
	}
	
	public int getCount() {
		return infoList.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}
	
	  //设置配对状态
	public void setPireState(boolean isPair, String adderss){
		for (DeviceInfoBean info : infoList) {
			if(info.getDeviceAddress().equals(adderss)){
				info.setPair(isPair);
				break;
			}
		}
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = View.inflate(context, R.layout.device_item, null);
		}
		TextView infoView = (TextView) convertView.findViewById(R.id.device_info);
		
		infoView.setText(infoList.get(position).toString());
		context.registerForContextMenu(convertView);
		return convertView;
	}

}
