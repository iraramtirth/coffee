package coffee.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.logging.Logger;

import coffee.server.cfg.ServerConfig;
import coffee.server.cfg.WebConfig;
import coffee.server.http.Request;
import coffee.server.http.RequestHandler;
import coffee.server.http.RequestParser;

/**
 * @author coffee
 */
public class HttpServer {

	private ServerSocketChannel ssc;
	private Selector selector;
	
	public static void main(String[] args) {
		HttpServer server = new HttpServer();
		try {
			server.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println();
	}
	
	/**
	 * 服务器配置信息
	 */
	private ServerConfig serverCfg;
	/**
	 *  web.xml 配置信息
	 */
	@SuppressWarnings("unused")
	private WebConfig webCfg;
	
	private static Logger log = Logger.getLogger(HttpServer.class.getName());
	
	/**
	 *  加载server.xml配置文件
	 *  
	 *  加载 web.xml 文件
	 */
	public HttpServer(){
		try {
			serverCfg = new ServerConfig();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 启动服务，打开ServerSocketChannel
	 * @throws IOException
	 * 当ssc.open()失败将抛出IOException 
	 */
	public void start() throws IOException{
		log.info("启动httpServer");
		if(ssc == null){
			ssc = ServerSocketChannel.open();
		}
		ssc.socket().bind(new InetSocketAddress(serverCfg.getPort()));
		ssc.configureBlocking(false);
		selector = Selector.open();
		ssc.register(selector, SelectionKey.OP_ACCEPT);
		//循环监听客户端请求;并处理
		this.select();
		System.out.println("xxxx");
	}
	
	
	private void select() throws IOException{
		log.info("服务器监听开始.....");
		//创建一个缓冲区
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		while(true){
			selector.select();
			log.info("监听到请求...");
			Set<SelectionKey> sk = selector.selectedKeys();
			for(SelectionKey key: sk){
				sk.remove(key);
				if(key.isAcceptable()){
					SocketChannel sc = ((ServerSocketChannel) key.channel()).accept();
					ByteBuffer data = ByteBuffer.allocate(1024);
					sc.read(data);
					// 解析 http 请求
					Request requestInfo = RequestParser.parse(new String(data.array()));
					// 处理Http请求
					String html = RequestHandler.handle(requestInfo);
					buffer.clear();
					try{
						buffer.put(html.getBytes());
					}catch(Exception e){
						e.printStackTrace();
					}
					buffer.flip();//使当前缓冲区大小符合put进去的string的实际长度
					sc.write(buffer);
					sc.close();
				}
			}
		}
	}
}
