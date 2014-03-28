/*
 * 文件名: URIUtils.java
 * 版    权：  Copyright Huawei Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: Maoah
 * 创建时间:2012-4-10
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package coffee.frame.imagescan;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import coffee.frame.App;

import com.util.log.Log;

/**
 * URI工具类
 * 
 * @author wangtaoyfx<br>
 *         2013下午5:19:42
 */
public class URIUtils {
	final String TAG = "URIUtils";

	/**
	 * 从uri获取图片路径
	 * 
	 * @param uri
	 *            图片的uri
	 * @return 图片本地路径
	 */
	public String getPathFromUri(Uri uri) {
		if (uri == null) {
			Log.e(TAG, "error:uri is null", null);
			return null;
		}

		String getPathFromUri = "";
		if (uri.getScheme().equals("file")) {
			getPathFromUri = uri.getPath();
		} else {
			// 查询数据库
			Cursor cursor = App
					.getContext()
					.getContentResolver()
					.query(uri, new String[] { MediaStore.Images.Media.DATA },
							null, null, null);
			if (cursor != null) {
				if (cursor.moveToFirst()) {
					// 获取数据库中的文件路径
					getPathFromUri = cursor.getString(cursor
							.getColumnIndex(MediaStore.Images.Media.DATA));
				}
				cursor.close();
			}
		}
		return getPathFromUri;
	}

}
