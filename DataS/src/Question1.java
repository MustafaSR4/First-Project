
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Question1 extends Application{
	private VBox vb1, vb2;
	private HBox hb1, hb2;
	private BorderPane Bp;
	private GridPane gp;
	private Button btAdd, btSub, btMult, btDiv;
	private RadioButton rbBold, rbItalic, rbNormal;
	private ToggleGroup group;
	private Label lblFirst, lblSecond,lblResult;
	private TextField tfFirst, tfSecond;
	private ComboBox<String> cmBox;
	
	
	@Override
	public void start(Stage primaryStage) {
		
		Bp = new BorderPane();
		Bp.setPadding(new Insets(10,10,10,10));
		
		gp = new GridPane();
		gp.setPadding(new Insets(10,10,10,10));
		gp.setHgap(15);
		gp.setVgap(15);
		gp.setAlignment(Pos.CENTER);
		
		vb1 = new VBox(10);
		vb2 = new VBox(10);
		
		hb1 = new HBox(15);
		hb1.setAlignment(Pos.CENTER);
		lblSecond = new Label("Enter the second number: ");
		tfSecond = new TextField();
		
		lblFirst = new Label("Enter the first number: ");
		tfFirst = new TextField();
		
		lblResult = new Label();
		
		btAdd = new Button("+");
		btAdd.setPrefWidth(100);
		btAdd.setOnAction(e->{
			lblResult.setText(Integer.parseInt(tfFirst.getText()) + Integer.parseInt(tfSecond.getText()) + "");
		});
		
		btSub = new Button("-");
		btSub.setPrefWidth(50);
		btSub.setOnAction(e->{
			lblResult.setText(Integer.parseInt(tfFirst.getText()) - Integer.parseInt(tfSecond.getText()) + "");
		});
		
		btMult = new Button("X");
		btMult.setOnAction(e->{
			lblResult.setText(Integer.parseInt(tfFirst.getText()) * Integer.parseInt(tfSecond.getText()) + "");
		});
		
		btDiv = new Button("/");
		btDiv.setOnAction(e->{
			lblResult.setText(Integer.parseInt(tfFirst.getText()) / Integer.parseInt(tfSecond.getText()) + "");
		});
		group = new ToggleGroup();
		
		rbBold = new RadioButton("Bold");
		rbItalic = new RadioButton("Italic");
		rbNormal = new RadioButton("Normal");
		
		rbBold.setToggleGroup(group);
		rbItalic.setToggleGroup(group);
		rbNormal.setToggleGroup(group);
		
		rbBold.setOnAction(e->{
			lblResult.setFont(Font.font("Times New Roman" , FontWeight.BOLD, FontPosture.REGULAR, 18));
		});
		
		rbNormal.setOnAction(e->{
			lblResult.setFont(Font.font("Times New Roman" , FontWeight.NORMAL, FontPosture.REGULAR, 18));
		});
		
		rbItalic.setOnAction(e->{
			lblResult.setFont(Font.font("Times New Roman" , FontWeight.NORMAL, FontPosture.ITALIC, 18));
		});
		
		vb1.getChildren().addAll(rbBold,rbItalic,rbNormal);
		
		hb1.getChildren().addAll(btAdd,btSub,btMult,btDiv);
		
		cmBox = new ComboBox<>();
		cmBox.getItems().addAll("Red", "Blue", "Green");
		
		cmBox.setOnAction(e->{
			String color = cmBox.getValue();
			
			if(color.equals("Red")) 
				Bp.setStyle("-fx-background-color: red");
			else if (color.equals("Blue")) 
				Bp.setStyle("-fx-background-color: lightblue");
			else if (color.equals("Green")) 
				Bp.setStyle("-fx-background-color: lightgreen");
		});
		
		vb2.getChildren().addAll(hb1, cmBox);
		vb2.setAlignment(Pos.CENTER);
		
		
		gp.add(lblFirst, 0, 0);
		gp.add(tfFirst, 1, 0);
		gp.add(lblSecond, 0, 1);
		gp.add(tfSecond, 1, 1);
		gp.add(lblResult, 1, 2);
		gp.setHalignment(lblResult, HPos.CENTER);
		
		Bp.setLeft(vb1);
		Bp.setCenter(gp);
		Bp.setBottom(vb2);
		
		
		primaryStage.setScene(new Scene(Bp,500,500));
		primaryStage.setTitle("Calculator");
		primaryStage.show();
	}

	
	
	
	public static void main(String[] args) {
		launch(args);
	}

	
}

