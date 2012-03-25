package coffee.imsdroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SettingActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.setting);
		Spinner spAccount = (Spinner) this.findViewById(R.id.spinner_aaccount);
		String[] accounts = new String[]{"786792111", "24535193"};
		ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, accounts);
		aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spAccount.setAdapter(aa);
		
		spAccount.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
			}
		});
	}
}
