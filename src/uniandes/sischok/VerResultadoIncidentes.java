package uniandes.sischok;

import java.util.List;

import uniandes.sischok.mundo.CentroIncidentes;
import uniandes.sischok.mundo.Incidente;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class VerResultadoIncidentes extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ver_resultado_incidentes);
		CentroIncidentes centroInc  = CentroIncidentes.darInstancia(this);
		List<Incidente> lstIncRsult = centroInc.consultarIncidentesPorZonas(getIntent().getStringExtra("zona"));
		ListView listIncResuConsulta = (ListView) findViewById(R.id.verIncConsultaZonasListView);
		IncidentesListAdapter mAdapt = new IncidentesListAdapter(this, R.layout.incidentelistview, lstIncRsult);
		listIncResuConsulta.setAdapter(mAdapt);
		listIncResuConsulta.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		      @Override
		      public void onItemClick(AdapterView<?> parent, final View view,
		          int position, long id) {
		    		Intent intentD = new Intent(VerResultadoIncidentes.this, DetalleIncidente.class);
		    		String strid = ((TextView) view.findViewById(R.id.lblSinTituloIncListView)).getTag()+"";
		    		Log.i("holi",strid);
		    		long lgid = Long.parseLong(strid);
		    		Log.i("holi long",lgid+"");
		    		intentD.putExtra("id", lgid);
		    		startActivity(intentD);
		      }

		    });
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ver_resultado_incidentes, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
