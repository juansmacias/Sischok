package uniandes.sischok;

import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestFactory;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
	
	private static final String PLACES_SEARCH_URL =  "https://maps.googleapis.com/maps/api/place/search/json?";
	private static final String PLACES_SEARCHTEXT_URL =  "https://maps.googleapis.com/maps/api/place/textsearch/output?parameters";
	
	private Context mContext;
	// Google Map
    private GoogleMap gMap;
    private Location location;
    protected LocationManager locationManager;
    private Location initialLocation;
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
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            
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
//                            initialLocation = new LatLng(latitude, longitude);
                            initialLocation = new Location("inicial");
                            initialLocation.setLatitude(latitude);
                            initialLocation.setLongitude(longitude);
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
//                                initialLocation = new LatLng(latitude, longitude);
                                initialLocation = new Location("inicial");
                                initialLocation.setLatitude(latitude);
                                initialLocation.setLongitude(longitude);
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
            
//            HttpRequestFactory httpRequestFactory = createRequestFactory(transport);
//            HttpRequest request = httpRequestFactory.buildGetRequest(new GenericUrl(PLACES_SEARCH_URL));
//            request.url.put("key", "");
//            request.url.put("location", latitude + "," + longitude);
//            request.url.put("radius", 500);
//            request.url.put("sensor", "false");
//            infowindow = new google.maps.InfoWindow();
            
//            var service = new google.maps.places.PlacesService(gMap);
//            service.nearbySearch(request, callback);
    }
 

    public void insertarIncidente(LatLng point)
    {
    	Intent intentCrearIncidenteLatLng = new Intent(this, CrearIncidenteConLatLng.class);
    	intentCrearIncidenteLatLng.putExtra("IncidenteLatLng", point.latitude+","+point.longitude );
    	startActivity(intentCrearIncidenteLatLng);
    }

    @Override
    public void onLocationChanged(Location location) 
    {
      latitude = (int) (location.getLatitude());
      longitude = (int) (location.getLongitude());
      LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
      CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
      gMap.animateCamera(cameraUpdate);
      locationManager.removeUpdates(this);
      
//      float[] results = new float[1];
//      Location.distanceBetween(initialLocation.latitude, initialLocation.longitude, latLng.latitude, latLng.longitude, results);
      float distance = initialLocation.distanceTo(location);
      if(distance > 12)
      {
    	  
    	  NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
      	//para que al notificar abra la app en la pagina principal
      	Intent notificationIntent = new Intent (mContext, Inicio.class );
      	notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
      	            | Intent.FLAG_ACTIVITY_SINGLE_TOP);
      	// prepare intent which is triggered if the notification is selected
      	PendingIntent pIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
      	Notification n  = new Notification.Builder(this)
          .setContentTitle("Incidentes de la nueva zona")
          .setContentText("Ha avanzado: "+ distance)
          .setSmallIcon(R.drawable.icono)
          .setContentIntent(pIntent)
          .setAutoCancel(true).build();
  //   NO     .addAction(R.drawable.icon, "Call", pIntent)
//       NO   .addAction(R.drawable.icon, "More", pIntent)
//         NO .addAction(R.drawable.icon, "And more", pIntent).build();
//      		Notification n = builder.build();A
      	notificationManager.notify(0, n);

      	//easy way
      	Toast.makeText(getApplicationContext(), 
                  "Revisar Incidentes de la zona", Toast.LENGTH_LONG).show();
      	
      	//Cambia el inicial al actual
      	initialLocation.setLatitude(latitude);
      	initialLocation.setLongitude(longitude);
      }
    }
    
    public double CalculationByDistance(Location startP, Location endP) 
    {
        int Radius=6371;//radius of earth in Km  
        double lat1 = startP.getLatitude();
        double lat2 = endP.getLatitude();
        double lon1 = startP.getLongitude();
        double lon2 = endP.getLongitude();
        double dLat = Math.toRadians(lat2-lat1);
        double dLon = Math.toRadians(lon2-lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
        Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.asin(Math.sqrt(a));
//        double valueResult= Radius*c;
//        double km=valueResult/1;
//        DecimalFormat newFormat = new DecimalFormat("####");
//        int kmInDec =  Integer.valueOf(newFormat.format(km));
//        double meter=valueResult%1000;
//        double meterInDec= Integer.valueOf(newFormat.format(meter));
//        Log.i("Radius Value",""+valueResult+"   KM  "+kmInDec+" Meter   "+meterInDec);

        return Radius * c;
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
