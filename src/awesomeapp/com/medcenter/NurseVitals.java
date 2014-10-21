package awesomeapp.com.medcenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class NurseVitals extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_patient_vitals_final);
		
		Button next = (Button) findViewById(R.id.button3);
		next.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			Intent shiftToPatientInfo = new Intent (v.getContext(), NursePatientInfo.class);
			startActivityForResult(shiftToPatientInfo, 0);
			}
		});
	}
}
