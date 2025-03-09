package ProjectPhase2;

public class MartyrNode {
    MartyrPH2 martyr;
    MartyrNode next;

    public MartyrNode(MartyrPH2 martyr) {
        this.martyr = martyr;
    }

	public MartyrPH2 getMartyr() {
		return martyr;
	}

	public void setMartyr(MartyrPH2 martyr) {
		this.martyr = martyr;
	}

	public MartyrNode getNext() {
		return next;
	}

	public void setNext(MartyrNode next) {
		this.next = next;
	}

	
}
