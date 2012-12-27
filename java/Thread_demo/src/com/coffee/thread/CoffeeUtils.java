package com.coffee.thread;


public class CoffeeUtils {
	
	private static CoffeeUtils instance;
	
    public static synchronized CoffeeUtils getInstance()
    {
    	System.out.println("getInstance entry " + Thread.currentThread().getName());
    	try {
			Thread.sleep(1000 * 3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	if(instance == null)
    	{
    		instance  = new CoffeeUtils();
    	}
    	
    	System.out.println("getInstance entry " + Thread.currentThread().getName());
    	return instance;
    }
    
    public void soSth_1()
    {
    	System.out.println("soSth_1 entry " + Thread.currentThread().getName());
    	try {
			Thread.sleep(1000 * 3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	
    	System.out.println("soSth_1 end " + Thread.currentThread().getName());
    }

    
    public void soSth_2()
    {
    	System.out.println("soSth_2 entry " + Thread.currentThread().getName());
    	try {
			Thread.sleep(1000 * 3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	
    	System.out.println("soSth_2 end " + Thread.currentThread().getName());
    }
    
    public static void main(String[] args) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				CoffeeUtils.getInstance().soSth_1();
			}
		}).start();
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				CoffeeUtils.getInstance().soSth_2();
			}
		}).start(); 
	}
}
