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


public class AddPatient extends Activity {
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
	
	private class createPatient extends AsyncTask<String, Void,String>{
	    @Override
	    protected String doInBackground(String... urls){
				return readJSONFeed(urls[0]);
	    	}
	    protected void onPostExecute(String result){
	    	try
	    	{
	    		
	    		JSONObject noteObject = new JSONObject(result);
	    		
	    		int status = noteObject.getInt("status");
	    		if(status == 201)
	    		{
	    			Toast.makeText(AddPatient.this, 
		               	    "Patient was created.", Toast.LENGTH_SHORT).show();
	    		}
	    		
	    		else if(status == 206)
	    		{
	    			Toast.makeText(AddPatient.this, 
		               	    "Partial Content.", Toast.LENGTH_SHORT).show();
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
		setContentView(R.layout.activity_add_patient);
		Bundle unBundler = getIntent().getExtras();
		userId = unBundler.getInt("UserId");
		
		final EditText patientFirstName = (EditText) findViewById(R.id.et_first_name);
		final EditText patientLastName = (EditText) findViewById(R.id.et_last_name);
		final EditText patientPhone = (EditText) findViewById(R.id.et_phone_number);
		final EditText patientDOB = (EditText) findViewById(R.id.et_birthday);
		final EditText patientBlood = (EditText) findViewById(R.id.et_blood_type);
		final EditText patientUser = (EditText) findViewById(R.id.et_user_name);
		final EditText patientPass = (EditText) findViewById(R.id.et_password);
		final EditText patientReason = (EditText) findViewById(R.id.et_reason);
		
		Button submit = (Button) findViewById(R.id.b_submit);
		submit.setOnClickListener(new View.OnClickListener(){
		@Override
		public void onClick(View b) {
			String firstName = patientFirstName.getText().toString();
			String lastName = patientLastName.getText().toString();
			String phone = patientPhone.getText().toString();
			String birthday = patientDOB.getText().toString();
			String blood = patientBlood.getText().toString();
			String username = patientUser.getText().toString();
			String password = patientPass.getText().toString();
			String reason =  patientReason.getText().toString();
			if (firstName.matches("") || lastName.matches("") || phone.matches("") || birthday.matches("") || blood.matches("") || username.matches("") || password.matches("") || reason.matches("")) {
			    Toast.makeText(AddPatient.this, "Blank field, try again.", Toast.LENGTH_SHORT).show();
			    
			}
			else{
				String createPatient = "http://104.131.116.247/api/patient/?first_name=" + firstName + "&last_name=" + lastName + "&date_of_birth=" + birthday + "&blood_type=" + blood + "&phone=" + phone + "&heart_rate=0&breathing_rate=0&respiration_rate=0&reason=" + reason + "&admitted=true&username=" + username + "&password=" + password + "&method=create-patient";
				new createPatient().execute(createPatient);
			}
			
			}
			
		});
		
		Button cancel = (Button) findViewById(R.id.b_cancel);
		cancel.setOnClickListener(new View.OnClickListener(){
		@Override
		public void onClick(View b) {
			//Intent shiftToNurseHome = new Intent (b.getContext(), NurseHome.class);
			//startActivity(shiftToNurseHome);
			Intent shiftToNurseHome = new Intent (b.getContext(), NurseHome.class);
			Bundle pidBundle = new Bundle();
			pidBundle.putInt("UserId", userId);
			shiftToNurseHome.putExtras(pidBundle);
			shiftToNurseHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(shiftToNurseHome);
		}
		
			
		});


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_patient, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		
	}
}
