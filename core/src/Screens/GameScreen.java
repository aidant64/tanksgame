package Screens;

import Managers.ImageManager;
import Physics.Calc;
import Physics.Position;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

import static com.badlogic.gdx.Gdx.gl;

public class GameScreen implements Screen, InputProcessor {

    private boolean isPaused;

    private static OrthographicCamera camera;

    private final SpriteBatch batch, hudBatch;
    private final ShapeRenderer shapeRenderer;
    private final BitmapFont font;

    private final World world;
    private float vpWidth, vpHeight, aspectRatio;

    public GameScreen () {

        isPaused = false;

        camera = new OrthographicCamera();
        camera.update();
        vpWidth = World.GAME_AREA_WIDTH / 4;

        Gdx.input.setInputProcessor(this);

        batch=new SpriteBatch();
        hudBatch=new SpriteBatch();
        shapeRenderer=new ShapeRenderer();
        font = new BitmapFont();

        ImageManager.loadImages();

        //start game!
        world=new World();
    }

    @Override
    public void resize(int width, int height){

        aspectRatio = (float) Gdx.graphics.getWidth() / Gdx.graphics.getHeight();

        vpHeight = vpWidth / aspectRatio;
        if(vpHeight > World.GAME_AREA_HEIGHT) {
            vpHeight = World.GAME_AREA_HEIGHT;
            vpWidth = vpHeight * aspectRatio;
        }

        updateCamera(true);

    }

    @Override
    public void pause() {
        isPaused=true;
    }

    @Override
    public void resume() {
        isPaused=false;
    }

    @Override
    public void hide() {
        isPaused=true;
    }

    private float[] getDesiredCamLocation(){
        float viewportW = vpWidth / 2;
        float viewportH = vpHeight / 2;
        float shipX = world.getShip().getX();
        float shipY = world.getShip().getY();

        boolean yBig = shipY + viewportH > World.GAME_AREA_HEIGHT;
        boolean ySmall = shipY - viewportH < 0;

        boolean xBig = shipX + viewportW > World.GAME_AREA_WIDTH;
        boolean xSmall = shipX - viewportW < 0;

        float desiredX = shipX;
        float desiredY = shipY;

        if(xBig){
            desiredX = World.GAME_AREA_WIDTH - viewportW;
        }else if(xSmall){
            desiredX = viewportW;
        }

        if(yBig){
            desiredY = World.GAME_AREA_HEIGHT - viewportH;
        }else if(ySmall){
            desiredY = viewportH;
        }

        return new float[]{desiredX, desiredY};

    }

    private void updateCamera(boolean isResize){
        updateDesiredViewportSize();
        float[] desiredCamLocation = getDesiredCamLocation();

        float lurch = 0.1f;
        if(isResize) lurch = 1f;

        float deltaX = (desiredCamLocation[0] - camera.position.x) * lurch;
        float deltaY = (desiredCamLocation[1] - camera.position.y) * lurch;
        float deltaW = (vpWidth - camera.viewportWidth) * lurch;
        float deltaH = (vpHeight - camera.viewportHeight) * lurch;

        camera.translate(deltaX, deltaY);
        camera.viewportWidth += deltaW;
        camera.viewportHeight += deltaH;

        if(Calc.isApproxZero(deltaX)) camera.position.x = desiredCamLocation[0];
        if(Calc.isApproxZero(deltaY)) camera.position.y = desiredCamLocation[1];
        if(Calc.isApproxZero(deltaW)) camera.viewportWidth = vpWidth;
        if(Calc.isApproxZero(deltaH)) camera.viewportHeight = vpHeight;

        camera.update();
    }

    @Override
    public void show() {
        isPaused=false;
    }

    private void updateDesiredViewportSize(){

        if(Gdx.input.isKeyPressed(Input.Keys.MINUS)){

            vpWidth *= 1.01f;
            vpHeight *= 1.01f;

            if(vpWidth > World.GAME_AREA_WIDTH){
                vpWidth = World.GAME_AREA_WIDTH;
                vpHeight = vpWidth / aspectRatio;

            }else if(vpHeight > World.GAME_AREA_HEIGHT){
                vpHeight = World.GAME_AREA_HEIGHT;
                vpWidth =  vpHeight * aspectRatio;
            }


        }else if(Gdx.input.isKeyPressed(Input.Keys.EQUALS)){

            vpWidth *= 0.99f;
            vpHeight *= 0.99f;

            if(vpWidth < 500){
                vpWidth = 500;
                vpHeight = vpWidth / aspectRatio;
            }

        }
    }

    @Override
    public void render (float deltaTime) {
        ScreenUtils.clear(0, 0, 0, 1);

        if(!isPaused) updateCamera(false);
        if(!isPaused) world.update(0.016f);

        Gdx.gl.glEnable(gl.GL_BLEND);
        Gdx.gl.glBlendFunc(gl.GL_SRC_ALPHA, gl.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BROWN);
        shapeRenderer.rect(0,0,World.GAME_AREA_WIDTH,World.GAME_AREA_HEIGHT);
        shapeRenderer.end();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        //world.renderTiledBackground(batch);
        world.render(batch);
        batch.end();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        world.renderFilled(shapeRenderer);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        world.render(shapeRenderer);
        shapeRenderer.end();

        hudBatch.begin();
        font.draw(hudBatch, "viewport: " + (int) camera.viewportWidth + ", " + (int) camera.viewportHeight, 100, 100);
        hudBatch.end();

        Gdx.gl.glDisable(gl.GL_BLEND);

    }

    @Override
    public void dispose () {
        world.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {

        world.keyDown(keycode);

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {

        if(isPaused) {
            if (keycode == Input.Keys.UP) {
                camera.translate(0, 10);
            }
            if (keycode == Input.Keys.DOWN) {
                camera.translate(0, -10);
            }
            if (keycode == Input.Keys.LEFT) {
                camera.translate(-10, 0);
            }
            if (keycode == Input.Keys.RIGHT) {
                camera.translate(10, 0);
            }
            if (keycode == Input.Keys.MINUS) {
                camera.zoom += .1f;
            }
            if (keycode == Input.Keys.EQUALS) {
                camera.zoom -= .1f;
            }
            camera.update();
        }

        world.keyUp(keycode);

        if(keycode==Input.Keys.P){
            isPaused = !isPaused;
        }

        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        Vector3 clickPosition = new Vector3(screenX,screenY,0);
        camera.unproject(clickPosition);

        world.touchDown((int) clickPosition.x, (int) clickPosition.y,pointer,button);

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        Vector3 clickPosition = new Vector3(screenX,screenY,0);
        camera.unproject(clickPosition);

        world.mouseMoved((int) clickPosition.x, (int) clickPosition.y);

        return false;
    }

    public static Position unproject(int x, int y){
        Vector3 clickPosition = new Vector3(x, y,0);
        camera.unproject(clickPosition);

        return new Position(clickPosition.x, clickPosition.y);
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {

        if(amountY > 0){
            vpWidth *= 0.95f;
        }else if(amountY < 0){
            vpWidth *= 1.05f;
        }

        if(vpWidth > World.GAME_AREA_WIDTH) vpWidth = World.GAME_AREA_WIDTH;
        if(vpWidth < 500) vpWidth = 500;

        vpHeight = vpWidth / aspectRatio;

        updateCamera(false);

        return false;
    }




}
