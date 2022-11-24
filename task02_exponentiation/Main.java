package task02_exponentiation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;
import java.util.function.Function;
import java.nio.file.Paths;

// Реализовать функцию возведения числа а в степень b. a, b ∈ Z. Сводя количество выполняемых действий к минимуму.
// Пример 1: а = 3, b = 2, ответ: 9
// Пример 2: а = 2, b = -2, ответ: 0.25
// Пример 3: а = 3, b = 0, ответ: 1
// Пример 4: а = 0, b = 0, ответ: не определено
// Пример 5
// входные данные находятся в файле input.txt в виде
// b 3
// a 10
// Результат нужно сохранить в файле output.txt
// 1000

public class Main {
	private static final String TITLE = "Возведение числа а в степень b : a, b ∈ Z";

	private static final String PATH_TO_FILES = "task02_exponentiation";
	// Paths.get(System.getProperty("user.dir"),"task02_exponentiation").toString();
	private static final String FILE_NAME_INPUT = "input.txt";
	private static final String FILE_NAME_OUTPUT = "output.txt";

	private static final String PARAM_A_NAME = "a";
	private static final String PARAM_B_NAME = "b";

	private static final String PLEASE_REPEAT = "Пожалуйста попробуйте снова.";
	private static final String ERROR_NOT_INT = "Некорректный ввод: Требуется целое число. " + PLEASE_REPEAT;

	private static final int DECIMAL_SCALE = 20;

	private record Inputs(int a, int b) {
	}

	private record IoResult(boolean isOk, String err, Object bag) {
	}

	private static BigDecimal binaryPowerBig(int base, int exp) {
		if (base == 1) {
			return BigDecimal.ONE;
		} else if (base == 0) {
			if (exp > 0) {
				return BigDecimal.ZERO;
			} else {
				return null;
			}
		}
		if (exp == 0) {
			// здесь основание заведомо != 0
			return BigDecimal.ONE;
		}

		boolean inverse;
		if (inverse = exp < 0) {
			exp = -exp;
		}

		BigDecimal bdResult = BigDecimal.ONE;
		BigDecimal bdBase = BigDecimal.valueOf(base);
		while (exp != 0) {
			if ((exp & 1) != 0) {
				bdResult = bdResult.multiply(bdBase);
			}
			bdBase = bdBase.multiply(bdBase);
			exp >>= 1;
		}

		return inverse ? BigDecimal.ONE.divide(bdResult, DECIMAL_SCALE, RoundingMode.HALF_EVEN).stripTrailingZeros()
				: bdResult;
	}

	// private static Double binaryPower(long base, long exp) {
	// if (base == 1) {
	// return 1d;
	// } else if (base == 0) {
	// if (exp > 0) {
	// return 0d;
	// } else {
	// return Double.NaN;
	// }
	// }
	// if (exp == 0) {
	// // здесь основание заведомо != 0
	// return 1d;
	// }

	// boolean inverse;
	// if (inverse = exp < 0) {
	// exp = -exp;
	// }

	// long res = 1L;
	// while (exp != 0) {
	// if ((exp & 1) != 0) {
	// res *= base;
	// }
	// base *= base;
	// exp >>= 1;
	// }

	// return inverse ? 1d / res : res;
	// }

	private static IoResult readInputs(String fileName, String pathToFile) {
		Integer a = null, b = null;
		try (FileReader fr = new FileReader(Paths.get(pathToFile, fileName).toFile());
				BufferedReader br = new BufferedReader(fr)) {

			for (String line; (line = br.readLine()) != null && (a == null || b == null);) {
				var parts = line.trim().split("\\s+");
				Integer value;
				if (parts.length == 2 && (value = tryParseInt(parts[1])) != null) {
					// берём только первое вхождение каждого параметра
					if (a == null && parts[0].equalsIgnoreCase(PARAM_A_NAME)) {
						a = value;
					} else if (b == null && parts[0].equalsIgnoreCase(PARAM_B_NAME)) {
						b = value;
					}
				}
			}
		} catch (IOException ex) {
			return new IoResult(false, ex.getMessage(), null);
		}
		if (a == null || b == null) {
			return new IoResult(false, "В файле отсутствуют необходимые данные или данные некорректны.", null);
		}
		return new IoResult(true, null, new Inputs(a, b));
	}

