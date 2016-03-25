package prueba.rappiprueba;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import connection.DownloadInformation;
import persistence.DBAItunesApplication;
import util.DeviceProperties;
import util.VarGlobal;


public class SplashActivity extends Activity implements VarGlobal{

    private static final long SPLASH_SCREEN_DELAY = 1000;
    private static final String URL_ITUNES = "https://itunes.apple.com/us/rss/topfreeapplications/limit=20/json";
    public static Context context;
    String lastRssUpdate = "";
    DisplayMetrics displayMetrics;
    Timer timer;

    ProgressBar splashProgressBar;
    TextView splashTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        splashProgressBar   = (ProgressBar) findViewById(R.id.splashProgressBar);
        splashTextView      = (TextView) findViewById(R.id.splashTextView);

        /*TimerTask task = new TimerTask() {
            @Override
            public void run() {

                // Start the next activity
                Intent mainIntent = new Intent().setClass(SplashActivity.this, PrincipalActivity.class);
                startActivity(mainIntent);

                // Close the activity so the user won't able to go back this
                // activity pressing Back button
                finish();
            }
        };*/

        // Simulate a long loading process on application startup.
        this.timer = new Timer();
        //timer.schedule(task, SPLASH_SCREEN_DELAY);

        this.context = this;
    }

    @Override
    public void onStart(){
        super.onStart();

        DBAItunesApplication fcnData = DBAItunesApplication.getInstance();
        lastRssUpdate = fcnData.StrSelectShieldWhere("data_feed", "update_last", "author_name is not null");

        //Verify device internet connection
        if(DeviceProperties.checkConnectivity(this)){

            new DownloadInformation(this).execute(URL_ITUNES,displayMetrics.densityDpi+"");
            timer.schedule(new TimerTask() {

                public void run() {

                    Intent topAppsIntent = new Intent(SplashActivity.this, PrincipalActivity.class);
                    startActivity(topAppsIntent);
                    overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_forward_out);

                    try {
                        SplashActivity.this.finish();
                        overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_forward_out);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }, SPLASH_SCREEN_DELAY);

        }else {

            //Validar si existe datos en la base de datos
            if(!fcnData.ExistRegistros("data_feed","author_name is not null")){

                //Without connection or entries stored in DB, then... Bye bye!
                new AlertDialog.Builder(this)
                        .setTitle("iTUNES TOP APPS")
                        .setMessage("To synchronize de iTunes Apps List, the internet connection must be available. Please check and try again!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dlg, int sumthin) {

                                SplashActivity.this.finish();
                                overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_forward_out);

                            }
                        })
                        .show();

            }else{

                //Show RSS saved in DB if user wants it
                new AlertDialog.Builder(this)
                        .setTitle("iTUNES APP OFFLINE")
                        .setMessage("Your device does not have internet access!\n" +
                                "Last Update: "+lastRssUpdate+"\n" +"Do you want to continue?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dlg, int sumthin) {

                                Intent topAppsIntent = new Intent(SplashActivity.this, PrincipalActivity.class);
                                startActivity(topAppsIntent);
                                overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_forward_out);
                                SplashActivity.this.finish();

                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dlg, int sumthin) {

                                SplashActivity.this.finish();
                                overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_forward_out);
                            }
                        })
                        .show();

            }
        }
    }
}
