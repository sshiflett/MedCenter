package awesomeapp.com.medcenter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
import zephyr.android.BioHarnessBT.BTClient;
import zephyr.android.BioHarnessBT.ZephyrProtocol;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



public class NurseVitals extends Activity {
	int patientId;
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
	
	private class recordVitals extends AsyncTask<String, Void,String>{
	    @Override
	    protected String doInBackground(String... urls){
				return readJSONFeed(urls[0]);
	    	}
	    protected void onPostExecute(String result){
			try
				{
					JSONObject vitalsInfo = new JSONObject(result);
					int status = vitalsInfo.getInt("status");
					if(status == 201){
						Toast.makeText(NurseVitals.this, 
			            	    "Vitals saved.", Toast.LENGTH_SHORT).show();
					}
					

	
				}
				catch(JSONException e){
					//oops
				}
	    	}
	    }
	
	int respirationRateInt;
	int heartRateInt;
    /** Called when the activity is first created. */
	BluetoothAdapter adapter = null;
	BTClient _bt;
	ZephyrProtocol _protocol;
	NewConnectedListener _NConnListener;
	private final int HEART_RATE = 0x100;
	private final int RESPIRATION_RATE = 0x102;
	private final int SKIN_TEMPERATURE = 0x103;
	int heartR=0;
	int respRate=0;
	
	
	  @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_nurse_vitals);
			Bundle unBundler = getIntent().getExtras();
			patientId = unBundler.getInt("PatientId");
			userId = unBundler.getInt("UserId");
			String userLookup = "http://104.131.116.247/api/user/?user_id=" +  userId +"&method=get-user";
			new parseRole().execute(userLookup);
	        
	        /*Sending a message to android that we are going to initiate a pairing request*/
	        IntentFilter filter = new IntentFilter("android.bluetooth.device.action.PAIRING_REQUEST");
	        /*Registering a new BTBroadcast receiver from the Main Activity context with pairing request event*/
	       this.getApplicationContext().registerReceiver(new BTBroadcastReceiver(), filter);
	        // Registering the BTBondReceiver in the application that the status of the receiver has changed to Paired
	        IntentFilter filter2 = new IntentFilter("android.bluetooth.device.action.BOND_STATE_CHANGED");
	       this.getApplicationContext().registerReceiver(new BTBondReceiver(), filter2);
	        
	    
	        Button btnConnect = (Button) findViewById(R.id.b_ps_confirm);
	        if (btnConnect != null)
	        {
	        	btnConnect.setOnClickListener(new OnClickListener() {
	        		public void onClick(View v) {
	        			String BhMacID = "00:07:80:9D:8A:E8";
	        			//String BhMacID = "00:07:80:88:F6:BF";
	        			adapter = BluetoothAdapter.getDefaultAdapter();
	        			
	        			Set<BluetoothDevice> pairedDevices = adapter.getBondedDevices();
	        			
	        			if (pairedDevices.size() > 0) 
	        			{
	                        for (BluetoothDevice device : pairedDevices) 
	                        {
	                        	if (device.getName().startsWith("BH")) 
	                        	{
	                        		BluetoothDevice btDevice = device;
	                        		BhMacID = btDevice.getAddress();
	                                break;

	                        	}
	                        }
	                        
	                        
	        			}
	        			
	        			//BhMacID = btDevice.getAddress();
	        			BluetoothDevice Device = adapter.getRemoteDevice(BhMacID);
	        			String DeviceName = Device.getName();
	        			_bt = new BTClient(adapter, BhMacID);
	        			_NConnListener = new NewConnectedListener(Newhandler,Newhandler);
	        			_bt.addConnectedEventListener(_NConnListener);
	        			
	        			EditText tv1 = (EditText)findViewById(R.id.et_heartRate);
	        			tv1.setText("000");
	        			
	        			EditText tv2 = (EditText)findViewById(R.id.et_respirationRate);
	        			tv2.setText("000");
	        			
	        			
	        			if(_bt.IsConnected())
	        			{
	        				_bt.start();				 
							 //Reset all the values to 0s
	        			}
	        			else
	        				Toast.makeText(NurseVitals.this, "Unable to connect to sensor", Toast.LENGTH_SHORT).show();
	        			
	        		}
	        	});
	        }
	        
	        Button stopRecord = (Button) findViewById(R.id.b_ps_cancel);
	        if (stopRecord != null)
	        {
	        	stopRecord.setOnClickListener(new OnClickListener() 
	        	{
					public void onClick(View v) 
	        		{
						TextView heartRateTV = (EditText)findViewById(R.id.et_heartRate);
	        			heartRateInt = Integer.parseInt(heartRateTV.getText().toString());
	        			
	        			TextView respirationRateTV = (EditText)findViewById(R.id.et_respirationRate);
	        			respirationRateInt = Integer.parseInt(respirationRateTV.getText().toString());
	        		
	        			String recordVitals = "http://104.131.116.247/api/vitals/?patient_id=" + patientId + "&heart_rate=" + heartRateInt + "&breathing_rate=3&respiration_rate=" + respirationRateInt + "&method=edit-vitals";
	        			new recordVitals().execute(recordVitals);
	        			
	        		}
	        	});
	        }
	  
	       
	        Button closeActivity= (Button) findViewById(R.id.b_dh_logout);
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
	   
	    
		private class BTBondReceiver extends BroadcastReceiver {
			@Override
			public void onReceive(Context context, Intent intent) {
				Bundle b = intent.getExtras();
				BluetoothDevice device = adapter.getRemoteDevice(b.get("android.bluetooth.device.extra.DEVICE").toString());
				Log.d("Bond state", "BOND_STATED = " + device.getBondState());
			}
	    }
	    private class BTBroadcastReceiver extends BroadcastReceiver {
			@Override
			public void onReceive(Context context, Intent intent) {
				Log.d("BTIntent", intent.getAction());
				Bundle b = intent.getExtras();
				Log.d("BTIntent", b.get("android.bluetooth.device.extra.DEVICE").toString());
				Log.d("BTIntent", b.get("android.bluetooth.device.extra.PAIRING_VARIANT").toString());
				try {
					BluetoothDevice device = adapter.getRemoteDevice(b.get("android.bluetooth.device.extra.DEVICE").toString());
					Method m = BluetoothDevice.class.getMethod("convertPinToBytes", new Class[] {String.class} );
					byte[] pin = (byte[])m.invoke(device, "1234");
					m = device.getClass().getMethod("setPin", new Class [] {pin.getClass()});
					Object result = m.invoke(device, pin);
					Log.d("BTTest", result.toString());
				} catch (SecurityException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NoSuchMethodException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	    }
	    

	    final  Handler Newhandler = new Handler(){
	    	public void handleMessage(Message msg)
	    	{
	    		TextView tv;
	    		switch (msg.what)
	    		{
	    		case HEART_RATE:
	    			String HeartRatetext = msg.getData().getString("HeartRate");
	    			tv = (EditText)findViewById(R.id.et_heartRate);
	    			System.out.println("Heart Rate Info is "+ HeartRatetext);
	    			if (tv != null)tv.setText(HeartRatetext);
	    		break;
	    		
	    		case RESPIRATION_RATE:
	    			String RespirationRatetext = msg.getData().getString("RespirationRate");
	    			tv = (EditText)findViewById(R.id.et_respirationRate);
	    			if (tv != null)tv.setText(RespirationRatetext);
	    		
	    		break;
	    		
	
	    	    		
	    		
	    		}
	    	}

	    };
	    
	}


