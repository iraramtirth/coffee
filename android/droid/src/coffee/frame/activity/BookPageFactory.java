package coffee.frame.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import org.coffee.util.lang.Reader;

import android.graphics.Canvas;
import coffee.frame.Config;
import coffee.utils.log.Log;

/**
 * 
 * @author coffee <br>
 *         2014年4月29日上午10:09:06
 */
public class BookPageFactory {
	/**
	 * 缓存路径
	 */
	private String path = Config.getBookDir();
	/**
	 * 书名。书籍缓存在new File(path, bookName)目录下
	 */
	// private String bookName = "";
	/**
	 * 当前页的岂止位移,根据这俩变量确定上、下两页的数据
	 */
	private int offsetFirst = 0;
	private int offsetLast = 0;

	/**
	 * Canvas的相关配置
	 */
	private BookConfig bookConfig;
	// java.nio相关
	private RandomAccessFile randomFile;
	/**
	 * 内存映射区域
	 */
	private MappedByteBuffer mbbuffer;
	private CharBuffer charBuffer;
	// 缓存的最大值 ,暂定位最大缓存10M
	private int maxSize = 10 * 1024 * 1024;
	// 图书的最大偏移量,用于判断翻页是否结束
	private int maxOffset;

	public BookPageFactory(String bookName, BookConfig bookConfig) {
		// this.bookName = bookName;
		this.bookConfig = bookConfig;

		try {
			// 书名。书籍缓存在new File(path, bookName)目录下
			File bookFile = new File(path, bookName);
			Reader reader = new Reader(bookFile.getPath());
			String bookContent = reader.readAll();
			reader.close();
			maxOffset = bookContent.length();
			File bookBuffer = new File(path, bookName + ".buffer");
			randomFile = new RandomAccessFile(bookBuffer, "rw");
			// 最大支持10M
			mbbuffer = randomFile.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, maxSize);
			// 把全部文件内容写入内存映射文件
			mbbuffer.asCharBuffer().put(bookContent);
			charBuffer = mbbuffer.asCharBuffer();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// private Bitmap[] pages;
	// /**
	// * 0 不操作<br>
	// * 1 向后翻页<br>
	// * -1向前翻页<br>
	// */
	// private int action;
	//
	// /**
	// * 更新缓存<br>
	// *
	// * @param action
	// * 当前操作
	// * @param pages
	// * prePageBitmap curPageBitmap nextPageBitmap
	// *
	// */
	// public void updateCache(int action, Bitmap[] pages) {
	// this.action = action;
	// this.pages = pages;
	//
	// if (action > 0) {
	// Bitmap bmp = pages[0];
	// pages[0] = pages[1];
	// pages[1] = pages[2];
	// pages[2] = bmp;
	// updatePage(pages[2], true);
	// } else if (action < 0) {
	// Bitmap bmp = pages[2];
	// pages[2] = pages[1];
	// pages[1] = pages[0];
	// pages[0] = bmp;
	// updatePage(pages[0], false);
	// } else {
	// // action == 0
	// }
	// }

	/**
	 * 
	 * @param offset
	 * @param isNext
	 *            是否绘制下一页数据
	 */
	public void updatePage(BookPage bookPage, int offset, boolean isNext) {
		// 每行显示的字数
		int lineFontNums = bookConfig.getLineFontNum();
		Canvas canvas = new Canvas(bookPage.getPageBitmap());
		canvas.drawColor(bookConfig.getBackColor());
		Log.d("bookFactory", " -  -  - - - - -  - - - - - - - - - - - - - ");
		this.offsetFirst = offset;
		this.offsetLast = offset;
		if (isNext) {
			float y = bookConfig.getMarginHeight();
			StringBuilder line = new StringBuilder();
			while (true) {
				line.setLength(0);
				y += bookConfig.getFontSize();
				if (y > bookConfig.getMarginHeight() + bookConfig.getVisibleHeight()) {
					break;
				}
				for (int i = 0; i < lineFontNums; i++) {
					char ch = charBuffer.get(offsetLast);
					line.append(ch);
					// 移动下标
					offsetLast = offsetLast + 1;
					charBuffer.position(offsetLast);
					if ("\n".equals(ch)) {
						break;
					}
				}
				Log.d("bookFactory-line-next", line);
				canvas.drawText(line.toString(), bookConfig.getMarginWidth(), y, bookConfig.getmPaint());
			}
			Log.d("bookFactory-pointer-next", offsetFirst + "," + offsetLast + "\n");
		} else {
			// 当前是第一页,前一页已经木有数据了
			if (offsetFirst == 0 && offsetLast == 0) {
				// 此时pages数组的位移情况大概为 [0,160] [160,320] [320-480]
				// 绘制结束以后为[0,0] [0,160] [160,320]
			} else {
				// 因为第一次循环会减去bookConfig.getFontSize()。所以初始值先加上
				float y = bookConfig.getMarginHeight() + bookConfig.getVisibleHeight();
				StringBuilder line = new StringBuilder();
				while (true) {
					line.setLength(0);
					if (y < bookConfig.getFontSize() + bookConfig.getMarginHeight()) {
						break;
					}
					for (int i = 0; i < lineFontNums; i++) {
						char ch = charBuffer.get(offsetFirst - 1);
						line.insert(0, ch);
						offsetFirst = offsetFirst - 1;
						charBuffer.position(offsetFirst);
						if ("\n".equals(ch)) {
							break;
						}
					}
					y -= bookConfig.getFontSize();
					Log.d("bookFactory-line-pre", line);
					canvas.drawText(line.toString(), bookConfig.getMarginWidth(), y, bookConfig.getmPaint());
				}
				Log.d("bookFactory-pointer-pre", offsetFirst + "," + offsetLast + "\n");
			}
		}
		//
		bookPage.setOffsetFirst(offsetFirst);
		bookPage.setOffsetLast(offsetLast);
	}

	/**
	 * 返回当前图书内容的最大偏移量, 即图片内容长度
	 * 
	 * @return
	 */
	public int getMaxOffset() {
		return this.maxOffset;
	}
}
