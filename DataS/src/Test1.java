
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Test1 extends Application {
	/* Panes */
	private BorderPane base;
	private HBox topPane, bottomPane;
	private VBox leftPane, rightPane;
	private Pane center;

	/* Top Pane */
	private Label lblName;
	private TextField tfName;
	private Button btGetName;

	/* Bottom Pane */
	private Button btLeft, btRight;

	/* Left Pane */
	private RadioButton rbRd, rbGreen, rbPink;
	private ToggleGroup group;

	/* Right Pane */
	private CheckBox chBold, chItalic;

	/* Center Pane */
	private Text txName;

	@Override
	public void start(Stage primaryStage) {

		base = new BorderPane();
		topPane = new HBox();
		topPane.setSpacing(10);
		topPane.setAlignment(Pos.CENTER);

		lblName = new Label("Enter your name");
		tfName = new TextField();
		btGetName = new Button("Get Name !");

		topPane.getChildren().addAll(lblName, tfName, btGetName);
		base.setTop(topPane);

		leftPane = new VBox();
		leftPane.setSpacing(10);

		rbRd = new RadioButton("Red");
		rbGreen = new RadioButton("Green");
		rbPink = new RadioButton("Pink");

		group = new ToggleGroup();
		rbRd.setToggleGroup(group);
		rbGreen.setToggleGroup(group);
		rbPink.setToggleGroup(group);

		leftPane.getChildren().addAll(rbRd, rbGreen, rbPink);
		base.setLeft(leftPane);

		rightPane = new VBox(10);

		chBold = new CheckBox("Bold");
		chItalic = new CheckBox("Italic");
		// rightPane.getChildren().addAll(chBold,chItalic);
		rightPane.getChildren().add(chBold);
		rightPane.getChildren().add(chItalic);
		base.setRight(rightPane);

		/* bottom Pane */
		bottomPane = new HBox(10);
		bottomPane.setAlignment(Pos.CENTER);
		btLeft = new Button("Left");
		btRight = new Button("Right");
		bottomPane.getChildren().addAll(btLeft, btRight);
		base.setBottom(bottomPane);

		/* Center Pane */
		center = new Pane();
		txName = new Text(50, 50, "Comp 231");
		txName.setFont(Font.font("Times New Roman", FontWeight.NORMAL, FontPosture.REGULAR, 20));
		
		center.getChildren().add(txName);
		base.setCenter(center);
		btGetName.setOnAction(e -> {
			txName.setText(tfName.getText());
		});
		rbRd.setOnAction(e -> {
			txName.setFill(Color.RED);
		});
		rbGreen.setOnAction(e -> {
			txName.setFill(Color.GREEN);
		});
		rbPink.setOnAction(e -> {
			txName.setFill(Color.PINK);
		});

		EventHandler<ActionEvent> handler = e -> {
			if (chBold.isSelected() && chItalic.isSelected())
				txName.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 20));
			else if (chBold.isSelected())
				txName.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 20));
			else if (chItalic.isSelected())
				txName.setFont(Font.font("Times New Roman", FontWeight.NORMAL, FontPosture.ITALIC, 20));
			else
				txName.setFont(Font.font("Times New Roman", FontWeight.NORMAL, FontPosture.REGULAR, 20));
		};

		chBold.setOnAction(handler);
		chItalic.setOnAction(handler);
		btLeft.setOnAction(e -> {
			txName.setX(txName.getX() - 10);
		});
		btRight.setOnAction(e -> {
			txName.setX(txName.getX() + 10);
		});

		Scene scene = new Scene(base, 450, 150);
		primaryStage.setTitle("Demo");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}