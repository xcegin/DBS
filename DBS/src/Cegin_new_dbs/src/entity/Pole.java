package entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Pole {

	private String rastlina_id;

	public String getRastlina_id() {
		return rastlina_id;
	}

	public void setRastlina_id(String rastlina_id) {
		this.rastlina_id = rastlina_id;
	}

	public int getVelkost() {
		return velkost;
	}

	public void setVelkost(int kapacita) {
		this.velkost = kapacita;
	}

	public int getDid() {
		return did;
	}

	public void setDid(int did) {
		this.did = did;
	}

	private int velkost;

	private int did;

	public String information() throws ClassNotFoundException {
		return "ID: " + this.getDid() + ", Velkost: " + this.getVelkost() + ", Typ rastliny: " + getIdFromTypeofAnimal()
			+ ".";
	}

	private String getIdFromTypeofAnimal() throws ClassNotFoundException {
		Connection c = null;
		Statement stmt = null;
		String code = null;
		try {

			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5121/polnohosp", "postgres", "chidorinagashi");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			System.out.print(this.getRastlina_id());
			ResultSet rs = stmt
				.executeQuery("SELECT type FROM rastlina WHERE code LIKE '" + this.getRastlina_id() + "';");
			while (rs.next()) {
				code = rs.getString("type");
				System.out.println();
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (SQLException ex) {

			if (c != null) {
				try {
					c.rollback();
				} catch (SQLException ex1) {
					System.err.println(ex1.getClass().getName() + ": " + ex1.getMessage());
					System.exit(0);
				}
			}
		}
		return code;
	}

}
