package uniandes.sischok;

import java.util.Date;

import com.google.android.gms.maps.model.LatLng;

import uniandes.sischok.mundo.CentroIncidentes;
import uniandes.sischok.mundo.Incidente;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
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
		    String value = extras.getString("IncidenteLatLng");
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
		LatLng incidenteP = new LatLng(Float.parseFloat(geo[0]), Float.parseFloat(geo[1]));
	    SharedPreferences sharedpreferences = getSharedPreferences(CentroIncidentes.nombrePreferencias, Context.MODE_PRIVATE);
		AlertDialog alertDialog = new AlertDialog.Builder(CrearIncidenteConLatLng.this).create();
	    if(!titulo.equals("")&&!descripcion.equals("")&&(gravedad<=5||gravedad>0))
	    	{
				final Incidente indicienteNuevo = new Incidente(null, "",titulo, descripcion, getIntent().getIntExtra("zona",0), gravedad,Long.valueOf(new Double(4.591655).longValue()), Long.valueOf((new Double(-74.063333)).longValue()), new Date(), sharedpreferences.getString(CentroIncidentes.prefNombre, "Administrador Sischok"));
				CentroIncidentes centroInc = CentroIncidentes.darInstancia(this);
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
