package Dijkstra;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import javafx.animation.PathTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.Stage;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;

public class MapScene extends Scene {

	static int numberOfVertices;
	static int numberOfEdges;

	private ImageView firstSelectedFlag = null;
	private ImageView secondSelectedFlag = null;
	private String firstCapital = null;
	private String secondCapital = null;

	Graph graph;

	File file; // File object for the file to be compressed

	BorderPane bp = new BorderPane(); // Main layout pane for the scene

	double maxWidth = 1150;
	double maxHeight = 770;
	Stage stage; // Stage on which the scene is set
	Scene scene; // Previous scene to return to
	EncancedComboBox sourceBoxCustom = new EncancedComboBox("Enter Source");
	EncancedComboBox desBoxCustom = new EncancedComboBox("Enter Destination");
	EncancedComboBox filterBoxCustom = new EncancedComboBox("Select Filter");

	public MapScene(Stage stage, Scene scene, File file) {
		super(new BorderPane(), 1200, 650);
		this.stage = stage;
		this.scene = scene;

		this.bp = ((BorderPane) this.getRoot());

		this.file = file;

		readFile();
		addFX();

	}

	public void readFile() {
		try (FileInputStream fileInputStream = new FileInputStream(file);
				InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

			String line;
			String[] header = bufferedReader.readLine().split(",");
			numberOfVertices = Integer.parseInt(header[0].trim());
			numberOfEdges = Integer.parseInt(header[1].trim());
			System.out.println("Header Line: [" + header[0] + ", " + header[1] + "]");
			System.out.println("Expected Vertices: " + numberOfVertices + ", Expected Edges: " + numberOfEdges);

			graph = new Graph(numberOfVertices);

			int vertexCount = 0;
			while ((line = bufferedReader.readLine()) != null && line.contains(",")) {
				String[] parts = line.split(",");
				if (parts.length == 3) { // Vertex definition
					try {
						String name = parts[0].trim();
						double latitude = Double.parseDouble(parts[1].trim());
						double longitude = Double.parseDouble(parts[2].trim());

						Capital capital = new Capital(name, longitude, latitude);
						capital.setX((((longitude + 180.0) / 360.0) * maxWidth));
						capital.setY((((90.0 - latitude) / 180.0) * maxHeight));

						Vertix vertix = new Vertix(capital);
						graph.addVertix(vertix);
						vertexCount++;
//						System.out.println("Added Vertex: " + name + " [" + latitude + ", " + longitude + "]");
					} catch (NumberFormatException e) {
						System.err.println("Skipping invalid vertex: " + line);
					}
				} else {
					break; // Stop processing vertices and move to edges
				}
			}

			int edgeCount = 0;
			do {
				if (line == null || !line.contains(","))
					continue;

				String[] parts = line.split(",");
				if (parts.length == 4) { // Edge definition with cost and time
					String sourceName = parts[0].trim();
					String destinationName = parts[1].trim();
					double cost;
					double time;

					try {
						cost = Double.parseDouble(parts[2].trim());
						time = Double.parseDouble(parts[3].trim());
					} catch (NumberFormatException e) {
						System.err.println("Skipping invalid edge due to invalid cost or time: " + line);
						continue;
					}

					// Skip edges with negative weights (cost or time)
					if (cost < 0 || time < 0) {
						System.out.println(
								"Skipping edge with negative weights: " + sourceName + " -> " + destinationName);
						continue;
					}

					// Prevent self-loops
					if (sourceName.equals(destinationName)) {
						System.out.println("Skipping self-loop: " + sourceName + " -> " + destinationName);
						continue;
					}

					Vertix source = graph.getVertix(sourceName);
					Vertix destination = graph.getVertix(destinationName);

					// Skip edge if either source or destination does not exist
					if (source == null || destination == null) {
						System.out.println(
								"Skipping edge with non-existent vertices: " + sourceName + " -> " + destinationName);
						continue;
					}

					Edge edge = new Edge(source, destination, cost, time);
					source.addEdge(edge);
					edgeCount++;
//					System.out.println("Added Edge: " + sourceName + " -> " + destinationName + " | Cost: " + cost
//							+ " | Time: " + time);
				} else {
					System.err.println("Skipping invalid line: " + line);
				}
			} while ((line = bufferedReader.readLine()) != null);

			System.out.println("Vertices Added: " + vertexCount + "/" + numberOfVertices);
			System.out.println("Edges Added: " + edgeCount + "/" + numberOfEdges);

			if (vertexCount != numberOfVertices) {
				System.err
				.println("Vertex count mismatch: Expected " + numberOfVertices + ", but found " + vertexCount);
			}
			if (edgeCount != numberOfEdges) {
				System.err.println("Edge count mismatch: Expected " + numberOfEdges + ", but found " + edgeCount);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private ScrollPane getMap() {
		// Load the map image
		Image mapImage = new Image(getClass().getResource("Images/WorldMap1.jpg").toExternalForm());
		ImageView map = new ImageView(mapImage);

		map.setFitWidth(maxWidth); // Adjust the size as needed
		map.setFitHeight(maxHeight);

		map.getStyleClass().add("map-image");

		Pane pane = new Pane(map);

		// Get all vertices (capitals) from the graph
		Vertix[] nodes = graph.getHashTable().getTable();

		for (int i = 0; i < nodes.length; i++) {
			Vertix vertix = nodes[i];
			if (vertix == null)
				continue; // Skip empty nodes

			String capitalName = vertix.getCapital().getCapitalName();
			ImageView flagImage = getIcon(capitalName);

			// Skip capitals with no valid icon
			if (flagImage == null) {
				continue;
			}

			// Add to combo boxes for source and destination
			sourceBoxCustom.addCapital(capitalName);
			desBoxCustom.addCapital(capitalName);

			flagImage.setLayoutX(vertix.getCapital().getX() - 512 / 2 / 20);
			flagImage.setLayoutY(vertix.getCapital().getY() - 512 / 20 + 10);


			// Add a label for the capital name
			Label capitalLabel = new Label(capitalName);
			capitalLabel.setStyle(
					"-fx-font-size: 14px;-fx-text-fill: black;-fx-background-color: white;-fx-padding: 3;-fx-border-color: black;-fx-border-radius: 5; -fx-background-radius: 5;");
			capitalLabel.setVisible(false); // Initially hidden
			capitalLabel.layoutXProperty().bind(flagImage.layoutXProperty()); // Bind label position dynamically
			capitalLabel.layoutYProperty().bind(flagImage.layoutYProperty().add(25)); // Position below the flag
			// dynamically

			// Show label on hover
			flagImage.setOnMouseEntered(e -> capitalLabel.setVisible(true));
			flagImage.setOnMouseExited(e -> capitalLabel.setVisible(false));

			// Add flag and label to the pane
			pane.getChildren().addAll(flagImage, capitalLabel);
		}

		// Make the map scrollable
		ScrollPane scrollPane = new ScrollPane(pane);
		scrollPane.setPannable(true); // Allow panning
		scrollPane.setPrefViewportWidth(maxWidth - 15);
		scrollPane.setPrefViewportHeight(maxHeight - 9);

		return scrollPane;
	}

	private void addFX() {
		VBox tableBox = new VBox(10, getMap());
		tableBox.setAlignment(Pos.CENTER);
		bp.setPadding(new Insets(15));

		BorderPane.setMargin(tableBox, new Insets(0, 0, 0, 20));
		bp.setLeft(tableBox);

		Label sourceLabel = new Label("Source: ");
		sourceLabel.setStyle("-fx-text-fill: #141E46; " + "-fx-background-color: white; " + "-fx-padding: 3; "
				+ "-fx-border-color: #41B06E; " + "-fx-border-radius: 5; " + "-fx-background-radius: 5; "
				+ "-fx-font-size: 16px;");
		sourceLabel.setPrefWidth(80);

		Label targetLabel = new Label("Target: ");
		targetLabel.setStyle("-fx-text-fill: #141E46; " + "-fx-background-color: white; " + "-fx-padding: 3; "
				+ "-fx-border-color: #41B06E; " + "-fx-border-radius: 5; " + "-fx-background-radius: 5; "
				+ "-fx-font-size: 16px;");
		targetLabel.setPrefWidth(80);

		Label filterLabel = new Label("Filter: ");
		filterLabel.setStyle("-fx-text-fill: #141E46; " + "-fx-background-color: white; " + "-fx-padding: 3; "
				+ "-fx-border-color: #41B06E; " + "-fx-border-radius: 5; " + "-fx-background-radius: 5; "
				+ "-fx-font-size: 16px;");

		filterBoxCustom.addCapital("Cost");
		filterBoxCustom.addCapital("Time");
		filterBoxCustom.addCapital("Distance");

		// Adjust ComboBox sizes
		sourceBoxCustom.getComboBox().setPrefWidth(150); // Compact size
		desBoxCustom.getComboBox().setPrefWidth(150);
		filterBoxCustom.getComboBox().setPrefWidth(150);

		Button goButton = new Button("GO");
		goButton.setPrefWidth(250); // Consistent width for button

		Label pathLabel = new Label("Path: ");
		pathLabel.setStyle("-fx-text-fill: #141E46; " + "-fx-background-color: white; " + "-fx-padding: 3; "
				+ "-fx-border-color: #41B06E; " + "-fx-border-radius: 5; " + "-fx-background-radius: 5; "
				+ "-fx-font-size: 16px;");

		// Adjust TextArea size
		TextArea pathArea = new TextArea();
		pathArea.setMinWidth(280);
		pathArea.setMaxWidth(280);
		pathArea.setPrefHeight(250);
		pathArea.setEditable(false); // Make the TextField non-editable

		// Change disField to a small TextArea
		Label disLabel = new Label("Summary: ");
		disLabel.setStyle("-fx-text-fill: #141E46; " + "-fx-background-color: white; " + "-fx-padding: 3; "
				+ "-fx-border-color: #41B06E; " + "-fx-border-radius: 5; " + "-fx-background-radius: 5; "
				+ "-fx-font-size: 16px;");

		// Small TextArea for displaying summary
		TextArea disArea = new TextArea();
		disArea.setPrefWidth(250);
		disArea.setPrefHeight(100);
		disArea.setEditable(false); // Make the TextArea non-editable

		// Reset Button
		Button resetButton = new Button("Reset");
		resetButton.setOnAction(e -> {
		    sourceBoxCustom.getComboBox().getSelectionModel().clearSelection();
		    desBoxCustom.getComboBox().getSelectionModel().clearSelection();
		    filterBoxCustom.getComboBox().getSelectionModel().clearSelection();

		   
		    pathArea.clear();
		    disArea.clear();

		    // Clear drawn paths, arrows, and plane images from the map
		    Pane mapPane = (Pane) ((ScrollPane) tableBox.getChildren().get(0)).getContent();
		    mapPane.getChildren().removeIf(node -> (node instanceof Line || node instanceof Polygon || 
		        (node instanceof ImageView && ((ImageView) node).getImage().getUrl().contains("plane.png"))));

		    System.out.println("Path and details reset successfully!");
		});

		resetButton.setPrefWidth(250); // Consistent width for button

		GridPane gp = new GridPane();

		gp.add(new HBox(10, sourceLabel, sourceBoxCustom.getComboBox()), 0, 0);
		gp.add(new HBox(10, targetLabel, desBoxCustom.getComboBox()), 0, 1);
		gp.add(new HBox(10, filterLabel, filterBoxCustom.getComboBox()), 0, 2);
		gp.add(goButton, 0, 3);
		GridPane.setMargin(goButton, new Insets(0, 0, 25, 0));
		gp.add(pathLabel, 0, 4);
		gp.add(pathArea, 0, 5);
		gp.add(disLabel, 0, 6);
		gp.add(disArea, 0, 7);
		gp.add(resetButton, 0, 8);

		gp.setVgap(15);
		gp.setAlignment(Pos.CENTER);

		bp.setRight(gp);

		goButton.setOnAction(e -> {
			pathArea.clear();
			disArea.clear();

			String source = sourceBoxCustom.getComboBox().getSelectionModel().getSelectedItem();
			String destination = desBoxCustom.getComboBox().getSelectionModel().getSelectedItem();
			String filter = filterBoxCustom.getComboBox().getSelectionModel().getSelectedItem();

			if (source == null || destination == null) {
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.setTitle("Warning");
				alert.setHeaderText("Source or Destination Not Selected");
				alert.setContentText("Please select both source and destination before proceeding.");
				alert.showAndWait();
				return;
			}

			if (filter == null || (!filter.equalsIgnoreCase("distance") && !filter.equalsIgnoreCase("cost")
					&& !filter.equalsIgnoreCase("time"))) {
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.setTitle("Warning");
				alert.setHeaderText("Filter Not Selected");
				alert.setContentText("Please select a valid filter (Distance, Cost, or Time) before proceeding.");
				alert.showAndWait();
				return;
			}

			if (source.equals(destination)) {
				pathArea.setText("Source and destination are the same.");
				disArea.setText("Total Travel Time: 0.00 Minutes\nEstimated Cost: 0.00 $\nTotal Distance: 0.00 KM");
				return;
			}

			Pane mapPane = (Pane) ((ScrollPane) tableBox.getChildren().get(0)).getContent();
			clearMapElements(mapPane);

			LinkedList resultPath = graph.getOptimalPath(source, destination, filter.toLowerCase());

			if (resultPath != null) {
			    StringBuilder pathDetails = new StringBuilder();
			    double totalDistance = 0, totalCost = 0, totalTime = 0;

			    LinkedListNode currentNode = resultPath.getFirstNode();

			    while (currentNode != null) {
			        Edge edge = currentNode.getEdge();
			        totalDistance += edge.getDistance();
			        totalCost += edge.getCost();
			        totalTime += edge.getTime();

			        pathDetails.append("From ").append(edge.getSource().getCapital().getCapitalName())
			                   .append(" to ").append(edge.getDestination().getCapital().getCapitalName());

			        switch (filter.toLowerCase()) {
			            case "cost" -> pathDetails.append(" | Cost: ").append(String.format("%.2f $", edge.getCost()));
			            case "time" -> pathDetails.append(" | Time: ").append(String.format("%.2f Minutes", edge.getTime()));
			            case "distance" -> pathDetails.append(" | Distance: ").append(String.format("%.2f KM", edge.getDistance()));
			        }
			        pathDetails.append("\n");

			        currentNode = currentNode.getNext();
			    }

			    pathArea.setText(pathDetails.toString());
			    disArea.setText(String.format(
			            "Total Travel Time: %.2f Minutes\nEstimated Cost: %.2f $\nTotal Distance: %.2f KM",
			            totalTime, totalCost, totalDistance));

			    drawPath(mapPane, resultPath);
			} else {
			    pathArea.setText("No valid path found.");
			    disArea.setText("Total Travel Time: N/A\nEstimated Cost: N/A\nTotal Distance: N/A");
			}

		});

	}
	



	private void drawPath(Pane mapPane, LinkedList path) {
	    mapPane.getChildren().removeIf(node -> node instanceof Line || node instanceof Polygon
	            || (node instanceof ImageView && ((ImageView) node).getImage().getUrl().contains("plane.png")));

	    Path combinedPath = new Path();  // Path to combine all segments for plane animation.
	    LinkedListNode currentNode = path.getFirstNode();  // Start from the first node of the path.

	    double dashLength = 14;  // Length of each dash segment in pixels.
	    double dashGap = 12;     // Gap between dashes in pixels.

	    // Iterate over all edges in the path.
	    while (currentNode != null) {
	        Edge edge = currentNode.getEdge();  // Get the edge for the current path segment.
	        Capital sourceCapital = edge.getSource().getCapital();  // Source capital for this edge.
	        Capital destinationCapital = edge.getDestination().getCapital();  // Destination capital for this edge.

	        // Move to the starting point of the first segment.
	        if (currentNode == path.getFirstNode()) {
	            combinedPath.getElements().add(new MoveTo(sourceCapital.getX(), sourceCapital.getY()));
	        }
	        // Add a line to the destination point for the path animation.
	        combinedPath.getElements().add(new LineTo(destinationCapital.getX(), destinationCapital.getY()));

	        // Calculate the total length of the current segment.
	        double length = Math.sqrt(Math.pow(destinationCapital.getX() - sourceCapital.getX(), 2)
	                + Math.pow(destinationCapital.getY() - sourceCapital.getY(), 2));

	        // Calculate unit vectors to determine the direction of the path.
	        double unitX = (destinationCapital.getX() - sourceCapital.getX()) / length;
	        double unitY = (destinationCapital.getY() - sourceCapital.getY()) / length;

	        // Start drawing from the source point.
	        double currentX = sourceCapital.getX();
	        double currentY = sourceCapital.getY();

	        // Draw dashed lines along the path.
	        while (length >= dashLength) {
	            double endX = currentX + unitX * dashLength;  // Calculate the endpoint of the dash.
	            double endY = currentY + unitY * dashLength;

	            // Draw a red dashed line segment.
	            Line line = new Line(currentX, currentY, endX, endY);
	            line.setStroke(Color.RED); 
	            line.setStrokeWidth(3);    
	            line.setStrokeLineCap(StrokeLineCap.ROUND);  // Round the ends of the line for aesthetics.
	            mapPane.getChildren().add(line);  // Add the dashed line to the map.

	            // Add an arrowhead at the end of the dash to indicate direction.
	            double arrowLength = 12;  // Length of the arrow sides.
	            double angle = Math.atan2(endY - currentY, endX - currentX);  // Calculate the direction angle.

	            // Calculate the points for the arrowhead (a triangle).
	            double x1 = endX - arrowLength * Math.cos(angle - Math.PI / 6);  // Left point of the arrowhead.
	            double y1 = endY - arrowLength * Math.sin(angle - Math.PI / 6);
	            double x2 = endX - arrowLength * Math.cos(angle + Math.PI / 6);  // Right point of the arrowhead.
	            double y2 = endY - arrowLength * Math.sin(angle + Math.PI / 6);

	            Polygon arrowHead = new Polygon();
	            arrowHead.getPoints().addAll(endX, endY, x1, y1, x2, y2);  // Define the points for the triangle.
	            arrowHead.setFill(Color.RED);  // Set arrowhead color to red.
	            mapPane.getChildren().add(arrowHead);  // Add the arrowhead to the map.

	            // Move the current point forward by the dash length + gap.
	            currentX = endX + unitX * dashGap;
	            currentY = endY + unitY * dashGap;
	            length -= (dashLength + dashGap);  
	        }

	        currentNode = currentNode.getNext();  // Move to the next edge in the path.
	    }

//	    // Add the plane image and animate it along the path.
//	    Image planeImage = new Image(getClass().getResource("Images/plane.png").toExternalForm());
//	    ImageView planeImageView = new ImageView(planeImage);
//	    planeImageView.setFitWidth(30);  // Set plane image width.
//	    planeImageView.setFitHeight(30);  // Set plane image height.
//
//	    PathTransition pathTransition = new PathTransition();  // Animation for the plane along the path.
//	    pathTransition.setDuration(Duration.seconds(10));  // Set animation duration (10 seconds).
//	    pathTransition.setPath(combinedPath);  
//	    pathTransition.setNode(planeImageView);  
//	    pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);  // Keep plane oriented along the path.
//	    pathTransition.setCycleCount(PathTransition.INDEFINITE);  // Make the plane animation loop indefinitely.
//	    pathTransition.setAutoReverse(false);  
//	    pathTransition.play();  
//	    mapPane.getChildren().add(planeImageView);  // Add the plane to the map.
	}



	private ImageView getIcon(String image) {
		Glow glow = new Glow();
		glow.setLevel(0.5);

		ColorAdjust colorAdjust = new ColorAdjust();
		colorAdjust.setBrightness(0.2);
		colorAdjust.setContrast(0.0);
		colorAdjust.setSaturation(-0.1);
		colorAdjust.setHue(0.166);

		glow.setInput(colorAdjust);
		
		String imagePath = "Images/" + image.toLowerCase() + ".png";

		Image flagImage = null;

		try {
			flagImage = new Image(getClass().getResource(imagePath).toExternalForm());
		} catch (Exception e) {
			System.err.println("Image not found for: " + imagePath + ". Using default unknown image.");
			flagImage = new Image(getClass().getResource("Images/unknown.png").toExternalForm());
		}

		ImageView flagImageView = new ImageView(flagImage);

		final boolean[] isGreen = { false };

		flagImageView.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
			if (!isGreen[0])
				flagImageView.setEffect(glow);
		});
		flagImageView.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
			if (!isGreen[0])
				flagImageView.setEffect(null);
		});

