package app.datamining;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import app.planning.State;
import app.representations.*;

public class BooleanDatabase {
	protected List<Variable> variables;
	protected Set<State> transactions;
	
	/**
	 * Constrcuteur de la classe BooleanDatabase
	 * @param variables les variables
	 * @param transactions l'ensemble des etats connus
	 */
	public BooleanDatabase(List<Variable> variables, Set<State> transactions) {
		// on verifie que les variables ont bien un domaine booleen
		for(Variable variable : variables) {
			if(variable.getDomain().getTaille() != 2) {
				throw new IllegalArgumentException("BooleanDatabase: tentative de creation d'une base de donnees non booleenne");
			}
		}
		
		this.variables = variables;
		this.transactions = transactions;
		
	}
	
	/**
	 * Constructeur a partir d'une base de donnees pas forcement booleenne
	 * @param database une base de donnees non-booleenne
	 */
	public BooleanDatabase(Database database) {
		this(new ArrayList<Variable>(), new HashSet<State>());
		
		// on reconstitue notre liste de variable (variables)
		for (Variable var : database.getChamps()) {
			
			// si la variable possède un domaine non booleen
			if(var.getDomain().getValues().size() > 2) {
				// on construit de nouvelles variables booleennes a partir des valeurs de leur domaine non booleen
				for(String val : var.getDomain().getValues()) {
					this.variables.add(new Variable( var.getName()+"_"+val , new Domain("1", "0")));
					
				}
			}else {
				this.variables.add(var);
				
			}
		}
		
		// on reconstitue nos transactions
		for (State state : database.getDonnees()) {
			State stateTmp = new State();
			
			for(Map.Entry<Variable, String> assignation : state.getAssignations().entrySet()){
				
				// si la variable possède un domaine non booleen
				if(assignation.getKey().getDomain().getValues().size() > 2) {
					
					// on construit de nouvelles affectations pour qu'elles soient booleennes
					// cad affecter 1 à la variable de la valeur affectee en tant que non booleene
					// et 0 aux autres
					for(String value : assignation.getKey().getDomain().getValues()) {
						
						if(value == assignation.getValue()) {
							stateTmp.addAffectedVariable(getBooleanVariableFromNonBooleanVariable(assignation.getKey(), value),"1");
						}else {
							stateTmp.addAffectedVariable(getBooleanVariableFromNonBooleanVariable(assignation.getKey(), value),"0");
						}
						
					}
					
				}else {
					stateTmp.addAffectedVariable(assignation.getKey(), assignation.getValue());
				}
				
			
			}
			
			transactions.add(stateTmp);
		}
		
	}
	
	/**
	 * @param variable la variable non booleenne
	 * @param value une varleur parmis le domaine de la variable
	 * @return une variable booleenne
	 */
	public Variable getBooleanVariableFromNonBooleanVariable(Variable variable, String value) {
		String namevar = variable.getName() + "_" + value;
		for(Variable var : this.variables) {
			if(var.getName().equals(namevar)) {
				return var;
			}
		}
		return null;
	}
	// GETs

	public List<Variable> getVariables() {
		return variables;
	}

	public Set<State> getTransactions() {
		return transactions;
	}
	
	// Representation
	
	@Override
	public String toString() {
		String res = "{\n";
		
		for(State etat : this.transactions) {
			res += " "+etat.toString()+", \n";
		}
		
		return res.substring(0, res.length()-3) + "\n}\n";
	}
	
	public void display(int nbOccurences) {
		nbOccurences = nbOccurences < 1 ? this.transactions.size() : nbOccurences;
		
		String res = "{\n";
		
		int i = 1;
		for(State etat : this.transactions) {
			
			res += " "+etat.toString()+", \n";
			
			if(i == nbOccurences) {
				break;
			}
			i++;
		}
		
		res = res.substring(0, res.length()-3) + "\n}\n";
		
		System.out.println(res);
	}
	
}
