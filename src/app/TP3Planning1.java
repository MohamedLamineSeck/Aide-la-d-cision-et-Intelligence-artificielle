package app;

import java.util.HashSet;
import java.util.Stack;

import app.examples.HealthCare;
import app.planning.Action;
import app.planning.PlanningProblem;
import app.planning.State;

public class TP3Planning1 {

	public static void main(String[] args) {
		HealthCare hc = new HealthCare(10);
		System.out.println(hc.getInitState());
		System.out.println(hc.getFinalState());

		PlanningProblem pb = new PlanningProblem(hc.getInitState(), hc.finalState(), hc.getActions());
		Stack<Action> sol = pb.dfs(pb, pb.getInitState(), new Stack<Action>(), new HashSet<State>());
		
		for(Action action : sol) {
			System.out.println(action);
		}
		/*
		//State etatfinal = pb.getState_finale();
		//l'etat init attendu
		System.out.println("etat init: "+pb.getInitState().toString());
		//BFS
		//l'etat final attendu
		System.out.println("etat fin: "+pb.getFinalState().toString());
		//BFS
		System.out.println("bfs :\n");
		//pb.toStringActions2();
		System.out.println(healthCare.toStringActions(pb.bfs(pb)));
		//DFS D'ABORD
		System.out.println("dfs_dabord :\n");
		healthCare.toStringActions(pb.dfs(pb,pb.getState_initial(),new Stack<Action>(),new HashSet<State>()));
		//DFS itérative
		//System.out.println("dfs_iterative :\n"+healthCare.toStringActions(pb.dfs_iterative(pb.getState_initial())));
		//les sondes des différents algorithmes
		System.out.println("noeuds explorés de bfs :"+pb.getSonde_bfs());
		System.out.println("noeuds explorés de dfs_d'abord :"+pb.getSonde_dfs_dabord());
		System.out.println("noeuds explorés de dfs itérative :"+pb.getSonde_dfs_itérative());
		*/
	}

}
