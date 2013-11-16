package ca.polymtl.inf3710.tp4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AfficherEmploisDuTemps
{
	Connection connection = null;
	
	public AfficherEmploisDuTemps(Connection connection)
	{
		this.connection = connection;
	}
	
	public void executer()
	{
		try
		{
			BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			
			System.out.println("Veuillez entrer le sigle du cours désiré:");
			
			String sigle = bufferRead.readLine();
			
			Statement stmt = connection.createStatement();
			ResultSet result = stmt.executeQuery("SELECT c.titre, c.nbCredit, c.cycle, d.nom AS nomDept, p.nom AS nomPers " +
					"FROM Cours c, Departement d, Personne p" +
					" WHERE c.idPers = p.idPers AND" +
					" c.sigle = '" + sigle + "' AND" +
					" c.idDept = d.idDept");
			
			while (result.next())
			{
				System.out.println("\nSigle: " + sigle + "\n"
						+ "Titre du cours: " + result.getString("titre") + "\n"
						+ "Nombre de credits: " + result.getString("nbCredit") + "\n"
						+ "Cycle: " + result.getString("cycle") + "\n"
						+ "Responsable(s): " + result.getString("nomPers") + "\n"
						+ "Departement(s): " + result.getString("nomDept") + "\n");
			}
			
			result = stmt.executeQuery("SELECT c.titre, c.nbCredit, c.cycle, d.nom AS nomDept, p.nom AS nomPers " +
					"FROM Seance c " +
					"WHERE c.sigle = s.sigle AND " +
					"s.sigle = '" + sigle + "' AND " +
					"c.idDept = d.idDept ");
			
			while (result.next())
			{
				System.out.println("\nSigle: " + sigle + "\n"
						+ "Titre du cours: " + result.getString("titre") + "\n"
						+ "Nombre de credits: " + result.getString("nbCredit") + "\n"
						+ "Cycle: " + result.getString("cycle") + "\n"
						+ "Responsable(s): " + result.getString("nomPers") + "\n"
						+ "Departement(s): " + result.getString("nomDept") + "\n");
			}
			
			result.close();
			stmt.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
