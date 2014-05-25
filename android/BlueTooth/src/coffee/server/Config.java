package coffee.server;

public class Config {
	//public static String SERVER_TCP = "192.168.1.105";
	//
	private static String SERVER_TCP = "115.28.208.227";

	/**
	 * 服务器端口
	 */
	public static final int PORT_TCP = 8888;

	/**
	 * UDP服务的通信端口
	 */
	public static final int PORT_UDP = 9999;

	public static void setServerHost(String host) {
		Config.SERVER_TCP = host;
	}
	
	public static String getServerHost(){
		return Config.SERVER_TCP;
	}
}
