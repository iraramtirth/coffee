package coffee.browser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import coffee.util.lang.MathUtils;
import coffee.util.window.TitleUtils;

import android.app.Activity;

/**
 * 访问URL的进度条 BrowserUtils继承与httpClient， 主要增加了进度条的显示
 * 
 * @author wangtao
 */
public class BrowserUtils extends HttpClient {


	/**
	 * 开始访问UEL
	 */
	public static String get(Activity context, String linkUrl) {
		TitleUtils.setTitleProgressShow(context);//
		doc.delete(0, doc.length());
		BufferedReader in = null;
		try {
			URL url = new URL(linkUrl);
			HttpURLConnection uc = (HttpURLConnection) url.openConnection();
			uc.setRequestProperty("user-agent", IPHONE_USERAGENT);
			// uc.setConnectTimeout(1000 * 3);
			in = new BufferedReader(new InputStreamReader(uc.getInputStream(),
					encode));
			int fileLen = uc.getContentLength(); // 远程文件的总大小，单位 b
			if (fileLen == -1) {
				fileLen = 20000;
			}
			int hasRead = 0;// 已经读取的总大小,单位b
			String line = null;
			while ((line = in.readLine()) != null) {
				doc.append(line);
				hasRead += line.getBytes().length;
				double v = MathUtils.div(hasRead, fileLen);
				context.setProgress((int) (v * 10000));
			}
			in.close();
		} catch (Exception e) {
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e) {
			} finally {
			}
		}
		return doc.toString();
	}

	/**
	 * 开始访问UEL
	 */
	public static byte[] getImage(Activity context, String linkUrl) {
		TitleUtils.setTitleProgressShow(context);//
		byte[] imageRaw = null;
		try {
			URL url = new URL(linkUrl);
			HttpURLConnection uc = (HttpURLConnection) url
					.openConnection();
			InputStream in = new BufferedInputStream(
					uc.getInputStream());
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int fileLen = uc.getContentLength(); // 远程文件的总大小，单位 b
			if (fileLen == -1) {
				fileLen = 20000;
			}
			int hasRead = 0;// 已经读取的总大小,单位b
			int len;
			byte[] data = new byte[1024 * 5];
			while ((len = in.read(data)) != -1) {
				out.write(data, 0, len);
				hasRead += len;
				double v = MathUtils.div(hasRead, fileLen);
				if(v >= 1){
					v = 0.99;
				}
				context.setProgress((int) (v * 10000));
				out.flush();
			}
			imageRaw = out.toByteArray();
			uc.disconnect();
			in.close();
			out.close();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return imageRaw;
	}

}
