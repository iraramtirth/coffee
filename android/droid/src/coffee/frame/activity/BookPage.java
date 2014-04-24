package coffee.frame.activity;

import android.graphics.Bitmap;

/**
 * 
 * @author coffee <br>
 *         2014年4月23日下午1:31:18
 */
public class BookPage {
	private int index;
	private Bitmap pageBitmap;
	private String pageText;

	public BookPage(int index, Bitmap pageBitmap) {
		super();
		this.index = index;
		this.pageBitmap = pageBitmap;
	}

	//
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public Bitmap getPageBitmap() {
		return pageBitmap;
	}

	public void setPageBitmap(Bitmap pageBitmap) {
		this.pageBitmap = pageBitmap;
	}

	public String getPageText() {
		return pageText;
	}

	public void setPageText(String pageText) {
		this.pageText = pageText;
	}

	@Override
	public String toString() {
		return index + "";
	}

}
