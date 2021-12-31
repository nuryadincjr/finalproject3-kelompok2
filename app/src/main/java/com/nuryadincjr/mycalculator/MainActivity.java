package com.nuryadincjr.mycalculator;

import static com.nuryadincjr.mycalculator.R.id.btnAddition;
import static com.nuryadincjr.mycalculator.R.id.btnAnswer;
import static com.nuryadincjr.mycalculator.R.id.btnDelete;
import static com.nuryadincjr.mycalculator.R.id.btnDivision;
import static com.nuryadincjr.mycalculator.R.id.btnDot;
import static com.nuryadincjr.mycalculator.R.id.btnModulation;
import static com.nuryadincjr.mycalculator.R.id.btnMultiplication;
import static com.nuryadincjr.mycalculator.R.id.btnSubtraction;
import static com.nuryadincjr.mycalculator.R.string.str_dot;
import static com.nuryadincjr.mycalculator.pojo.Constants.KEY_ACTION_ID;
import static com.nuryadincjr.mycalculator.pojo.Constants.KEY_DISPLAY_NUMBER;
import static com.nuryadincjr.mycalculator.pojo.Constants.KEY_INPUT_FIRST;
import static com.nuryadincjr.mycalculator.pojo.Constants.KEY_INPUT_SECOND;
import static java.lang.Double.parseDouble;
import static java.lang.String.valueOf;
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

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private double inputFirst = 0.0;
    private double inputSecond = 0.0;
    private int actionID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Configuration newConfig = getResources().getConfiguration();
        getOrientationDevice(newConfig);

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
        inputFirst = savedInstanceState.getDouble(KEY_INPUT_FIRST);
        inputSecond = savedInstanceState.getDouble(KEY_INPUT_SECOND);
        actionID = savedInstanceState.getInt(KEY_ACTION_ID);
        binding.tvResult.setText(displayNumber);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(KEY_DISPLAY_NUMBER, valueOf(binding.tvResult.getText()));
        outState.putDouble(KEY_INPUT_FIRST, inputFirst);
        outState.putDouble(KEY_INPUT_SECOND, inputSecond);
        outState.putInt(KEY_ACTION_ID, actionID);
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

        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
            requireNonNull(actionBar).hide();
        } else {
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            requireNonNull(actionBar).show();
        }
    }

    private void onNumberClickListener(Button button, int number) {
        button.setOnClickListener(v -> {
            String displayNumber = binding.tvResult.getText() + valueOf(number);
            binding.tvResult.setText(displayNumber);
        });
    }

    private void onOperatorClickListener(Button button) {
        button.setOnClickListener(v -> {
            String displayNumber = valueOf(binding.tvResult.getText());
            if(!displayNumber.isEmpty()){
                inputFirst = parseDouble(valueOf(binding.tvResult.getText()));
                binding.tvResult.setText(null);
            }
            actionID = button.getId();
        });
    }

    @SuppressLint("NonConstantResourceId")
    private void onActionClickListener(Button button) {
        button.setOnClickListener(v -> {
            String displayNumber = valueOf(binding.tvResult.getText());
            if(!displayNumber.isEmpty()){
                switch (button.getId()) {
                    case btnAnswer:
                        getActionAnswer();
                        break;
                    case btnDelete:
                        getActionDelete();
                        break;
                    case btnDot:
                        getActionDot();
                        break;
                }
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    private void getActionAnswer() {
        if(actionID != 0){
            String displayNumber = valueOf(binding.tvResult.getText());
            inputSecond = parseDouble(displayNumber);

            switch (actionID) {
                case btnAddition:
                    inputFirst += inputSecond;
                    break;
                case btnSubtraction:
                    inputFirst -= inputSecond;
                    break;
                case btnMultiplication:
                    inputFirst *= inputSecond;
                    break;
                case btnDivision:
                    inputFirst /= inputSecond;
                    break;
                case btnModulation:
                    inputFirst %= inputSecond;
                    break;
            }

            actionID = 0;
            binding.tvResult.setText(valueOf(inputFirst));
        }
    }

    private void getActionDelete() {
        binding.tvResult.setText(null);
        inputFirst = 0.0;
        inputSecond = 0.0;
    }

    @SuppressLint("SetTextI18n")
    private void getActionDot() {
        String displayNumber = valueOf(binding.tvResult.getText());
        if(!displayNumber.contains(getString(str_dot))){
            binding.tvResult.setText(displayNumber+getString(str_dot));
        }
    }
}