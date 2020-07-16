package com.example.ttt;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    //declaring an array of button variables instead of creating them one by one
    Button buttonNames[] = new Button[9];
    //declaring result textview
    TextView result;

    /*create an array of squares values for each player; it will get populated by the following values along the game:
        0: available square;
        1: square already played by the player
       -1: square already played by the other player */
    int[] cells = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    int[] cellsA = cells.clone();
    int[] cellsB = cells.clone();

    //create the array of winning moves
    ArrayList<int[]> winningMoves = new ArrayList<>(Arrays.asList(
            new int[]{1, 2, 3},
            new int[]{4, 5, 6},
            new int[]{7, 8, 9},
            new int[]{3, 5, 7},
            new int[]{1, 4, 7},
            new int[]{2, 5, 8},
            new int[]{3, 6, 9},
            new int[]{1, 5, 9}
    ));

    //creating a copy of winningMove array for each player to loop through and modify as the game progresses
    ArrayList<int[]> winningMovesA = (ArrayList<int[]>) winningMoves.clone();
    ArrayList<int[]> winningMovesB = (ArrayList<int[]>) winningMoves.clone();

    /*creating an array of possible winningMove indices for each button,
    so that when a button is clicked, only those moved to be checked.*/
    ArrayList<int[]> buttonMovesMap = new ArrayList(Arrays.asList(
            new int[]{0, 4, 7},
            new int[]{0, 5},
            new int[]{0, 3, 6},
            new int[]{1, 4},
            new int[]{1, 3, 5, 7},
            new int[]{1, 6},
            new int[]{2, 3, 4},
            new int[]{2, 5},
            new int[]{2, 6, 7}
    ));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initializing buttons and assigning click listener to them.
        for (int i = 0; i < 9; i++) {
            int id = i + 1;
            String button = "button_" + id;
            int buttonId = getResources().getIdentifier(button, "id", getPackageName());
            buttonNames[i] = findViewById(buttonId);
            final int cellId = i;// getting the value of i to use in the inner class setOnClickListener(i can't be used, as it is not final)
            System.out.println("button create " + " " + i + " " + button + " " + id + " " + buttonNames[i]);
            buttonNames[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //checking if user wins the game or the game should be continued
                    boolean gameCont = play(cellId, cellsA, cellsB, winningMovesA, "X");
                    boolean zero = containsZero(cellsB);//checking if there is any move available for computer
                    if (gameCont && zero) {//if both conditions are met, computer plays
                        computerPlays();
                    /*if there is no move available neither for the user nor fo the computer,
                    the game will bre restarted as it is tie*/
                    } else if (!zero) {
                        Toast.makeText(MainActivity.this, "It is a tie", Toast.LENGTH_LONG).show();
                        resetGame();
                    }
                }
            });
        }
    }

    /*defining play method
      @Param cellId: the id of the button just tapped
      @Param player: the player's array of moves
      @Param counter: the other player's array of moves
      @Param winMovArr: the player's copy of winningMove,
      @Parak mark: X if user plays, O if computer plays */
    protected boolean play(int cellId, int[] player, int[] counter, ArrayList<int[]> winMovArr, String mark) {
        if (player[cellId] == 0) {//check if there is any square(button) available for player to tap
            buttonNames[cellId].setText(mark);//change the button to X fo the user and O for computer
            //change the mark color to a different color for each player
            int color = (player == cellsA) ? getResources().getColor(R.color.colorPrimaryDark) : getResources().getColor(R.color.red);
            buttonNames[cellId].setTextColor(color);
            //set the square for the both players as a played square, so it won't be available anymore
            player[cellId] = 1;
            counter[cellId] = -1;
            //check if the move just played is a winning move
            if (winningMove(cellId, player, winMovArr)) {
                String winner;
                winner = (player == cellsA) ? "You" : "Computer";//change the winner name based on who wins
                Toast.makeText(this, winner + " won!", Toast.LENGTH_SHORT).show();
                resetGame();
                return false;
            }
            //informing player that the button just tapped is already marked
        } else {
            Toast.makeText(this, "Tap another button", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /*check if the a move is winning move
       @Param cellId: the id of the button just tapped
       @Param player: the player who just played
       @Param winMoveArr: the player's winning move array
     It uses buttonMovesMap based on the button tapped to see which winning moves should be checked and at the end
     it will remove the matched move from the list, so that it won't be checked again; */
    protected boolean winningMove(int cellId, int[] player, ArrayList<int[]> winMovArr) {
        for (int i = 0; i < buttonMovesMap.get(cellId).length; i++) {
            try {
                int[] movesToCheck = winMovArr.get(buttonMovesMap.get(cellId)[i]);
                if (player[movesToCheck[0] - 1] == 1 &&
                        player[movesToCheck[1] - 1] == 1 &&
                        player[movesToCheck[2] - 1] == 1) {
                    winMovArr.remove(buttonMovesMap.get(cellId)[i]);
                    return true;
                }
            } catch (IndexOutOfBoundsException e) {
                continue;
            }
        }
        return false;
    }

    /*computer select which button should be selected for its move; first it look for the button which blocks user;
      if there is no need to block the user, it randomly selects an available square (button) to play*/
    protected void computerPlays() {
        for (int i = 0; i < winningMovesB.size(); i++) {
            int countMinus = 0;
            int countPlus = 0;
            int nextCell = -1;// initialize the next cell (button) to -1 as a flag
            //first check if it can win with the next move
            for (int j = 0; j < 3; j++) {
                if (cellsB[winningMovesB.get(i)[j] - 1] == 1) {
                    countMinus++;
                } else if (cellsB[winningMovesB.get(i)[j] - 1] == 0) {
                    nextCell = winningMovesB.get(i)[j] - 1;
                }
            }
            if ((countMinus == 2) && nextCell != -1) {
                play(nextCell, cellsB, cellsA, winningMovesB, "O");
                return;
            }
            //then it checks if it should block the other player
            for (int j = 0; j < 3; j++) {
                if (cellsB[winningMovesB.get(i)[j] - 1] == -1) {
                    countPlus++;
                } else if (cellsB[winningMovesB.get(i)[j] - 1] == 0) {
                    nextCell = winningMovesB.get(i)[j] - 1;
                }
            }
            if ((countPlus == 2) && nextCell != -1) {
                play(nextCell, cellsB, cellsA, winningMovesB, "O");
                return;
            }
        }
        //finally, it will play a random square if there is no winning chance or no need to block the other player
        int randomButton = (new Random()).nextInt(9);
        while (cellsB[randomButton] == -1 || cellsB[randomButton] == 1) {
            randomButton = (new Random()).nextInt(9);
        }
        play(randomButton, cellsB, cellsA, winningMovesB, "O");
    }

    //resetting game to initial state
    private void resetGame() {
        for (int i = 0; i < buttonNames.length; i++) {
            buttonNames[i].setEnabled(false);
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                winningMovesA = (ArrayList<int[]>) winningMoves.clone();
                winningMovesB = (ArrayList<int[]>) winningMoves.clone();
                cellsA = cells.clone();
                cellsB = cells.clone();
                //setting back all squares (buttons) to their initial number values
                for (int i = 0; i < buttonNames.length; i++) {
                    buttonNames[i].setText(String.valueOf(i + 1));
                    buttonNames[i].setTextColor(getResources().getColor(R.color.black));
                    buttonNames[i].setEnabled(true);
                }
            }
        }, 4000);
    }

    //check if ther is any more squares (buttons) available to play
    public static boolean containsZero(int[] arr) {
        for (int s : arr) {
            if (s == 0)
                return true;
        }
        return false;
    }
}