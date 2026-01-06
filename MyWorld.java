import greenfoot.*;

public class MyWorld extends World
{
    GreenfootSound winSound = new GreenfootSound("winner-game-sound-404167.mp3");
    GreenfootSound gameOverSound = new GreenfootSound("game-over-417465.mp3");

    boolean winPlayed = false;
    boolean gameOverPlayed = false;

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

    // ===== WIN =====
    private void checkWin()
    {
        if (getObjects(Xijinping.class).isEmpty() && !winPlayed)
        {
            winPlayed = true;
            clearBullets();          // ✅ REMOVE ALL BULLETS
            winSound.play();
            Greenfoot.stop();        // or switch to WinWorld
        }
    }

    // ===== GAME OVER =====
    private void checkGameOver()
    {
        if (getObjects(Hero.class).isEmpty() && !gameOverPlayed)
        {
            gameOverPlayed = true;
            clearBullets();          // ✅ REMOVE ALL BULLETS
            gameOverSound.play();
            Greenfoot.stop();        // or switch to GameOverWorld
        }
    }

    // ===== BULLET CLEANUP =====
    private void clearBullets()
    {
        removeObjects(getObjects(Bullet.class));
    }
}
