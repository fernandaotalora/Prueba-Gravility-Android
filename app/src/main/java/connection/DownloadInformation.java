package connection;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Observable;
import java.util.Observer;

import model.DataEntry;
import model.DataFeed;
import util.ImageProcessing;
import util.UpdateInformation;

/**
 * Created by User on 16/03/2016.
 */
public class DownloadInformation extends AsyncTask<String, String, String>  {

    Connection informacion;
    DataFeed newFeed;
    DataEntry newEntry;
    UpdateInformation   updateInfo;


    public DownloadInformation(Context context){
        this.informacion    = new Connection();
        this.newFeed        = new DataFeed();
        this.updateInfo     = new UpdateInformation();
        this.newEntry       = new DataEntry();
    }


    protected void onPreExecute(){
    }

    @Override
    protected String doInBackground(String... params) {
        Bitmap entryBitmapImage;
        try{
            JSONObject dataFeed = informacion.getJson(params[0]).getJSONObject("feed");
            newFeed.setAuthor(dataFeed.getJSONObject("author").getJSONObject("name").getString("label"));
            newFeed.setUpdateLast(dataFeed.getJSONObject("updated").getString("label"));
            newFeed.setRights(dataFeed.getJSONObject("rights").getString("label"));
            newFeed.setTittle(dataFeed.getJSONObject("title").getString("label"));
            this.updateInfo.updateFeed(newFeed);

            JSONArray feedImage = dataFeed.getJSONArray("entry");
            for(int i=0;i<feedImage.length();i++) {
                newEntry.setName(feedImage.getJSONObject(i).getJSONObject("im:name").getString("label"));
                newEntry.setSummary(feedImage.getJSONObject(i).getJSONObject("summary").getString("label"));
                newEntry.setPriceAumount(feedImage.getJSONObject(i).getJSONObject("im:price").getJSONObject("attributes").getString("amount"));
                newEntry.setPriceCurrency(feedImage.getJSONObject(i).getJSONObject("im:price").getJSONObject("attributes").getString("currency"));
                newEntry.setRights(feedImage.getJSONObject(i).getJSONObject("rights").getString("label"));
                newEntry.setTitle(feedImage.getJSONObject(i).getJSONObject("title").getString("label"));
                newEntry.setDownloadLink(feedImage.getJSONObject(i).getJSONObject("link").getJSONObject("attributes").getString("href"));
                newEntry.setCategory(feedImage.getJSONObject(i).getJSONObject("category").getJSONObject("attributes").getString("label"));

                try{
                    switch(Integer.parseInt(params[1])){

                        case DisplayMetrics.DENSITY_MEDIUM:

                            entryBitmapImage = informacion.downloadImage(feedImage.getJSONObject(i).getJSONArray("im:image").getJSONObject(0).getString("label"));
                            newEntry.setImageB64(ImageProcessing.encodeBitmapToBase64(entryBitmapImage, 100));
                            break;

                        case DisplayMetrics.DENSITY_HIGH:

                            entryBitmapImage = informacion.downloadImage(feedImage.getJSONObject(i).getJSONArray("im:image").getJSONObject(1).getString("label"));
                            newEntry.setImageB64(ImageProcessing.encodeBitmapToBase64(entryBitmapImage, 100));

                            break;

                        case DisplayMetrics.DENSITY_XHIGH:
                        case DisplayMetrics.DENSITY_XXHIGH:
                        case DisplayMetrics.DENSITY_XXXHIGH:

                            entryBitmapImage = informacion.downloadImage(feedImage.getJSONObject(i).getJSONArray("im:image").getJSONObject(2).getString("label"));
                            newEntry.setImageB64(ImageProcessing.encodeBitmapToBase64(entryBitmapImage, 100));

                            break;
                    }
                }catch (Exception e){
                    Log.e("Exception", "Error: " + e.toString());
                }

                this.updateInfo.updateFeedImage(newEntry);
            }
        }
        catch (JSONException e) {
            Log.e("JSONException", "Error: " + e.toString());
        }
        return "";
    }



    @Override
    protected void onPostExecute(String rta) {
    }


    @Override
    protected void onProgressUpdate(String... values) {

    }

}

