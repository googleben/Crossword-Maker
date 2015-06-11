package com.ben.ui;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.ben.cross.Crossword;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class CrossUI extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public Stage stage;
	public String key;
	public int freq;
	public String location;
	
	public void start(Stage primaryStage) {
		stage = primaryStage;
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(2,0,2,0));
		grid.setStyle("-fx-background-color:#fff;");
		//grid.setGridLinesVisible(true);
		ColumnConstraints columnCon = new ColumnConstraints();
		columnCon.setHalignment(HPos.CENTER);
		grid.getColumnConstraints().add(columnCon);
		
		Label title = new Label("Steam Key");
		grid.add(title,0,0);
		TextArea keyA = new TextArea();
		keyA.setMaxSize(180, 5);
		grid.add(keyA,1,0);
		
		Label fTitle = new Label("# Characters To Replace");
		grid.add(fTitle, 0, 1);
		TextArea fr = new TextArea();
		fr.setMaxSize(180, 5);
		grid.add(fr, 1, 1);
		
		Label locLabel = new Label("Location Of Wordlist");
		grid.add(locLabel,0,2);
		TextArea loc = new TextArea();
		loc.setMaxSize(180, 5);
		grid.add(loc, 1, 2);
		
		Button run = new Button("Go!");
		run.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				key = keyA.getText();
				freq = Integer.parseInt(fr.getText());
				location = loc.getText();
				makeCross();
			}
		});
		grid.add(run,0,3);
		
		
		Scene scene = new Scene(grid, 400,200);
		stage.setScene(scene);
		stage.show();
        stage.setTitle("Crossword");
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
        	@Override
        	public void handle(WindowEvent e) {
        		System.exit(0);
        	}
        });
	}
	
	public void makeCross() {
		int mul = 50; //size multiplier for 0,1,2 grid to 0,10,20 grid
		Crossword cr = new Crossword(key, freq);
		cr.getWordsFromFile(location);
		cr.make();
		Pane pane = new Pane();
		String[] fin = cr.fin;
		int[] center = cr.center;
		int bigB = cr.getSpaceBefore();
		for (int i = 0; i<fin.length; i++) {
			int cent = center[i];
			String s = fin[i];
			int before = bigB-cent;
			String ans = "";
			for (int j = 0; j<before; j++) ans+="ˆ"; //not exponent key no worries
			ans+=s;
			for (int j = 0; j<ans.length(); j++) {
				char c = ans.charAt(j);
				
				if (c!=' ' && c!='ˆ') {
					
					Rectangle r = new Rectangle(j*mul + (0.2*mul/2),i*mul + (0.2*mul/2),mul,mul);
					r.setFill(Color.YELLOW);
					if ((s.length()>1 && j==bigB) || s.length()==1) r.setFill(Color.ORANGERED);
					r.setStroke(Color.BLACK);
					r.setStrokeWidth(0.2*mul);
					pane.getChildren().add(r);
					
					String square = ""+c;
					if (s.length()>1 && j==bigB) square=" ";
					
					Text t = new Text( ((j*mul)+(mul/6)) + (0.2*mul/2), ((i*mul)+(mul/5*4)) + (0.2*mul/2), square);
					t.setFont(new Font("Courier New", mul));
					pane.getChildren().add(t);
					
				} else if (c==' ') {
					Rectangle r = new Rectangle(j*mul + (0.2*mul/2),i*mul + (0.2*mul/2),mul,mul);
					r.setFill(Color.BLACK);
					r.setStroke(Color.BLACK);
					r.setStrokeWidth(0.2*mul);
					pane.getChildren().add(r);
				}
				
			}
		}
		
		WritableImage image = pane.snapshot(new SnapshotParameters(), null);
		File imageFile = new File("output.png");
		try {
			ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", imageFile);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
		
		Scene scene = new Scene(pane);
		stage.setScene(scene);
		stage.show();
	    stage.setTitle("Crossword");
	    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	    	@Override
	    	public void handle(WindowEvent e) {
	    		System.exit(0);
	    	}
	    });
	    
	}
	
}
