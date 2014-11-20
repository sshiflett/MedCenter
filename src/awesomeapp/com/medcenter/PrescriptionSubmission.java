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

public class PrescriptionSubmission extends Activity {
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
					final TextView doctor = (TextView) findViewById(R.id.tv_doctor_name);
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
	
	private class writeRx extends AsyncTask<String, Void,String>{
	    @Override
	    protected String doInBackground(String... urls){
				return readJSONFeed(urls[0]);
	    	}
	    protected void onPostExecute(String result){
	    	try
	    	{
	    		JSONObject rxObject = new JSONObject(result);
	    		int status = rxObject.getInt("status");
	    		if(status == 201)
	    		{
	    			Toast.makeText(PrescriptionSubmission.this, 
	                	    "Prescription was made.", Toast.LENGTH_SHORT).show();
	    		}

	    		
	    	}catch(JSONException e){
	    		e.printStackTrace();
	    	}
	    	
	    }
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_prescription_submission);
		final EditText prescription = (EditText) findViewById(R.id.et_ps_prescription);
		final EditText amount = (EditText) findViewById(R.id.et_ps_prescription_amt);
		
		Bundle unBundler = getIntent().getExtras();
		patientId = unBundler.getInt("PatientId");
		userId = unBundler.getInt("UserId");
		
		String viewPatient = "http://104.131.116.247/api/patient/?patient_id=" + patientId + "&method=get-patient";
		new viewPatientInfo().execute(viewPatient);
		String viewDoctor = "http://104.131.116.247/api/user/?user_id=" + userId + "&method=get-user";
		new viewDoctorInfo().execute(viewDoctor);
		
		Button submitRx = (Button) findViewById(R.id.b_ps_confirm);
		submitRx.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String rxName = prescription.getText().toString();
				String count = amount.getText().toString();
				if (rxName.matches("") || count.matches("")) {
				    Toast.makeText(PrescriptionSubmission.this, "You did not enter a prescription name, or you did not enter count.", Toast.LENGTH_SHORT).show();
				    
				}
				else{
						String prescribe = "http://104.131.116.247/api/prescription/?patient_id=" + patientId + "&name=" + rxName + "&count=" + count + "&filled=true" + "&ready=false" + "&method=create-prescription";
						new writeRx().execute(prescribe);
				}
			
		}
			
		});
		
		
		Button next = (Button) findViewById(R.id.b_ps_cancel);
		next.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
    			Intent shiftToDoctorInfo = new Intent (v.getContext(), PatientInfoFinal.class);
    			Bundle pidBundle = new Bundle();
    			pidBundle.putInt("PatientId", patientId);
    			pidBundle.putInt("UserId", userId);
    			Toast.makeText(PrescriptionSubmission.this, "userId: " + userId + "patiedId: " +patientId, Toast.LENGTH_SHORT).show();
    			shiftToDoctorInfo.putExtras(pidBundle);
    			shiftToDoctorInfo.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    			startActivity(shiftToDoctorInfo);
		}
			
		});
	}
	
	@Override
	public void onBackPressed() {
		
	}
}
