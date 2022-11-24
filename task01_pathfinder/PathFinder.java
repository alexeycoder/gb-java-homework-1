package task01_pathfinder;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;

// На вход
// некоторому исполнителю
// подаётся четыре числа (a, b, c, d).

// У исполнителя есть две команды
// - команда 1 (к1): увеличить в с раз, а умножается на c
// - команда 2 (к2): увеличить на d , к a прибавляется d
// написать программу, которая выдаёт общее количество
// возможных преобразований a в b
// набор команд, позволяющий число a превратить в число b или сообщить, что это
// невозможно

// Пример 1: а = 1, b = 7, c = 2, d = 1
// ответ: к1, к1, к1, к1, к1, к1 или к1, к2, к1, к1, к1 или к1, к1, к2, к1.
// Пример 2: а = 11, b = 7, c = 2, d = 1
// ответ: нет решения.
// *Подумать над тем, как сделать минимальное количество команд

public class PathFinder {
	private int cFactor;
	private int dAddition;
	private int initialValue;
	private int finalValue;

	// operations
	private final List<UnaryOperator<Integer>> operations;
	private final String[] opRepresentations;

	// solutions storage
	private final ArrayList<String> minLevelSolutions;
	private long solutionsCount;
	private int minLevel;
	private int maxLevel;

	// getters
	public ArrayList<String> getOptimumSolutions() {
		return minLevelSolutions;
	}

	public long getSolutionsCount() {
		return solutionsCount;
	}

	public int getMinOperationsCount() {
		return solutionsCount > 0 ? minLevel : 0;
	}

	public int getMaxOperationsCount() {
		return solutionsCount > 0 ? maxLevel : 0;
	}

	// aux fields
	private Instant startTime;
	private Instant finishTime;
	private static final long PROGRESS_STEP = 100_000_000L;
	private long progressCheckpoint = PROGRESS_STEP;

	// ctor
	public PathFinder(int initial, int result, int factor, int addition) {
		initialValue = initial;
		finalValue = result;
		cFactor = factor;
		dAddition = addition;
		operations = Arrays.asList(arg -> arg * cFactor, arg -> arg + dAddition);
		opRepresentations = new String[] { String.format(" \u00d7%d", cFactor), String.format(" +%d", dAddition) };
		minLevelSolutions = new ArrayList<String>();
	}

	private void reset() {
		solutionsCount = 0;
		minLevel = Integer.MAX_VALUE;
		maxLevel = Integer.MIN_VALUE;
		minLevelSolutions.clear();
	}

	public void solve() {
		reset();
		startTime = Instant.now();

		for (int i = 0; i < operations.size(); ++i) {
			next(0, initialValue, "", i);
		}

		finishTime = Instant.now();
	}

	private void next(int level, int value, String sequence, int operationIndex) {
		++level;
		sequence = level <= minLevel ? sequence + opRepresentations[operationIndex] : null;
		value = operations.get(operationIndex).apply(value);
		if (value == finalValue) {
			registerSolution(level, sequence);
			return;
		} else if (value > finalValue) {
			return;
		}

		for (int i = 0; i < operations.size(); ++i) {
			next(level, value, sequence, i);
		}
	}

	private void registerSolution(int level, String sequence) {
		++solutionsCount;

		if (level > maxLevel) {
			maxLevel = level;
		}

		if (level < minLevel) {
			minLevel = level;
			minLevelSolutions.clear();
			minLevelSolutions.add(sequence);
		} else if (level == minLevel) {
			minLevelSolutions.add(sequence);
		}

		if (solutionsCount == progressCheckpoint) {
			progressCheckpoint += PROGRESS_STEP;
			printCheckpoint();
		}
	}

	// info
	public void printParameters() {
		System.out.println(
				String.format("\u001b[1m\u001b[3mДано:\u001b[0m\n\ta = %d; b = %d; c = %d; d = %d", initialValue,
						finalValue, cFactor, dAddition));
	}

	private void printCheckpoint() {
		var time = Instant.now();
		var elapsed = Duration.between(startTime, time);
		System.out.printf("\t\t...найдено вариантов: %d  шагов минимум: %d  максимум: %d  затрачено %d сек...\n",
				solutionsCount, minLevel, maxLevel, elapsed.toSeconds());
	}

	public void printMinSequence() {
		int minSolutionsCount = minLevelSolutions.size();
		if (minSolutionsCount > 0) {
			System.out.printf("Всего решений с минимальным числом шагов: %d\n", minSolutionsCount);
			for (String sequenceStr : minLevelSolutions) {
				System.out.printf("\tПоследовательность операций: %s\n", sequenceStr);
			}
		}
	}

	public void printSummary() {
		var elapsed = Duration.between(startTime, finishTime);
		if (solutionsCount == 0) {
			System.out.printf(
					"\u001b[1m\u001b[3mИтого:\u001b[0m\n\tНет решения! затрачено %d сек\n",
					elapsed.toSeconds());
			return;
		}

		System.out.printf(
				"\u001b[1m\u001b[3mИтого найдено:\u001b[0m\n\tКомбинаций: \u001b[93m%-10d\u001b[0m шагов минимум: \u001b[93m%-8d\u001b[0m максимум: %-8d затрачено %d сек\n",
				getSolutionsCount(),
				getMinOperationsCount(),
				getMaxOperationsCount(),
				elapsed.toSeconds());
	}
}
