package app;

import java.util.Set;

import app.examples.ExampleRepresentation;
import app.planning.State;
import app.ppc.Backtracking;

public class TP2Backtracking2 {

	/**
	 * Tests du TP Backtracking
	 * tests basés sur l'exemple du cours
	 * 
	 * 1) test du backtrack
	 * 2) test du backtrack avec filtrage
	 * 3) test du backtrack avec heuristique
	 * 
	 * @param args .
	 */
	public static void main(String[] args) {

		ExampleRepresentation exemple = ExampleRepresentation.genereExempleCours();;
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

		double nbTestTheorique = Math.pow(2, 2) * 3 * 5;
		System.out.println(""
				+ "Dans l'exemple du cours, on a 4 variables, \n"
				+ "dont 2 avec 2 valeurs affectables \n"
				+ "dont 1 avec 3 valeurs affectables \n"
				+ "dont 1 avec 5 valeurs affectables \n\n"
				+ "De ce fait avec la version naive du backtracking pour trouver toutes les solutions, \n"
				+ "le nombre de tests sur isSatisfiedBy() théorique en version naif est de: \n2^2 * 3 * 5 = " + nbTestTheorique + " tests\n");

		// == SOLTUIONS SANS FILTER


		// Affiche les solutions: 
		System.out.println("LECTURE DES SOLUTIONS: \n");
		for (State sol : solutions) {
			System.out.println(sol.toString());
		}

		System.out.println("\n== SOLUTIONS VERSION NAIVE DU BACKTRACKING: ");

		System.out.println("\nNombre d'appels récursifs sur solutions(): " + backtrack.nbAppel);
		System.out.println("Nombre de tests isSatifiedBy() dans solutions(): " + backtrack.nbTest);
		System.out.println("Nombre de solutions trouvées: " + solutions.size());

				

	}

}
