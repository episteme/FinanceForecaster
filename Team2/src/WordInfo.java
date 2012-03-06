public class WordInfo {
	Double rel;
	Double sent;
	
	public WordInfo(Double r, Double s) {
		this.rel = r;
		this.sent = s;
	}

	public Double getRel() {
		return rel;
	}

	public void setRel(Double rel) {
		this.rel = rel;
	}

	public Double getSent() {
		return sent;
	}

	public void setSent(Double sent) {
		this.sent = sent;
	}
}