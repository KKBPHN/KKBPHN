package com.miui.internal.view.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.miui.internal.R;
import java.util.ArrayList;

public class ImmersionMenuAdapter extends BaseAdapter {
    private ArrayList mAvailableItems = new ArrayList();
    private Context mContext;
    private LayoutInflater mInflater;

    class ViewHolder {
        ImageView icon;
        TextView title;

        private ViewHolder() {
        }
    }

    protected ImmersionMenuAdapter(Context context, Menu menu) {
        this.mInflater = LayoutInflater.from(context);
        buildVisibleItems(menu, this.mAvailableItems);
        this.mContext = context;
    }

    private void buildVisibleItems(Menu menu, ArrayList arrayList) {
        arrayList.clear();
        if (menu != null) {
            int size = menu.size();
            for (int i = 0; i < size; i++) {
                MenuItem item = menu.getItem(i);
                if (checkMenuItem(item)) {
                    arrayList.add(item);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean checkMenuItem(MenuItem menuItem) {
        return menuItem.isVisible();
    }

    public int getCount() {
        return this.mAvailableItems.size();
    }

    public MenuItem getItem(int i) {
        return (MenuItem) this.mAvailableItems.get(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView;
        int i2 = 0;
        if (view == null) {
            view = this.mInflater.inflate(R.layout.immersion_popup_menu_item, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.icon = (ImageView) view.findViewById(16908294);
            viewHolder.title = (TextView) view.findViewById(16908308);
            view.setTag(viewHolder);
        }
        ImmersionMenuAdapterHelper.calcAndSetPadding(this.mContext, getCount(), i, view);
        Object tag = view.getTag();
        if (tag != null) {
            ViewHolder viewHolder2 = (ViewHolder) tag;
            MenuItem item = getItem(i);
            if (item.getIcon() != null) {
                viewHolder2.icon.setImageDrawable(item.getIcon());
                imageView = viewHolder2.icon;
            } else {
                imageView = viewHolder2.icon;
                i2 = 8;
            }
            imageView.setVisibility(i2);
            viewHolder2.title.setText(item.getTitle());
        }
        return view;
    }

    public void update(Menu menu) {
        buildVisibleItems(menu, this.mAvailableItems);
        notifyDataSetChanged();
    }
}
