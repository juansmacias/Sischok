package uniandes.sischok;

import java.util.Date;

import org.apache.http.Header;

import com.loopj.android.http.JsonHttpResponseHandler;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.support.v4.app.NavUtils;
import uniandes.sischok.mundo.CentroIncidentes;
import uniandes.sischok.mundo.Incidente;
import uniandes.sischok.BackendRestClient;

public class CrearIncidenteDescripcion extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crear_incidente_descripcion);
		setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.crear_incidente_descripcion, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@SuppressWarnings("deprecation")
	public void crearIncidente (View view)
	{
		String titulo = ((EditText) findViewById(R.id.txtTitulo)).getText().toString();
		String descripcion = ((EditText) findViewById(R.id.txtDescripcion)).getText().toString();
		int gravedad = Integer.parseInt(((EditText) findViewById(R.id.txtGravedad)).getText().toString());
		SharedPreferences sharedpreferences = getSharedPreferences(CentroIncidentes.nombrePreferencias, Context.MODE_PRIVATE);
		AlertDialog alertDialog = new AlertDialog.Builder(CrearIncidenteDescripcion.this).create();
		if(!titulo.equals("")&&!descripcion.equals("")&&(gravedad<=5||gravedad>0)){
			CentroIncidentes centroInc = CentroIncidentes.darInstancia(this);
			final Incidente indicienteNuevo = new Incidente(null, titulo, descripcion, getIntent().getIntExtra("zona",0), gravedad, new Date(), sharedpreferences.getString(CentroIncidentes.prefNombre, "Administrador Sischok"));
			ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = cm.getActiveNetworkInfo();
			if (netInfo != null && netInfo.isConnectedOrConnecting())
			{
				//esta conectado a internet
				try{
					String s = indicienteNuevo.toString();
					Log.i("sdsad", s);
//					BackendRestClient.post("incidentes", this,indicienteNuevo.toString(), new JsonHttpResponseHandler()
//					{
//						@Override
//						public void onSuccess(int statusCode, org.apache.http.Header[] headers, org.json.JSONObject response) {
//							CentroIncidentes centroInc = CentroIncidentes.darInstancia(CrearIncidenteDescripcion.this);
//							centroInc.crearIncidente(Incidente.toIncidente(response));
//							Log.i("Servicio Backend", "OnSuccess");
//						}
//						@Override
//						public void onFailure(int statusCode, Header[] headers,
//								String responseString, Throwable throwable) {
//							super.onFailure(statusCode, headers, responseString, throwable);
//							Log.e("Servicio Backend", "onFailure");
//						}
//
//					});

				}
				catch(Exception e)
				{
					Log.e("Servicio Backend", "catch");
					centroInc.crearIncidente(indicienteNuevo);
					Log.i("Servicio Backend", "Creo Incidente: "+indicienteNuevo.getTitulo()+". caso conectivididad");
				}

				//TODO falta guardar esto en algun lado para que despues se suba
			}

			else
			{
				//hacer algo cuando no tiene conectiviad
				centroInc.crearIncidente(indicienteNuevo);
				Log.i("Servicio Backend", "Creo Incidente: "+indicienteNuevo.getTitulo()+". caso No conectivididad");
				//TODO falta guardar esto en algun lado para que despues se suba

			}

			alertDialog.setTitle("Exito");
			alertDialog.setMessage("Su incidente se ha agregado");
			alertDialog.setButton("OK", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int which) 
				{
					Intent crearIncDes = new Intent(CrearIncidenteDescripcion.this, Inicio.class);
					startActivity(crearIncDes);
				}
			}
					);

			alertDialog.setButton2("Compartir ", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(CrearIncidenteDescripcion.this,CompartirIncidente.class);
					intent.putExtra("incidente", indicienteNuevo.toString());
					startActivity(intent);		            	
				}     
			});
			alertDialog.show();
		}

		else
		{
			alertDialog.setTitle("Error");
			alertDialog.setMessage("No cumple los requisitos basicos");
			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
				}
			});
			alertDialog.show();
		}	
	}
}
