package task02_exponentiation;

import java.util.HashMap;

public class Translator {
	public static final Translator ToSuperscriptedDecimalStr = new Translator("+-0123456789",
			"\u207a\u207b\u2070\u00b9\u00b2\u00b3\u2074\u2075\u2076\u2077\u2078\u2079");

	public static final Translator FromSuperscriptedToNormalDecimalStr = new Translator(
			"\u207a\u207b\u2070\u00b9\u00b2\u00b3\u2074\u2075\u2076\u2077\u2078\u2079", "+-0123456789");

	private HashMap<Character, Character> translationMap;

	public Translator(String from, String to) {
		int len = Math.min(from.length(), to.length());
		if (len <= 0)
			return;

		translationMap = new HashMap<Character, Character>();

		for (int i = 0; i < len; ++i) {
			char k = from.charAt(i);
			char v = to.charAt(i);
			translationMap.put(k, v);
		}
	}

	public String translate(String str) {
		if (translationMap == null || str == null || str.isEmpty())
			return str;

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < str.length(); ++i) {
			char c = str.charAt(i);
			if (translationMap.containsKey(c)) {
				sb.append(translationMap.get(c));
			} else {
				sb.append(c);
			}
		}

		return sb.toString();
	}
}
