package com.example.macbookretina.eventnow;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
TableLayout table;
Button something;
Button addEvent;
    GPSTracker gps;
public static int numberOfRows;
public static ArrayList<TableRow> tr_head;
public static ArrayList<TableRow> textArray;
public static HashMap<String, Integer> map;
public static HashMap<Integer, String> buttonMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);

        map = new HashMap<>();
        buttonMap = new HashMap<>();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference eventRef = database.getReference("event");
        addEvent = (Button) findViewById(R.id.addevent);
        something = (Button) findViewById(R.id.something);
        table = (TableLayout) findViewById(R.id.table);

        addEvent.getBackground().setColorFilter(0xFFFFFFFF, PorterDuff.Mode.MULTIPLY);
        something.getBackground().setColorFilter(0xFFFFFFFF, PorterDuff.Mode.MULTIPLY);
        addEvent.setTextColor(Color.parseColor("#0447a3"));
        something.setTextColor(Color.parseColor("#0447a3"));

        gps = new GPSTracker(this);
        // check if GPS enabled
        if(gps.canGetLocation()){

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }


        tr_head = new ArrayList<>();
//        textArray = new ArrayList<>();
//
//        TableRow tempRow = new TableRow(this);
//        tempRow.setId(numberOfRows + 1);
//        tempRow.setBackgroundColor(Color.parseColor("#252525"));
//        tempRow.setLayoutParams(new TableRow.LayoutParams(
//                TableRow.LayoutParams.MATCH_PARENT,
//                TableRow.LayoutParams.WRAP_CONTENT));
//
//
//        TextView tempText = new TextView(this);
//        tempText.setId(numberOfRows + 111);
//        tempText.setTextColor(Color.WHITE);
//        tempText.setText("hey");
//        tempText.setPadding(5, 5, 5, 5);
//
//
//        tempRow.addView(tempText);
//        tr_head.add(tempRow);


