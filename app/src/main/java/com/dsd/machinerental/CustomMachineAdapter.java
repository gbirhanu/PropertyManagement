package com.dsd.machinerental;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Gemechu
 */

public class CustomMachineAdapter extends RecyclerView.Adapter<CustomMachineAdapter.ProductViewHolder> {


    private Context mCtx;
    private List<Machine> productList;
    private String mName,mPhone,jString;
    private int mUsrIds;

    public CustomMachineAdapter(Context mCtx, List<Machine> productList,String jString) {
        this.mCtx = mCtx;
        this.productList = productList;
        this.jString = jString;


    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.machine_house_list, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, final int position) {
        final Machine machine = productList.get(position);
        //loading the image
        Glide.with(mCtx)
                .load(machine.getWithorWithoutDrivers())
                .into(holder.imageView);
        // Picasso.get().load(house.getImage()).placeholder(R.color.cardview_dark_background).fit().error(R.color.cardview_dark_background).fit().into(holder.imageView);

        // String houseImageString = house.getImage();

        //byte[] imageAsBytes = Base64.decode(houseImageString.getBytes(), Base64.DEFAULT);
        // Bitmap bitmap = BitmapFactory.decodeByteArray(imageAsBytes,0,imageAsBytes.length);
        //ByteArrayOutputStream stream = new ByteArrayOutputStream();
        //bitmap.compress(Bitmap.CompressFormat.PNG, 70, stream);
        //holder.imageView.setImageBitmap(bitmap);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUsrIds = machine.getMachineId();
                Intent intent = new Intent(mCtx,ImageShower.class);
                intent.putExtra("id",mUsrIds);
                intent.putExtra("type","machine");
                mCtx.startActivity(intent);
            }
        });
        holder.mViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  holder.textViewUphone.setVisibility(View.VISIBLE);
                holder.textViewUname.setVisibility(View.VISIBLE);
                */
                JSONObject jsonObject = null;
                ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
                try {
                    jsonObject = new JSONObject(jString);
                    JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
                    JSONObject jo = result.getJSONObject(position);
                    Intent intent =new Intent(mCtx,MachineDetails.class);
                    intent.putExtra("JsonObject",jo.toString());
                    mCtx.startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        holder.textViewUname.setText(mCtx.getString(R.string.owned_by)+": "+machine.getPrice());
        holder.textViewUphone.setText(mCtx.getString(R.string.contact)+": "+machine.getMachineDescription());
        holder.machineTypetext.setText(mCtx.getString(R.string.machinery_type_txt)+": "+machine.getmUname());
        holder.mForWhat.setText(mCtx.getString(R.string.is_driver_available)+": "+machine.getMachineType());
        holder.machineModelText.setText(mCtx.getString(R.string.machine_model)+": "+machine.getmPhone());
        holder.buildingYearText.setText(mCtx.getString(R.string.built_year_txt)+": "+machine.getMachineModel());
        holder.textViewPrice.setText(mCtx.getString(R.string.price)+": "+String.valueOf(machine.getImage())+"  "+mCtx.getString(R.string.currency));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView machineModelText, mForWhat,machineTypetext, buildingYearText, textViewPrice,textViewUname,textViewUphone;
        ImageView imageView;
        Button mViewAll;
        public ProductViewHolder(View itemView) {
            super(itemView);
            textViewUname=itemView.findViewById(R.id.ownerName);
            textViewUphone=itemView.findViewById(R.id.ownerPhones);
            mViewAll =itemView.findViewById(R.id.ownerInfo);
            machineTypetext = itemView.findViewById(R.id.machineTypes);
            machineModelText = itemView.findViewById(R.id.machineModelss);
            buildingYearText = itemView.findViewById(R.id.buildingYear);
            textViewPrice = itemView.findViewById(R.id.machinePrices);
            mForWhat  = itemView.findViewById(R.id.forWhat);
            imageView = itemView.findViewById(R.id.imageView);

        }
    }

}
