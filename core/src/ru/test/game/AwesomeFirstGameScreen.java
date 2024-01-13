package ru.test.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class AwesomeFirstGameScreen implements Screen {

    private final AwesomeFirstGame game;
    private OrthographicCamera camera;
    private Rectangle bucket;
    private Texture dropImage;
    private Texture bucketImage;
    private Sound dropSound;
    private Music rainMusic;
    private Array<Rectangle> raindrops;
    private long lastDropTime;
    private int dropsGathered;


    public AwesomeFirstGameScreen(AwesomeFirstGame game) {

        this.game = game;

        // load the images for the droplet and the bucket, 64x64 pixels each
        dropImage = new Texture("drop.png");
        bucketImage = new Texture("bucket.png");

        // load the drop sound effect and the rain background "music"
        dropSound = Gdx.audio.newSound(Gdx.files.internal("raid-drop.mp3"));
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain-sound.mp3"));
        rainMusic.setLooping(true);

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);
        //batch = new SpriteBatch();

        // create a Rectangle to logically represent the bucket
        bucket = new Rectangle();
        bucket.setX(800 / 2 - 64 / 2); // center the bucket horizontally
        bucket.setY(20); // bottom left corner of the bucket is 20 pixels above the bottom screen edge
        bucket.setWidth(64);
        bucket.setHeight(64);

        // create the raindrops array and spawn the first raindrop
        raindrops = new Array<>();
        spawnRaindrop();

    }

    @Override
    public void show() {
        // start the playback of the background music
        // when the screen is shown
        rainMusic.play();

    }

    @Override
    public void render(float delta) {
        // clear the screen with a dark blue color. The
        // arguments to clear are the red, green
        // blue and alpha component in the range [0,1]
        // of the color to be used to clear the screen.
        ScreenUtils.clear(0, 0, 0.2f, 1);

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.batch.setProjectionMatrix(camera.combined);

        // begin a new batch and draw the bucket and
        // all drops
        game.batch.begin();
        game.font.draw(game.batch, "Drops Collected: " + dropsGathered, 0, 480);
        game.batch.draw(bucketImage, bucket.getX(), bucket.getY(), bucket.getWidth(), bucket.getHeight());
        for (Rectangle raindrop : raindrops) {
            game.batch.draw(dropImage, raindrop.getX(), raindrop.getY());
        }
        game.batch.end();

        // process user input
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            float x = bucket.getX();
            bucket.setX(x - 200 * Gdx.graphics.getDeltaTime());
            //bucket.x -= 200 * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            float x = bucket.getX();
            bucket.setX(x + 200 * Gdx.graphics.getDeltaTime());
            //bucket.x += 200 * Gdx.graphics.getDeltaTime();
        }

        // make sure the bucket stays within the screen bounds
        if (bucket.getX() < 0) {
            bucket.setX(0);
        }
        if (bucket.getX() > 800 - 64) {
            bucket.setX(800 - 64);
        }

        // check if we need to create a new raindrop
        if (TimeUtils.nanoTime() - lastDropTime > 1000000000) {
            spawnRaindrop();
        }

        // move the raindrops, remove any that are beneath the bottom edge of
        // the screen or that hit the bucket. In the latter case we play back
        // a sound effect as well.
        for (Iterator<Rectangle> iter = raindrops.iterator(); iter.hasNext(); ) {
            Rectangle raindrop = iter.next();
            float y = raindrop.getY();
            raindrop.setY(y - 200 * Gdx.graphics.getDeltaTime());
            if (raindrop.getY() + 64 < 0) {
                iter.remove();
            }
            if (raindrop.overlaps(bucket)) {
                dropsGathered++;
                dropSound.play();
                iter.remove();
            }
        }

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        // dispose of all the native resources
        //batch.dispose();
        dropImage.dispose();
        bucketImage.dispose();
        dropSound.dispose();
        rainMusic.dispose();
    }

    private void spawnRaindrop() {
        Rectangle raindrop = new Rectangle();
        raindrop.setX(MathUtils.random(0, 800 - 64));
        raindrop.setY(480);
        raindrop.setWidth(64);
        raindrop.setHeight(64);
        raindrops.add(raindrop);
        lastDropTime = TimeUtils.nanoTime();
    }
}
