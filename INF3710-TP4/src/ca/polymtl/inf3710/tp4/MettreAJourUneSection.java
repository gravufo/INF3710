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
			Scanner scan = new Scanner(System.in);
			
			System.out.println("Veuillez entrer le sigle du cours concerné:");
			
			String sigle = scan.next();
			
			Statement stmt = connection.createStatement();
			
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
				scan.close();
				return;
			}
			
			System.out.println(cours);
			
			// Sigle, Titre, Nombre de crédits, Cycle, Département, Reponsable
			// SELECT titre, nbCredit, cycle, d.nom AS departement, p.nom AS nom, prenom FROM Cours c, Departement d,
			// Personne p WHERE c.idDept = d.idDept AND p.idPers = c.idPers AND sigle = 'INF3710';
			// Obtention des prérequis
			// SELECT preRequis FROM Prerequis WHERE cours = 'INF3710';
			
			// TODO
			// Affichage des information de chacune des sections, triée par type
			// Type, groupe, enseignants
			// SELECT s.leType, s.groupe, p.nom, p.prenom FROM Section s, Enseigner e, Personne p WHERE s.sigle =
			// e.sigle AND s.leType = e.leType AND s.groupe = e.groupe AND e.idPers = p.idPers AND s.sigle ='INF1995';
			
			// TODO
			// Choix d'action
			// TODO
			// Créer une section?
			// Type?
			// Numéro?
			// Enseignant(s)?
			
			// TODO
			// Supprimer un section?
			// Type?
			// Numéro?
			
			// TODO
			// Modifier une section?
			// Laquel?
			// Type?
			// Numéro?
			// TODO
			// Modifier le Type?
			// TODO
			// Modifier le numero?
			// TODO
			// Ajouter enseigant?
			// NOm?
			// Prenom?
			// TODO
			// Supprimer enseigant
			// Nom?
			// Prenom?
			
			String reponse;
			do
			{
				System.out.println("Voulez-vous envoyer les changements (COMMIT) (o ou n)?");
				
				reponse = scan.next();
				
				if (!reponse.equalsIgnoreCase("o") && !reponse.equalsIgnoreCase("n"))
				{
					System.out.println("Erreur, veuillez entrer 'o' ou 'n'.");
				}
			} while (!reponse.equalsIgnoreCase("o") && !reponse.equalsIgnoreCase("n"));
			
			if (reponse.equalsIgnoreCase("o"))
			{
				connection.commit();
			}
			else
			{
				connection.rollback();
			}
			
			scan.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
}
