package com.example.macbookretina.eventnow;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import android.support.v4.app.FragmentActivity;

import org.json.JSONObject;

/**
 * Created by MacbookRetina on 3/27/17.
 */

public class AddEvent extends AppCompatActivity implements OnMapReadyCallback {
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    EditText location;
    Intent googlePlace;
    Button create;
    Button back;
    boolean addressChange;
    Event event;
    String lattitude;
    String longitude;

    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        create = (Button) findViewById(R.id.create);
        back = (Button) findViewById(R.id.back);
        url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + " " + "&key=AIzaSyDcNt2MAoMM6HQWS3DVI1narZInJu9KvhE";
        addressChange = false;

        lattitude = null;
        longitude = null;
        event = new Event();

        try {
            googlePlace = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(this);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

        final EditText name = (EditText) findViewById(R.id.editText);
        final EditText date = (EditText) findViewById(R.id.editText3);
        final EditText description = (EditText) findViewById(R.id.editText4);
        final EditText capacity = (EditText) findViewById(R.id.editText8);
        location = (EditText) findViewById(R.id.editText11);
        final EditText price = (EditText) findViewById(R.id.editText10);


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference eventRef = database.getReference("event");

        create.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Event event = new Event(name.getText().toString(),date.getText().toString(),description.getText().toString(),Double.parseDouble(capacity.getText().toString()),location.getText().toString(),price.getText().toString());
                event.setName(name.getText().toString());
                event.setDate(date.getText().toString());
                event.setDescription(description.getText().toString());
                event.setCapacity(Double.parseDouble(capacity.getText().toString()));
                event.setLocation(location.getText().toString());
                event.setPrice(price.getText().toString());
                event.setLattitude(Double.parseDouble(lattitude));
                event.setLongitude(Double.parseDouble(longitude));
                System.out.println(event.getLattitude());
                eventRef.push().setValue(event);
            }
        });

        location.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!addressChange || location.getText().length() == 0) {
                    startActivityForResult(googlePlace, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                }
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });




        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                addressChange = true;
                Place place = PlaceAutocomplete.getPlace(this, data);
                Retrieve task = new Retrieve();
                location.setText(place.getAddress());
                task.execute(place.getAddress().toString());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
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

        public Event(String name, String date, String description, double capacity, String location, String price) {
            this.name = name;
            this.date = date;
            this.description = description;
            this.capacity = capacity;
            this.location = location;
            this.price = price;
            this.voteCount = 0;
            this.going = 0;
        }

        public double getLattitude() {
            return lattitude;
        }

        public double getLongitude() {
            return longitude;
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

        public String toString() {
            return "Name: " + name + " date: " + date;
        }




    }
    class Retrieve extends AsyncTask<String, String, String> {


        protected String doInBackground(String... urls) {
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                String temp = urls[0];
                temp = temp.replaceAll("\\s+","+");
                url = new URL("https://maps.googleapis.com/maps/api/geocode/json?address=" + temp + "&key=AIzaSyDcNt2MAoMM6HQWS3DVI1narZInJu9KvhE");

                urlConnection = (HttpURLConnection) url
                        .openConnection();

                InputStream in = urlConnection.getInputStream();

                BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                StringBuilder responseStrBuilder = new StringBuilder();

                String inputStr;
                while ((inputStr = streamReader.readLine()) != null)
                    responseStrBuilder.append(inputStr);
                JSONObject a = new JSONObject(responseStrBuilder.toString());

                lattitude = a.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lat").toString();
                longitude = a.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lng").toString();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            return null;
        }

        protected void onPostExecute(String... urls) {
            // TODO: check this.exception
            // TODO: do something with the feed
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        LatLng sydney = new LatLng(-33.852, 151.211);
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }


}
