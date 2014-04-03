package coffee.server;

import java.util.HashSet;
import java.util.Set;

/**
 * 保存在线用户的列表<br>
 * 
 * @author coffee <br>
 *         2014年4月3日下午2:59:17
 */
public class Online {
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

	private static Set<Reg> items = new HashSet<Reg>();

	public static void reg(String host, int port) {
		Reg reg = new Reg(host, port);
		items.add(reg);
	}

	public static void unReg(String host, int port) {
		Reg reg = new Reg(host, port);
		items.remove(reg);
	}

	public static Set<Reg> getItems() {
		return items;
	}
}
