package  uniandes.sischok.mundo;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import org.json.JSONObject;
import org.json.JSONException;
// KEEP INCLUDES END
/**
 * Entity mapped to table INCIDENTE.
 */
public class Incidente {

    private Long id;
    private String idServidor;
    /** Not-null value. */
    private String titulo;
    private String descripcion;
    private Integer zona;
    private Integer gravedad;
    private Double latitud;
    private Double longitud;
    private java.util.Date fechaCreacion;
    private String usuarioCreacion;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public Incidente() {
    }

    public Incidente(Long id) {
        this.id = id;
    }

    public Incidente(Long id, String idServidor, String titulo, String descripcion, Integer zona, Integer gravedad, Double latitud, Double longitud, java.util.Date fechaCreacion, String usuarioCreacion) {
        this.id = id;
        this.idServidor = idServidor;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.zona = zona;
        this.gravedad = gravedad;
        this.latitud = latitud;
        this.longitud = longitud;
        this.fechaCreacion = fechaCreacion;
        this.usuarioCreacion = usuarioCreacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdServidor() {
        return idServidor;
    }

    public void setIdServidor(String idServidor) {
        this.idServidor = idServidor;
    }

    /** Not-null value. */
    public String getTitulo() {
        return titulo;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getZona() {
        return zona;
    }

    public void setZona(Integer zona) {
        this.zona = zona;
    }

    public Integer getGravedad() {
        return gravedad;
    }

    public void setGravedad(Integer gravedad) {
        this.gravedad = gravedad;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public java.util.Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(java.util.Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    // KEEP METHODS - put your custom methods here
    
    @SuppressLint("SimpleDateFormat") 
    @Override
    public String toString() {
    	JSONObject jobjIncA = new JSONObject();
    			try {
    				jobjIncA.put("titulo", titulo);
    				jobjIncA.put("gravedad",gravedad);
    				jobjIncA.put("zona", zona);
    				jobjIncA.put("descripcion", descripcion);
    				jobjIncA.put("latitud",latitud);
    				jobjIncA.put("longitud", longitud);
    				SimpleDateFormat sdf = new SimpleDateFormat();
    				sdf.applyPattern("dd MMM yyyy HH:mm:ss");
    				jobjIncA.put("fechaCreacion", sdf.format(fechaCreacion));
    				jobjIncA.put("usuarioCreacion", usuarioCreacion);
    			} catch (JSONException e) {
    				e.printStackTrace();
    			}
    	return jobjIncA.toString();
    }
    
    @SuppressLint("SimpleDateFormat") 
    public static Incidente toIncidenteDeServidor (JSONObject jsonO)
    {
    	Incidente objInc= null;
    	try {
    		String date = jsonO.getString("fechaCreacion").replace('T', ' ');
    		date.replace(".000Z", "");
    		objInc = new Incidente( null, jsonO.getString("_id"),jsonO.getString("titulo"), jsonO.getString("descripcion"),  Integer.valueOf(jsonO.getInt("zona")),  Integer.valueOf(jsonO.getInt("gravedad")),  Double.valueOf(jsonO.getLong("latitud")),  Double.valueOf(jsonO.getLong("longitud")), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date), jsonO.getString("usuarioCreacion"));
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return objInc;
    }
    // KEEP METHODS END

}
