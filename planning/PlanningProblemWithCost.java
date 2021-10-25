package app.planning;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class PlanningProblemWithCost extends PlanningProblem {
	public int sonde_dijkstra = 0;
	public int sonde_astar = 0;


	public PlanningProblemWithCost(State state, State finalState, Set<Action> actions) {
		super(state, finalState, actions);

	}


	public int cost(Action action) {
		for(Action action2 : this.actions) {

			if(action.getNom().contains("SYRUP_")) {
				return 2;
			}else if(action.getNom().contains("MEDECINE_")) {
				return 1;
			}
		}
		//System.err.println(action);
		return 3;
	}

	//algorithme de djikstra
	public Stack<Action>  djikstra(PlanningProblem pb){
		Map<State,Integer> distance = new HashMap<State,Integer>();
		Map<State,State> father= new HashMap<State,State>();
		Map<State,Action> plan = new HashMap<>();
		Set<State> goals = new HashSet<>();
		Set<State> open = new HashSet<State>();
		Set<State> closed = new HashSet<>();

		open.add(pb.getInitState());
		distance.put(pb.getInitState(),0);
		father.put(pb.getInitState(),null);


		while(!open.isEmpty()){
			State state  = argmin(open,distance);
			open.remove(state);
			if(satisfies(state, pb.getFinalState()))
				goals.add(state);
			for(Action act : pb.getActions()){
				if(is_applicable(act, state)){
					State next = apply(act, state);
					sonde_dijkstra++;
					Set<State> stateDist = distance.keySet();
					if(!next.contientEtat(stateDist)){
						distance.put(next, Integer.MAX_VALUE);
						if(distance.get(next) > distance.get(state)+cost(act)){
							distance.put(next,distance.get(state)+cost(act));
							father.put(next,state);
							plan.put(next, act);
							open.add(next);
						}
					}
				}
			}
		}
		return get_djikstra_plan(father,plan, goals, distance);
	}


	public Stack<Action> get_djikstra_plan(Map<State,State> father, Map<State,Action> actions,Set<State> goals,Map<State,Integer> distance){
		Stack<Action> plan = new Stack<>();
		State goal = argmin(goals,distance);
		while(goal!=null){
			plan.push(actions.get(goal));
			goal = father.get(goal);
		}
		return reverse(plan);
	}

	//l'etat dans open qui a la plus petite distance
	public State argmin(Set<State> open,Map<State,Integer> distance){
		State stateMin = (State) open.toArray()[0];
		int min = (int) distance.get(stateMin);
		for(State etat : open){
			if(distance.get(etat)<=min)
				stateMin = etat;
			min=distance.get(stateMin);
		}
		return stateMin;
	}

	//algorithme A*
	public Stack<Action> aStar(PlanningProblem pb,Heuristic h){
		SimpleHeuristic simpleheuristic = new SimpleHeuristic(pb);
		Map<State,State> father = new HashMap<State,State>(){{put(pb.getInitState(),null);}};
		Set<State> open=new HashSet<State>(){{add(pb.getInitState());}};
		Map<State,Integer> distance = new HashMap<State,Integer>(){{put(pb.getInitState(),0);}};
		Map<State,Integer> value = new HashMap<State,Integer>(){{put(pb.getInitState(),h.heuristic(pb.getInitState()));}};
		Map<State,Action> plan = new HashMap<>();

		while(!open.isEmpty()){
			State state = argmin(open,distance);
			open.remove(state);
			if(satisfies(pb.getFinalState(),state)){
				System.out.println("final : "+state);
				return get_bfs_plan(father,plan,state);
			}
			for(Action act:pb.getActions()){
				if(is_applicable(act,state)){
					State next = apply(act,state);
					sonde_astar++;
					if(!next.contientEtat(distance.keySet())){
						distance.put(next, Integer.MAX_VALUE);
					}
					if(distance.get(next)>distance.get(state)+cost(act)){
						distance.put(next,distance.get(state)+cost(act));
						value.put(next, distance.get(next)+h.heuristic(next));
						father.put(next, state);
						plan.put(next, act);
						open.add(next);
					}
				}
			}
		}
	
	return null;
}

}
