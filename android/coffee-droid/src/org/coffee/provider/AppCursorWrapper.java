package org.coffee.provider;

import android.database.Cursor;
import android.database.CursorWrapper;

public class AppCursorWrapper extends CursorWrapper {

	public AppCursorWrapper(Cursor cursor) {
		super(cursor);
	}
	
}
