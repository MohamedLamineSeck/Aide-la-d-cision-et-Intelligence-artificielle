package app.representations.constraints;

import java.util.Deque;
import java.util.List;
import java.util.Set;

import app.planning.State;
import app.representations.*;

public interface Constraint {

	/**
	 * @return l'ensemble des variables contenus dans la regle
	 */
	public Set<Variable> getScope();
	
	/**
	 * @param state un etat
	 * @return vrai ou faux si cette etat est satisfait par la regle
	 */
	public boolean isSatisfiedBy(State state);
	
	/**
	 * @param unassignedVariables les variables non assignés déduit de leurs variables non viables
	 * @param state un etat donné
	 * @return vrai ou faix si un filtrage a été effecté
	 */
	public boolean filter(List<RestrictedDomain> unassignedVariables, State state) ;

	public int getnbIssatFilter();
}