//        table.addView(tempRow, new TableLayout.LayoutParams(
//                TableLayout.LayoutParams.MATCH_PARENT,
//                TableLayout.LayoutParams.WRAP_CONTENT));
        something.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                TableRow newRow = new TableRow(getApplicationContext());
                newRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                CheckBox newCheckBox = new CheckBox(getApplicationContext());

                TextView feeTypeText = new TextView(getApplicationContext());
                feeTypeText.setText("fee");
                feeTypeText.setTextColor(Color.WHITE);
                feeTypeText.setTextSize(16f);

                TextView dueAmountText = new TextView(getApplicationContext());
                dueAmountText.setText("dueAmount");
                dueAmountText.setTextColor(Color.WHITE);
                dueAmountText.setTextSize(16f);

                TextView dueDateText = new TextView(getApplicationContext());
                dueDateText.setText("dueDate");
                dueDateText.setTextColor(Color.WHITE);
                dueDateText.setTextSize(16f);

                newRow.addView(newCheckBox,(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,0.8f)));
                newRow.addView(feeTypeText,(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,0.8f)));
                newRow.addView(dueAmountText,(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,1.0f)));
                newRow.addView(dueDateText,(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,1.0f)));
                table.addView(newRow);
            }
        });

        eventRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot dataSnapshot, String prevChildKey) {
                //Event data from database
                final Event newPost = dataSnapshot.getValue(Event.class);
                System.out.println(newPost);

                //Row parent class
                TableRow tempRow = new TableRow(MainActivity.this);
                map.put(dataSnapshot.getKey(),numberOfRows + 1);
                tempRow.setId(numberOfRows + 1);
                tempRow.setBackgroundColor(Color.parseColor("#252525"));
                tempRow.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));

                //Left top right down
                tempRow.setPadding(3,3,3,3);
                tempRow.setBackgroundResource(R.drawable.row_border);

                TableLayout.LayoutParams lp =
                        new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                                TableLayout.LayoutParams.WRAP_CONTENT);

                lp.setMargins(10,10,10,10);
                tempRow.setLayoutParams(lp);


                // Here create the TextView dynamically

                TableRow.LayoutParams tableRowLP = new TableRow.LayoutParams();
                tableRowLP.height = TableRow.LayoutParams.MATCH_PARENT;

                TextView capacity = new TextView(getApplicationContext());
                capacity.setBackgroundColor(Color.parseColor("#0447a3"));

                Spanned text = Html.fromHtml("<font color=\"#a3df48\"> Likes " + newPost.voteCount + "</font> <br> <br> <font color=\"#ffffff\"> Going: " + newPost.going + "</font> <br>" + "<font color=\"#ffffff\">");
                capacity.setId(numberOfRows + 101);
                capacity.setText(text);
                capacity.setTextColor(Color.WHITE);
                capacity.setTextSize(10f);
                capacity.setMinHeight(300);
                capacity.setPadding(10,10,10,10);
                capacity.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

                capacity.setLayoutParams(tableRowLP);


                TextView tempText = new TextView(getApplicationContext());

                Spanned text2 = Html.fromHtml("<big><font color=\"#7da6d8\"><b>" + newPost.name + "</b></font></big> <br>" + newPost.date + "<br>" + "<font color=\"#ffffff\"> <big>" + newPost.description + "</big></font>");
                tempText.setText(text2);
                tempText.setMinHeight(100);
                tempText.setPadding(20,10,10,10);


                TableLayout.LayoutParams tableRowLP1 = new TableLayout.LayoutParams();
                tableRowLP.width = TableRow.LayoutParams.MATCH_PARENT;


                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT,1f);

                //TableLayout within tablerow
                TableLayout tempLay = new TableLayout(getApplicationContext());
                tempLay.setLayoutParams(params);

                //First Row
                TableRow tRow1 = new TableRow(getApplicationContext());
                tRow1.addView(tempText);
                tRow1.setLayoutParams(tableRowLP1);

                final Button newCheckBox = new Button(getApplicationContext());
                buttonMap.put(numberOfRows + 1, dataSnapshot.getKey());
                newCheckBox.setId(numberOfRows + 1);
                newCheckBox.setText("UPVOTE");
                newCheckBox.setTextSize(8);

                newCheckBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        newPost.upVote();
                        newPost.updateDatabase(buttonMap.get(newCheckBox.getId()));
                    }
                });

                final Button newCheckBox2 = new Button(getApplicationContext());
                newCheckBox2.setText("DOWNVOTE");
                newCheckBox2.setTextSize(6);

                newCheckBox2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        newPost.downVote();
                        if (newPost.checkLimit()) {dataSnapshot.getRef().removeValue();}
                        else {newPost.updateDatabase(buttonMap.get(newCheckBox.getId()));}
                    }
                });

                final Button newCheckBox3 = new Button(getApplicationContext());
                newCheckBox3.setText("Going");
                newCheckBox3.setTextSize(8);

                newCheckBox3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        newPost.upGo();
                        newPost.updateDatabase(buttonMap.get(newCheckBox.getId()));
                    }
                });

                final Button newCheckBox4 = new Button(getApplicationContext());
                newCheckBox4.setText("Share");
                newCheckBox4.setTextSize(8);

                newCheckBox4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println(newCheckBox4.getId());
                    }
                });

                //Second Row
                TableRow tRow2 = new TableRow(getApplicationContext());
                tRow2.addView(newCheckBox,(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,0.2f)));
                tRow2.addView(newCheckBox2,(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,0.2f)));
                tRow2.addView(newCheckBox3,(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,0.2f)));
                tRow2.addView(newCheckBox4,(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,0.2f)));
                tRow2.setBackgroundColor(Color.parseColor("#B7B7B7"));

                tempLay.addView(tRow1);
                tempLay.addView(tRow2);

                //Add all sub-components to tablerow
                tempRow.addView(capacity);
                tempRow.addView(tempLay);

                //Array of tablerows...probably not necessary
                tr_head.add(tempRow);

                //Update table layout
                table.addView(tempRow, lp);
                numberOfRows++;
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                final Event newPost = dataSnapshot.getValue(Event.class);
                TextView temp = (TextView) findViewById(map.get(dataSnapshot.getKey()) + 100);
                Spanned text = Html.fromHtml("<font color=\"#a3df48\"> Likes " + newPost.voteCount + "</font> <br> <br> <font color=\"#ffffff\"> Going: " + newPost.going + "</font> <br>" + "<font color=\"#ffffff\">");
                temp.setText(text);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String tempKey = dataSnapshot.getKey();
                table.removeView(findViewById(map.get(tempKey)));
                map.remove(tempKey);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        addEvent.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v)
            {
                Intent myIntent = new Intent(MainActivity.this,
                        AddEvent.class);
                startActivity(myIntent);
            }
        });


