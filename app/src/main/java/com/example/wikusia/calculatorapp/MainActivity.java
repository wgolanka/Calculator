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
//    private Double firstVal;
    private String pendingOperation = "=";
    boolean isFirstValReady = false;
    HashMap<String, Button> buttonsWithNumber = new HashMap<String, Button>();
    HashMap<String, Button> buttonsWithOperation = new HashMap<String, Button>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)
        {
//            firstVal = savedInstanceState.getDouble("FIRST_VALUE");
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

        setButtonsWithNumber();
        setButtonsWithOperations();

        final View.OnClickListener onClickNumberListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Button b = (Button) view;
                String buttonText = b.getText().toString();

                if (!isFirstValReady)
                {
                    setResultForFirstValue(buttonText);
                    return;
                }
                newNumber.append(buttonText);
            }
        };
        setButtonOnClickNumbers(onClickNumberListener);

        View.OnClickListener onClickOperationListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                isFirstValReady = true;
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

    private void setResultForFirstValue(String buttonText)
    {
        if (operand != null)
        {
            String newVal = String.valueOf( operand.intValue()) +
                    String.valueOf(Integer.valueOf(buttonText));
            operand = Double.valueOf(newVal);
            result.setText(String.valueOf(operand));
        }
        else
        {
            operand = Double.valueOf(buttonText);
            if (result.getText().toString().equals("-"))
            {
                setNumberOppositeValue();
            }
            result.setText(String.valueOf(operand));
        }
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
            return;
        }
        if (pendingOperation == null)
        {
            return;
        }
        else if (pendingOperation.equals("="))
        {
            pendingOperation = operation;
        }

        switch (pendingOperation)
        {
            case "=":
                operand = value;
                break;
            case "/":
                operand = (value == 0 ?
                        0.0 : (operand /= value));
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
        result.setText(String.valueOf(operand));
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
    public void onRestoreInstanceState(Bundle savedInstanceState)
    {

        operand = savedInstanceState.getDouble("OPERAND");
        pendingOperation = savedInstanceState.getString("OPERATION");
        displayOperation.setText(savedInstanceState.getString("OPERATION"));
        result.setText(savedInstanceState.getString("DISPLAY"));
        Log.d("onRestoreInst", result.toString());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        if (operand != null)
        {
            outState.putDouble("OPERAND", operand);
        }
        outState.putString("OPERATION", pendingOperation);
        outState.putString("DISPLAY",result.getText().toString());
        super.onSaveInstanceState(outState);
    }

    public void onClickOppositeButton(View v)
    {
       setNumberOppositeValue();
    }

    private void setNumberOppositeValue()
    {
        if(operand != null)
        {
            Log.d("setNumberOppos oper", String.valueOf(operand));
            operand *= -1;
            result.setText(String.valueOf(operand));
        }
        else
        {
            setResultTextOppositeSymbol();
        }
    }

    private void setResultTextOppositeSymbol()
    {
        if ((result.getText().toString().equals("-")))
        {
            result.setText("");
        }
        else
        {
            result.setText("-");
        }
    }
}
