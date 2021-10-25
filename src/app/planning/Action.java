package app.planning;

import java.util.HashSet;
import java.util.Set;

import app.representations.Variable;

public class Action {
	protected String nom;
	protected Set<State> preconditions;
	protected Set<State> effets;

	public Action(String nom, Set<State> preconditions, Set<State> effets) {
		this.preconditions = preconditions;
		this.effets = effets;
	}

	public Action(String nom) {
		this(nom, new HashSet<State>(), new HashSet<State>());
	}
	public void addInPrecondition(Variable variable, String value) {
		State state = new State();
		state.addAffectedVariable(variable, value);
		this.preconditions.add(state);
	}
	public void addInEffets(Variable variable, String value) {
		State state = new State();
		state.addAffectedVariable(variable, value);
		this.effets.add(state);
	}

	// GETs

	public Set<State> getPreconditions() {
		return preconditions;
	}

	public String getNom() {
		return nom;
	}

	public Set<State> getEffets() {
		return effets;
	}

	// Representation

	@Override
	public String toString(){
		String rpz = "precondis: ";
		
		for(State state : this.preconditions) {
			rpz += state.toString() + "";
		}
		
		rpz+= "\neffets: ";
		for(State state : this.effets) {
			rpz += state.toString() + "";
		}
		
		return rpz;
	}

}
