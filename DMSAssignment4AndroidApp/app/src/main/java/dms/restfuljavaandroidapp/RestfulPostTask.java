/**
 * An Aysnc class that accepts two strings and makes a background
 * HTTP POST request to a URL to create a new participant in the EJB.
 * @see MainActivity.java
 */
package dms.restfuljavaandroidapp;

import android.os.AsyncTask;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

class RestfulPostTask extends AsyncTask<String, Void, String> {
    public static final String TAG = "HTTP POST Request";
    private String name, surveyor, location;

    public RestfulPostTask(String name, String surveyor, String location){
        this.name = name;
        this.surveyor = surveyor;
        this.location = location;
    }

    public String doInBackground(String... IO) {

        // Predefine variables
        String io = "participant=" + this.name + "&surveyor=" + this.surveyor +"&location=" + this.location;
        URL url;

        try {
            //hardcoded URL for my RESTful Web Service
            url = new URL("http://10.0.2.2:8080/DMSAssignment3/surveyservice/survey/");

            // Open a connection using HttpURLConnection
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setReadTimeout(4000);
            con.setConnectTimeout(4000);
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setInstanceFollowRedirects(false);
            con.setRequestMethod("POST");
            con.setFixedLengthStreamingMode(io.getBytes().length);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // Send
            PrintWriter out = new PrintWriter(con.getOutputStream());
            out.print(io);
            out.close();

            con.connect();

            BufferedReader in = null;
            if (con.getResponseCode() != 200) {
                in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                Log.d(TAG, "!=200: " + in);
            } else {
                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                Log.d(TAG, "POST request send successful: " + in);
            }

        } catch (Exception e) {
            Log.d(TAG, "Exception" + io);
            e.printStackTrace();
            return null;
        }

        return null;
    }
}


