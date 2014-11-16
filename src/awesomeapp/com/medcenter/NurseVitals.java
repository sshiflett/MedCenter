package awesomeapp.com.medcenter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

import zephyr.android.BioHarnessBT.BTClient;
import zephyr.android.BioHarnessBT.ZephyrProtocol;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
    /** Called when the activity is first created. */
	BluetoothAdapter adapter = null;
	BTClient _bt;
	ZephyrProtocol _protocol;
	NewConnectedListener _NConnListener;
	private final int HEART_RATE = 0x100;
	private final int RESPIRATION_RATE = 0x102;
	private final int SKIN_TEMPERATURE = 0x103;
	final int heartR=0;
	final int respRate=0;
	final int temp=0;
	
	
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nurse_vitals);
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
        			
        			TextView tv1 = (EditText)findViewById(R.id.et_heartRate);
        			tv1.setText("000");
        			
        			TextView tv2 = (EditText)findViewById(R.id.et_respirationRate);
        			tv2.setText("000");
        			
        			TextView tv3  = 	(EditText)findViewById(R.id.et_temperature);
        			tv3.setText("000");
        			
        			if(_bt.IsConnected())
        			{
        				_bt.start();				 
						 //Reset all the values to 0s
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
    		
    		case SKIN_TEMPERATURE:
    			String SkinTemperaturetext = msg.getData().getString("SkinTemperature");
    			tv = (EditText)findViewById(R.id.et_temperature);
    			if (tv != null)tv.setText(SkinTemperaturetext);

    		break;
    	    		
    		
    		}
    	}

    };
    
}


