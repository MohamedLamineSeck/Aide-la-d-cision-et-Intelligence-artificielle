package app.representations;

import java.util.HashSet;
import java.util.Set;

public class Domain {
	protected Set<String> values;

	/**
	 * Constructeur de la classe Domaine
	 * represente un ensemble de valeurs
	 * @param values les valeurs du domaine
	 */
	public Domain(Set<String> values) {
		super();
		this.values = values;
	}
	
	/**
	 * Constructeur a partir d'une succession de valeurs
	 * @param values les valeurs du domaine
	 */
	public Domain(String ... values) {
		this.values = new HashSet<String>();
		for(String value : values) {
			this.values.add(value);
		}
	}
	
	// FONCTIONS
	
	/**
	 * @return une copie du domaine courant
	 */
	public Domain getCopy() {
		Set<String> copyvalues = new HashSet<String>();
		copyvalues.addAll(this.values);
		return new Domain(copyvalues);
	}
	
	public int getTaille() {
		return this.values.size();
	}
	
	// STATIC FUNCTION
	
	/**
	 * @param subdomain le sous domaine
	 * @param domain le domaine
	 * @return vrai ou faux si toutes les valeurs du sous-domaine sont comprises dans le domaine
	 */
	public static boolean isSubDomainOf(Domain subdomain, Domain domain) {
		return domain.getValues().containsAll(subdomain.getValues());
	}

	// GETs

	public Set<String> getValues() {
		return values;
	}
	
	// Representation
	@Override
	public String toString() {
		String str = "{";
		for(String v : this.values) {
			str += v + ", ";
		}
		str = str.substring(0, str.length()-2) + "}";
		return str;
	}
}
