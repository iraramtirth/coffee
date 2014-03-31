package coffee.frame.imagescan;

import java.io.File;
import java.lang.ref.SoftReference;
import java.lang.reflect.Constructor;

import org.coffee.util.lang.FileUtils;
import org.coffee.util.view.BitmapUtils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import coffee.frame.Config;
import coffee.frame.activity.base.BaseActivity;
import coffee.utils.log.Log;

/**
 * 浏览图片|拍照 <br>
 * 
 * @author coffee
 * 
 *         2014年2月24日下午4:53:51
 */
public class ImageScanActivity extends BaseActivity implements View.OnClickListener {

	final String TAG = "ImageScanActivity";
	/**
	 * 选中|拍的图片
	 */
	private ImageView mImage;
	/**
	 * 要发送的图片___绝对路径(处理后的原图) <br>
	 * 缩略图保存在{@link Config#getUserRootDir()}目录下, <br>
	 * 文件名为{@link FileOperator#getPictureMd5(String)}
	 */
	private String mImageBig;

	/**
	 * 选中的图片
	 */
	private Bitmap mBitmap;

	/**
	 * 图像的位置<br>
	 * 用户左旋转90°, 该值减一<br>
	 * 用户右旋转90°, 该值加一<br>
	 * 如果发送图片的时候该值不为0, 则需要先重新生成新的图片在发送<br>
	 */
	private int position = 0;

	/**
	 * 0-从本地选择<br>
	 * 1-拍摄<br>
	 */
	private static int type = 0;

	/**
	 * 存放照片(拍照)的真实路径
	 */
	private static Uri mCaptureUri;

	/**
	 * 注意：如果选择图片的时候该activity被杀死 <br>
	 * 回调执行的先后顺序是 onActiviyResult --然后才执行--> onCreate
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// activity之间跳转
		if (getIntent() != null) {
			type = getIntent().getIntExtra("type", 0);
			super.onCreate(savedInstanceState);
		} else {
			// activity被destroy
			if (savedInstanceState != null) {
				// 记录一下当前的图片选择方式：拍照|图片浏览
				// mWhat = savedInstanceState.getInt(EXTRA_WHAT);
				// mCaptureUri = Uri.fromFile(new File(savedInstanceState
				// .getString(EXTRA_ARG1)));
				super.onCreate(savedInstanceState);
				this.treatmentImageFile(mCaptureUri);
				return;
			} else {
				super.onCreate(savedInstanceState);
			}
		}
		// 本地
		if (type == 0) {
			//
			Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT);
			innerIntent.setType("image/*");
			Intent wrapperIntent = Intent.createChooser(innerIntent, null);
			startActivityForResult(wrapperIntent, 1);
		} else {
			// 拍摄照片
			Intent takepictureintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			// if (OsBuild.isThumbWhenTakePhoto()) {
			String filePath = Config.getCaptureDir() + "/capture_" + System.currentTimeMillis() + ".jpg";
			FileUtils.createNewFileOrDir(filePath);
			mCaptureUri = Uri.fromFile(new File(filePath));
			takepictureintent.putExtra(MediaStore.EXTRA_OUTPUT, mCaptureUri);
			// }
			startActivityForResult(takepictureintent, 2);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// 程序被kill掉收到
		// outState.putInt(EXTRA_WHAT, mWhat);
		// if (mCaptureUri != null) {
		// outState.putString(EXTRA_ARG1, mCaptureUri.getPath());
		// }
	}

	@Override
	public void doInitView() {
//		this.setContentView(R.layout.common_image_scan);
//		this.mImage = (ImageView) findViewById(R.id.image);
//		Button btn = (Button) findViewById(R.id.image_reset);
//		btn.setOnClickListener(this);
//		findViewById(R.id.image_send).setOnClickListener(this);
//		findViewById(R.id.image_rotation_left).setOnClickListener(this);
//		findViewById(R.id.image_rotation_right).setOnClickListener(this);
//		// 重选|重拍
//		if (type == 1) {
//			btn.setText("重拍");
//		} else {
//			btn.setText("重选");
//		}
	}

	@Override
	public void onClick(View v) {
//		int id = v.getId();
//		if (id == R.id.image_reset) {
//			finish();
//			Intent intent = new Intent();
//			intent.setClass(context, this.getClass());
//			intent.putExtra("type", type);
//			startActivity(intent);
//		} else if (id == R.id.image_rotation_left) {
//			position--;
//			mBitmap = BitmapUtils.toRotation(mBitmap, -90);
//			this.mImage.setImageBitmap(mBitmap);
//		} else if (id == R.id.image_rotation_right) {
//			this.position++;
//			// 创建操作图片用的matrix对象
//			mBitmap = BitmapUtils.toRotation(mBitmap, 90);
//			this.mImage.setImageBitmap(mBitmap);
//		} else if (id == R.id.image_send) {
//			if (position != 0) {
//				mImageBig = BitmapUtils.cacheBitmapToFile(mBitmap, mImageBig);
//			}
//			Intent data = new Intent();
//			data.putExtra("imgPath", mImageBig);
//			setResult(RESULT_OK, data);
//			finish();
//		}
	}

	/**
	 * 通过intent.putExtra(MediaStore.EXTRA_OUTPUT, mCaptureUri)<br>
	 * 这种方式启动相机的,返回结果data==null
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			if (null != data) {
				// 相册图片的URI
				Uri imageUri = data.getData();
				// 相册照片选择完成 处理压缩图片
				treatmentImageFile(imageUri);
			} else {
				finish();
			}
		} else if (requestCode == 2) {
			Uri captureUri = null;
			// 一些机型 通过这种方式启动相机的<br>
			// intent.putExtra(MediaStore.EXTRA_OUTPUT,
			// mCaptureUri)此时data==null
			if (data != null && data.getData() != null) {
				Bundle bundle = data.getExtras();
				if (bundle != null && bundle.get("data") != null) {
					Bitmap bitmap = (Bitmap) bundle.get("data");
					String tempFilePath = BitmapUtils.cacheBitmapToFile(bitmap, "capture.jpg");
					captureUri = Uri.fromFile(new File(tempFilePath));
				} else {
					captureUri = data.getData();
				}
			} else {
				captureUri = mCaptureUri;
				// 文件是空的||用户拍照的时候点击返回
				if (new File(mCaptureUri.getPath()).length() == 0) {
					finish();
				}
			}
			treatmentImageFile(captureUri);
			// 下行代码仅仅为了重新执行onCreate时候,显示一下当前选中的图片
			mCaptureUri = captureUri;
		} else {
			this.finish();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mBitmap != null && mBitmap.isRecycled() == false) {
			mBitmap.recycle();
		}
	}

	/**
	 * 根据URI获取图片 并且处理 压缩到适应大小体积
	 * 
	 * @param imageUri
	 */
	private void treatmentImageFile(Uri imageUri) {
		// 获取图片地址
		String iamgeFilePath = new URIUtils().getPathFromUri(imageUri);

		// 压缩后的大图(点开查看大图)
		SoftReference<Bitmap> bmpRefBig = BitmapUtils.getCompressedBitmapFromFile(iamgeFilePath, 800, 600);
		if (bmpRefBig == null || bmpRefBig.get() == null) {
			Log.e("MSG_TAG_" + TAG, "treatmentImageFile->failed, the bitmap is null", null);
			finish();
			return;
		}
		mBitmap = bmpRefBig.get();
		mImageBig = String.valueOf(System.currentTimeMillis());
		if (bmpRefBig != null && bmpRefBig.get() != null) {
			int degree = exifOrientationToDegrees(imageUri.getPath());
			Bitmap tmp = BitmapUtils.toRotation(bmpRefBig.get(), degree);
			if (tmp != null) {
				bmpRefBig = new SoftReference<Bitmap>(tmp);
			}
			// 压缩后的大图
			mImageBig = BitmapUtils.cacheBitmapToFile(bmpRefBig.get(), mImageBig);
			this.mImage.setImageBitmap(bmpRefBig.get());
		}
		// generateImageSmall(mImageBig);
	}

