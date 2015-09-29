package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;


public class MyGdxGame extends ApplicationAdapter{
	Batch batch; //batch to draw
	Texture boxTexture, overTexture; //textures for gameover and box
	Sprite box, over; //sprites for gameover and box
	int sy; //screen y
	int sx; //screen x
	int level; //level
	float boxSpeed; //boxspeed
	boolean touchedOut; //whether you touched outside the box
	BitmapFont keepScore, buttonFont; //score text
	String scoreText, buttonText; //the actual string in score text
	GlyphLayout glyphText, glyphButton; //need glyph of score text to get the width and height
	float widthText, heightText; //width and height of score text
	float widthButton, heightButton;
	boolean startAgain = false;
	//Preferences hs = Gdx.app.getPreferences("My Preferences");
	Sound hit,miss;

	@Override
	public void create () {
		batch = new SpriteBatch();
		sy = Gdx.graphics.getHeight();
		sx = Gdx.graphics.getWidth();
		createOver();
		createBox();
		createScore();
		createStartOver();
		touchedOut = false;
		hit = Gdx.audio.newSound(Gdx.files.internal("hit.wav"));
		miss = Gdx.audio.newSound(Gdx.files.internal("miss.wav"));
	}
	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();

		if(!touchedOut){
			box.draw(batch);
			scoreText = "" + level;
			keepScore.draw(batch,scoreText,0,sy);
			runIt();
		}
		if(touchedOut) {

		//	hs.putInteger("highscore",level);
			over.draw(batch);
			scoreText = "" + level;
			keepScore.draw(batch,scoreText,sx/2 - widthText/2 ,over.getY() - heightText);
			buttonFont.draw(batch,buttonText,0, sy);
			testTryAgain();
		}
		if(startAgain) {
			reset();
			startAgain = false;
		}
		batch.end();
	}
	public void runIt (){
		if (Gdx.input.justTouched()) {
			if ((Gdx.input.getX() > boxLeft()) && (Gdx.input.getX() < boxRight()) && (Gdx.input.getY() >= (sy  - boxTop()) - 100) && Gdx.input.getY() <= (sy - boxBottom())) {
				hit.play();
				boxSpeed += 3;
				box.setY(sy - box.getHeight());
				//box.setY(box.getY() - 10);
				level += 1;

			}
			else{
				miss.play();
				testPrint();
				touchedOut = true;
			}
		}
		if(!touchedOut) {
			if (box.getY() > (-box.getHeight()))
				//box.setY(box.getY() - boxSpeed); //Basically if Box is not completely gone keep moving down
				box.translateY(-boxSpeed);
			else {
				box.setY(sy);
			}
		}


	}
	public void testTryAgain(){
		if(Gdx.input.justTouched()){
			if(Gdx.input.getX() >0 && Gdx.input.getX() < widthButton){
				if(Gdx.input.getY() > 0 && Gdx.input.getY() < heightButton){
					startAgain = true;
				}
			}
		}
	}
	public void testPrint(){
		String tag = "";
		Gdx.app.log(tag,"Y "+Gdx.input.getY());
		Gdx.app.log(tag,"BOX TOP "+ boxTop());
		Gdx.app.log(tag,"BOX BOTTOM " + boxBottom());
		Gdx.app.log(tag, "SY - BOXTOP " + (sy - boxTop()));
		Gdx.app.log(tag, "SY - BOXBOTTOM " + (sy - boxBottom()));
	}
	public void createBox(){
		boxTexture = new Texture("BOX.png");
		box = new Sprite(boxTexture);
		boxSpeed = 1;
		box.setSize(sx / 3, sx / 3);
		box.setPosition(sx / 2 - centerX(), sy - box.getHeight());
		//box.setPosition(sx / 2 - centerX(), sy/2 - centerY());
	}
	public void createOver(){
		overTexture = new Texture("gameover.png");
		over = new Sprite(overTexture);
		over.setSize((float) (sx / 1.5), (float) ((sx / 1.5) / (500 / 70))); //original size is 500,70 so it sets the size and keeps the scale
		over.setPosition(sx/2 - over.getWidth()/2, sy/2 - over.getHeight()/2);
	}
	public void createScore(){
		glyphText = new GlyphLayout();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("myfont.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 200;
        keepScore = generator.generateFont(parameter);
        keepScore.setColor(Color.BLACK);
		scoreText = "0";
		glyphText.setText(keepScore,scoreText);
		widthText = glyphText.width;
		heightText = glyphText.height;
	}
	public void createStartOver(){
		glyphButton = new GlyphLayout();
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("myfont.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 100;
		buttonFont = generator.generateFont(parameter);
		buttonFont.setColor(Color.BLACK);
		buttonText = "Try Again";
		glyphButton.setText(buttonFont,buttonText);
		widthButton = glyphButton.width;
		heightButton = glyphButton.height;
	}
	public void reset(){
		level = 0;
		boxSpeed = 1;
		touchedOut = false;
		box.setPosition(sx / 2 - centerX(), sy - box.getHeight());

	}

	public float centerX (){
		return box.getWidth()/2;
	}
    public float centerY () {
		return box.getHeight() / 2;
	}
	public float boxTop(){ return box.getY() + box.getHeight();}
	public float boxBottom(){ return box.getY();}
	public float boxLeft(){ return box.getX();}
	public float boxRight(){ return box.getX() + box.getWidth();}

}