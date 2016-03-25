package util;

import android.content.ContentValues;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import adapter.DataApplication;
import model.DataEntry;
import persistence.DBAItunesApplication;

/**
 * Created by User on 23/03/2016.
 */
public class Consulta {
    DBAItunesApplication fcnData;

    private ContentValues                      _tempRegistro;
    private ArrayList<ContentValues>           _tempTabla;
    private ArrayList<DataApplication>         listApplication;
    private DataEntry                          detailEntry;

    private ArrayList<String>                   categoryEntry;

    public Consulta(){
        this.fcnData            = DBAItunesApplication.getInstance();
        this._tempRegistro      = new ContentValues();
        this._tempTabla         = new ArrayList<ContentValues>();
        this.listApplication    = new ArrayList<DataApplication>();
        this.detailEntry        = new DataEntry();
        this.categoryEntry      = new ArrayList<String>();
    }

    public ArrayList<DataApplication> getListApplicationAll(){
        _tempRegistro.clear();
        _tempTabla.clear();
        _tempTabla = fcnData.SelectData("data_entry", "name,image,price_amount,price_currency,rights,title", "category is not null");
        for(int i=0;i<_tempTabla.size();i++){
            _tempRegistro = _tempTabla.get(i);

            listApplication.add(new DataApplication(_tempRegistro.getAsString("image"),_tempRegistro.getAsString("name"),_tempRegistro.getAsString("title"),_tempRegistro.getAsString("price_amount"),_tempRegistro.getAsString("price_currency"),_tempRegistro.getAsString("rights")));
        }

        return listApplication;
    }

    public DataEntry getDetailApplication(String name){
        this._tempRegistro.clear();
        this._tempRegistro = this.fcnData.SelectDataRegistro("data_entry", "name,image,price_amount,price_currency,rights,title,link,summary", "name ='"+name+"'");
        this.detailEntry.setName(this._tempRegistro.getAsString("name"));
        this.detailEntry.setImageB64(this._tempRegistro.getAsString("image"));
        this.detailEntry.setPriceAumount(this._tempRegistro.getAsString("price_amount"));
        this.detailEntry.setPriceCurrency(this._tempRegistro.getAsString("price_currency"));
        this.detailEntry.setRights(this._tempRegistro.getAsString("rights"));
        this.detailEntry.setTitle(this._tempRegistro.getAsString("title"));
        this.detailEntry.setDownloadLink(this._tempRegistro.getAsString("link"));
        this.detailEntry.setSummary(this._tempRegistro.getAsString("summary"));
        return detailEntry;
    }

    public ArrayList<DataApplication> getListApplicationByCategory(String category){
        _tempRegistro.clear();
        _tempTabla.clear();
        _tempTabla = fcnData.SelectData("data_entry", "name,image,price_amount,price_currency,rights,title", "category='"+category+"'");
        for(int i=0;i<_tempTabla.size();i++){
            _tempRegistro = _tempTabla.get(i);

            listApplication.add(new DataApplication(_tempRegistro.getAsString("image"),_tempRegistro.getAsString("name"),_tempRegistro.getAsString("title"),_tempRegistro.getAsString("price_amount"),_tempRegistro.getAsString("price_currency"),_tempRegistro.getAsString("rights")));
        }

        return listApplication;
    }

    public ArrayList<String> getAllCategory(){
        this._tempTabla.clear();
        this._tempTabla = this.fcnData.SelectData("data_entry", "category", "category is not null");
        for (int i=0;i<this._tempTabla.size();i++){
            this._tempRegistro = this._tempTabla.get(i);
            this.categoryEntry.add(this._tempRegistro.getAsString("category"));
        }
        return this.categoryEntry;
    }
}
