package com.darkwinter.bookfilms;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class BookingTicketActivity extends AppCompatActivity {

    private Spinner Cinemas, Seats, Times;
    private Button Booking;
    private DatabaseReference mFilmRef, mCineRef, mRoomRef;
    private Films film;
    private ArrayList<String> ListCinemas = new ArrayList<>();
    private ArrayList<String> ListDates = new ArrayList<>();
    private ArrayList<String> ListDatesShow = new ArrayList<>();
    private ArrayList<String> ListSlots = new ArrayList<>();
    private ArrayList<String> ListSeats = new ArrayList<>();
    private ArrayAdapter<String> CinemaAdater;
    private Calendar c;
    private int pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_ticket);
        film = (Films) getIntent().getSerializableExtra("Film");
        loadCinemasFromFirebase();
        setupWidget();

    }

    private void loadCinemasFromFirebase(){
        c = GregorianCalendar.getInstance();
        mFilmRef = FirebaseDatabase.getInstance().getReference().child("Films").child(film.getId()).child("Dates");
        //Log.d("+++++++++++", mFilmRef.getDatabase().toString());
        //Toast.makeText(BookingTicketActivity.this,film.getId(), Toast.LENGTH_LONG).show();
        mFilmRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    String dates = data.getKey();

                    //mCineRef = data.getRef().child(dates);
                    mCineRef = FirebaseDatabase.getInstance().getReference().child("Films").child(film.getId()).child("Dates").child(dates);
                    mCineRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot data1 : dataSnapshot.getChildren()){
                                String cinima = data1.getKey();
                                Toast.makeText(BookingTicketActivity.this,cinima, Toast.LENGTH_LONG).show();
                                ListCinemas.add(cinima);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    Toast.makeText(BookingTicketActivity.this, mCineRef.child(dates).toString(), Toast.LENGTH_LONG).show();
                    ListDates.add(dates);
                    for (pos = 0; pos < ListDates.size(); pos++){
                        int day = c.get(Calendar.DATE);
                        //Toast.makeText(BookingTicketActivity.this, day + " ", Toast.LENGTH_LONG).show();
                        String date = ListDates.get(pos);
                        //Toast.makeText(BookingTicketActivity.this, date, Toast.LENGTH_LONG).show();
                        String[] part1 = date.split("-");
                        String da = part1[0];
                        int daycheck = Integer.parseInt(da);
                        //Toast.makeText(BookingTicketActivity.this, da, Toast.LENGTH_LONG).show();
                        if (daycheck >= day) {
                            ListDatesShow.add(date);


                        }



                    }
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void setupWidget(){
        Cinemas = findViewById(R.id.spinCinema);
        Seats = findViewById(R.id.spinSeat);
        Times = findViewById(R.id.spinTime);
        Booking = findViewById(R.id.btnBookAction);
        CinemaAdater = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ListCinemas);
        CinemaAdater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Cinemas.setAdapter(CinemaAdater);
        Cinemas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getApplicationContext(),"--------"+adapterView.getItemAtPosition(i).toString(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
}
