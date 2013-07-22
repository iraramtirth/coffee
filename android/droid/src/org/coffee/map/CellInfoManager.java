package org.coffee.map;

import java.lang.reflect.Method;
import java.util.List;

import org.coffee.App;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
/**
 * 
 * @author coffee <br>
 *          2013-7-22上午9:40:56
 */
public class CellInfoManager {
	private int cid; // cellId
	private int lac; // location area code
	private int mcc; // mobile country code
	private int mnc; // mobile network code
	private int lat; // 纬度 getBaseStationLatitude
	private int lng; // 精度getBaseStationLongitude

	private int asu;
	private int bid;
	private int nid;
	private int sid;

	private TelephonyManager tel;

	private boolean valid;
	private boolean isCdma;
	private boolean isGsm;

	public CellInfoManager() {
		tel = (TelephonyManager) App.getContext().getSystemService(
				Context.TELEPHONY_SERVICE);
		// this.listener = new CellInfoListener(this);
		// this.tel.listen(this.listener,
		// PhoneStateListener.LISTEN_CELL_LOCATION
		// | PhoneStateListener.LISTEN_SIGNAL_STRENGTH);
	}

	public static int dBm(int i) {
		int j;
		if (i >= 0 && i <= 31)
			j = i * 2 + -113;
		else
			j = 0;
		return j;
	}

	public int asu() {
		return this.asu;
	}

	public int bid() {
		if (!this.valid)
			update();
		return this.bid;
	}

