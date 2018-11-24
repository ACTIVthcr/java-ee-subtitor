package com.subtitlor.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.subtitlor.dao.DaoException;
import com.subtitlor.dao.DaoFactory;
import com.subtitlor.dao.SubtitlesDao;
import com.subtitlor.model.SubtitleException;
import com.subtitlor.model.Subtitles;

@WebServlet("/UploadSubtitles")
public class UploadSubtitles extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private SubtitlesDao subtitlesDao;

	public void init() throws ServletException {
		DaoFactory daoFactory = DaoFactory.getInstance();
		this.subtitlesDao = daoFactory.getSubtitleDao();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Part part = request.getPart("fichier");
		String name = getNomFichier(part);
		request.setAttribute("name", name);
		try {
			subtitlesDao.saveFile(part, getServletContext().getRealPath(Subtitles.FILE_LOCATION));
		} catch (DaoException | SubtitleException e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", e.getMessage());
		}
		request.getRequestDispatcher("WEB-INF/upload.jsp").forward(request, response);
	}

	private static String getNomFichier(Part part) {
		for (String contentDisposition : part.getHeader("content-disposition").split(";")) {
			if (contentDisposition.trim().startsWith("filename")) {
				return contentDisposition.substring(contentDisposition.indexOf('=') + 1).trim().replace("\"", "");
			}
		}
		return null;
	}
}
