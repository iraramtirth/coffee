package coffee.server;

import java.util.HashMap;

/**
 * 保存在线用户的列表<br>
 * 
 * @author coffee <br>
 *         2014年4月3日下午2:59:17
 */
public class Online {

	private static HashMap<String, Integer> udps = new HashMap<String, Integer>();
	private static HashMap<String, Integer> tcps = new HashMap<String, Integer>();

	/**
	 * @param host
	 * @param port
	 * @param type
	 *            0-udp .1-tcp
	 */
	public static void reg(String host, int port, int type) {
		if (type == 0) {
			udps.put(host, port);
		} else if (type == 1) {
			tcps.put(host, port);
		}
	}

	public static void unReg(String host, int type) {
		if (type == 0) {
			udps.remove(host);
		} else if (type == 1) {
			tcps.remove(host);
		}
	}

	public static HashMap<String, Integer> getItems(int type) {
		if (type == 0) {
			return udps;
		} else if (type == 1) {
			return tcps;
		}
		return null;
	}

	public static int getPort(String host, int type) {
		Integer port = 0;
		if (type == 0) {
			port = udps.get(host);
		} else if (type == 1) {
			port = tcps.get(host);
		}
		return port == null ? 0 : port;
	}

	// /////////////////////////////////////////
	public static class Reg {
		private String host;
		private int port;

		public Reg(String host, int port) {
			this.host = host;
			this.port = port;
		}

		public String getHost() {
			return host;
		}

		public void setHost(String host) {
			this.host = host;
		}

		public int getPort() {
			return port;
		}

		public void setPort(int port) {
			this.port = port;
		}

		@Override
		public String toString() {
			return host + ":" + port;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((host == null) ? 0 : host.hashCode());
			result = prime * result + port;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Reg other = (Reg) obj;
			if (host == null) {
				if (other.host != null)
					return false;
			} else if (!host.equals(other.host))
				return false;
			if (port != other.port)
				return false;
			return true;
		}

	}
}
