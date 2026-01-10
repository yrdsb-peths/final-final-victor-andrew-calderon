import greenfoot.*;

public class Shell extends Actor
{
    public void act()
    {
        Hero hero = (Hero)getOneIntersectingObject(Hero.class);
        if (hero != null)
        {
            hero.activateShield(5000); // 5 sec
            getWorld().removeObject(this);
        }
    }
}
