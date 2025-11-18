package game;
import gui.MonsterBattleGUI;
import java.util.ArrayList;

/**
 * Game - YOUR monster battle game!
 * 
 * Build your game here. Look at GameDemo.java for examples.
 * 
 * Steps:
 * 1. Fill in setupGame() - create monsters, items, set health
 * 2. Fill in the action methods - what happens when player acts?
 * 3. Customize the game loop if you want
 * 4. Add your own helper methods
 * 
 * Run this file to play YOUR game
 */
public class Game {
    
    // The GUI (I had AI build most of this)
    private MonsterBattleGUI gui;
    
    // Game state - YOU manage these
    private ArrayList<Monster> monsters;
    private ArrayList<Item> inventory;
    private double shieldPower = 0;
    private int playerHealth;
    private int maxHealth;
    private int playerDamage;
    private int playerHeal;
    private int playerSpeed;
    private int playerShield;
    private Monster lastAttacked;
    private boolean poisoned = false;
    
    /**
     * Main method - start YOUR game!
     */
    public static void main(String[] args) {
        Game game = new Game(); // it instantiates a copy of this file. We're not running static
        game.play(); // this extra step is unnecessary AI stuff
    }
    
    /**
     * Play the game!
     */
    public void play() {
        setupGame();
        gameLoop();
    }
    
    /**
     * Setup - create the GUI and initial game state
     */
    private void setupGame() {
        // Create the GUI
        gui = new MonsterBattleGUI("Monster Battle - Javi Edition");

        
        //  CHOOSE DIFFICULTY (number of monsters to face)
        int numMonsters = chooseDifficulty();
        monsters = new ArrayList<>();
        
        //special abilities?
        for(int k = 0; k<numMonsters; k++) {
            
            int specialPick = (int)(Math.random() * 8)+1;
            
            if(specialPick == 2){
                //special monster
                if(Math.random() > .2){
                    monsters.add(new Monster("Vampire"));
            }
            else monsters.add(new Monster());
            }
            else if(specialPick == 4){
                if(Math.random() > .3){
                    monsters.add(new Monster("Poison"));
            }
            else monsters.add(new Monster());
            }
            else if(specialPick == 6){
                if(Math.random() > .3){
                    monsters.add(new Monster("Equalizer"));
            }
            else monsters.add(new Monster());
            }
            else if(specialPick == 8){
                if(Math.random() > .4){
                    monsters.add(new Monster("Beefy"));
            }
            else monsters.add(new Monster());
            }
            else if(specialPick == 1){
                if(Math.random() > .7){
                    if(Math.random() > .8){
                        monsters.add(new Monster("Good Luck"));
                    }
                    else monsters.add(new Monster());
            }
                //else{
                 //   monsters.add(new Monster());
                //}
            }
            else{
                monsters.add(new Monster());
            }
        }
    
        
        gui.updateMonsters(monsters);
       

        // PICK YOUR CHARACTER BUILD (using the 4 action buttons!)
        pickCharacterBuild();


        
        
        
        // TODO: Create starting items
        inventory = new ArrayList<>();
        // Add items here! Look at GameDemo.java for examples
        if(Math.random() > .1) addHealthPotion();
        if(Math.random() > .3) addBomb();
        if(Math.random() > .3) addHealthPotion();
        if(Math.random() > .5) addHealthPotion();
        if(Math.random() > .7) addBomb();


        gui.updateInventory(inventory);
        
        // TODO: Customize button labels
        String[] buttons = {"Attack ("+playerDamage+"dmg)", 
                            "Defend (-"+playerShield+"dmg)", 
                            "Heal ("+playerHeal+"hp)", 
                            "Use Item"};
        gui.setActionButtons(buttons);
        
        // Welcome message
        gui.displayMessage("Battle Start! Pick your poison.");
    }
    
