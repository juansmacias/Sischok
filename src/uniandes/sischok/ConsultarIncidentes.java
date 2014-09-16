package uniandes.sischok;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ConsultarIncidentes extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_consultar_incidentes);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.consultar_incidentes, menu);
		return true;
	}

}
