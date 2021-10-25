package app.representations.constraints;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import app.planning.State;
import app.representations.*;

public class Rule implements Constraint {
	protected Set<RestrictedDomain> premisse;
	protected Set<RestrictedDomain> conclusion;

	public int nbF;
	public int nbFH;

	/**
	 * Constructeur de la classe Rule
	 * une premisse est sensee impliquer la conclusion
	 * @param premisse conjonction entre des RestrictedDomain
	 * @param conclusion disjonction entre des RestrictedDomain
	 */
	public Rule(Set<RestrictedDomain> premisse, Set<RestrictedDomain> conclusion) {
		nbF = 0;
		this.premisse = premisse;
		this.conclusion = conclusion;
	}

	public Rule() {
		this(new HashSet<RestrictedDomain>(), new HashSet<RestrictedDomain>());
	}

	public void addInPremisse(RestrictedDomain rd) {
		this.premisse.add(rd);
	}
	public void addInPremisse(Variable variable, String ...values) {
		addInPremisse(new RestrictedDomain(variable, values));
	}

	public void addInConclusion(RestrictedDomain rd) {
		this.conclusion.add(rd);
	}
	public void addInConclusion(Variable variable, String ...values) {
		addInConclusion(new RestrictedDomain(variable, values));
	}

	// FONCTIONS

	@Override
	public Set<Variable> getScope() {
		Set<Variable> scope = new HashSet<Variable>();
		for(RestrictedDomain rd : this.premisse) {
			scope.add(rd.getVariable());
		}
		for(RestrictedDomain rd : this.conclusion) {
			scope.add(rd.getVariable());
		}
		return scope;
	}

	@Override
	public boolean isSatisfiedBy(State state) {
		boolean is_sat_premisse, is_sat_conclusion;
		is_sat_premisse = true;
		is_sat_conclusion = false;

		for(Map.Entry<Variable, String> assignation : state.getAssignations().entrySet()) {
			for(RestrictedDomain dr : this.premisse) {
				if(assignation.getKey().equals(dr.getVariable())) {
					is_sat_premisse &= (dr.getSubdomain().getValues().contains(assignation.getValue()));
				}
			}
		}

		for(Map.Entry<Variable, String> assignation : state.getAssignations().entrySet()) {
			for(RestrictedDomain dr : this.conclusion) {
				if(assignation.getKey().equals(dr.getVariable())) {
					is_sat_conclusion |= (dr.getSubdomain().getValues().contains(assignation.getValue()));
				}
			}
		}

		return (is_sat_premisse && (!is_sat_conclusion)) ? false : true;
	}
	
	// ===============================================
	// ============ FILTER ===========================

	@Override
	public boolean filter(List<RestrictedDomain> unassignedVariables, State state) {
		boolean filter = false;
		boolean filterables = true;
		boolean filterable = false;

		for(RestrictedDomain rd : this.conclusion){
			filterable = false;
			for (Entry<Variable, String> assignation : state.getAssignations().entrySet()) {
				// si la variable de la conclusion a deja ete affectee dans l'etat
				if(rd.getVariable() == assignation.getKey()) {
					// si la valeur affectée induit à une conclusion fausse
					if(!rd.getSubdomain().getValues().contains(assignation.getValue())) {
						filterable = true;
					}

				}
			}
			filterables &= filterable;
		}

		filterables &= filterable;

		if(filterables) {
			for(RestrictedDomain rdprem : this.premisse) {
				for(RestrictedDomain rduna : unassignedVariables) {
					if(rdprem.getVariable() == rduna.getVariable()) {

						// on enleve les valeurs de la conclusions aux sous domaines des variables non affectés
						if(!this.isSatisfiedBy(state)) {
							this.nbF++;
							rduna.getSubdomain().getValues().removeAll(rdprem.getSubdomain().getValues());

						}

					}
				}
			}
		}

		List<RestrictedDomain> rdremove = new ArrayList<RestrictedDomain>();
		for(RestrictedDomain rd : unassignedVariables) {
			if(rd.getSubdomain().getValues().size() == 1) {
				for(String value : rd.getSubdomain().getValues()) {
					state.addAffectedVariable(rd.getVariable(), value);
				}
				rdremove.add(rd);
			}
		}

		unassignedVariables.removeAll(rdremove);
		
		return filter;
	}
	
	// ===============================================
	// ===============================================

	// GETs

	public Set<RestrictedDomain> getPremisse() {
		return premisse;
	}

	public Set<RestrictedDomain> getConclusion() {
		return conclusion;
	}

	// Representation

	@Override
	public String toString() {
		String str = "";
		for(RestrictedDomain rd : this.premisse) {
			str+= rd.toString() + " && ";
		}
		str = str.substring(0, str.length()-4) + " -> ";
		for(RestrictedDomain rd : this.conclusion) {
			str+= rd.toString() + " || ";
		}
		str = str.substring(0, str.length()- 4);
		return str;
	}
	public int getnb() {
		return this.nbF;
	}

	@Override
	public int getnbIssatFilter() {
		return this.nbF;
	}
}
