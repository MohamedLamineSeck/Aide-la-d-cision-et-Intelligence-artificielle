package app.datamining;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import app.representations.*;
import app.representations.constraints.*;

public class AssociationRuleMiner {
	protected Map<Set<Variable>, Integer> itemsets;

	/**
	 * Constructeur de la classe AssociationRuleMiner
	 * @param itemsets
	 */
	public AssociationRuleMiner(Map<Set<Variable>, Integer> itemsets) {
		this.itemsets = itemsets;
	}
	
	public Set<Constraint> findRules(Double frequenceMin, Double confianceMin){
		Set<Constraint> rules = new HashSet<Constraint>();
		
		return rules;
	}
	
	// GET

	public Map<Set<Variable>, Integer> getItemsets() {
		return itemsets;
	}

}
