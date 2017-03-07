package taxinonstop.iknow72.com.spentio;

import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {

	UsageStats usageStats;
	long timeInForeground=500;
	String packageName;
	final int MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS=1;
	RecyclerView recyclerView;
	String TAG="Spentio";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if(!hasPermission()){
			startActivityForResult(
					new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS),
					MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS);
		}
		Log.i(TAG,"granted " +String.valueOf(hasPermission()));

		recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		initUsageStats();
	}


	private boolean hasPermission() {
		AppOpsManager appOps = (AppOpsManager)
				getSystemService(APP_OPS_SERVICE);
		int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
				android.os.Process.myUid(), getPackageName());
		return mode == AppOpsManager.MODE_ALLOWED;
	}

	private void initUsageStats() {
		UsageStatsManager usageStatsManager=(UsageStatsManager)getSystemService(USAGE_STATS_SERVICE);
		long currentMillis=System.currentTimeMillis();
		List<UsageStats> statsList=usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_YEARLY,0,currentMillis);
		Log.i(TAG,"size "+statsList.size());
		Log.i(TAG,statsList.toString());

		if(statsList!=null){
			SortedMap<String,UsageStats> mySortedMap = new TreeMap<String,UsageStats>();

			List<UsageData> usageDataList=new ArrayList<>();
			String dateFormat="dd/MM/yyyy hh:mm:ss";
			for(UsageStats usageStats:statsList){
				timeInForeground=usageStats.getTotalTimeInForeground();
				packageName=usageStats.getPackageName();

				int minutes = (int) ((timeInForeground / (1000*60)) % 60);
				int seconds = (int) (timeInForeground / 1000) % 60 ;
				int hours   = (int) ((timeInForeground / (1000*60*60)) % 24);
				Log.i(TAG, "PackageName is"+packageName +"Time is: "+hours+"h"+":"+minutes+"m"+seconds+"s");
				String duration=hours+"h"+":"+minutes+"m"+seconds+"s";

				final PackageManager pm = getApplicationContext().getPackageManager();


				ApplicationInfo ai;
				try {
					ai = pm.getApplicationInfo( packageName, 0);
				} catch (final PackageManager.NameNotFoundException e) {
					ai = null;
				}
				if(ai!=null) {
					if (!((ai.flags & (ApplicationInfo.FLAG_UPDATED_SYSTEM_APP | ApplicationInfo.FLAG_SYSTEM)) > 0)) {


						String title = (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");

						long lastUsageLong = usageStats.getLastTimeStamp();
						String lastUsage = new SimpleDateFormat(dateFormat).format(lastUsageLong);

						Drawable icon = null;
						try {
							icon = pm.getApplicationIcon(packageName);
						} catch (PackageManager.NameNotFoundException e) {
							e.printStackTrace();
						}

						usageDataList.add(new UsageData(title, lastUsage, duration, icon));
					}
				}
			}
			Log.i(TAG,"usdl size "+usageDataList.size());
			recyclerView.setAdapter(new UsageAdapter(usageDataList,this));
		}
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode){
			case MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS:
				if (hasPermission()){
					initUsageStats();
				}else{
					requestPermission();
				}
				break;
		}
	}


	private void requestPermission(){

	}
}
