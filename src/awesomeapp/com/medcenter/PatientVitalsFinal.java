package awesomeapp.com.medcenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class PatientVitalsFinal extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_patient_vitals_final);
		
		Button next = (Button) findViewById(R.id.b_dh_logout);
		next.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			Intent shiftToPatientInfo = new Intent (v.getContext(), PatientInfoFinal.class);
			startActivityForResult(shiftToPatientInfo, 0);
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		
	}
}
