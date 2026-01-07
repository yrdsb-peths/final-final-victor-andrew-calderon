import greenfoot.*;

public class GameOverWorld extends World
{
    GreenfootSound gameOverSound = new GreenfootSound("game-over-417465.mp3");

    public GameOverWorld()
    {
        super(900, 600, 1);

        // Background
        GreenfootImage bg = new GreenfootImage("images/dungeon.jpg");
        setBackground(bg);

        // Title
        Label title = new Label("GAME OVER", 100);
        addObject(title, getWidth() / 2, getHeight() / 2 - 50);

        // Restart button
        Button restartButton = new Button("Restart", 50);
        addObject(restartButton, getWidth() / 2, getHeight() / 2 + 100);

        gameOverSound.setVolume(90);
        gameOverSound.play();
    }

    public void act()
    {
        if (!getObjects(Button.class).isEmpty())
        {
            Button btn = getObjects(Button.class).get(0);
            if (btn.isClicked())
            {
                gameOverSound.stop();
                Greenfoot.setWorld(new TitleWorld()); // go back to title
            }
        }
    }
}
