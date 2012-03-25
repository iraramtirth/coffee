package coffee.imsdroid;

import org.doubango.ngn.NgnApplication;
import org.doubango.ngn.NgnEngine;

public class App extends NgnApplication {

	/**
	 * 加载库文件并初始化NgnEngine
	 */
	private void initEngine() {
		String DATA_FOLDER = String.format("/data/data/%s", App.class
				.getPackage().getName());
		String LIBS_FOLDER = String.format("%s/lib", DATA_FOLDER);
		String LIB_NAME = "libtinyWRAP.so";

		String libPath = String.format("%s/%s", LIBS_FOLDER, LIB_NAME);
		// Load the library
		System.load(libPath);
		// Initialize the engine
		NgnEngine.initialize();

	}

	@Override
	public void onCreate() {
		super.onCreate();
		initEngine();
	}
	
}
