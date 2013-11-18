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
				
				sigle = scan.next().toUpperCase();
				
				result = stmt.executeQuery("SELECT * " + "FROM Seance s, Jour j, Heure h " + "WHERE sigle = '" + sigle
				                           + "' AND " + "s.codJour = j.codJour AND " + "s.codHeure = h.codHre");
				
				while (result.next())
				{
					System.out.println("Type: " + result.getString("leType") + "\n" + "Groupe: "
					                   + result.getString("groupe") + "\n" + "Jour: " + result.getString("nom") + "\n"
					                   + "Heure: " + result.getString("hre") + "\n" + "Duree: "
					                   + result.getString("duree") + "\n" + "Alternance: "
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
						break;
					case 2:
						ajouterSeance();
						break;
					case 3:
						supprimerSeance();
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
		
		String groupe, leType, jour, heure, alternance;
		
		String nouveauDuree, nouveauLocal, nouveauJour, nouveauHeure, nouveauAlternance;
		
		System.out.println("Veuillez entrer le numero du groupe que vous voulez modifier:");
		groupe = scan.next();
		System.out.println("Veuillez entrer le type de la seance que vous voulez modifier (C ou L):");
		leType = scan.next();
		System.out.println("Veuillez entrer le jour de la seance que vous voulez modifier (Lundi...Vendredi):");
		jour = scan.next();
		System.out.println("Veuillez entrer l'heure de la seance que vous voulez modifier (ex.: 09h30):");
		heure = scan.next();
		System.out.println("Veuillez entrer l'alternance du groupe que vous voulez modifier (B1 ou B2 ou HE):");
		alternance = scan.next();
		
		System.out.println("Veuillez entrer le jour de la seance a assigner a la seance (Lundi...Vendredi):");
		nouveauJour = scan.next();
		System.out.println("Veuillez entrer l'heure de la seance a assigner a la seance (ex.: 09h30):");
		nouveauHeure = scan.next();
		System.out.println("Veuillez entrer la duree a assigner a la seance:");
		nouveauDuree = scan.next();
		System.out.println("Veuillez entrer l'alternance du groupe a assigner a la seance (B1 ou B2 ou HE):");
		nouveauAlternance = scan.next();
		System.out.println("Veuillez entrer le local a assigner a la seance:");
		nouveauLocal = scan.next();
		
		try
		{
			ResultSet resultat = stmt.executeQuery("UPDATE Seance " + "SET lelocal = '" + nouveauLocal + "', "
			                                       + "duree = '" + nouveauDuree + "', "
			                                       + "codJour = (SELECT codJour FROM Jour WHERE nom = '" + nouveauJour
			                                       + "'), " + "codHeure = (SELECT codHre FROM Heure WHERE hre = '"
			                                       + nouveauHeure + "'), " + "alternance = '" + nouveauAlternance
			                                       + "' WHERE groupe = '" + groupe + "' AND " + "sigle = '" + sigle
			                                       + "' AND " + "leType = '" + leType + "' AND "
			                                       + "codJour = (SELECT codJour FROM Jour WHERE nom = '" + jour
			                                       + "') AND " + "codHeure = (SELECT codHre FROM Heure WHERE hre = '"
			                                       + heure + "') AND " + "alternance = '" + alternance + "'");
			
			resultat = stmt.executeQuery("SELECT * FROM Seance s, Jour j, Heure h WHERE " + "s.sigle = '" + sigle
			                             + "' AND " + "s.codHeure = h.codHre AND " + "s.codJour = j.codJour AND "
			                             + "s.leType = '" + leType + "' AND "
			                             + "s.codJour = (SELECT codJour FROM Jour WHERE nom = '" + nouveauJour
			                             + "') AND " + "s.codHeure = (SELECT codHre FROM Heure WHERE hre = '"
			                             + nouveauHeure + "') AND " + "s.alternance = '" + nouveauAlternance + "'");
			
			while (resultat.next())
			{
				System.out.println("Type: " + resultat.getString("leType") + "\n" + "Groupe: "
				                   + resultat.getString("groupe") + "\n" + "Jour: " + resultat.getString("nom") + "\n"
				                   + "Heure: " + resultat.getString("hre") + "\n" + "Duree: "
				                   + resultat.getString("duree") + "\n" + "Alternance: "
				                   + resultat.getString("alternance") + "\n" + "Local: "
				                   + resultat.getString("lelocal") + "\n");
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		verifierRequete();
	}
	
	private void ajouterSeance()
	{
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
		
		String groupe, leType, jour, heure, alternance, lelocal, duree;
		
		System.out.println("Veuillez entrer le groupe a assigner a la seance");
		groupe = scan.next();
		System.out.println("Veuillez entrer le type de la seance que vous voulez ajouter (C ou L):");
		leType = scan.next();
		System.out.println("Veuillez entrer le jour a assigner a la seance (Lundi...Vendredi):");
		jour = scan.next();
		System.out.println("Veuillez entrer l'heure a assigner a la seance (ex.: 09h30):");
		heure = scan.next();
		System.out.println("Veuillez entrer la duree a assigner a la seance:");
		duree = scan.next();
		System.out.println("Veuillez entrer l'alternance du groupe a assigner a la seance (B1 ou B2 ou HE):");
		alternance = scan.next();
		System.out.println("Veuillez entrer le local a assigner a la seance:");
		lelocal = scan.next();
		
		try
		{
			ResultSet resultat = stmt.executeQuery("SELECT * FROM Jour j, Heure h WHERE hre = '" + heure
			                                       + "' AND nom = '" + jour + "'");
			resultat.next();
			
			int codJour = resultat.getInt("codJour"), codHeure = resultat.getInt("codHre");
			
			stmt.executeQuery("INSERT INTO Seance (sigle, leType, groupe, codJour, codHeure, alternance, duree, lelocal) "
			                  + "VALUES ('"
			                  + sigle
			                  + "', '"
			                  + leType
			                  + "', '"
			                  + groupe
			                  + "', '"
			                  + codJour
			                  + "', '"
			                  + codHeure + "', '" + alternance + "', '" + duree + "', '" + lelocal + "')");
			
			System.out.println("Vous avez ajoute la seance");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		verifierRequete();
	}
	
	private void supprimerSeance()
	{
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
		
		String groupe, leType, jour, heure, alternance;
		
		System.out.println("Veuillez entrer le groupe de la seance a supprimer");
		groupe = scan.next();
		System.out.println("Veuillez entrer le type de la seance a supprimer (C ou L):");
		leType = scan.next();
		System.out.println("Veuillez entrer le jour de la seance a supprimer (Lundi...Vendredi):");
		jour = scan.next();
		System.out.println("Veuillez entrer l'heure de la seance a supprimer (ex.: 09h30):");
		heure = scan.next();
		System.out.println("Veuillez entrer l'alternance de la seance a supprimer (B1 ou B2 ou HE):");
		alternance = scan.next();
		
		try
		{
			ResultSet resultat = stmt.executeQuery("SELECT * FROM Jour j, Heure h WHERE hre = '" + heure
			                                       + "' AND nom = '" + jour + "'");
			resultat.next();
			
			int codJour = resultat.getInt("codJour"), codHeure = resultat.getInt("codHre");
			
			resultat = stmt.executeQuery("DELETE FROM Seance " + "WHERE groupe = '" + groupe + "' AND " + "sigle = '"
			                             + sigle + "' AND " + "leType = '" + leType + "' AND " + "codJour = '"
			                             + codJour + "' AND " + "codHeure = '" + codHeure + "' AND " + "alternance = '"
			                             + alternance + "'");
			
			System.out.println("La seance a ete supprimee!");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		verifierRequete();
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
			
			if (reponse.equalsIgnoreCase("o") && reponse.equalsIgnoreCase("n"))
			{
				System.out.println("Erreur, veuillez entrer 'o' ou 'n'.");
			}
		} while (reponse.equalsIgnoreCase("o") && reponse.equalsIgnoreCase("n"));
		
		try
		{
			if (reponse.equals("o"))
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
