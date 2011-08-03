

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Arrays;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;


public class Test {
	public static void main(String[] args) {
		//List<map string=""><string ,="">> topList = new ArrayList</string></map><map string=""><string ,="">>();
		
		String source = "package com.test; public class Main { public static void main(String[] args) {System.out.println(\"Hello World!\");} }";  
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
		try {//保存class文件编译保存在user.dir
			File[] outputs = new File[]{new File("WebRoot/WEB-INF/classes/")};   
			fileManager.setLocation(StandardLocation.CLASS_OUTPUT,Arrays.asList(outputs));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		SimpleJavaFileObject  sourceObject = new StringSourceJavaObject("Main",source);  
      
		Iterable<? extends JavaFileObject> fileObjects = Arrays.asList(sourceObject);  
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null, null, null, fileObjects);  
        boolean result = task.call();  
        if (result) {  
           System.out.println("编译成功。"); 
		try {
			 Class<?> cls = Class.forName("com.test.Main");
			 Method method = cls.getDeclaredMethod("main", new Class[]{String[].class});
			 method.invoke(cls.newInstance(), new Object[]{new String[]{}});
		} catch (Exception e) {
			e.printStackTrace();
		}
           
        }  
	        
	}
	
	 static class StringSourceJavaObject extends SimpleJavaFileObject {  
	        private String content = null;  
	  
	        public StringSourceJavaObject(String name, String content) {  
	            super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);  
	            this.content = content;  
	        }  
	  
	        public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {  
	            return content;  
	        }  
	    }  
}
