package Services;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.HashMap;

public class LocalizationService {
	private static HashMap<String, String> lookup;
	private static String currentLang;

	public static String localizeString(String id) {
		if (currentLang == null) {
			setLanguage("EN");
		}

		if (lookup != null) {
			String str = lookup.get(id);
			return (str == null) ? id : str;
		} else
			return null;
	}

	public static void setLanguage(String code) {
		Path path = FileSystems.getDefault().getPath("locale", "lang_" + code + ".ini");
		try {
			String[] localeFile = new String(Files.readAllBytes(path), Charset.forName("UTF-8")).split(";;;");
			setLangStrings(localeFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		currentLang = code;
	}

	public static String getLanguage() {
		return currentLang;
	}

	private static void setLangStrings(String[] localeFile) {
		if (lookup == null)
			lookup = new HashMap<>();

		lookup.clear();
		for (String s : localeFile) {

			String[] pair = s.split("=", 2);
			pair[0] = pair[0].trim();
			try {
				lookup.put(pair[0], pair[1]);
			} catch (ArrayIndexOutOfBoundsException e) {
			}
		}
	}

}