package coffee.frame.activity;

import java.util.ArrayList;
import java.util.List;

import org.coffee.R;
import org.coffee.util.lang.ObjectSerialize;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import coffee.frame.activity.base.BaseActivity;

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
		List<String> gameData = ObjectSerialize.read(ArrayList.class, String.class);
		this.mListVIew.setAdapter(new HistoryAdapter(gameData, this));
	}

	private class HistoryAdapter extends coffee.frame.adapter.base.BaseAdapter<String> {

		public HistoryAdapter(List<String> items, Activity mContext) {
			super(items, mContext);
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.game2048_history_item, parent, false);
			TextView dataView = (TextView) convertView.findViewById(R.id.history_text);
			View deleteView = convertView.findViewById(R.id.history_delete);
			View selectView = convertView.findViewById(R.id.history_select);
			String data = getItem(position);
			dataView.setText(formatData(data));
			deleteView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
//					ObjectSerialize.read(beanClass, id)
				}
			});
			selectView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent data = new Intent();
					data.putExtra("data", getItem(position));
					mContext.setResult(RESULT_OK, data);
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
