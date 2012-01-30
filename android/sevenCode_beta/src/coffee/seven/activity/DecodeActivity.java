package coffee.seven.activity;

import java.util.ArrayList;
import java.util.List;

import org.droid.util.lbs.LocationAsyncTask;

import android.location.Location;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import coffee.seven.R;
import coffee.seven.activity.base.BaseActivity;
import coffee.seven.activity.base.IActivity;

public class DecodeActivity extends BaseActivity{
	
	private Spinner mSpinner;
	private List<String> cityList = new ArrayList<String>(){/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		{
			add("正在定位...");
			add("北京");
			add("上海");
		}
	}; 
	
	protected void onCreate(Bundle bundle)
	  {
	    Bundle localBundle = new Bundle();
	    localBundle.putInt(IActivity.KEY_LAYOUT_RES, R.layout.decode);
	    super.onCreate(localBundle);
	    
	    mSpinner = (Spinner) this.findViewById(R.id.decode_city);
	    ArrayAdapter<String> arrAdapter = new ArrayAdapter<String>(this, 
	    		android.R.layout.simple_spinner_item, cityList);
	    arrAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    mSpinner.setAdapter(arrAdapter);
	    
	    LocationAsyncTask locationAsync = new LocationAsyncTask(){
	    	@Override
	    	protected void onPostExecute(Location location) {
	    		super.onPostExecute(location);
	    		int position = cityList.indexOf(getCity());
	    		if(position != -1){
	    			mSpinner.setSelection(position);
	    		}
	    	}
	    };
	    locationAsync.execute(this);
	  }
}
