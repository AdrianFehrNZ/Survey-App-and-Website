/**
 * An Aysnc class that accepts a URL as a string and makes a background
 * HTTP request to that URL. In the (presumed XML) response it searches
 * for any <pet> tag and collected the string within the <petName> and
 * within the <petSpecies> into a list, which it uses to populate the
 * TextView passed to its constructor
 * @see MainActivity.java
 */
package dms.restfuljavaandroidapp;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RestfulLookupTask extends AsyncTask<String, Void, String>
{
    @SuppressLint("StaticFieldLeak")
    private TextView surveyView;
    private int button_num;

    //accepts TextView and an int representing one of the three buttons
    // that access this method through the MainActivity.java
    public RestfulLookupTask(TextView surveyView, int button_num)
    {
        this.surveyView = surveyView;
        this.button_num = button_num;

    }

    //works in background when this method is called
    protected String doInBackground(String... params)
    {
        if (params.length == 0)
        {
            return "No URL provided";
        }
        try
        {
            //if not button 1 (button 1 simply accesses an unnamed GET method)
            if(this.button_num!=1){

                //add to the request url path depending on which button was pressed
                if(this.button_num == 2)
                    params[0] = params[0] + "surveyors/";

                if(this.button_num == 3)
                    params[0] = params[0] + "participants/";
                if(this.button_num == 4)
                    params[0] = params[0] + "locations/";
            }

            URL surveyUrl = new URL(params[0]);

            //establish connection
            HttpURLConnection conn
                = (HttpURLConnection) surveyUrl.openConnection();
            conn.setReadTimeout(3000); // 3000ms
            conn.setConnectTimeout(3000); // 3000ms
            conn.setRequestMethod("GET");
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK)
            {
                //get output and add to string
                BufferedReader br = new BufferedReader
                    (new InputStreamReader(conn.getInputStream()));
                StringBuilder htmlResponse = new StringBuilder();
                String line = br.readLine();
                while (line != null)
                {
                    htmlResponse.append(line);
                    line = br.readLine();
                }

                //close connections
                br.close();
                conn.disconnect();
                if (htmlResponse.length()==0)
                    return "Empty response";

                else {
                    //remove html tags
                    String htmlString = htmlResponse.toString();
                    htmlString = htmlString.replaceAll("\\<.*?\\>", "\n");
                    return htmlString;
                }
            }
            else
                return "HTTP Response code " + responseCode;
        }
        catch (MalformedURLException e)
        {
            Log.e("RestfulLookupTask", "Malformed URL: " + e);
            e.printStackTrace();
        }
        catch (IOException e)
        {
            Log.e("RestfulLookupTask", "IOException: " + e);
            e.printStackTrace();
        }
        return "Error during HTTP request to url " + params[0];
    }

    //post results to android UI
    protected void onPostExecute(String workerResult)
    {
        surveyView.setText(workerResult);
    }
}