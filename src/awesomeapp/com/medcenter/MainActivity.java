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
	
	public String readJSONFeed(String URL)throws URISyntaxException, ClientProtocolException, IOException {

		   HttpClient client = new DefaultHttpClient();
           HttpGet request = new HttpGet();
  
           request.setURI(new URI(URL));
           HttpResponse response = client.execute(request);
          
           
           BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
           StringBuffer sb = new StringBuffer("");
           String line = "";
           String NL = System.getProperty("line.separator");
           while ((line = in.readLine()) != null) 
           {
               sb.append(line + NL);
           }
           in.close();
     
           return sb.toString();
	}
	



	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		final EditText username = (EditText) findViewById(R.id.et_user_name);
		final EditText password = (EditText) findViewById(R.id.et_input2);

	
		Button next = (Button) findViewById(R.id.b_ps_confirm);
		next.setOnClickListener(new View.OnClickListener() {
	
				@Override
				public void onClick(View v) {
		        	// Get entered text from EditText boxes.

					
		           	String userName = username.getText().toString();
		        	String passWord = password.getText().toString();
		        	
		        	JSONObject loginObject;
		        	JSONTokener theTokener;
		        	
		        try{
		            String httpRequest = "http://173.78.61.249:8080/api/authenticate/?username=" + userName + "&password=" + passWord;
		            String httpResponse = readJSONFeed(httpRequest);
		            theTokener = new JSONTokener(httpResponse);
		            
		        	
		        	// Use "authenticate" function, use response.
		             
		             loginObject = (JSONObject)theTokener.nextValue();
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
		        	 		// Send a request for user info using id returned from authenticate
		             		String userLookup = "http://173.78.61.249:8080/api/user/?user_id="+userId;
		             		String userResponse = readJSONFeed(userLookup);
		       
		             		
		             		
		                  		JSONObject user = new JSONObject(userResponse);
		                  		JSONObject userInfo = user.getJSONObject("user");
		             			int role_id = userInfo.getInt("role");
		        				Intent shiftToDoctorHome = new Intent (v.getContext(), DoctorHome.class);
		        				Intent shiftToPharm = new Intent (v.getContext(), PharmacistMainFinal.class);
		        				Intent shiftToNurseHome = new Intent (v.getContext(), NurseHome.class);
		        				Intent shiftToPatientHome = new Intent (v.getContext(), PatientMain.class);
		        				
		        				if(role_id == 1){
		        					startActivityForResult(shiftToDoctorHome, 0);
		        				}	
		        				else if(role_id == 2){
		        					startActivityForResult(shiftToNurseHome, 0);
		        				}
		        				else if(role_id == 3){
		        					startActivityForResult(shiftToPharm, 0);
		        				}
		        				else if (role_id == 4){
		        					startActivityForResult(shiftToPatientHome, 0);
		        				}
		             		}
		             			//oops
		             	}
		        		
		             
		       catch(JSONException e)
		        {
		            	 //oops
		        }
		        
		        catch(URISyntaxException ex)
		        {
		        	
		        }
		        catch(IOException ex)
		        {
		        	
		        	
		        }
					
				
				}});
			
		
			
		}
	 
	
	
}
