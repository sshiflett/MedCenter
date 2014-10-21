package awesomeapp.com.medcenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class FillPrescript extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_fill_prescript);
		
		Button next = (Button) findViewById(R.id.button3);
		next.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			Intent shiftToPrescript = new Intent (v.getContext(), PharmacistMainFinal.class);
			startActivityForResult(shiftToPrescript, 0);
				
			}
		});
	
	}
}
