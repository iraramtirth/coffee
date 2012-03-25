package coffee.imsdroid;

import org.doubango.ngn.NgnEngine;
import org.doubango.ngn.model.NgnHistoryEvent.StatusType;
import org.doubango.ngn.model.NgnHistorySMSEvent;
import org.doubango.ngn.services.INgnSipService;
import org.doubango.ngn.sip.NgnMessagingSession;
import org.doubango.ngn.utils.NgnUriUtils;

import android.os.Bundle;

public class MainActivity extends BaseActivity {

	String TAG = MainActivity.class.getCanonicalName();

	private NgnEngine engine;
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