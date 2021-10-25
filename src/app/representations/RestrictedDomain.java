package app.representations;

public class RestrictedDomain {
	protected Variable variable;
	protected Domain subdomain;
	
	/**
	 * Constructeur de la classe RestrictedDomain
	 * @param variable la variable
	 * @param sub_domain le domaine restreint de cette variable
	 */
	public RestrictedDomain(Variable variable, Domain sub_domain) {
		this.variable = variable;
		this.subdomain = sub_domain;
		
		if(!Domain.isSubDomainOf(this.subdomain, this.variable.getDomain())) {
			System.err.println("le sous-domaine ne correspond pas au domaine de la variable " + this.variable);
		}
		
	}
	
	/**
	 * Constructeur a partir d'une succession de valeurs
	 * @param variable la variable
	 * @param values le domaine restreint de cette variable
	 */
	public RestrictedDomain(Variable variable, String ...values) {
		this(variable,new Domain(values));
		
	}
	
	/**
	 * Constructeur specifique attribuant comme sous-domaine le domaine de la variable
	 * @param variable la variable
	 */
	public RestrictedDomain(Variable variable) {
		this(variable, variable.getDomain().getCopy());
	}
	
	// GETs

	public Variable getVariable() {
		return variable;
	}

	public Domain getSubdomain() {
		return subdomain;
	}
	
	// Representation
	@Override
	public String toString() {
		return "(" + this.variable.toString() + " \\in " + this.subdomain.toString() + ")";
	}
	
	
}
