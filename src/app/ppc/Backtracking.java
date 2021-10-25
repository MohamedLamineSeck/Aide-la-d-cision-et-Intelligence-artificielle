package app.ppc;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import app.planning.State;
import app.representations.*;
import app.representations.constraints.*;

public class Backtracking {
	protected Set<Variable> scope;
	protected Set<Constraint> constraints;

	public int nbAppel; // test efficacité
	public int nbTest;

	public int nbAppelFiltre;// test efficacité filtre
	public int nbTestFiltre;

	public int nbAppelHeuristic;
	public int nbTestHeuristic;

	/**
	 * Constructeur de la classe Backtracking
	 * elle a pour but de pouvoir générée toutes les solutions possible satisfaisants des contraintes
	 * @param scope l'ensemble des variables contenues dans les contraintes
	 * @param constraints l'ensemble des contraintes
	 */
	public Backtracking(Set<Variable> variables, Set<Constraint> constraints) {
		this.scope = variables;
		this.constraints = constraints;
	}

	/**
	 * Constructeur qui genere de lui même la portee des contraintes
	 * @param constraints l'ensemble des contraintes
	 */
	public Backtracking(Set<Constraint> constraints) {
		this(new HashSet<Variable>(), constraints);

		// récupère les variables dans la portée de la contrainte
		for(Constraint c : this.constraints) {
			this.scope.addAll(c.getScope());
		}
	}

	// UTILE POUR LA SUITE: 

	/**
	 * @param state un ensemble d'affectations
	 * @return vrai ou faux si l'ensemble est satisfait
	 */
	public boolean areSatisfiedBy(State assignation) {
		boolean res = true;

		for(Constraint constraint : this.constraints) {
			res &= constraint.isSatisfiedBy(assignation);
		}

		return res;
	}

	// ======================================
	// ========== BACKTRACK =================

	public Set<State> allSolutions() {
		this.nbAppel=0;
		this.nbTest=0;

		// je commence avec une assignatiation partielle vide
		State partialAssignment = new State();

		// je considère que toutes les variables sont pas affectées pour l'instant 
		List<Variable> unassignedVariables = new ArrayList<Variable>();
		unassignedVariables.addAll(this.scope);

		// je garde une trace de toute solution que je recontre durant ma recherche
		Set<State> res = new HashSet<State>();

		this.solutions(partialAssignment, unassignedVariables, res);

		return res;
	}

	protected void solutions(State partialAssignment, List<Variable> unassignedVariables, Set<State> accumulator) {
		this.nbAppel++;

		if(unassignedVariables.isEmpty())// est-ce l'assignation partielle est complète ? Je ferai quoi si oui ?
		{
			this.nbTest++;

			// nombre de fois où l'on rentre ici correspond à la multipliaction entre le nombre de valeur possible que peut prendre une variable pour chaque domaine
			if(areSatisfiedBy(partialAssignment)) {
				accumulator.add(partialAssignment);
			}

		} else {
			// construire pas à pas ma solution partielle
			State stateTmp;
			Variable variableTmp = unassignedVariables.remove(0);

			for(String value : variableTmp.getDomain().getValues()) {
				stateTmp = partialAssignment.getCopy();
				stateTmp.addAffectedVariable(variableTmp, value);

				solutions(stateTmp, unassignedVariables, accumulator); // appel recursif
			}

			unassignedVariables.add(variableTmp);
		}
	}

	// =====================================
	// ========== FILTRAGE =================

	public Set<State> allSolutionsFilter() {
		this.nbAppelFiltre=0;
		this.nbTestFiltre=0;

		// je commence avec une assignatiation partielle vide
		State partialAssignment = new State();

		// je considère que toutes les variables sont pas affectées pour l'instant 
		List<RestrictedDomain> unassignedVariables = new ArrayList<RestrictedDomain>();
		for(Variable var : this.scope) {
			unassignedVariables.add(new RestrictedDomain(var));
		}

		// je garde une trace de toute solution que je recontre durant ma recherche
		Set<State> res = new HashSet<State>();

		this.solutionsFilter(partialAssignment, unassignedVariables, res);

		return res;
	}

