package ca.polymtl.inf3710.tp4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MettreAJourEmploiDuTemps
{
	Connection connection;
	
	public MettreAJourEmploiDuTemps(Connection connection)
	{
		this.connection = connection;
	}
	
	public void executer()
	{
		try
		{
			BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			
			System.out.println("Veuillez entrer le sigle du cours a supprimer:");
			
			String sigle = bufferRead.readLine();
			
			System.out.println("Veuillez entrer le titre du cours désiré:");
			
			String titre = bufferRead.readLine();
			
			Statement stmt = connection.createStatement();
			
			// todo
			ResultSet result = stmt.executeQuery("SELECT c.titre, c.nbCredit, c.cycle, d.nom AS nomDept, p.nom AS nomPers " +
					"FROM Cours c, Departement d, Personne p " +
					"WHERE c.idPers = p.idPers AND " +
					"c.sigle = '" + sigle + "' AND " +
					"c.idDept = d.idDept");
			
			while (result.next())
			{
				// todo
				System.out.println("\nSigle: " + sigle + "\n"
						+ "Titre du cours: " + result.getString("titre") + "\n"
						+ "Nombre de credits: " + result.getString("nbCredit") + "\n"
						+ "Cycle: " + result.getString("cycle") + "\n"
						+ "Responsable(s): " + result.getString("nomPers") + "\n"
						+ "Departement(s): " + result.getString("nomDept") + "\n");
			}
			
			String reponse;
			do
			{
				System.out.println("Voulez-vous envoyer les changements (COMMIT) (o ou n)?");
				
				reponse = bufferRead.readLine();
				
				if(reponse != "o" && reponse != "n")
				{
					System.out.println("Erreur, veuillez entrer 'o' ou 'n'.");
				}
			}while(reponse != "o" && reponse != "n");
			
			if(reponse == "o")
			{
				connection.commit();
			}
			else
			{
				connection.rollback();
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
}
