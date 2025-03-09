package CursorLab;

public class CursorDriver {

  public static void main(String[] args) {
    Cursor cursorManager = new Cursor();
    cursorManager.initialization();

    System.out.println("Creating List 1");
    int list1 = cursorManager.createList();

    cursorManager.insertAtHead('A', list1);
    cursorManager.insertAtHead('B', list1);
    cursorManager.traversList(list1);

    System.out.println("\nCreating List 2");
    int list2 = cursorManager.createList();
    cursorManager.insertAtHead('X', list2);
    cursorManager.traversList(list2);

    int foundPos = cursorManager.find('B', list1);
    if (foundPos != -1) {
      System.out.println("Element 'B' found at position: " + foundPos);
    } else {
      System.out.println("Element 'B' not found in List 1");
    }

    System.out.println("\nInserting element 'C' after element 'B' in List 1");
    cursorManager.insert('C', list1, foundPos + 1);

    System.out.println("\nTraversing List 1 again");
    cursorManager.traversList(list1);
  }
}