package com.subtitlor.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import com.subtitlor.dao.DaoException;
import com.subtitlor.dao.DaoFactory;

public class ResetDatabase {

	public static void main(String[] args) throws DaoException {
		DaoFactory daoFactory = DaoFactory.getInstance();
		Connection connexion = null;
		try {
			connexion = daoFactory.getConnection();
			String dropDatabseIfExists = "DROP DATABASE IF EXISTS " + DaoFactory.DATABASE_NAME + ";";
			dropDatabase(connexion, dropDatabseIfExists);
			String createDatabase = "CREATE DATABASE " + DaoFactory.DATABASE_NAME
					+ " DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;";
			createDatabase(connexion, createDatabase);
			String querySetLimit = "SET GLOBAL max_allowed_packet=104857600;"; // 10 MB
			setMaxLimitAllowedPacket(connexion, querySetLimit);
			String useDatabase = "USE " + DaoFactory.DATABASE_NAME + ";"; //
			useDatabase(connexion, useDatabase);
			String insertTable = " CREATE TABLE SUBTITLES (" //
					+ "id INT(11) NOT NULL AUTO_INCREMENT," //
					+ "name VARCHAR(200) NOT NULL," //
					+ "original MEDIUMBLOB," //
					+ "translated MEDIUMBLOB," //
					+ "PRIMARY KEY ( id )" //
					+ ") ENGINE = INNODB;"; //
			insertTable(connexion, insertTable);
			String insertData = "INSERT INTO SUBTITLES (name, original) values (?,?)";
			insertData(connexion, insertData);
			connexion.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			if (connexion != null) {
				try {
					connexion.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
					throw new DaoException(DaoFactory.DEFAULT_MESSAGE);
				}
			}
			throw new DaoException(DaoFactory.DEFAULT_MESSAGE);
		}

	}

	private static void useDatabase(Connection connexion, String useDatabase) throws SQLException, DaoException {
		try (Statement queryUseDatabase = connexion.createStatement()) {
			queryUseDatabase.executeQuery(useDatabase);
		} catch (SQLException e) {
			e.printStackTrace();
			connexion.rollback();
			throw new DaoException(DaoFactory.DEFAULT_MESSAGE);
		}
	}

	private static void createDatabase(Connection connexion, String createDatabase) throws SQLException, DaoException {
		try (Statement queryCreateDatabase = connexion.createStatement()) {
			queryCreateDatabase.execute(createDatabase);
			System.out.println("SUCCESS TO CREATE DATABASE");
		} catch (SQLException e) {
			e.printStackTrace();
			connexion.rollback();
			throw new DaoException(DaoFactory.DEFAULT_MESSAGE);
		}
	}

	private static void insertData(Connection connexion, String insertData) throws SQLException {
		try (PreparedStatement queryInsertData = connexion.prepareStatement(insertData)) {
			queryInsertData.setString(1, "password_presentation.srt");
			try (InputStream inputStream = new FileInputStream(
					new File("WebContent/WEB-INF/subtitle-uploaded/password_presentation.srt"))) {
				queryInsertData.setBlob(2, inputStream);

				int row = queryInsertData.executeUpdate();
				if (row > 0) {
					System.out.println("SUCCESS TO INSERT DATA");
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void insertTable(Connection connexion, String insertTable) throws SQLException, DaoException {
		try (Statement queryInsertTable = connexion.createStatement()) {
			queryInsertTable.execute(insertTable);
			System.out.println("SUCCESS TO CREATE TABLE");
		} catch (SQLException e) {
			e.printStackTrace();
			connexion.rollback();
			throw new DaoException(DaoFactory.DEFAULT_MESSAGE);
		}
	}

	private static void setMaxLimitAllowedPacket(Connection connexion, String querySetLimit)
			throws SQLException, DaoException {
		try (Statement queryInsertTable = connexion.createStatement()) {
			queryInsertTable.execute(querySetLimit);
			System.out.println("SUCCESS TO SET LIMIT");
		} catch (SQLException e) {
			connexion.rollback();
			throw new DaoException(DaoFactory.DEFAULT_MESSAGE);
		}
	}

	private static void dropDatabase(Connection connexion, String dropDatabseIfExists)
			throws SQLException, DaoException {
		try (Statement dropDatabse = connexion.createStatement()) {
			dropDatabse.execute(dropDatabseIfExists);
			System.out.println("SUCCESS TO DROP DATABASE WITH NAME: " + DaoFactory.DATABASE_NAME);
		} catch (SQLException e) {
			e.printStackTrace();
			connexion.rollback();
			throw new DaoException(DaoFactory.DEFAULT_MESSAGE);
		}
	}

}
