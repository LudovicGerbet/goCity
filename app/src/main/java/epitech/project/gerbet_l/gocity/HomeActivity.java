package epitech.project.gerbet_l.gocity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

public class HomeActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;
    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_home);
        prefs = getSharedPreferences("epitech.project.gerbet_l.gocity", MODE_PRIVATE);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                if (prefs.getBoolean("firstrun", true)) {
                    User createdUser = new User("Prénom", "Nom");
                    prefs.edit().putBoolean("firstrun", false).commit();
                    Intent profilIntent = new Intent(HomeActivity.this, ProfilActivity.class);
                    profilIntent.putExtra("firstRun", true);
                    profilIntent.putExtra("user", createdUser);
                    startActivity(profilIntent);
                    finish();
                } else {
                    Intent homeIntent = new Intent(HomeActivity.this, MapsActivity.class);
                    startActivity(homeIntent);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);

    }
}
