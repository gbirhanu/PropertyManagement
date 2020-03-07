package com.dsd.machinerental;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.List;

public class VerticalCustomAdapter extends RecyclerView.Adapter<VerticalCustomAdapter.ProductViewHolder> {


    private Context mCtx;
    private List<House> HouseList;
    private String name,phone;
    private  int mId;

    public VerticalCustomAdapter(Context mCtx, List<House> HouseList) {
        this.mCtx = mCtx;
        this.HouseList = HouseList;
        this.name = name;
        this.phone = phone;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.bighouselist,null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, int position) {
        final House house = HouseList.get(position);
        //loading the image


        Glide.with(mCtx)
                .load(house.getImage())
                .apply(new RequestOptions()
                    .placeholder(R.color.colorPrimary)
                        .dontAnimate().skipMemoryCache(true))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.imageView);
       // final String houseImageString = house.getImage();
       // byte[] imageAsBytes = Base64.decode(houseImageString.getBytes(), Base64.DEFAULT);
       // final String imageTy=house.getType();
       // holder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
        //mIds = String.valueOf(mId);
        holder.textViewUphone.setText("Phone: "+house.getmPhone());
        holder.textViewUname.setText("Owner: "+house.getmUname());
        holder.textViewTitle.setText("Type of the House: "+house.getPtype());
        holder.textViewShortDesc.setText("The house id made of: "+house.getCtype());
        holder.txtHouseLocx.setText("House Size: "+house.getmUname());
        holder.textViewRating.setText("Bedroom: "+String.valueOf(house.getNoofroom()));
        holder.textViewPrice.setText("Price: "+String.valueOf(house.getPrice()+"  ETB"));
        holder.mViewOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.textViewUname.setVisibility(View.VISIBLE);
                holder.textViewUphone.setVisibility(View.VISIBLE);
            }
        });
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mId = house.getId();
                Intent intent =new Intent(mCtx,ImageShower.class);
                intent.putExtra("id",mId);
              //  intent.putExtra("image",houseImageString);
              //  intent.putExtra("imageType",imageTy);
                mCtx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return HouseList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle, textViewShortDesc, txtHouseLocx,textViewRating, textViewPrice,textViewUname,textViewUphone;
        ImageView imageView;
        Button mViewOwner;
        ProgressBar progressBar;
        public ProductViewHolder(View itemView) {
            super(itemView);
             mViewOwner=itemView.findViewById(R.id.ownerInfo);
             progressBar = itemView.findViewById(R.id.homeprogress);
            textViewTitle = itemView.findViewById(R.id.houseTypes);
            textViewShortDesc = itemView.findViewById(R.id.serviceTypes);
            textViewRating = itemView.findViewById(R.id.textViewRating);
            textViewPrice = itemView.findViewById(R.id.housePrices);
            txtHouseLocx = itemView.findViewById(R.id.housLocations);
            textViewUname=itemView.findViewById(R.id.ownerUserName);
            textViewUphone=itemView.findViewById(R.id.ownerPhone);

            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
