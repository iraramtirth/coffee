package coffee.android;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import coffee.common.util.FileUtils;
import coffee.common.util.io.Outer;

public class SrcTools {
	/**
	 * 查找framework中的源文件与标准的android.jar中不同的java文件列表
	 * 即：存在于framework而不存在于android.jar中的
	 */
	public static void findDifferentJava(String frameworkPath, String androidJarPath, int level){
		String jarPath = null;
		if(androidJarPath == null){
			jarPath = System.getenv("ANDROID_HOME");
			jarPath += "/platforms/android-"+level+"/android.jar";
		}
		List<String> differentList = new ArrayList<String>();
		List<String> jarFilesList = new ArrayList<String>();
		try {
			JarFile jar = new JarFile(new File(jarPath));
			Enumeration<JarEntry> files = jar.entries();
			while(files.hasMoreElements()){
				JarEntry je = files.nextElement();
				String name = je.getName();
				System.out.println(name);
				if(name.contains("$")){
					continue;
				}
				jarFilesList.add(name);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<String> srcFileList = FileUtils.listFiles(frameworkPath, "java");
		for(String filePath : srcFileList){
			System.out.println(filePath);
			filePath = filePath.replace("\\", "/");
			String classPart = filePath.substring(frameworkPath.length()+1,filePath.length() - 5);
			classPart = classPart.replace("\\", "/");
			if(jarFilesList.contains(classPart) == false){
				differentList.add(filePath);
			}
		}
		Outer.setPath(null, true, false);
		//将differentList列表中的java文件，拷贝到src目录下
		for(String differentJava : differentList){
			String srcPath = differentJava.replaceFirst("/android", "/src");
			//FileUtils.copy(differentJava, srcPath);
			Outer.pl(srcPath);
		}
	}
	
	public static void main(String[] args) {
		findDifferentJava("E:/Workspaces/MyEclipse9/froyo/android", null,8);
	}
}
