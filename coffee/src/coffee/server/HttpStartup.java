package coffee.server;

import java.io.IOException;

public class HttpStartup {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		HttpServer server = new HttpServer();
		server.start();
		
	}

}
