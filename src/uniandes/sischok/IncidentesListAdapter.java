package uniandes.sischok;

import java.util.List;

import uniandes.sischok.mundo.Incidente;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.TextView;

public class IncidentesListAdapter extends ArrayAdapter<Incidente> {

	private List<Incidente> mlistInc;
	private Context mContext;


	public IncidentesListAdapter(Context mContextp, int layoutResourceIdp,List<Incidente> listIncp) {
		super(mContextp,layoutResourceIdp,listIncp); 
		mContext = mContextp;
	    mlistInc = listIncp;	    
	}
	
	@Override
	public int getCount() {
		
		return mlistInc.size();
	}

	@Override
	public Incidente getItem(int i) {
		return mlistInc.get(i);
	}

	@Override
	public long getItemId(int i) {
		return mlistInc.get(i).getId();
	}

	
	@Override
	public View getView(int posicion, View vista, ViewGroup pariente) {
		if(vista==null)
		{
			vista =  ((Activity) mContext).getLayoutInflater().inflate(R.layout.incidentelistview, pariente, false);
		}
		Incidente incActual = mlistInc.get(posicion);
		TextView lblTitulo = (TextView) vista.findViewById(R.id.lblSinTituloIncListView);
		lblTitulo.setText(incActual.getTitulo());
		lblTitulo.setTag(incActual.getId());
		TextView lblUsuario = (TextView) vista.findViewById(R.id.lblUsuarioIncListview);
		lblUsuario.setText(incActual.getUsuarioCreacion());
		return vista;
	}



}
