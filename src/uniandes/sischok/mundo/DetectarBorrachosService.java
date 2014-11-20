package uniandes.sischok.mundo;

import uniandes.sischok.VistaEbrio;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class DetectarBorrachosService extends IntentService implements SensorEventListener{

	public static final String AVISAR_ACTION = "BROADCAST_AVISO";
	//	private static final float CONVERSION_SEGUNDOS = 1.0f / 1000000000.0f;
	//	private static final float EPSILON = 0.000000001f;

	private boolean boolLuz;
	private boolean boolBorracho;
	private int contDiferencias;
	private SensorManager objSensorManager;
	private Sensor sLuz;
	private Sensor sAccelerometro;
	private float[] arrAcceleracion_Lineal = new float[3];
	private Sensor sGiroscopio;
	//	private float[] arrGiroscopio;
	//	private final float[] deltaRotationVector = new float[4];
	//	private float timestamp;



	public DetectarBorrachosService() {
		super("detectarBorrachosService");
		boolLuz= false;
		boolBorracho = false;

	}

	@Override
	public void onCreate() {
		super.onCreate();
		objSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sLuz = objSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
		objSensorManager.registerListener(this, sLuz, SensorManager.SENSOR_DELAY_NORMAL);
		sAccelerometro = objSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
		objSensorManager.registerListener(this, sAccelerometro, SensorManager.SENSOR_DELAY_NORMAL);
		sGiroscopio = objSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		objSensorManager.registerListener(this, sGiroscopio, SensorManager.SENSOR_DELAY_NORMAL);
	}


	@Override
	protected void onHandleIntent(Intent arg0) {
	}

	@Override
	public final void onAccuracyChanged(Sensor sensor, int accuracy) {
		// Do something here if sensor accuracy changes.
	}

	@Override
	public final void onSensorChanged(SensorEvent event) {
		String strNombre = event.sensor.getName();
		if(strNombre.equals(sLuz.getName()))
		{
			float lxLight = event.values[0];
			if(lxLight<=5)
				boolLuz=true;
			else
				boolLuz =false;
		}
		else if(boolLuz)
		{
			if(strNombre.equals(sAccelerometro.getName()))
			{
				if(!(arrAcceleracion_Lineal[0]==event.values[0] && arrAcceleracion_Lineal[1]==event.values[1] && arrAcceleracion_Lineal[2]==event.values[2]))
				{
					float diference = arrAcceleracion_Lineal[0] - event.values[0];
					if(Math.abs(diference)>4)
					{
						if(contDiferencias==6)
						{
							boolBorracho = true;
						}
						else
						{
							contDiferencias++;
						}
					}
					arrAcceleracion_Lineal[0] = event.values[0];
					arrAcceleracion_Lineal[1] = event.values[1];
					arrAcceleracion_Lineal[2] = event.values[2];	   
					Log.i("Sensor Accelerometro", "aceleracion en x: "+arrAcceleracion_Lineal[0]+" en y: "+arrAcceleracion_Lineal[1]+" en z: "+arrAcceleracion_Lineal[2]);
				}
			}
			//	else if(strNombre.equals(sGiroscopio.getName()))
			//	{
			//	float [] arrGiroscopioLocal = new float [3];
			//	arrGiroscopioLocal[0] = event.values[0];
			//	arrGiroscopioLocal[1] =event.values[1];
			//	arrGiroscopioLocal[2] = event.values[2];
			//	if (timestamp != 0) {
			//	final float dT = (event.timestamp - timestamp) * CONVERSION_SEGUNDOS;
			//	// Calculate the angular speed of the sample
			//	float omegaMagnitude = (float)Math.sqrt(arrGiroscopioLocal[0]*arrGiroscopioLocal[0] + arrGiroscopioLocal[1]*arrGiroscopioLocal[1] + arrGiroscopioLocal[2]*arrGiroscopioLocal[2]);
			//
			//	if (omegaMagnitude > EPSILON) {
			//	arrGiroscopioLocal[0] /= omegaMagnitude;
			//	arrGiroscopioLocal[1] /= omegaMagnitude;
			//	arrGiroscopioLocal[2] /= omegaMagnitude;
			//	}
			//
			//	float thetaOverTwo = omegaMagnitude * dT / 2.0f;
			//	float sinThetaOverTwo = (float) Math.sin(thetaOverTwo);
			//	float cosThetaOverTwo = (float) Math.cos(thetaOverTwo);
			//	deltaRotationVector[0] = sinThetaOverTwo * arrGiroscopioLocal[0];
			//	deltaRotationVector[1] = sinThetaOverTwo * arrGiroscopioLocal[1];
			//	deltaRotationVector[2] = sinThetaOverTwo * arrGiroscopioLocal[2];
			//	deltaRotationVector[3] = cosThetaOverTwo;
			//	}
			//	timestamp = event.timestamp;
			//	float[] deltaRotationMatrix = new float[9];
			//	SensorManager.getRotationMatrixFromVector(deltaRotationMatrix, deltaRotationVector);
			//	if(arrGiroscopio == null)
			//	{
			//	arrGiroscopio = new float[3];
			//	       SensorManager.getOrientation(deltaRotationMatrix, arrGiroscopio);
			//	       arrGiroscopio = matrixMultiplication(arrGiroscopioLocal, deltaRotationMatrix);
			//	Log.i("Sensor Giroscopio", "rotacion inicial en x: "+ arrGiroscopio[0]+" en y: "+ arrGiroscopio[1]+" en z: "+ arrGiroscopio[2]);
			//	}
			//
			//	else if(!(arrGiroscopio[0]*deltaRotationMatrix[0]>1&&arrGiroscopio[1]*deltaRotationMatrix[1]>1&&arrGiroscopio[2]*deltaRotationMatrix[2]>1))
			//	{
			//	arrGiroscopio[0] = arrGiroscopio[0]*deltaRotationMatrix[0];
			//	arrGiroscopio[1] = arrGiroscopio[1]*deltaRotationMatrix[1];
			//	arrGiroscopio[2] = arrGiroscopio[2]*deltaRotationMatrix[2];
			//	Log.i("Sensor Giroscopio", "rotacion en x: "+ arrGiroscopio[0]+" en y: "+ arrGiroscopio[1]+" en z: "+ arrGiroscopio[2]);
			//
			//	}
			//
			//	}
			if(boolBorracho)
			{
				SharedPreferences sharedpreferences = getSharedPreferences(CentroIncidentes.nombrePreferencias, Context.MODE_PRIVATE);
				Editor editor = sharedpreferences.edit();
				editor.putBoolean(CentroIncidentes.prefBorracho, true);
				editor.commit();
				//	Intent avisarIntent = new Intent(AVISAR_ACTION);
				//	LocalBroadcastManager.getInstance(this).sendBroadcast(avisarIntent);
				objSensorManager.unregisterListener(this);
				Intent intentBorracho = new Intent(this, VistaEbrio.class);
				startActivity(intentBorracho);
				this.onDestroy();
			}

		}
	}
}
