package com.subtitlor.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.subtitlor.dao.DaoException;
import com.subtitlor.dao.DaoFactory;
import com.subtitlor.dao.SubtitlesDao;
import com.subtitlor.model.SubtitleException;

@WebServlet("/DownloadTranslation")
public class DownloadTranslation extends HttpServlet {
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
		String name = request.getParameter("name");
		BufferedReader content = null;
		try {
			content = this.subtitlesDao.getTranslatedContent(name);
		} catch (DaoException | SubtitleException e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", e.getMessage());
		}
		// forces download
		String headerKey = "Content-Disposition";
		String outputName = name.substring(0, name.lastIndexOf('.'));
		outputName = outputName + "_translated.srt";
		String headerValue = String.format("attachment; filename=\"%s\"", outputName);
		response.setHeader(headerKey, headerValue);

		OutputStream out = response.getOutputStream();

		if (content != null) {
			String line;
			while ((line = content.readLine()) != null) {
				line = line + "\n";
				char[] lineByte = line.toCharArray();
				for (int i = 0; i < lineByte.length; i++) {
					out.write(lineByte[i]);
				}
			}
			content.close();
		}
		out.close();
	}

}
