package com.example.ttt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class MojActivity extends AppCompatActivity {

    Button button_1, button_2, button_3, button_4, button_5, button_6, button_7, button_8, button_9;
    TextView result;

    //create a variable to set the current player
    String player;

    //create an array of cells for each player
    int[] cellsA = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    int[] cellsB =  {0, 0, 0, 0, 0, 0, 0, 0, 0};

    //create two array of winning moves for each player
    int[][] winningMoveA = {
        {1,2,3},
        {4,5,6},
        {7,8,9},
            {3,5,7},
        {1,4,7},
        {2,5,8},
        {3,6,9},
        {1,5,9},   
    };
    int[][] winningMoveB = {
            {1,2,3},
            {4,5,6},
            {7,8,9},
            {3,5,7},
            {1,4,7},
            {2,5,8},
            {3,6,9},
            {1,5,9},
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}