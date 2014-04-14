package coffee.server;

import java.util.HashMap;

/**
 * 保存在线用户的列表<br>
 * 
 * @author coffee <br>
 *         2014年4月3日下午2:59:17
 */
public class Online {

	/**
	 * k-用户名或者IP
	 */
	private static HashMap<String, Reg> udps = new HashMap<String, Reg>();
	private static HashMap<String, Reg> tcps = new HashMap<String, Reg>();

	/**
	 * @param host
	 * @param port
	 * @param type
	 *            0-udp .1-tcp
	 */
	public static void reg(String user, String host, String port, int type) {
		int portInt = Integer.valueOf(port);
		Reg reg = new Reg(user, host, portInt);
		if (type == 0) {
			udps.put(user, reg);
		} else if (type == 1) {
			tcps.put(user, reg);
		}
	}

	public static void unReg(String username, int type) {
		if (type == 0) {
			udps.remove(username);
		} else if (type == 1) {
			tcps.remove(username);
		}
	}

	public static HashMap<String, Reg> getItems(int type) {
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
			Reg reg = udps.get(host);
			if (reg != null) {
				port = reg.getPort();
			}
		} else if (type == 1) {
			Reg reg = tcps.get(host);
			if (reg != null) {
				port = reg.getPort();
			}
		}
		return port == null ? 0 : port;
	}

	public static void clearAll() {
		tcps.clear();
		udps.clear();
	}

	// /////////////////////////////////////////
	public static class Reg {
		private String user;
		private String host;
		private int port;

		public Reg(String user, String host, int port) {
			this.user = user;
			this.host = host;
			this.port = port;
		}

		public String getUser() {
			return user;
		}

		public void setUser(String user) {
			this.user = user;
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
