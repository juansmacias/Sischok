package uniandes.sischok;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class GeneradorDaos {

	    public static void main(String[] args) throws Exception {
	        Schema schema = new Schema(1000, "uniandes.sischok");
	        
	        AgregarInciente(schema);
	        
	        new DaoGenerator().generateAll(schema, "/Users/macbook/git/Sischok/src/uniandes/sischok/mundo");
	    }

	    private static void AgregarInciente(Schema schema) {
	        Entity incidentep = schema.addEntity("Incidente");
	        incidentep.addIdProperty();
	        incidentep.addStringProperty("titulo").notNull();
	        incidentep.addStringProperty("descripcion");
	        incidentep.addIntProperty("zona");
	        incidentep.addIntProperty("gravedad");
	        incidentep.addDateProperty("fechaCreacion");
	        incidentep.addStringProperty("usuarioCreacion");
	    }

	}
