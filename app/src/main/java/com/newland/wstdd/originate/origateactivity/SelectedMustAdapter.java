package com.newland.wstdd.originate.origateactivity;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.newland.wstdd.R;
import com.newland.wstdd.originate.beanrequest.SelectMustItemInfo;

/**
 * 活动的时候必填项
 *
 * @author Administrator
 */
public class SelectedMustAdapter extends BaseAdapter {

    private ArrayList<SelectMustItemInfo> arrayList;
    private LayoutInflater layoutInflater;
    private Context context;

    public SelectedMustAdapter(Context context, ArrayList<SelectMustItemInfo> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return arrayList == null ? 0 : arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        if (arrayList != null && arrayList.size() != 0) {
            return arrayList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"NewApi", "ResourceAsColor"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.selected_must_adapter_item, parent, false);
            holder = new Holder();
            holder.button = (TextView) convertView.findViewById(R.id.selected_must_item);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        if (arrayList.get(position).getSelectItem() == null) {
            holder.button.setText("+");
            holder.button.setTextSize(18);
            holder.button.setTextColor(context.getResources().getColor(R.color.lightgray));
        } else {
            holder.button.setText(arrayList.get(position).getSelectItem());
        }
        holder.button.setTextColor(R.color.lightgray);
        if (arrayList.get(position).isSelect()) {
            holder.button.setBackground(context.getResources().getDrawable(R.drawable.text_originate_chair_mustselect_style));
            holder.button.setTextColor(context.getResources().getColor(R.color.text_red));
            ;

        } else {
            holder.button.setTextColor(context.getResources().getColor(R.color.lightgray));
            ;
            holder.button.setBackground(context.getResources().getDrawable(R.drawable.text_originate_chair_mustnormal_style));
        }
        return convertView;
    }

    class Holder {
        TextView button;
    }

    public ArrayList<SelectMustItemInfo> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<SelectMustItemInfo> arrayList) {
        this.arrayList = arrayList;
    }

}
