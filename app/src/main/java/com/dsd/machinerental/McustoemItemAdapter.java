package com.dsd.machinerental;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class McustoemItemAdapter extends ArrayAdapter<CustomSpinnerItem> {
    public McustoemItemAdapter(@NonNull Context context, ArrayList<CustomSpinnerItem> custoemItem) {
        super(context, 0,custoemItem);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initializeView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initializeView(position, convertView, parent);
    }
    private View initializeView(int position,View convertView, ViewGroup parent){
        if (convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.mcustom_spinner_row,parent,false);
        }
       // ImageView ItemImage = convertView.findViewById(R.id.custome_spinner_image);
        TextView ItemName = convertView.findViewById(R.id.custome_spinner_name);
        CustomSpinnerItem currentSpinnerItem = getItem(position);

        if(currentSpinnerItem!=null) {
            //ItemImage.setImageResource(currentSpinnerItem.getmCustomeItemImage());
            ItemName.setText(currentSpinnerItem.getmCustomItemName());
        }
        return convertView;
    }
}
