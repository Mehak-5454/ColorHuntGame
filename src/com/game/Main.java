package com.game;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.layout.HBox;
import java.util.Random;
public class Main extends Application {
	private int speed = 1000; // milliseconds (1 second)
	private boolean gameRunning = true;

	private Timeline timeline; // declare at top
	private Label levelLabel;

    private Label wordLabel, scoreLabel, timerLabel;
    private int score = 0;
    private int time = 30;

    private String[] colors ={"RED", "BLUE", "GREEN", "YELLOW", "PINK", "ORANGE"};
    private String currentWord;

    private Button redBtn, blueBtn, greenBtn, yellowBtn, pinkBtn, orangeBtn;

    private void restartTimer() {
        timeline.stop();
        startTimer();
    }
    private int highScore = 0;
	private Label highScoreLabel;
	private void saveHighScore() {
	    try {
	        java.io.FileWriter writer = new java.io.FileWriter("highscore.txt");
	        writer.write(String.valueOf(highScore));
	        writer.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	private void loadHighScore() {
	    try {
	        java.io.File file = new java.io.File("highscore.txt");
	        if (file.exists()) {
	            java.util.Scanner sc = new java.util.Scanner(file);
	            highScore = sc.nextInt();
	            sc.close();
	        }
	    } catch (Exception e) {
	        highScore = 0;
	    }
	}
	
    @Override
    public void start(Stage stage) {

    	    wordLabel = new Label();
    	    wordLabel.setStyle(
    	    	    "-fx-font-size: 45px;" +
    	    	    	    "-fx-font-weight: bold;" +
    	    	    	    "-fx-text-fill: white;"
    	    	    	);

    	    scoreLabel = new Label("Score: 0");
    	    timerLabel = new Label("Time: 30");

    	    highScoreLabel = new Label("High Score: 0");
    	    highScoreLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: yellow;");
    	    loadHighScore();
    	    highScoreLabel.setText("High Score: " + highScore);
    	    levelLabel = new Label("Level: Medium");
    	    levelLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: lightgreen;");
    	    
    	    
    	    
    	    Button easyBtn = new Button("Easy");
    	    Button mediumBtn = new Button("Medium");
    	    Button hardBtn = new Button("Hard");
    	    easyBtn.setStyle("-fx-background-color: green; -fx-text-fill: white;");
    	    mediumBtn.setStyle("-fx-background-color: orange; -fx-text-fill: white;");
    	    hardBtn.setStyle("-fx-background-color: red; -fx-text-fill: white;");
    	    easyBtn.setOnAction(e -> {
    	        speed = 1200;
    	        levelLabel.setText("Level: Easy");
    	        restartTimer();
    	    });

    	    mediumBtn.setOnAction(e -> {
    	        speed = 800;
    	        levelLabel.setText("Level: Medium");
    	        restartTimer();
    	    });

    	    hardBtn.setOnAction(e -> {
    	        speed = 500;
    	        levelLabel.setText("Level: Hard");
    	        restartTimer();
    	    });
    	    
    	    HBox difficultyBox = new HBox(10, easyBtn, mediumBtn, hardBtn);
    	    difficultyBox.setStyle("-fx-alignment: center;");
    	    scoreLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: white;");
    	    timerLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: white;");
    	    String btnStyle =
    	    	    "-fx-font-size: 14px;" +
    	    	    "-fx-background-radius: 10;" +
    	    	    "-fx-text-fill: white;" +
    	    	    "-fx-pref-width: 200px;";

    	     redBtn = new Button("RED");
    	     blueBtn = new Button("BLUE");
    	     greenBtn = new Button("GREEN");
    	     yellowBtn = new Button("YELLOW");
    	     pinkBtn = new Button("PINK");
    	     orangeBtn = new Button("ORANGE");


    	    Button restartBtn = new Button("Restart 🔄");
    	    

    	    redBtn.setMaxWidth(Double.MAX_VALUE);
    	    blueBtn.setMaxWidth(Double.MAX_VALUE);
    	    greenBtn.setMaxWidth(Double.MAX_VALUE);
    	    yellowBtn.setMaxWidth(Double.MAX_VALUE);
    	    pinkBtn.setMaxWidth(Double.MAX_VALUE);
    	    orangeBtn.setMaxWidth(Double.MAX_VALUE);


    	    redBtn.setOnAction(e -> checkAnswer("RED"));
    	    blueBtn.setOnAction(e -> checkAnswer("BLUE"));
    	    greenBtn.setOnAction(e -> checkAnswer("GREEN"));
    	    yellowBtn.setOnAction(e -> checkAnswer("YELLOW"));
    	    pinkBtn.setOnAction(e -> checkAnswer("PINK"));
    	    orangeBtn.setOnAction(e -> checkAnswer("ORANGE"));
    	    
    	    redBtn.setStyle(btnStyle + "-fx-background-color: #e74c3c;");
    	    blueBtn.setStyle(btnStyle + "-fx-background-color: #3498db;");
    	    greenBtn.setStyle(btnStyle + "-fx-background-color: #2ecc71;");
    	    yellowBtn.setStyle(btnStyle + "-fx-background-color: #f1c40f;");
    	    pinkBtn.setStyle(btnStyle + "-fx-background-color: #ff69b4;");
    	    orangeBtn.setStyle(btnStyle + "-fx-background-color: #e67e22;");
    	    
    	    


    	    restartBtn.setOnAction(e -> resetGame());

    	    VBox root = new VBox(15);
    	    root.setStyle(
    	    			    "-fx-padding: 25;" +
    	    			    "-fx-alignment: center;" +
    	    			    "-fx-background-color: linear-gradient(to bottom, #1e3c72, #2a5298);"
    	    			);
    	    root.setSpacing(15);
    	    root.getChildren().addAll(
    	        difficultyBox,
    	        levelLabel,
    	    	wordLabel,
    	        redBtn, blueBtn, greenBtn, yellowBtn,pinkBtn,orangeBtn,
    	        scoreLabel, 
    	        highScoreLabel,
    	        timerLabel,
    	        restartBtn
    	    );

    	    nextRound();
    	    startTimer();

    	    Scene scene = new Scene(root, 400, 450);
    	    stage.setScene(scene);
    	    stage.setTitle("Color Hunt Game 🎮");
    	    stage.show();
    	}
    private void nextRound() {
   
    	    Random rand = new Random();

    	    currentWord = colors[rand.nextInt(colors.length)];
    	    String displayColor = colors[rand.nextInt(colors.length)];

    	    wordLabel.setText(currentWord);

    	    switch (displayColor) {
    	        case "RED": wordLabel.setTextFill(Color.RED); break;
    	        case "BLUE": wordLabel.setTextFill(Color.BLUE); break;
    	        case "GREEN": wordLabel.setTextFill(Color.GREEN); break;
    	        case "YELLOW": wordLabel.setTextFill(Color.YELLOW); break;
    	        case "PINK": wordLabel.setTextFill(Color.PINK); break;
    	        case "ORANGE": wordLabel.setTextFill(Color.ORANGE); break;
    	    
    	}  
    }

    private void checkAnswer(String answer) {

    	    if (!gameRunning)
    	        return;

    	    if (answer.equals(currentWord)) {
    	        score++;
    	    } else {
    	        score--;
    	    }

    	    // ⭐ CHECK HIGH SCORE
    	    if (score > highScore) {
    	        highScore = score;
    	        highScoreLabel.setText("High Score: " + highScore);
    	        saveHighScore();
    	    }

    	    scoreLabel.setText("Score: " + score);

    	    nextRound();
    	}
    private void startTimer() {

    	    timeline = new Timeline(
    	        new KeyFrame(Duration.millis(speed), e -> {

    	            time--;
    	            timerLabel.setText("Time: " + time);

    	            if (time <= 0) {
    	                gameOver();
    	            }
    	        })
    	    );

    	    timeline.setCycleCount(Timeline.INDEFINITE);
    	    timeline.play();
    	}
       

    private void gameOver() {
    	  
            gameRunning = false;
    	    timeline.stop();   // safety stop

    	    // 🔒 Disable all buttons
    	    redBtn.setDisable(true);
    	    blueBtn.setDisable(true);
    	    greenBtn.setDisable(true);
    	    yellowBtn.setDisable(true);
    	    pinkBtn.setDisable(true);
    	    orangeBtn.setDisable(true);

    	    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    	    alert.setTitle("Game Over");
    	    alert.setHeaderText("Final Score: " + score);
    	    alert.showAndWait();

    	    resetGame();
    }

    private void resetGame() {
            gameRunning = true;
    	    score = 0;
    	    time = 30;

    	    scoreLabel.setText("Score: 0");
    	    timerLabel.setText("Time: 30");

    	    // 🔓 Enable buttons again
    	    redBtn.setDisable(false);
    	    blueBtn.setDisable(false);
    	    greenBtn.setDisable(false);
    	    yellowBtn.setDisable(false);
    	    pinkBtn.setDisable(false);
    	    orangeBtn.setDisable(false);

    	    timeline.stop();   // stop old timer
    	    startTimer();      // start new timer

    	    nextRound();
    }

    public static void main(String[] args) {
        launch();
    }
}