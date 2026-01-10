import greenfoot.*;

public class Potion extends Actor
{
    public Potion()
    {
        setImage(new GreenfootImage("images/potion.png"));
        getImage().scale(30, 30);
    }

    public void act()
    {
        Hero h = (Hero)getOneIntersectingObject(Hero.class);
        if (h != null)
        {
            h.refillHP();
            getWorld().removeObject(this);
        }
    }
}
