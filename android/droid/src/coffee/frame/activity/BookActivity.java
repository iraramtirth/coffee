package coffee.frame.activity;

import android.os.Bundle;
import coffee.frame.activity.base.BaseActivity;
import coffee.frame.view.BookPage;

public class BookActivity extends BaseActivity {

	 @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BookPage page = new BookPage(this);
		//
		setContentView(page);
	}

}
