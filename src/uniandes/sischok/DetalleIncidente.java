package uniandes.sischok;

import uniandes.sischok.mundo.CentroIncidentes;
import uniandes.sischok.mundo.Incidente;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import uniandes.sischok.R;


public class DetalleIncidente extends Activity {
	
	private Incidente incActual;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detalle_incidente);		
		long id = getIntent().getLongExtra("id",0);
		CentroIncidentes centroIncidentes = CentroIncidentes.darInstancia(this);
		incActual = centroIncidentes.darIncidentePorId(id);
		setTitle(incActual.getTitulo());		
		EditText txtdesc = (EditText) findViewById(R.id.txtDetalleIncDescripcion);
		txtdesc.setText(incActual.getDescripcion());
		EditText txtZona = (EditText) findViewById(R.id.txtDetalleIncZona);
		txtZona.setText(incActual.getZona()+"");
		EditText txtGravedad = (EditText) findViewById(R.id.txtDetalleIncGravedad);
		txtGravedad.setText(incActual.getGravedad()+"");
		EditText txtCreador = (EditText) findViewById(R.id.txtDetalleIncUsuario);
		txtCreador.setText(incActual.getUsuarioCreacion());
		EditText txtfecha = (EditText) findViewById(R.id.txtDetalleIncFechaCreacion);
		txtfecha.setText(incActual.getFechaCreacion().toString());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detalle_incidente, menu);
		return true;
	}
	
	public void compartirInc (View view)
	{
		Intent intent = new Intent(this,CompartirIncidente.class);
		intent.putExtra("incidente", incActual.toString());
		startActivity(intent);	
	}
}
