package taxinonstop.iknow72.com.spentio;

import android.graphics.drawable.Drawable;

/**
 * Created by root on 07.03.17.
 */

public class UsageData {
	public String title;
	public String lastUsage;
	public String duration;
	public Drawable icon;

	public UsageData(String title, String lastUsage, String duration, Drawable icon){
		this.title=title;
		this.lastUsage=lastUsage;
		this.duration=duration;
		this.icon=icon;
	}

}
