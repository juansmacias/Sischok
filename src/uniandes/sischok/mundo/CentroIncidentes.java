package uniandes.sischok.mundo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import de.greenrobot.dao.query.QueryBuilder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import uniandes.sischok.R;
import uniandes.sischok.mundo.Incidente;
import uniandes.sischok.mundo.DaoMaster.DevOpenHelper;
import uniandes.sischok.mundo.IncidenteDao.Properties;

public class CentroIncidentes {

	//-------------------Atributos-------------------------------
	
	public static final String nombrePreferencias = "Preferencias";
	public static final String prefPrimeraVez = "PrimeraVez";
	public static final String prefNombre = "nombre";
	public static final String prefEdad = "edad";
	public static final String nomdb = "sischok-db";
	public static final String strarchivoIncidentesBasicos = "IncidentesBasicos";
	private static CentroIncidentes instancia;	
	@SuppressWarnings("unused")
	private ArrayList<Incidente> incidentesLocales;
	private IncidenteDao incidenteDao;
	private Context contexto;
	
	
	
	//-------------------Constructor-----------------------------
	
	public CentroIncidentes(Context contexto)
	{
		incidentesLocales = new ArrayList<Incidente>();
		DevOpenHelper helperNuevo = new DaoMaster.DevOpenHelper(contexto, nomdb, null);
		 SQLiteDatabase db = helperNuevo.getWritableDatabase();
		DaoMaster daoMaster = new DaoMaster(db);
		DaoSession daoSession = daoMaster.newSession();
		incidenteDao = daoSession.getIncidenteDao();
		this.contexto = contexto; 
	}
	
	//-------------------Metodos---------------------------------
	
	public static CentroIncidentes darInstancia(Context contexto)
	{
		if(instancia==null)
		{
			instancia = new CentroIncidentes( contexto);
		}
		return instancia;
	}

	@SuppressLint("SimpleDateFormat") public void iniciarBasedeDatos()
	{
		try {
			String jsonIncidentes = "";
		    StringBuilder builder = new StringBuilder();
		    InputStream instream = contexto.getResources().openRawResource(R.raw.incidentesbasicos);
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
	        	Incidente inciactual = new Incidente(null, "", jIncidente.getString("titulo"), jIncidente.getString("descripcion"), Integer.parseInt(jIncidente.getString("zona")), Integer.parseInt(jIncidente.getString("gravedad")), Double.valueOf(jIncidente.getString("latitud")),Double.valueOf(jIncidente.getString("longitud")), fechap, jIncidente.getString("usuarioCreacion"));
	        	incidenteDao.insert(inciactual);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<Incidente> darUltimos5Incidentes()
	{
		QueryBuilder<Incidente> qb = incidenteDao.queryBuilder();
		qb.limit(5);
		qb.orderDesc(Properties.FechaCreacion);
		return qb.list();
		
	}
	
	public Incidente darIncidentePorId(long id)
	{
		QueryBuilder<Incidente> qb = incidenteDao.queryBuilder();
		qb.limit(1);
		qb.where(Properties.Id.eq(id));
		return qb.unique();
	}
	
	public Boolean registarIncidentes (String strTitulo,String strDescripcion, String strUsuario, int intZona,int intGravedad)
	{
//		incidentesLocales.add(new Incidente(strTitulo, strDescripcion, strUsuario, intZona, intGravedad ));		
		return true;
	}
	
	
	/**
	 * Metodo para Consultar los incidentes Por Zonas
	 * @param zonas Codigos de zonas que se quiere consultar en formato json.
	 * @return Se retorna un arreglo de Incidentes en formato json
	 */
	public List<Incidente> consultarIncidentesPorZonas(String zonas)
	{
		
		String[] zonasp = zonas.split("-");
		List<Integer> zonasInt = new ArrayList<Integer>();
		for (int i = 0; i < zonasp.length; i++) {
			zonasInt.add(Integer.decode(zonasp[0]));
		}
		QueryBuilder<Incidente> qb = incidenteDao.queryBuilder();
		qb.where(Properties.Zona.in(zonasInt));
		return qb.list();
	}
	
	public void crearIncidente (Incidente incidente)
	{
		incidenteDao.insert(incidente);
	}
	
	@SuppressWarnings("unused")
	private Incidente darIncidentesPorZona(int zona)
	{
		return null;
	}
	
//	JSONArray jArryIncs = new JSONArray();
//	for (int i = 0; i < ultimosIncidentes.size(); i++) {
//		JSONObject jobjIncA = new JSONObject();
//
//		try {
//			Incidente incA = (Incidente) ultimosIncidentes.get(i);
//			jobjIncA.put("titulo", incA.getTitulo());
//			jobjIncA.put("id", incA.getId());
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		jArryIncs.put(jobjIncA);
//		}
//	return jArryIncs.toString();
}
