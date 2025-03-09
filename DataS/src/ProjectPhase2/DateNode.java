package ProjectPhase2;



class DateNode {
	    private String date;
	    private MartyrLinkedList martyrs;
	    private DateNode left;
	    private DateNode right;

	    public DateNode(String date) {
	        this.date = date;
	        this.martyrs = new MartyrLinkedList();
	    }

	    public String getDate() {
	        return date;
	    }

	    public MartyrLinkedList getMartyrs() {
	        return martyrs;
	    }

	    public DateNode getLeft() {
	        return left;
	    }

	    public void setLeft(DateNode left) {
	        this.left = left;
	    }

	    public DateNode getRight() {
	        return right;
	    }

	    public void setRight(DateNode right) {
	        this.right = right;
	    }
	


//	// Add a martyr to this date node
//       private MartyrLinkedList martyrs;  // Linked list to store martyrs who died on this date
	public void addMartyr(MartyrPH2 martyr) {
        martyrs.insertSorted(martyr);
    }




	


	

	
}