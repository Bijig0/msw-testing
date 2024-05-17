import bagel.Image;
import bagel.Input;
import bagel.Keys;
import java.util.Properties;

/**
 * Class for the enemy.
 */
public class Enemy {
    private final int Y;
    private final double RADIUS;
    private final int SPEED_X;
    private final double DAMAGE_SIZE;
    private Image image;
    private int x;
    private boolean killedTarget = false;
    private boolean hitPlayer = false;

    public Enemy(int x, int y, Properties props) {
        this.x = x;
        this.Y = y;
        this.RADIUS = Double.parseDouble(props.getProperty("gameObjects.enemy.radius"));
        this.DAMAGE_SIZE = Double.parseDouble(props.getProperty("gameObjects.enemy.damageSize"));
        this.SPEED_X = Integer.parseInt(props.getProperty("gameObjects.enemy.speed"));
        this.image = new Image(props.getProperty("gameObjects.enemy.image"));
    }

    /***
     * Method that updates the enemy movement and draws it. Also checks for collisions with the player.
     */
    public void updateWithTarget(Input input, Player target) {
        move(input);
        image.draw(x, Y);

        if (target != null && CollisionDetector.isCollided(target, this.x, this.Y, this.RADIUS) && !hitPlayer) {
            hitPlayer = true;
            damageTarget(target);
        }
    }

    /***
     * Method that moves the enemy based on the player's movement.
     */
    private void move(Input input) {
        if (input.isDown(Keys.RIGHT)){
            this.x -= SPEED_X;
        } else if (input.isDown(Keys.LEFT)){
            this.x += SPEED_X;
        }
    }

    /***
     * Method that damages the player. If the health of the player is less than or equal to 0,
     * the player will be marked as dead.
     */
    private void damageTarget(Player player) {
        double newHealth = player.getHealth() - DAMAGE_SIZE;
        player.setHealth(newHealth);

        if (newHealth <= 0 && !killedTarget) {
            player.dead();
            killedTarget = true;
        }
    }
}