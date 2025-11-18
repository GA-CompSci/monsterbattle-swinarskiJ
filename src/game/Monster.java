package game;
public class Monster {
    // INSTANCE VARIABLES (properties)
    private int health;
    private double damage;
    private int speed;
    private String special; 

    // CONSTRUCTOR
    public Monster(){
        // randomly generate health, damage, speed
        health = (int)(Math.random() * 60 + 1) + 20;

        // random 5 - 20
        damage = (Math.random() * 15) + 5;

        // speed: random 1-8
        speed = (int)(Math.random() * 10) + 1;

        // by default, the monster doesn't have a special move
        special = "";
    }
    // OVERLOADED CONSTRUCTOR
    public Monster(String special){
        this();
        this.special = special;
    }
    
    // ACCESSOR METHODS
    public int health() { return this.health; }
    public double damage() { return Math.round(damage * 100.0) / 100.0; }
    public int speed() { return speed; }
    public String special() { return this.special; }

    // MUTATOR METHODS
    public void takeDamage(int dmg){
        health -= dmg;
    }
}