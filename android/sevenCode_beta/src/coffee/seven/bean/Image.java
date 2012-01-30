package coffee.seven.bean;

import android.os.Parcel;
import android.os.Parcelable;

import coffee.seven.action.SaleService.ImageType;

public class Image implements Parcelable{
	public String netUrl;
	public ImageType type;
	public String viewKey;
	public Image(){
	}
	
	public Image(String netUrl, ImageType type) {
		this.netUrl = netUrl;
		this.type = type;
	}
	
	public Image(String netUrl,String viewKey, ImageType type){
		this.netUrl = netUrl;
		this.type = type;
		this.viewKey = viewKey;
	}
	//java.lang.RuntimeException: Unable to start receiver
	//
	//: Parcelable protocol requires a Parcelable.Creator object called  CREATOR on class com.mmb.shop.bean.Image

	public static final Parcelable.Creator<Image> CREATOR  = new Creator<Image>() {
		@Override
		public Image createFromParcel(Parcel source) {
			Image image = new Image();
			image.netUrl = source.readString();
			image.type = ImageType.valueOf(source.readString());
			image.viewKey = source.readString();
			return image;
		}

		@Override
		public Image[] newArray(int size) {
			return new Image[size];
		}
	};
	
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.netUrl);
		dest.writeString(this.type.name());
		//ImageType.valueOf("0");
		dest.writeString(this.viewKey);
	}
}