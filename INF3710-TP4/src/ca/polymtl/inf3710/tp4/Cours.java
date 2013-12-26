package ca.polymtl.inf3710.tp4;

public class Cours
{
	enum Cycle
	{
		EtudeSuperieur,
		Baccalaureat,
		Certificat
	}
	
	public Cours(String sigle, String titre, int nombreDeCredit, String cycle, Personne responsable, String departement)
	{
		this.sigle = sigle;
		this.titre = titre;
		this.nombreDeCredit = nombreDeCredit;
		switch (cycle)
		{
			case "ES":
				this.cycle = Cycle.EtudeSuperieur;
				break;
			case "BA":
				this.cycle = Cycle.Baccalaureat;
				break;
			case "CE":
				this.cycle = Cycle.Certificat;
				break;
			default:
				this.cycle = Cycle.Baccalaureat;
				break;
		}
		this.responsable = responsable;
		this.departement = departement;
	}
	
	@Override
	public String toString()
	{
		return System.lineSeparator() + "Sigle: " + getSigle() + System.lineSeparator() + "Titre du cours: "
		       + getTitre() + System.lineSeparator() + "Nombre de credits: " + getNombreDeCredit()
		       + System.lineSeparator() + "Cycle: " + getCycle() + System.lineSeparator() + "Responsable(s): "
		       + responsable.getNom() + ", " + responsable.getPrenom() + System.lineSeparator() + "Departement(s): "
		       + getDepartement() + System.lineSeparator();
	}
	
	public String getSigle()
	{
		return sigle;
	}
	
	public String getTitre()
	{
		return titre;
	}
	
	public int getNombreDeCredit()
	{
		return nombreDeCredit;
	}
	
	public String getCycle()
	{
		switch (cycle)
		{
			case EtudeSuperieur:
				return "Études supérieures";
			case Baccalaureat:
				return "Baccalauréat";
			case Certificat:
				return "Certificat";
			default:
				return "Baccalauréat";
		}
	}
	
	public Personne getResponsable()
	{
		return responsable;
	}
	
	public String getDepartement()
	{
		return departement;
	}
	
	private String   sigle;
	private String   titre;
	private int      nombreDeCredit;
	private Cycle    cycle;
	private Personne responsable;
	private String   departement;
}
