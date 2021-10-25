package app.examples;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import app.planning.Action;
import app.planning.State;
import app.representations.Domain;
import app.representations.Variable;

public class HealthCare {
	protected Set<Variable> variablesBool;
	protected Set<Variable> variablesNiv;
	protected Set<Action> actions;
	protected Set<Action> actionsMedoc;
	protected State initState;
	protected State finalState;

	public HealthCare(int nbMedoc) {
		this.variablesBool = new HashSet<Variable>();
		this.variablesNiv = new HashSet<Variable>();
		this.actions = new HashSet<Action>();
		this.actionsMedoc = new HashSet<Action>();
		
		// VALUES
		String val_oui, val_non, val_high, val_medium, val_low, val_none;
		val_oui = "true";
		val_non = "false";
		val_high = "high";
		val_medium = "medium";
		val_low = "low";
		val_none = "none";

		// DOMAINS
		Domain d1 = new Domain(val_oui, val_non);
		Domain d2 = new Domain(val_high, val_medium, val_low, val_none);
		
		// VARIABLES
		
		//1. des variables booléennes représentant les maladies : ANGINA, FLU, POX, PLAGUE
		
		Variable v_ANGINA = new Variable("ANGINA", d1);
		Variable v_FLU = new Variable("FLU", d1);
		Variable v_POX = new Variable("POX", d1);
		Variable v_PLAGUE = new Variable("PLAGUE", d1);

		// 2. des variables à niveau dans {high, medium, low, none} représentant des symptômes FEVER, COUGH, BUTTONS,
		
		Variable v_FEVER = new Variable("FEVER", d2);
		Variable v_COUGH = new Variable("COUGH", d2);
		Variable v_BUTTONS = new Variable("BUTTONS", d2);

		this.variablesNiv.addAll(Arrays.asList(v_FEVER,v_COUGH,v_BUTTONS));
		this.variablesBool.addAll(Arrays.asList(v_ANGINA,v_FLU,v_POX,v_PLAGUE));
		
		// ACTIONS

		// 3. des actions qui représentent trois sirops qui réduisent chacun un symptôme distinct d’un niveau
		
		Action a1 = new Action("SYRUP_BUTTONS_HIGH");
		a1.addInPrecondition(v_BUTTONS, val_high);
		a1.addInEffets(v_BUTTONS, val_medium);
		Action a2 = new Action("SYRUP_BUTTONS_MEDIUM");
		a2.addInPrecondition(v_BUTTONS, val_medium);
		a2.addInEffets(v_BUTTONS, val_low);
		Action a3 = new Action("SYRUP_BUTTONS_LOW");
		a3.addInPrecondition(v_BUTTONS, val_low);
		a3.addInEffets(v_BUTTONS, val_none);


		Action a4 = new Action("SYRUP_COUGH_HIGH");
		a4.addInPrecondition(v_COUGH, val_high);
		a4.addInEffets(v_COUGH, val_medium);
		Action a5 = new Action("SYRUP_COUGH_MEDIUM");
		a5.addInPrecondition(v_COUGH, val_medium);
		a5.addInEffets(v_COUGH, val_low);
		Action a6 = new Action("SYRUP_COUGH_LOW");
		a6.addInPrecondition(v_COUGH, val_low);
		a6.addInEffets(v_COUGH, val_none);

		Action a7 = new Action("SYRUP_FEVER_HIGH");
		a7.addInPrecondition(v_FEVER, val_high);
		a7.addInEffets(v_FEVER, val_medium);
		Action a8 = new Action("SYRUP_FEVER_MEDIUM");
		a8.addInPrecondition(v_FEVER, val_medium);
		a8.addInEffets(v_FEVER, val_low);
		Action a9 = new Action("SYRUP_FEVER_LOW");
		a9.addInPrecondition(v_FEVER, val_low);
		a9.addInEffets(v_FEVER, val_none);

		this.actions.addAll(Arrays.asList(a1,a2,a3,a4,a5,a6,a7,a8,a9));


		// 4. des actions qui représentant n médicaments
		
		nbMedoc = (nbMedoc < 0 || nbMedoc > 24) ? 3 : nbMedoc;
		
		char[] alphabet = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P',
				'Q','R','R','S','T','U','V','W','X','Y','Z'};

		Action medocTmp;

		Random random = new Random();
		int indexNone;
		int j = 0;

		for(int i = 0 ; i < nbMedoc ; i++) {

			indexNone = new Random().nextInt(this.variablesNiv.size());
			medocTmp = new Action("MEDECINE_" + alphabet[i]);
			for(Variable variable : this.variablesNiv) {
				if(j == indexNone) {
					medocTmp.addInEffets(variable, val_none);
				}else {
					medocTmp.addInEffets(variable, niveauxAleatoire());
				}

				j++;
			}
			this.actionsMedoc.add(medocTmp);
			j=0;

		}
		
		// 5. une action de guérison :
		
		Action guerison = new Action("HEAL");
		guerison.addInPrecondition(v_FEVER, val_none);
		guerison.addInPrecondition(v_COUGH, val_none);
		guerison.addInPrecondition(v_BUTTONS, val_none);
		guerison.addInEffets(v_ANGINA, val_non);
		guerison.addInEffets(v_FLU, val_non);
		guerison.addInEffets(v_POX, val_non);
		guerison.addInEffets(v_PLAGUE, val_non);
		//this.actions.add(guerison);
		

		this.initState = initState();
		this.finalState = finalState();
	}
	
	private  Map<Variable, String> maladieAleatoire() {
        int r = (int) (Math.random() * 4);
        Map<Variable, String> v = new HashMap<>();
        String name = new String[]{"ANGINA", "FLU", "POX", "PLAGUE"}[r];

        for (Variable var : this.variablesBool) {
            if (var.getName() == name) {
                v.put(var, "true");
            }
        }
        return v;
    }

	//Condition initiale d'une maladie
    public State initState(){
        Map<Variable, String> ensemble = new HashMap<Variable, String>();
        Map<Variable, String> maladie = maladieAleatoire();
        for(Variable var : this.variablesNiv) {
            ensemble.put(var,niveauxAleatoire());
        }
        ensemble.putAll(maladie);
        return new State(ensemble);
    }
    

    public State finalState(){

    	State finalState = this.initState.getCopy();
    	
        for( Map.Entry<Variable, String> var : finalState.getAssignations().entrySet()) {
            if(var.getValue()=="true"){
                finalState.getAssignations().put(var.getKey(), "false");
            }
            else {
            	finalState.getAssignations().put(var.getKey(), "none");
            }
        }
        return finalState;
    }

	private String niveauxAleatoire() {
		int r = (int) (Math.random() * 4);
		String name = new String[]{"high", "medium", "low", "none"}[r];
		return name;
	}

	public Set<Variable> getVariablesBool() {
		return variablesBool;
	}

	public Set<Variable> getVariablesNiv() {
		return variablesNiv;
	}

	public Set<Action> getActions() {
		return actions;
	}

	public Set<Action> getActionsMedoc() {
		return actionsMedoc;
	}

	public State getInitState() {
		return initState;
	}

	public State getFinalState() {
		return finalState;
	}
	
	

}
