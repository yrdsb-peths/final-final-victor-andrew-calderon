import greenfoot.*;

public class HeroHPBar extends Actor
{
    Hero hero;
    int width = 60;
    int height = 6;

    public HeroHPBar(Hero h)
    {
        hero = h;
        update();
    }

    public void act()
    {
        if (hero == null || hero.getWorld() == null) {
            getWorld().removeObject(this);
            return;
        }

        setLocation(hero.getX(), hero.getY() - 45);
        update();
    }

    private void update()
    {
        GreenfootImage img = new GreenfootImage(width, height);

        img.setColor(Color.RED);
        img.fillRect(0, 0, width, height);

        img.setColor(Color.GREEN);
        int hpWidth = (int)((hero.currentHP / (double)hero.maxHP) * width);
        img.fillRect(0, 0, hpWidth, height);

        setImage(img);
    }
}
