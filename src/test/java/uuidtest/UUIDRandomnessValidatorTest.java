package uuidtest;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class UUIDRandomnessValidatorTest {

	UUIDRandomnessValidator underTest = new UUIDRandomnessValidator();
	private static final double DELTA = 1e-4;

	@Test
	public void testCalculateResultsSummary() {

		Map<String, Integer> testMap = new HashMap<String, Integer>();
		testMap.put("key1", 10);
		testMap.put("key2", 1);
		testMap.put("key3", 15);
		testMap.put("key4", 24);
		double expectedMean = 12.5;
		double expectedMedian = 12.5;
		double expectedSD = 8.3217;
		Result result = underTest.calculateResultsSummary(testMap);
		Assert.assertEquals(4, result.getNumKeys());
		Assert.assertEquals(24, result.getMaxDuplicates());
		Assert.assertEquals(1, result.getMinDuplicates());
		Assert.assertEquals(expectedMean, result.getMean(), DELTA);
		Assert.assertEquals(expectedMedian, result.getMedian(), DELTA);
		Assert.assertEquals(expectedSD, result.getSd(), DELTA);
	}
}