    /**
     * Main game loop
     * 
     * This controls the flow: player turn → monster turn → check game over
     * You can modify this if you want!
     */
    private void gameLoop() {
        // Keep playing while monsters alive and player alive
        while (countLivingMonsters() > 0 && playerHealth > 0) {
            shieldPower = 0;
            
            // PLAYER'S TURN
            gui.displayMessage("Your turn! HP: " + playerHealth);
            int action = gui.waitForAction();  // Wait for button click (0-3)
            handlePlayerAction(action);
            gui.updateMonsters(monsters);
            gui.pause(500);

            itemChance();
            
            // MONSTER'S TURN (if any alive and player alive)
            if (countLivingMonsters() > 0 && playerHealth > 0) {
                monsterAttack();
                gui.updateMonsters(monsters);
                gui.pause(500);
            }
        }
        
        // Game over!
        if (playerHealth <= 0) {
            gui.displayMessage("💀 DEFEAT! You suck...");
        } else {
            gui.displayMessage("🎉 HOORAY! You won woohoo yay wow good for you yayay wow so good hooray wooohoo yippee so proud yayay so cool!");
        }
    }
    
        /**
     * Let player choose difficulty (number of monsters) using the 4 buttons
     * This demonstrates using the GUI for menu choices!
     */
    private int chooseDifficulty() {
        // Set button labels to difficulty levels
        String[] difficulties = {"Easy (3-4)", "Medium (4-5)", "Hard (5-7)", "Extreme (7-10)"};
        gui.setActionButtons(difficulties);
        
        // Display choice prompt
        gui.displayMessage("---- CHOOSE DIFFICULTY ----");
        
        // Wait for player to click a button (0-3)
        int choice = gui.waitForAction();
        int numMonsters = 0;
        switch(choice){
            case 0:
                numMonsters = (int)(Math.random()*(4-3+1))+3; 
                break;
            case 1:
                numMonsters = (int)(Math.random()*(5-4+1))+4; 
                break;
            case 2:
                numMonsters = (int)(Math.random()*(7-5+1))+5; 
                break;
            case 3:
                numMonsters = (int)(Math.random()*(10-7+1))+7; 
                break;
        }
        
        // Determine number of monsters based on choice
        
        gui.displayMessage("You will be battling " + numMonsters + " monsters. Good Luck!");
        gui.pause(1500);
        
        return numMonsters;
    }

    private void pickCharacterBuild() {
        // Set button labels to character classes
        String[] characterClasses = {"Fighter", "Tank", "Healer", "Ninja"};
        gui.setActionButtons(characterClasses);
        
        // Display choice prompt
        gui.displayMessage("---- PICK YOUR BUILD ----");
        
        // Wait for player to click a button (0-3)
        int choice = gui.waitForAction();
        
        // Initialize default stats
        playerDamage = 50;
        playerShield = 50;
        playerHeal = 30;
        playerSpeed = 10;
        playerHealth = 100;
        maxHealth = 100;
        
        // Customize stats based on character choice
        if (choice == 0) {
            // Fighter: high damage, low healing and shield
            gui.displayMessage("You chose Fighter! High damage, but weak defense.");
            playerShield -= (int)(Math.random() * 25 + 1) + 5;  // Reduce shield by 6-50
            playerHeal -= (int)(Math.random() * 20) + 5;        // Reduce heal by 5-50
            playerSpeed = (int)(Math.random() * 6) + 5;        // calc speed by by 5-10
        } else if (choice == 1) {
            // Tank: high shield, low damage and speed
            gui.displayMessage("You chose Tank! Tough defense, but slow attacks.");
            playerSpeed = (int)(Math.random() * 9) + 1;        // calc speed by by 1-9
            playerDamage -= (int)(Math.random() * 21) + 5;   // Reduce damage by 5-25
        } else if (choice == 2) {
            // Healer: high healing, low damage and shield
            gui.displayMessage("You chose Healer! Great recovery, but fragile.");
            playerDamage -= (int)(Math.random() * 21) + 5;      // Reduce damage by 5-30
            playerShield -= (int)(Math.random() * 21) + 5;      // Reduce shield by 5-50
            playerSpeed = (int)(Math.random() * 10) + 1;        // calc speed by by 1-10
        } else {
            // Ninja: high speed, low healing and health
            gui.displayMessage("You chose Ninja! Fast and deadly, but risky.");
            playerHeal -= (int)(Math.random() * 20) + 5;        // Reduce heal by 5-50
            playerHealth -= (int)(Math.random() * 21) + 5;         // Reduce max health by 5-25
            maxHealth = playerHealth;
            playerSpeed = (int)(Math.random() * 6) + 6;        // calc speed by by 6-11
        }
        
        gui.setPlayerMaxHealth(playerHealth);
        gui.updatePlayerHealth(playerHealth);
        // Pause to let player see their choice
        gui.pause(1500);
    }
    /**
     * Handle player's action choice
     * 
     * TODO: What happens for each action?
     */
    private void handlePlayerAction(int action) {
        switch (action) {
            case 0: // Attack button
                attackMonster();
                break;
            case 1: // Defend button
                defend();
                break;
            case 2: // Heal button
                heal();
                break;
            case 3: // Use Item button
                useItem();
                break;
        }
    }
    