//        LocationManager locationManager = (LocationManager)
//                getSystemService(Context.LOCATION_SERVICE);
//        LocationListener locationListener = new MyLocationListener();
//
//        if ( Build.VERSION.SDK_INT >= 23 &&
//                ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
//                ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return  ;
//        }
//        locationManager.requestLocationUpdates(
//                LocationManager.GPS_PROVIDER, 5000, 10, locationListener);



    }
    public static class Event {
        public String name;
        public String date;
        public String description;
        public double capacity;
        public String location;
        public String price;
        public double lattitude;
        public double longitude;
        public int voteCount;
        public int going;
        private int limit;
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        public Event() {
            this.name = null;
            this.date = null;
            this.description = null;
            this.capacity = 0;
            this.location = null;
            this.price = null;
            this.voteCount = 0;
            this.going = 0;
        }

        public Event(String name, String date, String description, double capacity, String location, String price, int voteCount, int going) {
            this.name = name;
            this.date = date;
            this.description = description;
            this.capacity = capacity;
            this.location = location;
            this.price = price;
            this.voteCount = voteCount;
            this.going = going;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setCapacity(double capacity) {
            this.capacity = capacity;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public void setLattitude(double lattitude) {
            this.lattitude = lattitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public void upVote() {
            this.voteCount++;
        }

        public void downVote() {
            this.voteCount--;
        }

        public void upGo() {
            this.going++;
        }

        public boolean checkLimit() {
            return voteCount <= -5;
        }

        @Exclude
        public Map<String, Object> toMap() {
            HashMap<String, Object> result = new HashMap<>();
            result.put("name", this.name);
            result.put("date", this.date);
            result.put("description", this.description);
            result.put("capacity", this.capacity);
            result.put("location", this.location);
            result.put("price", this.price);
            result.put("voteCount", this.voteCount);
            result.put("going", this.going);
            result.put("lattitude",this.lattitude);
            result.put("longitude",this.longitude);
            return result;
        }

        private void updateDatabase(String key) {
            // Create new post at /user-posts/$userid/$postid and at
            // /posts/$postid simultaneously
            DatabaseReference event  = database.getReference("event");
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put(key, toMap());

            event.updateChildren(childUpdates);
        }

        public String toString() {
            return "Name: " + name + " date: " + date;
        }
    }

    /*---------- Listener class to get coordinates ------------- */
    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {
            Toast.makeText(
                    getBaseContext(),
                    "Location changed: Lat: " + loc.getLatitude() + " Lng: "
                            + loc.getLongitude(), Toast.LENGTH_SHORT).show();
            String longitude = "Longitude: " + loc.getLongitude();
            String latitude = "Latitude: " + loc.getLatitude();

        /*------- To get city name from coordinates -------- */
            String cityName = null;
            Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(loc.getLatitude(),
                        loc.getLongitude(), 1);
                if (addresses.size() > 0) {
                    System.out.println(addresses.get(0).getLocality());
                    cityName = addresses.get(0).getLocality();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            String s = longitude + "\n" + latitude + "\n\nMy Current City is: "
                    + cityName;
        }

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }
}
