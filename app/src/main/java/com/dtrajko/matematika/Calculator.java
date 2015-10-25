package com.dtrajko.matematika;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.io.IOException;
import java.util.Random;


public class Calculator extends ActionBarActivity {

    Random r;
    String[] operacije = new String[2];
    String current_operation_title;
    int operand_1 = 0;
    int operand_2 = 0;
    int operand_1_max_value_sabiranje = 10;
    int operand_2_max_value_sabiranje = 10;
    int operand_1_max_value_oduzimanje = 20;
    int operand_2_max_value_oduzimanje = 20;
    int rezultat = 0;
    int game_score = 0;
    int num_lives = 5;
    String life_sign = "☺";
    TextView game_score_textview;
    TextView lives_textview;
    Button dugme;
    boolean answer_checked = false;
    boolean game_end = false;
    MediaPlayer sound_succ;
    MediaPlayer sound_fail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kalkulator);

        dugme = (Button) findViewById(R.id.dugme);
        dugme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextGameStep();
            }
        });
        initGame();
        nextGameStep();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_kalkulator, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean initGame() {
        operacije[0] = "Sabiranje";
        operacije[1] = "Oduzimanje";
        r = new Random();
        int current_operation_index = r.nextInt(2);
        current_operation_title = operacije[current_operation_index];
        game_score = 0;
        num_lives = 5;
        answer_checked = true;
        game_end = false;
        game_score_textview = (TextView) findViewById(R.id.game_score);
        lives_textview = (TextView) findViewById(R.id.lives);
        dugme = (Button) findViewById(R.id.dugme);
        dugme.setTextColor(Color.WHITE);
        return true;
    }

    public boolean nextGameStep() {
        updateTopBar();
        if (!game_end) {
            if (num_lives > 0) {
                if (answer_checked) {
                    setQuestion();
                } else {
                    checkAnswer();
                }
            } else {
                game_end = true;
                dugme.setBackgroundColor(Color.BLACK);
                dugme.setText("Крај игре!");
            }
        } else {
            if (playAgain()) {
                initGame();
            }
        }
        return true;
    }

    public boolean playAgain() {
        dugme.setBackgroundColor(Color.DKGRAY);
        dugme.setText("Играмо опет?");
        return true;
    }

    public boolean setQuestion() {
        // TextView operacija_label = (TextView) findViewById(R.id.operacija_label);
        // operacija_label.setText(current_operation_title);
        TextView operator_label_textview = (TextView) findViewById(R.id.operator_label);
        switch (current_operation_title) {
            case "Sabiranje":
                operator_label_textview.setText("+");
                operand_1 = r.nextInt(operand_1_max_value_sabiranje - 1) + 1;
                operand_2 = r.nextInt(operand_2_max_value_sabiranje - 1) + 1;
                break;
            case "Oduzimanje":
                operator_label_textview.setText("-");
                operand_1 = r.nextInt(operand_1_max_value_oduzimanje - 1) + 1;
                operand_2 = r.nextInt(operand_2_max_value_oduzimanje - 1) + 1;
                if (operand_2 > operand_1)
                {
                    int operand_temp = operand_1;
                    operand_1 = operand_2;
                    operand_2 = operand_temp;
                }
                break;
        }

        TextView operand_1_textview = (TextView) findViewById(R.id.operand_1);
        operand_1_textview.setText(String.valueOf(operand_1));
        TextView operand_2_textview = (TextView) findViewById(R.id.operand_2);
        operand_2_textview.setText(String.valueOf(operand_2));
        EditText rezultat_edittext = (EditText) findViewById(R.id.rezultat);
        rezultat_edittext.setText("");
        // rezultat_edittext.setFocusableInTouchMode(true);
        // rezultat_edittext.setFocusable(true);
        // rezultat_edittext.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        imm.showSoftInput(rezultat_edittext, InputMethodManager.SHOW_IMPLICIT);

        dugme.setBackgroundColor(Color.DKGRAY);
        dugme.setText("Провери");
        answer_checked = false;
        return true;
    }

    public boolean updateTopBar() {
        game_score_textview.setText(String.valueOf(game_score));
        lives_textview.setText("");
        lives_textview.setTextColor(Color.RED);
        for (int lives_index = 1; lives_index <= num_lives; lives_index++) {
            lives_textview.append(life_sign);
        }
        return true;
    }

    public boolean checkAnswer() {
        EditText rezultat_edittext = (EditText) findViewById(R.id.rezultat);
        String rezultat_text = rezultat_edittext.getText().toString();
        rezultat = !rezultat_text.matches("") ? Integer.parseInt(rezultat_text) : 0;
        sound_succ = MediaPlayer.create(this, R.raw.tada);
        sound_fail = MediaPlayer.create(this, R.raw.fail);
        boolean answer_correct = false;
        switch (current_operation_title) {
            case "Sabiranje":
                answer_correct = (rezultat == operand_1 + operand_2);
                break;
            case "Oduzimanje":
                answer_correct = (rezultat == operand_1 - operand_2);
                break;
        }
        if (answer_correct)
        {
            dugme.setBackgroundColor(Color.GREEN);
            dugme.setText("Тачно!");
            game_score++;
            updateTopBar();
            sound_succ.start();
        }
        else
        {
            dugme.setBackgroundColor(Color.RED);
            dugme.setText("Нетачно!");
            num_lives--;
            updateTopBar();
            sound_fail.start();
        }
        answer_checked = true;
        return true;
    }
}
