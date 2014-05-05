package coffee.frame.book;

import android.graphics.Bitmap;

/**
 * 
 * @author coffee <br>
 *         2014年4月23日下午1:31:18
 */
public class BookPage {
	private int index;
	private int offsetFirst;
	private int offsetLast;
	private Bitmap pageBitmap;
	private String pageText;

	public BookPage(Bitmap pageBitmap) {
		super();
		this.pageBitmap = pageBitmap;
	}

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

	public int getOffsetFirst() {
		return offsetFirst;
	}

	public void setOffsetFirst(int offsetFirst) {
		this.offsetFirst = offsetFirst;
	}

	public int getOffsetLast() {
		return offsetLast;
	}

	public void setOffsetLast(int offsetLast) {
		this.offsetLast = offsetLast;
	}

	@Override
	public String toString() {
		return index + ", [" + offsetFirst + ",\t" + offsetLast + "]";
	}

}
