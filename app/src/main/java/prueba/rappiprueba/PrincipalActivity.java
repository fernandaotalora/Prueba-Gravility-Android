package prueba.rappiprueba;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import adapter.AdapterApplication;
import adapter.DataApplication;
import connection.DownloadInformation;
import connection.Connection;
import util.Consulta;

public class PrincipalActivity extends AppCompatActivity implements OnItemClickListener {

    private ListView                    listApplication;
    private Intent                      newForm;
    private AdapterApplication          adapterInformacion;
    private ArrayList<DataApplication>  arrayData = new ArrayList<DataApplication>();
    private ArrayList<String>           arrayCatergory = new ArrayList<String>();

    private Consulta consulta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        this.listApplication = (ListView)findViewById(R.id.ListAplicaciones);
        this.consulta        = new Consulta();

        this.arrayData.clear();
        this.arrayData = consulta.getListApplicationAll();
        this.arrayCatergory = consulta.getAllCategory();

        this.adapterInformacion = new AdapterApplication(PrincipalActivity.this,arrayData);
        this.listApplication.setAdapter(adapterInformacion);
        this.listApplication.setOnItemClickListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        menu.add("All");
        for(int i=0;i<this.arrayCatergory.size();i++){
            menu.add(this.arrayCatergory.get(i));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getTitle().equals("All")){
            this.arrayData.clear();
            this.arrayData = consulta.getListApplicationAll();
            this.adapterInformacion = new AdapterApplication(PrincipalActivity.this,arrayData);
            this.listApplication.setAdapter(adapterInformacion);
            this.listApplication.setOnItemClickListener(this);
        }else{
            this.arrayData.clear();
            this.arrayData = consulta.getListApplicationByCategory(String.valueOf(item.getTitle()));
            this.adapterInformacion = new AdapterApplication(PrincipalActivity.this,arrayData);
            this.listApplication.setAdapter(adapterInformacion);
            this.listApplication.setOnItemClickListener(this);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.newForm = new Intent(this, DetailApplication.class);
        this.newForm.putExtra("nameApplication",this.arrayData.get(position).getName());
        startActivity(this.newForm);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