		flagImageView.setFitWidth(flagImage.getWidth() / 25);
		flagImageView.setFitHeight(flagImage.getHeight() / 25);

		ColorAdjust greenAdjust = new ColorAdjust();
		greenAdjust.setHue(0.5); // Adjust the hue to make the image green

		flagImageView.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
			if (isGreen[0]) {
				flagImageView.setEffect(null); // Set back to normal

				// Remove from source or destination if already set
				if (sourceBoxCustom.getComboBox().getSelectionModel().getSelectedItem() != null
						&& sourceBoxCustom.getComboBox().getSelectionModel().getSelectedItem().equals(image)) {
					sourceBoxCustom.getComboBox().getSelectionModel().clearSelection();
					sourceBoxCustom.getComboBox().setPromptText("Enter Source");
					firstSelectedFlag = null;
					firstCapital = null;
				} else if (desBoxCustom.getComboBox().getSelectionModel().getSelectedItem() != null
						&& desBoxCustom.getComboBox().getSelectionModel().getSelectedItem().equals(image)) {
					desBoxCustom.getComboBox().getSelectionModel().clearSelection();
					desBoxCustom.getComboBox().setPromptText("Enter Destination");
					secondSelectedFlag = null;
					secondCapital = null;
				}

			} else {
				if (firstSelectedFlag == null) {
					flagImageView.setEffect(greenAdjust); // Set to green
					firstSelectedFlag = flagImageView;
					firstCapital = image;
					sourceBoxCustom.getComboBox().setValue(image);
				} else if (secondSelectedFlag == null) {
					flagImageView.setEffect(greenAdjust); // Set to green
					secondSelectedFlag = flagImageView;
					secondCapital = image;
					desBoxCustom.getComboBox().setValue(image);
				} else {
					// Replace the first selected flag
					firstSelectedFlag.setEffect(null); // Set back to normal
					sourceBoxCustom.getComboBox().getSelectionModel().clearSelection();
					sourceBoxCustom.getComboBox().setPromptText("Enter Source");

					firstSelectedFlag = secondSelectedFlag;
					firstCapital = secondCapital;
					sourceBoxCustom.getComboBox().setValue(firstCapital);

					flagImageView.setEffect(greenAdjust); // Set new one to green
					secondSelectedFlag = flagImageView;
					secondCapital = image;
					desBoxCustom.getComboBox().setValue(image);
				}
			}
			isGreen[0] = !isGreen[0]; // Toggle the state
		});

		return flagImageView;
	}

	private void clearMapElements(Pane mapPane) {
		mapPane.getChildren().removeIf(node -> (node instanceof Line || node instanceof Path)
				&& !(node instanceof ImageView && ((ImageView) node).getImage().getUrl().contains("locations")));

		mapPane.getChildren().removeIf(
				node -> node instanceof ImageView && ((ImageView) node).getImage().getUrl().contains("plane"));
	}

}