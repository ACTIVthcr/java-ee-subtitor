package com.subtitlor.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.subtitlor.model.SubtitleException;

public class SubtitlesHandler {

	private ArrayList<String> originalSubtitles = null;
	private ArrayList<String> translatedSubtitles = null;

	public SubtitlesHandler(BufferedReader originalContent, BufferedReader translatedContent) throws SubtitleException {
		originalSubtitles = new ArrayList<String>();
		translatedSubtitles = new ArrayList<>();
		try {
			String line;
			if (originalContent != null) {
				while ((line = originalContent.readLine()) != null) {
					originalSubtitles.add(line);
				}
				originalContent.close();
			}
			if (translatedContent != null) {
				while ((line = translatedContent.readLine()) != null) {
					translatedSubtitles.add(line);
				}
				translatedContent.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public SubtitlesHandler(BufferedReader originalContent) throws SubtitleException {
		originalSubtitles = new ArrayList<String>();
		try {
			String line;
			if (originalContent == null) {
				throw new SubtitleException("Une erreur est survenue");
			}
			while ((line = originalContent.readLine()) != null) {
				originalSubtitles.add(line);
			}
			originalContent.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<String> getOriginalSubtitles() {
		return originalSubtitles;
	}

	public List<String> getTranslatedSubtitles() {
		return translatedSubtitles;
	}

	public static boolean isModifiable(String line) {
		return !line.matches("^[0-9]{1,4}$")
				&& !line.matches("^[0-9]{2}:[0-9]{2}:[0-9]{2},[0-9]{3} --> [0-9]{2}:[0-9]{2}:[0-9]{2},[0-9]{3}$");
	}
}
