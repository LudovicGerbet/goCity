package epitech.project.gerbet_l.gocity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.UUID;


public class NewEventActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private Button btnDatePicker, btnTimeStart, btnTimeEnd, btnBack, btnValid;
    private TextView txtDate, txtTimeStart, txtTimeEnd;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private City city;
    private User user;
    private EditText eventTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_new_event);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("/citys");
        this.city = (City) getIntent().getSerializableExtra("city");
        this.user = (User) getIntent().getSerializableExtra("user");
        btnDatePicker=(Button)findViewById(R.id.btn_date);
        btnTimeStart=(Button)findViewById(R.id.btn_time_start);
        btnTimeEnd=(Button)findViewById(R.id.btn_time_end);
        txtDate=(TextView) findViewById(R.id.in_date);
        txtTimeStart=(TextView) findViewById(R.id.in_time_start);
        txtTimeEnd=(TextView) findViewById(R.id.in_time_end);
        eventTitle = findViewById(R.id.event_title);
        btnBack= findViewById(R.id.btn_back);
        btnValid= findViewById(R.id.btn_valid);
        btnDatePicker.setOnClickListener(this);
        btnTimeStart.setOnClickListener(this);
        btnTimeEnd.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnValid.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final Calendar c = Calendar.getInstance();
        switch (v.getId()) {
            case R.id.btn_date:
                // Get Current Date
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                break;
            case R.id.btn_time_start:
                // Get Current Time
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                txtTimeStart.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
                break;
            case R.id.btn_time_end:
                // Get Current Time
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog2 = new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                txtTimeEnd.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog2.show();
                break;

            case R.id.btn_back:
                System.out.println("Yolo");
                finish();
                break;
            case R.id.btn_valid:
                valid();
                break;
        }
    }

    private void valid() {
        if (eventTitle.getText().toString() == null || txtDate.getText().toString() == "" || txtTimeStart.getText().toString() == "" || txtTimeEnd.getText().toString() == "") {
            Toast.makeText(NewEventActivity.this, "Elements manquants", Toast.LENGTH_SHORT).show();
            return;
        }
        Event newEvent = new Event(
                UUID.randomUUID().toString(),
                user,
                eventTitle.getText().toString(),
                txtDate.getText().toString(),
                txtTimeStart.getText().toString(),
                txtTimeEnd.getText().toString());

        newEvent.addPlayer(user);
        city.addEvent(newEvent);
        myRef.child(city.getId()).setValue(city);

        Intent detailIntent = new Intent(NewEventActivity.this, DetailActivity.class);
        detailIntent.putExtra("user", user);
        detailIntent.putExtra("city", city);
        startActivity(detailIntent);
    }
}
