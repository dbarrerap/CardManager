package org.dbarrera.cardmanager;

import android.util.Log;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dav3 on 6/13/13.
 */
public class wsClient extends Thread {
    private static final String url = "http://10.0.2.2/cardmanager/index.php";

    private List<NameValuePair> parametros = null;

    public void setParametros(String card_name, String card_number, String card_ccv,
                              String card_type, String card_intl, String card_debit,
                              String card_bank, String card_validthru_month, String card_validthru_year,
                              String card_image){
        this.parametros = new ArrayList<NameValuePair>();
        this.parametros.add(new BasicNameValuePair("opcion","NEWCC"));
        this.parametros.add(new BasicNameValuePair("card_name",card_name));
        this.parametros.add(new BasicNameValuePair("card_number",card_number));
        this.parametros.add(new BasicNameValuePair("card_ccv",card_ccv));
        this.parametros.add(new BasicNameValuePair("card_type",card_type));
        this.parametros.add(new BasicNameValuePair("card_intl",card_intl));
        this.parametros.add(new BasicNameValuePair("card_debit",card_debit));
        this.parametros.add(new BasicNameValuePair("card_bank",card_bank));
        this.parametros.add(new BasicNameValuePair("card_validthru_month",card_validthru_month));
        this.parametros.add(new BasicNameValuePair("card_validthru_year",card_validthru_year));
        this.parametros.add(new BasicNameValuePair("card_image",card_image));
    }

    @Override
    public void run() {
        JSONObject json = JSONParser.getJSONFromUrl(url, parametros);
        try {
            JSONArray jsonArray = json.getJSONArray("exito");
            if (jsonArray != null){
                Log.d(wsClient.class.getName(),"Success! Data has been saved.");
            } else {
                Log.d(wsClient.class.getName(),"Error! Parsing could not be made.");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
