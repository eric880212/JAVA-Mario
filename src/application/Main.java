package application;
	

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.text.Text;


public class Main extends Application {
	Stage stage;
	Pane root;
	Scene scene;
	
	Timeline jump, gameLoop, ground_down, winning; 
	
	ImageView player;
	ImageView groundsolid_1;
	ImageView groundsolid_2;
	ImageView groundsolid_3;
	ImageView groundsolid_4;
	ImageView playersolid_down;
	ImageView playersolid_top;
	ImageView blocksolid_1;
	ImageView ground;
	ImageView roll;
	ImageView rollsolid;
	ImageView pipe;
	ImageView pipesolid;
	ImageView block;
	
	Text restart,YouWin;
	
	Animation animationLeft,animationRight,animationUp;
	
	boolean jumppp = true, GameOver = false, win = false;
	
	final double width = 1500,height = 800;
	final double minX = 0 , maxX = width-32 ; // move range
	final double minY = 0 , maxY = height-32 ; // move rang
	double angleSpeed = 100 ;  //move speed

	@Override
	public void start(Stage primaryStage) {
		//stage = primaryStage;    	
    	
    	try {
    	scene = new Scene (FXMLLoader.load(getClass().getResource("background.fxml"))); 

    	primaryStage.setScene(scene);
		primaryStage.setTitle("Mario");
		primaryStage.show();
		
		player = (ImageView)scene.lookup("#player"); 
		playersolid_down = (ImageView)scene.lookup("#playersolid_down");
		playersolid_top = (ImageView)scene.lookup("#playersolid_top"); 
    	groundsolid_1 = (ImageView)scene.lookup("#groundsolid_1");
    	groundsolid_2 = (ImageView)scene.lookup("#groundsolid_2");
    	groundsolid_3 = (ImageView)scene.lookup("#groundsolid_3");
    	groundsolid_4 = (ImageView)scene.lookup("#groundsolid_4");
    	blocksolid_1 = (ImageView)scene.lookup("#blocksolid_1");
    	ground = (ImageView)scene.lookup("#ground");
    	roll = (ImageView)scene.lookup("#roll");
    	rollsolid = (ImageView)scene.lookup("#rollsolid");
    	pipe = (ImageView)scene.lookup("#pipe");
    	pipesolid = (ImageView)scene.lookup("#pipesolid");
    	block = (ImageView)scene.lookup("#block");
		
    	restart = (Text)scene.lookup("#restart");
    	YouWin = (Text)scene.lookup("#YouWin");
    	
		animationLeft = new SpriteAnimation(player, Duration.millis(500),5,5,0,0,31,37);
		animationRight = new SpriteAnimation(player, Duration.millis(500),5,5,0,0,31,37);
		
		//Move smooth
				final DoubleProperty angleVelocity = new SimpleDoubleProperty();
				final LongProperty lastUpdateTime = new SimpleLongProperty();
				final AnimationTimer angleAnimation = new AnimationTimer() {
				  @Override
				  public void handle(long timestamp) {
				    if (lastUpdateTime.get() > 0) {
				      final double elapsedSeconds = (timestamp - lastUpdateTime.get()) / 1_000_000_000.0 ;
				      final double deltaX = elapsedSeconds * angleVelocity.get();
				      final double oldX = player.getTranslateX();
				      final double newX = Math.max(minX, Math.min(maxX, oldX + deltaX));
				      player.setTranslateX(newX); 
				      playersolid_down.setTranslateX(newX);
				      playersolid_top.setTranslateX(newX);

				    }   
				    lastUpdateTime.set(timestamp);			    
				  }
				};
				angleAnimation.start();
				
			
				scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

					@Override
					public void handle(KeyEvent event) {
						// TODO Auto-generated method stub
						if(!GameOver && !win) {
							if (event.getCode()==KeyCode.RIGHT) {
								animationRight.play();
								angleVelocity.set(angleSpeed);
							}else if(event.getCode()==KeyCode.LEFT) {
								animationLeft.play();
								angleVelocity.set(-angleSpeed);
							}
							if( jumppp == true) {
								if(event.getCode()==KeyCode.UP ) {
									
	
									jump = new Timeline(new KeyFrame(Duration.millis(0.1), new EventHandler<ActionEvent>() {
	
										@Override
										public void handle(ActionEvent e) {
											// TODO Auto-generated method stub
											player.setLayoutY(player.getLayoutY()-0.07);
											jumppp = false;
										}
									}));
									jump.setCycleCount(4000);
									jump.play();
								
								}
							}
						 }
						
						if(event.getCode() == KeyCode.ENTER) {
							if(GameOver)
								initializeGame();
						}

						
						
					}
				});
				scene.setOnKeyReleased(new EventHandler<KeyEvent>() {

					@Override
					public void handle(KeyEvent event) {
						// TODO Auto-generated method stub
						if (event.getCode()==KeyCode.RIGHT || event.getCode()==KeyCode.LEFT) {
							animationRight.pause();
							animationLeft.pause();
							angleVelocity.set(0);
						}
						else if(event.getCode()==KeyCode.UP)
							jump.stop();
					}
				});
				Game();
    	}catch (Exception e){
    		e.printStackTrace();
    	}	
	}
	
	void Game(){
		
		gameLoop = new Timeline(new KeyFrame(Duration.millis(6), new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent e) {
				// TODO Auto-generated method stub
				playersolid_down.setLayoutX(player.getLayoutX()+8);
				playersolid_down.setLayoutY(player.getLayoutY()+31);
				playersolid_top.setLayoutX(player.getLayoutX());
				playersolid_top.setLayoutY(player.getLayoutY());

				
				if(checkCollisions(playersolid_down,groundsolid_1) || checkCollisions(playersolid_down,groundsolid_2) || checkCollisions(playersolid_down,groundsolid_3) || checkCollisions(playersolid_down,groundsolid_4)) {
					player.setLayoutY(player.getLayoutY()-0.01);
					jumppp = true;
				}
				else
					player.setLayoutY(player.getLayoutY()+1.2);
				
				if(checkCollisions(playersolid_top,blocksolid_1)) {
					jump.stop();
				}
				if(checkCollisions(playersolid_down,ground)) {
					ground_down= new Timeline(new KeyFrame(Duration.millis(6), new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent e) {
							ground.setLayoutY(ground.getLayoutY()+0.12);
						}
					}));
					ground_down.setCycleCount(200);
					ground_down.play();
					
				}
				
				if(checkCollisions(playersolid_down,pipesolid)) {
				player.setTranslateX(50);
					player.setLayoutY(10);
				}
				if(checkCollisions(playersolid_top,blocksolid_1)) {
					block.setOpacity(1);
				}


				if(checkCollisions(playersolid_down,rollsolid)) {
					win = true;
					angleSpeed = 0;
					jump.stop();
					gameLoop.stop();
					player.setTranslateX(888);
					player.setLayoutY(354);
					winning = new Timeline(new KeyFrame(Duration.millis(3), new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent arg0) {
							// TODO Auto-generated method stub
						}
					}));
					winning.setCycleCount(910);
					winning.play();
					YouWin.setOpacity(1);
				}
				
				if(player.getLayoutY()>800) {
					restart.setOpacity(1);
					GameOver = true;
					gameLoop.stop();
				}
					

			}
		}));
		
		gameLoop.setCycleCount(-1);
		gameLoop.play();
		
		initializeGame();
	}
	boolean checkCollisions(ImageView i,ImageView j) {
		if(i.getBoundsInParent().intersects(j.getBoundsInParent())) 
			return true;
		
		return false;
	}
	void initializeGame() {
		GameOver = false;
		angleSpeed = 100;
		player.setLayoutX(5);
		player.setTranslateX(50);
		player.setLayoutY(600);
		
		groundsolid_1.setOpacity(0);
		groundsolid_2.setOpacity(0);
		groundsolid_3.setOpacity(0);
		groundsolid_4.setOpacity(0);
		playersolid_down.setOpacity(0);
		playersolid_top.setOpacity(0);
		rollsolid.setOpacity(0);
		pipesolid.setOpacity(0);
		blocksolid_1.setOpacity(0);
		block.setOpacity(0);
		
		restart.setOpacity(0);
		YouWin.setOpacity(0);
		
		ground.setLayoutX(667);
		ground.setLayoutY(751);
		
		gameLoop.play();
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
}
