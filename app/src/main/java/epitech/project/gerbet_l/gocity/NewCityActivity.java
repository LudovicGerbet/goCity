package epitech.project.gerbet_l.gocity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

public class NewCityActivity extends AppCompatActivity {

    private String TAG = "_GOOGLE PLACE API";
    private City newCity;
    private EditText cityTitle;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_new_city);

        newCity = new City();
        this.user = (User) getIntent().getSerializableExtra("user");
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName());
                Log.i(TAG, place.getAddress().toString());

                newCity.setAddress(place.getAddress().toString());
                newCity.setLatitude(place.getLatLng().latitude);
                newCity.setLongitude(place.getLatLng().longitude);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        cityTitle = findViewById(R.id.cityTitle);
        final ImageButton button = findViewById(R.id.validNewCity);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                valid();
            }
        });
    }

    protected void valid() {
        if (newCity.getAddress() == null)
        {
            Toast.makeText(this, "Veuillez renseigner l'adresse", Toast.LENGTH_LONG).show();
            return;
        }
        newCity.setTitle(cityTitle.getText().toString());
        newCity.setCreator(user);

        Intent mapsIntent = new Intent(NewCityActivity.this, MapsActivity.class);
        mapsIntent.putExtra("newCity", newCity);
        startActivity(mapsIntent);
    }
}
