package coffee.frame.pull2refresh;

/**
 * 
 * @author coffee <br>
 *         2014年3月18日下午3:17:15
 */
public interface XListener {

	public interface Header {
		public void pullRefresh();
	}

	public interface Footer {
		public void loadMore();
	}
}
