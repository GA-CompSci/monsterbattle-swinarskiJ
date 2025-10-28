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
    private int playerHealth;
    private int playerDamage;
    private int playerHeal;
    private int playerSpeed;
    private int playerShield;
    
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
     * 
     * TODO: Customize this! How many monsters? What items? How much health?
     */
    private void setupGame() {
        // Create the GUI
        gui = new MonsterBattleGUI("Monster Battle - Javi Edition");

        
        // CHOOSE DIFFICULTY (number of monsters to face)
        int numMonsters = chooseDifficulty();
        monsters = new ArrayList<>();
        //special abilities?
        for(int k = 0; k<numMonsters; k++) {
            if(k==0){
                //special monster
                monsters.add(new Monster("Vampire"));
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
            
            // PLAYER'S TURN
            gui.displayMessage("Your turn! HP: " + playerHealth);
            int action = gui.waitForAction();  // Wait for button click (0-3)
            handlePlayerAction(action);
            gui.updateMonsters(monsters);
            gui.pause(500);
            
            // MONSTER'S TURN (if any alive and player alive)
            if (countLivingMonsters() > 0 && playerHealth > 0) {
                monsterAttack();
                gui.updateMonsters(monsters);
                gui.pause(500);
            }
        }
        
        // Game over!
        if (playerHealth <= 0) {
            gui.displayMessage("💀 DEFEAT! You have been defeated...");
        } else {
            gui.displayMessage("🎉 VICTORY! You defeated all monsters!");
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
        
        // Customize stats based on character choice
        if (choice == 0) {
            // Fighter: high damage, low healing and shield
            gui.displayMessage("You chose Fighter! High damage, but weak defense.");
            playerShield -= (int)(Math.random() * 25 + 1) + 5;  // Reduce shield by 6-50
            playerHeal -= (int)(Math.random() * 20) + 5;        // Reduce heal by 5-50
        } else if (choice == 1) {
            // Tank: high shield, low damage and speed
            gui.displayMessage("You chose Tank! Tough defense, but slow attacks.");
            playerSpeed -= (int)(Math.random() * 9) + 1;        // Reduce speed by 1-9
            playerDamage -= (int)(Math.random() * 40) + 100;   // Reduce damage by 100-199
        } else if (choice == 2) {
            // Healer: high healing, low damage and shield
            gui.displayMessage("You chose Healer! Great recovery, but fragile.");
            playerDamage -= (int)(Math.random() * 21) + 5;      // Reduce damage by 5-30
            playerShield -= (int)(Math.random() * 21) + 5;      // Reduce shield by 5-50
        } else {
            // Ninja: high speed, low healing and health
            gui.displayMessage("You chose Ninja! Fast and deadly, but risky.");
            playerHeal -= (int)(Math.random() * 20) + 5;        // Reduce heal by 5-50
            playerHealth -= (int)(Math.random() * 21) + 5;         // Reduce max health by 5-25
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
        // TODO: Implement your attack!
        // Hint: Look at GameDemo.java for an example
        
        gui.displayMessage("TODO: Implement attack!");
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
        // TODO: Implement your defend!
        
        gui.displayMessage("TODO: Implement defend!");
    }
    
    /**
     * Heal yourself
     * 
     * TODO: How does healing work?
     * - How much HP?
     * - Any limits?
     */
    private void heal() {
        // TODO: Implement your heal!
        
        gui.displayMessage("TODO: Implement heal!");
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
     * 
     * TODO: Customize how monsters attack!
     * - How much damage?
     * - Which monster attacks?
     * - Special abilities?
     */
    private void monsterAttack() {
        Monster target = getRandomLivingMonster();
        if (target != null) {
            int baseDamage = (int)(playerDamage * 0.15);  // 15% of damage stat
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
    
    // TODO: Add more helper methods as you need them!
    // Examples:
    // - Method to find the strongest monster
    // - Method to check if player has a specific item
    // - Method to add special effects
    // - etc.
}