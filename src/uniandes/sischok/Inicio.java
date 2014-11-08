package uniandes.sischok;

import java.util.List;
import java.util.Calendar;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import uniandes.sischok.R;
import uniandes.sischok.mundo.CentroEventos;
import uniandes.sischok.mundo.CentroIncidentes;
import uniandes.sischok.mundo.Incidente;



public class Inicio extends Activity implements LocationListener{

	private SharedPreferences sharedpreferences;
	private IncidentesListAdapter mAdapter;
	private List<Incidente> ultimosIncidentes;
	@SuppressWarnings("unused")
	private Context mContext;
	private GoogleMap gMap;
	private Location location;
	protected LocationManager locationManager;
	private LatLng myLocation;
	boolean isGPSEnabled = false;
	boolean isNetworkEnabled = false;
	boolean canGetLocation = false;

	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inicio);
		sharedpreferences = getSharedPreferences(CentroIncidentes.nombrePreferencias, Context.MODE_PRIVATE);
		CentroIncidentes centroI = CentroIncidentes.darInstancia(this);

		if (!sharedpreferences.contains(CentroIncidentes.prefPrimeraVez))
		{
			  
			centroI.iniciarBasedeDatos();
			Intent intentBienvenida = new Intent(this, Bienvenida.class);
			startActivityForResult(intentBienvenida,1);
		}
		Intent intent = new Intent(this, CentroEventos.class);
		startService(intent);

		// TODO:crea el intent de las notificaciones		
		// Start service Centro Eventos using AlarmManager

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, 10);

		PendingIntent pintent = PendingIntent.getService(this, 0, intent, 0);

		AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		//for 30 mint 60*60*1000
		alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
				60*60*1000, pintent);
        try 
        {
            initilizeMap();
 
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public void refrescarIncidentesListAdapter(List<Incidente> lstIncidentes)
	{
		if(lstIncidentes!=null)
		ultimosIncidentes = lstIncidentes;
		mAdapter = new IncidentesListAdapter(this,R.layout.incidentelistview,ultimosIncidentes);
		ListView lstUltimo = (ListView) findViewById(R.id.lstNuevosIncidentes);
		lstUltimo.setAdapter(mAdapter);
		lstUltimo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {
				Intent intentD = new Intent(Inicio.this, DetalleIncidente.class);
				intentD.putExtra("id",Long.parseLong(((TextView)view.findViewById(R.id.lblSinTituloIncListView)).getTag()+""));
				startActivity(intentD);
			}

		});
		mAdapter.notifyDataSetChanged();
		
        if(ultimosIncidentes!=null)
        {
        	for (Incidente incActual : ultimosIncidentes) {
            	gMap.addMarker(new MarkerOptions()
                .position(new LatLng(incActual.getLatitud(), incActual.getLongitud()))
                .title(incActual.getTitulo()));
			}

        }
	}

	@Override
	protected void onResume() {
		CentroIncidentes centroI = CentroIncidentes.darInstancia(this);
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting())
		{
			try{
				BackendRestClient.get("ultimos5incidentes",new JsonHttpResponseHandler()
				{
					@Override
					public void onSuccess(int statusCode, org.apache.http.Header[] headers, org.json.JSONArray response) {
						CentroIncidentes centroInc = CentroIncidentes.darInstancia(Inicio.this);
						Log.i("Servicio Backend", "OnSuccess: "+response);
						Inicio.this.refrescarIncidentesListAdapter(centroInc.darUltimos5IncidentesEnServidor(response));
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
				Log.i("Servicio Backend", "ultimos");
			}
		}
		else
		{
			ultimosIncidentes = centroI.darUltimos5Incidentes();
			refrescarIncidentesListAdapter(null);
		}
		
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.inicio, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch  ( item . getItemId ()) 
		{ 
		case R.id.crear : 
			//crear intent y start y verificar conexion
			ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = cm.getActiveNetworkInfo();
			if (netInfo != null && netInfo.isConnectedOrConnecting())
			{
				//esta conectado a internet
				Intent intentCrearGPS = new Intent(this, CrearIncidenteGPS.class);
				startActivity(intentCrearGPS);
				return true;
			}
			else 
			{
				Intent intetCrear = new Intent(this, CrearIncidenteBarrios.class);
				startActivity(intetCrear);
				return true;
			}
		case R.id.ver : 
			//crear intent y start 
			Intent intetConsultar = new Intent(this, ConsultarIncidentes.class);
			startActivity(intetConsultar);
			return true;
		}
		return  false ; 
	}
	public  void onGroupItemClick ( MenuItem item ) 
	{ 
		//	    // Uno de los elementos del grupo (con el atributo onClick) se ha hecho clic 
		//	    // El parametro elemento pasado aqui indica que elemento es 
		//	    // Todas las demas clics elemento de menu se manejan por onOptionsItemSelected ()
		//		 switch  ( item . getItemId ()) 
		//		 { 
		//	        case R.id.crear : 
		//	        	//crear intent y start y verificar conexion
		//	       	 ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		//			    NetworkInfo netInfo = cm.getActiveNetworkInfo();
		//			    if (netInfo != null && netInfo.isConnectedOrConnecting())
		//			    {
		//			        //esta conectado a internet
		//			    	Intent intentCrearGPS = new Intent(this, CrearIncidenteGPS.class);
		//			    	startActivity(intentCrearGPS);
		//			    }
		//			    
		//			    else 
		//			    {
		//			    	Intent intetCrear = new Intent(this, CrearIncidenteBarrios.class);
		//			    	startActivity(intetCrear);
		//			    }
		//	        case R.id.ver : 
		//	        	//crear intent y start 
		//	    		Intent intetConsultar = new Intent(this, ConsultarIncidentes.class);
		//	    		startActivity(intetConsultar);
		//	    }
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==1)
		{
			Editor editor = sharedpreferences.edit();
			editor.putString(CentroIncidentes.prefNombre, data.getStringExtra("nombre"));
			editor.putBoolean(CentroIncidentes.prefPrimeraVez, true);
			editor.putInt(CentroIncidentes.prefEdad, data.getIntExtra("edad", 0));
			editor.putBoolean(CentroIncidentes.prefBorracho, false);
			editor.commit();

			setContentView(R.layout.activity_inicio);
		}
	}
	

    /**
     * function to load map. If map is not created it will create it for you
     * */
    private void initilizeMap() 
    {
    	mContext = getApplicationContext();
    	try 
    	{
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);       
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) 
            {
                // No network provider is enabled
            } 
            else 
            {
                this.canGetLocation = true;
                if (isGPSEnabled) 
                {
                    if (location == null) 
                    {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) 
                        {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        }
                    }
                }
                else if (isNetworkEnabled) 
                {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                    Log.d("Network", "Network");
                    if (locationManager != null)
                    {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

            gMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapInicio)).getMap();
            myLocation = new LatLng(location.getLatitude(),location.getLongitude());
            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 11));
            gMap.setMyLocationEnabled(true);
            
    }
    
    @Override
    public void onLocationChanged(Location location) 
    {
      myLocation = new LatLng(location.getLatitude(), location.getLongitude());
      CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(myLocation, 11);
      gMap.animateCamera(cameraUpdate);
      locationManager.removeUpdates(this);
    }
    
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {		
	}

	@Override
	public void onProviderEnabled(String provider) {
		 Toast.makeText(this, "Enabled new provider " + provider, Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		 Toast.makeText(this, "Disabled provider " + provider, Toast.LENGTH_SHORT).show();
		
	}

	public void crearIncidente (View view)
	{
		//Verifica si el dispositivo esta conectado 
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting())
		{
			//esta conectado a internet
			Intent intentCrearGPS = new Intent(this, CrearIncidenteGPS.class);
			startActivity(intentCrearGPS);
		}

		else
		{
			Intent intetCrear = new Intent(this, CrearIncidenteBarrios.class);
			startActivity(intetCrear);
		}
	}
	public void consultarIncidente (View view)
	{
		Intent intetConsultar = new Intent(this, ConsultarIncidentes.class);
		startActivity(intetConsultar);
	}
	public void consultarIncidentePorAmigos (View view)
	{

	}
	public void ajustes (View view)
	{

	}

}
