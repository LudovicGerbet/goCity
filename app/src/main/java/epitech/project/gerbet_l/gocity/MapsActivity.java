package epitech.project.gerbet_l.gocity;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Put citys on map
        cityList = new ArrayList<>();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Connect to database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("citys");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cityList.clear();
                for(DataSnapshot citySnapshot : dataSnapshot.getChildren()){
                    City city = citySnapshot.getValue(City.class);
                    cityList.add(city);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        newCity("Stade de Bartrès",43.123347,-0.045800);
        newCity("City de Sydney", -34, 151);
    }

    public void newCity(String title, double lat, double lng) {
        //Add to BDD
        myRef = database.getReference("/citys");
        String id = myRef.push().getKey();
        City newCity = new City(id, title, lat, lng);
        myRef.child(id).setValue(newCity);

        //Add to the Map
        mMap.addMarker(new MarkerOptions().position(new LatLng( lat, lng)).title(title));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng( lat, lng)));

        Toast.makeText(this,title + " sauvegardé", Toast.LENGTH_LONG).show();
    }
}
