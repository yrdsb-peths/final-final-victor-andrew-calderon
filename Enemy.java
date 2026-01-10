import greenfoot.*;

public abstract class Enemy extends Actor
{
    protected int maxHP;
    protected int currentHP;
    protected boolean dead = false;

    protected void die()
    {
        if (dead) return;
        dead = true;

        World w = getWorld();
        if (w instanceof MyWorld)
        {
            ((MyWorld) w).enemyKilled();
        }

        w.removeObject(this);
    }
}
