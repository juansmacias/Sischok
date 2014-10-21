package uniandes.sischok;

import java.util.Date;

import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.JsonHttpResponseHandler;

import uniandes.sischok.mundo.CentroIncidentes;
import uniandes.sischok.mundo.Incidente;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class CrearIncidenteConLatLng extends Activity
{

	//String con el punto LatLng
	private String value;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crear_incidente_latlng);
		setupActionBar();

		Bundle extras = getIntent().getExtras();
		if (extras != null) 
		{
			//punto elegido en el mapa
			value = extras.getString("IncidenteLatLng");
		}
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar()
	{
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) 
		{
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	//TODO
	@SuppressWarnings("deprecation")
	public void crearIncidenteLatLng (View view)
	{
		String titulo = ((EditText) findViewById(R.id.txtTitulo)).getText().toString();
		String descripcion = ((EditText) findViewById(R.id.txtDescripcion)).getText().toString();
		int gravedad = Integer.parseInt(((EditText) findViewById(R.id.txtGravedad)).getText().toString());
		//Saca la localizacion del incidente y la agrega en 
		String[] geo = value.split(",");
		SharedPreferences sharedpreferences = getSharedPreferences(CentroIncidentes.nombrePreferencias, Context.MODE_PRIVATE);
		AlertDialog alertDialog = new AlertDialog.Builder(CrearIncidenteConLatLng.this).create();
		if(!titulo.equals("")&&!descripcion.equals("")&&(gravedad<=5||gravedad>0))
		{
			final Incidente indicienteNuevo = new Incidente(null, "",titulo, descripcion, getIntent().getIntExtra("zona",18), gravedad,Double.valueOf(geo[0]), Double.valueOf(geo[1]), new Date(), sharedpreferences.getString(CentroIncidentes.prefNombre, "Administrador Sischok"));
			CentroIncidentes centroInc = CentroIncidentes.darInstancia(this);
			ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = cm.getActiveNetworkInfo();
			if (netInfo != null && netInfo.isConnectedOrConnecting())
			{
				//esta conectado a internet
				try{

					BackendRestClient.post("incidentes", this,indicienteNuevo.toString(), new JsonHttpResponseHandler()
					{
						@Override
						public void onSuccess(int statusCode, org.apache.http.Header[] headers, org.json.JSONObject response) {
							CentroIncidentes centroInc = CentroIncidentes.darInstancia(CrearIncidenteConLatLng.this);
							centroInc.crearIncidente(Incidente.toIncidenteDeServidor(response));
							Log.i("Servicio Backend", "OnSuccess");
						}
						@Override
						public void onFailure (int statusCode, org.apache.http.Header[] headers,
								String responseString, Throwable throwable) {
							super.onFailure(statusCode, headers, responseString, throwable);
							Log.e("Servicio Backend", "onFailure");

						}

					});
				}
				catch(Exception e)
				{
					Log.e("Servicio Backend", "catch");
					centroInc.crearIncidente(indicienteNuevo);
					Log.i("Servicio Backend", "Creo Incidente: "+indicienteNuevo.getTitulo()+". caso conectivididad");
				}
			}
			
			centroInc.crearIncidente(indicienteNuevo);
			alertDialog.setTitle("Exito");
			alertDialog.setMessage("Su incidente se ha agregado");
			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					Intent crearIncDes = new Intent(CrearIncidenteConLatLng.this, Inicio.class);
					startActivity(crearIncDes);
				}
			});
			alertDialog.setButton2("Compartir ", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(CrearIncidenteConLatLng.this,CompartirIncidente.class);
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
