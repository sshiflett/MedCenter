package awesomeapp.com.medcenter;

import java.util.Set;

import zephyr.android.BioHarnessBT.BTClient;
import zephyr.android.BioHarnessBT.BTReceiver;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class NurseVitals extends Activity {
/* Comment to test commit */
	
	BluetoothAdapter adapter = null;
	BTClient _bt;
	ZephyrProtocol _protocol;
	NewConnectedListener _NConnListener;
	final Handler Newhandler = new Handler();
	private final int HEART_RATE = 0x100;
	private final int RESPIRATION_RATE = 0x101;
	private final int SKIN_TEMPERATURE = 0x102;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_patient_vitals_final);
		IntentFilter filter = new IntentFilter("android.bluetooth.device.action.PAIRING_REQUEST");
		this.getApplicationContext().registerReceiver(new BTReceiver(), filter);
		IntentFilter filter2 = new IntentFilter("android.bluetooth.device.action.BOND_STATE_CHANGED");
		this.getApplicationContext().registerReceiver(new BTReceiver(), filter2);
		
		
		Button next = (Button) findViewById(R.id.b_dh_logout);
		next.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			Intent shiftToPatientInfo = new Intent (v.getContext(), NursePatientInfo.class);
			startActivityForResult(shiftToPatientInfo, 0);
			}
		
			
		});
		
		Button btnConnect = (Button) findViewById(R.id.b_ps_confirm);
		if (btnConnect != null)
		{
			btnConnect.setOnClickListener(new OnClickListener() 
			{
				public void onClick(View v)
				{
					String BHMacID="00:07:80:9D:8A:E8";
					adapter = BluetoothAdapter.getDefaultAdapter();
					
					Set<BluetoothDevice> pairedDevices = adapter.getBondedDevices();
				
					if (pairedDevices.size() > 0)
					{
						for (BluetoothDevice device : pairedDevices)
						{
							if (device.getName().startsWith("BH"))
							{
								BluetoothDevice btDevice = device;
								BHMacID = btDevice.getAddress();
								break;
							}
						}
					}
					
				BluetoothDevice Device = adapter.getRemoteDevice(BHMacID);
				String DeviceName = Device.getName();
				_bt = new BTClient(adapter, BHMacID);
				_NConnListener = new NewConnectedListener(Newhandler, Newhandler);
				_bt.addConnectedEventListener(_NConnListener);
				
				TextView tv1 = (EditText)findViewById(R.id.et_heartRate);
				tv1.setText("000");
				
				tv1 = (EditText)findViewById(R.id.et_respirationRate);
				tv1.setText("000");
				
				tv1 = (EditText)findViewById(R.id.et_temperature);
				tv1.setText("000");
				
				
				
				}
			});
		}
	}

	/*
private class BTBondReceiver extends BroadcastReceiver{
	@Override
	public void onReceiver(Context context, Intent intent)
	{
			Bundle b = intent.getExtras();
			BluetoothDevice device = adapter.getRemoteDevice(b.get("android.bluetooth.device.extra.DEVICE").toString());
	}
	
}
*/
}