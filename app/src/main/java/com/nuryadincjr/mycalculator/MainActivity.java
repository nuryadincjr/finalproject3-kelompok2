package com.nuryadincjr.mycalculator;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.view.View.SYSTEM_UI_FLAG_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_VISIBLE;
import static com.nuryadincjr.mycalculator.R.id.btnAddition;
import static com.nuryadincjr.mycalculator.R.id.btnAnswer;
import static com.nuryadincjr.mycalculator.R.id.btnDelete;
import static com.nuryadincjr.mycalculator.R.id.btnDivision;
import static com.nuryadincjr.mycalculator.R.id.btnDot;
import static com.nuryadincjr.mycalculator.R.id.btnMultiplication;
import static com.nuryadincjr.mycalculator.R.id.btnPercentage;
import static com.nuryadincjr.mycalculator.R.id.btnSubtraction;
import static com.nuryadincjr.mycalculator.databinding.ActivityMainBinding.*;
import static com.nuryadincjr.mycalculator.pojo.AlgorithmArithmetic.getExpressions;
import static com.nuryadincjr.mycalculator.pojo.Constants.DECIMAL_PATTERN;
import static com.nuryadincjr.mycalculator.pojo.Constants.FRACTION_DIGITS;
import static com.nuryadincjr.mycalculator.pojo.Constants.KEY_DISPLAY_NUMBER;
import static com.nuryadincjr.mycalculator.pojo.Constants.KEY_INPUT_LIST;
import static com.nuryadincjr.mycalculator.pojo.Constants.REGEX_DECIMAL_PATTERN;
import static java.lang.Double.parseDouble;
import static java.lang.String.valueOf;
import static java.text.DecimalFormatSymbols.getInstance;
import static java.util.Objects.requireNonNull;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Configuration newConfig = getResources().getConfiguration();
        getOrientationDevice(newConfig);

        inputList = new ArrayList<>();
        decimalFormat = new DecimalFormat(DECIMAL_PATTERN, getInstance(Locale.US));
        decimalFormat.setMaximumFractionDigits(FRACTION_DIGITS);

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
        onOperatorClickListener(binding.btnPercentage);

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
            String displayNumber = String.valueOf(binding.tvResult.getText());
            switch (button.getId()) {
                case btnAddition:
                    operator = "+";
                    break;
                case btnSubtraction:
                    operator = "-";
                    if(displayNumber.isEmpty()){
                        displayNumber = operator;
                        binding.tvResult.setText(displayNumber);
                    }
                    break;
                case btnMultiplication:
                    operator = "*";
                    break;
                case btnDivision:
                    operator = "/";
                    break;
                case btnPercentage:
                    operator = "%";
                    break;
            }

            if(!displayNumber.equals("-") && displayNumber.length() >= 2) {
                inputList.add(decimalFormat.format(parseDouble(displayNumber)));
            }

            if(!displayNumber.equals("-")){
                if(!binding.tvResult.getText().toString().isEmpty()){
                    inputList.add(decimalFormat.format(parseDouble(displayNumber)));
                    binding.tvResult.setText("");
                }

                if(inputList.size() != 0){
                    final int position = inputList.size() - 1;
                    String lastInputOfList = inputList.get(position);
                    boolean isNumber = lastInputOfList.matches(REGEX_DECIMAL_PATTERN);

                    if(isNumber || lastInputOfList.equals("%")) {
                        inputList.add(operator);
                    } else inputList.set(position, operator);
                }
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    private void onActionClickListener(Button button) {
        button.setOnClickListener(v -> {

            if(button.getId() == btnDelete) getActionDelete();

            switch (button.getId()) {
                case btnAnswer:
                    getActionAnswer();
                    break;
                case btnDot:
                    getActionDot();
                    break;
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    private void getActionAnswer() {
        String displayNumber = valueOf(binding.tvResult.getText());

        if(inputList.size() !=0 && displayNumber.isEmpty() &&
                inputList.get(inputList.size()-1).equals("%")){
            onGetResult(decimalFormat.format(1));
        } else if(!displayNumber.isEmpty()){
            onGetResult(decimalFormat.format(parseDouble(displayNumber)));
        }
    }

    private void onGetResult(String format) {
        inputList.add(format);
        inputList.add("=");

        String result = decimalFormat.format(getExpressions(inputList));
        binding.tvResult.setText(result);
        inputList.clear();
    }

    private void getActionDelete() {
        binding.tvResult.setText("");
        inputList.clear();
    }

    @SuppressLint("SetTextI18n")
    private void getActionDot() {
        String displayNumber = valueOf(binding.tvResult.getText());
        if(displayNumber.isEmpty()){
            binding.tvResult.setText("0.");
        }else if(!displayNumber.contains(".")){
            binding.tvResult.setText(displayNumber + ".");
        }
    }
}