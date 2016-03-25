package util;

import android.content.ContentValues;

import org.json.JSONArray;
import org.json.JSONException;

import model.DataEntry;
import model.DataFeed;
import persistence.DBAItunesApplication;

/**
 * Created by User on 23/03/2016.
 */
public class UpdateInformation {
    private static DBAItunesApplication fcnData;
    private ContentValues _tempRegistro;

    public UpdateInformation(){
        this.fcnData = DBAItunesApplication.getInstance();
        this._tempRegistro = new ContentValues();
    }

    public void updateFeed(DataFeed datos){
        this._tempRegistro.clear();
        this._tempRegistro.put("author_name", datos.getAuthor());
        this._tempRegistro.put("update_last",datos.getUpdateLast());
        this._tempRegistro.put("rights",datos.getRights());
        this._tempRegistro.put("title",datos.getTittle());
        this.fcnData.InsertRegistro("data_feed",this._tempRegistro);
    }

    public void updateFeedImage(DataEntry dataImage){
            this._tempRegistro.clear();
            this._tempRegistro.put("name", dataImage.getName());
            this._tempRegistro.put("summary",dataImage.getSummary());
            this._tempRegistro.put("price_amount", dataImage.getPriceAumount());
            this._tempRegistro.put("price_currency", dataImage.getPriceCurrency());
            this._tempRegistro.put("rights", dataImage.getRights());
            this._tempRegistro.put("title", dataImage.getTitle());
            this._tempRegistro.put("link", dataImage.getDownloadLink());
            this._tempRegistro.put("category",dataImage.getCategory());
            this._tempRegistro.put("image",dataImage.getImageB64());
            this.fcnData.InsertRegistro("data_entry", this._tempRegistro);
    }
}
