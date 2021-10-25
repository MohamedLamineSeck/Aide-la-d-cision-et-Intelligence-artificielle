package app;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import app.datamining.*;
import app.planning.State;
import app.representations.*;

public class TP4Datamining1 {

	/**
	 * Tests du TP Datamining 
	 * ce main() est basé sur les petits exemples du cours
	 * 
	 * @param args .
	 */
	public static void main(String[] args) {
		
		// DEFINITION DES VARIABLES
		
		Domain d = new Domain("0", "1");
		
		Variable v_chaussure = new Variable("chaussure", d);
		Variable v_chemise = new Variable("chemise", d);
		Variable v_veste = new Variable("veste", d);
		Variable v_jeans = new Variable("jeans", d);
		Variable v_sweatshirt = new Variable("sweat-shirt", d);
		
		// DEFINITION DES TRANSACTIONS
		
		State s1 = new State();
		s1.addAffectedVariable(v_chaussure, "1");
		s1.addAffectedVariable(v_chemise, "1");
		s1.addAffectedVariable(v_veste, "1");
		
		State s2 = new State();
		s2.addAffectedVariable(v_chaussure, "1");
		s2.addAffectedVariable(v_veste, "1");
		
		State s3 = new State();
		s3.addAffectedVariable(v_chaussure, "1");
		s3.addAffectedVariable(v_jeans, "1");
		
		State s4 = new State();
		s4.addAffectedVariable(v_chemise, "1");
		s4.addAffectedVariable(v_sweatshirt, "1");
		
		// DATAMINING

		List<Variable> variables = new ArrayList<Variable>(Arrays.asList(v_chaussure, v_chemise, v_veste, v_jeans, v_sweatshirt));
		Set<State> transactions= new HashSet<State>(Arrays.asList(s1,s2,s3,s4));
		
		BooleanDatabase db = new BooleanDatabase(variables, transactions);
		db.display(-1);
		
		FrequentItemsetMiner fsm = new FrequentItemsetMiner(db);
		System.out.println(fsm.frequentItemsets(51f));
		
		
	}

}
