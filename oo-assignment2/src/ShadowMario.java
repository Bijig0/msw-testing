import bagel.*;
import java.util.Properties;

/**
 * Sample solution for SWEN20003 Project 1, Semester 1, 2024
 *
 * @author Dimuthu Kariyawasan & Tharun Dharmawickrema
 */
public class ShadowMario extends AbstractGame {

    private final int WINDOW_HEIGHT;
    private final String GAME_TITLE;
    private final Image BACKGROUND_IMAGE;
    private final String FONT_FILE;
    private final Font TITLE_FONT;
    private final int TITLE_X;
    private final int TITLE_Y;
    private final String INSTRUCTION;
    private final Font INSTRUCTION_FONT;
    private final int INS_Y;
    private final Font MESSAGE_FONT;
    private final int MESSAGE_Y;
    private final Properties PROPS;
    private final Properties MESSAGE_PROPS;
    private final Font SCORE_FONT;
    private final int SCORE_X;
    private final int SCORE_Y;
    private final Font HEALTH_FONT;
    private final int HEALTH_X;
    private final int HEALTH_Y;
    private int score;
    private boolean finished = false;
    private Player player;
    private Platform platform;
    private Enemy[] enemies;
    private Coin[] coins;
    private EndFlag endFlag;
    private boolean started = false;

    public ShadowMario(Properties game_props, Properties message_props) {
        super(Integer.parseInt(game_props.getProperty("windowWidth")),
              Integer.parseInt(game_props.getProperty("windowHeight")),
              message_props.getProperty("title"));

        WINDOW_HEIGHT = Integer.parseInt(game_props.getProperty("windowHeight"));
        GAME_TITLE = message_props.getProperty("title");
        BACKGROUND_IMAGE = new Image(game_props.getProperty("backgroundImage"));
        FONT_FILE = game_props.getProperty("font");

        TITLE_FONT = new Font(FONT_FILE, Integer.parseInt(game_props.getProperty("title.fontSize")));
        TITLE_X = Integer.parseInt(game_props.getProperty("title.x"));
        TITLE_Y = Integer.parseInt(game_props.getProperty("title.y"));

        SCORE_FONT = new Font(FONT_FILE, Integer.parseInt(game_props.getProperty("score.fontSize")));
        SCORE_X = Integer.parseInt(game_props.getProperty("score.x"));
        SCORE_Y = Integer.parseInt(game_props.getProperty("score.y"));

        INSTRUCTION = message_props.getProperty("instruction");
        INSTRUCTION_FONT = new Font(FONT_FILE, Integer.parseInt(game_props.getProperty("instruction.fontSize")));
        INS_Y = Integer.parseInt(game_props.getProperty("instruction.y"));

        MESSAGE_FONT = new Font(FONT_FILE, Integer.parseInt(game_props.getProperty("message.fontSize")));
        MESSAGE_Y = Integer.parseInt(game_props.getProperty("message.y"));

        HEALTH_FONT = new Font(FONT_FILE, Integer.parseInt(game_props.getProperty("health.fontSize")));
        HEALTH_X = Integer.parseInt(game_props.getProperty("health.x"));
        HEALTH_Y = Integer.parseInt(game_props.getProperty("health.y"));

        this.PROPS = game_props;
        this.MESSAGE_PROPS = message_props;
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        Properties game_props = IOUtils.readPropertiesFile("res/app.properties");
        Properties message_props = IOUtils.readPropertiesFile("res/message_en.properties");
        ShadowMario game = new ShadowMario(game_props, message_props);
        game.run();
    }

    /**
     * Performs a state update.
     * Allows the game to exit when the escape key is pressed.
     */
    @Override
    protected void update(Input input) {

        // close window
        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }

        BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);

        if (!started) {
            TITLE_FONT.drawString(GAME_TITLE, TITLE_X, TITLE_Y);
            INSTRUCTION_FONT.drawString(INSTRUCTION,
                    Window.getWidth() / 2 - INSTRUCTION_FONT.getWidth(INSTRUCTION)/2, INS_Y);

            if (input.wasPressed(Keys.SPACE)) {
                started = true;
                finished = false;
                score = 0;

                String[][] lines = IOUtils.readCsv(this.PROPS.getProperty("levelFile"));
                populateGameObjects(lines);
            }
        } else if (player.isDead() && player.getY() > WINDOW_HEIGHT) {
            String message = MESSAGE_PROPS.getProperty("gameOver");
            MESSAGE_FONT.drawString(message,
                    Window.getWidth() / 2 - MESSAGE_FONT.getWidth(message)/2,
                    MESSAGE_Y);
            if (input.wasPressed(Keys.SPACE)) {
                started = false;
            }
        } else {
            if (finished) {
                String message = MESSAGE_PROPS.getProperty("gameWon");
                MESSAGE_FONT.drawString(message,
                        Window.getWidth() / 2 - MESSAGE_FONT.getWidth(message)/2,
                        MESSAGE_Y);
                if(input.wasPressed(Keys.SPACE)) {
                    started = false;
                 }
            } else {
                // game is running
                SCORE_FONT.drawString(MESSAGE_PROPS.getProperty("score") + score, SCORE_X, SCORE_Y);
                HEALTH_FONT.drawString(MESSAGE_PROPS.getProperty("health") + Math.round(player.getHealth()*100),
                        HEALTH_X, HEALTH_Y);
                updateGameObjects(input);
            }
        }
    }

    /**
     * Method that creates the game objects using the lines read from the CSV file.
     */
    private void populateGameObjects(String[][] lines) {

        coins = new Coin[Integer.parseInt(this.PROPS.getProperty("gameObjects.coin.coinCount"))];
        enemies = new Enemy[Integer.parseInt(this.PROPS.getProperty("gameObjects.enemy.enemyCount"))];

        int enemyIndex = 0;
        int coinIndex = 0;

        for(String[] lineElement: lines) {
            int x = Integer.parseInt(lineElement[1]);
            int y = Integer.parseInt(lineElement[2]);

            if (lineElement[0].equals("PLAYER")) {
                player = new Player(x, y, this.PROPS);
            } else if (lineElement[0].equals("PLATFORM")) {
                platform = new Platform(x, y, this.PROPS);
            } else if (lineElement[0].equals("ENEMY")) {
                Enemy enemy = new Enemy(x, y, PROPS);
                enemies[enemyIndex++] = enemy;
            } else if (lineElement[0].equals("COIN")) {
                Coin coin = new Coin(x, y, PROPS);
                coins[coinIndex++] = coin;
            } else if (lineElement[0].equals("END_FLAG")) {
                endFlag = new EndFlag(x, y, PROPS);
            }
        }
    }

    /**
     * Method that updates the game objects each frame, when the game is running.
     */
    public void updateGameObjects(Input input) {

        platform.update(input);

        for(Enemy e: enemies) {
            e.updateWithTarget(input, player);
        }

        for(Coin c: coins) {
            score += c.updateWithTarget(input, player);
        }

        player.update(input);
        endFlag.updateWithTarget(input, player);

        if(endFlag.isCollided()) {
            finished = true;
        }
    }
}