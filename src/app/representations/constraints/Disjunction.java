package app.representations.constraints;

import java.util.Deque;
import java.util.List;
import java.util.Map;

import app.planning.State;
import app.representations.*;

public class Disjunction extends Rule {

	@Override
	public boolean isSatisfiedBy(State state) {
		boolean estSat_premisse = true;
		boolean estSat_conclusion = false;
		
		for(Map.Entry<Variable, String> affectation : state.getAssignations().entrySet()) {
			for(RestrictedDomain dr : this.premisse) {
				if(affectation.getKey().equals(dr.getVariable())) {
					estSat_premisse |= (dr.getSubdomain().getValues().contains(affectation.getValue()));
				}
			}
		}
		
		for(Map.Entry<Variable, String> affectation : state.getAssignations().entrySet()) {
			for(RestrictedDomain dr : this.conclusion) {
				if(affectation.getKey().equals(dr.getVariable())) {
					estSat_conclusion |= (dr.getSubdomain().getValues().contains(affectation.getValue()));
				}
			}
		}
		return (estSat_premisse && (!estSat_conclusion)) ? false : true;
	}
	
	// Representation
	
	@Override
	public String toString() {
		String str = "";
		for(RestrictedDomain dr : this.premisse) {
			str+= dr + " || ";
		}
		str = str.substring(0, str.length()-4) + " -> ";
		for(RestrictedDomain dr : this.conclusion) {
			str+= dr + " || ";
		}
		str = str.substring(0, str.length()- 4);
		return str;
	}
	
	@Override
	public boolean filter(List<RestrictedDomain> unassignedVariables, State state) {
		return false;
	}
}
