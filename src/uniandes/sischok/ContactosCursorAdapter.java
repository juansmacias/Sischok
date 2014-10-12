package uniandes.sischok;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class ContactosCursorAdapter extends CursorAdapter {

	private LayoutInflater mInflater;
	
	public ContactosCursorAdapter(Context context, Cursor c,int flags) {
		super(context, c,flags);
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public void bindView(View view, Context context, Cursor c) {
				TextView usuarioDes = (TextView) view.findViewById(R.id.lblUsuarioComConListview);
				usuarioDes.setText(c.getString(1));
				TextView sinTele = (TextView) view.findViewById(R.id.lblSinTeleComCon);
				try
					{
						if(!c.getString(2).equals(""))
						sinTele.setText(c.getString(2));
						else
						{
							sinTele.setText(context.getResources().getString(R.string.lblSinTelefono));
							sinTele.setTextColor(Color.RED);
						}
					}
				catch(Exception e)
					{
						
					}
	}

	@Override
	public View newView(Context context, Cursor c, ViewGroup viewGroup) {
		return mInflater.inflate(R.layout.doblelbllistview, viewGroup, false);
	}

}
