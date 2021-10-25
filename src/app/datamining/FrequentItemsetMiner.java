package app.datamining;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import app.planning.State;
import app.representations.*;

public class FrequentItemsetMiner {
	protected BooleanDatabase db;

	/**
	 * Constructeur de la classe FrequentItemsetMiner
	 * @param db
	 */
	public FrequentItemsetMiner(BooleanDatabase db) {
		this.db = db;

	}
	
	/**
	 * Apriori P:
	 * @param frequenceMin la frequence minimum des itemsets que l'on veut
	 * @return les itemssets et leur frequence repectant la frequence minimum
	 */
	public Map<Set<Variable>, Float> frequentItemsets(Float frequenceMin) {
		Map<Set<Variable>, Float> res = new HashMap<Set<Variable>,Float>();
		
		float support;
		String value;
		
		/*
		1) Dans la première itération de l'algorithme, 
		chaque élément est considéré comme un candidat d'itemsets. 
		L'algorithme comptera les occurrences de chaque élément.
		*/

		int nbVar = 1;
		Set<Set<Variable>> itemsets1 = new HashSet<Set<Variable>>();
		Set<Set<Variable>> itemsets2 = new HashSet<Set<Variable>>();
		Set<Set<Variable>> itemsetsTmp1 = new HashSet<Set<Variable>>();
		Set<Set<Variable>> itemsetsTmp2 = new HashSet<Set<Variable>>();
		Set<Variable> itemsetTmp;
		
		for(Variable variable : this.db.getVariables()) {
			itemsetTmp = itemset(variable);
			support = support(itemsetTmp);
			
			// 2) on ne conserve que les itemset ayant une occurence au moins égal à la frequence
			if(support >= frequenceMin) {
				res.put(itemset(variable), support);
				itemsets1.add(itemsetTmp); // utile pour la suite
			}
			
		}
		
		// 3) plus qu'à continuer sur le reste
		boolean continuer = true;
		
		while(continuer && (nbVar < getNbTransactions()) ) {
			itemsetsTmp1 = !itemsets1.isEmpty() ? itemsets1 : itemsets2;
			itemsetsTmp2 = itemsetsTmp1.equals(itemsets2) ? itemsets1 : itemsets2;
			itemsetsTmp2.clear();
			
			nbVar++;
			continuer = false;
			
			for(Variable variable : this.db.getVariables()) {
				
				for(Set<Variable> itemset : itemsetsTmp1) {
					if(!itemset.contains(variable)) {
						itemsetTmp = itemsetCopy(itemset);
						itemsetTmp.add(variable);
						itemsetsTmp2.add(itemsetTmp);
						support = support(itemsetTmp);
						if(support >= frequenceMin) {
							res.put(itemsetTmp, support);
							continuer=true;
						}
					}
						
						
				}
			}
			itemsetsTmp1.clear();

		}
		return res;
	}
	
	public Set<Variable> itemset(Variable ...variables){
		return new HashSet<Variable>(Arrays.asList(variables));
	}
	
	public Set<Variable> itemsetCopy(Set<Variable> itemset){
		Set<Variable> copy = new HashSet<Variable>();
		copy.addAll(itemset);
		return copy;
	}
	
	/**
	 * @param variables un itemset
	 * @return le nombre d'occurence de la variable à vraie dans les transactions
	 */
	public int nbOccurences(Set<Variable> variables) {
		boolean allTrue;
		int nb = 0;
		
		for(State transaction : db.transactions) {
			if(transaction.getScope().containsAll(variables)) {
				
				allTrue = true;
				
				for(Variable variable : variables) {
					allTrue &= (transaction.getAssignations().get(variable).equals("1")) ? true : false;
				}
				
				if(allTrue) {
					nb += 1;
				}
			}
		}
		
		return nb;
	}
	
	public Float support(Set<Variable> variables) {
		return ((float) nbOccurences(variables)/ (float) this.getNbTransactions()*100f);
	}
	
	public Float confidence(Set<Variable> a, Set<Variable> b) {
		Set<Variable> union = new HashSet<Variable>();
		union.addAll(a);
		union.addAll(b);
		return support(union)/support(a);
	}

	public int getNbTransactions() {
		return this.db.getTransactions().size();
	}
}