	protected void solutionsFilter(State partialAssignment, List<RestrictedDomain> unassignedVariables, Set<State> accumulator) {
		this.nbAppelFiltre++;

		if(unassignedVariables.isEmpty())// est-ce l'assignation partielle est complète ? Je ferai quoi si oui ?
		{
			this.nbTestFiltre++;

			// nombre de fois où l'on rentre ici correspond à la multipliaction entre le nombre de valeur possible que peut prendre une variable pour chaque domaine
			if(areSatisfiedBy(partialAssignment)) {
				accumulator.add(partialAssignment);
			}

		} else {
			// construire pas à pas ma solution partielle
			State stateTmp;
			RestrictedDomain variableTmp = unassignedVariables.remove(0);
			List<RestrictedDomain> unassignedVariablesCopy;
			boolean filter;
			
			for(String value : variableTmp.getSubdomain().getValues()) {
				
				unassignedVariablesCopy = copyRestrictedDomainDeque(unassignedVariables);
				stateTmp = partialAssignment.getCopy();
				stateTmp.addAffectedVariable(variableTmp.getVariable(), value);
				
				filter = true;
				
				while(filter) {
					filter = false;
					for(Constraint constraint : this.constraints) {
						filter |= constraint.filter(unassignedVariablesCopy, stateTmp);
					}
				}
				
				
				if(!filter) {
					solutionsFilter(stateTmp, unassignedVariablesCopy, accumulator); // appel recursif
				}
				
			}

			unassignedVariables.add(variableTmp);
		}
	}
	
	public List<RestrictedDomain> copyRestrictedDomainDeque(List<RestrictedDomain> stack){
		List<RestrictedDomain> stackCopy = new ArrayList<RestrictedDomain>();
		
    	for(RestrictedDomain rd : stack) {
    		stackCopy.add(new RestrictedDomain(rd.getVariable(), rd.getSubdomain().getCopy()));
    	}
		return stackCopy;
    	
    }

	// ======================================
	// ========== HEURISTIC =================
	
	public Set<State> allSolutionsHeuristic() {
		this.nbAppelHeuristic=0;
		this.nbTestHeuristic=0;

		// je commence avec une assignatiation partielle vide
		State partialAssignment = new State();

		// je considère que toutes les variables sont pas affectées pour l'instant 
		List<Variable> unassignedVariables = new ArrayList<Variable>();
		unassignedVariables.addAll(this.scope);
		
		// je garde une trace de toute solution que je recontre durant ma recherche
		Set<State> res = new HashSet<State>();

		this.solutionsHeuristic(partialAssignment, unassignedVariables, res);

		return res;
	}
	
	/**
	 * Grosso modo cette fonction ne fait que tester isSatisfiedBy() à l'avance
	 * si une contrainte est verifiable afin d'arreter les appels recursifs si elle ne l'est pas
	 * 
	 * par soucis de temps et d'investissement cette partie est négligée
	 * 
	 * @param state
	 * @return
	 */
	public boolean heuristic(State state) {
		
		for(Constraint constraint : this.constraints) {
			
			if(state.getAssignations().keySet().containsAll(constraint.getScope())) {
				
				if(!constraint.isSatisfiedBy(state)) {
					return false;
				}
			}
		}
		return true;
	}
	
	protected void solutionsHeuristic(State partialAssignment, List<Variable> unassignedVariables, Set<State> accumulator) {
		this.nbAppelHeuristic++;

		if(unassignedVariables.isEmpty())// est-ce l'assignation partielle est complète ? Je ferai quoi si oui ?
		{
			this.nbTestHeuristic++;

			// nombre de fois où l'on rentre ici correspond à la multipliaction entre le nombre de valeur possible que peut prendre une variable pour chaque domaine
			if(areSatisfiedBy(partialAssignment)) {
				accumulator.add(partialAssignment);
			}

		} else {
			
			// construire pas à pas ma solution partielle
			State stateTmp;
			Variable variableTmp = unassignedVariables.remove(0);
			
			for(String value : variableTmp.getDomain().getValues()) {
				stateTmp = partialAssignment.getCopy();
				stateTmp.addAffectedVariable(variableTmp, value);
				
				if(heuristic(partialAssignment)) {
					solutionsHeuristic(stateTmp, unassignedVariables, accumulator); // appel recursif
				}
				
			}

			unassignedVariables.add(variableTmp);
		}
	}

	// ===============================================
	// ===============================================
	
	// GETs

	public Set<Variable> getScope() {
		return scope;
	}

	public Set<Constraint> getConstraints() {
		return constraints;
	}

	// Representation

	@Override
	public String toString() {
		String rpz = "PORTEE DES CONTRAINTES (Variables): \n{";

		for(Variable var : scope) {
			rpz += var.toString() + ", ";
		}

		rpz = rpz.substring(0, rpz.length()-2) + "}";
		rpz += "\n\nCONTRAINTES: \n{\n";

		for(Constraint c : constraints) {
			rpz += " (" + c.toString() + "), \n";
		}

		rpz = rpz.substring(0, rpz.length()-3) + "\n}";

		return rpz;
	}

}
