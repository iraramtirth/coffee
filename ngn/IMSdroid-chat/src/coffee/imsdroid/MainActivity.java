package coffee.imsdroid;

import org.doubango.ngn.NgnEngine;
import org.doubango.ngn.model.NgnHistoryEvent.StatusType;
import org.doubango.ngn.model.NgnHistorySMSEvent;
import org.doubango.ngn.services.INgnSipService;
import org.doubango.ngn.sip.NgnMessagingSession;
import org.doubango.ngn.sip.NgnMsrpSession;
import org.doubango.ngn.utils.NgnUriUtils;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

	String TAG = MainActivity.class.getCanonicalName();

	private NgnEngine engine;

	private static final String DATA_FOLDER = String.format("/data/data/%s",
			MainActivity.class.getPackage().getName());
	private static final String LIBS_FOLDER = String.format("%s/lib",
			MainActivity.DATA_FOLDER);
	private static final String LIB_NAME = "libtinyWRAP.so";

	 
	static {
		String libPath = String.format("%s/%s", MainActivity.LIBS_FOLDER,
				MainActivity.LIB_NAME);

		// Load the library
		System.load(libPath);
		// Initialize the engine
		NgnEngine.initialize();
	}
	NgnMsrpSession mSession;
	private INgnSipService mSipService;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		engine = NgnEngine.getInstance();
		mSipService = engine.getSipService();
		
		engine.start();
		
		boolean bool = mSipService.register(this);
		System.out.println(bool);
		
		if (true) {
			sendMessage();// /发送信息
		}

	}

	private String sRemoteParty = "245355193";

	private boolean sendMessage() {
		boolean ret = false;
		final String content = "hello world";
		final NgnHistorySMSEvent e = new NgnHistorySMSEvent(sRemoteParty,
				StatusType.Outgoing);
		e.setContent(content);

		boolean isRegister = engine.getSipService().register(this);
		System.out.println(isRegister);
		try {
			final String remotePartyUri = NgnUriUtils.makeValidSipUri(sRemoteParty);
			final NgnMessagingSession imSession = NgnMessagingSession
					.createOutgoingSession(engine.getSipService().getSipStack(),
							remotePartyUri);
			if (!(ret = imSession.sendTextMessage(content))) {
				e.setStatus(StatusType.Failed);
			}
			NgnMessagingSession.releaseSession(imSession);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		return ret;
	}

}