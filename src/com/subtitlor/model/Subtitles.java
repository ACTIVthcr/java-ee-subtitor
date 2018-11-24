package com.subtitlor.model;

public class Subtitles {

	public static final int TAILLE_TAMPON = 10240;
	public static final String FILE_LOCATION = "WEB-INF/subtitle-uploaded/";

	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static boolean isSrtFile(String name) {
		// check if name finish by .srt
		String extension = name.substring(name.lastIndexOf(".") + 1, name.length());
		if (!extension.equals("srt")) {
			return false;
		}
		return true;
	}
}
