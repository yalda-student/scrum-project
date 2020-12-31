package ir.scrumproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.scrumproject.R;
import ir.scrumproject.api.Group;

/**
 * Scrum Project
 * Created by yalda mohasseli  on  12/10/2020.
 */
public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {

    private final Context context;
    private List<Group> groupList;

    public GroupAdapter(Context context, List<Group> groupList) {
        this.context = context;
        this.groupList = groupList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.group_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.groupName.setText(groupList.get(position).getName());
        holder.groupStatus.setText(groupList.get(position).getAvatar());
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView groupIcon;
        TextView groupName;
        TextView groupStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            groupIcon = itemView.findViewById(R.id.groupImageView);
            groupName = itemView.findViewById(R.id.group_name);
            groupStatus = itemView.findViewById(R.id.group_status);

        }
    }
}
