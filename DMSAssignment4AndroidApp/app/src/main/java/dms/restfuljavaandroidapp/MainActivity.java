/**
 * Note this requires the SurveyRestfulService Java EE application from
 * to be deployed (eg to localhost
 * in which case the Android emulator uses IP address 10.0.2.2 for it)
 * @author Adrian Fehr
 */
package dms.restfuljavaandroidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //method to change the UI view to the Add Participant Page when the Add Participant button is tapped
    public void addParticipant(View v)
    {
        startActivity(new Intent(MainActivity.this, AddParticipantPage.class));
    }

    //called when the user taps Results, Surveyors, or Participants buttons
    public void lookupSurvey(View v)
    {
        TextView surveyView = (TextView) findViewById(R.id.survey_view);
        surveyView.setMovementMethod(new ScrollingMovementMethod()); //make the output scrollable

        //add a slash if the editable textfield containing the URL is missing one
        EditText editUrl = (EditText) findViewById(R.id.url_edit);
        String surveyUrl = editUrl.getText().toString();
        if (!surveyUrl.endsWith("/"))
            surveyUrl = surveyUrl + "/";

        //switch by button value
        //then create new RestfulLookupTask and call its doInBackground method
        switch(v.getId()) {
            case R.id.lookup_results:

                RestfulLookupTask task = new RestfulLookupTask(surveyView, 1);
                task.execute(surveyUrl);

                break;
            case R.id.lookup_surveyors:

                task = new RestfulLookupTask(surveyView, 2);
                task.execute(surveyUrl);

                break;
            case R.id.lookup_participants:

                task = new RestfulLookupTask(surveyView, 3);
                task.execute(surveyUrl);

                break;
            case R.id.location_button:

                task = new RestfulLookupTask(surveyView, 4);
                task.execute(surveyUrl);

                break;
        }
    }
}
