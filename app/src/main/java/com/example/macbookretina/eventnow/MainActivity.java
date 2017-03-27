package com.example.macbookretina.eventnow;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
TableLayout table;
Button something;
Button addEvent;
public static int numberOfRows;
public static ArrayList<TableRow> tr_head;
public static ArrayList<TableRow> textArray;
public static HashMap<String, Integer> map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);

        map = new HashMap<>();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference eventRef = database.getReference("event");
        addEvent = (Button) findViewById(R.id.addevent);
        something = (Button) findViewById(R.id.something);
        table = (TableLayout) findViewById(R.id.table);

        addEvent.getBackground().setColorFilter(0xFFFFFFFF, PorterDuff.Mode.MULTIPLY);
        something.getBackground().setColorFilter(0xFFFFFFFF, PorterDuff.Mode.MULTIPLY);
        addEvent.setTextColor(Color.parseColor("#0447a3"));
        something.setTextColor(Color.parseColor("#0447a3"));


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
//        something.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                eventRef.push().setValue(new AddEvent.Event("hey","1"));
//            }
//        });

        eventRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                Event newPost = dataSnapshot.getValue(Event.class);
                System.out.println(newPost);
                TableRow tempRow = new TableRow(MainActivity.this);
                map.put(dataSnapshot.getKey(),numberOfRows + 1);
                tempRow.setId(numberOfRows + 1);
                tempRow.setBackgroundColor(Color.parseColor("#252525"));
                tempRow.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tempRow.setPadding(10,10,10,10);
                tempRow.setBackgroundResource(R.drawable.row_border);

                // Here create the TextView dynamically

                TextView tempText = new TextView(MainActivity.this);
                tempText.setId(numberOfRows + 111);
                tempText.setTextColor(Color.WHITE);
                tempText.setText(newPost.name + " " + newPost.date);
                tempText.setPadding(10, 10, 10, 10);

                Button btn = new Button(MainActivity.this);
                btn.setText("hey");

                //tr_head[i].addView(textArray[i]);

                tempRow.addView(tempText);
                tempRow.addView(btn);

                tr_head.add(tempRow);

                table.addView(tempRow, new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT));
                numberOfRows++;
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String tempKey = dataSnapshot.getKey();
                table.removeView(findViewById(map.get(tempKey)));
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
    }
    public static class Event {
        public String name;
        public String date;
        public String description;
        public String capacity;
        public String location;
        public String price;

        public Event() {
            this.name = null;
            this.date = null;
            this.description = null;
            this.capacity = null;
            this.location = null;
            this.price = null;
        }

        public Event(String name, String date, String description, String capacity, String location, String price) {
            this.name = name;
            this.date = date;
            this.description = description;
            this.capacity = capacity;
            this.location = location;
            this.price = price;
        }

        public String toString() {
            return "Name: " + name + " date: " + date;
        }
    }
}
