package task01_pathfinder;

// Тестовые данные

// a: 2 b: 7 c: 2 d: 1 -> 3
// a: 3 b: 27 c: 3 d: 2 -> 6
// a: 30 b: 345 c: 5 d: 6 -> 0
// a: 30 b: 345 c: 2 d: 1 -> 7047
// a: 22 b: 333 c: 3 d: 1 -> 467
// a: 55 b: 555 c: 5 d: 2 -> 30
// a: 22 b: 2022 c: 11 d: 56 -> 0
// a: 22 b: 2022 c: 11 d: 10 -> 18
// a: 22 b: 2022 c: 3 d: 1 -> 763827
// a: 22 b: 20220 c: 3 d: 1 -> 535173226980
// a: 1 b: 1111 c: 2 d: 1 -> 3990330794
// a: 1 b: 11111 c: 2 d: 1 -> 606408167570737286

public class Main {
	private static void printSeparator() {
		System.out.println("\u2508".repeat(120));
	}

	public static void main(String[] args) {
		record Params(int a, int b, int c, int d) {
		}

		Params[] parameters = {
				new Params(2, 7, 2, 1),
				new Params(3, 27, 3, 2),
				new Params(30, 345, 5, 6),
				new Params(30, 345, 2, 1),
				new Params(22, 333, 3, 1),
				new Params(55, 555, 5, 2),
				new Params(22, 2022, 11, 56),
				new Params(22, 2022, 11, 10),
				new Params(22, 2022, 3, 1),
				new Params(1, 1111, 2, 1),
				new Params(22, 20220, 3, 1),
				new Params(1, 11111, 2, 1),
		};

		for (var param : parameters) {
			printSeparator();

			var pathFinder = new PathFinder(param.a, param.b, param.c, param.d);
			pathFinder.printParameters();
			pathFinder.solve();

			pathFinder.printSummary();
			pathFinder.printMinSequence();
		}
		printSeparator();
	}
}
