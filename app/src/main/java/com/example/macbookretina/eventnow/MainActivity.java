package com.example.macbookretina.eventnow;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                //Event data from database
                Event newPost = dataSnapshot.getValue(Event.class);
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
                capacity.setText("# Available: " + "\n" + newPost.capacity);
                capacity.setTextColor(Color.WHITE);
                capacity.setTextSize(10f);
                capacity.setMinHeight(300);
                capacity.setPadding(10,10,10,10);
                capacity.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

                capacity.setLayoutParams(tableRowLP);




                TextView tempText = new TextView(getApplicationContext());

                Spanned text = Html.fromHtml("<big><font color=\"#7da6d8\"><b>" + newPost.name + "</b></font></big> <br>" + newPost.date + "<br>" + "<font color=\"#ffffff\"> <big>" + newPost.description + "</big></font>");
                tempText.setText(text);
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
                tRow1.setBackgroundResource(R.drawable.row_border2);
                tRow1.addView(tempText);
                tRow1.setLayoutParams(tableRowLP1);

                LinearLayout la = new LinearLayout(getApplicationContext());

                Button newCheckBox = new Button(getApplicationContext());
                Button newCheckBox2 = new Button(getApplicationContext());
                Button newCheckBox3 = new Button(getApplicationContext());
                Button newCheckBox4 = new Button(getApplicationContext());


                //Second Row
                TableRow tRow2 = new TableRow(getApplicationContext());
                tRow2.addView(newCheckBox,(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,0.8f)));
                tRow2.addView(newCheckBox2,(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,0.8f)));
                tRow2.addView(newCheckBox3,(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,0.8f)));
                tRow2.addView(newCheckBox4,(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,0.8f)));
                tRow2.setBackgroundResource(R.drawable.row_border2);

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
