package coffee;


public class MakeJar {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		makeJdbc();
	}
	
	
	public static void makeExcel(){
		MakeJar.make("coffee-excel", "org/coffee/tools/excel");
	}
	
	public static void makeJdbc(){
		MakeJar.make("coffee-jdbc", "org/coffee/jdbc","javax/persistence");
	}
	
	/**
	 * 打jar包 
	 * @param jarName
	 * @param pkgs
	 */
	public static void make(String jarName, String ... pkgs){
		String pathPersistence = MakeJar.class.getResource("/").getPath();
		String jar = "jar cvf "+jarName+".jar ";
		for(String pkg : pkgs){
			jar += " -C " + pathPersistence.substring(1) + " " + pkg;
		}
		System.out.println(jar);
		try {
			Runtime runtime = Runtime.getRuntime();//
			runtime.exec(jar);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
}
