package adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import prueba.rappiprueba.R;
import prueba.rappiprueba.R.*;
import util.ImageProcessing;

/**
 * Created by User on 23/03/2016.
 */
public class AdapterApplication extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<DataApplication> arrayData;

    public AdapterApplication(Activity act, ArrayList<DataApplication> listData){
        this.activity = act;
        this.arrayData = listData;
    }
    @Override
    public int getCount() {
        return arrayData.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(convertView == null){
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(layout.adapter_application, null);
        }

        DataApplication data = arrayData.get(position);

        ImageView   image       = (ImageView)v.findViewById(R.id.imageIconApplication);
        TextView    name        = (TextView)v.findViewById(R.id.nameEntry);
        TextView    aumount     = (TextView)v.findViewById(R.id.priceEntryAumount);
        TextView    currency    = (TextView)v.findViewById(R.id.priceEntryCurrency);
        TextView    rights      = (TextView)v.findViewById(R.id.rigtsEntry);

        image.setImageBitmap(ImageProcessing.decodeBitmapFromBase64(data.getImageBase64()));
        name.setText(data.getName());
        aumount.setText(data.getPriceAumount());
        currency.setText(data.getPriceCurrency());
        rights.setText(data.getRights());

        return v;
    }
}
