package ca.polymtl.inf3710.tp4;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class MettreAJourEmploiDuTemps
{
	private Connection connection;
	private String     sigle;
	private Statement  stmt;
	
	public MettreAJourEmploiDuTemps(Connection connection)
	{
		this.connection = connection;
	}
	
	public void executer()
	{
		try
		{
			@SuppressWarnings("resource")
			Scanner scan = new Scanner(System.in);
			boolean continuer = true;
			ResultSet result;
			do
			{
				stmt = connection.createStatement();
				
				System.out.println("Veuillez entrer le sigle du cours a mettre a jour:");
				
				sigle = scan.next();
				
				result = stmt.executeQuery("SELECT * " + "FROM Seance s, Jour j, Heure h " + "WHERE sigle = '" + sigle
				                           + "' AND " + "s.codJour = j.codJour AND " + "s.codHeure = h.codHre");
				
				while (result.next())
				{
					System.out.println("Type: " + result.getString("leType") + "\n" + "Groupe: "
					                   + result.getString("groupe") + "\n" + "Jour: " + result.getString("nom") + "\n"
					                   + "Heure: " + result.getString("hre") + "\n" + "Alternance: "
					                   + result.getString("alternance") + "\n" + "Local: "
					                   + result.getString("lelocal") + "\n");
				}
				
				System.out.println("Voici les options disponibles:\n" + "1. Modifier une seance\n"
				                   + "2. Ajouter une seance\n" + "3. Supprimer une seance\n"
				                   + "4. Retourner au menu principal\n"
				                   + "Veuillez entrer le chiffre correspondant a l'option desiree:");
				
				int choix = scan.nextInt();
				
				while (choix < 1 || choix > 4)
				{
					System.out.println("La valeur entree est invalide. Veuillez entrer une valeur entre 1 et 4");
					choix = scan.nextInt();
				}
				
				switch (choix)
				{
					case 1:
						modifierSeance();
						verifierRequete();
						break;
					case 2:
						ajouterSeance();
						verifierRequete();
						break;
					case 3:
						supprimerSeance();
						verifierRequete();
						break;
					case 4:
						continuer = false;
						break;
					default:
						break;
				}
			} while (continuer);
			
			result.close();
			stmt.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	private void modifierSeance()
	{
		Scanner scan = new Scanner(System.in);
		
		String groupe,
				leType,
				jour,
				heure,
				alternance;

		String nouveauGroupe,
				nouveauLeType,
				nouveauJour,
				nouveauHeure,
				nouveauAlternance;
		
		
		System.out.println("Veuillez entrer le numero du groupe que vous voulez modifier:");
		groupe = scan.next();
		System.out.println("Veuillez entrer le type de la seance que vous voulez modifier (C ou L):");
		leType = scan.next();
		System.out.println("Veuillez entrer le jour de la seance que vous voulez modifier (lundi...vendredi):");
		jour = scan.next();
		System.out.println("Veuillez entrer l'heure de la seance que vous voulez modifier (ex.: 9h30):");
		heure = scan.next();
		System.out.println("Veuillez entrer l'alternance du groupe que vous voulez modifier (B1 ou B2 ou HE):");
		alternance = scan.next();
		
		System.out.println("Veuillez entrer le numero de groupe a assigner:");
		nouveauGroupe = scan.next();
		System.out.println("Veuillez entrer le type de la seance a assigner (C ou L):");
		nouveauLeType = scan.next();
		System.out.println("Veuillez entrer le jour de la seance a assigner (lundi...vendredi):");
		nouveauJour = scan.next();
		System.out.println("Veuillez entrer l'heure de la seance a assigner (ex.: 9h30):");
		nouveauHeure = scan.next();
		System.out.println("Veuillez entrer l'alternance du groupe a assigner (B1 ou B2 ou HE):");
		nouveauAlternance = scan.next();
		
		try
		{
			ResultSet resultat = stmt.executeQuery("UPDATE Seance s, Jour j, Heure h " +
					"SET groupe = " + nouveauGroupe + " AND " +
							"s.leType = " + nouveauLeType + " AND " +
							"s.codJour = j.codJour" + " AND " +
							"s.codHeure = h.codHre" + " AND " +
							"s.alternance = " + nouveauAlternance +
							" WHERE j.nom = " + nouveauJour + " AND " +
							"h.hre = " + nouveauHeure + " AND " +
							"s.groupe = " + groupe + " AND " +
							"s.leType = " + leType + " AND " +
							"s.codJour = (SELECT codJour FROM Jour WHERE nom = " + jour + ") AND " +
							"s.codHeure = (SELECT codHre FROM Heure WHERE hre = " + heure + ") AND " +
							"s.alternance = " + alternance);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	private void ajouterSeance()
	{
		
	}
	
	private void supprimerSeance()
	{
		
	}
	
	private void verifierRequete()
	{
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
		String reponse;
		
		do
		{
			System.out.println("Voulez-vous envoyer les changements (COMMIT) (o ou n)?");
			
			reponse = scan.next();
			
			if (reponse != "o" && reponse != "n")
			{
				System.out.println("Erreur, veuillez entrer 'o' ou 'n'.");
			}
		} while (reponse != "o" && reponse != "n");
		
		try
		{
			if (reponse == "o")
			{
				connection.commit();
			}
			else
			{
				connection.rollback();
			}
		}
		catch (SQLException e)
		{   
			e.printStackTrace();
		}
	}
}
