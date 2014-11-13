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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;    
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends Activity {
	
		
		public class JSONParser {

		    InputStream is = null;
		    JSONObject jObj = null;
		    String json = "";

		    // constructor
		    public JSONParser() {}

		    public JSONObject getJSONFromUrl(String url) {

		        // Making HTTP request
		        try {
		            // defaultHttpClient
		            DefaultHttpClient httpClient = new DefaultHttpClient();
		            HttpGet httpGet = new HttpGet(url);

		            HttpResponse httpResponse = httpClient.execute(httpGet);
		            HttpEntity httpEntity = httpResponse.getEntity();
		            is = httpEntity.getContent();

		        } catch (UnsupportedEncodingException e) {
		            e.printStackTrace();
		        } catch (ClientProtocolException e) {
		            e.printStackTrace();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }

		        try {
		            BufferedReader reader = new BufferedReader(new InputStreamReader(
		                    is, "iso-8859-1"), 8);
		            StringBuilder sb = new StringBuilder();
		            String line = null;
		            while ((line = reader.readLine()) != null) {
		                sb.append(line + "\n");
		            }
		            is.close();
		            json = sb.toString();
		        } catch (Exception e) {
		            Log.e("Buffer Error", "Error converting result " + e.toString());
		        }

		        // try parse the string to a JSON object
		        try {
		            jObj = new JSONObject(json);
		        } catch (JSONException e) {
		            Log.e("JSON Parser", "Error parsing data " + e.toString());
		        }

		        // return JSON String
		        return jObj;

		    }
		}
			



			
			@Override
			protected void onCreate(Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);
				requestWindowFeature(Window.FEATURE_NO_TITLE);
				setContentView(R.layout.activity_main);

				final JSONParser jParser = new JSONParser();
				final EditText username = (EditText) findViewById(R.id.et_user_name);
				final EditText password = (EditText) findViewById(R.id.et_input2);
			
				Button next = (Button) findViewById(R.id.b_ps_confirm);
				next.setOnClickListener(new View.OnClickListener() {
			
						@Override
						public void onClick(View v) {
				        	// Get entered text from EditText boxes.
				           	String userName = username.getText().toString();
				        	String passWord = password.getText().toString();
				        	int role_id = 0;
				        	String userId = "";
				        	
				        	
				            String httpRequest = "http://173.78.61.249:8080/api/authenticate/?username=" + userName + "&password=" + passWord;
				            //String httpResponse = readJSONFeed(httpRequest);
				            JSONObject loginObject = jParser.getJSONFromUrl(httpRequest);
				            
				        	
				        	// Use "authenticate" function, use response.
				             try{
				     
				             int status =  loginObject.getInt("status");  
				        	
				        	// If authenticate status returns "forbidden", show error message using toast.
				        	 if(status == 202)
				        	 {
				        	 		userId = String.valueOf(loginObject.getInt("id"));
				        	 		// Send a request for user info using id returned from authenticate
				             		String userLookup = "http://173.78.61.249:8080/api/user/?user_id=".concat(userId);
				             		JSONObject userObject = jParser.getJSONFromUrl(userLookup);
				             		
				             		try{
				             			role_id = userObject.getInt("role");
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
				            
				                    
				        	 
				             		else
				        	 			{
				             				Toast.makeText(MainActivity.this, 
					                	    "Your login was incorrect. Please try again.", Toast.LENGTH_SHORT).show();
				             			}
				             		}
				             		catch(JSONException f){
				             			//oops
				             		}
				        		}
				             }
				             catch(JSONException e)
				             {
				            	 //oops
				             }
							
						
						}});
					
				
					
				}
			 
			
			
		}
