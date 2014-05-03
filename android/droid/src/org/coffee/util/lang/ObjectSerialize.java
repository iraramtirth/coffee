package org.coffee.util.lang;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import org.coffee.App;

/**
 * 对象序列化工具类<br>
 * 文件保存在/data/data/..package../cache/目录下
 * 
 * @author coffee
 * 
 *         2014年1月16日下午2:32:25
 */
public class ObjectSerialize {

	/**
	 * 将对象序列化到文件<br>
	 * 文件名命名规则 com.package.className _ id <br>
	 * 如果是ArrayList类型,则传入ArrayList<Object>对象, com.package.Object.class
	 * 
	 * @param obj
	 * @param id
	 *            主键ID--int或者string类型<br>
	 *            class类型, 此时obj一般为ArrayList对象
	 */
	public synchronized static void write(Serializable obj, Object id) {
		if (obj == null) {
			return;
		}
		FileOutputStream fout = null;
		ObjectOutputStream oout = null;
		try {
			File f = getCacheFile(obj.getClass(), id);
			fout = new FileOutputStream(f);
			oout = new ObjectOutputStream(fout);
			oout.writeObject(obj); // 括号内参数为要保存java对象
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (oout != null) {
					oout.close();
				}
				if (fout != null) {
					fout.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 追加到结尾
	 * 
	 * @param bean
	 */
	@SuppressWarnings("unchecked")
	public synchronized static <T> void append(T bean) {
		ArrayList<T> items = read(ArrayList.class, bean.getClass());
		if (items == null) {
			items = new ArrayList<T>();
		}
		items.add(bean);
		write(items, bean.getClass());
	}

	private static File getCacheFile(Class<?> objClass, Object id) {
		String fileName = "";
		if (id == null) {
			fileName = objClass.getName();
		} else {
			if (id instanceof Class) {
				fileName = objClass.getName() + "_" + ((Class<?>) id).getName();
			} else {
				fileName = objClass.getName() + "_" + id;
			}
		}
		// data/data/..package../cache/.fileName
		// File file = new File("E:/workspace/java-kepler/Main/cache",
		// fileName);
		File file = new File(App.getContext().getCacheDir(), fileName);
		return file;
	}

	/**
	 * 从文件中反序列化对象
	 * 
	 * @param beanClass
	 * @param id
	 * @return 如果没有缓存值,将返回null
	 */
	@SuppressWarnings("unchecked")
	public synchronized static <T> T read(Class<T> beanClass, Object id) {
		FileInputStream fin = null;
		ObjectInputStream oin = null;
		try {
			File f = getCacheFile(beanClass, id);
			fin = new FileInputStream(f);
			oin = new ObjectInputStream(fin);
			T obj = (T) oin.readObject();// 强制类型转换
			return obj;
		} catch (FileNotFoundException e) {
			// ignore
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fin != null) {
					fin.close();
				}
				if (oin != null) {
					oin.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 移除缓存
	 * 
	 * @param beanClass
	 * @param id
	 */
	public synchronized static <T> void remove(Class<T> beanClass, Object id) {
		File file = getCacheFile(beanClass, id);
		if (file != null) {
			file.delete();
		}
	}
}
