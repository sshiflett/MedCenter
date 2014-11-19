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
import org.w3c.dom.Text;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
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


public class DoctorHome extends Activity {
	int patientId;
	int userId;
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
	private class foundPatient extends AsyncTask<String, Void,String>{
	    @Override
	    protected String doInBackground(String... urls){
				return readJSONFeed(urls[0]);
	    	}
	    protected void onPostExecute(String result){
				try
				{
					JSONObject patient = new JSONObject(result);
					int status = patient.getInt("status");
					if(status == 404)
					{
	                	Toast.makeText(DoctorHome.this, 
		                	    "That is not a valid patient ID.", Toast.LENGTH_SHORT).show();
					}
					else if(status == 302)
					{
						//Bundle the patient id entered, send it to PatientInfoFinal.
						Intent shiftToPatientInfo = new Intent (getApplicationContext(), PatientInfoFinal.class);
						Bundle dataBundle = new Bundle();
						dataBundle.putInt("PatientId", patientId);
						dataBundle.putInt("UserId", userId);
						shiftToPatientInfo.putExtras(dataBundle);
						startActivity(shiftToPatientInfo);
					}
					
				}
				catch(JSONException e){
					//oops
				}
	    }
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_doctor_home);
		
		//Edit text Search Bar initialization
		final EditText patientFinder = (EditText) findViewById(R.id.et_input1);
		//Search button 
		Button searchPatient = (Button) findViewById(R.id.b_ps_confirm);
		searchPatient.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			patientId = Integer.parseInt(patientFinder.getText().toString());
			Bundle unBundler = getIntent().getExtras();
			userId = unBundler.getInt("UserId");
			
			String patientSearch = "http://104.131.116.247/api/patient/?patient_id=" + patientId + "&method=get-patient";
			new foundPatient().execute(patientSearch);

		}
			
		});
		
		Button logout = (Button) findViewById(R.id.b_dh_logout);
		logout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			Intent shiftToPatientInfo = new Intent (v.getContext(), MainActivity.class);
			startActivity(shiftToPatientInfo);
			finish();
		}
			
		});
	
	}
}
