package app.representations;

public class Variable {
	protected String name;
	protected Domain domain;

	/**
	 * Constructeur de la classe Variable
	 * cette classe permet de representer une variable et toutes les valeurs que celle peut se voir affecter
	 * @param name le nom de la variable
	 * @param domain son domaine
	 */
	public Variable(String name, Domain domain) {
		this.name = name;
		this.domain = domain;
	}
	
	// GETs

	public String getName() {
		return name;
	}
	public Domain getDomain() {
		return domain;
	}
	
	// Representation
	@Override
	public String toString() {
		return this.name;
	}
}
