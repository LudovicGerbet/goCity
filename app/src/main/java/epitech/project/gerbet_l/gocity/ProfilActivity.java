package epitech.project.gerbet_l.gocity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class ProfilActivity extends AppCompatActivity {

    private User user;
    private EditText firstName;
    private EditText lastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_profil);

        firstName = findViewById(R.id.editFirstName);
        lastName = findViewById(R.id.editLastName);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        final ImageButton button = findViewById(R.id.validProfil);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                valid();
            }
        });

        User user = (User) getIntent().getSerializableExtra("user");
        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_city:
                    System.out.println("NEW CITY");
                    Intent cityIntent = new Intent(ProfilActivity.this, NewCityActivity.class);
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

        user = new User(firstName.getText().toString(), lastName.getText().toString());
        Intent mapsIntent = new Intent(ProfilActivity.this, MapsActivity.class);
        mapsIntent.putExtra("user", user);
        startActivity(mapsIntent);
    }
}
