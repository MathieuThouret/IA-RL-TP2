package agent.rlagent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import environnement.Action;
import environnement.Environnement;
import environnement.Etat;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import javafx.util.Pair;
/**
 * 
 * @author laetitiamatignon
 *
 */
public class QLearningAgent extends RLAgent{
	//TODO
	Map<Etat,Map<Action, Double>> Q;
	
	/**
	 * 
	 * @param alpha
	 * @param gamma
	 * @param Environnement
	 */
	public QLearningAgent(double alpha, double gamma,
			Environnement _env) {
		super(alpha, gamma,_env);
                this.Q = new HashMap<>();
		//TODO
		
	
	}


	
	
	/**
	 * renvoi la (les) action(s) de plus forte(s) valeur(s) dans l'etat e
	 *  
	 *  renvoi liste vide si aucunes actions possibles dans l'etat 
	 */
	@Override
	public List<Action> getPolitique(Etat e) {
		List<Action> actionsPossibles = this.getActionsLegales(e);
                List<Action> bestActions = new ArrayList<>();
                for (Action a: actionsPossibles){
                    if (getQValeur(e,a)> 0.0){
                        bestActions.add(a);
                    }
                }
		return bestActions;
		
		
	}
	
	/**
	 * @return la valeur d'un etat
         * renvoi la valeur max de Q(e,.)(lire getQValeur)
	 */
	@Override
	public double getValeur(Etat e) {
		double valeur = 0.0;
                List<Action> actionsPossibles = this.getActionsLegales(e);
                for (Action a: actionsPossibles){
                    valeur = Math.max(valeur, getQValeur(e,a));
                }
                return valeur;
	}

	/**
	 * On verifie si e et a existent, on récupère le double
         * sinon on crée la clé ou l'action et on lui met 0.0
	 * @param e
	 * @param a
	 * @return Q valeur du couple (e,a)
	 */
	@Override
	public double getQValeur(Etat e, Action a) {
		if (Q.containsKey(e)){
                    if(Q.get(e).containsKey(a)){
                        return Q.get(e).get(a);
                    }
                    else{
                        Q.get(e).put(a, 0.0);
                        //return 0.0;
                    }
                }
                else{
                    Map<Action, Double> newAction = new HashMap<>();
                    newAction.put(a, 0.0);
                    Q.put(e, newAction);
                    //return 0.0;
                }
            return 0.0;
	}
	
	/**
	 * setter sur Q-valeur
	 */
	@Override
	public void setQValeur(Etat e, Action a, double d) {
		//TODO
		Q.get(e).put(a,d);
		
		//mise a jour vmin et vmax pour affichage gradient de couleur
		//...
		Double min = Double.MAX_VALUE;
                double max = -Double.MAX_VALUE;
                Set<Etat> etats = this.Q.keySet();
                for (Etat etat: etats){
                    for (Double Qvalue : Q.get(etat).values()){
                        min = Math.min(min, Qvalue);
                        max = Math.max(max, Qvalue);
                    }
                }
                super.vmin = min;
                super.vmax = max;
		
		
		this.notifyObs();
	}
	
	
	/**
	 *
	 * mise a jour de la Q-valeur du couple (e,a) apres chaque interaction <etat e,action a, etatsuivant esuivant, recompense reward>
	 * la mise a jour s'effectue lorsque l'agent est notifie par l'environnement apres avoir realise une action.
	 * @param e
	 * @param a
	 * @param esuivant
	 * @param reward
	 */
	@Override
	public void endStep(Etat e, Action a, Etat esuivant, double reward) {
            Double valeur = (1-this.alpha)*getQValeur(e,a) + this.alpha*(reward + this.gamma*getValeur(esuivant));
            setQValeur(e,a, valeur);	
	}

	@Override
	public Action getAction(Etat e) {
		this.actionChoisie = this.stratExplorationCourante.getAction(e);
		return this.actionChoisie;
	}

	/**
	 * reinitialise les Q valeurs
	 */
	@Override
	public void reset() {
		super.reset();
		this.episodeNb =0;
		//TODO
		
		
		this.notifyObs();
	}




	



	


}
