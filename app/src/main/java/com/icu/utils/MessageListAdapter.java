package com.icu.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.icu.ApplicationLoader;
import com.icu.R;
import com.icu.database.Message;
import com.icu.widgets.CircularImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sitaram on 10/7/16.
 */

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.ViewHolder> {
    private Context context;
    private List<Message> messages;
    private static String mapPreviewUrl = "http://maps.google.com/maps/api/staticmap?zoom=15&size=400x200&sensor=false&center=";

    public MessageListAdapter(Context context) {
        this.context = context;
        messages = new ArrayList<>();

    }

    public void updateMessages(List<Message> messages) {

        this.messages.addAll(messages);
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return messages != null ? messages.size() : 0;
    }

    @Override
    public MessageListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false));
    }

    @Override
    public void onBindViewHolder(MessageListAdapter.ViewHolder holder, int position) {
        final Message order = messages.get(position);
        if (order != null) {
            holder.tvUserName.setText(order.getUserId());
            holder.icon.setImageUrl(mapPreviewUrl + order.getLatitude() + "," + order.getLongitude(), ApplicationLoader.getInstance().getImageLoader());
            holder.profileImage.setDefaultImageResId(R.drawable.profile);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private NetworkImageView icon;
        private CircularImageView profileImage;
        private TextView tvUserName;

        public ViewHolder(View view) {
            super(view);
            icon = (NetworkImageView) view.findViewById(R.id.mapLocation);
            profileImage = (CircularImageView) view.findViewById(R.id.profileImage);
            tvUserName = (TextView) view.findViewById(R.id.tvUserName);

        }
    }

}