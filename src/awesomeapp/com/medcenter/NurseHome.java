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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NurseHome extends Activity {
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_nurse_home);
		//Patient finder search box edit text w/e
		final EditText nPatientFinder = (EditText) findViewById(R.id.et_prescription);
		//Text views for the table info
		final TextView patient_id1 = (TextView) findViewById(R.id.patient_id1);
		final TextView patient_id2 = (TextView) findViewById(R.id.patient_id2);
		final TextView patient_id3 = (TextView) findViewById(R.id.patient_id3);
		final TextView patient_name1 = (TextView) findViewById(R.id.patient_name1);
		final TextView patient_name2 = (TextView) findViewById(R.id.patient_name2);
		final TextView patient_name3 = (TextView) findViewById(R.id.patient_name3);
		
		//Button declarations
		Button search = (Button) findViewById(R.id.b_ps_confirm);
		Button logout = (Button) findViewById(R.id.b_dh_logout);
		
		//Search button event handler
		search.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			int patientId = Integer.parseInt(nPatientFinder.getText().toString());

			
			String patientSearch = "http://173.78.61.249:8080/api/patient/?patient_id=" + patientId;
			String patientFound = readJSONFeed(patientSearch);
			try
			{
				JSONObject patient = new JSONObject(patientFound);
				int status = patient.getInt("status");
				if(status == 404)
				{
                	Toast.makeText(NurseHome.this, 
	                	    "That is not a valid patient ID.", Toast.LENGTH_SHORT).show();
				}
				else if(status == 302)
				{
					//Bundle the patient id entered, send it to NursePatientInfo.
					Intent shiftToPatientInfo = new Intent (v.getContext(), NursePatientInfo.class);
					startActivityForResult(shiftToPatientInfo, 0);
				}
				
			}
			catch(JSONException e){
				//oops
			}
		}
			
		});
		
		//Logout event handler
		logout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			Intent shiftToPatientInfo = new Intent (v.getContext(), MainActivity.class);
			startActivityForResult(shiftToPatientInfo, 0);
		}
			
		});
	}
}
