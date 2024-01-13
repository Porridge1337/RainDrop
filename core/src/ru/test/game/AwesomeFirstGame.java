package ru.test.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class AwesomeFirstGame extends Game {

    protected SpriteBatch batch;
    protected BitmapFont font;


    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont(); // use libGDX's default Arial font
        this.setScreen(new AwesomeFirstGameMainMenuScreen(this));
    }

    //A common mistake is to forget to call super.render() with a Game implementation. Without this call,
    // the Screen that you set in the create() method will not be rendered if you override the render method in your Game class!
    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
