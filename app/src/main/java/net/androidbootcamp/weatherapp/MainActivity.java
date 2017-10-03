/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package net.androidbootcamp.weatherapp;

import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
/*
 * MainActivity class that loads MainFragment
 */
public class MainActivity extends Activity
{
    /**
     * Called when the activity is first created.
     */

   // final double LATITUDE = 42.4000;
    //final double LONGITUDE = -88.2300;
    //String txturldarksky = "https://api.darksky.net/forecast/";
    //String txturldarkskykey = "262c4fb7e2ce7befe2bc41b951dce750";

    private TextView tvData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnHit = (Button) findViewById(R.id.btnHit);
        tvData = (TextView) findViewById(R.id.tvjsonItem);

       /// String numLatitude;
        //String numLongitude;
        final EditText numLatitude = (EditText) findViewById(R.id.txtLatitude);
        final EditText numLongitude = (EditText) findViewById(R.id.txtLongitude);

        //new JSONTask().execute("https://api.darksky.net/forecast/088fa6b5249d1da73803553b4e7d8542/42.3147436,-88.44870209999999");

       btnHit.setOnClickListener(new View.OnClickListener()
       {
            @Override
            public void onClick(View view)
            {
                String strLatitude = numLatitude.getText().toString();
                String strLongitude = numLongitude.getText().toString();
                String weatherURL = "https://api.darksky.net/forecast/088fa6b5249d1da73803553b4e7d8542/"+strLatitude +","+ strLongitude;

                //tvData.setText(weatherURL);
               new JSONTask().execute("https://api.darksky.net/forecast/088fa6b5249d1da73803553b4e7d8542/"+ strLatitude + ","+ strLongitude); //42.3147436,-88.44870209999999");//"https://jsonparsingdemo-cec5b.firebaseapp.com/jsonData/moviesDemoList.txt");//'https://jsonparsingdemo-cec5b.firebaseapp.com/jsonData/moviesDemoItem.txt");
            }

       });
    }

    public class JSONTask extends AsyncTask<String, String, String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try
            {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();

                String line = "";
                while((line = reader.readLine()) != null)
                {
                    buffer.append(line);
                }

                String finalJson = buffer.toString();
                JSONObject parentObject = new JSONObject(finalJson);
                //JSONArray parentArray = parentObject.getJSONArray("currently");
                //JSONObject timeZ = parentObject.getJSONObject("timezone");
                String timeZone = parentObject.getString("timezone");
                JSONObject finalObject = parentObject.getJSONObject("currently");

                String summary = finalObject.getString("summary");

                float temp = (float)finalObject.getDouble("temperature");
                float apparentTemp = (float)finalObject.getDouble("apparentTemperature");
                float dewPoint = (float)finalObject.getDouble("dewPoint");
                float humidity = (float)finalObject.getDouble("humidity");
                float windSpeed = (float)finalObject.getDouble("windSpeed");
                float cloudCover = (float)finalObject.getDouble("cloudCover");
                int uvIndex = finalObject.getInt("uvIndex");

                return "\nThe weather forecast is :\nTime Zone: " + timeZone+ "\nSummary: " + summary + "\nTemperature: " + temp + "\nApparent Temperature: " + apparentTemp + "\nDew Point: " + dewPoint + "\nHumidity: " + humidity + "\nWind Speed: " + windSpeed + "\nCloud Cover: " + cloudCover + "\nUV Index: " + uvIndex;


                //return movieModelList;
                //return movieName + "  " + year;

                //return buffer.toString();
                //tvData.setText(buffer.toString());

            }

            catch(MalformedURLException e)
            {

                e.printStackTrace();
            }
            catch(IOException e)
            {

                e.printStackTrace();
            }

           catch(JSONException e)
            {
               e.printStackTrace();

            }


            finally
            {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return null;
        }


        @Override
        protected void onPostExecute (String result)
        {

            super.onPostExecute(result);
            tvData.setText(result);
        }



    }


}

