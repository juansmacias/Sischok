package uniandes.sischok;

import java.util.Date;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.support.v4.app.NavUtils;
import uniandes.sischok.mundo.CentroIncidentes;
import uniandes.sischok.mundo.DaoMaster;
import uniandes.sischok.mundo.DaoSession;
import uniandes.sischok.mundo.Incidente;
import uniandes.sischok.mundo.IncidenteDao;
import uniandes.sischok.mundo.DaoMaster.DevOpenHelper;

public class CrearIncidenteDescripcion extends Activity {


	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crear_incidente_descripcion);
		// Show the Up button in the action bar.
		setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.crear_incidente_descripcion, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@SuppressWarnings("deprecation")
	public void crearIncidente (View view)
	{
		String titulo = ((EditText) findViewById(R.id.txtTitulo)).getText().toString();
		String descripcion = ((EditText) findViewById(R.id.txtDescripcion)).getText().toString();
		int gravedad = Integer.parseInt(((EditText) findViewById(R.id.txtGravedad)).getText().toString());
	    SharedPreferences sharedpreferences = getSharedPreferences(Inicio.nombrePreferencias, Context.MODE_PRIVATE);
	    if(!titulo.equals("")&&!descripcion.equals("")&&(gravedad<=5||gravedad>0))
	    	{
				Incidente indicienteNuevo = new Incidente(null, titulo, descripcion, getIntent().getIntExtra("zona",0), gravedad, new Date(), sharedpreferences.getString(Inicio.prefNombre, "Administrador Sischok"));
				DevOpenHelper helperNuevo = new DaoMaster.DevOpenHelper(this, Inicio.nomdb, null);
				SQLiteDatabase db = helperNuevo.getWritableDatabase();
				DaoMaster daoMaster = new DaoMaster(db);
				DaoSession daoSession = daoMaster.newSession();
				IncidenteDao indicenteDao = daoSession.getIncidenteDao();
				indicenteDao.insert(indicienteNuevo);
	    	}
	    else
	    {
			AlertDialog alertDialog = new AlertDialog.Builder(
					CrearIncidenteDescripcion.this).create();

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
