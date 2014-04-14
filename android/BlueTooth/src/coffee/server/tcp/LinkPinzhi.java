package coffee.server.tcp;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * 
 */
public class LinkPinzhi {
	private static String BR_TAG = "\r\n";
	private static String content = "GET / HTTP/1.1" + BR_TAG + //
			"Host: stackoverflow.com:80" + BR_TAG + //
			"Connection: keep-alive" + BR_TAG + //
			"Cache-Control: max-age=0" + BR_TAG + //
			"Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8" + BR_TAG + //
			"User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/34.0.1847.116 Safari/537.36" + BR_TAG + //
			"Accept-Encoding: gzip,deflate,sdch" + BR_TAG + //
			"Accept-Language: zh-CN,zh;q=0.8,en;q=0.6,zh-TW;q=0.4" + BR_TAG + //
			"" + BR_TAG;

	public static void main(String[] args) {
		final Socket client = new Socket();
		try {
			client.connect(new InetSocketAddress("www.stackoverflow.com", 80));
			BufferedOutputStream bout = new BufferedOutputStream(client.getOutputStream());
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						zipInputStream(client.getInputStream());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).start();
			Thread.sleep(1000 * 2);
			byte[] data = content.getBytes("UTF-8");
			bout.write(data, 0, data.length);
			// 注意flush, 否则对方可能收不到消息
			bout.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Thread.sleep(1000 * 5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	private static String zipInputStream(InputStream is) throws IOException {  
//	    GZIPInputStream gzip = new GZIPInputStream(is); 
	    BufferedReader in = new BufferedReader(new InputStreamReader(is, "UTF-8"));  
	    StringBuffer buffer = new StringBuffer();  
	    String line;  
	    while ((line = in.readLine()) != null) {
	        buffer.append(line + "\n");
	        System.out.println(line);
	    }
	    is.close();  
	    return buffer.toString();  
	}  


}
