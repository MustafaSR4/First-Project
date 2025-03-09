//1221015 Mustafa Alayasa

import javafx.application.Application;
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
import javafx.stage.Stage;

public class Question3 extends Application {

	private BorderPane bpane;
	private GridPane gpane;
	private HBox hbox;
	private HBox hbox2;
	private VBox vbox;
	private Label lb1;
	private ComboBox cmBox;
	private Button btcal;
	private Label lbl1;
	private Label lbl2;

	private Label lbl3;

	private Label lbl4;

	private Label lbl5;

	private Label lbl6;
	private Label lbl7;


	
	
	
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
			gpane=new GridPane();
			gpane.setHgap(10);
			gpane.setVgap(10);
			gpane.add(new Label ("StudentID: "),0,0);
			gpane.add(new Label ("StudentName: "),0,1);
			TextField tfQuiz1 = new TextField();
		    TextField tfQuiz2 = new TextField();
		    TextField tfProject = new TextField();
		    TextField tfMidterm = new TextField();
		    TextField tfFinal = new TextField();
		    TextField tfParticipations = new TextField();
		    TextField tfCalculatedGrade = new TextField();
		    
		    gpane.addRow(2, new Label("Quiz#1: "), tfQuiz1);
		    gpane.addRow(3, new Label("Quiz#2: "), tfQuiz2);
		    gpane.addRow(4, new Label("Project: "), tfProject);
		    gpane.addRow(5, new Label("Midterm: "), tfMidterm);
		    gpane.addRow(6, new Label("Final: "), tfFinal);
		    gpane.addRow(7, new Label("Participations: "), tfParticipations);
		    gpane.addRow(8, new Label("Calculated grade: "), tfCalculatedGrade);

		    Button btCal = new Button("Calculate grade");
		    gpane.add(btCal, 1, 8);

			
			gpane.add(new Label ("Grade: "),2,8);
			gpane.add(new TextField(), 3, 8);
			
			
			hbox = new HBox(10);
			hbox2 = new HBox(10);
			vbox = new VBox (10);
			ToggleGroup group =new ToggleGroup();
			RadioButton rb1=new RadioButton("Abdallah karakra");
			RadioButton rb2=new RadioButton("Mamoun Nawahdeh ");
			RadioButton rb3=new RadioButton("Murad Njom");
			RadioButton rb4=new RadioButton("Fadi Khalil ");
			rb1.setToggleGroup(group);
			rb2.setToggleGroup(group);
			rb3.setToggleGroup(group);
			rb4.setToggleGroup(group);
			
			
			Button addTolist=new Button("Add To List");
			Button savetofile=new Button("Save To File");
			
			hbox.getChildren().addAll(rb1,rb2,rb3,rb4);
			hbox.setAlignment(Pos.CENTER);
			hbox2.getChildren().addAll(addTolist,savetofile);
			hbox2.setAlignment(Pos.CENTER);
			vbox.getChildren().addAll(hbox,hbox2);
			vbox.setAlignment(Pos.CENTER);

			cmBox =new ComboBox<>();
			String option1= "Add 2 marks";
			String option2= "Add 3 marks";

			String option3= "Best match";

			lb1=new Label ("Select Push Critira");
			
			VBox vbox3 =new VBox (10);
			vbox3.getChildren().addAll(lb1,cmBox);
			
			
			 btCal.setOnAction(e -> {
			        try {
			            double Q1 = Double.parseDouble(tfQuiz1.getText()) * 0.05;
			            double Q2 = Double.parseDouble(tfQuiz2.getText()) * 0.05;
			            double project = Double.parseDouble(tfProject.getText()) * 0.1;
			            double mid = Double.parseDouble(tfMidterm.getText()) * 0.25;
			            double fin = Double.parseDouble(tfFinal.getText()) * 0.35;
			            double part = Double.parseDouble(tfParticipations.getText()) * 0.1;
			            double res = Q1 + Q2 + project + mid + fin + part;
			            tfCalculatedGrade.setText(String.format("%.2f", res));
			        } catch (NumberFormatException ex) {
			            tfCalculatedGrade.setText("Error in input");
			        }
			    });

			
			
			
			
			
			
			
			
			bpane=new BorderPane();
			bpane.setLeft(gpane);
			bpane.setBottom(vbox);
			bpane.setRight(vbox3);
			bpane.setAlignment(vbox, Pos.CENTER);
			Scene scene= new Scene(bpane ,1000,500);
			primaryStage.setTitle("Grades");
			primaryStage.setScene(scene);
			primaryStage.show();
			
	}
	public static void main(String[]args) {
		launch(args);
	}
   
}
