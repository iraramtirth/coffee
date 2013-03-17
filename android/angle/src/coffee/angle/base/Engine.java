package coffee.angle.base;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import coffee.angle.SurfaceThread;

/**
 * texture engine
 * 
 * @author coffee<br>
 *         2013-3-17下午12:14:37
 */
public class Engine {
	/**
	 * key : {@link Texture#level} <br>
	 * value : {@link Texture} collection
	 */
	private static SortedMap<Integer, List<Texture>> textures = new TreeMap<Integer, List<Texture>>();

	private static SurfaceThread surfaceThread;

	public static Background createBackground(int drawableResource) {
		Background tx = new Background(drawableResource);
		put(tx.getLevel(), tx);
		return tx;
	}

	public static Sprite createSprite(int drawableResource) {
		Sprite tx = new Sprite(drawableResource);
		put(tx.getLevel(), tx);
		return tx;
	}
	
	public static Sprite createSprite(int drawableResource,int cx, int cy) {
		Sprite tx = new Sprite(drawableResource);
		put(tx.getLevel(), tx);
		return tx;
	}

	private static void put(int level, Texture tx) {
		List<Texture> items = textures.get(level);
		if (items == null) {
			textures.put(level, new ArrayList<Texture>());
		}
		textures.get(level).add(tx);
		notifyThread();
	}

	/**
	 * get all textures
	 * 
	 * @return
	 */
	public static SortedMap<Integer, List<Texture>> getTextures() {
		return textures;
	}

	public static void setSurfaceThread(SurfaceThread thread) {
		surfaceThread = thread;
	}

	public static void notifyThread() {
		if (surfaceThread != null) {
			synchronized (surfaceThread) {
				surfaceThread.notify();
			}
		}
	}
}
