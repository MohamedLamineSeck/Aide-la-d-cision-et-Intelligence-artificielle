package app.examples;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import app.representations.constraints.*;
import app.planning.State;
import app.representations.*;

public class ExampleRepresentation {
	protected Set<Variable> variables;
	protected Set<Constraint> constraints;
	
	public ExampleRepresentation(Set<Variable> variables, Set<Constraint> contraintes) {
		this.variables = variables;
		this.constraints = contraintes;
	}
	
	public ExampleRepresentation() {
		this.variables = new HashSet<Variable>();
		this.constraints =  new HashSet<Constraint>();
	}
	
	public static ExampleRepresentation genereExemplesFilRouge() {
		ExampleRepresentation example = new ExampleRepresentation();
		
		// VALUES
		String val_oui, val_non, val_basse, val_moyenne, val_haute;
		val_oui = "1";
		val_non = "0";
		val_basse = "basse";
		val_moyenne = "moyenne";
		val_haute = "haute";
		
		// DOMAINS
		Domain d1 = new Domain(val_oui, val_non);
		Domain d2 = new Domain(val_basse, val_moyenne, val_haute);
		
		// VARIABLES
		Variable v_fievre = new Variable("fièvre", d2);
		Variable v_allergie_au_sucre = new Variable("allergie_sucre", d2);
		Variable v_angine = new Variable("angine", d1);
		Variable v_grippe = new Variable("grippe", d1);
		Variable v_fatigue = new Variable("fatigué(e)", d1);
		Variable v_virus = new Variable("virus", d1);
		Variable v_toux = new Variable("toux", d1);
		Variable v_boutons = new Variable("boutons", d1);
		Variable v_oedeme = new Variable("œdème", d1);
		Variable v_hypothermie = new Variable("hypothermie", d1);
		Variable v_vaccination = new Variable("vacciné(e)", d1);
		Variable v_sirop = new Variable("prise_sirop", d1);
		
		example.variables.addAll(Arrays.asList(
				v_fievre,v_allergie_au_sucre,
				v_angine,v_grippe,v_fatigue,v_virus,
				v_toux,v_boutons,v_oedeme,
				v_hypothermie,v_vaccination,v_sirop));
		
		// CONSTRAINTS
		
		// L'angine provoque une fièvre haute ou moyenne
		Rule c1 = new Rule();
		c1.addInPremisse(v_angine,val_oui);
		c1.addInConclusion(v_fievre, val_moyenne, val_haute);
		
		// L'angine provoque la toux
		Rule c2 = new Rule();
		c2.addInPremisse(v_angine, val_oui);
		c2.addInConclusion(v_toux, val_oui);
		
		// Une grippe, en l'absence de vaccination, provoque une fièvre haute
		Rule c3 = new Rule();
		c3.addInPremisse(v_grippe, val_oui);
		c3.addInPremisse(v_vaccination, val_non);
		c3.addInConclusion(v_fievre, val_haute);
		
		// Une grippe, en l'absence de vaccination, provoque la fatigue
		Rule c4 = new Rule();
		c4.addInPremisse(v_grippe, val_oui);
		c4.addInPremisse(v_vaccination, val_non);
		c4.addInConclusion(v_fatigue, val_oui);
		
		// L'angine peut ou non être provoquée par un virus
		Rule c5 = new Disjunction();
		c5.addInPremisse(v_virus, val_oui);
		c5.addInPremisse(v_virus, val_non);
		c5.addInConclusion(v_angine, val_oui, val_non);
		
		 // Une grippe est toujours provoquée par un virus
		Rule c6 = new Rule();
		c6.addInPremisse(v_virus, val_oui);
		c6.addInConclusion(v_grippe, val_oui);
		
		// La prise de sirop avec une allergie moyenne au sucre provoque des boutons
		Rule c7 = new Rule();
		c7.addInPremisse(v_sirop, val_oui);
		c7.addInPremisse(v_allergie_au_sucre, val_moyenne);
		c7.addInConclusion(v_boutons, val_oui);
		
		// La prise de sirop avec une allergie haute au sucre provoque un œdème
		Rule c8 = new Rule();
		c8.addInPremisse(v_sirop, val_oui);
		c8.addInPremisse(v_allergie_au_sucre, val_haute);
		c8.addInConclusion(v_oedeme, val_oui);
		
		// On ne peut pas à la fois avoir une fièvre haute ou moyenne et être en hypothermie
		Rule c9 = new IncompatibilityConstraint();
		c9.addInPremisse(v_fievre, val_moyenne, val_haute);
		c9.addInPremisse(v_hypothermie, val_oui);
		
		example.constraints.addAll(Arrays.asList(c9,c8,c7,c6,c5,c4,c3,c2,c1));
		
		return example;
	}
	
	public static ExampleRepresentation genereExempleCours() {
		ExampleRepresentation example = new ExampleRepresentation();
		
		// ÉTATS D'UN ETAT D'UN SYMPTOME
		
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
		
		example.variables.addAll(Arrays.asList(x0, x1, x2, x3));
		
		// REGLES
		
		Rule rule = new Rule();
		rule.addInPremisse(new RestrictedDomain(x0, etat_non));
		rule.addInPremisse(new RestrictedDomain(x1, etat_0));
		
		rule.addInConclusion(new RestrictedDomain(x2, etat_1));
		rule.addInConclusion(new RestrictedDomain(x3, etat_2, etat_6 ));
		/*
		Rule rule2 = new Rule();
		rule2.addInPremisse(new RestrictedDomain(x2, etat_1));
		rule2.addInPremisse(new RestrictedDomain(x3, etat_6));
		rule2.addInPremisse(new RestrictedDomain(x1, etat_0));
		rule2.addInConclusion(new RestrictedDomain(x3, etat_6));
		
		Rule rule3 = new Rule();
		rule3.addInPremisse(new RestrictedDomain(x0, etat_oui));
		rule3.addInPremisse(new RestrictedDomain(x1, etat_0));
		
		rule3.addInConclusion(new RestrictedDomain(x2, etat_1));
		rule3.addInConclusion(new RestrictedDomain(x3, etat_2, etat_6 ));
		*/
		example.constraints.addAll(Arrays.asList(rule/*, rule2, rule3*/));
		
		return example;
	}
	
	public State genereEtatAleatoire() {
		State etat = new State();
		
		Random random = new Random();
		int size;
		int item;
		int i;
		
		for(Variable variable : this.variables) {
			i = 0;
			size = variable.getDomain().getTaille();
			item = new Random().nextInt(size);
			
			for(String value : variable.getDomain().getValues())
			{
			    if (i == item) {
			    	etat.addAffectedVariable(variable, value);
			    	break;
			    }
			    
			    i++;
			}
		}
		
		return etat;
	}
	
	// GETs
	
	public Set<Variable> getVariables() {
		return variables;
	}
	public Set<Constraint> getConstraints() {
		return constraints;
	}
	
	// Representation
	
	@Override
	public String toString() {
		String rpz = "VARIABLES: \n{";
		
		for(Variable var : this.variables) {
			rpz += var.toString() + ", ";
		}
		
		rpz = rpz.substring(0, rpz.length()-2) + "}";
		rpz += "\n\nCONSTRAINTS: \n{\n";
		
		for(Constraint c : this.constraints) {
			rpz += " (" + c.toString() + "), \n";
		}
		
		rpz = rpz.substring(0, rpz.length()-3) + "\n}";
		
		return rpz;
	}
	
}
