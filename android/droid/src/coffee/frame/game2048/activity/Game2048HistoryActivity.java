package coffee.frame.game2048.activity;

import java.util.ArrayList;
import java.util.List;

import org.coffee.R;
import org.coffee.util.framework.Alert;
import org.coffee.util.lang.ObjectSerialize;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import coffee.frame.activity.base.BaseActivity;
import coffee.frame.game2048.bean.GridDataBean;
import coffee.utils.log.Log;

public class Game2048HistoryActivity extends BaseActivity {
	private ListView mListVIew;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void findViewById() {
		setContentView(R.layout.game2048_history);
		super.findViewById();
		setCommonTitle("游戏进度");
		this.mListVIew = (ListView) findViewById(R.id.listView);
		List<GridDataBean> datas = ObjectSerialize.read(ArrayList.class, GridDataBean.class);
		this.mListVIew.setAdapter(new HistoryAdapter(datas, this));
	}

	private class HistoryAdapter extends coffee.frame.adapter.base.BaseAdapter<GridDataBean> {

		public HistoryAdapter(List<GridDataBean> items, Activity mContext) {
			super(items, mContext);
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.game2048_history_item, parent, false);
			TextView valueView = (TextView) convertView.findViewById(R.id.history_text);
			View deleteView = convertView.findViewById(R.id.history_delete);
			View selectView = convertView.findViewById(R.id.history_select);
			final GridDataBean data = getItem(position);
			valueView.setText(formatData(data.getValue()));
			deleteView.setOnClickListener(new View.OnClickListener() {
				@SuppressWarnings("unchecked")
				@Override
				public void onClick(View v) {
					Alert.dialog(context, "确认删除", "删除后数据不可恢复,是否删除?", new Alert.Item("删除", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							ArrayList<GridDataBean> datas = ObjectSerialize.read(ArrayList.class, GridDataBean.class);
							for(GridDataBean item : datas){
								if(item.getValue().equals(data.getValue())){
									datas.remove(item);
									break;
								}
							}
							//先清空,然后重新写入
							for(GridDataBean item : datas){
								Log.d("JSON", item.getValue());
								Log.d("JSON", item.getJson());
							}
							//覆盖之前的数据
							ObjectSerialize.write(datas, GridDataBean.class);
							notifyData(datas, true);
						}
					}), new Alert.Item("取消", null));
				}
			});
			selectView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.putExtra("json", data.getJson());
					mContext.setResult(RESULT_OK, intent);
					mContext.finish();
				}
			});
			return convertView;
		}

		private String formatData(String data) {
			String[][] arr = new String[4][4];
			String[] cols = data.split(",;");
			for (int i = 0; i < arr.length; i++) {
				String[] cells = cols[i].split(",");
				for (int j = 0; j < arr[i].length; j++) {
					arr[i][j] = cells[j];
				}
			}
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < arr.length; i++) {
				for (int j = 0; j < arr[i].length; j++) {
					sb.append(arr[j][i]).append(" ");
				}
				sb.append("\n");
			}
			// Alert.toast(sb.toString());
			return sb.toString().trim();
		}
	}

}
