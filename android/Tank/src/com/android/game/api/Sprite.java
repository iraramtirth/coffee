package com.android.game.api;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * @author wangtao
 */
@SuppressWarnings("unused")
public class Sprite extends Layer {

	// 图像
	private Bitmap srcImage;
	private int srcFrameWidth;
	private int srcFrameHeight;

	// 方向常量
	public final static int NONE = 0;	// 暂停
	public final static int UP = 1;	// 上
	public final static int RIGHT = 2;// 右
	public final static int DOWN = 3;	// 下
	public final static int LEFT = 4; // 左
	// Sprite当前方向
	private int currentDirection = UP;
	// sprite要移动的方向
	private int moveTowards;

	private final int STEP = 5; // Sprite 移动步幅 
	
	//移动方式
	private int t_currentTransformation;
	// 旋转角度
	public static final int TRANS_NONE = 0; // 静止
	public static final int TRANS_ROT90 = 5; // 顺时针旋转 90°
	public static final int TRANS_ROT180 = 3; // 顺时针180°
	public static final int TRANS_ROT270 = 6; // 顺时针270°

	public static final int TRANS_MIRROR = 2; // 逆时针 180
	public static final int TRANS_MIRROR_ROT90 = 7; // 逆时针90°
	public static final int TRANS_MIRROR_ROT180 = 1;// 静止
	public static final int TRANS_MIRROR_ROT270 = 4;// 逆时针270°
	
	
	
	// 帧的左上角(相对)坐标, 从 0,0 开始
	private int[] frameCoordsX;
	private int[] frameCoordsY;

	// 碰撞
	private int collisionRectX; // =0
	private int collisionRectY; // =0
	private int collisionRectWidth;
	private int collisionRectHeight;



	public Sprite(Bitmap image) {
		this(image, image.getWidth(), image.getHeight());
	}
	public Sprite(Bitmap image,int frameWidth, int frameHeight) {
		super(image.getWidth(), image.getHeight());
		// 初始化帧
		initializeFrames(image, frameWidth, frameHeight);
		// 初始化碰撞矩形的边界
		initCollisionRectBounds();
	}
	@Override
	public void paint(Canvas c) {
		if (c == null) {
			throw new NullPointerException();
		}
		this.computeTransformedBounds();
		if (visible) {
			Matrix matrix = new Matrix();
			matrix.reset();
			switch (this.t_currentTransformation) {
			case TRANS_ROT90:
				matrix.setRotate(90f);
				break;
			case TRANS_ROT180:
				matrix.setRotate(180f);
				break;
			case TRANS_ROT270:
				matrix.setRotate(270f);
				break;
			case TRANS_MIRROR:
				matrix.setScale(-1f, 1f);
				break;
			case TRANS_MIRROR_ROT90:
				matrix.setScale(-1f, 1f);
				matrix.postRotate(90f);
				break;
			case TRANS_MIRROR_ROT180:
				matrix.setScale(-1f, 1f);
				matrix.postRotate(180f);
				break;
			case TRANS_MIRROR_ROT270:
				matrix.setScale(-1f, 1f);
				matrix.postRotate(270f);
				break;
			default:
				matrix.reset();
				break;
			}
			this.t_currentTransformation = TRANS_NONE;
			// 帧大小
			Rect src = new Rect(0, 0, srcFrameWidth, srcFrameHeight);
			Rect dst = new Rect(super.x, super.y, super.x + srcFrameWidth,
					super.y + srcFrameHeight);
			// 图像转换
			Bitmap bmp = Bitmap.createBitmap(srcImage, 0, 0, srcFrameWidth,
					srcFrameHeight, matrix, true);
			Paint paint = new Paint();
			//c.drawColor(paint.getColor());
			c.drawBitmap(bmp, src, dst, paint);
			
		}
	}

	// 初始化帧
	private void initializeFrames(Bitmap image, int fWidth, int fHeight) {
		this.srcImage = image;
		this.srcFrameWidth = fWidth;
		this.srcFrameHeight = fHeight;
		
		// 水平方向帧的数量
		int numHorizontalFrames = image.getWidth() / fWidth;
		// 垂直..数量
		int numVerticalFrames = image.getHeight() / fHeight;
		// 帧 总数
		int numberFrames = numHorizontalFrames * numVerticalFrames;
		// 水平方向-每个帧左上角的坐标
		frameCoordsX = new int[numberFrames];
		frameCoordsY = new int[numberFrames];
		int currentFrame = 0;
		for (int yy = 0; yy < image.getHeight(); yy += fHeight) {
			for (int xx = 0; xx <  image.getWidth(); xx += fWidth) {
				frameCoordsX[currentFrame] = xx;
				frameCoordsY[currentFrame] = yy;
				currentFrame++;
			}
		}
	}
	
	private void initCollisionRectBounds() {
		// reset x and y of collision rectangle
		collisionRectX = 0;
		collisionRectY = 0;

		// intialize the collision rectangle bounds to that of the sprite
		collisionRectWidth = super.width;
		collisionRectHeight = super.height;
	}
	
	//计算移动方式
	private void computeTransformedBounds(){
		switch(this.currentDirection){
			case UP:
					switch(this.moveTowards){
					case NONE:	this.t_currentTransformation = TRANS_NONE;
						break;
					case UP: 	this.y -= STEP;	 //移动
						break;
					case RIGHT: this.t_currentTransformation = TRANS_ROT90;
						break;
					case DOWN: 	this.t_currentTransformation = TRANS_ROT180;
						break;
					case LEFT:	this.t_currentTransformation = TRANS_ROT270;
						break;
					}
				break;
			case RIGHT:
				switch(this.moveTowards){
				case NONE:	this.t_currentTransformation = TRANS_NONE;
					break;
				case RIGHT: this.x += STEP;	
					break;
				case DOWN: 	this.t_currentTransformation = TRANS_ROT90;
					break;
				case LEFT: 	this.t_currentTransformation = TRANS_ROT180;
					break;
				case UP:	this.t_currentTransformation = TRANS_ROT270;
					break;
				}
			break;
			case DOWN:
				switch(this.moveTowards){
				case NONE:	this.t_currentTransformation = TRANS_NONE;
					break;
				case DOWN: 	this.y += STEP;
					break;
				case LEFT: 	this.t_currentTransformation = TRANS_ROT90;
					break;
				case UP: 	this.t_currentTransformation = TRANS_ROT180;
					break;
				case RIGHT:	this.t_currentTransformation = TRANS_ROT270;
					break;
				}
			break;
			case LEFT:
				switch(this.moveTowards){
				case NONE:	this.t_currentTransformation = TRANS_NONE;
					break;
				case LEFT: 	this.x -= STEP;
					break;
				case UP: 	this.t_currentTransformation = TRANS_ROT90;
					break;
				case RIGHT: this.t_currentTransformation = TRANS_ROT180;
					break;
				case DOWN:	this.t_currentTransformation = TRANS_ROT270;
					break;
				}
			break;
			default:
					this.t_currentTransformation = NONE;
				break;
		}
		/////////// 注意别忘记改变当前方向 ////////////
		this.currentDirection = moveTowards;
		
	}
	
	public void moveTowards(int direction){
		this.moveTowards = direction;
	}
	
}
