package coffee.util.view;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

public class ViewParceler implements Parcelable {

	private View view;
	
	private ViewParceler(){
	}
	
	public ViewParceler(View view){
		this.view = view;
	}
	
	public static final Parcelable.Creator<ViewParceler> CERATOR  = new Creator<ViewParceler>() {

		@Override
		public ViewParceler createFromParcel(Parcel source) {
			ViewParceler vp = new ViewParceler();
			vp.view = (View) source.readValue(View.class.getClassLoader());
			return vp;
		}

		@Override
		public ViewParceler[] newArray(int size) {
			return new ViewParceler[size];
		}
	};
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(view);
	}

}
