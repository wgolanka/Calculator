package com.example.wikusia.calculatorapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity
{
    public static String PACKAGE_NAME;
    private EditText result;
    private EditText newNumber;
    private TextView displayOperation;
    Context context;

    private Double operand = null;
    private String pendingOperation = "=";
    HashMap<String, Button> buttonsWithNumber = new HashMap<String, Button>();
    HashMap<String, Button> buttonsWithOperation = new HashMap<String, Button>();
    // TODO
    // create a button that will make input negative,
    // if there is no value put minus and next number make negative
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)
        {
            operand = savedInstanceState.getDouble("OPERAND");
            pendingOperation = savedInstanceState.getString("OPERATION");
        }
        setContentView(R.layout.activity_main);
        Log.d("CONTENT_VIEW", "is ready");

        PACKAGE_NAME = getApplicationContext().getPackageName();
        result = (EditText) findViewById(R.id.result);
        newNumber = (EditText) findViewById(R.id.newNumber);
        displayOperation = (TextView) findViewById(R.id.operation);
        context = getApplicationContext();
        Log.d("START", "wchodze");

        setButtonsWithNumber();
        setButtonsWithOperations();

        View.OnClickListener onClickNumberListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Button b = (Button) view;
                newNumber.append(b.getText().toString());
            }
        };
        setButtonOnClickNumbers(onClickNumberListener);

        View.OnClickListener onClickOperationListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Button button = (Button) view;
                String operation = button.getText().toString();
                String value = newNumber.getText().toString();
                if(value.length() != 0 )
                {
                    tryToPerformOperation(value, operation);
                }
                pendingOperation = operation;
                displayOperation.setText(pendingOperation);
            }
        };

        setButtonOnClickOperations(onClickOperationListener);
    }

    private void tryToPerformOperation(String value, String operation)
    {
        try
        {
            performOperation(Double.valueOf(value), operation);
        }catch (NumberFormatException e)
        {
            newNumber.setText("");
        }
    }

    private void performOperation(Double value, String operation)
    {
        if (operand == null) {
            operand = value;
            updateResultAndNewNumberTextField();
            return;
        }


        if (pendingOperation.equals("="))
        {
            pendingOperation = operation;
        }
        switch (pendingOperation)
        {
            case "=":
                operand = value;
                break;
            case "/":
                if (value == 0) {

                    operand = 0.0;
                }
                else
                {
                    operand /= value;
                }
                break;
            case "*":
                operand *= value;
                break;
            case "-":
                operand -= value;
                break;
            case "+":
                operand += value;
                break;
        }

        updateResultAndNewNumberTextField();
    }

    private void updateResultAndNewNumberTextField()
    {
        result.setText(operand.toString());
        newNumber.setText("");
    }

    public void setButtonsWithNumber()
    {
        String buttonNumber;
        int numberOfButtons = 11;
        for(int i = 0; i < numberOfButtons; i++)
        {
            buttonNumber = "button" + i;
            if(i == 10)
                buttonNumber = "buttonDot";

            int id = getResources().getIdentifier(buttonNumber, "id", PACKAGE_NAME);
            Button button = (Button) findViewById(id);
            Log.d("BUTTON_ID", buttonNumber);
            buttonsWithNumber.put(buttonNumber, button);
        }
    }

    private void setButtonsWithOperations()
    {
        buttonsWithOperation.put("buttonEquals", (Button) findViewById(R.id.buttonEquals));
        buttonsWithOperation.put("buttonDivide", (Button) findViewById(R.id.buttonDivide));
        buttonsWithOperation.put("buttonMultiply", (Button) findViewById(R.id.buttonMultiply));
        buttonsWithOperation.put("buttonMinus", (Button) findViewById(R.id.buttonMinus));
        buttonsWithOperation.put("buttonPlus", (Button) findViewById(R.id.buttonPlus));
//        buttonsWithOperation.put("buttonDot", (Button) findViewById(R.id.buttonDot));
    }

    public void setButtonOnClickNumbers(View.OnClickListener listener)
    {
        for(Button button : buttonsWithNumber.values())
        {
            button.setOnClickListener(listener);
        }
    }

    public void setButtonOnClickOperations(View.OnClickListener listener)
    {
        for(Button button : buttonsWithOperation.values())
        {
            button.setOnClickListener(listener);
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        operand = savedInstanceState.getDouble("OPERAND");
        displayOperation.setText(savedInstanceState.getString("OPERATION"));
        result.setText(savedInstanceState.getString("DISPLAY"));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        if (operand == null)
            return;

        outState.putDouble("OPERAND", operand);
        outState.putString("OPERATION", pendingOperation);
        outState.putString("DISPLAY",result.getText().toString());
        super.onSaveInstanceState(outState);
    }
}
