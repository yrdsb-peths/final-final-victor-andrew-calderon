import greenfoot.*;

public class TitleWorld extends World
{
    GreenfootImage dungeonImage = new GreenfootImage("images/dungeon.jpg");
    Label titleLabel = new Label("Fight For Democracy", 100);

    public TitleWorld()
    {
        super(900, 600, 1);
        setBackground(dungeonImage);

        addObject(titleLabel, getWidth()/2, getHeight()/2);
    }

    public void act()
    {
        if (Greenfoot.isKeyDown("space"))
        {
            MyWorld gameWorld = new MyWorld();
            Greenfoot.setWorld(gameWorld);
        }
    }
}
