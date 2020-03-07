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

public class MyCustomMachineAdapter extends RecyclerView.Adapter<MyCustomMachineAdapter.ProductViewHolder> {

    private Context mCtx;
    private List<Machine> productList;
    private String mName,mPhone,jString,hfros,uStatus,function;
    private int mUsrIds;

    public MyCustomMachineAdapter(Context mCtx, List<Machine> productList, String jString, String hfros, String uStatus, String function) {
        this.mCtx = mCtx;
        this.productList = productList;
        this.jString = jString;
        this.hfros = hfros;
        this.uStatus = uStatus;
        this.function = function;
    }

    @Override
    public MyCustomMachineAdapter.ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.machine_house_list, null);
        return new MyCustomMachineAdapter.ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyCustomMachineAdapter.ProductViewHolder holder, final int position) {
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
                //Toast.makeText(mCtx, "User Id"+machine.getUserId(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mCtx, ImageShower.class);
                intent.putExtra("id", mUsrIds);
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
              //  Toast.makeText(mCtx, "User Id  "+machine.getUserId(), Toast.LENGTH_SHORT).show();
                JSONObject jsonObject = null;
                ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
                try {
                    jsonObject = new JSONObject(jString);
                    JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
                    JSONObject jo = result.getJSONObject(position);
                    Intent intent = new Intent(mCtx, UpdateMachinery.class);
                    intent.putExtra("JsonObject", jo.toString());
                    intent.putExtra("ownerId",machine.getUserId()+"");
                    intent.putExtra("mStatus", uStatus);
                    intent.putExtra("func", function);
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



           private String languageTranslate(String value) {
        if (value.equals("Single Family")) {
            return mCtx.getString(R.string.single_family_txt);
        } else if (value.equals("Twine Homes")) {
            return mCtx.getString(R.string.twines_home);
        } else if (value.equals("Town Homes")) {
            return mCtx.getString(R.string.town_homes_txt);
        } else if (value.equals("Apartment")) {
            return mCtx.getString(R.string.apartment);
        } else if (value.equals("Condominiums")) {
            return mCtx.getString(R.string.condo_txt);
        } else if (value.equals("Commercial Space")) {
            return mCtx.getString(R.string.commercial_space_txt);
        } else if (value.equals("Office Space")) {
            return mCtx.getString(R.string.office_space_txt);
        } else if (value.equals("Land/lot")){
            return mCtx.getString(R.string.lang_lot_txt);
        }
        //Bed room
        else if (value.equals("Studio")) {
            return   mCtx.getString(R.string.studio_txt);
        } else if (value.equals("1 Bedroom")) {
            return mCtx.getString(R.string.one_bedroom_txt);
        } else if (value.equals("2 Bedroom")) {
            return mCtx.getString(R.string.two_bedroom_txt);
        } else if (value.equals("3 Bedrooms")) {
            return mCtx.getString(R.string.three_bedroom_txt);
        } else if (value.equals("4 Bedrooms")) {
            return mCtx.getString(R.string.four_bedroom_txt);
        } else if (value.equals("5 Bedrooms")) {
            return String.valueOf(R.string.five_bedroom_txt);
        } else if (value.equals("6 Bedrooms")) {
            return mCtx.getString(R.string.six_bedroom_txt);
        } else if (value.equals("7 Bedrooms")) {
            return mCtx.getString(R.string.seven_bedroom_txt);
        } else if (value.equals("8 Bedrooms")) {
            return mCtx.getString(R.string.eight_bedroom_txt);
        } else if (value.equals("9 Bedrooms")) {
            return mCtx.getString(R.string.nine_bedroom_txt);
        } else if (value.equals("10 and Above")){
            return mCtx.getString(R.string.ten_and_above_bedroom_txt);
        }
        //Construction Type
        else if (value.equals("Wood and mud")){
            return mCtx.getString(R.string.wood_mud);
        }else if (value.equals("Wood and mud + Stucco")){
            return mCtx.getString(R.string.wood_mud_stucco);
        }else if (value.equals("Maison/stone")){
            return mCtx.getString(R.string.myson_stone);
        }else if (value.equals("Cement Blocks + Stucco")){
            return mCtx.getString(R.string.cement_stucco);
        }else if (value.equals("Poured Concrete")){
            return mCtx.getString(R.string.poured_concrete);
        }
        else if (value.equals("rent")){
            return  mCtx.getString(R.string.yemikeray);
        }else  if (value.equals("sale")){
            return  mCtx.getString(R.string.yemishet);
        }
        return null;
    }
    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView machineTypetext,mForWhat, machineModelText, buildingYearText, textViewPrice,textViewUname,textViewUphone;
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
