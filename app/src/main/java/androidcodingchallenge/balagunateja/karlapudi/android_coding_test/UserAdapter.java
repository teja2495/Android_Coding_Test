package androidcodingchallenge.balagunateja.karlapudi.android_coding_test;

/*
Created by
Bala Guna Teja Karlapudi
*/

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    List<User> userList;

    public UserAdapter(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d("demo", "Hello");
        holder.firstName.setText(userList.get(position).getFirstName());
        holder.lastName.setText(userList.get(position).getLastName());
        if (!userList.get(position).getImageURL().equals(""))
            Picasso.get().load(userList.get(position).getImageURL()).into(holder.userImage);
        else
            Picasso.get().load("http://noodleblvd.com/wp-content/uploads/2016/10/No-Image-Available.jpg").into(holder.userImage);

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView userImage;
        TextView firstName;
        TextView lastName;

        public ViewHolder(View itemView) {
            super(itemView);

            firstName = itemView.findViewById(R.id.firstName);
            lastName = itemView.findViewById(R.id.lastName);
            userImage = itemView.findViewById(R.id.userImage);

        }
    }

}
