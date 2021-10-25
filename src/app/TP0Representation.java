package app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.datamining.BooleanDatabase;
import app.datamining.DBReader;
import app.datamining.Database;
import app.datamining.FrequentItemsetMiner;
import app.examples.ExampleRepresentation;
import app.planning.State;
import app.representations.constraints.Constraint;

public class TP0Representation {

	public static void main(String[] args) {

		// ================================
		// === EXEMPLES DU FIL ROUGE ======
		// ================================
		
		List<String> dbNames = new ArrayList<String>(Arrays.asList(
				"db_b1_n1000_p01", 
				"db_b1_n5000_p01", 
				"db_b1_n10000_p00", 
				"db_b1_n10000_p01", 
				"db_b1_n10000_p02", 
				"db_b1_n10000_p03", 
				"db_b2_n10000_p00", 
				"db_b2_n10000_p01", 
				"db_b2_n10000_p02", 
				"db_b2_n10000_p03"
				));

		String path = "resources/databases/" + dbNames.get(1) + ".csv";

		ExampleRepresentation exemple = ExampleRepresentation.genereExemplesFilRouge();;
		System.out.println("\n=== REPRESENTATION EXEMPLES DU FILROUGE: \n\n" + exemple + "\n");

		DBReader dbr = new DBReader(exemple.getVariables());

		Database db = dbr.importDB(path);
		db.displayDatabase(40);

		BooleanDatabase bdb = new BooleanDatabase(db);
		//System.out.println(bdb);
		//bdb.display(23);

		//FrequentItemsetMiner fsm = new FrequentItemsetMiner(bdb);
		//System.out.println(fsm.frequentItemsets(42f));
		
		/////////////////////
		
		int nbSat = 0;
		boolean areSat;
		
		for(State state : db.getDonnees()) {
			
			areSat = true;
			
			for(Constraint constraint : exemple.getConstraints()) {
				areSat &= constraint.isSatisfiedBy(state);
			}
			
			if(areSat) {
				nbSat++;
			}
		}
		System.out.println(db.getDonnees().size());
		System.out.println("nombre de solutions trouvees: " + nbSat);
		
		int nbDoublons=0;
		
		for(State s1 : db.getDonnees()) {
			for(State s2 : db.getDonnees()) {
				if(s1.hashCode() != s2.hashCode()) {
					if(s1.equals(s2)) {
						nbDoublons++;
					}
				}
			}
		}
		
		System.out.println("nombre de doublons dans la bdd: " + nbDoublons);
		
	}

}
