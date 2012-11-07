package com.test;


//public class Test {
//	public static void main(String[] args) {
//		
//		String str = RegexUtils.match("120023888888", "^[1-9]\\d{2,10}$");
//		
//		System.out.println(str);
//	}
//}	

public class Test {
	   
	  //连接时间的设定
	   private final int n= 20000;
	   
	   public static void main(String[] args){
		   Test test =new Test ();
	       test.testStringTime();
	       test.testStringBufferTime();
	       test.testStringBuilderTime();
	    }
	   
	   /**
	     *测试String连接字符串的时间
	     */
	   public void testStringTime(){
	      long start = System.currentTimeMillis();
	       String a ="";
	      for(int k=0; k<n; k++ ){
	           a +="_"+ k;
	       }
	      long end = System.currentTimeMillis();
	      long time = end - start;
	       System.out.println("String time:"+ time);
	      //System.out.println("String str:" + str);
	    }
	   
	   /**
	     *测试StringBuffer连接字符串的时间
	     */
	   public void testStringBufferTime(){
	      long start = System.currentTimeMillis();
	       StringBuffer b = new StringBuffer() ;
	      for(int k=0; k < n;  k++ ){
	           b.append("_"+ k );
	       }
	      long end = System.currentTimeMillis();
	      long time = end - start;
	       System.out.println("StringBuffer time:"+ time);
	      //System.out.println("StringBuffer str:" + str);
	    }
	  
	   /**
	     *测试StringBuilder连接字符串的时间
	     */
	   public void testStringBuilderTime(){
	      long start = System.currentTimeMillis();
	       StringBuilder c = new StringBuilder() ;
	      for(int k=0;k<n;k++ ){
	           c.append("_"+ k );
	       }
	      long end = System.currentTimeMillis();
	      long time = end - start;
	       System.out.println("StringBuilder time:"+ time);
	      //System.out.println("StringBuffer str:" + str);
	    }
	}