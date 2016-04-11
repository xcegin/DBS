package entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class Zviera {

	private int did;
	private String name;
	private Boolean hungry;
	private int weight;
	private String type_id;
	private int chliev_id;
	private Date date_added;

	public int getDid() {
		return did;
	}

	public void setDid(int did) {
		this.did = did;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getHungry() {
		return hungry;
	}

	public void setHungry(Boolean hungry) {
		this.hungry = hungry;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public String getType_id() {
		return type_id;
	}

	public void setType_id(String type_id) {
		this.type_id = type_id;
	}

	public int getChliev_id() {
		return chliev_id;
	}

	public void setChliev_id(int chliev_id) {
		this.chliev_id = chliev_id;
	}

	public Date getDate_added() {
		return date_added;
	}

	public void setDate_added(Date date_added) {
		this.date_added = date_added;
	}
	public String information() throws ClassNotFoundException {
		return "Meno: " + this.getName() + ", ID: " + this.getDid() + ", Vaha: " + this.getWeight() + ", Typ zvierata: "
			+ getIdFromTypeofAnimal() + ", Bolo hladné: " + this.getHungry().toString() + ", ID chlieva: "
			+ this.getChliev_id() + ", Pridane dna:´" + this.getDate_added() + ".";
	}

	public void makeItHappen(int did) throws ClassNotFoundException {
		Connection c = null;
		Statement stmt = null;
		try {

			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5121/polnohosp", "postgres", "chidorinagashi");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM ZVIERA WHERE DID=" + did + ";");
			while (rs.next()) {
				System.out.println();
				this.setDid(rs.getInt("did"));
				this.setName(rs.getString("name"));
				this.setWeight(rs.getInt("weight"));
				this.setDate_added(rs.getDate("date_added"));
				this.setChliev_id(rs.getInt("chliev_id"));
				this.setHungry(rs.getBoolean("hungry"));
				this.setType_id(rs.getString("type_id"));
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
			System.out.print(this.getType_id());
			ResultSet rs = stmt.executeQuery("SELECT typ FROM zviera_typ WHERE code LIKE '" + this.getType_id() + "';");
			while (rs.next()) {
				code = rs.getString("typ");
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
