import greenfoot.*;

public class GameOverWorld extends World
{
    // Constants
    private static final int WORLD_WIDTH = 900;
    private static final int WORLD_HEIGHT = 600;
    private static final int TEXT_SIZE = 100;
    private static final int BUTTON_SIZE = 50;
    private static final int SOUND_VOLUME = 90;

    // Sounds
    private GreenfootSound gameOverSound;

    // UI
    private Button restartButton;

    public GameOverWorld()
    {
        super(WORLD_WIDTH, WORLD_HEIGHT, 1);

        setupBackground();
        setupUI();
        playGameOverSound();
    }

    private void setupBackground()
    {
        setBackground(new GreenfootImage("images/dungeon.jpg"));
    }

    private void setupUI()
    {
        Label title = new Label("GAME OVER", TEXT_SIZE);
        addObject(title, getWidth() / 2, getHeight() / 2 - 50);

        restartButton = new Button("Restart", BUTTON_SIZE);
        addObject(restartButton, getWidth() / 2, getHeight() / 2 + 100);
    }

    private void playGameOverSound()
    {
        gameOverSound = new GreenfootSound("game-over-417465.mp3");
        gameOverSound.setVolume(SOUND_VOLUME);
        gameOverSound.play();
    }

    public void act()
    {
        if (restartButton.isClicked())
        {
            gameOverSound.stop();
            Greenfoot.setWorld(new TitleWorld());
        }
    }
}
