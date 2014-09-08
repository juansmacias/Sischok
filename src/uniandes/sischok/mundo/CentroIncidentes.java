package uniandes.sischok.mundo;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import uniandes.sischok.mundo.Incidente;

public class CentroIncidentes {

	//-------------------Atributos-------------------------------
	
	
	private static CentroIncidentes instancia;
	
	private ArrayList<Incidente> incidentesLocales;
	
	
	//-------------------Constructor-----------------------------
	
	public CentroIncidentes(Context contexto)
	{
		incidentesLocales = new ArrayList<Incidente>();
	}
	
	//-------------------Metodos---------------------------------
	
	public static CentroIncidentes darInstancia(Context contexto)
	{
		if(instancia==null)
		{
			instancia = new CentroIncidentes(contexto);
		}
		return instancia;
	}
	
	public Boolean registarIncidentes (String strTitulo,String strDescripcion, String strUsuario, int intZona,int intGravedad)
	{
		incidentesLocales.add(new Incidente(strTitulo, strDescripcion, strUsuario, intZona, intGravedad));		
		return true;
	}
	
	
	/**
	 * Metodo para Consultar los incidentes Por Zonas
	 * @param zonas Codigos de zonas que se quiere consultar en formato json.
	 * @return Se retorna un arreglo de Incidentes en formato json
	 */
	public String consultarIncidentesPorZonas(String zonas)
	{
		JSONObject jObjZonas;
		JSONArray arrIncidentes = new JSONArray();
		try {
		jObjZonas = new JSONObject(zonas);
		int contZ = Integer.parseInt(jObjZonas.getString("Tamano"));
		for(int i =0;i<contZ;i++)
		{
			int zonaAcual = Integer.parseInt(jObjZonas.getString("Zona"+i));
			Incidente incidenteActual= darIncidentesPorZona(zonaAcual);
			JSONObject jIncidenteActaul = new JSONObject();
			jIncidenteActaul.put("titulo", incidenteActual.darTitulo());
			arrIncidentes.put(jIncidenteActaul);
		}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return arrIncidentes.toString();

	}
	
	private Incidente darIncidentesPorZona(int zona)
	{
		return null;
	}
	
	public static Boolean compartirIncidente(String textoMensaje, String UsuariosDestino)
	{
		return false;
	}
}
