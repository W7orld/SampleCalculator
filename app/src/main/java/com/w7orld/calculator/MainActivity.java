package com.w7orld.calculator;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import java.text.NumberFormat;
import java.util.Locale;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class MainActivity extends AppCompatActivity {

    //UI
    private AppCompatTextView inputBox;

    private final NumberFormat numberFormat = NumberFormat.getInstance(new Locale("en", "US"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputBox = findViewById(R.id.input_box);

        //On click numbers
        findViewById(R.id.btn0).setOnClickListener(v -> onClickNumber(0));
        findViewById(R.id.btn1).setOnClickListener(v -> onClickNumber(1));
        findViewById(R.id.btn2).setOnClickListener(v -> onClickNumber(2));
        findViewById(R.id.btn3).setOnClickListener(v -> onClickNumber(3));
        findViewById(R.id.btn4).setOnClickListener(v -> onClickNumber(4));
        findViewById(R.id.btn5).setOnClickListener(v -> onClickNumber(5));
        findViewById(R.id.btn6).setOnClickListener(v -> onClickNumber(6));
        findViewById(R.id.btn7).setOnClickListener(v -> onClickNumber(7));
        findViewById(R.id.btn8).setOnClickListener(v -> onClickNumber(8));
        findViewById(R.id.btn9).setOnClickListener(v -> onClickNumber(9));

        //On click operands buttons
        findViewById(R.id.btn_plus).setOnClickListener(v -> onClickOperator('+'));
        findViewById(R.id.btn_minus).setOnClickListener(v -> onClickOperator('-'));
        findViewById(R.id.btn_division).setOnClickListener(v -> onClickOperator('\u00F7'));
        findViewById(R.id.btn_multiplication).setOnClickListener(v -> onClickOperator('x'));

        //Delete buttons
        findViewById(R.id.btn_clear).setOnClickListener(v -> {
            //Clear input
            inputBox.setText("");
        });

        findViewById(R.id.btn_delete).setOnClickListener(v -> backspace());

        findViewById(R.id.btn_equal).setOnClickListener(view -> calculate());

    }

    private void backspace() {
        //Backspace
        String input = getInput();
        if (input.length() > 0) {
            input = input.substring(0, input.length() - 1);
            inputBox.setText(input);
        }

    }

    private String getInput() {
        return inputBox.getText().toString();
    }

    public void onClickNumber(int number) {
        inputBox.append(String.valueOf(number));
    }

    public void onClickOperator(char operator) {

        if (getInput().length() == 0) {
            return;
        }

        if (endsWithOperator()) {
            backspace();
        }

        inputBox.append(String.valueOf(operator));
    }

    private boolean endsWithOperator() {
        return getInput().endsWith("+") || getInput().endsWith("\u00F7") || getInput().endsWith("-") || getInput().endsWith("/") || getInput().endsWith("x");
    }

    private void calculate() {
        String input = getInput();

        if (endsWithOperator()) {
            //Remove last operator
            backspace();

            //get input value after remove operator
            input = getInput();
        }

        //Fix input
        input = input.replaceAll("x", "*")
                .replaceAll(",", "")
                .replaceAll("[^\\x00-\\x7F]", "/");

        ScriptEngine engine = new ScriptEngineManager().getEngineByName("rhino");

        try {

            Object result = engine.eval(input);

            inputBox.setText(numberFormat.format(result));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}