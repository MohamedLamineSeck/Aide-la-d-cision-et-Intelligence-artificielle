package app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.datamining.BooleanDatabase;
import app.datamining.DBReader;
import app.datamining.Database;
import app.datamining.FrequentItemsetMiner;
import app.examples.ExampleRepresentation;

public class TP4Datamining2 {
	
	/**
	 * Tests du TP Datamining 
	 * il se base sur les bases de donnees csv fournies
	 * @param args .
	 */
	public static void main(String[] args) {
		
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
		
		String path = "resources/databases/" + dbNames.get(0) + ".csv";
		
		ExampleRepresentation exemple = ExampleRepresentation.genereExemplesFilRouge();;
		System.out.println("\n=== REPRESENTATION EXEMPLES DU FILROUGE: \n\n" + exemple + "\n");
		
		DBReader dbr = new DBReader(exemple.getVariables());
		
		Database db = dbr.importDB(path);
		db.displayDatabase(40);
		
		BooleanDatabase bdb = new BooleanDatabase(db);
		//System.out.println(bdb);
		bdb.display(23);
		
		FrequentItemsetMiner fsm = new FrequentItemsetMiner(bdb);
		System.out.println(fsm.frequentItemsets(5f));
		System.out.println(bdb.getTransactions().size());

	}
}
