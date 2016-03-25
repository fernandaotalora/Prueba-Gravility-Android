package prueba.rappiprueba;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import model.DataEntry;
import util.Consulta;
import util.ImageProcessing;

public class DetailApplication extends AppCompatActivity implements View.OnClickListener{

    Bundle bundle;
    String nameApplication;
    Consulta consulta;

    private ImageView   imageEntry;
    private TextView    nameEntry,titleEntry,priceEntry,rigthsEntry;
    private TextView    summaryEntry;
    private ImageButton imageDownload;

    private DataEntry   dataEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_application);

        this.bundle             =  getIntent().getExtras();
        this.nameApplication    = bundle.getString("nameApplication");
        this.consulta           = new Consulta();

        this.imageEntry         = (ImageView)findViewById(R.id.imageEntryDetail);
        this.imageDownload      = (ImageButton)findViewById(R.id.imageEntryDownload);
        this.nameEntry          = (TextView)findViewById(R.id.nameEntryDetail);
        this.titleEntry         = (TextView)findViewById(R.id.titleEntryDetail);
        this.priceEntry         = (TextView)findViewById(R.id.priceEntryDetail);
        this.rigthsEntry        = (TextView)findViewById(R.id.rigthsEntryDetail);
        this.summaryEntry       = (TextView)findViewById(R.id.summaryEntry);

        this.dataEntry          = new DataEntry();

        this.dataEntry          = this.consulta.getDetailApplication(nameApplication);

        this.imageEntry.setImageBitmap(ImageProcessing.decodeBitmapFromBase64(this.dataEntry.getImageB64()));
        this.nameEntry.setText(this.dataEntry.getName());
        this.titleEntry.setText(this.dataEntry.getTitle());
        this.priceEntry.setText(this.dataEntry.getPriceAumount()+" "+this.dataEntry.getPriceCurrency());
        this.rigthsEntry.setText(this.dataEntry.getRights());
        this.summaryEntry.setText(this.dataEntry.getSummary());

        this.imageDownload.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageEntryDownload:
                    Uri uri = Uri.parse(this.dataEntry.getDownloadLink());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                break;
        }
    }
}
