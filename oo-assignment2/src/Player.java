import bagel.Image;
import bagel.Input;
import bagel.Keys;
import java.util.Properties;

/**
 * Class for the player.
 */
public class Player {
    private final int X;
    private final int INITIAL_Y;
    private final double RADIUS;
    private final Properties PROPS;
    private final int INITIAL_JUMP_SPEED = -20;
    private final int FALL_SPEED = 2;
    private int y;
    private int speedY = 0;
    private double health;
    private Image image;

    public Player(int x, int y, Properties props) {
        this.X = x;
        this.y = y;
        this.INITIAL_Y = y;
        this.PROPS = props;
        this.RADIUS = Double.parseDouble(props.getProperty("gameObjects.player.radius"));
        this.image = new Image(props.getProperty("gameObjects.player.imageRight"));
        this.health = Double.parseDouble(props.getProperty("gameObjects.player.health"));
    }

    /**
     * Method that updates the image rendered based on user input and draws it.
     */
    public void update(Input input) {
        if (input.wasPressed(Keys.LEFT)) {
            image = new Image(this.PROPS.getProperty("gameObjects.player.imageLeft"));
        }
        if (input.wasPressed(Keys.RIGHT)) {
            image = new Image(this.PROPS.getProperty("gameObjects.player.imageRight"));
        }
        image.draw(X, y);
        jump(input);
    }

    /**
     * Method that handles the player's jumping movement.
     */
    public void jump(Input input) {

        // on platform and up arrow key is pressed
        if (input.wasPressed(Keys.UP) && y == INITIAL_Y) {
            speedY = INITIAL_JUMP_SPEED;
        }

        // mid jump
        if (y < INITIAL_Y) {
            speedY += 1;
        }

        // finishing jump
        if (speedY > 0 && y >= INITIAL_Y && !isDead()) {
            speedY = 0;
            y = INITIAL_Y;
        }

        this.y += speedY;
    }

    /**
     * Method that sets the fall speed if the player's health has reached 0.
     */
    public void dead() {
        this.speedY = FALL_SPEED;
    }

    public int getX() {
        return X;
    }

    public int getY() {
        return y;
    }

    public double getRADIUS() {
        return RADIUS;
    }

    public double getHealth() {
        return health;
    }
    public void setHealth(double health) {
        this.health = health;
    }

    public boolean isDead() {
        return health <= 0;
    }
}