package com.example.janet.foodhygiene;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

public class FHGoogleMap extends FragmentActivity implements OnMapReadyCallback {


    private static final String LOG_TAG = "Google Map ";
    //ArrayList<Restaurant>restaurants;
    public static final double Radius=6372.8;
    URI uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fhgoogle_map);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //restaurants=getIntent().getParcelableArrayListExtra("restaurants")  ;
        Intent intent = getIntent();
        //String jsonArray = intent.getStringExtra("jsonArray");
        String city = intent.getStringExtra("city");

        /*try {
            JSONArray array = new JSONArray(jsonArray);
            System.out.println(array.toString(2));
        } catch (JSONException e) {
            e.printStackTrace();
        }*/




    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private void setUpMap() {
        //Retrieve the city data from the web service
        //in a worker thread since it is a network opertion
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    retrieveRestaurants();
                } catch (IOException ie) {
                    Log.e(LOG_TAG, "cannot retrieve restaurants", ie);
                    return;
                }
            }
        }).start();
    }

    protected void retrieveRestaurants() throws IOException {

    }

    //calculate the distances in Kilo Meters
    public static double distance(double lat1, double lon1, double lat2, double lon2)
    {
        double dLat=Math.toRadians(lat2-lat1);
        double dLon=Math.toRadians(lon2-lon1);

        lat1=Math.toRadians(lat1);
        lat2=Math.toRadians(lat2);

        double a=Math.sin(dLat/2) * Math.sin(dLat/2) +Math.sin(dLon/2)*Math.cos(lat1)*Math.cos(lat2);
        double c=2*Math.asin(Math.sqrt(a));
        return Radius *c;
    }


    void createMarkersFromJson(String json) throws JSONException {
        // De-serialize the JSON string into an array of restaurants objects
        JSONArray jsonArray = new JSONArray(json);
        for (int i = 0; i < jsonArray.length(); i++) {
            // Create a marker for each restaurant in the JSON data.
            JSONObject jsonObj = jsonArray.getJSONObject(i);

        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

/*
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        googleMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney"));
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        googleMap.getUiSettings().setZoomControlsEnabled(true);

*/


       /*map.addMarker(new MarkerOptions()
                .title(jsonObj.getString("BusinessName"))
                .snippet(Integer.toString(jsonObj.getInt("RatingValue")))
                .position(new LatLng(
                        jsonObj.getJSONArray("Latitude").getDouble(0),
                        jsonObj.getJSONArray("Longitude").getDouble(1)
                ))
        );*/

        Intent intent = getIntent();
        //String jsonArray = intent.getStringExtra("jsonArray");
        String city = intent.getStringExtra("city");

        JSONArray jsonRestaurantList = null;
        try {
            jsonRestaurantList = getJSONRestaurants(city);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        //process json to java Object
        ArrayList<Restaurant> restaurants = new ArrayList<>();
        try {
            restaurants = SearchByLatLong.fromJson(jsonRestaurantList);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }


        for (int i=0;i<restaurants.size();i++)
        {
            Restaurant r = new Restaurant();
            r.setLatitude(restaurants.get(i).getLatitude());
            r.setLongitude(restaurants.get(i).getLongitude());

            Marker marker =googleMap.addMarker(new MarkerOptions()
                    .title(r.getBusinessName())
                    .position(new LatLng(r.getLatitude(),r.getLongitude()))
                    .title(restaurants.get(i).getBusinessName())
                    .snippet(restaurants.get(i).getPostCode())
            );

            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(r.getLatitude(),r.getLongitude()),6.0F));

        }
        Restaurant restaurant=new Restaurant();
        restaurant.setBusinessName(restaurant.getBusinessName());
        restaurant.setAddressLine1(restaurant.getAddressLine1());
        restaurant.setAddressLine2(restaurant.getAddressLine2());
        restaurant.setAddressLine3(restaurant.getAddressLine3());
        restaurant.setPostCode(restaurant.getPostCode());


        Restaurant r = new Restaurant();
        r.setBusinessName("Default Restaurant");
        r.setRatingValue(3);
        r.setLatitude(53.4);
        r.setLongitude(-2);


    }

    public JSONArray getJSONRestaurants(String city) throws URISyntaxException {
        int pos = city.lastIndexOf('/') + 1;
        uri = new URI(city.substring(0, pos) + Uri.encode(city.substring(pos)));
        JSONArray ja = null;
        URL url;
        HttpURLConnection connection;
        //ArrayList<Integer> restaurants = new ArrayList<>();
        ArrayList<String>restaurants=new ArrayList<>();
        BufferedReader rd;
        InputStreamReader ins;

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();


        try {
            URL restaurantBaseURL = new URL("http://sandbox.kriswelsh.com/hygieneapi/hygiene.php");

            String fullURL = restaurantBaseURL + "?op=s_name&name=" + uri;

            String line = "";


            url = new URL(fullURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            //GET to send Data
            ins = new InputStreamReader(connection.getInputStream());
            rd = new BufferedReader(ins);


            //read the result to process response

            while ((line = rd.readLine()) != null) {
                ja = new JSONArray(line);

                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = (JSONObject) ja.get(i);

                    restaurants.add(ja.getString(i));
                }

            }

            rd.close();

        } catch (ProtocolException pe) {
        } catch (MalformedURLException mue) {
        } catch (IOException ioe) {
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ja;

    }
}


