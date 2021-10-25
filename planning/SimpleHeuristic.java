package app.planning;

import java.util.Map;

import app.representations.Variable;

public class SimpleHeuristic implements Heuristic{

	private PlanningProblem pb;
	public SimpleHeuristic(PlanningProblem pb){
		this.pb=pb;
	}

	@Override
	public int heuristic(State state) {
		int nombre =0;
		for(Map.Entry<Variable, String> affectation : state.getAssignations().entrySet()) {
			if(affectation.getValue()== "high") {
				nombre +=3;
			}
			else if(affectation.getValue()== "medium"){
				nombre +=2;
			}
			else if(affectation.getValue()== "low"){
				nombre +=1;
			}
		}

		return nombre;
	}
}

/* for(Map.Entry<Variable,String> conclusion : state.getAssignations().entrySet()){
if(finalState.getAssignations((conclusion.getKey())!=conclusion.getValue()))
    nombre++;
}
return nombre;*/
