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
import org.json.JSONArray;
import org.json.JSONObject;

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
import android.widget.TextView;
import android.widget.Toast;



public class PatientMain extends Activity {
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
	
	private class getPatientInfo extends AsyncTask<String, Void,String>{
	    @Override
	    protected String doInBackground(String... urls){
				return readJSONFeed(urls[0]);
	    	}
	    protected void onPostExecute(String result){
	    	try
	    	{
		        	JSONObject patientObject = new JSONObject(result);
		        	int status = patientObject.getInt("status");
		    		final TextView heartRate = (TextView) findViewById(R.id.selected_patient_heartrate);
		    		final TextView rr = (TextView) findViewById(R.id.selected_patient_rr);
		    		final TextView patientName = (TextView) findViewById(R.id.tv_patientName);
		        
					if(status == 404)
					{
						// Managed to reach the patient info screen with an invalid patient id
						// More likely: An invalid patient id was bundled and recieved on this screen.
			        	Toast.makeText(PatientMain.this, 
			            	    "Something has gone terribly wrong.", Toast.LENGTH_SHORT).show();
					}
					else if(status == 302)
					{
							// Set textfields to patient info from patientObject.
							JSONObject patientInfo = patientObject.getJSONObject("user");
							String patientFirstName = patientInfo.getString("first_name");
							String patientLastName = patientInfo.getString("last_name");
							String fullPatientName = patientFirstName + " " + patientLastName;
							patientName.setText(fullPatientName);
							int patientHeartRate = patientInfo.getInt("heart_rate");
							heartRate.setText(String.valueOf(patientHeartRate));
							int patientRR = patientInfo.getInt("respiration_rate");
							rr.setText(String.valueOf(patientRR));								
					}
	

	    		
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}
	    }
	}
	
	private class viewNotes extends AsyncTask<String, Void,String>{
	    @Override
	    protected String doInBackground(String... urls){
				return readJSONFeed(urls[0]);
	    	}
	    protected void onPostExecute(String result){
	    	try
	    	{
	    		
	    			JSONObject noteObject = new JSONObject(result);

	    			//---print out the content of the json feed---


	    				int status = noteObject.getInt("status");
	    				if(status == 302){
	    					JSONArray noteArray = noteObject.getJSONArray("notes");
	    					
	    					for (int i = 0; i < noteArray.length(); i++) {
	    						
	    						JSONObject notesObject = noteArray.getJSONObject(i);
	    						Toast.makeText(getBaseContext(),"Author: " + notesObject.getString("author") +
	    						" - Note: " + notesObject.getString("note"),
	 
	    						Toast.LENGTH_LONG).show();
	    				}
	    			}
	    		
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}
	    }
	}
	
	private class viewPrescriptions extends AsyncTask<String, Void,String>{
	    @Override
	    protected String doInBackground(String... urls){
				return readJSONFeed(urls[0]);
	    	}
	    protected void onPostExecute(String result){
	    	try
	    	{

	    				JSONObject rxObject = new JSONObject(result);
	    				int status = rxObject.getInt("status");
	    				
	    				if(status == 302){
	    					JSONArray rxFeed = rxObject.getJSONArray("prescriptions");
	    					for (int i = 0; i < rxFeed.length(); i++) {
	    						JSONObject prescription = rxFeed.getJSONObject(i);
	    						boolean filled = prescription.getBoolean("filled");
	    						if(filled == true){
	    							Toast.makeText(getBaseContext(),"Prescription: " + prescription.getString("name") + " " +
	    						" - Count: " + prescription.getInt("count"),
	 
	    						Toast.LENGTH_LONG).show();
	    				}
	    				}
	    			}

	    		
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}
	    }
	}
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_patient_main);
		

		
		//Need to unbundle the patient id, set it to following variable and use it for database lookup.
		Bundle unBundler = getIntent().getExtras();
		final int ubPatientId = unBundler.getInt("Patient_Id");
		
		
		String viewPatient = "http://104.131.116.247/api/patient/?patient_id=" + ubPatientId + "&method=get-patient";
		new getPatientInfo().execute(viewPatient);
		
		Button viewNote = (Button) findViewById(R.id.b_pv_viewNote);
		viewNote.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				String viewPatientNotes = "http://104.131.116.247/api/note/?patient_id=" + ubPatientId + "&method=get-note";
				new viewNotes().execute(viewPatientNotes);	
			}
		});

		/*
		 * 		Button viewPrescriptions = (Button) findViewById(R.id.b_pv_viewNote);
				viewPrescriptions.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View b) {
					String viewPrescription = "http://104.131.116.247/api/prescription/?patient_id="+ ubPatientId;
					new viewPrescriptions().execute(viewPrescription);
				
			}
			
		}); 
		*/
        
		Button cancel = (Button) findViewById(R.id.b_ps_cancel);
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			//Intent shiftToHomeScreen = new Intent (v.getContext(), MainActivity.class);
			//startActivity(shiftToHomeScreen);
			finish();
			}
			
		});
		
		
}
	
	@Override
	public void onBackPressed() {
		
	}
}
