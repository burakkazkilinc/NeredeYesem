package com.gmail.burakkazkilinc.nerdeyesem;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private String TAG = MainActivity.class.getSimpleName();
    ListView liste;
    List<RestaurantInfo> restoran_listesi;
    private LocationManager locationManager;
    private String provider;
    Location location;
    double lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new JSONDownload().execute();
        liste = findViewById(R.id.list);

        liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
                intent.putExtra("url", ""+restoran_listesi.get(position).getUrl());
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(provider, 400, 1, this);
        }
        locationManager.requestLocationUpdates(provider, 400, 1, this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
         lat = location.getLatitude();
         lng = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    private class JSONDownload extends AsyncTask<Void, Void, Void> {
        String url = "https://developers.zomato.com/api/v2.1/search?apikey=e6e83a56cad719f45089e52afd462b9d&count=5&";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Toast.makeText(MainActivity.this,"Data is downloading",Toast.LENGTH_SHORT).show();

            restoran_listesi = new ArrayList<RestaurantInfo>();

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            Criteria criteria = new Criteria();
            provider = locationManager.getBestProvider(criteria, false);
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                location = locationManager.getLastKnownLocation(provider);
            }
            else
            {
                location = locationManager.getLastKnownLocation(provider);
            }

            if (location != null) {
                Log.i(TAG, "Provider " + provider + " has been selected.");
                onLocationChanged(location);
            } else {
                lat = 0;
                lng = 0;
            }

            //konum bilgisinden gelen lat,long alınır

            url+="lat="+lat+"&lon="+lng+"&sort=real_distance&order=asc";

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: "+jsonStr.length() + jsonStr);
            if (jsonStr != null) {
                try {
                    // Getting JSON Array node
                    JSONObject object = new JSONObject(jsonStr);
                    JSONArray restoranlar = object.getJSONArray("restaurants");

                        for(int i=0;i<restoranlar.length();i++)
                        {
                            JSONObject r = restoranlar.getJSONObject(i);
                            JSONObject restaurant = r.getJSONObject("restaurant");
                            JSONObject rlocation = restaurant.getJSONObject("location");
                            JSONObject rrating = restaurant.getJSONObject("user_rating");
                            RestaurantInfo rinfo = new RestaurantInfo(restaurant.getString("name"));
                            rinfo.setAddress(rlocation.getString("address"));
                            rinfo.setRating(rrating.getString("aggregate_rating"));
                            rinfo.setType(restaurant.getString("cuisines"));
                            rinfo.setRating_text(rrating.getString("rating_text"));
                            rinfo.setUrl(restaurant.getString("url"));
                            restoran_listesi.add(rinfo);
                        }

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Toast.makeText(MainActivity.this,"Download complete!",Toast.LENGTH_SHORT).show();
            ArrayAdapter<RestaurantInfo> adaptor = new ArrayAdapter<RestaurantInfo>(MainActivity.this,android.R.layout.simple_list_item_1,restoran_listesi);
            liste.setAdapter(adaptor);
        }
    }
}
