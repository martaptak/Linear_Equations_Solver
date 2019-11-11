package solver;

class Swap { // slawek: Co ta klasa reprezentuje? Dwa indeksy elementów do zamiany? Czy na pewno jest konieczna?
	// Jeżeli tak, to może umieścić ją gdzieś wewnątrz innej (w jakimś kontekście)?
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
