package ca.polymtl.inf3710.tp4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class MettreAJourEmploiDuTemps
{
	private Connection connection;
	private String sigle;
	
	public MettreAJourEmploiDuTemps(Connection connection)
	{
		this.connection = connection;
	}
	
	public void executer()
	{
		try
		{
			Scanner scan = new Scanner(System.in);
			
			System.out.println("Veuillez entrer le sigle du cours a supprimer:");
			
			sigle = scan.next();
			
			Statement stmt = connection.createStatement();
			
			ResultSet result = stmt.executeQuery("SELECT * " +
					"FROM Seance s, Jour j, Heure h " +
					"WHERE sigle = '" + sigle + "' AND " +
							"s.codJour = j.codJour AND " +
							"s.codHeure = h.codHre");
			
			while (result.next())
			{
				System.out.println("Type: " + result.getString("leType")+ "\n"
						+ "Groupe: " + result.getString("groupe") + "\n"
						+ "Jour: " + result.getString("nom") + "\n"
						+ "Heure: " + result.getString("hre") + "\n"
						+ "Alternance: " + result.getString("alternance") + "\n"
						+ "Local: " + result.getString("lelocal") + "\n");
			}
			
			System.out.println("Voici les options disponibles:\n" +
					"1. Modifier une seance\n" +
					"2. Ajouter une seance\n" +
					"3. Supprimer une seance\n" +
					"4. Retourner au menu principal\n" +
					"Veuillez entrer le chiffre correspondant a l'option desiree:");
			
			int choix = scan.nextInt();
			
			while(choix < 1 || choix > 4)
			{
				System.out.println("La valeur entree est invalide. Veuillez entrer une valeur entre 1 et 4");
				choix = scan.nextInt();
			}
			
			switch(choix)
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
				break;
			default:
				break;
			}
			
			scan.close();
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	private void modifierSeance()
	{
		
	}
	
	private void ajouterSeance()
	{
		
	}
	
	private void supprimerSeance()
	{
	}
	
	private void verifierRequete()
	{
		String reponse;
		Scanner scan = new Scanner(System.in);
		
		do
		{
			System.out.println("Voulez-vous envoyer les changements (COMMIT) (o ou n)?");
			
			reponse = scan.next();
			
			if(reponse != "o" && reponse != "n")
			{
				System.out.println("Erreur, veuillez entrer 'o' ou 'n'.");
			}
		}while(reponse != "o" && reponse != "n");
		
		scan.close();
		
		try
		{
			if(reponse == "o")
			{
				connection.commit();
			}
			else
			{
				connection.rollback();
			}
		}
		catch(SQLException e)
		{
			
		}
	}
}