    /**
     * Attack a monster
     * 
     * TODO: How does attacking work in your game?
     * - How much damage?
     * - Which monster gets hit?
     * - Special effects?
     */
    private void attackMonster() {
        gui.displayMessage("Attack!");
        Monster target = getRandomLivingMonster();
        lastAttacked = target;
        if (target != null) {
            int baseDamage = (int)(playerDamage * 0.3);  // 15% of damage stat
            int damage = baseDamage + (int)(Math.random() * baseDamage);
            if(damage == 0) {
                playerHealth -= 5;
                gui.displayMessage("WOW! You suck!");
                gui. updatePlayerHealth(playerHealth);
            }
            else if(damage == playerDamage) {
                gui.displayMessage("Critical Kill! ("+damage+" dmg)");
                target.takeDamage(target.health());
            }
            else {
                target.takeDamage(damage);
                gui.displayMessage("Hit monster for "+damage+" dmg.");
            }
            
            target.takeDamage(damage);
            gui.displayMessage("Hit for " + damage + " damage!");

            
            // Show which one we hit
            int index = monsters.indexOf(target);
            gui.highlightMonster(index);
            gui.pause(600);
            gui.highlightMonster(-1);
            gui.updateInventory(inventory);
            
        }
    }
    
    /**
     * Defend
     * 
     * TODO: What does defending do?
     * - Reduce damage?
     * - Block next attack?
     * - Something else?
     */
    private void defend() {
        shieldPower = playerShield;
        
        gui.displayMessage("Shield up!");
    }
    
    /**
     * Heal yourself
     * 
     * - How much HP?
     * - Any limits?
     */
    private void heal() {
        playerHealth += playerHeal;
        int healAmount = playerHeal;
        gui.displayMessage("Healed for "+playerHeal+" HP.");
    }
    
    /**
     * Use an item from inventory
     */
    private void useItem() {
        if (inventory.isEmpty()) {
            gui.displayMessage("No items in inventory!");
            return;
        }
        
        // Use first item
        Item item = inventory.remove(0);
        gui.updateInventory(inventory);
        item.use();  // The item knows what to do!
    }
    
