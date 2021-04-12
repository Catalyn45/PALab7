package game;
import java.util.LinkedList;
import java.util.List;
class Token {
    public int firstNode;
    public int secondNode;
    public int value;

    public Token(int firstNode, int secondNode, int value) {
        this.firstNode = firstNode;
        this.secondNode = secondNode;
        this.value = value;
    }
}

class TokenDoesNotExists extends Exception {
    public TokenDoesNotExists() {
        super("Token does not exists");
    }
}

class NoMoreTokens extends Exception {
    public NoMoreTokens() {
        super("No more tokens on the table");
    }
}

class Timer implements Runnable {
    private int interval;
    public Timer(int interval) {
        this.interval = interval;
    }
    @Override
    public void run() {
        int seconds = interval;
        while(true) {
            try {
                Thread.sleep(interval * 1000);
            } catch (InterruptedException e) {
                break;
            }
            System.out.println("Seconds: " + seconds);
            seconds+=interval;
        }
    }
}

public class Game {
    List<Player> players;
    List<Token> tokens;
    private final int n;
    private int lastId;

    public Game(int n) {
        players = new LinkedList<>();
        tokens = new LinkedList<>();
        this.n = n;

        for(int i = 0; i < n; i++)
            for(int j = 0; j < n; j++) {
                if(i != j) {
                    tokens.add(new Token(i, j, (int)(Math.random() * 60 + 40)));
                }
            }

        lastId = 0;
    }

    public void start() {
        List<Thread> threads = new LinkedList<>();
        Timer timer = new Timer(5);

        Thread timerThread = new Thread(timer);
        timerThread.setDaemon(true);

        for(Player player : players) {
            threads.add(new Thread(player));
        }

        timerThread.start();
        for(Thread thread : threads) {
            thread.start();
        }

        for(Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {

            }
        }

        int maxScore = 0;
        String winner = "";


        for(Player player : players) {
            if(player.calculateScore() >= maxScore) {
                maxScore = player.calculateScore();
                winner = player.getName();
            }
        }

        System.out.println("The winner is: " + winner + " with score: " + maxScore);

    }

    public void addPlayer(Player p) {
        players.add(p);
    }

    public synchronized Token getToken(int firstNode, int secondNode) throws TokenDoesNotExists, NoMoreTokens {
        if(tokens.size() == 0) {
            throw new NoMoreTokens();
        }

        for(Token token : tokens) {
            if(token.firstNode == firstNode && token.secondNode == secondNode) {
                tokens.remove(token);
                return token;
            }
        }

        throw new TokenDoesNotExists();
    }

    public int getN() {
        return n;
    }

    public synchronized boolean getTurn(int id) {
        return lastId != id;
    }

    public synchronized void endTurn(int id) {
        lastId = id;
    }
}
