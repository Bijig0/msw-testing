/**
 * Class that handles the collision detection.
 */
public class CollisionDetector {

    /**
     * Method that checks for a collision between the player and the given entity's position.
     */
    public static boolean isCollided(Player player, int x, int y, double radius) {
        return Math.sqrt(Math.pow(player.getX() - x, 2) +
                Math.pow(player.getY() - y, 2)) <= player.getRADIUS() + radius;
    }
}