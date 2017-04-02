package com.example.macbookretina.eventnow;

import android.content.Intent;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import android.support.v4.app.FragmentActivity;

/**
 * Created by MacbookRetina on 3/27/17.
 */

public class AddEvent extends AppCompatActivity {
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    EditText location;
    Intent googlePlace;
    Button create;
    Button back;
    boolean addressChange;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);
        create = (Button) findViewById(R.id.create);
        back = (Button) findViewById(R.id.back);

        addressChange = false;

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
                eventRef.push().setValue(new Event(name.getText().toString(),date.getText().toString(),description.getText().toString(),capacity.getText().toString(),location.getText().toString(),price.getText().toString()));
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
                location.setText(place.getAddress());
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
        public String capacity;
        public String location;
        public String price;
        public int voteCount;
        public int going;

        public Event() {
            this.name = null;
            this.date = null;
            this.description = null;
            this.capacity = null;
            this.location = null;
            this.price = null;
            this.voteCount = 0;
            this.going = 0;
        }

        public Event(String name, String date, String description, String capacity, String location, String price) {
            this.name = name;
            this.date = date;
            this.description = description;
            this.capacity = capacity;
            this.location = location;
            this.price = price;
            this.voteCount = 0;
            this.going = 0;
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
            return result;
        }

        public String toString() {
            return "Name: " + name + " date: " + date;
        }


    }
}
