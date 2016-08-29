package uuidtest;

public class Result {

	private final int numKeys;
	private final int maxDuplicates;
	private final int minDuplicates;
	private final double mean;
	private final double median;
	private final double sd;

	public Result(int numKeys, int maxDuplicates, int minDuplicates,
			double mean, double median, double sd) {
		super();
		this.numKeys = numKeys;
		this.maxDuplicates = maxDuplicates;
		this.minDuplicates = minDuplicates;
		this.mean = mean;
		this.median = median;
		this.sd = sd;
	}

	public int getNumKeys() {
		return numKeys;
	}

	public int getMaxDuplicates() {
		return maxDuplicates;
	}

	public int getMinDuplicates() {
		return minDuplicates;
	}

	public double getMean() {
		return mean;
	}

	public double getMedian() {
		return median;
	}

	public double getSd() {
		return sd;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + maxDuplicates;
		long temp;
		temp = Double.doubleToLongBits(mean);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(median);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + minDuplicates;
		result = prime * result + numKeys;
		temp = Double.doubleToLongBits(sd);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Result other = (Result) obj;
		if (maxDuplicates != other.maxDuplicates)
			return false;
		if (Double.doubleToLongBits(mean) != Double
				.doubleToLongBits(other.mean))
			return false;
		if (Double.doubleToLongBits(median) != Double
				.doubleToLongBits(other.median))
			return false;
		if (minDuplicates != other.minDuplicates)
			return false;
		if (numKeys != other.numKeys)
			return false;
		if (Double.doubleToLongBits(sd) != Double.doubleToLongBits(other.sd))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Result [numKeys=" + numKeys + ", maxDuplicates="
				+ maxDuplicates + ", minDuplicates=" + minDuplicates
				+ ", mean=" + mean + ", median=" + median + ", sd=" + sd + "]";
	}

}
