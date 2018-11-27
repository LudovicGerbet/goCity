package epitech.project.gerbet_l.gocity;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private List<City> cityList;
    private City lastCity;

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

        //Put citys on map
        cityList = new ArrayList<>();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_city:
                    System.out.println("NEW CITY");
                    //A APPELER LORS DE EVENT BOUTON NEW CITY
                    //newCity("Stade de Bartrès",43.123347,-0.045800);
                    //newCity("City de Sydney", -34, 151);
                    return true;
                case R.id.navigation_localisation:
                    System.out.println("LOCALISATION");
                    return true;
                case R.id.navigation_profil:
                    System.out.println("PROFIL");
                    Intent intent = new Intent(MapsActivity.this, ProfilActivity.class);
                    startActivity(intent);
                    return true;
            }
            return false;
        }

    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
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
                    putAllCity(cityList);
                }
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

    public void newCity(String title, double lat, double lng) {
        //Add to BDD
        myRef = database.getReference("/citys");
        String id = myRef.push().getKey();
        City newCity = new City(id, title, lat, lng);
        myRef.child(id).setValue(newCity);
        lastCity = newCity;

        //Add to the Map
        mMap.addMarker(new MarkerOptions().position(new LatLng( lat, lng)).title(title));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng( lat, lng)));

        Toast.makeText(this,title + " sauvegardé", Toast.LENGTH_LONG).show();
    }
}
