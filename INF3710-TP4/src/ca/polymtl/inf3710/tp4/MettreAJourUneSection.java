package ca.polymtl.inf3710.tp4;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 * Créer, supprimer ou modifier les information raltives à une section de cours, incluant les enseignants.
 * 
 * @author Benjamin O'Connell-Armand
 * 
 * 
 */
public class MettreAJourUneSection
{
	Connection connection;
	
	public MettreAJourUneSection(Connection connection)
	{
		this.connection = connection;
	}
	
	public void executer()
	{
		try
		{
			boolean continuer = true;
			@SuppressWarnings("resource")
			Scanner scan = new Scanner(System.in);
			Statement stmt = connection.createStatement();
			
			do
			{
				
				System.out.println("Veuillez entrer le sigle du cours concerné: (ex: INF3710)");
				
				String sigle = scan.next().toUpperCase();
				
				// Affichage des informations du cours
				ResultSet infoCours = stmt.executeQuery("SELECT titre, nbCredit, cycle, "
				                                        + "d.nom AS departement, p.nom AS nom, prenom FROM "
				                                        + "Cours c, Departement d, Personne p "
				                                        + "WHERE c.idDept = d.idDept AND p.idPers = c.idPers "
				                                        + "AND sigle = '" + sigle + "'");
				
				Cours cours;
				
				if (infoCours.next())
				{
					Personne responsable = new Personne(infoCours.getString("nom"), infoCours.getString("prenom"));
					cours = new Cours(sigle, infoCours.getString("titre"), infoCours.getInt("nbCredit"),
					                  infoCours.getString("cycle"), responsable, infoCours.getString("departement"));
				}
				else
				{
					// scan.close();
					System.out.println("Sigle de cours invalide.");
					return;
				}
				
				infoCours.close();
				
				// Obtention des prérequis
				String prerequis = "";
				ResultSet infoPrerequis = stmt.executeQuery("SELECT preRequis FROM Prerequis WHERE cours = 'INF3710'");
				
				if (infoPrerequis.next())
					prerequis += (infoPrerequis.getString("preRequis"));
				while (infoPrerequis.next())
				{
					prerequis += ", " + infoPrerequis.getString("preRequis");
				}
				
				System.out.println(cours + "Prérequis: " + prerequis + System.lineSeparator());
				
				// Affichage des information de chacune des sections, triée par type
				ResultSet infoSection = stmt.executeQuery("SELECT leType, groupe FROM Section WHERE sigle = '" + sigle
				                                          + "' ORDER BY leType, groupe");
				
				while (infoSection.next())
				{
					String type = infoSection.getString("leType");
					int groupe = infoSection.getInt("groupe");
					Statement stmtTemp = connection.createStatement();
					ResultSet resultatEnseignant = stmtTemp.executeQuery("SELECT nom, prenom FROM Enseigner e, Personne p "
					                                                     + "WHERE e.idPers = p.idPers AND sigle ='"
					                                                     + sigle
					                                                     + "' "
					                                                     + "AND leType = '"
					                                                     + type
					                                                     + "' AND groupe = '" + groupe + "'");
					
					String enseigant = "";
					
					if (resultatEnseignant.next())
						enseigant += resultatEnseignant.getString("nom") + ", "
						             + resultatEnseignant.getString("prenom");
					while (resultatEnseignant.next())
					{
						enseigant += "; " + resultatEnseignant.getString("nom") + ", "
						             + resultatEnseignant.getString("prenom");
					}
					
					if (type.equals("L"))
						type = "Travaux pratiques";
					else
						type = "Cours";
					
					System.out.println("Type: " + type + System.lineSeparator() + "Groupe: " + groupe
					                   + System.lineSeparator() + "Enseignant(s): " + enseigant
					                   + System.lineSeparator());
					stmtTemp.close();
				}
				
				System.out.println("Voici les options disponibles:" + System.lineSeparator()
				                   + "1. Modifier une section" + System.lineSeparator() + "2. Ajouter une section"
				                   + System.lineSeparator() + "3. Supprimer une section" + System.lineSeparator()
				                   + "4. Retourner au menu principal" + System.lineSeparator()
				                   + "Veuillez entrer le chiffre correspondant a l'option desiree:");
				
				int choix = scan.nextInt();
				
				// Choix d'action
				while (choix < 1 || choix > 4)
				{
					System.out.println("La valeur entree est invalide. Veuillez entrer une valeur entre 1 et 4");
					choix = scan.nextInt();
				}
				
				switch (choix)
				{
					case 1:
						modifierSection(sigle);
						break;
					case 2:
						ajouterSection(sigle);
						break;
					case 3:
						supprimerSection(sigle);
						break;
					case 4:
						continuer = false;
						break;
					default:
						break;
				}
			} while (continuer);
			
			stmt.close();
			// scan.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	private void modifierSection(String sigle)
	{
		try
		{
			@SuppressWarnings("resource")
			Scanner scan = new Scanner(System.in);
			// Modifier une section?
			String type = "";
			do
			{
				System.out.println("La section à modifier est-elle une section de travaux pratiques(L) ou de cours(C)?");
				String entree = scan.next();
				
				if (entree.equalsIgnoreCase("c") || entree.equalsIgnoreCase("Cours"))
					type = "C";
				else if (entree.equalsIgnoreCase("L") || entree.equalsIgnoreCase("TP")
				         || entree.equalsIgnoreCase("Travaux pratiques") || entree.equalsIgnoreCase("Lab")
				         || entree.equalsIgnoreCase("Laboratoires"))
					type = "L";
			} while (type.isEmpty());
			
			System.out.println("Quel est le numéro de la section à modifier?");
			int section = scan.nextInt();
			
			Statement stmt = connection.createStatement();
			// Vérification
			ResultSet verification = stmt.executeQuery("SELECT * FROM Section WHERE sigle = '" + sigle
			                                           + "' AND leType = '" + type + "' AND groupe = '" + section
			                                           + "' ORDER BY leType, groupe");
			if (!verification.next())
			{
				System.out.println("Section invalide");
				return;
			}
			
			boolean continuee = true;
			while (continuee)
			{
				System.out.println("Voulez-vous:" + System.lineSeparator() + "1. Ajouter un enseignant"
				                   + System.lineSeparator() + "2. Supprimer un enseignant" + System.lineSeparator()
				                   + "3. Retour" + System.lineSeparator()
				                   + "Veuillez entrer le chiffre correspondant a l'option desiree:");
				
				int choix = scan.nextInt();
				
				switch (choix)
				{
					case 1:
						ajouterEnseignant(sigle, type, section);
						break;
					case 2:
						supprimerEnseigant(sigle, type, section);
						break;
					case 3:
						continuee = false;
						break;
					default:
						System.out.println("Option invalide.");
						break;
				}
			}
			
			stmt.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	private void ajouterEnseignant(String sigle, String type, int section)
	{
		// Ajouter enseigant?
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
		String nom, prenom = "";
		System.out.println("Quel est le prenom de l'enseignant à ajouter?");
		prenom = scan.next();
		System.out.println("Quel est le nom de famille de l'enseignant à ajouter?");
		nom = scan.next();
		
		try
		{
			Statement stmt = connection.createStatement();
			
			ResultSet verification1 = stmt.executeQuery("SELECT * FROM Enseigner e, Personne p "
			                                            + "WHERE e.idPers = p.idPers AND sigle = '" + sigle
			                                            + "' AND leType = '" + type + "' AND groupe = '" + section
			                                            + "' AND prenom = '" + prenom + "' AND nom = '" + nom + "'");
			if (verification1.next())
			{
				System.out.println("Cet enseignant enseigne déjà se à cette section.");
				return;
			}
			
			ResultSet obtentionId = stmt.executeQuery("SELECT idPers FROM Personne WHERE prenom = '" + prenom
			                                          + "' AND nom = '" + nom + "'");
			if (!obtentionId.next())
			{
				System.out.println("Cet enseignant n'existe pas!");
				return;
			}
			
			stmt.executeUpdate("INSERT INTO Enseigner(idPers, sigle, leType, groupe) VALUES ('"
			                   + obtentionId.getInt("idPers") + "', '" + sigle + "', '" + type + "', '" + section
			                   + "')");
			
			String fin = "Vous avez ajouté " + nom + ", " + prenom + " comme enseignant pour le groupe " + section
			             + " de ";
			if (type.equals("L"))
			{
				fin += "Travaux pratiques";
			}
			else
			{
				fin += "Cours";
			}
			fin += " du cours " + sigle + ".";
			
			System.out.println(fin);
			verifierRequete();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
	}
	
	private void supprimerEnseigant(String sigle, String type, int section)
	{
		// Supprimer enseigant
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
		String nom, prenom = "";
		System.out.println("Quel est le prenom de l'enseignant à supprimer?");
		prenom = scan.next();
		System.out.println("Quel est le nom de famille de l'enseignant à supprimer?");
		nom = scan.next();
		
		try
		{
			Statement stmt = connection.createStatement();
			
			ResultSet obtentionId = stmt.executeQuery("SELECT e.idPers AS muhId FROM Enseigner e, Personne p "
			                                          + "WHERE e.idPers = p.idPers AND sigle = '" + sigle
			                                          + "' AND leType = '" + type + "' AND groupe = '" + section
			                                          + "' AND prenom = '" + prenom + "' AND nom = '" + nom + "'");
			if (!obtentionId.next())
			{
				System.out.println("Cet enseignant n'enseigne pas à cette section.");
				return;
			}
			
			stmt.executeUpdate("DELETE FROM Enseigner WHERE idPers = '" + obtentionId.getInt("muhId")
			                   + "' AND sigle = '" + sigle + "' AND leType = '" + type + "' AND groupe = '" + section
			                   + "'");
			
			String fin = "Vous avez supprimé " + nom + ", " + prenom + " comme enseignant pour le groupe " + section
			             + " de ";
			if (type.equals("L"))
			{
				fin += "Travaux pratiques";
			}
			else
			{
				fin += "Cours";
			}
			fin += " du cours " + sigle + ".";
			
			System.out.println(fin);
			verifierRequete();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	private void ajouterSection(String sigle)
	{
		// Créer une section?
		try
		{
			@SuppressWarnings("resource")
			Scanner scan = new Scanner(System.in);
			// Modifier une section?
			String type = "";
			do
			{
				System.out.println("La section à ajouter sera-elle une section de travaux pratiques(L) ou de cours(C)?");
				String entree = scan.next();
				
				if (entree.equalsIgnoreCase("c") || entree.equalsIgnoreCase("Cours"))
					type = "C";
				else if (entree.equalsIgnoreCase("L") || entree.equalsIgnoreCase("TP")
				         || entree.equalsIgnoreCase("Travaux pratiques") || entree.equalsIgnoreCase("Lab")
				         || entree.equalsIgnoreCase("Laboratoires"))
					type = "L";
			} while (type.isEmpty());
			
			System.out.println("Quel est le numéro de la section à ajouter?");
			int section = scan.nextInt();
			
			Statement stmt = connection.createStatement();
			// Vérification
			ResultSet verification = stmt.executeQuery("SELECT * FROM Section WHERE sigle = '" + sigle
			                                           + "' AND leType = '" + type + "' AND groupe = '" + section
			                                           + "' ORDER BY leType, groupe");
			if (verification.next())
			{
				System.out.println("La section existe déjà");
				return;
			}
			
			// Ajout de la section
			stmt.executeUpdate("INSERT INTO Section (sigle, leType, groupe) VALUES ('" + sigle + "', '" + type + "', '"
			                   + section + "')");
			
			String fin = "Vous avez ajouter la section " + section + " de ";
			if (type.equals("L"))
			{
				fin += "Travaux pratiques";
			}
			else
			{
				fin += "Cours";
			}
			fin += " du cours " + sigle + ".";
			
			System.out.println(fin);
			verifierRequete();
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	private void supprimerSection(String sigle)
	{
		// Supprimer un section?
		try
		{
			@SuppressWarnings("resource")
			Scanner scan = new Scanner(System.in);
			// Modifier une section?
			String type = "";
			do
			{
				System.out.println("La section à supprimer est-elle une section de travaux pratiques(L) ou de cours(C)?");
				String entree = scan.next();
				
				if (entree.equalsIgnoreCase("c") || entree.equalsIgnoreCase("Cours"))
					type = "C";
				else if (entree.equalsIgnoreCase("L") || entree.equalsIgnoreCase("TP")
				         || entree.equalsIgnoreCase("Travaux pratiques") || entree.equalsIgnoreCase("Lab")
				         || entree.equalsIgnoreCase("Laboratoires"))
					type = "L";
			} while (type.isEmpty());
			
			System.out.println("Quel est le numéro de la section à supprimer?");
			int section = scan.nextInt();
			
			Statement stmt = connection.createStatement();
			// Vérification
			ResultSet verification = stmt.executeQuery("SELECT * FROM Section WHERE sigle = '" + sigle
			                                           + "' AND leType = '" + type + "' AND groupe = '" + section
			                                           + "' ORDER BY leType, groupe");
			if (!verification.next())
			{
				System.out.println("Section invalide");
				return;
			}
			
			// Suppression des séances lié
			stmt.executeUpdate("DELETE FROM Seance WHERE sigle = '" + sigle + "' AND leType = '" + type
			                   + "' AND groupe = '" + section + "'");
			
			// Suppression des enseignement
			stmt.executeUpdate("DELETE FROM Enseigner WHERE sigle = '" + sigle + "' AND leType = '" + type
			                   + "' AND groupe = '" + section + "'");
			
			// Suppression de la section
			stmt.executeUpdate("DELETE FROM Section WHERE sigle = '" + sigle + "' AND leType = '" + type
			                   + "' AND groupe = '" + section + "'");
			
			String fin = "Vous avez supprimé la section " + section + " de ";
			if (type.equals("L"))
			{
				fin += "Travaux pratiques";
			}
			else
			{
				fin += "Cours";
			}
			fin += " du cours " + sigle + ".";
			
			System.out.println(fin);
			verifierRequete();
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	private void verifierRequete()
	{
		String reponse;
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
		
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
			
		}
	}
}
