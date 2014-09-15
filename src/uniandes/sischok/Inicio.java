package uniandes.sischok;

import java.util.Calendar;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;

import uniandes.sischok.R;
import uniandes.sischok.mundo.CentroEventos;


public class Inicio extends Activity {
	
	 SharedPreferences sharedpreferences;
	 public static final String nombrePreferencias = "Preferencias";
	 public static final String prefPrimeraVez = "PrimeraVez";
	 public static final String prefNombre = "nombre";
	 public static final String prefEdad = "edad";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    sharedpreferences = getSharedPreferences(nombrePreferencias, Context.MODE_PRIVATE);

		if (sharedpreferences.contains(prefPrimeraVez))
	      {
			setContentView(R.layout.activity_inicio);
	      }
		else
		{
			Intent intentBienvenida = new Intent(this, Bienvenida.class);
			startActivityForResult(intentBienvenida,1);
		}

		
		Intent intent = new Intent(this, CentroEventos.class);
		startService(intent);
		
		// TODO: If exposing deep links into your app, handle intents here.
		//crea el intent de las notificaciones
		
		 // Start service Centro Eventos using AlarmManager

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 10);
       
        PendingIntent pintent = PendingIntent.getService(this, 0, intent, 0);
       
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //for 30 mint 60*60*1000
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                     60*60*1000, pintent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.inicio, menu);
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==1)
		{
			Editor editor = sharedpreferences.edit();
			editor.putString(prefNombre, data.getStringExtra("name"));
			editor.putBoolean(prefPrimeraVez, true);
			editor.putInt(prefEdad, data.getIntExtra("edad", 0));
			editor.commit();
			setContentView(R.layout.activity_inicio);
		}
	}

}
