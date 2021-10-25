package app;

import app.examples.ExampleRepresentation;
import app.planning.State;
import app.representations.*;
import app.representations.constraints.*;

public class TP1Representation1 {

	/**
	 * Tests du TP Représentations
	 * ce main() reprend les exemples du fil rouge 
	 * avec un état généré aléatoirement 
	 * avec lequel on test toutes les contraintes par isSatifiedBy(etat)
	 * @param args .
	 */
	public static void main(String[] args) {

		// ================================
		// === EXEMPLES DU FIL ROUGE ======
		// ================================

		ExampleRepresentation exemple_filrouge = ExampleRepresentation.genereExemplesFilRouge();;
		System.out.println("=== REPRESENTATION DES EXEMPLES DU FILROUGE: \n\n" + exemple_filrouge + "\n");

		State etat = exemple_filrouge.genereEtatAleatoire();

		System.out.println("== TESTS SUR UN ETAT ALEATOIRE (ensemble d'affectations)\n");
		System.out.println("=> état aléatoire generé pour tester: \n" + etat + "\n");
		System.out.println("=> est il satisfait par nos différentes règles ?\n");

		for(Constraint constraint : exemple_filrouge.getConstraints()) {
			System.out.println("*" + constraint);
			System.out.println(constraint.isSatisfiedBy(etat) + "\n");
		}


	}

}
