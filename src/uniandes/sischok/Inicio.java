package uniandes.sischok;

import java.util.List;
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
import android.database.MatrixCursor;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import uniandes.sischok.R;
import uniandes.sischok.mundo.CentroEventos;
import uniandes.sischok.mundo.CentroIncidentes;
import uniandes.sischok.mundo.Incidente;



public class Inicio extends Activity {
	
	 private SharedPreferences sharedpreferences;
	 private SimpleCursorAdapter mAdapter;
	
	@SuppressWarnings({ "rawtypes", "deprecation" })
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
		List ultimosIncidentes = centroI.darUltimos5Incidentes();
		String[] columns = new String[] { "_id","titulo", "id"};
		MatrixCursor matrixCursor= new MatrixCursor(columns);
		startManagingCursor(matrixCursor);
		for (int i = 0; i < ultimosIncidentes.size(); i++ ) {
			Incidente inc = (Incidente) ultimosIncidentes.get(i);
			matrixCursor.addRow(new Object[] { i,inc.getTitulo(), inc.getUsuarioCreacion()});
		}
		int[] toViews = {android.R.id.edit, android.R.id.text1,android.R.id.text2};
		mAdapter = new SimpleCursorAdapter (this,android.R.layout.simple_list_item_2, matrixCursor,columns, toViews, 0);
		ListView lstUltimo = (ListView) findViewById(R.id.lstNuevosIncidentes);
		lstUltimo.setAdapter(mAdapter);
		mAdapter.swapCursor(matrixCursor);
		lstUltimo.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		      @Override
		      public void onItemClick(AdapterView<?> parent, final View view,
		          int position, long id) {
		    		Intent intentD = new Intent(Inicio.this, DetalleIncidente.class);
		    		intentD.putExtra("titulo", ((TextView)view.findViewById(android.R.id.text1)).getText());
		    		intentD.putExtra("Usuario", ((TextView)view.findViewById(android.R.id.text2)).getText());
		    		startActivity(intentD);
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
			editor.putString(CentroIncidentes.prefNombre, data.getStringExtra("nombre"));
			editor.putBoolean(CentroIncidentes.prefPrimeraVez, true);
			editor.putInt(CentroIncidentes.prefEdad, data.getIntExtra("edad", 0));
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
