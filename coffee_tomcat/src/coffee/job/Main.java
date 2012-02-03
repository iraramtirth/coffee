package coffee.job;

class Father {    
    static {    
        System.out.println("Father static Create");    
    }    
    {    
        System.out.println("Father Create");    
    }    
        
    public static void StaticFunction(){    
        System.out.println("Father static Function");    
    }    
    
    public void Function(){    
        System.out.println("Father Function");    
    }    
}    

class ChildOne extends Father {    
    static {    
        System.out.println("ChildOne static Create");    
    }    
    {    
        System.out.println("ChildOne Create");    
    }    
        
    public static void StaticFunction(){    
        System.out.println("ChildOne static Function");    
    }    
    
}  

 class ChildTwo extends Father {    
    static {    
        System.out.println("ChildTwo static Create");    
    }    
    {    
        System.out.println("ChildTwo Create");    
    }    
    
    public static void StaticFunction() {    
        System.out.println("ChildTwo static Function");    
    }    
    
    public void Function() {    
        System.out.println("ChildTwo Function");    
    }    
}   

public class Main {    
    @SuppressWarnings("static-access")
	public static void main(String[] args) {    
    	/**
    	 *  Father static Create
		 *	ChildOne static Create
		 *	Father Create
		 *	ChildOne Create
    	 */
    	Father a = new ChildOne(); 
    	/**
    	 *  ChildTwo static Create
		 *	Father Create
		 *	ChildTwo Create
    	 */
        Father b = new ChildTwo();    
        a.StaticFunction();	//Father static Function    
        a.Function();   	//Father Function 
        b.StaticFunction();	//Father static Function    
        b.Function();    	//ChildTwo Function
    }  
}  