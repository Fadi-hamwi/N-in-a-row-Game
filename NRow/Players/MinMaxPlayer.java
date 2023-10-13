package NRow.Players;

import NRow.Board;
import NRow.Game;
import NRow.Heuristics.Heuristic;
import NRow.GameStateTree;

import java.util.ArrayList;


public class MinMaxPlayer extends PlayerController {
    private final int depth;
    private final boolean withPrune;
    public MinMaxPlayer(int playerId, int gameN, int depth, Heuristic heuristic, boolean withPrune) {
        super(playerId, gameN, heuristic);
        this.depth = depth;
        //You can add functionality which runs when the player is first created (before the game starts)
        this.withPrune = withPrune;
    }

    /**
   * Implement this method yourself!
   * @param board the current board
   * @return column integer the player chose
   */
    @Override
    public int makeMove(Board board) {

        if(Game.winning(board.getBoardState(), this.gameN) != 0){  // Game is finsihed no need to do any computations.
            return Integer.MIN_VALUE;
        }

        GameStateTree root=new GameStateTree(board, this.playerId, -1);
        buildTree(root, this.depth);


        return moveMinMax(root, this.depth, Integer.MIN_VALUE, Integer.MAX_VALUE, false, this.withPrune);


    }


    /*
     This function builds the game tree recording every game state.
     */
    public void buildTree(GameStateTree node,  int depth){
        if(depth == 0 || Game.winning(node.gameBoard.getBoardState(), gameN) != 0){
            // either draw or win or lose
            node.evaluation = heuristic.evaluateBoard(node.playerID, node.gameBoard);
            return;
        }

        for(int i=0;i<node.gameBoard.width; i++){
            if(node.gameBoard.isValid(i)){
                Board newBoard = node.gameBoard.getNewBoard(i, node.playerID);

                GameStateTree newNode = new GameStateTree(newBoard , -1, i);

                newNode.playerID = (node.playerID == 1) ? 2 : 1;

                node.addChild(newNode);
                buildTree(newNode, depth-1);
            }
        }
    }

    /* now we will implement the minmax algorithm
       it has to return an index to maximize playerID chance of win. or minimize his chance of lose.
        */

    public int moveMinMax(GameStateTree node, int depth, int alpha, int beta, boolean maximizingPlayer, boolean activateAlphaBeta) {
        if(depth == 0 || Game.winning(node.gameBoard.getBoardState(), this.gameN) != 0){
            return node.chosenIndx;
        }

        int bestAction = 0;
        if(maximizingPlayer){
            node.evaluation= Integer.MIN_VALUE;
            for(GameStateTree t : node.children){
                moveMinMax(t, depth-1, alpha, beta, false, activateAlphaBeta);
                if(node.evaluation < t.evaluation){
                    node.evaluation = t.evaluation;
                    bestAction = t.chosenIndx;
                }
                if(activateAlphaBeta) {
                    alpha = Math.max(alpha, t.evaluation);
                    if (beta <= alpha) {
                        break; // Beta cutoff
                    }
                }
            }
        }else{
            node.evaluation = Integer.MAX_VALUE;
            for(GameStateTree t : node.children){
                moveMinMax(t, depth-1, alpha, beta, true, activateAlphaBeta);
                if(node.evaluation > t.evaluation){
                    node.evaluation = t.evaluation;
                    bestAction = t.chosenIndx;
                }
                if(activateAlphaBeta) {
                    beta = Math.min(beta, t.evaluation);
                    if (beta <= alpha) {
                        break; // Alpha cutoff
                    }
                }
            }
        }
        return bestAction;
    }

}
