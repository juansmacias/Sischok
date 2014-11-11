package uniandes.sischok.mundo;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;



public class DetectarBorrachosService extends IntentService implements SensorEventListener{

    public static final String AVISAR_ACTION =
        "BROADCAST_AVISO";
    
    private boolean ya;
    private SensorManager objSensorManager;
    private Sensor sLuz;
	
	public DetectarBorrachosService() {
		super("detectarBorrachosService");
		ya= false;

	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		objSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sLuz = objSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
		objSensorManager.registerListener(this, sLuz, SensorManager.SENSOR_DELAY_NORMAL);
	}
	

	@Override
	protected void onHandleIntent(Intent arg0) {
//		if(!ya)
//		{
//	    	SharedPreferences sharedpreferences = getSharedPreferences(CentroIncidentes.nombrePreferencias, Context.MODE_PRIVATE);
//	    	Editor editor = sharedpreferences.edit();
//			editor.putBoolean(CentroIncidentes.prefBorracho, true);
//			editor.commit();
////			Intent avisarIntent = new Intent(AVISAR_ACTION);
////			LocalBroadcastManager.getInstance(this).sendBroadcast(avisarIntent);
//			ya =true;
//		    objSensorManager.unregisterListener(this);
//
//		}

	}

	@Override
	  public final void onAccuracyChanged(Sensor sensor, int accuracy) {
	    // Do something here if sensor accuracy changes.
	  }

	  @Override
	  public final void onSensorChanged(SensorEvent event) {
	    float lxLight = event.values[0];
	    Log.i("Sensores de luz","Medicion de luz: "+lxLight+" lux");
	    // Do something with this sensor data.
	  }
}
