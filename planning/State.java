package app.planning;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import app.representations.Variable;

public class State {
	protected Map<Variable,String> assignations;

	/**
	 * Constructeur de la classe State
	 * un etat represente par exemple les symptomes d'un patient
	 * @param assignations enssemble d'affectations
	 */
	public State(Map<Variable,String> assignations){
		this.assignations = assignations;
	}

	public State(){
		this(new HashMap<Variable,String>());
	}
	public void addAffectedVariable(Variable variable, String value){
		if(this.assignations.containsKey(variable)){
			System.err.println("La variable " + variable + " a deja ete affectee dans cette etat");
		}
		this.assignations.put(variable, value);
	}
	
	// ===== PLANNING
	
	
	// FONCTIONS
	
	/**
	 * @return la derniere affectation realisee
	 */
	public Entry<Variable,String> getLastAssignation(){
		Entry<Variable, String> lastAssignation = null;
		for (Entry<Variable, String> assignation : this.assignations.entrySet()) {
			lastAssignation = assignation;
		}
		return lastAssignation;
	}
	
	/**
	 * @return une copie de l'etat courant
	 */
	public State getCopy(){
		State copy = new State();
		for(Map.Entry<Variable, String> assignation : this.assignations.entrySet()){
			copy.addAffectedVariable(assignation.getKey(), assignation.getValue());
		}
		return copy;
	}
	
	/**
	 * @return l'ensemble des variables contenus dans l'etat
	 */
	public Set<Variable> getScope(){
		Set<Variable> scope = new HashSet<Variable>();
		for (Entry<Variable, String> assignation : this.assignations.entrySet()){
			scope.add(assignation.getKey());
		}
		return scope;
	}
	
	public boolean contientEtat(Collection<State> state){
		for(State etat: state){
			if(this.equals(etat))
				return true;
		}
		return false;
	}
	
	// GET
	
	public Map<Variable, String> getAssignations() {
		return assignations;
	}
	
	// Representation
	
	@Override
	public String toString() {
		String str = "{ ";
		for(Map.Entry<Variable, String> assign : this.assignations.entrySet()){
			str += "(" + assign.getKey().getName() + " = " + assign.getValue() + "), ";
		}
		str = str.substring(0, str.length()-2) + " }";
		return str;
	}
	
	@Override
	public boolean equals(Object obj) {
		State s = (State) obj;
		boolean res, test;
		
		res = s.getAssignations().size() == this.assignations.size();
		
		for(Map.Entry<Variable, String> assign : this.assignations.entrySet()){
			test = false;
			for(Map.Entry<Variable, String> assign2 : s.getAssignations().entrySet()){
				if(assign2.getKey() == assign.getKey() && assign2.getValue() == assign.getValue()) {
					test = true;
				}
			}
			res &= test;
		}
		
		return res;
	}
}
