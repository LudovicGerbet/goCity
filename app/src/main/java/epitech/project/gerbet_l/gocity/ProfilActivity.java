package epitech.project.gerbet_l.gocity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class ProfilActivity extends AppCompatActivity {

    private static final String[] INITIAL_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS
    };
    private static final String[] LOCATION_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final int INITIAL_REQUEST=1337;
    private static final int LOCATION_REQUEST=INITIAL_REQUEST+3;

    private User user;
    private EditText firstName;
    private EditText lastName;
    private BottomNavigationView bottomNavigationView;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_profil);

        firstName = findViewById(R.id.editFirstName);
        lastName = findViewById(R.id.editLastName);
        bottomNavigationView = findViewById(R.id.bottom_navigation_profil);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        final ImageButton button = findViewById(R.id.validProfil);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                valid();
            }
        });

        this.user = (User) getIntent().getSerializableExtra("user");
        if (getIntent().getSerializableExtra("firstRun") != null) {
            System.out.println("FIRST RUN");
            bottomNavigationView.setVisibility(View.INVISIBLE);
        }

        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());
        requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_city:
                    System.out.println("NEW CITY");
                    Intent cityIntent = new Intent(ProfilActivity.this, NewCityActivity.class);
                    cityIntent.putExtra("user", user);
                    startActivity(cityIntent);
                    return true;
                case R.id.navigation_localisation:
                    System.out.println("LOCALISATION");
                    Intent mapsIntent = new Intent(ProfilActivity.this, MapsActivity.class);
                    startActivity(mapsIntent);
                    return true;
                case R.id.navigation_profil:
                    System.out.println("PROFIL");
                    return true;
            }
            return false;
        }

    };

    protected void valid() {
        if (firstName.getText().toString() == "" || lastName.getText().toString() == "")
        {
            Toast.makeText(this, "Veuillez renseigner un Prénom et un Nom", Toast.LENGTH_LONG).show();
            return;
        }

        user.setFirstName(firstName.getText().toString());
        user.setLastName(lastName.getText().toString());
        Intent mapsIntent = new Intent(ProfilActivity.this, MapsActivity.class);
        mapsIntent.putExtra("user", user);
        startActivity(mapsIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case INITIAL_REQUEST: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // La permission est garantie
                } else {
                    requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
                }
                return;
            }
        }
    }

}
