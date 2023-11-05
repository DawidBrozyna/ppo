    class Dice {
    private int sides

    Dice(int sides) {
        this.sides = sides
    }

    public int roll() {
        return (int) (Math.random() * sides) + 1
    }
}

class Player {
    private String name
    private int health

    Player(String name) {
        this.name = name
        this.health = 50
    }

    public String getName() {
        return name
    }

    public int getHealth() {
        return health
    }

    public void reduceHealth(int damage) {
        this.health -= damage
    }
}

class Game {
    private HashMap<Integer, Integer> fields = new HashMap<>()
    private List<Player> players = new ArrayList<>()
    private Dice dice
    private int maxFields = 100
    private int maxTurns

    private boolean gameStarted = false
    private boolean gameEnded = false

    public void addDice(Dice dice) {
        if (!gameStarted) {
            this.dice = dice
        }
    }

    public void addPlayers(List<Player> players) {
        if (!gameStarted) {
            this.players = players
        }
    }

    public void setMaxFields(int maxFields) {
        if (!gameStarted) {
            this.maxFields = maxFields
        }
    }

    public void setMaxTurns(int maxTurns) {
        if (!gameStarted) {
            this.maxTurns = maxTurns
        }
    }

    public void startGame() {
        if (dice != null && !players.isEmpty()) {
            gameStarted = true
            prepareGame()
            run()
        }
    }

    private void prepareGame() {
        for (Player player : players) {
            fields.put(players.indexOf(player), 0)
        }
    }

    private void run() {
        int turns = 0
        Player winner = null

        while (!gameEnded) {
            for (Player player : players) {
                if (gameEnded) {
                    break
                }

                int result = dice.roll()
                int index = players.indexOf(player)
                int position = fields.get(index) + result
                if (position >= maxFields) {
                    position = maxFields
                }
                fields.put(index, position)

                println "${player.getName()} rolled ${result}. Now is on position ${position}."

                if (position >= maxFields) {
                    println "${player.getName()} won!"
                    winner = player
                    gameEnded = true
                    break
                }

                if (position % 7 == 0) {
                    int damage = dice.roll()
                    player.reduceHealth(damage)
                    println "${player.getName()} rolled ${damage} on the 7's field. Health remaining: ${player.getHealth()}."
                    if (player.getHealth() <= 0) {
                        println "${player.getName()} lost all health and is out of the game!"
                        players.remove(player)
                        if (players.size() == 1) {
                            winner = players.get(0)
                            gameEnded = true
                        }
                    }
                }
            }

            turns++
            if (maxTurns > 0 && turns >= maxTurns) {
                gameEnded = true
                println "Max turns reached!"
            }
        }
    }
}

Game game = new Game()
game.addDice(new Dice(6))
game.addPlayers([
    new Player("Anakin Skywalker"),
    new Player("Obi-Wan Kenobi")
])
game.setMaxFields(40)
game.setMaxTurns(0) 
game.startGame()        