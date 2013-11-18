package ca.polymtl.inf3710.tp4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class SelectionMethode
{
	static String     username   = "INF3710-133-32";
	static String     password   = "URLNHW";
	static Connection connection = null;
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		Scanner scan = new Scanner(System.in);
		
		do
		{
			System.out.println("Veuillez entrer le nom d'usager:");
			
			//username = scan.next();
			
			System.out.println("Veuillez entrer le mot de passe du compte:");
			
			//password = scan.next();
			
		} while (!initialiserConnection());
		
		while (true)
		{
			int methodeSelectionnee = 0;
			
			System.out.println("Voici la liste des méthodes disponibles: \n"
			                   + "1. Afficher l'emploi du temps d'un cours\n" + "2. Mettre à jour une section\n"
			                   + "3. Mettre à jour l'emploi du temps d'une section\n" + "4. Quitter\n\n"
			                   + "Veuillez entrer le chiffre correspondant à la fonction désirée:");
			
			methodeSelectionnee = scan.nextInt();
			
			switch (methodeSelectionnee)
			{
				case 1:
					AfficherEmploisDuTemps a = new AfficherEmploisDuTemps(connection);
					a.executer();
					break;
				case 2:
					MettreAJourUneSection m = new MettreAJourUneSection(connection);
					m.executer();
					break;
				case 3:
					break;
				case 4:
					try
					{
						connection.close();
					}
					catch (SQLException e)
					{
						e.printStackTrace();
					}
					scan.close();
					System.exit(0);
					break;
				default:
					System.out.println("La valeur entree n'est pas valide. Veuillez recommencer.");
					break;
			}
		}
	}
	
	public static boolean initialiserConnection()
	{
		String driverName = "oracle.jdbc.driver.OracleDriver"; // for Oracle
		
		try
		{
			// Load the JDBC driver
			Class.forName(driverName);
		}
		catch (ClassNotFoundException e)
		{
			// Could not find the database driver
			System.out.println("ClassNotFoundException : " + e.getMessage());
		}
		
		String serverName = "ora-labos.labos.polymtl.ca";
		String portNumber = "2001";
		String sid = "LABOS";
		
		String url = "jdbc:oracle:thin:@" + serverName + ":" + portNumber + ":" + sid; // for Oracle
		
		try
		{
			// Create a connection to the database
			connection = DriverManager.getConnection(url, username, password);
		}
		catch (SQLException e)
		{
			// Could not connect to the database
			System.out.println(e.getMessage());
			return false;
		}
		
		return true;
	}
}
