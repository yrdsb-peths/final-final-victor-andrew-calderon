import greenfoot.*;

public class TitleWorld extends World
{
    // ================= CONSTANTS =================
    private static final int WORLD_WIDTH = 900;
    private static final int WORLD_HEIGHT = 600;

    private static final int TITLE_SIZE = 100;
    private static final int INSTRUCTION_SIZE = 30;

    // ================= ASSETS =================
    private GreenfootImage background;

    // ================= UI =================
    private Label titleLabel;
    private Label instructionsLabel;
    private StartButton startButton;

    public TitleWorld()
    {
        super(WORLD_WIDTH, WORLD_HEIGHT, 1);

        setupBackground();
        setupUI();
    }

    // ================= SETUP =================

    private void setupBackground()
    {
        background = new GreenfootImage("images/dungeon.jpg");
        setBackground(background);
    }

    private void setupUI()
    {
        titleLabel = new Label("Fight For Democracy", TITLE_SIZE);
        addObject(titleLabel, getWidth() / 2, getHeight() / 3);

        instructionsLabel = new Label(
            "Use ↑ ← ↓ → or WASD to move, SPACE to attack",
            INSTRUCTION_SIZE
        );
        addObject(instructionsLabel, getWidth() / 2, getHeight() / 2 + 200);

        startButton = new StartButton();
        addObject(startButton, getWidth() / 2, getHeight() / 2 + 60);
    }
}
