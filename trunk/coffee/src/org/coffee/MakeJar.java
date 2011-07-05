package org.coffee;


public class MakeJar {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String pathPersistence = MakeJar.class.getResource("/").getPath();
		System.out.println(pathPersistence);
		//pathPersistence.substring(1)
		String cd = "cd " + pathPersistence;
		String jar = "jar cvf test.jar -C . /javax/persistence/";
		
		System.out.println(cd + "\n" +jar);
		
		try {
			Runtime.getRuntime().exec(cd);
			Runtime.getRuntime().exec(jar);
			//process.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

}
