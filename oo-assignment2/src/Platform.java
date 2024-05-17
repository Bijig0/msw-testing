import bagel.Image;
import bagel.Input;
import bagel.Keys;
import java.util.Properties;

/**
 * Class for the platform entity.
 */
public class Platform {
    private final int Y;
    private final int SPEED_X;
    private final int MAX_COORDINATE = 3000;
    private int x;
    private Image image;

    public Platform(int x, int y, Properties props) {
        this.x = x;
        this.Y = y;
        this.SPEED_X = Integer.parseInt(props.getProperty("gameObjects.platform.speed"));
        this.image = new Image(props.getProperty("gameObjects.platform.image"));
    }

    /***
     * Method that updates the platform movement and draws it.
     */
    public void update(Input input) {
        move(input);
        image.draw(x, Y);
    }

    /***
     * Method that moves the platform based on the player's movement.
     */
    private void move(Input input){
        if (input.isDown(Keys.RIGHT)){
            this.x -= SPEED_X;
        } else if (input.isDown(Keys.LEFT)){
            if (this.x < MAX_COORDINATE){
                this.x += SPEED_X;
            }
        }
    }
}