	/**
	 * 根据图片路径获取图片方向
	 * 
	 * @param fileName
	 * @return(菜单键朝右是0|菜单键朝下是90)
	 */
	public static int exifOrientationToDegrees(String fileName) {
		int rotation = 0;
		try {
			// android2.0以上才有ExifInterface的API支持，使用Java反射机制调用系统隐藏API，存在系统API被更新的风险
			Constructor<?> constructor = Class.forName("android.media.ExifInterface").getDeclaredConstructor(String.class);
			Object exifInterface = constructor.newInstance(fileName);
			Class<?> clazz = exifInterface.getClass();
			String tagOrientation = (String) Class.forName("android.media.ExifInterface").getField("TAG_ORIENTATION").get(null); // 得到静态变量，因为静态成员不需要对象就可以调用，所以在get方法传入null的意思就是取静态变量
			int orientationNormal = (Integer) Class.forName("android.media.ExifInterface").getField("ORIENTATION_NORMAL").get(null);
			int orientationRotate90 = (Integer) Class.forName("android.media.ExifInterface").getField("ORIENTATION_ROTATE_90").get(null);
			int orientationRotate180 = (Integer) Class.forName("android.media.ExifInterface").getField("ORIENTATION_ROTATE_180").get(null);
			int orientationRotate270 = (Integer) Class.forName("android.media.ExifInterface").getField("ORIENTATION_ROTATE_270").get(null);
			int exifOrientation = (Integer) clazz.getMethod("getAttributeInt", new Class[] { String.class, int.class }).invoke(exifInterface, new Object[] { tagOrientation, orientationNormal });
			if (exifOrientation == orientationRotate90) {
				rotation = 90;
			} else if (exifOrientation == orientationRotate180) {
				rotation = 180;
			} else if (exifOrientation == orientationRotate270) {
				rotation = 270;
			} else {
				rotation = 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rotation;
	}

}
