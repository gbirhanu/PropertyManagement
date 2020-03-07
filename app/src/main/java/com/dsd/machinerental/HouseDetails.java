package com.dsd.machinerental;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HouseDetails extends AppCompatActivity {
    TextView textViewTitle, textViewShortDesc, txtHouseLocx, textViewRating, textViewPrice, textViewUname, textViewUphone;
    TextView txtCompoundSizze, txtServiceType, txtHousDesc, txtWaterTank, txtGenerator, txtFull, txtHalf, txtParking;
    TextView txtBuildyear, txtServant, txtServantBath, txtKitchen, txtConstruction, txtimageInfo;
    ImageView imageView;
    Button mViewOwner;
    ProgressBar progressBar;
    private String JSON_STRING;
    List<House> houselists;
    House houses;
    String phoneNo;
    String msBody;
    Toolbar mToolbar;
    //String for language translation
    String homeType, bedRoom, constFrom, homeFor, halfbRoom, fullbRoom, serviceRoom,
            serviceBathroom, haveKitchen, haveParking, haveWatertank, haveGenerator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_details);
        // mToolbar = findViewById(R.id.navigation_action);
        //setSupportActionBar(mToolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mViewOwner = findViewById(R.id.ownerInfo);
        progressBar = findViewById(R.id.homeprogress);
        textViewTitle = findViewById(R.id.houseTypes);
        textViewShortDesc = findViewById(R.id.serviceTypes);
        textViewRating = findViewById(R.id.textViewRating);
        textViewPrice = findViewById(R.id.housePrices);
        txtHouseLocx = findViewById(R.id.buildSize);
        txtCompoundSizze = findViewById(R.id.compSize);
        textViewUname = findViewById(R.id.ownerUserName);
        textViewUphone = findViewById(R.id.ownerPhone);
        txtServiceType = findViewById(R.id.serviceTypes);
        txtHousDesc = findViewById(R.id.descRiption);
        txtWaterTank = findViewById(R.id.waterTankAlew);
        txtGenerator = findViewById(R.id.generator);
        txtParking = findViewById(R.id.parking);
        txtFull = findViewById(R.id.fullBath);
        txtHalf = findViewById(R.id.halfBath);
        txtBuildyear = findViewById(R.id.yearBuilt);
        txtServant = findViewById(R.id.servantQuart);
        txtServantBath = findViewById(R.id.servQuaBath);
        txtKitchen = findViewById(R.id.modernKitch);
        txtConstruction = findViewById(R.id.constType);
        JSON_STRING = getIntent().getStringExtra("JsonObject");
        houselists = new ArrayList<>();
        imageView = findViewById(R.id.imageView);
        txtimageInfo = findViewById(R.id.imageInfo);
        checkLocationPermission();
        phoneNo = "6093";
        msBody = "HR";
        /**
         * house information will be extracted from json array
         * which come from server
         */
        JSONObject house = null;
        try {
            house = new JSONObject(JSON_STRING);

            houselists.add(houses = new House(
                    house.getInt(Config.TAG_ID),
                    house.getInt(Config.TAG_USER_ID),
                    house.getString(Config.TAG_TYPE),
                    house.getString(Config.TAG_CONSTYPE),
                    house.getString(Config.TAG_BUILDING_SIZE),
                    house.getString(Config.TAG_COMPOUND_SIZE),
                    house.getString(Config.TAG_KITCHEN),
                    house.getString(Config.TAG_PARKING),
                    house.getString(Config.TAG_GENERATOR),
                    house.getString(Config.TAG_QUARTER),
                    house.getString(Config.TAG_QUARTER_BATH),
                    house.getString(Config.TAG_FULL_BATH),
                    house.getString(Config.TAG_HALF_BATH),
                    house.getString(Config.TAG_WATER_TANK),
                    house.getString(Config.TAG_SERVTYPE),
                    house.getString(Config.TAG_NOOFROOM),
                    house.getString(Config.TAG_PRICE),
                    house.getString(Config.TAG_BUILT_YEAR),
                    house.getString(Config.TAG_PHOTO),
                    house.getString(Config.TAG_USER_NAME),
                    house.getString(Config.TAG_PHONE),
                    house.getString(Config.TAG_DESCRIPTION)
            ));
            DisplayMetrics dis = new DisplayMetrics();
            this.getWindowManager().getDefaultDisplay().getMetrics(dis);
            int height = dis.heightPixels;
            int width = dis.widthPixels;
            imageView.setMaxHeight(height);
            imageView.setMaxWidth(width);
            /**
             * load image to image viewer using picasso library
             *
             */
            try {
                Picasso.get().load(houses.getImage()).centerInside().placeholder(R.color.colorPrimaryDark).fit().error(R.color.colorPrimaryDark).fit().into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.GONE);
                        txtimageInfo.setText(getString(R.string.click_here_txt));
                    }

                    @Override
                    public void onError(Exception e) {
                        progressBar.setVisibility(View.GONE);

                    }
                });
            } catch (Exception ex) {

            }
            // final String houseImageString = house.getImage();
            // byte[] imageAsBytes = Base64.decode(houseImageString.getBytes(), Base64.DEFAULT);
            // final String imageTy=house.getType();
            // holder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
            //mIds = String.valueOf(mId);
            int firstColor = Color.BLACK;
            int secondColor = Color.BLUE;
            handleLanguage(houses);
            String firstWord = getString(R.string.owned_by) + ":";
            String lastWord = houses.getmUname();
            Spannable spannable = new SpannableString(firstWord + lastWord);
            spannable.setSpan(new ForegroundColorSpan(firstColor), 0, firstWord.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new ForegroundColorSpan(secondColor), firstWord.length(),
                    firstWord.length() + lastWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textViewUname.setText(spannable);
            //textViewUname.setText(getString(R.string.owned_by) + ": " + houses.getmUname());
            String firstWord1 = getString(R.string.contact) + ":";
            String lastWord1 = houses.getmPhone();
            Spannable spannable1 = new SpannableString(firstWord1 + lastWord1);
            spannable1.setSpan(new ForegroundColorSpan(firstColor), 0, firstWord1.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable1.setSpan(new ForegroundColorSpan(secondColor), firstWord1.length(),
                    firstWord1.length() + lastWord1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textViewUphone.setText(spannable1);
            // textViewUphone.setText(getString(R.string.contact) + ": " + houses.getmPhone());
            String firstWord2 = getString(R.string.property_type_txt) + ":";
            String lastWord2 = homeType;
            Spannable spannable2 = new SpannableString(firstWord2 + lastWord2);
            spannable2.setSpan(new ForegroundColorSpan(firstColor), 0, firstWord2.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable2.setSpan(new ForegroundColorSpan(secondColor), firstWord2.length(),
                    firstWord2.length() + lastWord2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textViewTitle.setText(spannable2);
            // textViewTitle.setText(getString(R.string.property_type_txt) + ": " + homeType);
            String firstWord3 = getString(R.string.half_bathroom_txt) + ":";
            String lastWord3 = halfbRoom;
            Spannable spannable3 = new SpannableString(firstWord3 + lastWord3);
            spannable3.setSpan(new ForegroundColorSpan(firstColor), 0, firstWord3.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable3.setSpan(new ForegroundColorSpan(secondColor), firstWord3.length(),
                    firstWord3.length() + lastWord3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            txtHalf.setText(spannable3);
            //txtHalf.setText(getString(R.string.half_bathroom_txt) + ": " + halfbRoom);

            String firstWord4 = getString(R.string.full_bathroom_txt) + ":";
            String lastWord4 = fullbRoom;

                Spannable spannable4 = new SpannableString(firstWord4 + lastWord4);
                spannable4.setSpan(new ForegroundColorSpan(firstColor), 0, firstWord4.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannable4.setSpan(new ForegroundColorSpan(secondColor), firstWord4.length(),
                        firstWord4.length() + lastWord4.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                txtFull.setText(spannable4);

            // txtFull.setText(getString(R.string.full_bathroom_txt) + ": " + fullbRoom);
            String firstWord5 = getString(R.string.water_tank_txt) + ":";
            String lastWord5 = haveWatertank;
            Spannable spannable5 = new SpannableString(firstWord5 + lastWord5);
            spannable5.setSpan(new ForegroundColorSpan(firstColor), 0, firstWord5.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable5.setSpan(new ForegroundColorSpan(secondColor), firstWord5.length(),
                    firstWord5.length() + lastWord5.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            txtWaterTank.setText(spannable5);
            //txtWaterTank.setText(getString(R.string.water_tank_txt) + ": " + haveWatertank);
            String firstWord6 = getString(R.string.parking_sp_txt) + ":";
            String lastWord6 = haveParking;
            Spannable spannable6 = new SpannableString(firstWord6 + lastWord6);
            spannable6.setSpan(new ForegroundColorSpan(firstColor), 0, firstWord6.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable6.setSpan(new ForegroundColorSpan(secondColor), firstWord6.length(),
                    firstWord6.length() + lastWord6.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            txtParking.setText(spannable6);
            //txtParking.setText(getString(R.string.parking_sp_txt) + ": " + haveParking);
            String firstWord7 = getString(R.string.built_year_txt) + ":";
            String lastWord7 = houses.getBuiltYear();
            Spannable spannable7 = new SpannableString(firstWord7 + lastWord7);
            spannable7.setSpan(new ForegroundColorSpan(firstColor), 0, firstWord7.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable7.setSpan(new ForegroundColorSpan(secondColor), firstWord7.length(),
                    firstWord7.length() + lastWord7.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            txtBuildyear.setText(spannable7);
            //txtBuildyear.setText(getString(R.string.built_year_txt) + ": " + houses.getBuiltYear());
            String firstWord8 = getString(R.string.modern_kitchen_txt) + ":";
            String lastWord8 = haveKitchen;
            Spannable spannable8 = new SpannableString(firstWord8 + lastWord8);
            spannable8.setSpan(new ForegroundColorSpan(firstColor), 0, firstWord8.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable8.setSpan(new ForegroundColorSpan(secondColor), firstWord8.length(),
                    firstWord8.length() + lastWord8.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            txtKitchen.setText(spannable8);
            // txtKitchen.setText(getString(R.string.modern_kitchen_txt) + ": " + haveKitchen);
            String firstWord9 = getString(R.string.server_quarter_txt) + ":";
            String lastWord9 = serviceRoom;
            Spannable spannable9 = new SpannableString(firstWord9 + lastWord9);
            spannable9.setSpan(new ForegroundColorSpan(firstColor), 0, firstWord9.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable9.setSpan(new ForegroundColorSpan(secondColor), firstWord9.length(),
                    firstWord9.length() + lastWord9.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            txtServant.setText(spannable9);
            //txtServant.setText(getString(R.string.server_quarter_txt) + ": " + serviceRoom);
            String firstWord10 = getString(R.string.serv_quar_bath_txt) + ":";
            String lastWord10 = serviceBathroom;
            Spannable spannable10 = new SpannableString(firstWord10 + lastWord10);
            spannable10.setSpan(new ForegroundColorSpan(firstColor), 0, firstWord10.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable10.setSpan(new ForegroundColorSpan(secondColor), firstWord10.length(),
                    firstWord10.length() + lastWord10.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            txtServantBath.setText(spannable10);
            //txtServantBath.setText(getString(R.string.serv_quar_bath_txt) + ": " + serviceBathroom);
            String firstWord11 = getString(R.string.cons_type_txt) + ":";
            String lastWord11 = constFrom;
            Spannable spannable11 = new SpannableString(firstWord11 + lastWord11);
            spannable11.setSpan(new ForegroundColorSpan(firstColor), 0, firstWord11.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable11.setSpan(new ForegroundColorSpan(secondColor), firstWord11.length(),
                    firstWord11.length() + lastWord11.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            txtConstruction.setText(spannable11);
            //txtConstruction.setText(getString(R.string.cons_type_txt) + ": " + constFrom);
            String firstWord12 = getString(R.string.generater_txt) + ":";
            String lastWord12 = haveGenerator;
            Spannable spannable12 = new SpannableString(firstWord12 + lastWord12);
            spannable12.setSpan(new ForegroundColorSpan(firstColor), 0, firstWord12.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable12.setSpan(new ForegroundColorSpan(secondColor), firstWord12.length(),
                    firstWord12.length() + lastWord12.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            txtGenerator.setText(spannable12);
            // txtGenerator.setText(getString(R.string.generater_txt) + ": " + haveGenerator);
            String firstWord13 = getString(R.string.builting_square_meter) + ":";
            String lastWord13 = houses.getPsize();
            Spannable spannable13 = new SpannableString(firstWord13 + lastWord13);
            spannable13.setSpan(new ForegroundColorSpan(firstColor), 0, firstWord13.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable13.setSpan(new ForegroundColorSpan(secondColor), firstWord13.length(),
                    firstWord13.length() + lastWord13.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            txtHouseLocx.setText(spannable13);
            //txtHouseLocx.setText(getString(R.string.builting_square_meter) + ": " + houses.getPsize());
            String firstWord14 = getString(R.string.compound_square_meter) + ":";
            String lastWord14 = houses.getPcsize();
            Spannable spannable14 = new SpannableString(firstWord14 + lastWord14);
            spannable14.setSpan(new ForegroundColorSpan(firstColor), 0, firstWord14.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable14.setSpan(new ForegroundColorSpan(secondColor), firstWord14.length(),
                    firstWord14.length() + lastWord14.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            txtCompoundSizze.setText(spannable14);
            //txtCompoundSizze.setText(getString(R.string.compound_square_meter) + ": " + houses.getPcsize());
            String firstWord22 = getString(R.string.serv_type_txt) + ":";
            String lastWord22 = homeFor;
            Spannable spannable22 = new SpannableString(firstWord22 + lastWord22);
            spannable22.setSpan(new ForegroundColorSpan(firstColor), 0, firstWord22.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable22.setSpan(new ForegroundColorSpan(secondColor), firstWord22.length(),
                    firstWord22.length() + lastWord22.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            txtServiceType.setText(spannable22);
            //txtServiceType.setText(getString(R.string.serv_type_txt) + ": " + homeFor);

                String firstWord33 = getString(R.string.bed_room_txt) + ":";
                String lastWord33 = bedRoom;
                Spannable spannable33 = new SpannableString(firstWord33 + lastWord33);
                spannable33.setSpan(new ForegroundColorSpan(firstColor), 0, firstWord33.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannable33.setSpan(new ForegroundColorSpan(secondColor), firstWord33.length(),
                        firstWord33.length() + lastWord33.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                textViewRating.setText(spannable33);

            //textViewRating.setText(getString(R.string.bed_room_txt) + ": " + bedRoom);
            String firstWord44 = getString(R.string.price) + ":";
            String lastWord44 = String.valueOf(houses.getPrice()) + " " + getString(R.string.currency);
            Spannable spannable44 = new SpannableString(firstWord44 + lastWord44);
            spannable44.setSpan(new ForegroundColorSpan(firstColor), 0, firstWord44.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable44.setSpan(new ForegroundColorSpan(secondColor), firstWord44.length(),
                    firstWord44.length() + lastWord44.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textViewPrice.setText(spannable44);
            //textViewPrice.setText(getString(R.string.price) + ": " + String.valueOf(houses.getPrice()) + "  " + getString(R.string.currency));
            String firstWord444 = getString(R.string.house_info_txt) + "\n";
            String lastWord444 = "    " + houses.getHouseDesc();
            Spannable spannable444 = new SpannableString(firstWord444 + lastWord444);
            spannable444.setSpan(new ForegroundColorSpan(firstColor), 0, firstWord444.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable444.setSpan(new ForegroundColorSpan(secondColor), firstWord444.length(),
                    firstWord444.length() + lastWord444.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable444.setSpan(new RelativeSizeSpan(1.1f), 0, firstWord444.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // set size
            spannable444.setSpan(new RelativeSizeSpan(0.8f), firstWord444.length(), firstWord444.length() +
                    lastWord444.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // set size

            txtHousDesc.setText(spannable444);
            //textViewPrice.setText(getString(R.string.price) + ": " + String.valueOf(houses.getPrice()) + "  " + getString(R.string.currency));

            // txtHousDesc.setText(getString(R.string.house_info_txt) + "\n " + houses.getHouseDesc());
            mViewOwner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   /* for (int i = 1; i <= 2; i++) {
                        smsSendMessage(phoneNo, msBody);
                    }*/
                    textViewUname.setVisibility(View.VISIBLE);
                    textViewUphone.setVisibility(View.VISIBLE);
                }
            });
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int mId = houses.getId();
                    Intent intent = new Intent(HouseDetails.this, ImageShower.class);
                    intent.putExtra("id", mId);
                    //  intent.putExtra("image",houseImageString);
                    //  intent.putExtra("imageType",imageTy);
                    intent.putExtra("type", "house");

                    startActivity(intent);
                }
            });

        } catch (JSONException e) {
            Toast.makeText(this, "Sorry Some error Encountered" + e, Toast.LENGTH_SHORT).show();
        }
    }

    private void handleLanguage(House houses) {
        //property type
        if (houses.getPtype().equals("Single Family")) {
            homeType = getString(R.string.single_family_txt);
        } else if (houses.getPtype().equals("Twine Homes")) {
            homeType = getString(R.string.twines_home);
        } else if (houses.getPtype().equals("Town Homes")) {
            homeType = getString(R.string.town_homes_txt);
        } else if (houses.getPtype().equals("Apartment")) {
            homeType = getString(R.string.apartment);
        } else if (houses.getPtype().equals("Condominiums")) {
            homeType = getString(R.string.condo_txt);
        } else if (houses.getPtype().equals("Commercial Space")) {
            homeType = getString(R.string.commercial_space_txt);
        } else if (houses.getPtype().equals("Office Space")) {
            homeType = getString(R.string.office_space_txt);
        } else if (houses.getPtype().equals("Warehouse")) {
            homeType = getString(R.string.ware_house_text);
        } else if (houses.getPtype().equals("Land/lot")) {
            homeType = getString(R.string.lang_lot_txt);
        }
        //Bed room
        String room = houses.getNoofroom();
        if (room.contains("+")){
            room = room.replace("+"," ");
        }
        if (room.equals("Studio")) {
            bedRoom = getString(R.string.studio_txt);
        } else if (room.equals("1 Bedroom")) {
            bedRoom = getString(R.string.one_bedroom_txt);
        } else if (room.equals("2 Bedroom")) {
            bedRoom = getString(R.string.two_bedroom_txt);
        } else if (room.equals("3 Bedrooms")) {
            bedRoom = getString(R.string.three_bedroom_txt);
        } else if (room.equals("4 Bedrooms")) {
            bedRoom = getString(R.string.four_bedroom_txt);
        } else if (room.equals("5 Bedrooms")) {
            bedRoom = getString(R.string.five_bedroom_txt);
        } else if (room.equals("6 Bedrooms")) {
            bedRoom = getString(R.string.six_bedroom_txt);
        } else if (room.equals("7 Bedrooms")) {
            bedRoom = getString(R.string.seven_bedroom_txt);
        } else if (room.equals("8 Bedrooms")) {
            bedRoom = getString(R.string.eight_bedroom_txt);
        } else if (room.equals("9 Bedrooms")) {
            bedRoom = getString(R.string.nine_bedroom_txt);
        } else if (room.equals("10 and Above")) {
            bedRoom = getString(R.string.ten_and_above_bedroom_txt);
        }
        //Construction Type
        if (houses.getCtype().equals("Wood and mud")) {
            constFrom = getString(R.string.wood_mud);
        } else if (houses.getCtype().equals("Wood and mud + Stucco")) {
            constFrom = getString(R.string.wood_mud_stucco);
        } else if (houses.getCtype().equals("Maison/stone")) {
            constFrom = getString(R.string.myson_stone);
        } else if (houses.getCtype().equals("Cement Blocks + Stucco")) {
            constFrom = getString(R.string.cement_stucco);
        } else if (houses.getCtype().equals("Poured Concrete")) {
            constFrom = getString(R.string.poured_concrete);
        }
        //House Services for Rent or For sale
        if (houses.getServceType().equals("rent")) {
            homeFor = getString(R.string.for_rent);
        } else if (houses.getServceType().equals("sale")) {
            homeFor = getString(R.string.for_sale);
        }
        //Bath Room Information Half
        if (houses.getHalfbath().equals("0 Half Bathroom")) {
            halfbRoom = getString(R.string.zero_half_bathroom_txt);
        } else if (houses.getHalfbath().equals("1 Half Bathroom")) {
            halfbRoom = getString(R.string.one_half_bathroom_txt);
        } else if (houses.getHalfbath().equals("2 Half Bathrooms")) {
            halfbRoom = getString(R.string.two_half_bathroom_txt);
        }
        //Bath Room Full
        if (houses.getFullbath().equals("1 Full Bathroom")) {
            fullbRoom = getString(R.string.one_bathroom_txt);
        } else if (houses.getFullbath().equals("2 Full Bathrooms")) {
            fullbRoom = getString(R.string.two_bathroom_txt);
        } else if (houses.getFullbath().equals("3 Full Bathrooms")) {
            fullbRoom = getString(R.string.three_full_bathroom_txt);
        } else if (houses.getFullbath().equals("4 Full Bathrooms")) {
            fullbRoom = getString(R.string.four_full_bathroom_txt);
        } else if (houses.getFullbath().equals("5 Full Bathrooms")) {
            fullbRoom = getString(R.string.five_full_bathroom_txt);
        } else if (houses.getFullbath().equals("6+ Full Bathrooms")) {
            fullbRoom = getString(R.string.six_full_bathroom_txt);
        } else {
            fullbRoom = "Not Applicable";
        }
        //Servant Quarter
        if (houses.getServantQuarter().equals("1 Room")) {
            serviceRoom = getString(R.string.one_room_txt);
        } else if (houses.getServantQuarter().equals("2 Rooms")) {
            serviceRoom = getString(R.string.two_room_txt);
        } else if (houses.getServantQuarter().equals("3 Rooms")) {
            serviceRoom = getString(R.string.three_room_txt);
        } else if (houses.getServantQuarter().equals("4 Rooms")) {
            serviceRoom = getString(R.string.four_room_txt);
        }
        //servant Quarter  BathRoom
        if (houses.getServantQuarterBath().equals("1 Bathroom")) {
            serviceBathroom = getString(R.string.one_bathroom_txt);
        } else if (houses.getServantQuarterBath().equals("2 Bathrooms")) {
            serviceBathroom = getString(R.string.two_bathroom_txt);
        }
        //Modern Kitchen Available
        if (houses.getKitchen().equals("Yes")) {
            haveKitchen = getString(R.string.yes_txt);
        } else if (houses.getKitchen().equals("No")) {
            haveKitchen = getString(R.string.no_txt);
        }
        //parking space
        if (houses.getParking().equals("1 Parking Space")) {
            haveParking = getString(R.string.parking_one_txt);
        } else if (houses.getParking().equals("2 Parking Spaces")) {
            haveParking = getString(R.string.parking_two_txt);
        } else if (houses.getParking().equals("3 Parking Spaces")) {
            haveParking = getString(R.string.parking_three_txt);
        } else if (houses.getParking().equals("4 Parking Spaces")) {
            haveParking = getString(R.string.parking_four_txt);
        } else if (houses.getParking().equals("5+ Parking Spaces")) {
            haveParking = getString(R.string.parking_five_txt);
        }
        //Water Tank Available
        if (houses.getWatertank().equals("Yes")) {
            haveWatertank = getString(R.string.yes_txt);
        } else if (houses.getWatertank().equals("No")) {
            haveWatertank = getString(R.string.no_txt);
        }
        //Generator Available ??
        if (houses.getGenerators().equals("Yes")) {
            haveGenerator = getString(R.string.yes_txt);
        } else if (houses.getGenerators().equals("No")) {
            haveGenerator = getString(R.string.no_txt);
        } else {
            fullbRoom = "No Info";
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /*  public void smsSendMessage(String phoneNo, String sms) {
          try {
              SmsManager smsManager = SmsManager.getDefault();
              smsManager.sendTextMessage(Uri.parse(phoneNo).toString(), null, sms, null, null);

          } catch (Exception e) {
              Toast.makeText(getApplicationContext(),
                      "SMS faild, please try again later!" +e,
                      Toast.LENGTH_LONG).show();
              e.printStackTrace();
          }
      }*/
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                new android.app.AlertDialog.Builder(this)
                        .setTitle("give permission")
                        .setMessage("give permission message")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(HouseDetails.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(HouseDetails.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    final int MY_PERMISSIONS_REQUEST_SEND_SMS = 2;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (permissions[0].equalsIgnoreCase
                        (android.Manifest.permission.SEND_SMS)
                        && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    // smsSendMessage(phoneNo, msBody);
                    // Permission was granted. Enable sms button.
                } else {
                    Toast.makeText(getApplicationContext(), "Please provide Permisiion", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
