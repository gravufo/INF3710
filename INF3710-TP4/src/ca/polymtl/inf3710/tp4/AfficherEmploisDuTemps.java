package ca.polymtl.inf3710.tp4;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

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
			@SuppressWarnings("resource")
			Scanner scan = new Scanner(System.in);
			
			System.out.println("Veuillez entrer le sigle du cours désiré:");
			
			String sigle = scan.next();
			
			// scan.close();
			
			Statement stmt = connection.createStatement();
			ResultSet result = stmt.executeQuery("SELECT c.titre, c.nbCredit, c.cycle, d.nom AS nomDept, p.nom AS nomPers, p.prenom "
			                                     + "FROM Cours c, Departement d, Personne p "
			                                     + "WHERE c.idPers = p.idPers AND "
			                                     + "c.sigle = '"
			                                     + sigle
			                                     + "' AND "
			                                     + "c.idDept = d.idDept");
			
			while (result.next())
			{
				System.out.println("\nSigle: " + sigle + "\n" + "Titre du cours: " + result.getString("titre") + "\n"
				                   + "Nombre de credits: " + result.getString("nbCredit") + "\n" + "Cycle: "
				                   + result.getString("cycle") + "\n" + "Responsable(s): "
				                   + result.getString("nomPers") + ", " + result.getString("prenom") + "\n"
				                   + "Departement(s): " + result.getString("nomDept"));
			}
			
			result = stmt.executeQuery("SELECT * " + "FROM Prerequis " + "WHERE cours = '" + sigle + "'");
			
			while (result.next())
			{
				System.out.println("Prerequis: " + result.getString("preRequis") + "\n");
			}
			
			result = stmt.executeQuery("SELECT * " + "FROM Seance s, Jour j, Heure h " + "WHERE sigle = '" + sigle
			                           + "' AND " + "s.codJour = j.codJour AND " + "s.codHeure = h.codHre");
			
			while (result.next())
			{
				System.out.println("Type: " + result.getString("leType") + "\n" + "Groupe: "
				                   + result.getString("groupe") + "\n" + "Jour: " + result.getString("nom") + "\n"
				                   + "Heure: " + result.getString("hre") + "\n" + "Alternance: "
				                   + result.getString("alternance") + "\n" + "Local: " + result.getString("lelocal")
				                   + "\n");
			}
			
			result.close();
			stmt.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
}
