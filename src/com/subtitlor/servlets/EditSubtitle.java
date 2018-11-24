package com.subtitlor.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.subtitlor.dao.DaoException;
import com.subtitlor.dao.DaoFactory;
import com.subtitlor.dao.SubtitlesDao;
import com.subtitlor.model.SubtitleException;
import com.subtitlor.utilities.SubtitlesHandler;

@WebServlet("/EditSubtitle")
public class EditSubtitle extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private SubtitlesDao subtitlesDao;

	public void init() throws ServletException {
		DaoFactory daoFactory = DaoFactory.getInstance();
		this.subtitlesDao = daoFactory.getSubtitleDao();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String name = request.getParameter("name");
		request.setAttribute("name", name);
		BufferedReader originalContent = null;
		BufferedReader translatedContent = null;
		try {
			originalContent = subtitlesDao.getOriginalContent(name);
			translatedContent = subtitlesDao.getTranslatedContent(name);
		} catch (DaoException | SubtitleException e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", e.getMessage());
		}
		SubtitlesHandler subtitles = null;
		try {
			subtitles = new SubtitlesHandler(originalContent, translatedContent);
		} catch (SubtitleException e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", e.getMessage());
		}

		if (subtitles != null) {
			request.setAttribute("subtitles", subtitles.getOriginalSubtitles());
			request.setAttribute("translatedSubtitles", subtitles.getTranslatedSubtitles());
		}
		this.getServletContext().getRequestDispatcher("/WEB-INF/edit_subtitle.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String name = request.getParameter("name");
		try {
			BufferedReader originalContent = subtitlesDao.getOriginalContent(name);
			SubtitlesHandler subtitlesHandler = new SubtitlesHandler(originalContent);
			List<String> originalSubtitles = subtitlesHandler.getOriginalSubtitles();
			List<String> translatedSubtitles = new ArrayList<>();
			int i = 0;
			for (String originalLine : originalSubtitles) {
				String translatedLine = request.getParameter("line" + i);
				if (translatedLine != null && !translatedLine.equals("")) {
					translatedSubtitles.add(translatedLine);
				} else {
					translatedSubtitles.add(originalLine);
				}
				i++;
			}
			subtitlesDao.saveTranslation(translatedSubtitles, name);
		} catch (DaoException | SubtitleException e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", e.getMessage());
		}
		response.sendRedirect("/Subtitlor");
	}

}
