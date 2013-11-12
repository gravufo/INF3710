package ca.polymtl.inf3710.tp4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class testJDBCTP4
{
	public static void main(String args[])
	{
		
		String sigle = "";
		String titre = "";
		int NB_Credits = 0;
		String cycle = "";
		String resp = "";
		String dep = "";
		
		ResultSet rset = null;
		Connection connection = null;
		Statement stmt = null;
		
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
			connection = DriverManager.getConnection(url, "INF3710-133-32", "URLNHW");
		}
		catch (SQLException e)
		{
			// Could not connect to the database
			System.out.println(e.getMessage());
		}
		
		// Lecture du sigle du cours
		System.out.println("LISTE DES COURS\n");
		
		// Create statement
		try
		{
			stmt = connection.createStatement();
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}
		
		try
		{
			rset = stmt.executeQuery("select * from cours");
			while (rset.next())
			{
				
				sigle = rset.getString(1);
				titre = rset.getString(2);
				NB_Credits = rset.getInt(3);
				cycle = rset.getString(4);
				resp = rset.getString(5);
				dep = rset.getString(6);
				System.out.println(sigle + " " + titre + " " + NB_Credits);
			}
			stmt.close();
		}
		catch (SQLException e)
		{
			
		}
	}
}
