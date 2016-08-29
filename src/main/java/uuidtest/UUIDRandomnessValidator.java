package uuidtest;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jfree.ui.RefineryUtilities;

public class UUIDRandomnessValidator {

	public static void main(String[] args) throws Exception {

		/**
		 * This test is to validate a design for creating a directory structure
		 * using the first 4 characters of a UUID. The first 2 characters (8
		 * bits) will represent the level 1 directory and the next 2 characters
		 * will represent level 2. If the characters are evenly spread, then
		 * there should be 2^8 = 256 directories at level 1 and 256 at level 2.
		 * If you consider storing a million files, there will be
		 * (1,000,000/256)/256 = 15.2 files in each directory
		 * 
		 */
		long startTime = System.currentTimeMillis();
		UUIDRandomnessValidator uuidObj = new UUIDRandomnessValidator();

		/**
		 * This hashmap stores the first 4 characters (16 bits) of the UUID. If
		 * evenly spread, it should have 2^16= 65536 keys
		 */
		Map<String, Integer> level0 = new HashMap<String, Integer>();

		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("results/result.tsv"), "utf-8"));
		BufferedWriter writer2 = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("results/resultsummary.tsv"), "utf-8"));
		writer2.write("NumIterations\tNumber of Unique Keys\tMax Files in each Directory\tMin Files in Each Directory\tMean\tMedian\tStandard Deviation\n");
		/**
		 * The below code generates 1 million uuid's and records the keys
		 * generated & how many times each key is generated
		 */
		try {
			int countTo10000 = 0;
			for (int i = 0; i < 1000000; i++) {
				countTo10000++;
				UUID uuid = UUID.randomUUID();
				String level0Str = uuid.toString().substring(0, 4);
				if (level0.containsKey(level0Str)) {
					int count = level0.get(level0Str);
					level0.put(level0Str, count + 1);
				} else {
					level0.put(level0Str, 1);
				}
				if (countTo10000 == 10000) {
					countTo10000 = 0;
					Result result = uuidObj.calculateResultsSummary(level0);
					writer2.write(uuidObj.writeResultAsTSV(result, i + 1));
				}

			}
		} finally {
			uuidObj.writeResultToFile(writer, level0);
			Result result = uuidObj.calculateResultsSummary(level0);
			uuidObj.plotGraph(result, level0);
			writer.close();
			writer2.close();
			long endTime = System.currentTimeMillis();
			System.out.println(String.format(
					"Program completed successfully in %d milliseconds",
					endTime - startTime));
		}
	}

	public Result calculateResultsSummary(Map<String, Integer> map) {

		int maxOccurences = 0;
		int minOccurences = Integer.MAX_VALUE;
		int sumOccurences = 0;
		int[] occurenceArray = new int[map.size()];
		int i = 0;
		for (String s : map.keySet()) {
			int occurences = map.get(s);
			if (occurences > maxOccurences) {
				maxOccurences = occurences;
			}
			if (occurences < minOccurences) {
				minOccurences = occurences;
			}
			sumOccurences = sumOccurences + occurences;
			occurenceArray[i++] = occurences;
		}

		double meanOccurences = (double) sumOccurences / map.size();
		double median = calculateMedian(occurenceArray);
		double sd = calculateStandardDeviation(occurenceArray);
		return new Result(map.size(), maxOccurences, minOccurences,
				meanOccurences, median, sd);
	}

	private double calculateMedian(int[] array) {
		Arrays.sort(array);
		double median = 0;
		if (array.length % 2 == 0) {
			median = (double) (array[array.length / 2 - 1] + array[array.length / 2]) / 2;
		} else {
			median = (double) array[(array.length + 1) / 2 - 1];
		}
		return median;
	}

	private double calculateStandardDeviation(int[] array) {
		int sum = 0;
		for (int val : array) {
			sum = sum + val;
		}
		double mean = (double) sum / array.length;
		double varianceSumSquared = 0;
		for (int val : array) {
			varianceSumSquared = varianceSumSquared + Math.pow((val - mean), 2);
		}
		double sd = Math.sqrt(varianceSumSquared / array.length);
		return sd;
	}

	private String writeResultAsTSV(Result result, int numIterations) {
		String tab = "\t";
		StringBuilder sb = new StringBuilder();
		return sb.append(String.valueOf(numIterations)).append(tab)
				.append(result.getNumKeys()).append(tab)
				.append(result.getMaxDuplicates()).append(tab)
				.append(result.getMinDuplicates()).append(tab)
				.append(result.getMean()).append(tab)
				.append(result.getMedian()).append(tab).append(result.getSd())
				.append("\n").toString();
	}

	private void writeResultToFile(BufferedWriter writer,
			Map<String, Integer> level0) throws IOException {
		writer.write("Key\tValue");
		writer.newLine();
		for (String s : level0.keySet()) {
			writer.write(s + "\t" + level0.get(s));
			writer.newLine();
		}
		writer.write("\n---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
	}

	private void plotGraph(Result result, Map<String, Integer> map)
			throws IOException {
		int[] values = map.values().stream().mapToInt(i -> i).toArray();
		GraphPlotter plotter = new GraphPlotter("File Distribution", result,
				values);
		plotter.pack();
		RefineryUtilities.centerFrameOnScreen(plotter);
		plotter.setVisible(true);

	}
}
