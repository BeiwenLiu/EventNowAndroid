package com.example.macbookretina.eventnow;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by MacbookRetina on 3/27/17.
 */

public class AddEvent extends AppCompatActivity {

    Button click;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);
        click = (Button) findViewById(R.id.click);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference eventRef = database.getReference("event");

        click.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                eventRef.push().setValue(new Event("hey","1"));
            }
        });
    }

    public static class Event {
        public String name;
        public String date;

        public Event() {
            this.name = null;
            this.date = null;
        }

        public Event(String name, String date) {
            this.name = name;
            this.date = date;
        }

        public String toString() {
            return "Name: " + name + " date: " + date;
        }
    }
}
