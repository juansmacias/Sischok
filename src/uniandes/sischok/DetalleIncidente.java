package uniandes.sischok;

import com.loopj.android.http.JsonHttpResponseHandler;

import uniandes.sischok.mundo.CentroIncidentes;
import uniandes.sischok.mundo.Incidente;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
		CentroIncidentes centroIncidentes = CentroIncidentes.darInstancia(this);
		String id = getIntent().getStringExtra("id");
		try{
			long idLong = Long.parseLong(id);
			incActual = centroIncidentes.darIncidentePorId(idLong);
		}
		catch(NumberFormatException e)
		{
			try
			{
				String urlGet = "incidentes/"+id;
				BackendRestClient.get(urlGet,new JsonHttpResponseHandler()
				{
					@Override
					public void onSuccess(int statusCode, org.apache.http.Header[] headers, org.json.JSONObject response) {
						Log.i("Servicio Backend", "OnSuccess: "+response);
						DetalleIncidente.this.setIncidente(Incidente.toIncidenteDeServidor(response));
					}
					@Override
					public void onFailure (int statusCode, org.apache.http.Header[] headers,
							String responseString, Throwable throwable) {
						super.onFailure(statusCode, headers, responseString, throwable);
						Log.e("Servicio Backend", "onFailure");

					}

				});
			}
			catch(Exception e1)
			{
				
			}
			
		}
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
	public void setIncidente(Incidente inc)
	{
		incActual = inc;
	}
}
