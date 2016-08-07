public class Main {

	public static void main(String[] args) {
		Translator worker;
		if (args.length > 0) {
			worker = new Translator(args[0], true);
		} else {
			worker = new Translator(null, false);
		}

		worker.translate();
	}

}
