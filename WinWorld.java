import greenfoot.*;

public class WinWorld extends World
{
    // ================= CONSTANTS =================
    private static final int WORLD_WIDTH = 900;
    private static final int WORLD_HEIGHT = 600;

    // ================= ASSETS =================
    private GreenfootImage background;

    public WinWorld()
    {
        super(WORLD_WIDTH, WORLD_HEIGHT, 1);
        setupBackground();
    }

    // ================= SETUP =================
    private void setupBackground()
    {
        background = new GreenfootImage("images/dungeon.jpg");
        setBackground(background);
    }
}
