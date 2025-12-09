import greenfoot.*;

public class MyWorld extends World {
    GreenfootImage dungeonImage = new GreenfootImage ("images/dungeon.jpg");
    
    public MyWorld() {
        super(600, 400, 1);
        setBackground(dungeonImage);
    }
}
