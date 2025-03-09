package Summer1;

public class Date {
	private long elapseTime;
		public Date(){
			
			
		}
		public Date(long elapseTime){
			this.elapseTime=elapseTime;
		}
		public long getTime(long elapseTime) {
			return elapseTime;
		}
		
		public void setTime(long elapseTime) {
			this.elapseTime=elapseTime;
		}
		@Override
		public String toString() {
			return "Date [elapseTime=" + elapseTime + "]";
		}
		

}


