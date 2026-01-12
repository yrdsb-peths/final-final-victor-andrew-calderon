import greenfoot.*;

public class WinWorld extends World
{
    // ================= CONSTANTS =================
    private static final int WORLD_WIDTH = 900;
    private static final int WORLD_HEIGHT = 600;

    // ================= ASSETS =================
    private GreenfootImage background;
    private Label winLabel;
    private Label subLabel;
    private Label hintLabel;

    public WinWorld()
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
        winLabel = new Label("YOU WIN", 100);
        winLabel.setFillColor(Color.YELLOW);

        subLabel = new Label("The boss has been defeated", 40);
        subLabel.setFillColor(Color.WHITE);

        hintLabel = new Label("Press ENTER to play again | ESC to quit", 30);
        hintLabel.setFillColor(Color.LIGHT_GRAY);

        addObject(winLabel, getWidth() / 2, getHeight() / 2 - 80);
        addObject(subLabel, getWidth() / 2, getHeight() / 2);
        addObject(hintLabel, getWidth() / 2, getHeight() / 2 + 70);
    }

    // ================= INPUT =================
    public void act()
    {
        if (Greenfoot.isKeyDown("enter"))
        {
            Greenfoot.setWorld(new TitleWorld());
        }

        if (Greenfoot.isKeyDown("escape"))
        {
            Greenfoot.stop();
        }
    }
}
