package ProjectPhase2;


//Represents the Martyr Date Tree
class MartyrDateTree {
 private DateNode root;
 
 public DateNode getRoot() {
     return root;
 }

 public boolean insertMartyr(String date, MartyrPH2 martyr, StringBuilder path) {
     if (root == null) {
         root = new DateNode(date);
         root.getMartyrs().insertMartyr(martyr);
         return true;
     } else {
         return insertMartyrRecursive(root, date, martyr, path);
     }
 }

 private boolean insertMartyrRecursive(DateNode currentNode, String date, MartyrPH2 martyr, StringBuilder path) {
     if (date.compareTo(currentNode.getDate()) < 0) {
         if (currentNode.getLeft() == null) {
             currentNode.setLeft(new DateNode(date));
             currentNode.getLeft().getMartyrs().insertMartyr(martyr);
             return true;
         } else {
             return insertMartyrRecursive(currentNode.getLeft(), date, martyr, path);
         }
     } else if (date.compareTo(currentNode.getDate()) > 0) {
         if (currentNode.getRight() == null) {
             currentNode.setRight(new DateNode(date));
             currentNode.getRight().getMartyrs().insertMartyr(martyr);
             return true;
         } else {
             return insertMartyrRecursive(currentNode.getRight(), date, martyr, path);
         }
     } else {
         // Date already exists, insert martyr into the existing linked list
         currentNode.getMartyrs().insertMartyr(martyr);
         return true;
     }
 }


//	    public void insertMartyr(String dateOfDeath, MartyrPH2 martyr, StringBuilder path) {
//	        root = insertMartyrRec(root, dateOfDeath, martyr, path);
//	    }
//
//	    private DateNode insertMartyrRec(DateNode node, String dateOfDeath, MartyrPH2 martyr, StringBuilder path) {
//	        if (node == null) {
//	            path.append("Inserted Date '").append(dateOfDeath).append("' with Martyr '").append(martyr.getName()).append("'\n");
//	            return new DateNode(dateOfDeath, martyr);
//	        }
//
//	        int compareResult = dateOfDeath.compareTo(node.getDate());
//	        if (compareResult < 0) {
//	            path.append("Go Left -> ");
//	            node.setLeft(insertMartyrRec(node.getLeft(), dateOfDeath, martyr, path));
//	        } else if (compareResult > 0) {
//	            path.append("Go Right -> ");
//	            node.setRight(insertMartyrRec(node.getRight(), dateOfDeath, martyr, path));
//	        } else {
//	            // Same date, insert martyr in this date's list
//	            node.getMartyrs().insertSorted(martyr, path);
//	        }
//	        return node;
//	    }
	


//	public DateNode insertDate(LocationNode locationNode, String dateOfDeath, MartyrPH2 martyr, StringBuilder path) {
//		   MartyrDateTree dateTree = locationNode.getLocation().getMartyrDateTree();
//	    return insertDateRec(dateTree.getRoot(), dateOfDeath, martyr, path);
//	}
//
//	private DateNode insertDateRec(DateNode node, String dateOfDeath, MartyrPH2 martyr, StringBuilder path) {
//	    if (node == null) {
//	        node = new DateNode(dateOfDeath);
//	        node.addMartyr(martyr);
//	        path.append("Root created with date '").append(dateOfDeath).append("' and added martyr '")
//	            .append(martyr.getName()).append("'\n");
//	        return node;
//	    }
//
//	    int compareResult = dateOfDeath.compareTo(node.getDate());
//	    if (compareResult < 0) {
//	        path.append("Go left from date '").append(node.getDate()).append("'\n");
//	        node.setLeft(insertDateRec(node.getLeft(), dateOfDeath, martyr, path));
//	    } else if (compareResult > 0) {
//	        path.append("Go right from date '").append(node.getDate()).append("'\n");
//	        node.setRight(insertDateRec(node.getRight(), dateOfDeath, martyr, path));
//	    } else {
//	        node.addMartyr(martyr);
//	        path.append("Inserted martyr '").append(martyr.getName()).append("' into existing date '")
//	            .append(dateOfDeath).append("'\n");
//	    }
//	    return node;
//	}
//
//	public boolean insertMartyr(String dateOfDeath, MartyrPH2 martyr, StringBuilder path) {
//	    if (root == null) {
//	        // If there's no root, create a new DateNode as root
//	        root = new DateNode(dateOfDeath);
//	        root.getMartyrs().insertSorted(martyr, path);
//	        path.append("New date created: ").append(dateOfDeath).append(" with martyr ").append(martyr.getName()).append("\n");
//	        return true;
//	    } else {
//	        // Traverse or insert date node accordingly
//	        return insertMartyrRec(root, dateOfDeath, martyr, path);
//	    }
//	}
//
//	private boolean insertMartyrRec(DateNode node, String dateOfDeath, MartyrPH2 martyr, StringBuilder path) {
//	    int comp = dateOfDeath.compareTo(node.getDate());
//	    if (comp == 0) {
//	        // Date node exists, insert martyr here
//	        node.getMartyrs().insertSorted(martyr, path);
//	        path.append("Martyr '").append(martyr.getName()).append("' added on ").append(dateOfDeath).append("\n");
//	        return true;
//	    } else if (comp < 0) {
//	        if (node.getLeft() == null) {
//	            node.setLeft(new DateNode(dateOfDeath));
//	            node.getLeft().getMartyrs().insertSorted(martyr, path);
//	            path.append("New date created: ").append(dateOfDeath).append(" with martyr ").append(martyr.getName()).append("\n");
//	            return true;
//	        } else {
//	            return insertMartyrRec(node.getLeft(), dateOfDeath, martyr, path);
//	        }
//	    } else {
//	        if (node.getRight() == null) {
//	            node.setRight(new DateNode(dateOfDeath));
//	            node.getRight().getMartyrs().insertSorted(martyr, path);
//	            path.append("New date created: ").append(dateOfDeath).append(" with martyr ").append(martyr.getName()).append("\n");
//	            return true;
//	        } else {
//	            return insertMartyrRec(node.getRight(), dateOfDeath, martyr, path);
//	        }
//	    }
//	}







//    // Public method to initiate the insertion process
//    public DateNode insertDate(String dateOfDeath, MartyrPH2 martyr, StringBuilder path) {
//        if (root == null) {
//            root = new DateNode(dateOfDeath); // Create the root if it does not exist
//            root.getMartyrs().insertSorted(martyr, path); // Insert the martyr into the newly created list
//            path.append("Root created with date '").append(dateOfDeath).append("' and added martyr '").append(martyr.getName()).append("'\n");
//            return root;
//        } else {
//            return insertDateRec(root, dateOfDeath, martyr, path);
//        }
//    }

	// Method to insert a martyr at a specific date
//    public boolean insertMartyr(String date, MartyrPH2 martyr, StringBuilder path) {
//        DateNode dateNode = findOrInsertDate(date);
//        if (dateNode != null) {
//            dateNode.addMartyr(martyr);
//            return true;
//        }
//        return false;
//    }

    // Helper method to find or insert a date node
//    private DateNode findOrInsertDate(String date) {
//        return findOrInsertDateRec(root, date);
//    }
//
//    private DateNode findOrInsertDateRec(DateNode node, String date) {
//        if (node == null) {
//            return new DateNode(date);
//        } else if (node.date.equals(date)) {
//            return node;
//        } else if (date.compareTo(node.date) < 0) {
//            node.left = findOrInsertDateRec(node.left, date);
//        } else {
//            node.right = findOrInsertDateRec(node.right, date);
//        }
//        return node;
//    }

   

	


}
