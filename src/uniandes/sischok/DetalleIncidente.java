package uniandes.sischok;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;
import uniandes.sischok.mundo.DaoMaster;
import uniandes.sischok.mundo.DaoSession;
import uniandes.sischok.mundo.Incidente;
import uniandes.sischok.mundo.IncidenteDao;
import uniandes.sischok.mundo.DaoMaster.DevOpenHelper;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.widget.EditText;
import uniandes.sischok.mundo.IncidenteDao.Properties;

public class DetalleIncidente extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detalle_incidente);
		String usuarioInc =getIntent().getStringExtra("Usuario");	
		setTitle(getIntent().getStringExtra("titulo"));
		SharedPreferences sharedpreferences = getSharedPreferences(Inicio.nombrePreferencias, Context.MODE_PRIVATE);
		DevOpenHelper helperNuevo = new DaoMaster.DevOpenHelper(this, Inicio.nomdb, null);
		SQLiteDatabase db = helperNuevo.getWritableDatabase();
		DaoMaster daoMaster = new DaoMaster(db);
		DaoSession daoSession = daoMaster.newSession();
		IncidenteDao indicenteDao = daoSession.getIncidenteDao();
		QueryBuilder qb = indicenteDao.queryBuilder();
		qb.where(Properties.UsuarioCreacion.eq(usuarioInc),
		qb.and(Properties.Titulo.eq(getTitle()),null));		
		List listaInc = qb.list();
		Incidente inc = (Incidente) listaInc.get(0);
		EditText txtdesc = (EditText) findViewById(R.id.txtDetalleIncDescripcion);
		txtdesc.setText(inc.getDescripcion());
		EditText txtZona = (EditText) findViewById(R.id.txtDetalleIncZona);
		txtZona.setText(inc.getZona());
		EditText txtGravedad = (EditText) findViewById(R.id.txtDetalleIncGravedad);
		txtGravedad.setText(inc.getGravedad());
		EditText txtCreador = (EditText) findViewById(R.id.txtDetalleIncUsuario);
		txtCreador.setText(inc.getUsuarioCreacion());
		EditText txtfecha = (EditText) findViewById(R.id.txtDetalleIncFechaCreacion);
		txtfecha.setText(inc.getFechaCreacion().toString());
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detalle_incidente, menu);
		return true;
	}
	

}
