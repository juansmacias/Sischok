package uniandes.sischok;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.content.res.Resources;

import org.json.JSONArray;
import org.json.JSONObject;

import de.greenrobot.dao.query.QueryBuilder;

import uniandes.sischok.mundo.DaoMaster.DevOpenHelper;
import java.util.Calendar;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetFileDescriptor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import uniandes.sischok.R;
import uniandes.sischok.mundo.CentroEventos;
import uniandes.sischok.mundo.DaoMaster;
import uniandes.sischok.mundo.DaoSession;
import uniandes.sischok.mundo.Incidente;
import uniandes.sischok.mundo.IncidenteDao;
import uniandes.sischok.mundo.IncidenteDao.Properties;


public class Inicio extends Activity {
	
	 private SharedPreferences sharedpreferences;
	 private SQLiteDatabase db;
	 public static final String nombrePreferencias = "Preferencias";
	 public static final String prefPrimeraVez = "PrimeraVez";
	 public static final String prefNombre = "nombre";
	 public static final String prefEdad = "edad";
	 public static final String nomdb = "sischok-db";
	 public static final String strarchivoIncidentesBasicos = "IncidentesBasicos";
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inicio);
	    sharedpreferences = getSharedPreferences(nombrePreferencias, Context.MODE_PRIVATE);
		DevOpenHelper helperNuevo = new DaoMaster.DevOpenHelper(this, nomdb, null);
		db = helperNuevo.getWritableDatabase();
		DaoMaster daoMaster = new DaoMaster(db);
		DaoSession daoSession = daoMaster.newSession();
		IncidenteDao indicenteDao = daoSession.getIncidenteDao();
		if (sharedpreferences.contains(prefPrimeraVez))
	      {
			
	      }
		else
		{			
			//File archivoIncidentesBasicos = new File();
			try {
				String jsonIncidentes = "";
			    StringBuilder builder = new StringBuilder();
			    InputStream instream = getResources().openRawResource(R.raw.incidentesbasicos);
			    BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
		        while ((jsonIncidentes = reader.readLine()) != null) {
		          builder.append(jsonIncidentes);
		        }
		        jsonIncidentes = builder.toString();
		        reader.close();
		        JSONObject jObjIncidentes = new JSONObject(jsonIncidentes);
		        JSONArray jAryIncidentes = jObjIncidentes.getJSONArray("Incidentes");
		        for (int i = 0; i < jAryIncidentes.length(); i++) {
		        	JSONObject jIncidente = (JSONObject)jAryIncidentes.get(i);
		        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		        	Date fechap = sdf.parse(jIncidente.getString("fechaCreacion"));
		        	Incidente inciactual = new Incidente(null, jIncidente.getString("titulo"), jIncidente.getString("descripcion"), Integer.parseInt(jIncidente.getString("zona")), Integer.parseInt(jIncidente.getString("gravedad")),fechap, jIncidente.getString("usuarioCreacion"));
		        	indicenteDao.insert(inciactual);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			Intent intentBienvenida = new Intent(this, Bienvenida.class);
			startActivityForResult(intentBienvenida,1);
		}
		QueryBuilder qb = indicenteDao.queryBuilder();
		qb.limit(5);
		qb.orderDesc(Properties.FechaCreacion);
		List ultimosIncidentes = qb.list();
		ArrayList<String> arryTitulosI = new ArrayList<String>();
		for (int i = 0; i < ultimosIncidentes.size(); i++) {
			Incidente incidenteActual = (Incidente)ultimosIncidentes.get(i);
			arryTitulosI.add(incidenteActual.getTitulo());
		}
		ListView listIn = (ListView) findViewById(R.id.lstNuevosIncidentes);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, arryTitulosI);
		listIn.setAdapter(adapter);
		listIn.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		      @Override
		      public void onItemClick(AdapterView<?> parent, final View view,
		          int position, long id) {
		       
		      }

		    });
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
	
	public void crearIncidente (View view)
	{
		Intent intetCrear = new Intent(this, CrearIncidenteBarrios.class);
		startActivity(intetCrear);
	}
	public void consultarIncidente (View view)
	{
		
	}
	public void consultarIncidentePorAmigos (View view)
	{
		
	}
	public void ajustes (View view)
	{
		
	}

}
