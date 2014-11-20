package awesomeapp.com.medcenter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class Add_note extends Activity {
	int patientId;
	String note;
	int userId;
	int role;

	
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
	private class parseRole extends AsyncTask<String, Void,String>{
	    @Override
	    protected String doInBackground(String... urls){
				return readJSONFeed(urls[0]);
	    	}
	    protected void onPostExecute(String httpResponse){
	    	try
	    	{
	    		JSONObject user = new JSONObject(httpResponse);
          		JSONObject userInfo = user.getJSONObject("user");
     			role = userInfo.getInt("role");
		
	    	}catch(JSONException e){
	    		e.printStackTrace();
	    	}
	    }
}
	
	private class createNote extends AsyncTask<String, Void,String>{
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
	    			Toast.makeText(Add_note.this, 
		               	    "Your note was created.", Toast.LENGTH_SHORT).show();
	    		}
	    		
	    		else if(status == 404)
	    		{
	    			Toast.makeText(Add_note.this, 
		               	    "Not found.", Toast.LENGTH_SHORT).show();
	    		}
	    		
	    	}catch(JSONException e){
	    		e.printStackTrace();
	    	}
	    	
	    }
	}
	
	private class getPatientName extends AsyncTask<String, Void,String>{
	    @Override
	    protected String doInBackground(String... urls){
				return readJSONFeed(urls[0]);
	    	}
	    protected void onPostExecute(String result){
				try
				{
					JSONObject patient = new JSONObject(result);
					TextView patientName = (TextView) findViewById(R.id.tv_patientName);
					int status = patient.getInt("status");
					if(status == 404)
					{
	                	Toast.makeText(Add_note.this, 
		                	    "Something went wrong.", Toast.LENGTH_SHORT).show();
					}
					else if(status == 302)
					{
						JSONObject patientInfo = patient.getJSONObject("user");
						String patientFirstName = patientInfo.getString("first_name");
						String patientLastName = patientInfo.getString("last_name");
						String fullPatientName = patientFirstName + " " + patientLastName;
						patientName.setText(fullPatientName);

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
		setContentView(R.layout.activity_add_note);
		Bundle unBundler = getIntent().getExtras();
		patientId = unBundler.getInt("PatientId");
		userId = unBundler.getInt("UserId");
		String userLookup = "http://104.131.116.247/api/user/?user_id=" +  userId +"&method=get-user";
		new parseRole().execute(userLookup);
		
		
		String viewPatient = "http://104.131.116.247/api/patient/?patient_id=" + patientId + "&method=get-patient";
		new getPatientName().execute(viewPatient);
		final EditText noteEntered = (EditText) findViewById(R.id.editText1);
	

		

		
		
		
		Button submit = (Button) findViewById(R.id.b_ps_submitNote);
		submit.setOnClickListener(new View.OnClickListener(){
		@Override
		public void onClick(View b) {
			note = noteEntered.getText().toString();
			if (note.matches("")) {
			    Toast.makeText(Add_note.this, "Blank note, try again.", Toast.LENGTH_SHORT).show();
			    
			}
			else{
				String finalNote = note.replaceAll(" ", "%20");
				String createThisNote = "http://104.131.116.247/api/note/?patient_id=" + patientId + "&author_id=" + userId +"&title=Thisisanexampletitle." +"&note=" + finalNote + "&method=create-note";
				new createNote().execute(createThisNote);
			}
			
			}
			
		});
		
		 Button closeActivity= (Button) findViewById(R.id.b_ps_cancel);
	        if (closeActivity != null)
	        {
	        	closeActivity.setOnClickListener(new OnClickListener() 
	        	{
	        		public void onClick(View v) {
	        			if(role == 1){
	        			Intent shiftToDoctorInfo = new Intent (v.getContext(), PatientInfoFinal.class);
	        			Bundle pidBundle = new Bundle();
	        			pidBundle.putInt("PatientId", patientId);
	        			pidBundle.putInt("UserId", userId);
	        			shiftToDoctorInfo.putExtras(pidBundle);
	        			shiftToDoctorInfo.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        			startActivity(shiftToDoctorInfo);
	        			}
	        			else if(role == 2){
	        			Intent shiftToNurseInfo = new Intent (v.getContext(), NursePatientInfo.class);
	        			Bundle pidBundle = new Bundle();
	        			pidBundle.putInt("PatientId", patientId);
	        			pidBundle.putInt("UserId", userId);
	        			shiftToNurseInfo.putExtras(pidBundle);
	        			shiftToNurseInfo.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        			startActivity(shiftToNurseInfo);
	        			}
	        			else
	        			{
	        				finish();
	        			}
	        			

	        		}
	        	});
	        }     
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_note, menu);
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

