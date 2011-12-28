package com.android.game.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import com.android.game.activity.R;
import com.android.game.api2.Sprite;
import com.android.game.api2.Sprite.ActionType;

/**
 * 游戏主界面
 * 
 * @author wangtao
 */
public class GameView extends SurfaceView implements Callback {

	private String TAG = "GameView";
	
	private final Sprite player;

	// 创建 thread 服务进程
	private Thread gameTread;
	private SurfaceHolder holder;
	private boolean isActive = true;

	private ActionType action = ActionType.NONE;


	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		Bitmap map = BitmapFactory.decodeResource(this.getResources(), R.drawable.bore);
		player = new Sprite(map);
		player.setPosition(150, 150);
		// 添加回调对象
		holder = this.getHolder();
		holder.addCallback(this);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Canvas canvas = null;
		for (int i = 0; i < 2; i++) {
			canvas = holder.lockCanvas();
			canvas.drawColor(Color.BLACK);
			holder.unlockCanvasAndPost(canvas);
		}
		// 启动游戏线程
		gameTread = new GameThread();
		gameTread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}

	class GameThread extends Thread{
		@Override
		public void run() {
			while (isActive) {
				Canvas canvas = null;
				try {
					synchronized (holder) {
						Rect rect = player.getRect(action);
						canvas = holder.lockCanvas(rect);
						player.paint(canvas,action);
					}
				} catch (Exception e) {
					Log.e(TAG, e.getMessage());
				} finally {
					// 等待
					if (canvas != null) {
						holder.unlockCanvasAndPost(canvas);
					}// 等待
					synchronized (gameTread) {
						try {
							gameTread.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}// 等待
					}
				}
			}// while
		}
	}

	public void playerAction(ActionType action) {
		this.action = action;
		synchronized (this.gameTread) {
			this.gameTread.notify();// 唤醒线程
		}
	}

	public Thread getGameThread() {
		return gameTread;
	}
}
