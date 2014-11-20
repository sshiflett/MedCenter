package awesomeapp.com.medcenter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class FillPrescript extends Activity {
	public int patientId;
	int userId;
	Spinner rxList;
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
	private class viewPatientInfo extends AsyncTask<String, Void,String>{
	    @Override
	    protected String doInBackground(String... urls){
				return readJSONFeed(urls[0]);
	    	}
	    protected void onPostExecute(String result){
			try
				{
					final TextView patient = (TextView) findViewById(R.id.selected_patient_name);
		        	JSONObject patientObject = new JSONObject(result);
		        	int status = patientObject.getInt("status");
		        	if(status == 302){
		        		JSONObject patientInfo = patientObject.getJSONObject("user");
		        		String patientFirstName = patientInfo.getString("first_name");
		        		String patientLastName = patientInfo.getString("last_name");
		        		String fullPatientName = patientFirstName + " " + patientLastName;
		        		patient.setText(fullPatientName);
		        	}
	
				}
				catch(JSONException e){
					//oops
				}
	    	}
	    }
	
	private class viewDoctorInfo extends AsyncTask<String, Void,String>{
	    @Override
	    protected String doInBackground(String... urls){
				return readJSONFeed(urls[0]);
	    	}
	    protected void onPostExecute(String result){
			try
				{
					final TextView doctor = (TextView) findViewById(R.id.patient_doctor);
		        	JSONObject doctorObject = new JSONObject(result);
		        	int status = doctorObject.getInt("status");
		        	if(status == 302){
		        		JSONObject doctorInfo = doctorObject.getJSONObject("user");
		        		String doctorFirstName = doctorInfo.getString("first_name");
		        		String doctorLastName = doctorInfo.getString("last_name");
		        		String fullDoctorName = doctorFirstName + " " + doctorLastName;
		        		doctor.setText(fullDoctorName);
		        	}
	
				}
				catch(JSONException e){
					//oops
				}
	    	}
	    }
	
	private class showPrescriptions extends AsyncTask<String, Void,String>{
	    @Override
	    protected String doInBackground(String... urls){
				return readJSONFeed(urls[0]);
	    	}
	    protected void onPostExecute(String result){
				try
				{
					//Spinner spinner = (Spinner) findViewById(R.id.spinner1);
					rxList = (Spinner) findViewById(R.id.spinner1);
					JSONObject showRx = new JSONObject(result);
	    			int status = showRx.getInt("status");
    				ArrayList<String> prescriptions =new ArrayList<String>();
	    			if(status == 302){
	    				JSONArray rxArray = showRx.getJSONArray("prescriptions");
	    				for (int i = 0; i < rxArray.length(); i++) {
	    				JSONObject rxObject = rxArray.getJSONObject(i);
	    				boolean filled = rxObject.getBoolean("filled");
	    				if(filled == true){
	    				int rxId = rxObject.getInt("id");
	    				String psName = rxObject.getString("name");
	    				//int rxCount = rxObject.getInt("count");
	    				String patientPs = rxObject.getString("patient");
	    				String fullRx = "Id: " + rxId + " Name: " + psName + " For: " + patientPs;
	    				prescriptions.add(fullRx);
	    				}
	    				}
	    				 ArrayAdapter<String> adapter = new ArrayAdapter<String>(FillPrescript.this,android.R.layout.simple_spinner_item, prescriptions);
	    				 rxList.setAdapter(adapter);
	    				 rxList.setOnItemSelectedListener(new OnItemSelectedListener() {

	    						@Override
	    						public void onItemSelected(AdapterView<?> parent, View view,
	    								int position, long id) {

	    							String selected = parent.getItemAtPosition(position).toString();
	    							String intValue = selected.replaceAll("[^0-9]", "");
	    							int rxId = Integer.parseInt(intValue);
	    							Toast.makeText(FillPrescript.this, 
	    				            	    "The id of the prescription selected is " + rxId, Toast.LENGTH_SHORT).show();
	    							
	    						}

	    						@Override
	    						public void onNothingSelected(AdapterView<?> arg0) {

	    						}
	    					});

	    			}
					

					
				}
				catch(JSONException e){
					//oops
				}
	    }
	}
	
	private class rejectPrescription extends AsyncTask<String, Void,String>{
	    @Override
	    protected String doInBackground(String... urls){
				return readJSONFeed(urls[0]);
	    	}
	    protected void onPostExecute(String result){
			try
				{
					JSONObject rxInfo = new JSONObject(result);
					int status = rxInfo.getInt("status");
					if(status == 201)
					{
						Toast.makeText(FillPrescript.this, 
			            	    "The prescription was rejected.", Toast.LENGTH_SHORT).show();
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
		
		setContentView(R.layout.activity_fill_prescript);;
		final EditText rejectedId = (EditText) findViewById(R.id.editText1);
		Bundle unBundler = getIntent().getExtras();
		patientId = unBundler.getInt("PatientId");
		userId = unBundler.getInt("UserId");
		String populateRx = "http://104.131.116.247/api/prescription/?patient_id=" + patientId + "&method=get-prescription";
		new showPrescriptions().execute(populateRx);
		
		//INITIALIZE BUTTONS (to names as they appear in the screen)

		//TEXT VIEWS (To names that correspond to their labels)
		

		String viewPatient = "http://104.131.116.247/api/patient/?patient_id=" + patientId + "&method=get-patient";
		new viewPatientInfo().execute(viewPatient);
		String viewDoctor = "http://104.131.116.247/api/user/?user_id=" + userId + "&method=get-user";
		new viewDoctorInfo().execute(viewDoctor);
		


	
		

		
		//REJECT HANDLER
		Button reject = (Button) findViewById(R.id.b_reject);
	    reject.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				String rxRejectedId = rejectedId.getText().toString();
				if (rxRejectedId.matches("")) {
				    Toast.makeText(FillPrescript.this, "You did not enter a prescription id, try again.", Toast.LENGTH_SHORT).show();
				    
				}
				else{
					int rejectedRx = Integer.parseInt(rxRejectedId);
					String rejected = "http://104.131.116.247/api/prescription/?prescription_id=" + rejectedRx + "&ready=true&filled=false&method=edit-prescription";
					new rejectPrescription().execute(rejected);
				}
					
			}
		});
		
	    //CANCEL HANDLER
	    Button cancel = (Button) findViewById(R.id.b_cancel);
		cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			//Intent shiftToPharmHome = new Intent (v.getContext(), PharmacistMainFinal.class);
			//startActivity(shiftToPharmHome);
			finish();
				
			}
		});
	
	}
	
	@Override
	public void onBackPressed() {
		
	}
}
