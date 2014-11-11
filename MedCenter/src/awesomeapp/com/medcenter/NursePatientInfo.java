package awesomeapp.com.medcenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class NursePatientInfo extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_nurse_patient_info);
		
		
		Button next1 = (Button) findViewById(R.id.button3);
		next1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			Intent shiftToPatientInfo = new Intent (v.getContext(), NurseVitals.class);
			startActivityForResult(shiftToPatientInfo, 0);
			}
			
		});
		
		Button next2 = (Button) findViewById(R.id.button2);
		next2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			Intent shiftToPatientInfo = new Intent (v.getContext(), NurseHome.class);
			startActivityForResult(shiftToPatientInfo, 0);
			}
			
		});
	}
}
