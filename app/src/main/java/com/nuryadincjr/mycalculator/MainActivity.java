package com.nuryadincjr.mycalculator;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.view.View.SYSTEM_UI_FLAG_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_VISIBLE;
import static com.nuryadincjr.mycalculator.R.id.btnAddition;
import static com.nuryadincjr.mycalculator.R.id.btnAnswer;
import static com.nuryadincjr.mycalculator.R.id.btnDelete;
import static com.nuryadincjr.mycalculator.R.id.btnDivision;
import static com.nuryadincjr.mycalculator.R.id.btnDot;
import static com.nuryadincjr.mycalculator.R.id.btnModulation;
import static com.nuryadincjr.mycalculator.R.id.btnMultiplication;
import static com.nuryadincjr.mycalculator.R.id.btnSubtraction;
import static com.nuryadincjr.mycalculator.R.string.str_dot;
import static com.nuryadincjr.mycalculator.pojo.Constants.KEY_DISPLAY_NUMBER;
import static com.nuryadincjr.mycalculator.pojo.Constants.KEY_INPUT_LIST;
import static com.nuryadincjr.mycalculator.pojo.Constants.REGEX_DECIMAL_PATTERN;
import static com.nuryadincjr.mycalculator.pojo.Constants.REGEX_OPERATOR_PATTERN;
import static java.lang.Double.parseDouble;
import static java.lang.String.join;
import static java.lang.String.valueOf;
import static java.text.DecimalFormatSymbols.getInstance;
import static java.util.Objects.requireNonNull;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.nuryadincjr.mycalculator.databinding.ActivityMainBinding;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private List<String> inputList;
    private DecimalFormat decimalFormat;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Configuration newConfig = getResources().getConfiguration();
        getOrientationDevice(newConfig);

        inputList = new ArrayList<>();
        decimalFormat = new DecimalFormat("0.0",
                getInstance(Locale.US));
        decimalFormat.setMaximumFractionDigits(340);

        if(savedInstanceState != null) onStateData(savedInstanceState);

        onNumberClickListener(binding.btnOne, 1);
        onNumberClickListener(binding.btnTwo, 2);
        onNumberClickListener(binding.btnThere, 3);
        onNumberClickListener(binding.btnFour, 4);
        onNumberClickListener(binding.btnFive, 5);
        onNumberClickListener(binding.btnSix, 6);
        onNumberClickListener(binding.btnSeven, 7);
        onNumberClickListener(binding.btnEight, 8);
        onNumberClickListener(binding.btnNine, 9);
        onNumberClickListener(binding.btnZero, 0);

        onOperatorClickListener(binding.btnAddition);
        onOperatorClickListener(binding.btnSubtraction);
        onOperatorClickListener(binding.btnMultiplication);
        onOperatorClickListener(binding.btnDivision);
        onOperatorClickListener(binding.btnModulation);

        onActionClickListener(binding.btnAnswer);
        onActionClickListener(binding.btnDelete);
        onActionClickListener(binding.btnDot);
    }

    private void onStateData(Bundle savedInstanceState) {
        String displayNumber = savedInstanceState.getString(KEY_DISPLAY_NUMBER);
        inputList = savedInstanceState.getStringArrayList(KEY_INPUT_LIST);
        binding.tvResult.setText(displayNumber);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(KEY_DISPLAY_NUMBER, valueOf(binding.tvResult.getText()));
        outState.putStringArrayList(KEY_INPUT_LIST, (ArrayList<String>) inputList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getOrientationDevice(newConfig);
    }

    private void getOrientationDevice(@NonNull Configuration newConfig) {
        ActionBar actionBar = getSupportActionBar();
        View decorView = getWindow().getDecorView();

        if(newConfig.orientation == ORIENTATION_LANDSCAPE) {
            decorView.setSystemUiVisibility(SYSTEM_UI_FLAG_FULLSCREEN);
            requireNonNull(actionBar).hide();
        } else {
            decorView.setSystemUiVisibility(SYSTEM_UI_FLAG_VISIBLE);
            requireNonNull(actionBar).show();
        }
    }

    private void onNumberClickListener(Button button, int number) {
        button.setOnClickListener(v -> {
            String displayNumber = binding.tvResult.getText() + valueOf(number);
            binding.tvResult.setText(displayNumber);
        });
    }

    @SuppressLint("NonConstantResourceId")
    private void onOperatorClickListener(Button button) {
        button.setOnClickListener(v -> {
            String operator = "";
            switch (button.getId()) {
                case btnAddition:
                    operator = "+";
                    break;
                case btnSubtraction:
                    operator = "-";
                    break;
                case btnMultiplication:
                    operator = "*";
                    break;
                case btnDivision:
                    operator = "/";
                    break;
                case btnModulation:
                    operator = "%";
                    break;
            }

            if(!binding.tvResult.getText().toString().isEmpty()){
                String displayNumber = valueOf(binding.tvResult.getText());
                inputList.add(decimalFormat.format(parseDouble(displayNumber)));
                binding.tvResult.setText("");
            }

            if(inputList.size() != 0){
                final int position = inputList.size() - 1;
                String lastInputOfList = inputList.get(position);
                boolean isNumber = lastInputOfList.matches(REGEX_DECIMAL_PATTERN);

                if(isNumber) inputList.add(operator);
                else inputList.set(position, operator);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("NonConstantResourceId")
    private void onActionClickListener(Button button) {
        button.setOnClickListener(v -> {
            String displayNumber = valueOf(binding.tvResult.getText());
            if(button.getId() == btnDelete) getActionDelete();
            if(!displayNumber.isEmpty()){
                switch (button.getId()) {
                    case btnAnswer:
                        getActionAnswer();
                        break;
                    case btnDot:
                        getActionDot();
                        break;
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("NonConstantResourceId")
    private void getActionAnswer() {
        String displayNumber = valueOf(binding.tvResult.getText());
        inputList.add(decimalFormat.format(parseDouble(displayNumber)));

        String expressions = join("", inputList);
        String[] operators = expressions.split(REGEX_DECIMAL_PATTERN);
        String[] operands = expressions.split(REGEX_OPERATOR_PATTERN);

        double result = parseDouble(operands[0]);
        for(int i=1; i<operands.length; i++){
            switch (operators[i]) {
                case "+":
                    result += parseDouble(operands[i]);
                    break;
                case "-":
                    result -= parseDouble(operands[i]);
                    break;
                case "*":
                    result *= parseDouble(operands[i]);
                    break;
                case "/":
                    result /= parseDouble(operands[i]);
                    break;
                default:
                    result %= parseDouble(operands[i]);
                    break;
            }
        }

        binding.tvResult.setText(decimalFormat.format(result));
        inputList.clear();
    }

    private void getActionDelete() {
        binding.tvResult.setText("");
        inputList.clear();
    }

    @SuppressLint("SetTextI18n")
    private void getActionDot() {
        String displayNumber = valueOf(binding.tvResult.getText());
        if(!displayNumber.contains(getString(str_dot))){
            binding.tvResult.setText(displayNumber + getString(str_dot));
        }
    }
}