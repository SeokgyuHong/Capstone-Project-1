package com.kakao.sdk.link.sample.common.widget;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.kakao.sdk.link.sample.R;

import java.util.List;

/**
 * @author leoshin
 * Created by leoshin on 15. 6. 18..
 */
public class KakaoSpinnerAdapter extends BaseAdapter {
    public interface ISpinnerListener {
        int getSelectedItemPosition();

        void onItemSelected(BaseAdapter adapter, int position);
    }


    public static class KakaoSpinnerItems {
        private final List<String> titleList;
        private final int iconResId;

        public KakaoSpinnerItems(int iconResId, List<String> titleList) {
            this.titleList = titleList;
            this.iconResId = iconResId;
        }

        public String getTitle(int position) {
            return titleList.get(position);
        }

        public int getIconResId() {
            return iconResId;
        }

        public int getSize() {
            return titleList.size();
        }
    }

    private final KakaoSpinnerItems items;
    private final ISpinnerListener listener;

    public KakaoSpinnerAdapter(KakaoSpinnerItems items, ISpinnerListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return items.getSize();
    }

    @Override
    public Object getItem(int position) {
        return items.getTitle(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View item = convertView;
        if (item == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            item = inflater.inflate(R.layout.view_spinner_item, parent, false);
        }

        TextView title = item.findViewById(R.id.menu_title);
        title.setText(items.getTitle(position));

        if (listener != null) {
            final CheckBox checked = item.findViewById(R.id.menu_checkbox);
            int selectedPosition = listener.getSelectedItemPosition();
            checked.setChecked(selectedPosition == position);
            item.setOnClickListener(v -> listener.onItemSelected(KakaoSpinnerAdapter.this, position));
        }
        return item;
    }
}
