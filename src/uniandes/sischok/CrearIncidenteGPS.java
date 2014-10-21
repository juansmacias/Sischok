package uniandes.sischok;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


public class CrearIncidenteGPS extends Activity implements LocationListener
{
	private Context mContext;
	// Google Map
    private GoogleMap gMap;
    private Location location;
    protected LocationManager locationManager;
//    private String provider;
    private LatLng myLocation;
//    private ArrayList<LatLng> lstLatLngs;
    // Flag for GPS status
    boolean isGPSEnabled = false;
    // Flag for network status
    boolean isNetworkEnabled = false;
    // Flag for GPS status
    boolean canGetLocation = false;
    double latitude; // Latitude
    double longitude; // Longitude
    
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_incidente_gps);
        try 
        {
            // Loading map
            initilizeMap();
 
        } catch (Exception e) {
            e.printStackTrace();
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
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
            
            // Getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // Getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) 
            {
                // No network provider is enabled
            } 
            else 
            {
                this.canGetLocation = true;
                if (isNetworkEnabled) 
                {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                    Log.d("Network", "Network");
                    if (locationManager != null)
                    {
//                        Criteria criteria = new Criteria();
//                        provider = locationManager.getBestProvider(criteria, false);
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) 
                        {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                // If GPS enabled, get latitude/longitude using GPS Services
                if (isGPSEnabled) 
                {
                    if (location == null) 
                    {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) 
                        {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null)
                            {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

            gMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            myLocation = new LatLng(latitude,longitude);
            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));
// 
//            // check if map is created successfully or not
//            if (gMap == null) 
//            {
//                Toast.makeText(getApplicationContext(),
//                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
//                        .show();
//            }
            gMap.setMyLocationEnabled(true);
//            Marker TP = gMap.addMarker(new MarkerOptions().position(myLocation).title("Estoy aca").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));

//            lstLatLngs = new ArrayList<LatLng>();
            gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() 
            {
            	@Override
            	public void onMapClick(final LatLng point) 
            	{
            		// TODO Auto-generated method stub
            		
            		 AlertDialog.Builder builder = new AlertDialog.Builder(CrearIncidenteGPS.this);
            		 builder.setMessage("Desea agregar un incidente en este lugar?")
            		        .setCancelable(false)
            		        .setPositiveButton("Si", new DialogInterface.OnClickListener() 
            		        {
            		            public void onClick(DialogInterface dialog, int id) 
            		            {
            		            	//Agregar el incidente
//            		            	lstLatLngs.add(point);
            		            	insertarIncidente(point);
//            		            	gMap.addMarker(new MarkerOptions().position(point));
            		            }
            		        })
            		        .setNegativeButton("No", new DialogInterface.OnClickListener() 
            		        {
            		            public void onClick(DialogInterface dialog, int id) 
            		            {
            		                 dialog.cancel();
            		            }
            		        });
            		 AlertDialog alert = builder.create();
            		 alert.show();
            		
            	}
            });
    }
 

    public void insertarIncidente(LatLng point)
    {
    	Intent intentCrearIncidenteLatLng = new Intent(this, CrearIncidenteConLatLng.class);
    	intentCrearIncidenteLatLng.putExtra("IncidenteLatLng", point.latitude+","+point.longitude );
    	startActivity(intentCrearIncidenteLatLng);
    }
    @Override
    public void onLocationChanged(Location location) {
      latitude = (int) (location.getLatitude());
      longitude = (int) (location.getLongitude());
//      latituteField.setText(String.valueOf(lat)); nuevo text view
//      longitudeField.setText(String.valueOf(lng));
//      myLocation = new LatLng(latitude,longitude);
//      gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
    }

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		 Toast.makeText(this, "Enabled new provider " + provider, Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		 Toast.makeText(this, "Disabled provider " + provider, Toast.LENGTH_SHORT).show();
		
	}

}
