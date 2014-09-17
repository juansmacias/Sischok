package uniandes.sischok;

import java.util.List;

import uniandes.sischok.mundo.CentroIncidentes;
import uniandes.sischok.mundo.DaoMaster;
import uniandes.sischok.mundo.DaoMaster.DevOpenHelper;
import uniandes.sischok.mundo.DaoSession;
import uniandes.sischok.mundo.Incidente;
import uniandes.sischok.mundo.IncidenteDao;
import uniandes.sischok.mundo.IncidenteDao.Properties;
import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.widget.EditText;
import de.greenrobot.dao.query.QueryBuilder;

public class DetalleIncidente extends Activity {

	@SuppressWarnings("rawtypes")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detalle_incidente);
		String usuarioInc = getIntent().getStringExtra("Usuario");	
		setTitle(getIntent().getStringExtra("titulo"));
		DevOpenHelper helperNuevo = new DaoMaster.DevOpenHelper(this, CentroIncidentes.nomdb, null);
		SQLiteDatabase db = helperNuevo.getWritableDatabase();
		DaoMaster daoMaster = new DaoMaster(db);
		DaoSession daoSession = daoMaster.newSession();
		IncidenteDao indicenteDao = daoSession.getIncidenteDao();
		QueryBuilder qb = indicenteDao.queryBuilder();
		qb.and(Properties.UsuarioCreacion.eq(usuarioInc),Properties.Titulo.eq(getTitle()));		
		List listaInc = qb.list();
		Incidente inc = (Incidente) listaInc.get(0);
		EditText txtdesc = (EditText) findViewById(R.id.txtDetalleIncDescripcion);
		txtdesc.setText(inc.getDescripcion());
		EditText txtZona = (EditText) findViewById(R.id.txtDetalleIncZona);
		txtZona.setText(inc.getZona()+"");
		EditText txtGravedad = (EditText) findViewById(R.id.txtDetalleIncGravedad);
		txtGravedad.setText(inc.getGravedad()+"");
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
