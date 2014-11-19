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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class Add_note extends Activity {
	int patientId;
	int userId;
	String note;
	
	public String postData(String URL) {
	    // Create a new HttpClient and Post Header
		StringBuilder stringBuilder = new StringBuilder();
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(URL);

	    try {
	        // Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("patient_id", String.valueOf(patientId)));
	        nameValuePairs.add(new BasicNameValuePair("author_id", String.valueOf(userId)));
	        nameValuePairs.add(new BasicNameValuePair("title", "we dont use titles"));
	        nameValuePairs.add(new BasicNameValuePair("note", note));
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	        HttpEntity entity = response.getEntity();
	        
			InputStream content = entity.getContent();

			BufferedReader reader = new BufferedReader(

					new InputStreamReader(content));

			String line;

			while ((line = reader.readLine()) != null) {

				stringBuilder.append(line);

			}
	        
	    } catch (ClientProtocolException e) {
	        // TODO Auto-generated catch block
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	    }
	    
	    return stringBuilder.toString();
	}
	
	private class createNote extends AsyncTask<String, Void,String>{
	    @Override
	    protected String doInBackground(String... urls){
				return postData(urls[0]);
	    	}
	    protected void onPostExecute(String result){
	    	try
	    	{
	    		
	    		JSONObject noteObject = new JSONObject(result);
	    		
	    		int status = noteObject.getInt("status");
	    		if(status == 202)
	    		{
	    			Toast.makeText(Add_note.this, 
		               	    "Your note was created.", Toast.LENGTH_SHORT).show();
	    		}
	    		else if(status == 206)
	    		{
	    			Toast.makeText(Add_note.this, 
		               	    "Partial Content.", Toast.LENGTH_SHORT).show();
	    		}
	    		/*
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
	       	 		patientId = userObject.getInt("patient_id");
	       	 		new parseRole().execute(userLookup);	
	       	 	}*/
	    		
	    	}catch(JSONException e){
	    		e.printStackTrace();
	    	}
	    	
	    }
	}
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
		final EditText noteEntered = (EditText) findViewById(R.id.editText1);
		
		Bundle unBundler = getIntent().getExtras();
		patientId = unBundler.getInt("PatientId");
		userId = unBundler.getInt("UserId");
		String viewPatient = "http://104.131.116.247/api/patient/?patient_id=" + patientId;
		new getPatientName().execute(viewPatient);

		

		
		
		
		Button submit = (Button) findViewById(R.id.b_ps_submitNote);
		submit.setOnClickListener(new View.OnClickListener(){
		@Override
		public void onClick(View b) {
			note = noteEntered.getText().toString();
			new createNote().execute("http://104.131.116.247/api/note/");
			
			}
			
		});
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
}
