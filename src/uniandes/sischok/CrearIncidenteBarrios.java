package uniandes.sischok;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class CrearIncidenteBarrios extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crear_incidente_barrios);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.crear_incidente_barrios, menu);
		return true;
	}
	
	public void pasarADescripcion (View view)
	{
		ImageButton boton = (ImageButton) findViewById(view.getId());
		Intent intentDescripcion = new Intent(this, CrearIncidenteDescripcion.class);
		intentDescripcion.putExtra("zona", Integer.parseInt(boton.getContentDescription()+""));
		startActivity(intentDescripcion);
	}

}