    /**
     * Monster attacks player
     * - How much damage?
     * - Which monster attacks?
     * - Special abilities?
     */
    private void monsterAttack() {
        //build every monster that gets to attack
        ArrayList<Monster> attackers = getSpeedyMonsters();
        // first check if there is a lastAttacked
        if(lastAttacked != null && lastAttacked.health() > 0 && !attackers.contains(lastAttacked)) 
            attackers.add(lastAttacked);

        // MONSTERS TAKE TURNS ATTACKING
        for (Monster monster : attackers) {
            // shoudn't the monster's damage dealt logic be handle in the Monster class? 
            int damageTaken = (int)(Math.random() * monster.damage() + 1);
                
            // SPECIALS
            if (!monster.special().isEmpty()) {
                if(monster.special().equals("Vampire")) {
                    monster.takeDamage(-damageTaken);
                    // JAVI SAY SOMETHING
                }
                else if(monster.special().equals("Poison")){
                    poisoned = true;
                }
                else if(monster.special().equals("Equalizer")){
                    if(monster.health() < 50){
                        if(monster.health() < 25){
                            damageTaken += damageTaken*4;
                        }
                        else damageTaken += damageTaken*2;
                    }
                }
                else if(monster.special().equals("Beefy")){
                    damageTaken += damageTaken*1.5;
                }
                else if(monster.special().equals("Good Luck")){
                    damageTaken += damageTaken*1000;
                }
            } // end specials

            //SHIELD
            if(shieldPower > 0) {
                double absorbance = Math.min(damageTaken, shieldPower);
                damageTaken -= absorbance;
                shieldPower -= absorbance;
                gui.displayMessage("You block for " + absorbance + " damage. You have " + shieldPower + " shield left.");
            }

            if(damageTaken > 0) {
                playerHealth -= damageTaken;
                gui.displayMessage("Monster hits you for " + damageTaken + " damage!");
                gui.pause(600);

            }
            // POISON 
            if(poisoned) {
                playerHealth -= 5;
                gui.updatePlayerHealth(playerHealth);
                gui.displayMessage("You took 5 poison damage.");
                gui.pause(1000);
                if(Math.random() > .6) {
                    gui.displayMessage("You recovered from the posion.");
                    gui.pause(1000);
                    playerHealth += 5;
                    poisoned = false;
            }

            }
            gui.updatePlayerHealth(playerHealth);
            int index = monsters.indexOf(monster);
            gui.highlightMonster(index);
            gui.pause(300);
            gui.highlightMonster(-1);


        }
        
    }
    
    // ==================== HELPER METHODS ====================
    // Add your own helper methods here!
    
    /**
     * Count how many monsters are still alive
     */
    
    private int countLivingMonsters() {
        int count = 0;
        for (Monster m : monsters) {
            if (m.health() > 0) count++;
        }
        return count;
    }

    private ArrayList<Monster> getSpecialMonsters(){
        //how many special monsters?
        ArrayList<Monster> result = new ArrayList<>();
        for (Monster m : monsters) {
            if (m.special()!= null && !m.special().equals("") && m.health()>0) result.add(m);
        }
        return result;
    }

    private ArrayList<Monster> getSpeedyMonsters(){
        //monsters above player speed 
        ArrayList<Monster> result = new ArrayList<>();
        for (Monster m : monsters) {
            if (m.speed() > playerSpeed && m.health() > 0) result.add(m);
        }
        return result;

    }
    
    /**
     * Get a random living monster
     */
    private Monster getRandomLivingMonster() {
        ArrayList<Monster> alive = new ArrayList<>();
        for (Monster m : monsters) {
            if (m.health() > 0) alive.add(m);
        }
        if (alive.isEmpty()) return null;
        return alive.get((int)(Math.random() * alive.size()));
    }


    private void itemChance(){
        if(Math.random() > .9){ 
            addBomb();
            gui.displayMessage("You found a Bomb!");
            gui.pause(800);
            
        }
        if(Math.random() > .9) {
            addHealthPotion();
            gui.displayMessage("You found a Health Potion!");
            gui.pause(800);
        }
    }
    
    // H E A L T H
    private void addHealthPotion() {
        inventory.add(new Item("Health Potion", "🧪", () -> {
            playerHealth += maxHealth-playerHealth;
            gui.updatePlayerHealth(playerHealth);
            gui.displayMessage("Healed for max HP!");
        }));
    }
    
    // B O M B
    private void addBomb() {
        inventory.add(new Item("Bomb", "💣", () -> {
            int bombDamage = 15 + (int)(Math.random() * 15);
            for (Monster m : monsters) {
                if (m.health() > 0) {
                    m.takeDamage(bombDamage);
                }
            }
            gui.displayMessage("BOOM! All monsters take " + bombDamage + " damage!");
            gui.updateMonsters(monsters);
            gui.pause(800);
        }));
    }
}