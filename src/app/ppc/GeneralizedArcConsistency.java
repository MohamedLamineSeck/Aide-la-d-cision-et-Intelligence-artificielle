package app.ppc;

import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import app.representations.constraints.*;
import app.planning.State;
import app.representations.*;

/**
 * A class for enforcing generalized arc consistency to domains wrt to variables.
 * The algorithms are brute-force: for deciding whether a value is GAC wrt a constraint, at
 * worst all tuples in the Cartesian product of the variables in the scope of the
 * constraint are tested. 
 */
public class GeneralizedArcConsistency {

	protected Set<Constraint> constraints;

	public GeneralizedArcConsistency(Set<Constraint> constraints) {
		this.constraints = constraints;
	}

	/**
	 * Filters given domains by removing values which are not arc consistent
	 * with respect to a given set of constraints.
	 * @param constraints A set of constraints
	 * @param domains A map from (at least) the variables occurring in the scope of
	 * the constraint to domains
	 * @throws IllegalArgumentException if a variable occurring in the scope of some
	 * constraint is mapped to no domain
	 */
	public void enforceArcConsistency (List<RestrictedDomain> domains) {
		boolean hasChanged = true;
		while (hasChanged) {
			for (Constraint constraint: this.constraints) {
				hasChanged = GeneralizedArcConsistency.enforceArcConsistency(constraint, domains);
			}
		}
	}

	/**
	 * Filters given domains by removing values which are not arc consistent
	 * with respect to a given constraint.
	 * @param constraint A constraint
	 * @param domains A map from (at least) the variables occurring in the scope of
	 * the constraint to domains
	 * @return true if at least one domain has changed, false otherwise
	 * @throws IllegalArgumentException if a variable occurring in the scope of the
	 * constraint is mapped to no domain
	 */
	public static boolean enforceArcConsistency (Constraint constraint, List<RestrictedDomain> domains) {
		boolean hasChanged = false;
		
		// on boucle sur les variables de la contraintes (var)
		boolean isInDomain;
		
		for(Variable variable : constraint.getScope()) {
			isInDomain = false;
			Domain rd = null;;
			for(RestrictedDomain domain : domains) {
				if(domain.getVariable() == variable) {
					isInDomain = true;
					//rd = domain.getSubdomain().getCopy();
					rd = domain.getSubdomain();
					break;
				}
			}
			// si var n'est pas dans le domains donnée, alors erreurs
			if(!isInDomain) {
				throw new IllegalArgumentException("enforceArcConsistency: la variable " + variable + " n'est pas dans le domaine");
			}
			
			if(isInDomain) {
				for(String value : rd.getValues()) {
					// récupérer les valeurs de var dans domains 
					if ( ! GeneralizedArcConsistency.isConsistent(variable, value, constraint, domains) ) {
						// faites les modification nécéssaire
						rd.getValues().remove(value);
						// enlever toutes les valeurs non viables
						// en cas de changement
						// appel récusive sur le rest de domains
						return true;
					} else {
						return false;
					}
				}
				
			}
			
		}
		return false;
		
		

	}
	/**
	 * Decides whether a given value is arc consistent for a given variable wrt a given constraint
	 * and given domains.
	 * @param variable A variable
	 * @param value A value
	 * @param constraint A constraint
	 * @param domains A map from (at least) the variables occurring in the scope of
	 * the constraint to domains (except possibly the given variable)
	 * @return true if the given value is arc consistent for the given variable
	 * @throws IllegalArgumentException if a variable occurring in the scope of the
	 * constraint (except possibly the given variable) is mapped to no domain
	 */
	public static boolean isConsistent (Variable variable, String value, Constraint constraint, List<RestrictedDomain> domains) {
		Deque<Variable> unassignedVariables = new LinkedList<>(constraint.getScope());
		unassignedVariables.remove(variable);
		State partialAssignment = new State();
		partialAssignment.addAffectedVariable(variable, value);
		return GeneralizedArcConsistency.isConsistent(partialAssignment, unassignedVariables, constraint, domains);
	}

	// Helper method ===================================================

	protected static boolean isConsistent (State partialAssignment, Deque<Variable> unassignedVariables, Constraint constraint, List<RestrictedDomain> domains) {
		if (unassignedVariables.isEmpty()) {
			return constraint.isSatisfiedBy(partialAssignment);
		} else {
			Variable var = unassignedVariables.pop();

			boolean containsVar = false;

			for(RestrictedDomain domain : domains) {
				if(var == domain.getVariable()) {
					containsVar = true;
					break;
				}
			}

			if ( ! containsVar ) { // ajout containsVar dans RestrictedDomain
				throw new IllegalArgumentException("Variable " + var + " occurs in " + constraint + " but has no domain in " + domains);
			} else {
				// vérifier la consistance (satisfiabilité) par un appel récursif partialAssignment sur les variables du domains, sachant que le test d'arrêt est déjà fait

				return isConsistent(partialAssignment, unassignedVariables, constraint, domains);


				// et que si tout est vrai on retourne vrai

				// sinon on retourne false 

			}
		}
	}

}
