package epitech.project.gerbet_l.gocity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private List<City> cityList;
    private City lastCity;
    private City createdCity;
    private User user;

    //LOGS TAGS
    public String loadCity = "CHARGEMENT D'UN CITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        database = FirebaseDatabase.getInstance();
        user = new User("Prénom", "Nom");
        //Put citys on map
        cityList = new ArrayList<>();

        //Retrieve created of changed
        createdCity = (City) getIntent().getSerializableExtra("newCity");
        User newUser = (User) getIntent().getSerializableExtra("user");

        if (newUser != null){
            this.user = newUser;
            SharedPreferences settings = getSharedPreferences("user", MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("firstName", this.user.getFirstName());
            editor.putString("lastName", this.user.getLastName());
            editor.commit();
        } else {
            SharedPreferences settings = getSharedPreferences("user", MODE_PRIVATE);
            this.user.setFirstName(settings.getString("firstName", "Prénom"));
            this.user.setLastName(settings.getString("lastName", "Nom"));
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_city:
                    System.out.println("NEW CITY");
                    Intent cityIntent = new Intent(MapsActivity.this, NewCityActivity.class);
                    cityIntent.putExtra("user", user);
                    startActivity(cityIntent);
                    return true;
                case R.id.navigation_localisation:
                    System.out.println("LOCALISATION");
                    return true;
                case R.id.navigation_profil:
                    System.out.println("PROFIL");
                    Intent profilIntent = new Intent(MapsActivity.this, ProfilActivity.class);
                    profilIntent.putExtra("user", user);
                    startActivity(profilIntent);
                    return true;
            }
            return false;
        }

    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (createdCity != null) {
            newCity(createdCity.getTitle(), createdCity.getAddress(), createdCity.getLatitude(), createdCity.getLongitude(), createdCity.getCreator(), createdCity.getPictureId());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onStart() {
        super.onStart();

        // Connect to database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("citys");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cityList.clear();
                for(DataSnapshot citySnapshot : dataSnapshot.getChildren()){
                    City city = citySnapshot.getValue(City.class);
                    Log.i(loadCity, city.getTitle());
                    cityList.add(city);

                }
                putAllCity(cityList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void putAllCity(List<City> cityList) {
        if (cityList.size() == 0) {
            return;
        }
        cityList.forEach(city -> {
            lastCity = city;
            mMap.addMarker(new MarkerOptions().position(new LatLng( city.getLatitude(), city.getLongitude())).title(city.getTitle()));
        });
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng( lastCity.getLatitude(), lastCity.getLongitude())));
    }

    public void newCity(String title, String address, double lat, double lng, User creator, String pictureId) {
        //Add to BDD
        myRef = database.getReference("/citys");
        String id = myRef.push().getKey();
        City newCity = new City(id, title, address, lat, lng, creator, pictureId);
        myRef.child(id).setValue(newCity);
        lastCity = newCity;

        //Add to the Map
        mMap.addMarker(new MarkerOptions().position(new LatLng( lat, lng)).title(title));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng( lat, lng)));

        Toast.makeText(this,title + " sauvegardé", Toast.LENGTH_LONG).show();
    }
}