	private static IoResult writeOutputs(String fileName, String pathToFile, BigDecimal value) {
		try (FileWriter fw = new FileWriter(Paths.get(pathToFile, fileName).toFile(), false)) {
			fw.write(value.toString());
		} catch (IOException ex) {
			return new IoResult(false, ex.getMessage(), null);
		}
		return new IoResult(true, null, null);
	}

	// private static IoResult writeOutputs(String fileName, String pathToFile,
	// double value) {
	// try (FileWriter fw = new FileWriter(Paths.get(pathToFile, fileName).toFile(),
	// false)) {
	// int intVal = (int) value;
	// String valueStr = (value == intVal) ? Integer.toString(intVal) :
	// Double.toString(value);
	// fw.write(valueStr);
	// } catch (IOException ex) {
	// return new IoResult(false, ex.getMessage(), null);
	// }
	// return new IoResult(true, null, null);
	// }

	// aux
	private static Integer tryParseInt(String str) {
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	// ui
	private static Integer getUserInputInt(
			Scanner console, String prompt,
			Function<Integer, Boolean> checkIfValid,
			String warnOutOfRange) {

		boolean wrongType = false;
		boolean outOfRange = false;

		while (true) {
			if (wrongType) {
				wrongType = false;
				System.err.println(ERROR_NOT_INT);
			}
			if (outOfRange) {
				outOfRange = false;
				if (warnOutOfRange != null)
					System.err.println(warnOutOfRange);
			}
			System.out.print(prompt);

			var value = tryParseInt(console.nextLine());
			if (value != null) {
				if (checkIfValid == null || checkIfValid.apply(value)) {
					return value;
				}
				outOfRange = true;
			} else {
				wrongType = true;
			}
		}
	}

	private static boolean askYesNo(Scanner console, String prompt) {
		System.out.print(prompt);
		var answer = console.nextLine();
		return answer.isBlank() || answer.toLowerCase().startsWith("y");
	}

	private static void clearConsole() {
		System.out.println("\u001b[1;1H\u001b[2J");
	}

	public static void main(String[] args) {
		Scanner conScan = new Scanner(System.in);

		do {
			clearConsole();
			System.out.printf("\u001b[3m\u001b[97m%s\u001b[0m\n", TITLE);
			System.out.println("\u2550".repeat(TITLE.length()));
			System.out.println("\nВыберите способ ввода исходных значений:");
			int actionId = getUserInputInt(conScan,
					String.format("0 \u2014 ввести вручную; 1 \u2014 прочитеть из файла %s: ", FILE_NAME_INPUT),
					choise -> choise == 0 || choise == 1,
					"Ошибка ввода: Требуется 0 или 1. " + PLEASE_REPEAT);
			System.out.println();

			int a, b;
			if (actionId == 0) {
				a = getUserInputInt(conScan, "Введите целое число \u2014 основание a: ", null, null);
				b = getUserInputInt(conScan, "Введите целое число \u2014 показатель степени b: ", null, null);
			} else {
				IoResult ioResult = readInputs(FILE_NAME_INPUT, PATH_TO_FILES);
				if (!ioResult.isOk) {
					System.err.printf("Ошибка при чтении файла %s:\n\t", FILE_NAME_INPUT);
					if (ioResult.err != null)
						System.err.println(ioResult.err);
					continue;
				}

				var inputs = (Inputs) ioResult.bag;
				a = inputs.a;
				b = inputs.b;
				System.out.printf("Прочитано:\ta = %d\tb = %d\n", a, b);
			}

			var res = binaryPowerBig(a, b);

			// if (Double.isNaN(res))
			if (res == null) {
				System.out.printf("\nРезультат возведения числа %d в степень %d не определён!\n", a, b);
				continue;
			}

			String expStr = Translator.ToSuperscriptedDecimalStr.translate(Integer.toString(b));
			System.out.printf("\nРезультат: %d%s = %s\n\n", a, expStr, res.toString());

			var askUser = String.format("Записать результат в файл %s (Y/n)? ", FILE_NAME_OUTPUT);
			if (askYesNo(conScan, askUser)) {
				IoResult ioResult = writeOutputs(FILE_NAME_OUTPUT, PATH_TO_FILES, res);
				if (ioResult.isOk) {
					System.out.println("Результат успешно записан.");
				} else {
					System.out.println("Не удалось записать в файл.");
					if (ioResult.err != null)
						System.out.println(ioResult.err);
				}
			}
			System.out.println();

		} while (askYesNo(conScan, "Желаете повторить (Y/n)? "));

		conScan.close();
		System.out.println();
	}
}
