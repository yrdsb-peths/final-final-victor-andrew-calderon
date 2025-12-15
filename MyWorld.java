import greenfoot.*;

public class MyWorld extends World {
    GreenfootImage dungeonImage = new GreenfootImage ("images/dungeon.jpg");
    
    public MyWorld() {
        super(900, 600, 1);
        setBackground(dungeonImage);
        
        Hero hero = new Hero();
        addObject(hero, 300, 200);
        
        Xijinping xijinping = new Xijinping();
        addObject(xijinping, 100, 150);
        
        Bullet bullet = new Bullet();
        addObject(bullet, 400, 400);
    }
}
