package epitech.project.gerbet_l.gocity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetailActivity extends AppCompatActivity {


    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private City thisCity;
    private ImageView cityPicture;
    private TextView cityTitle;
    private TextView description;
    private EditText editDesc;
    private ImageButton descModificationButton;
    FirebaseStorage storage;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_detail);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        storage = FirebaseStorage.getInstance();
        this.thisCity = (City) getIntent().getSerializableExtra("city");
        this.user = (User) getIntent().getSerializableExtra("user");

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("/citys");
        cityPicture = findViewById(R.id.detailedCityPicture);
        cityTitle = findViewById(R.id.detailedCityTitle);
        description = findViewById(R.id.description);
        editDesc = findViewById(R.id.hiddenEditDesc);
        descModificationButton = findViewById(R.id.descModification);
        cityTitle.setText(thisCity.getTitle());
        description.setText(thisCity.getDescription());
        ViewSwitcher switcher = (ViewSwitcher) findViewById(R.id.my_switcher);
        getPicture(thisCity.getPictureId());
        descModificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switcher.getCurrentView() == description) {
                    switcher.showNext(); //or switcher.showPrevious();
                    //descModificationButton.setBackgroundColor(R.color.colorLight);
                    descModificationButton.setBackgroundResource(R.color.colorLight);
                    editDesc.setText(thisCity.getDescription());
                } else {
                    thisCity.setDescription(editDesc.getText().toString());
                    myRef.child(thisCity.getId()).setValue(thisCity);
                    description.setText(thisCity.getDescription());
                    switcher.showPrevious();
                    descModificationButton.setBackgroundResource(R.color.colorPrimary);
                }
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_city:
                    System.out.println("NEW CITY");
                    Intent cityIntent = new Intent(DetailActivity.this, NewCityActivity.class);
                    startActivity(cityIntent);
                    return true;
                case R.id.navigation_localisation:
                    System.out.println("LOCALISATION");
                    Intent mapsIntent = new Intent(DetailActivity.this, MapsActivity.class);
                    startActivity(mapsIntent);
                    return true;
                case R.id.navigation_profil:
                    System.out.println("PROFIL");
                    Intent profilIntent = new Intent(DetailActivity.this, ProfilActivity.class);
                    profilIntent.putExtra("user", user);
                    startActivity(profilIntent);
                    return true;
            }
            return false;
        }

    };

    protected void getPicture(String pictureId) {
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();

        // Create a reference with an initial file path and name
        StorageReference pathReference = storageRef.child("images/" + pictureId);

        final long ONE_MEGABYTE = 1024 * 1024;
        pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                cityPicture.setImageBitmap(Bitmap.createBitmap(bmp));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }
}
