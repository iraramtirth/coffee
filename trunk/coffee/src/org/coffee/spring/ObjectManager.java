package org.coffee.spring;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.coffee.spring.ioc.annotation.Repository;
import org.coffee.spring.ioc.annotation.Resource;
import org.coffee.spring.ioc.annotation.Service;

/**
 * 对象管理器
 * 
 * @author wangtao
 */
public class ObjectManager {
	//
	private static List<String> classNameList = new ArrayList<String>();

	private static Map<String, Object> iocObjs = new HashMap<String, Object>();

	public static Map<String, Object> getIocObject(){
		return iocObjs;
	}
	/**
	 * 通过注解创建指定父package下的类的对象
	 */
	public synchronized static void createObject(String basePackage)
			throws Exception {
		if (basePackage == null) {
			basePackage = "/";
		}
		for (String claaname : getClassNameByBasePackage(basePackage)) {
			Class<?> clazz = Class.forName(claaname);
			// 扫描所有的class 如果该类型中含有Service注解;则实例化该对象
			Service service = clazz.getAnnotation(Service.class);
			if (service != null) {
				if ("null".equals(service.name())) {
					String key = clazz.getSimpleName();
					key = key.replaceFirst(".", key.substring(0, 1)
							.toLowerCase());
					iocObjs.put(key, clazz.newInstance());
				} else {
					iocObjs.put(service.name(), clazz.newInstance());
				}
			}
			// 扫描 Repository
			Repository repository = clazz.getAnnotation(Repository.class);
			if (repository != null) {
				// 默认首字母小写
				if ("null".equals(repository.name())) {
					String key = clazz.getSimpleName();
					key = key.replaceFirst(".", key.substring(0, 1)
							.toLowerCase());
					iocObjs.put(key, clazz.newInstance());
				} else {
					iocObjs.put(repository.name(), clazz.newInstance());
				}
			}
		}
		// 创建完所有的对象之后进行注入
		for (String key : iocObjs.keySet()) {
			setterByIoc(iocObjs.get(key));
		}
		System.out.println(iocObjs);
	}

	/**
	 * 方法注入 必须符合 setXXX 规范； 其中 xxx 是注解的name属性 先获取该类的方法上的annotation信息
	 */
	private synchronized static void setterByIoc(Object obj) throws Exception {
		BeanInfo bi = Introspector.getBeanInfo(obj.getClass(), Object.class);
		PropertyDescriptor[] props = bi.getPropertyDescriptors();
		for (PropertyDescriptor prop : props) {
			// 如果该Field不存在Method方法；则跳过；
			// 否则 method.getAnnotation() 将会抛nullpointer异常
			if(prop.getWriteMethod() == null){
				continue;
			}
			Resource resource = prop.getWriteMethod().getAnnotation(
					Resource.class);
			if (resource != null) {
				prop.getWriteMethod().invoke(obj, iocObjs.get(prop.getName()));
			}
		}
	}

	/**
	 * 返回指定base包的下的类名
	 * 
	 * @throws IOException
	 */
	private static String[] getClassNameByBasePackage(String base)
			throws IOException {
		// 在 web 服务器中 一下 输出 null
		//System.out.println(ClassLoader.getSystemResource("."));
		// web工程的跟目录
		String rootFile = ObjectManager.class.getClassLoader().getResource("/").getPath();
		rootFile = rootFile.replace("%20", " ");
		// 获取工程中所有的class文件
		if (base == null || "".equals(base) || "/".equals(base)) {
			return getFiles(new File(rootFile));
		}
		return getFiles(new File(rootFile), base);
	}

	/**
	 * 获取工程中的所有class文件名【已去除后缀.class】
	 * 
	 * @param rootFile
	 *            工程的父级目录
	 * @param base
	 *            包结构的父目录
	 */
	private static String[] getFiles(File rootFile, String base)
			throws IOException {
		// 除去win系统路径中的空格 %20
		rootFile = new File(rootFile.getCanonicalPath().replace("%20", " "));
		final String finalBase = base.replace(".", "\\");
		File[] files = rootFile.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				try {
					if (pathname.isFile()
							&& pathname.getCanonicalPath().contains(finalBase)) {
						String filename = pathname.getPath().substring(
								pathname.getPath().indexOf(finalBase));
						// cn\ioc\xxx.class 转换成 cn.ioc.xxx.class 格式
						filename = filename.replace("\\", ".");
						filename = filename.substring(0, filename
								.lastIndexOf(".class"));
						classNameList.add(filename);
						return true;
					} else {// 如果不符合；则遍历其子类
						getFiles(pathname, finalBase);
					}
				} catch (IOException e) {
				}
				return false;
			}
		});
		if (files != null) {
			for (File file : files) {
				getFiles(file, base);
			}
		}
		return classNameList.toArray(new String[] {});
	}

	/**
	 * 获取工程中的所有类文件
	 * 
	 * @param rootFile
	 * @return
	 */
	private static String[] getFiles(File rootFile) throws IOException {
		File[] allFiles = rootFile.listFiles();
		for (File file : allFiles) {
			if (file.isDirectory()) {
				getFiles(file);
			} else {
				String path = file.getCanonicalPath();
				String clazz = path.substring(path.indexOf("classes") + 8);
				classNameList.add(clazz.replace("\\", ".").substring(0,
						clazz.lastIndexOf(".")));
			}
		}
		return classNameList.toArray(new String[] {});
	}

	public static void main(String[] args) throws Exception {
		createObject("/");
		for (String key : iocObjs.keySet()) {
			System.out.println(key + "   " + iocObjs.get(key));
		}
	}
}
