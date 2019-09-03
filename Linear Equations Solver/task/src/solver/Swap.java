package solver;

class Swap {
	private int prevIndex;
	private int nextIndex;

	Swap(int prevIndex, int nextIndex) {
		this.prevIndex = prevIndex;
		this.nextIndex = nextIndex;
	}

	int[] getReversedSwap() {
		return new int[]{nextIndex, prevIndex};
	}
}
