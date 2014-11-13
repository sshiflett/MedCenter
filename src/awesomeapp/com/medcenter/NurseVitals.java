package awesomeapp.com.medcenter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class NurseVitals extends Activity {
	public String readJSONFeed(String URL) {

		StringBuilder stringBuilder = new StringBuilder();

		HttpClient client = new DefaultHttpClient();

		HttpGet httpGet = new HttpGet(URL);

		try {

			HttpResponse response = client.execute(httpGet);

			StatusLine statusLine = response.getStatusLine();

			int statusCode = statusLine.getStatusCode();

			if (statusCode == 200) {

				HttpEntity entity = response.getEntity();

				InputStream content = entity.getContent();

				BufferedReader reader = new BufferedReader(

				new InputStreamReader(content));

				String line;

				while ((line = reader.readLine()) != null) {

					stringBuilder.append(line);

				}

			} else {

				Log.e("JSON", "Failed to download file");

			}

		} catch (ClientProtocolException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}

		return stringBuilder.toString();

	}
/* Comment to test commit */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_patient_vitals_final);
		
		// Grabs the patient name and displays.
		
		final TextView patientName = (TextView) findViewById(R.id.tv_patientName);
		//Need to unbundle the patient id, set it to following variable and use it for database lookup.
				int ubPatientId = 0;
				
				
				String viewPatient = "http://173.78.61.249:8080/api/patient/?patient_id=" + ubPatientId;
				String patientViewer = readJSONFeed(viewPatient);
				
		        try{
			        	JSONObject patientObject = new JSONObject(patientViewer);
			        	int status = patientObject.getInt("status");
			        
						if(status == 404)
						{
							// Managed to reach the patient info screen with an invalid patient id
							// More likely: An invalid patient id was bundled and recieved on this screen.
				        	Toast.makeText(NurseVitals.this, 
				            	    "Something has gone terribly wrong.", Toast.LENGTH_SHORT).show();
						}
						else if(status == 302)
						{
								// Set textfields to patient info from patientObject.
								String patientFirstName = patientObject.getString("first_name");
								String patientLastName = patientObject.getString("first_name");
								String fullPatientName = patientFirstName + " " + patientLastName;
								patientName.setText(fullPatientName);								
						}
		        }
		        catch(JSONException e){
		        	//oops
		        }
		        
		       
		
		Button next = (Button) findViewById(R.id.b_dh_logout);
		next.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			Intent shiftToPatientInfo = new Intent (v.getContext(), NursePatientInfo.class);
			startActivityForResult(shiftToPatientInfo, 0);
			}
		});
	}
}
