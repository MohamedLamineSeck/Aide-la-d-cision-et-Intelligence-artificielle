package app.planning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import app.representations.RestrictedDomain;
import app.representations.Variable;

/**
 * 
 */
public class PlanningProblem {
	protected State initState;
	protected State finalState;
	protected Set<Action> actions;
	
	public int sonde_dfs = 0;
	public int sonde_bfs = 0;

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
					sonde_dfs++;
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
		return null;
	}

	//=============== Recherche en Largeur ===============//
	public Stack<Action> bfs(PlanningProblem pb) {

		Map<State, State> father = new HashMap<State, State>();
		Map<State, Action> plan = new HashMap<State, Action>();
		Set<State> closed = new HashSet<State>();
		List<State> opened = new ArrayList<State>();
		//Queue<State> opened = new LinkedList<>();
		//Deque<State> deque;
		opened.add(pb.getInitState());
		father.put(pb.getInitState(), null);
		while (!opened.isEmpty()) {
			
			State state = opened.remove(0);
			closed.add(state);
			for (Action action : pb.getActions()) {
				
				if(is_applicable(action,state)){
					
					State next = apply(action, state);
					sonde_bfs++;
					
					if (!next.contientEtat(closed) && !next.contientEtat(opened)) {
						//System.err.println(action);
						father.put(next, state);
						plan.put(next, action);
						if (satisfies(next,pb.getFinalState())) {
							return get_bfs_plan(father, plan, next);
						}
						else
							opened.add(next);
						//System.out.println( "liste des opened : "+opened.toString());
					}
				}
			}
		}
		return null;
	}



	public Stack<Action> get_bfs_plan(Map<State,State> father, Map<State,Action> action, State goal){
		Stack<Action> plan = new Stack();
		//System.err.println("??");
		while(goal!=null){
			plan.push(action.get(goal));
			goal=father.get(goal);

		}
		//System.out.println(" plan : "+plan.toString());
		return reverse(plan);
	}


	// Permet d'inverser une liste
	public Stack<Action> reverse(Stack<Action> plan){
		Stack<Action> plan2 = new Stack<Action>();
		for(int i=plan.size()-1; i>=0; i--)
			plan2.add(plan.get(i));
		return plan2;
	}

	//affiche les elements d'un Stack
	public String toStringStack(Stack<Action> stack){
		String str="Actions: \n";
		for(Action action:stack){
			if(action != null) {
				str+="* " + action + "\n";
			}
		}
		return str;
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
