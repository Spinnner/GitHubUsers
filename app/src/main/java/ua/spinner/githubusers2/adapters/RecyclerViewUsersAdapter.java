package ua.spinner.githubusers2.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import ua.spinner.githubusers2.R;
import ua.spinner.githubusers2.pojo.User;

/**
 * Created by Spinner on 3/11/2017.
 */

public class RecyclerViewUsersAdapter extends RecyclerView.Adapter<RecyclerViewUsersAdapter.RecyclerViewHolder> {

    private List<User> listUsers;
    private Context context;


    public RecyclerViewUsersAdapter(Context context, List<User> listUsers){
        this.context = context;
        this.listUsers = listUsers;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.item_user, viewGroup, false);
        RecyclerViewHolder viewHolder = new RecyclerViewHolder(mainGroup);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        User user = listUsers.get(position);
        String avatar = user.getAvatar();
        String login = user.getLogin();
        String type = user.getType();

        Picasso.with(context).load(avatar).placeholder(R.drawable.profile_photo).into(holder.ivAvatar);
        holder.tvLogin.setText(context.getText(R.string.login) + " " + login);
        holder.tvType.setText(context.getText(R.string.type) + " " + type);
    }

    @Override
    public int getItemCount() {
        return (null != listUsers ? listUsers.size() : 0);
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder  {

        @BindView(R.id.ivAvatar) CircleImageView ivAvatar;
        @BindView(R.id.textViewLogin) TextView tvLogin;
        @BindView(R.id.textViewType) TextView tvType;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
