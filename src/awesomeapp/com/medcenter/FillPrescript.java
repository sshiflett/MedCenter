package awesomeapp.com.medcenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class FillPrescript extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_fill_prescript);
		
		//INITIALIZE BUTTONS (to names as they appear in the screen)
		Button reject = (Button) findViewById(R.id.b_reject);
		Button cancel = (Button) findViewById(R.id.b_cancel);
		Button fill_rx = (Button) findViewById(R.id.b_ps_confirm);
		//TEXT VIEWS (To names that correspond to their labels)
		TextView prescribing_doctor = (TextView) findViewById(R.id.patient_doctor);
		TextView patient = (TextView) findViewById(R.id.selected_patient_name);
		TextView prescription = (TextView) findViewById(R.id.selected_patient_prescription);
		TextView amount = (TextView) findViewById(R.id.selected_patient_prescription_amt1);
		
		//Fill RX HANDLER
		fill_rx.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			Intent shiftToPrescript = new Intent (v.getContext(), PharmacistMainFinal.class);
			startActivityForResult(shiftToPrescript, 0);
				
			}
		});
		
		//REJECT HANDLER
	    reject.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			Intent shiftToPrescript = new Intent (v.getContext(), PharmacistMainFinal.class);
			startActivityForResult(shiftToPrescript, 0);
				
			}
		});
		
	    //CANCEL HANDLER
		cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			Intent shiftToPrescript = new Intent (v.getContext(), PharmacistMainFinal.class);
			startActivityForResult(shiftToPrescript, 0);
				
			}
		});
	
	}
}
