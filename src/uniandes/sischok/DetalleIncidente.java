package uniandes.sischok;

import uniandes.sischok.mundo.CentroIncidentes;
import uniandes.sischok.mundo.Incidente;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.EditText;
import uniandes.sischok.R;


public class DetalleIncidente extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detalle_incidente);		
		long id = getIntent().getLongExtra("id",0);
		CentroIncidentes centroIncidentes = CentroIncidentes.darInstancia(this);
		Incidente inc = centroIncidentes.darIncidentePorId(id);
		setTitle(inc.getTitulo());		
		EditText txtdesc = (EditText) findViewById(R.id.txtDetalleIncDescripcion);
		txtdesc.setText(inc.getDescripcion());
		EditText txtZona = (EditText) findViewById(R.id.txtDetalleIncZona);
		txtZona.setText(inc.getZona()+"");
		EditText txtGravedad = (EditText) findViewById(R.id.txtDetalleIncGravedad);
		txtGravedad.setText(inc.getGravedad()+"");
		EditText txtCreador = (EditText) findViewById(R.id.txtDetalleIncUsuario);
		txtCreador.setText(inc.getUsuarioCreacion());
		EditText txtfecha = (EditText) findViewById(R.id.txtDetalleIncFechaCreacion);
		txtfecha.setText(inc.getFechaCreacion().toString());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detalle_incidente, menu);
		return true;
	}
}
