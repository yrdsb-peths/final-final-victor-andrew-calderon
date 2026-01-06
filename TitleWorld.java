import greenfoot.*;

public class TitleWorld extends World
{
    GreenfootImage dungeonImage = new GreenfootImage("images/dungeon.jpg");
    Label titleLabel = new Label("Fight For Democracy", 100);
    Label instructionsLabel = new Label("Use ↑ ← ↓ → or WASD to move, SPACE to attack", 30);

    public TitleWorld()
    {
        super(900, 600, 1);
        setBackground(dungeonImage);

        // Title
        addObject(titleLabel, getWidth()/2, getHeight()/3);

        // Instructions
        addObject(instructionsLabel, getWidth()/2, getHeight()/2 + 200);

        // Start button
        StartButton startButton = new StartButton();
        addObject(startButton, getWidth()/2, getHeight()/2 + 60);
    }
}
