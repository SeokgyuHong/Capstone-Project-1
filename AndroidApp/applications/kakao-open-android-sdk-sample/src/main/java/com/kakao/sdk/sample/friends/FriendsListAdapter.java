package com.kakao.sdk.sample.friends;

import android.app.Application;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.kakao.friends.response.model.AppFriendInfo;
import com.kakao.sdk.sample.R;
import com.kakao.sdk.sample.common.GlobalApplication;

import java.util.List;

/**
 * @author leoshin, created at 15. 7. 29..
 */
public class FriendsListAdapter extends BaseAdapter {
    private final IFriendListCallback listener;

    public interface IFriendListCallback {
        void onItemSelected(int position, AppFriendInfo friendInfo);

        void onPreloadNext();
    }

    private class ViewHolder {
        NetworkImageView profileView;
        TextView nickName;
    }

    private List<? extends AppFriendInfo> items = null;

    public FriendsListAdapter(List<? extends AppFriendInfo> items, IFriendListCallback listener) {
        this.items = items;
        this.listener = listener;
    }

    public void setItem(List<? extends AppFriendInfo> items) {
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public AppFriendInfo getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (position == getCount() - 5) {
            if (listener != null) {
                listener.onPreloadNext();
            }
        }

        View layout = convertView;
        ViewHolder holder;
        if (layout == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            layout = inflater.inflate(R.layout.view_friend_item, parent, false);
            holder = new ViewHolder();
            holder.profileView = layout.findViewById(R.id.profile_image);
            holder.profileView.setDefaultImageResId(R.drawable.thumb_story);
            holder.profileView.setErrorImageResId(R.drawable.thumb_story);
            holder.nickName = layout.findViewById(R.id.nickname);

            layout.setTag(holder);
        } else {
            holder = (ViewHolder) layout.getTag();
        }

        final AppFriendInfo info = getItem(position);
        Application app = GlobalApplication.getGlobalApplicationContext();
        String profileUrl = info.getProfileThumbnailImage();
        if (profileUrl != null && profileUrl.length() > 0) {
            holder.profileView.setImageUrl(profileUrl, ((GlobalApplication) app).getImageLoader());
        } else {
            holder.profileView.setImageResource(R.drawable.thumb_story);
        }

        String nickName = position + " " + info.getProfileNickname();
        if (nickName.length() > 0) {
            holder.nickName.setVisibility(View.VISIBLE);
            holder.nickName.setText(nickName);
        } else {
            holder.nickName.setVisibility(View.INVISIBLE);
        }

        layout.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemSelected(position, info);
            }
        });
        return layout;
    }
}
