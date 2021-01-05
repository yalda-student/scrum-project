package ir.scrumproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.scrumproject.R;
import ir.scrumproject.api.Member;

/**
 * Scrum Project
 * Created by yalda mohasseli  on  1/5/2021.
 */
public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {

    private final Context context;
    private List<Member> memberList;

    public MemberAdapter(Context context, List<Member> memberList) {
        this.context = context;
        this.memberList = memberList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.member_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Member member = memberList.get(position);
        holder.username.setText(member.getUser().getUsername());
        holder.role.setText(member.getRole());
    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }

    public void setMemberList(List<Member> memberList) {
        this.memberList = memberList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView username;
        TextView role;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.member_name);
            role = itemView.findViewById(R.id.member_role);
        }
    }
}
