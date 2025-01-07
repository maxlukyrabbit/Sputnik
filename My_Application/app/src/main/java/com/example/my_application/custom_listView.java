package com.example.my_application;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class custom_listView extends BaseAdapter {
    private final Context context;
    private final ArrayList<Note_class> items;

    public custom_listView(Context context, ArrayList<Note_class> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_custom_list_view, parent, false);
        TextView id_panel = view.findViewById(R.id.panel_id);
        TextView number = view.findViewById(R.id.number);
        number.setText(String.valueOf(this.items.get(position).number));

        id_panel.setText(this.items.get(position).id_panel);

        return view;
    }
}
