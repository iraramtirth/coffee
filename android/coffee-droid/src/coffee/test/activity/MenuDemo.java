package coffee.test.activity;

import android.R.drawable;
import android.app.Activity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MenuDemo extends Activity {
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	
		TextView tv = new TextView(this);
		tv.setText("hello world");
		this.setContentView(tv);
		//注册上下文菜单
		registerForContextMenu(tv);
		
	}
	
	
	/**
	 * 单击Menu时; 系统调用当前的Activity的ononCreateOptionsMenu方法
	 * 并传入一个实现了Menu接口的对象供使用
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE,Menu.FIRST,1,"保存").setIcon(android.R.drawable.ic_menu_save);
		menu.add(Menu.NONE,Menu.FIRST+1,2,"删除").setIcon(drawable.ic_menu_delete);
		
		return super.onCreateOptionsMenu(menu);
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
		return super.onContextItemSelected(item);
	}


	// 创建上下文菜单
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.add("hello");
		menu.add("world");
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	// 上下文菜单选取事件
	public boolean onContextItemSelected(MenuItem item) {
		Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
		return super.onContextItemSelected(item);
	}

	
	
}






























