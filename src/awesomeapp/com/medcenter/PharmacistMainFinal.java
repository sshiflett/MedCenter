package awesomeapp.com.medcenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class PharmacistMainFinal extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_pharmacist_main_final);
		
		Button next = (Button) findViewById(R.id.b_ps_confirm);
		next.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			Intent shiftToPrescript = new Intent (v.getContext(), FillPrescript.class);
			startActivityForResult(shiftToPrescript, 0);
				
			}
		});
		
		Button next1 = (Button) findViewById(R.id.b_ps_cancel);
		next1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			Intent shiftToPrescript = new Intent (v.getContext(), MainActivity.class);
			startActivityForResult(shiftToPrescript, 0);
				
			}
		});
	}
}
