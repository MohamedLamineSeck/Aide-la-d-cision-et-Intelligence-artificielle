package app;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import app.examples.ExampleRepresentation;
import app.planning.State;
import app.ppc.Backtracking;
import app.representations.constraints.*;
import app.representations.*;

public class TP2Backtracking1 {

	/**
	 * Tests du TP Backtracking
	 * tests basés sur l'exemple du fil rouge (symptomes médicaux)
	 * 
	 * 1) test du backtrack
	 * 2) test du backtrack avec filtrage
	 * 3) test du backtrack avec heuristique
	 * 
	 * @param args .
	 */
	public static void main(String[] args) {
		
		ExampleRepresentation exemple = ExampleRepresentation.genereExemplesFilRouge();;
		//System.out.println("=== BACKTRACKING: \n\n" + exemple + "\n");
		
		Backtracking backtrack;
		Set<State> solutions;
		Set<State> solutionsFilter;
		Set<State> solutionsHeuristique;

		//backtrack = new Backtracking(exemple.getVariables(),exemple.getConstraints());
		backtrack = new Backtracking(exemple.getConstraints());
		
		solutions = backtrack.allSolutions(); // backtracking
		solutionsFilter = backtrack.allSolutionsFilter(); // backtracking avec filtre
		solutionsHeuristique = backtrack.allSolutionsHeuristic(); // backtracking avec heuristique
		
		
		System.out.println("== BACKTRACK SUR: \n\n" + backtrack.toString() + "\n");
		
		double nbTestTheorique = Math.pow(2, 10) * Math.pow(3, 2);
		System.out.println(""
				+ "Dans l'exemple du fil rouge, on a 12 variables, \n"
				+ "dont 10 avec 2 valeurs affectables \n"
				+ "dont  2 avec 3 valeurs affectables \n\n"
				+ "De ce fait avec la version naive du backtracking pour trouver toutes les solutions, \n"
				+ "le nombre de tests sur isSatisfiedBy() théorique en version naif est de: \n2^10 * 3^2 = " + nbTestTheorique + " tests\n");
		
		// == SOLTUIONS SANS FILTER
		
		System.out.println("== SOLUTIONS VERSION NAIVE DU BACKTRACKING: \n");
		
		// Affiche les solutions: 
		/*
		for (State sol : solutions) {
			System.out.println(sol.toString());
		}
		*/
		
		System.out.println("Nombre d'appels récursifs sur solutions(): " + backtrack.nbAppel);
		System.out.println("Nombre de tests areSatifiedBy() dans solutions(): " + backtrack.nbTest);
		System.out.println("Nombre de solutions trouvées: " + solutions.size());
		
		// == SOLUTIONS AVEC FILTER
		
		System.out.println("\n== SOLUTIONS VERSION AVEC FILTER: \n");
		
		System.out.println("Nombre d'appels récursifs sur solutions(): " + backtrack.nbAppelFiltre);
		System.out.println("Nombre de tests areSatifiedBy() dans solutions(): " + backtrack.nbTestFiltre);
		System.out.println("Nombre de solutions trouvées: " + solutionsFilter.size());
		
		int nbF = 0;
		for(Constraint r : backtrack.getConstraints()) {
			nbF += r.getnbIssatFilter();
		}
		System.out.println("nb appels de areSatifiedBy() via filter(): " + nbF);
		
		

		// == SOLUTIONS AVEC HEURISTIQUE

		System.out.println("\n== SOLUTIONS VERSION AVEC HEURISTIQUE: \n");

		System.out.println("Nombre d'appels récursifs sur solutions(): " + backtrack.nbAppelHeuristic);
		System.out.println("Nombre de tests areSatifiedBy() dans solutions(): " + backtrack.nbTestHeuristic);
		System.out.println("Nombre de solutions trouvées: " + solutionsHeuristique.size());

		
		// TEST SI resultats identiques
		System.out.println("\n== chargement ... pour savoir si les solutions sont identiques...");
		boolean same, testsame;
		same = true;
		for(State s1 : solutions) {
			testsame = false;
			for(State s2 : solutionsFilter) {
				if(s1.equals(s2)) {
					testsame = true;
				}
			}
			same &= testsame;
		}
		for(State s1 : solutions) {
			testsame = false;
			for(State s2 : solutionsHeuristique) {
				if(s1.equals(s2)) {
					testsame = true;
				}
			}
			same &= testsame;
		}
		same &= (solutions.size() == solutionsFilter.size()) && (solutions.size() == solutionsHeuristique.size());
		
		System.out.println("\nles solutions sont elles bien les mêmes ? " + same);
		
		
	}

}
