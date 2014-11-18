package awesomeapp.com.medcenter;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;    
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;


public class MainActivity extends Activity {
	
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
	
	private class Authenticate extends AsyncTask<String, Void,String>{
	    @Override
	    protected String doInBackground(String... urls){
				return readJSONFeed(urls[0]);
	    	}
	    protected void onPostExecute(String result){
	    	try
	    	{
	    		JSONObject loginObject = new JSONObject(result);
	    		
	            int status = loginObject.getInt("status");  
	            // If authenticate status returns "forbidden", show error message using toast.
	       	 	if(status == 403)
	       	 	{
	               	Toast.makeText(MainActivity.this, 
	               	    "Your login was incorrect. Please try again.", Toast.LENGTH_SHORT).show();
	            }
	       	 	else if(status == 202)
	       	 	{
	       	 		JSONObject userObject = loginObject.getJSONObject("user");
	       	 		int userId = userObject.getInt("id");
	       	 		String userLookup = "http://104.131.116.247/api/user/?user_id="+userId;
	       	 		new parseRole().execute(userLookup);	
	       	 	}
	    		
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}
	    }
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
     			int role_id = userInfo.getInt("role");
     			int patient_id = userInfo.getInt("patient_id");
				Intent shiftToDoctorHome = new Intent (MainActivity.this, DoctorHome.class);
				Intent shiftToPharm = new Intent (MainActivity.this, PharmacistMainFinal.class);
				Intent shiftToNurseHome = new Intent (MainActivity.this, NurseHome.class);
				Intent shiftToPatientHome = new Intent (MainActivity.this, PatientInfoFinal.class);
				Bundle idBundle = new Bundle();
				idBundle.putInt("Patient_Id", patient_id);
				shiftToPatientHome.putExtras(idBundle);
				
				if(role_id == 1){
					startActivity(shiftToDoctorHome);
				}	
				else if(role_id == 2){
					startActivity(shiftToNurseHome);
				}
				else if(role_id == 3){
					startActivity(shiftToPharm);
				}
				else if (role_id == 4){
					startActivity(shiftToPatientHome);
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
		setContentView(R.layout.activity_main);
		
		Button next = (Button) findViewById(R.id.b_ps_confirm);
		next.setOnClickListener(new View.OnClickListener() {
	
				@Override
				public void onClick(View v) {
		        	// Get entered text from EditText boxes.
					EditText username = (EditText) findViewById(R.id.et_user_name);
					EditText password = (EditText) findViewById(R.id.et_input2);
					
		           	String userName = username.getText().toString();
		        	String passWord = password.getText().toString();
		        	
		        	String httpRequest = "http://104.131.116.247/api/authenticate/?username=" + userName + "&password=" + passWord;
		        	new Authenticate().execute(httpRequest);
		        	
				}
			
		});	
}}
