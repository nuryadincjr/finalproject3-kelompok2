package com.nuryadincjr.mycalculator;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.nuryadincjr.mycalculator.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private double inputFirst = 0.0;
    private double inputSecond = 0.0;
    boolean isAddition;
    boolean isSubtraction;
    boolean isMultiplication;
    boolean isDivision;
    boolean isModulation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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

    private void onNumberClickListener(Button button, int number) {
        button.setOnClickListener(v -> {
            String displayNumber = binding.tvResult.getText() + String.valueOf(number);
            binding.tvResult.setText(displayNumber);
        });
    }

    @SuppressLint("NonConstantResourceId")
    private void onOperatorClickListener(Button button) {
        button.setOnClickListener(v -> {
            inputFirst = Double.parseDouble(String.valueOf(binding.tvResult.getText()));

            switch (button.getId()) {
                case R.id.btnAddition:
                    getIsActionSelected(true, false,
                            false, false, false);
                    break;
                case R.id.btnSubtraction:
                    getIsActionSelected(false, true,
                            false, false, false);
                    break;
                case R.id.btnMultiplication:
                    getIsActionSelected(false, false,
                            true, false, false);
                    break;
                case R.id.btnDivision:
                    getIsActionSelected(false, false,
                            false, true, false);
                    break;
                case R.id.btnModulation:
                    getIsActionSelected(false, false,
                            false, false, true);
                    break;
            }

            binding.tvResult.setText("");

        });
    }

    @SuppressLint("NonConstantResourceId")
    private void onActionClickListener(Button button) {
        button.setOnClickListener(v -> {
            if(!binding.tvResult.getText().equals("")){
                switch (button.getId()) {
                    case R.id.btnAnswer:
                        getActionAnswer();
                        break;
                    case R.id.btnDelete:
                        getActionDelete();
                        break;
                    case R.id.btnDot:
                        getActionDot();
                        break;
                }
            }
        });
    }

    private void getIsActionSelected(boolean isAddition, boolean isSubtraction,
                                     boolean isMultiplication, boolean isDivision,
                                     boolean isModulation) {
        this.isAddition = isAddition;
        this.isSubtraction = isSubtraction;
        this.isMultiplication = isMultiplication;
        this.isDivision = isDivision;
        this.isModulation = isModulation;
    }

    private void getActionAnswer() {
        if(isAddition || isSubtraction || isMultiplication || isDivision || isModulation) {
            inputSecond = Double.parseDouble(String.valueOf(binding.tvResult.getText()));
        }

        if(isAddition) inputFirst += inputSecond;
        else if(isSubtraction)inputFirst -= inputSecond;
        else if(isMultiplication)inputFirst *= inputSecond;
        else if(isDivision) inputFirst /= inputSecond;
        else if(isModulation) inputFirst %= inputSecond;

        binding.tvResult.setText(String.valueOf(inputFirst));
        getIsActionSelected(false, false, false, false, false);
    }

    private void getActionDelete() {
        binding.tvResult.setText("");
        inputFirst = 0.0;
        inputSecond = 0.0;
    }

    @SuppressLint("SetTextI18n")
    private void getActionDot() {
        String displayNumber = String.valueOf(binding.tvResult.getText());
        if(!displayNumber.contains(getString(R.string.str_dot))){
            binding.tvResult.setText(displayNumber+getString(R.string.str_dot));
        }
    }
}