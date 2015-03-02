package org.rbdc.sra.helperClasses;


import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.rbdc.sra.objects.FoodItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Asyncer extends AsyncTask<String,Void,FoodItem>{
    final private static String key = "c69bd76b98dd8d4e1fd629241b3bb199";
    final private static String id = "f67bfd42";

    @Override
    protected FoodItem doInBackground(String... params) {
        final HttpClient httpclient = new DefaultHttpClient();
        final HttpPost httppost = new HttpPost("https://api.nutritionix.com/v1_1/search");
        FoodItem item = new FoodItem();
        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<>(3);
            nameValuePairs.add(new BasicNameValuePair("appId", id));
            nameValuePairs.add(new BasicNameValuePair("appKey", key));
            nameValuePairs.add(new BasicNameValuePair("query",params[0]));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request

            String string = new String();
            HttpResponse response;
            try{
                response = httpclient.execute(httppost);
                string = EntityUtils.toString(response.getEntity());
                Log.i("Fired: ",string);

            }catch (IOException e){
                Log.i("Failed",e.getLocalizedMessage());
            }
            System.out.println("Flag");
            try{

                JSONObject object = new JSONObject(string);
                String first = object.getJSONArray("hits").getJSONObject(0).get("_id").toString();
                Log.i("Object",first);
                HttpGet httpGet = new HttpGet("https://api.nutritionix.com/v1_1/item?id=" + first +"&appId=" + id + "&appKey=" + key);
                response = httpclient.execute(httpGet);
                String string1 = EntityUtils.toString(response.getEntity());
                Log.i("Item: ",string1);
                Gson gson = new GsonBuilder().create();
                item = gson.fromJson(string1,FoodItem.class);
                Log.i("Item",item.getItemName());

            }
            catch (JSONException e){
                Log.i("Failed",e.getLocalizedMessage());
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        return item;
    }

    @Override
    protected void onPostExecute(FoodItem result) {

    }

    @Override
    protected void onPreExecute() {}

    @Override
    protected void onProgressUpdate(Void... values) {}
}
