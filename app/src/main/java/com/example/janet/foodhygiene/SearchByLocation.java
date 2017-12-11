package com.example.janet.foodhygiene;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.content.Intent;

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

public class SearchByLocation extends AppCompatActivity {

    private String city=null;
    public static double lat=0;
    public static double lon=0;
    JSONArray ja = new JSONArray();
    URL url;
    URI uri;
    ArrayList<Restaurant> restaurants = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_location);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        LocationManager locMan = (LocationManager) getApplicationContext().getSystemService(this.LOCATION_SERVICE);
        //locMan.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        try
        {
            locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            });
        }
        catch (SecurityException se) {
            System.err.println("Permissions Problem: " + se.getMessage());
            se.printStackTrace();
        }


    }

    /*public class JSONTask extends AsyncTask<URL,String,JSONArray>
    {

        @Override
        protected JSONArray doInBackground(URL... params)
        {
            ja = getJSONRestaurants(city);
            return ja;
        }

        @Override
        protected void onPostExecute(JSONArray result)
        {
            // send the ja to convert to java

            try
            {
                restaurants = fromJson(result);
                displayInTable(restaurants);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }*/

    public double getLat() { return lat; }
    public double getLon() { return lon; }




    public JSONArray getJSONRestaurants(String city) {
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


    //process Json to java objects

    public static ArrayList<Restaurant> fromJson(JSONArray jsonRestaurantList) throws JSONException {
        ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>(jsonRestaurantList.length());


        //process each result in jsonArray, decode and convert
        for (int i = 0; i < jsonRestaurantList.length(); i++) {
            JSONObject restaurantJson = null;
            try {
                restaurantJson = jsonRestaurantList.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            Restaurant restaurant = SearchByLatLong.fromJson(restaurantJson);

            if (restaurant != null) {
                restaurants.add(restaurant);
            }
        }
        return restaurants;
    }



    public static Restaurant fromJson(JSONObject json) {
        //add each restaurant to java object
        Restaurant review;
        try {

            review = new Restaurant(
                    json.getInt("id"),json.getString("BusinessName"),json.getString("AddressLine1"),
                    json.getString("AddressLine2"),json.getString("AddressLine3"), json.getString("PostCode"),
                    json.getInt("RatingValue"), json.getString("RatingDate"), json.getDouble("Latitude"),
                    json.getDouble("Longitude"));



        }
        catch (JSONException e)
        {
            e.printStackTrace();
            return null;
        }
        return review;
    }


    //Method to display the output in the table Layout
    public void displayInTable(ArrayList<Restaurant> reviews) {
        //1. Locate TableLayout in the resources
        TableLayout tl = (TableLayout) findViewById(R.id.layoutTable);

        tl.removeAllViews();
        //3. Iterate over all restaurants
        for (int i = 0; i < reviews.size(); i++) {


            //get each restaurant out of reviews
            Restaurant oneReview = reviews.get(i);

            //extract each component of a review from this object
            int id = oneReview.getId();
            String businessName = oneReview.getBusinessName();
            String addressLine1 = oneReview.getAddressLine1();
            String addressLine2 = oneReview.getAddressLine2();
            String addressLine3 = oneReview.getAddressLine3();
            String postcode = oneReview.getPostCode();
            int ratingValue = oneReview.getRatingValue();
            String ratingDate = oneReview.getRatingDate();
            Double latitude = oneReview.getLatitude();
            Double longitude = oneReview.getLongitude();

            //creating imageView for the rating value picture
            ImageView iv1 = new ImageView(this);



            //create the individual text views from Restaurant components and add each to the TableRow
            TextView tvBusinessName = new TextView(this);
            TextView tvAddressLine1 = new TextView(this);
            TextView tvAddressLine2 = new TextView(this);
            TextView tvAddressLine3 = new TextView(this);
            TextView tvPostCode = new TextView(this);
            TextView tvRatingValue = new TextView(this);


            //create the entries that are need in the table row
            tvBusinessName.setTextColor(Color.BLACK);
            tvAddressLine1.setTextColor(Color.DKGRAY);
            tvAddressLine2.setTextColor(Color.DKGRAY);
            tvAddressLine3.setTextColor(Color.DKGRAY);
            tvPostCode.setTextColor(Color.DKGRAY);
            tvRatingValue.setTextColor(Color.DKGRAY);



            tvBusinessName.setText(businessName);
            tvAddressLine1.setText(addressLine1);
            tvAddressLine2.setText(addressLine2);
            tvAddressLine3.setText(addressLine3);
            tvPostCode.setText(postcode);

            //add imageView to display rating value pictures depends on the Restaurants Food Hygiene Rating
            if (ratingValue == -1) {
                iv1.setImageResource(R.drawable.exempt);
            } else if (ratingValue == 0) {
                iv1.setImageResource(R.drawable.rate0);
            } else if (ratingValue == 1) {
                iv1.setImageResource(R.drawable.rate1);
            } else if (ratingValue == 2) {
                iv1.setImageResource(R.drawable.rate2);
            } else if (ratingValue == 3) {
                iv1.setImageResource(R.drawable.rate3);
            } else if (ratingValue == 4) {
                iv1.setImageResource(R.drawable.rate4);
            } else if (ratingValue == 5) {
                iv1.setImageResource(R.drawable.rate5);
            }


            TableRow tr1 = new TableRow(this);

            //add views to the row
            tr1.addView(tvBusinessName);
            tr1.addView(tvAddressLine1);
            tr1.addView(tvAddressLine2);
            tr1.addView(tvAddressLine3);
            tr1.addView(tvPostCode);
            tr1.addView(iv1);

            //add row to the table
            tl.addView(tr1);

        }
    }

    //By clicking on the update button table should display all restaurant details
    public void cmdUpdateLoc(View v) throws URISyntaxException {


        EditText tvCity= (EditText) findViewById(R.id.editTextCity);
        city=tvCity.getText().toString();
        int pos = city.lastIndexOf('/') + 1;
        uri = new URI(city.substring(0, pos) + Uri.encode(city.substring(pos)));



        JSONArray jsonRestaurantList = null;
        jsonRestaurantList = getJSONRestaurants(city);

        //process json to java Object
        ArrayList<Restaurant> restaurants = new ArrayList<>();
        try {
            restaurants = SearchByLatLong.fromJson(jsonRestaurantList);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        //calling displayInTable Method
        displayInTable(restaurants);

    }

    public void finishActivity(View v)
    {
        finish();
    }


    public void searchMap(View view) throws URISyntaxException {


            EditText tvCity= (EditText) findViewById(R.id.editTextCity);
            city=tvCity.getText().toString();
            int pos = city.lastIndexOf('/') + 1;
            uri = new URI(city.substring(0, pos) + Uri.encode(city.substring(pos)));



           /* JSONArray jsonRestaurantList = null;
            jsonRestaurantList = getJSONRestaurants(city);

            //process json to java Object
            ArrayList<Restaurant> restaurants = new ArrayList<>();
            try {
                restaurants = SearchByLatLong.fromJson(jsonRestaurantList);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }*/

        Intent intent = new Intent(SearchByLocation.this, FHGoogleMap.class);
        intent.putExtra("city", city.toString());
        //intent.putExtra("jsonArray", restaurants.toString());
        startActivity(intent);
    }
}