	public JSONObject cdmaInfo() {
		if (!isCdma()) {
			return null;
		}
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("bid", bid());
			jsonObject.put("sid", sid());
			jsonObject.put("nid", nid());
			jsonObject.put("lat", lat());
			jsonObject.put("lng", lng());
		} catch (JSONException ex) {
			jsonObject = null;
		}
		return jsonObject;
	}

	public JSONArray cellTowers() {
		JSONArray jsonarray = new JSONArray();
		if (!hasIccCard()) {
			return jsonarray;
		}

		int lat;
		int mcc;
		int mnc;
		int aryCell[] = dumpCells();
		lat = lac();
		mcc = mcc();
		mnc = mnc();
		if (aryCell == null || aryCell.length < 2) {
			aryCell = new int[2];
			aryCell[0] = cid;
			aryCell[1] = -60;
		}
		for (int i = 0; i < aryCell.length; i += 2) {
			try {
				int j2 = dBm(i + 1);
				JSONObject jsonobject = new JSONObject();
				jsonobject.put("cell_id", aryCell[i]);
				jsonobject.put("location_area_code", lat);
				jsonobject.put("mobile_country_code", mcc);
				jsonobject.put("mobile_network_code", mnc);
				jsonobject.put("signal_strength", j2);
				jsonobject.put("age", 0);
				jsonarray.put(jsonobject);
			} catch (Exception ex) {
				ex.printStackTrace();
				Log.e("CellInfoManager",
						"cellTowers Exception" + ex.getMessage());
			}
		}
		if (isCdma()) {
			jsonarray = new JSONArray();
			CdmaCellLocation location = (CdmaCellLocation) tel
					.getCellLocation();
			int cellIDs = location.getBaseStationId();
			int networkID = location.getNetworkId();
			StringBuilder nsb = new StringBuilder();
			nsb.append(location.getSystemId());
			try {
				JSONObject jsonobject = new JSONObject();
				jsonobject.put("cell_id", cellIDs);
				jsonobject.put("location_area_code", networkID);
				jsonobject.put("mobile_country_code", tel.getNetworkOperator()
						.substring(0, 3));
				jsonobject.put("mobile_network_code", nsb.toString());
				jsonobject.put("age", 0);
				jsonarray.put(jsonobject);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return jsonarray;

	}

	public int cid() {
		if (!this.valid)
			update();
		return this.cid;
	}

	public int[] dumpCells() {
		int[] aryCells;

		List<NeighboringCellInfo> lsCellInfo = this.tel
				.getNeighboringCellInfo();
		if (lsCellInfo == null || lsCellInfo.size() == 0) {
			return new int[] { cid() };
		}
		aryCells = new int[lsCellInfo.size() * 2 + 2];
		aryCells[0] = cid();
		aryCells[1] = asu();
		int m = 2;
		for (NeighboringCellInfo cellInfo : lsCellInfo) {
			if (cellInfo.getCid() <= 0 || cellInfo.getCid() == 65535) {
				continue;
			}
			aryCells[m++] = cellInfo.getCid();
			aryCells[m++] = cellInfo.getRssi();
		}
		return aryCells;

	}

	public JSONObject gsmInfo() {
		if (!isGsm()) {
			return null;
		}
		while (true) {
			try {
				JSONObject localJSONObject1 = new JSONObject();
				String str1 = this.tel.getNetworkOperatorName();
				localJSONObject1.put("operator", str1);
				String str2 = this.tel.getNetworkOperator();
				if ((str2.length() == 5) || (str2.length() == 6)) {
					String str3 = str2.substring(0, 3);
					String str4 = str2.substring(3, str2.length());
					localJSONObject1.put("mcc", str3);
					localJSONObject1.put("mnc", str4);
				}
				localJSONObject1.put("lac", lac());
				int[] arrayOfInt = dumpCells();
				JSONArray localJSONArray1 = new JSONArray();
				int k = 0;
				int m = arrayOfInt.length / 2;
				while (true) {
					if (k >= m) {
						localJSONObject1.put("cells", localJSONArray1);
						break;
					}
					int n = k * 2;
					int i1 = arrayOfInt[n];
					int i2 = k * 2 + 1;
					int i3 = arrayOfInt[i2];
					JSONObject localJSONObject7 = new JSONObject();
					localJSONObject7.put("cid", i1);
					localJSONObject7.put("asu", i3);
					localJSONArray1.put(localJSONObject7);
					k += 1;
				}
			} catch (JSONException localJSONException) {
			}
		}
	}

	public boolean isCdma() {
		if (!this.valid)
			update();
		return this.isCdma;
	}

	public boolean isGsm() {
		if (!this.valid)
			update();
		return this.isGsm;
	}

	public int lac() {
		if (!this.valid)
			update();
		return this.lac;
	}

	public int lat() {
		if (!this.valid)
			update();
		return this.lat;
	}

	public int lng() {
		if (!this.valid)
			update();
		return this.lng;
	}

	public int mcc() {
		if (!this.valid)
			update();
		return this.mcc;
	}

	public int mnc() {
		if (!this.valid)
			update();
		return this.mnc;
	}

	public int nid() {
		if (!this.valid)
			update();
		return this.nid;
	}

	public float score() {
		float f1 = 0f;
		int[] aryCells = null;
		int i = 0;
		float f2 = 0f;
		if (isCdma()) {
			f2 = 1065353216;
			return f2;
		}
		if (isGsm()) {
			f1 = 0.0F;
			aryCells = dumpCells();
			int j = aryCells.length;
			if (i >= j)
				f2 = f1;
		}
		if (i <= 0) {
			return 1065353216;
		}
		int m = aryCells[i];
		for (i = 0; i < m; i++) {
			if ((m < 0) || (m > 31))
				f1 += 0.5F;
			else
				f1 += 1.0F;
		}
		f2 = f1;

		return f2;
	}

	public int sid() {
		if (!this.valid)
			update();
		return this.sid;
	}

	public boolean hasIccCard() {
		return (tel.hasIccCard() || tel.getCellLocation() != null);
	}

	public void update() {
		this.isGsm = false;
		this.isCdma = false;
		this.cid = -1;
		this.lac = -1;
		this.mcc = 0;
		this.mnc = 0;
		CellLocation cellLocation = this.tel.getCellLocation();
		int nPhoneType = this.tel.getPhoneType();
		if (nPhoneType == 1 && cellLocation instanceof GsmCellLocation) {
			this.isGsm = true;
			GsmCellLocation gsmCellLocation = (GsmCellLocation) cellLocation;
			int nGSMCID = gsmCellLocation.getCid();
			if (nGSMCID > 0) {
				if (nGSMCID != 65535) {
					this.cid = nGSMCID;
					this.lac = gsmCellLocation.getLac();
				}
			}
		}
		try {
			String strNetworkOperator = this.tel.getNetworkOperator();
			int nNetworkOperatorLength = strNetworkOperator.length();
			if (nNetworkOperatorLength != 5) {
				if (nNetworkOperatorLength != 6)
					;
			} else {
				this.mcc = Integer.parseInt(strNetworkOperator.substring(0, 3));
				this.mnc = Integer.parseInt(strNetworkOperator.substring(3,
						nNetworkOperatorLength));
			}
			if (this.tel.getPhoneType() == 2) {
				this.valid = true;
				Class<?> clsCellLocation = cellLocation.getClass();
				Class<?>[] aryClass = new Class[0];
				Method localMethod1 = clsCellLocation.getMethod(
						"getBaseStationId", aryClass);
				Method localMethod2 = clsCellLocation.getMethod("getSystemId",
						aryClass);
				Method localMethod3 = clsCellLocation.getMethod("getNetworkId",
						aryClass);
				Object[] aryDummy = new Object[0];
				this.bid = ((Integer) localMethod1.invoke(cellLocation,
						aryDummy)).intValue();
				this.sid = ((Integer) localMethod2.invoke(cellLocation,
						aryDummy)).intValue();
				this.nid = ((Integer) localMethod3.invoke(cellLocation,
						aryDummy)).intValue();
				Method localMethod7 = clsCellLocation.getMethod(
						"getBaseStationLatitude", aryClass);
				Method localMethod8 = clsCellLocation.getMethod(
						"getBaseStationLongitude", aryClass);
				this.lat = ((Integer) localMethod7.invoke(cellLocation,
						aryDummy)).intValue();
				this.lng = ((Integer) localMethod8.invoke(cellLocation,
						aryDummy)).intValue();
				this.isCdma = true;
			}
		} catch (Exception ex) {
			Log.e("CellInfoManager", "update exception:" + ex.getMessage());
		}
	}

	class CellInfoListener extends PhoneStateListener {
		CellInfoListener(CellInfoManager manager) {

		}

		/**
		 * Callback invoked when device cell location changes.
		 */
		@Override
		public void onCellLocationChanged(CellLocation paramCellLocation) {
			CellInfoManager.this.valid = false;
		}

		@Override
		public void onSignalStrengthChanged(int paramInt) {
			CellInfoManager.this.asu = paramInt;
		}
	}
}
