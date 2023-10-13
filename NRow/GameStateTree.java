package NRow;

import java.util.ArrayList;
import java.util.List;

public class GameStateTree {
    public Board gameBoard;
    public int evaluation;
    public int playerID;
    public int chosenIndx;
    public List<GameStateTree> children;
    public GameStateTree(Board board, int playerID, int chosenIndx) {
        this.gameBoard = board;
        this.playerID = playerID;
        this.chosenIndx = chosenIndx;
        this.evaluation = -1;   // computed from the simple heuristic
        this.children = new ArrayList<>();
    }

    public void addChild(GameStateTree child) {
        children.add(child);
    }
}
