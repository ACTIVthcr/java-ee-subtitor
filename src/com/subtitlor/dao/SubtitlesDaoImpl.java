package com.subtitlor.dao;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Part;

import com.subtitlor.model.SubtitleException;
import com.subtitlor.model.Subtitles;

public class SubtitlesDaoImpl implements SubtitlesDao {

	private DaoFactory daoFactory;

	public SubtitlesDaoImpl(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public void saveFile(Part part, String path) throws DaoException, SubtitleException {
		// On vérifie qu'on a bien reçu un fichier
		String nomFichier = getNomFichier(part);

		if (!Subtitles.isSrtFile(nomFichier)) {
			throw new SubtitleException("fichier de sous-titre non compatible");
		}

		// Si on a bien un fichier
		if (nomFichier != null && !nomFichier.isEmpty()) {

			// Corrige un bug du fonctionnement d'Internet Explorer
			nomFichier = nomFichier.substring(nomFichier.lastIndexOf('/') + 1)
					.substring(nomFichier.lastIndexOf('\\') + 1);

			// On enregistre le fichier dans la base de données.
			try (Connection connexion = this.daoFactory.getConnection()) {
				String sql = "INSERT INTO Subtitles (name,original) values (?,?)";
				try (PreparedStatement preparedStatement = connexion.prepareStatement(sql)) {
					preparedStatement.setString(1, nomFichier);
					try (InputStream input = new BufferedInputStream(part.getInputStream(), Subtitles.TAILLE_TAMPON)) {
						preparedStatement.setBlob(2, input);
						preparedStatement.executeUpdate();
					}
				} catch (SQLException e) {
					e.printStackTrace();
					connexion.rollback();
					throw new DaoException(DaoFactory.DEFAULT_MESSAGE);
				}
				connexion.commit();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new DaoException(DaoFactory.DEFAULT_MESSAGE);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public BufferedReader getOriginalContent(String name) throws DaoException, SubtitleException {
		return getSelectedContent(name, "original");
	}

	@Override
	public BufferedReader getTranslatedContent(String name) throws DaoException, SubtitleException {
		return getSelectedContent(name, "translated");
	}

	private BufferedReader getSelectedContent(String name, String columnName) throws SubtitleException {
		BufferedReader bufferedReader = null;
		try {
			Connection connection = daoFactory.getConnection();

			String sql = "SELECT " + columnName + " FROM subtitles WHERE name=?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, name);

			ResultSet result = statement.executeQuery();
			if (result.next()) {
				Blob blob = result.getBlob(columnName);
				if (blob == null) {
					return null;
				}
				InputStream inputStream = blob.getBinaryStream();
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return bufferedReader;
	}

	private static String getNomFichier(Part part) {
		for (String contentDisposition : part.getHeader("content-disposition").split(";")) {
			if (contentDisposition.trim().startsWith("filename")) {
				return contentDisposition.substring(contentDisposition.indexOf('=') + 1).trim().replace("\"", "");
			}
		}
		return null;
	}

	@Override
	public List<String> getSubtitlesListName() throws DaoException {
		List<String> subtitleList = new ArrayList<>();

		try (Connection connection = daoFactory.getConnection()) {
			String sql = "SELECT name from subtitles";
			try (Statement statement = connection.createStatement()) {
				ResultSet result = statement.executeQuery(sql);
				while (result.next()) {
					subtitleList.add(result.getString(1));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException(DaoFactory.DEFAULT_MESSAGE);
		}

		return subtitleList;
	}

	@Override
	public void saveTranslation(List<String> translatedSubtitles, String name) throws DaoException {
		String completeString = "";
		for (String lineTranslated : translatedSubtitles) {
			completeString += lineTranslated + "\n";
		}
		InputStream inputStream = new ByteArrayInputStream(completeString.getBytes(StandardCharsets.UTF_8));
		try (Connection connexion = this.daoFactory.getConnection()) {
			String sql = "UPDATE Subtitles SET translated = ? WHERE name = ?";
			try (PreparedStatement preparedStatement = connexion.prepareStatement(sql)) {
				try (InputStream input = new BufferedInputStream(inputStream, Subtitles.TAILLE_TAMPON)) {
					preparedStatement.setBlob(1, input);
					preparedStatement.setString(2, name);
					preparedStatement.executeUpdate();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				connexion.rollback();
				throw new DaoException(DaoFactory.DEFAULT_MESSAGE);
			}
			connexion.commit();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException(DaoFactory.DEFAULT_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
