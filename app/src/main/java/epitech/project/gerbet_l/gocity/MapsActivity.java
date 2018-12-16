package epitech.project.gerbet_l.gocity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.nfc.Tag;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private List<City> cityList;
    private City lastCity;
    private City createdCity;
    private User user;
    private Location mLocation;
    private LocationManager locationManager;

    private static final String[] INITIAL_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS
    };
    private static final String[] LOCATION_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final int INITIAL_REQUEST=1337;
    private static final int LOCATION_REQUEST=INITIAL_REQUEST+3;
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
        mLocation = null;
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
            editor.putString("token", this.user.getToken());
            editor.commit();
        } else {
            SharedPreferences settings = getSharedPreferences("user", MODE_PRIVATE);
            this.user.setFirstName(settings.getString("firstName", "Prénom"));
            this.user.setLastName(settings.getString("lastName", "Nom"));
            this.user.setToken(settings.getString("token", null));
        }
        requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
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
                    if (mLocation != null) {
                        LatLng latLng = new LatLng(mLocation.getLatitude(),mLocation.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
                    }
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
            newCity(createdCity.getTitle(), createdCity.getAddress(), createdCity.getLatitude(), createdCity.getLongitude(), createdCity.getCreator(), createdCity.getPictureId(), createdCity.getDescription());
        }
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LatLng position = marker.getPosition();
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot citySnapshot : dataSnapshot.getChildren()){
                            City city = citySnapshot.getValue(City.class);
                            if (city.getLatitude() == position.latitude && city.getLongitude() == position.longitude) {
                                System.out.println(city.getTitle());
                                //Basculer sur la nouvelle activitée
                                Intent detailIntent = new Intent(MapsActivity.this, DetailActivity.class);
                                detailIntent.putExtra("user", user);
                                detailIntent.putExtra("city", city);
                                startActivity(detailIntent);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                return true;
            }
        });
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

    public void newCity(String title, String address, double lat, double lng, User creator, String pictureId, String description) {
        //Add to BDD
        myRef = database.getReference("/citys");
        String id = myRef.push().getKey();
        City newCity = new City(id, title, address, lat, lng, creator, pictureId, description);
        myRef.child(id).setValue(newCity);
        lastCity = newCity;

        //Add to the Map
        mMap.addMarker(new MarkerOptions().position(new LatLng( lat, lng)).title(title));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng( lat, lng)));

        Toast.makeText(this,title + " sauvegardé", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResume() {
        super.onResume();

        //Obtention de la référence du service
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        //Si le GPS est disponible, on s'y abonne
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            abonnementGPS();
        }

        if (mLocation != null) {
            LatLng latLng = new LatLng(mLocation.getLatitude(),mLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
        }
    }
    @Override
    public void onPause() {
        super.onPause();

        //On appelle la méthode pour se désabonner
        desabonnementGPS();
    }

    /**
     * Méthode permettant de s'abonner à la localisation par GPS.
     */
    public void abonnementGPS() {
        //On s'abonne
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);
    }

    /**
     * Méthode permettant de se désabonner de la localisation par GPS.
     */
    public void desabonnementGPS() {
        //Si le GPS est disponible, on s'y abonne
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(final Location location) {
        //On affiche dans un Toat la nouvelle Localisation
        mLocation = location;
        /*final StringBuilder msg = new StringBuilder("lat : ");
        msg.append(location.getLatitude());
        msg.append( "; lng : ");
        msg.append(location.getLongitude());

        Toast.makeText(this, msg.toString(), Toast.LENGTH_SHORT).show();
        */
    }

    @Override
    public void onProviderDisabled(final String provider) {
        //Si le GPS est désactivé on se désabonne
        if("gps".equals(provider)) {
            desabonnementGPS();
        }
    }

    @Override
    public void onProviderEnabled(final String provider) {
        //Si le GPS est activé on s'abonne
        if("gps".equals(provider)) {
            abonnementGPS();
        }
    }

    @Override
    public void onStatusChanged(final String provider, final int status, final Bundle extras) { }

}
