package com.subtitlor.dao;

import java.io.BufferedReader;
import java.util.List;

import javax.servlet.http.Part;

import com.subtitlor.model.SubtitleException;

public interface SubtitlesDao {

	void saveFile(Part part, String path) throws DaoException, SubtitleException;

	BufferedReader getOriginalContent(String name) throws DaoException, SubtitleException;

	List<String> getSubtitlesListName() throws DaoException;

	void saveTranslation(List<String> translatedSubtitles, String name) throws DaoException;

	BufferedReader getTranslatedContent(String name) throws DaoException, SubtitleException;

}
