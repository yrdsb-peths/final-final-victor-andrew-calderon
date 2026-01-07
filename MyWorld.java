import greenfoot.*;

public class MyWorld extends World
{
    GreenfootSound winSound = new GreenfootSound("winner-game-sound-404167.mp3");
    GreenfootSound gameOverSound = new GreenfootSound("game-over-417465.mp3");

    boolean winPlayed = false;
    boolean gameOverPlayed = false;
    boolean kimSpawned = false; // Track if KimJongUn is spawned

    public MyWorld()
    {
        super(900, 600, 1);
        setBackground(new GreenfootImage("images/dungeon.jpg"));

        addObject(new Hero(), 450, 200);
        addObject(new Xijinping(), 450, 150);
    }

    public void act()
    {
        checkWin();
        checkGameOver();
    }

    // Spawn KimJongUn when called by Xijinping
    public void spawnKim(int x, int y)
    {
        if (!kimSpawned)
        {
            kimSpawned = true;
            addObject(new KimJongUn(), x, y);
        }
    }

    // Win condition: all bosses gone
    private void checkWin()
    {
        if (getObjects(Xijinping.class).isEmpty() &&
            getObjects(KimJongUn.class).isEmpty() &&
            !winPlayed)
        {
            winPlayed = true;
            clearBullets();
            winSound.play();
            Greenfoot.stop();
        }
    }

    private void checkGameOver()
    {
        if (getObjects(Hero.class).isEmpty() && !gameOverPlayed)
        {
            gameOverPlayed = true;
            clearBullets();
            gameOverSound.play();
            Greenfoot.stop();
        }
    }

    private void clearBullets()
    {
        removeObjects(getObjects(Bullet.class));
    }
}
