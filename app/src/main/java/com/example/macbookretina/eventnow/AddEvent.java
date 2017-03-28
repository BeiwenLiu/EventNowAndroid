package com.example.macbookretina.eventnow;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MacbookRetina on 3/27/17.
 */

public class AddEvent extends AppCompatActivity {

    Button create;
    Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);
        create = (Button) findViewById(R.id.create);
        back = (Button) findViewById(R.id.back);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference eventRef = database.getReference("event");

        create.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                EditText name = (EditText) findViewById(R.id.editText);
                EditText date = (EditText) findViewById(R.id.editText3);
                EditText description = (EditText) findViewById(R.id.editText4);
                EditText capacity = (EditText) findViewById(R.id.editText8);
                EditText location = (EditText) findViewById(R.id.editText11);
                EditText price = (EditText) findViewById(R.id.editText10);
                eventRef.push().setValue(new Event(name.getText().toString(),date.getText().toString(),description.getText().toString(),capacity.getText().toString(),location.getText().toString(),price.getText().toString(),0));
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public static class Event {
        public String name;
        public String date;
        public String description;
        public String capacity;
        public String location;
        public String price;
        public int voteCount;

        public Event() {
            this.name = null;
            this.date = null;
            this.description = null;
            this.capacity = null;
            this.location = null;
            this.price = null;
            this.voteCount = 0;
        }

        public Event(String name, String date, String description, String capacity, String location, String price, int voteCount) {
            this.name = name;
            this.date = date;
            this.description = description;
            this.capacity = capacity;
            this.location = location;
            this.price = price;
            this.voteCount = voteCount;
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
