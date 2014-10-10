package uniandes.sischok;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class ConsultarIncidentes extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_consultar_incidentes);
		addListenerOnRadioGroup();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.consultar_incidentes, menu);
		return true;
	}
	
	public void addListenerOnRadioGroup() {

		final RadioGroup grpradioLocalidad = (RadioGroup) findViewById(R.id.radioLocalidad);
		Button btnConsultar = (Button) findViewById(R.id.btnConsultar);

		btnConsultar.setOnClickListener(new OnClickListener() {

	        @Override
	        public void onClick(View v) {
	            int selectedId = grpradioLocalidad.getCheckedRadioButtonId();
	            RadioButton radioLocalidad = (RadioButton) findViewById(selectedId);
	            Intent intentVerIncC = new Intent(ConsultarIncidentes.this,VerResultadoIncidentes.class);
	            intentVerIncC.putExtra("zona", radioLocalidad.getTag()+"");
	            startActivity(intentVerIncC);
	        }

	    });

	  }
	
	public void consultar (View view)
	{
		
	}

}
