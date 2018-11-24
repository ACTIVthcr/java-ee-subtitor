package com.subtitlor.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.subtitlor.dao.DaoException;
import com.subtitlor.dao.DaoFactory;
import com.subtitlor.dao.SubtitlesDao;

@WebServlet("/Accueil")
public class Accueil extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private SubtitlesDao subtitlesDao;

	public void init() throws ServletException {
		DaoFactory daoFactory = DaoFactory.getInstance();
		this.subtitlesDao = daoFactory.getSubtitleDao();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			List<String> subtitleNameList = subtitlesDao.getSubtitlesListName();
			request.setAttribute("subtitlesListName", subtitleNameList);
		} catch (DaoException e) {
			request.setAttribute("errorMessage", e.getMessage());
		}
		this.getServletContext().getRequestDispatcher("/WEB-INF/accueil.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

}
