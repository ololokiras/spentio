package taxinonstop.iknow72.com.spentio;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by root on 07.03.17.
 */

public class UsageAdapter extends RecyclerView.Adapter<UsageAdapter.ViewHolder> {

	List<UsageData> usageDataList;

	Context ctx;

	public UsageAdapter(List<UsageData> usageDataList,Context ctx){
		this.usageDataList=usageDataList;
		this.ctx=ctx;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.usage_info_item,parent,false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(UsageAdapter.ViewHolder holder, int position) {
		holder.setData(usageDataList.get(position));
	}

	@Override
	public int getItemCount() {
		return usageDataList.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder{
		TextView title;
		TextView duration;
		TextView lastUsage;
		ImageView image;
		public ViewHolder(View itemView) {
			super(itemView);
		}

		public void setData(UsageData usageData){
			title=(TextView)itemView.findViewById(R.id.title);
			duration=(TextView)itemView.findViewById(R.id.duration);
			lastUsage=(TextView)itemView.findViewById(R.id.lastUsage);
			image=(ImageView)itemView.findViewById(R.id.image);

			title.setText(usageData.title);
			duration.setText(usageData.duration);
			lastUsage.setText(usageData.lastUsage);
			image.setImageDrawable(usageData.icon);
		}
	}
}
