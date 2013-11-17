package ca.polymtl.inf3710.tp4;

public class Personne
{
	public Personne(String nom, String prenom)
	{
		this.nom = nom;
		this.prenom = prenom;
	}
	
	public String getNom()
	{
		return nom;
	}
	
	public String getPrenom()
	{
		return prenom;
	}
	
	private String nom;
	private String prenom;
}
