package app.planning;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import app.representations.RestrictedDomain;
import app.representations.Variable;

public class PlanningProblem {
	protected State initState;
	protected State finalState;
	protected Set<Action> actions;

	public PlanningProblem(State state, State finalState, Set<Action> actions) {
		this.initState = state;
		this.finalState = finalState;
		this.actions = actions;
	}

	public boolean satisfies(State state, State partialState) {
		String value;

		for(Map.Entry<Variable, String> assignation : partialState.getAssignations().entrySet()){
			value = state.getAssignations().get(assignation.getKey());

			if(value != assignation.getValue()) {
				return false;
			}
		}
		return true;
	}

	public boolean is_applicable(Action action, State state) {

		for(State precondition : action.getPreconditions()) {
			if(this.satisfies(state, precondition)) {
				return true;
			}
		}

		return false;
	}

	public State apply(Action action, State state) {
		State copy = state.getCopy();

		if(is_applicable(action, state)) {
			for(State precondition : action.getPreconditions()) {
				if(this.satisfies(state, precondition)) {
					for(State effet : action.getEffets()) {
						copy.getAssignations().putAll(effet.getAssignations());;
					}
				}
			}
		}

		return copy;
	}

	//=============== Recherche en Profondeur ===============//
	public Stack<Action> dfs( PlanningProblem pb, State stateInitial, Stack<Action> plan, HashSet<State> closed) {
		//sonde_dfs_dabord++;
		if (this.satisfies(stateInitial, pb.getFinalState())) {
			return plan;
		} else {
			for (Action action : pb.getActions()) {
				if (this.is_applicable(action,stateInitial)) {   // is_applicable return true or false
					State next = apply(action, stateInitial);
					if (!next.contientEtat(closed)) {
						plan.push(action);
						closed.add(next);
						Stack<Action> subplan;
						subplan = dfs(pb, next, plan, closed);
						if (subplan != null) {
							// System.out.println("plan0 = " + plan);
							//  System.out.println("subplan0=" + subplan);
							return subplan;
						} else {
							plan.pop();                 // pop retourne le premier element de la pile et le supprime
						}
					}
				}
			}
			//System.out.println("plan = " + plan);
		}
		return plan;
	}

	// GETs

	public State getInitState() {
		return initState;
	}
	public State getFinalState() {
		return finalState;
	}
	public Set<Action> getActions() {
		return actions;
	}

}
