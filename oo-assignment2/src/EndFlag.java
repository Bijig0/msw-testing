import bagel.Image;
import bagel.Input;
import bagel.Keys;
import java.util.Properties;

/**
 * Class for the end flag entity.
 */
public class EndFlag {
    private final int Y;
    private final double RADIUS;
    private final int SPEED_X;
    private int x;
    private Image image;
    private boolean isCollided = false;

    public EndFlag(int x, int y, Properties props) {
        this.x = x;
        this.Y = y;
        this.RADIUS = Double.parseDouble(props.getProperty("gameObjects.endFlag.radius"));
        this.SPEED_X = Integer.parseInt(props.getProperty("gameObjects.endFlag.speed"));
        this.image = new Image(props.getProperty("gameObjects.endFlag.image"));
    }

    /***
     * Method that updates the end flag movement and draws it. Also checks for collisions with the player.
     */
    public void updateWithTarget(Input input, Player target) {
        move(input);
        image.draw(x, Y);

        if (CollisionDetector.isCollided(target, this.x, this.Y, this.RADIUS) && !isCollided) {
            isCollided = true;
        }
    }

    /***
     * Method that moves the end flag based on the player's movement.
     */
    private void move(Input input) {
        if (input.isDown(Keys.RIGHT)){
            this.x -= SPEED_X;
        } else if (input.isDown(Keys.LEFT)){
            this.x += SPEED_X;
        }
    }

    public boolean isCollided() {
        return isCollided;
    }
}