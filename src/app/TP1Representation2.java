package app;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import app.examples.ExampleRepresentation;
import app.planning.State;
import app.representations.Domain;
import app.representations.RestrictedDomain;
import app.representations.Variable;
import app.representations.constraints.Constraint;
import app.representations.constraints.Rule;

public class TP1Representation2 {
	
	/**
	 * Tests du TP Représentations
	 * ce main() reprend les tests des exemples du cours
	 * @param args .
	 */
	public static void main(String[] args) {

		// ================================
		// === EXEMPLE COURS ==============
		// ================================

		ExampleRepresentation exemple_cours = ExampleRepresentation.genereExempleCours();
		System.out.println("=== REPRESENTATION EXEMPLE DU COURS: \n\n" + exemple_cours + "\n");

		/* Par soucis de simplicité avec la generation des etats, 
		l'exemple du cours est totalement repris ci-dessous 
		afin de tester le bon fonctionnement de nos regles */

		String etat_oui = "oui";
		String etat_non = "non";

		String etat_0 = "0";
		String etat_1 = "1";
		String etat_2 = "2";
		String etat_3 = "3";
		String etat_6 = "6";

		// DÉFINITION DES DOMAINES

		Domain d0 = new Domain(etat_oui,etat_non);
		Domain d1 = new Domain(etat_0,etat_1);
		Domain d2 = new Domain(etat_0,etat_1,etat_2);
		Domain d3 = new Domain(etat_0,etat_1,etat_2,etat_3,etat_6);

		// VARIABLES 

		Variable x0 = new Variable("x0", d0);
		Variable x1 = new Variable("x1", d1);
		Variable x2 = new Variable("x2", d2);
		Variable x3 = new Variable("x3", d3);

		Set<Variable> allVariables = new HashSet<>(Arrays.asList(x0, x1, x2, x3));

		// REGLE

		Rule rule = new Rule();

		rule.addInPremisse(new RestrictedDomain(x0, etat_non));
		rule.addInPremisse(new RestrictedDomain(x1, etat_0));

		rule.addInConclusion(new RestrictedDomain(x2, etat_1));
		rule.addInConclusion(new RestrictedDomain(x3, etat_2, etat_6 ));

		// DIFFERENTS ETATS A TESTER

		State a1 = new State();
		a1.addAffectedVariable(x0, "non");
		a1.addAffectedVariable(x1, "0");
		a1.addAffectedVariable(x2, "0");
		a1.addAffectedVariable(x3, "2");

		State a2 = new State();
		a2.addAffectedVariable(x0, "non");
		a2.addAffectedVariable(x1, "1");
		a2.addAffectedVariable(x2, "0");
		a2.addAffectedVariable(x3, "0");

		State a3 = new State();
		a3.addAffectedVariable(x0, "oui");
		a3.addAffectedVariable(x1, "1");
		a3.addAffectedVariable(x2, "1");
		a3.addAffectedVariable(x3, "2");

		State a4 = new State();
		a4.addAffectedVariable(x0, "non");
		a4.addAffectedVariable(x1, "0");
		a4.addAffectedVariable(x2, "2");
		a4.addAffectedVariable(x3, "1");

		// === TESTS === 

		System.out.println("== TESTS\n");

		System.out.print("Testing scope: ");
		System.out.println(rule);
		System.out.println(rule.getScope().equals(allVariables) ? "OK" : "KO");

		System.out.print("Testing satisfying assignment: ");
		System.out.println(a1);
		System.out.println(rule.isSatisfiedBy(a1) ? "OK" : "KO");

		System.out.print("Testing satisfying assignment: ");
		System.out.println(a2);
		System.out.println(rule.isSatisfiedBy(a2) ? "OK" : "KO");

		System.out.print("Testing satisfying assignment: ");
		System.out.println(a3);
		System.out.println(rule.isSatisfiedBy(a3) ? "OK" : "KO");

		System.out.print("Testing falsifying assignment: ");
		System.out.println(a4);
		System.out.println(!rule.isSatisfiedBy(a4) ? "OK" : "KO");

	}
}
