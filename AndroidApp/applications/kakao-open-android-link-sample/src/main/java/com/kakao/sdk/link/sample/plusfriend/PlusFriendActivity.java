package com.kakao.sdk.link.sample.plusfriend;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.plusfriend.PlusFriendService;
import com.kakao.sdk.link.sample.BuildConfig;
import com.kakao.sdk.link.sample.R;
import com.kakao.util.exception.KakaoException;

public class PlusFriendActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plus_friend);

        final String[] methods = new String[] {
                getString(R.string.text_add_plus_friend),
                getString(R.string.text_chat_plus_friend),
        };

        final int[] images = new int[] {
                R.drawable.icon_feed,
                R.drawable.icon_list,
        };

        ListView listView = findViewById(R.id.plus_friend_method_list);
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return methods.length;
            }

            @Override
            public Object getItem(int position) {
                return methods[position];
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater mInflater = (LayoutInflater) PlusFriendActivity.this
                        .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

                if (convertView == null && mInflater != null) {
                    convertView = mInflater.inflate(R.layout.menu_item, parent, false);
                    ImageView imageView = convertView.findViewById(R.id.method_image);
                    TextView textView = convertView.findViewById(R.id.method_text);
                    imageView.setImageDrawable(getResources().getDrawable(images[position]));
                    textView.setText(methods[position]);
                }
                return convertView;
            }
        });
        listView.setOnItemClickListener((parent, view, position, id) -> {
            switch (position) {
                case 0:
                    addFriend();
                    break;
                case 1:
                    chat();
                    break;
            }
        });
    }

    private String plusFriendId() {
        switch (BuildConfig.FLAVOR) {
            case "dev":
            case "sandbox":
                return "_Txaxj";
            case "cbt":
            case "production":
            default:
                return "_xcLqmC";
        }
    }

    private void addFriend() {
        try {
            PlusFriendService.getInstance().addFriend(PlusFriendActivity.this, plusFriendId());
        } catch (KakaoException e) {
            // 에러 처리
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void chat() {
        try {
            PlusFriendService.getInstance().chat(this, plusFriendId());
        } catch (KakaoException e) {
            // 에러 처리
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
