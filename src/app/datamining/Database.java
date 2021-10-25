package app.datamining;

import java.util.List;
import java.util.Map;
import java.util.Set;

import app.planning.State;
import app.representations.*;


public class Database {
	private List<Variable> champs;
	private Set<State> donnees;

	/**
	 * Constructeur de la classe Database
	 * @param champs les variables contenues dans les donnees
	 * @param donnees des etats representant chacun un ensemble d'affectation des variables (champs)
	 */
	public Database(List<Variable> champs, Set<State> donnees) {
		this.champs = champs;
		this.donnees = donnees;
	}
	
	// GETs
	public List<Variable> getChamps() {
		return champs;
	}

	public Set<State> getDonnees() {
		return donnees;
	}
	
	// Representation

	@Override
	public String toString() {
		return "";
	}
	
	public void displayDatabase(int numberOfLines){
		int count = 0;
		for (Variable variable : champs) {
			System.out.print(variable.getName() + " ");
		}
		System.out.println();
		for (State state : donnees) {
			if(count > numberOfLines)
				break;
			//System.out.print(count + "> ");
			for(Map.Entry<Variable, String> assign : state.getAssignations().entrySet()){
				System.out.print(assign.getKey().getName()+"["+assign.getValue() + "] ");
			}
			System.out.println();
			count++;
		}
	}
}
