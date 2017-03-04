package edu.fandm.dstern.calculator;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getName();
    public int maxline;
    public boolean declegal;
    public boolean overwrite;
    private ArrayList<String> history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //on creation of app sets all inputs to 0 and legalities to true
        setContentView(R.layout.activity_main);
        TextView tv = (TextView)findViewById(R.id.main_tv_output);
        String number = "0";
        overwrite = true;
        declegal = true;
        history = new ArrayList<String>();
        //retrieve saved instances
        if(savedInstanceState != null){
            number = savedInstanceState.getString("number");
            declegal = savedInstanceState.getBoolean("dec");
            overwrite = savedInstanceState.getBoolean("ovrwrite");
            history = savedInstanceState.getStringArrayList("history");

        }

        maxline = 1;
        tv.setText(number);
        Log.d(TAG, "here");
        Log.d(TAG, Integer.toString(tv.getLineCount()));
        while(tv.getLineCount() > maxline)
        {
            float a;
            a = tv.getTextSize();
            Log.d(TAG, Float.toString(a));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,a-25);
            a = tv.getTextSize();
            Log.d(TAG, Float.toString(a));
            maxline+=1;
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        //saves certain instances to deal with change from landscape to portrait
        TextView tv = (TextView)findViewById(R.id.main_tv_output);
        String displaytext = tv.getText().toString();
        savedInstanceState.putString("number", displaytext);
        savedInstanceState.putBoolean("dec", declegal);
        savedInstanceState.putBoolean("ovrwrite", overwrite);
        savedInstanceState.putStringArrayList("history", history);

    }

    public void buttonPress(View v)
    {
        TextView tv = (TextView)findViewById(R.id.main_tv_output);
        String displaytext = tv.getText().toString();
        //overwrites last answer or default 0
        if ((overwrite)||displaytext.equals("0"))
        {
            displaytext = "";
            overwrite = false;
        }
        Button b = (Button) v;
        String tmp = b.getText().toString();
        //gets the buttons symbol
        //deals with decimal legality
        if (tmp.equals("."))
        {
            if (declegal == false)
            {
                tmp = "";
            }
            declegal = false;
        }
        displaytext += tmp;


        tv.setText(displaytext);
        Log.d(TAG, "here2");
        Log.d(TAG, Integer.toString(tv.getLineCount()));
        //makes the font smaller to deal with going of screen
        while(tv.getLineCount() > maxline)
        {
            float a;
            a = tv.getTextSize();
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,a-25);
            maxline+=1;
        }
    }

    public void clear(View v)
    {
        TextView tv = (TextView)findViewById(R.id.main_tv_output);
        //resets the input to 0 and to default textsize
        declegal = true;
        String displaytext = "0";
        tv.setTextSize(40);
        maxline=1;
        overwrite = true;

        tv.setText(displaytext);

    }
    public void maths(View v)
    {
        //deals with math functions button presses
        TextView tv = (TextView)findViewById(R.id.main_tv_output);
        String displaytext = tv.getText().toString();
        Button b = (Button) v;
        String tmp = b.getText().toString();
        displaytext+= tmp;
        tv.setText(displaytext);
        while(tv.getLineCount() > maxline)
        {
            float a;
            a = tv.getTextSize();
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,a-25);
            maxline+=1;
        }
        overwrite = false;
    }


    public int numfunctions(String equation)
    {
        //counts how long a string array needs to be also figures out if the user input is legal
        int count = 0;
        boolean legal = false;
        char[] chrarray = equation.toCharArray();

        for(int i = 0; i<equation.length();i++)
        {
            if (rank(Character.toString(chrarray[i]))>0)
            {
                if (!legal)
                {
                    return -1;
                }
                count +=2;
                legal = false;

            }
            else
            {
                legal = true;
            }

        }
        if (!legal)
        {
            return -1;
        }
        return count+1;

    }
    public int rank(String pot)
    {
        //sets the rank of each math symbol for conversion to postfix
        if (pot.equals("+")||pot.equals("-"))
        {
            return 1;
        }
        else if (pot.equals("*")||pot.equals("/"))
        {
            return 2;
        }
        if (pot.equals("^"))
        {
            return 3;
        }

        return 0;
    }
    public ArrayList<String> infixtoPostfix(ArrayList<String> equ)
    {
        //converts infix to postfix uses fisher-yates
        ArrayList<String> fin = new ArrayList<String>();
        Stack<String> stk1 = new Stack<String>();
        String holder = "none";

        for(int i = 0; i < equ.size();i++)
        {
            if(rank(equ.get(i)) == 0)
            {
                fin.add(equ.get(i));
                if(!holder.equals("none"))
                {
                    String tmp = holder;
                    holder = "none";
                    if (!stk1.isEmpty())
                    {
                        if (rank(tmp)>rank(stk1.peek()))
                        {
                            stk1.push(tmp);
                        }
                        else
                        {
                            fin.remove(fin.size()-1);
                            while(!stk1.isEmpty())
                            {
                                fin.add(stk1.pop());
                            }
                            stk1.push(tmp);
                            fin.add(equ.get(i));
                        }
                    }
                    else
                    {
                        stk1.push(tmp);
                    }


                }
            }
            else
            {
                holder = equ.get(i);
            }
        }
        while(!stk1.isEmpty())
            fin.add(stk1.pop());
        return fin;
    }

    public double result(ArrayList<String> post)
    {
        //converts postfix to the answer
        Stack<Double> stk = new Stack<Double>();
        for(int i =0; i<post.size(); i++)
        {
            if (rank(post.get(i)) ==0)
            {
                stk.push(Double.parseDouble(post.get(i)));
            }
            else
            {
                double tmp1 = stk.pop();
                double tmp2 = stk.pop();

                if (post.get(i).equals("+"))
                {
                    stk.push(tmp1+tmp2);
                }
                else if(post.get(i).equals("-"))
                {
                    stk.push(tmp2-tmp1);
                }
                else if(post.get(i).equals("*"))
                {
                    stk.push(tmp1*tmp2);
                }
                else if(post.get(i).equals("/"))
                {
                    stk.push(tmp2/tmp1);
                }
                else if(post.get(i).equals("^"))
                {
                    stk.push(Math.pow(tmp2, tmp1));
                }
            }
        }
        return stk.pop();
    }

    public double calculate(String[] orig)
    {

        //converts string arryas to arrayList of Strings to make it easier to work with then
        //converts it to postfix then uses that to get the answer
        ArrayList<String> infix = new ArrayList<String>();

        for(int i=0; i<orig.length;i++)
        {
            infix.add(orig[i]);


        }

        ArrayList<String> postfix = infixtoPostfix(infix);

        return(result(postfix));

    }

    public void enter(View v)
    {
        String operation = "";

        TextView tv = (TextView)findViewById(R.id.main_tv_output);
        String displaytext = tv.getText().toString();
        String displaytext2 = tv.getText().toString();
        // to deal with the first number being negative
        displaytext = "0"+displaytext;
        //functions
        Log.d(TAG, displaytext);
        int size = 0;
        if (displaytext.contains("illegal operation"))
        {
            size = -1;
        }
        else
        {
            size = numfunctions(displaytext);
        }
        if (size == -1)
        {
            displaytext = "illegal operation";
            overwrite = true;
        }
        else if(displaytext.contains("Infinity")){
            operation = displaytext2;
            displaytext = "Infinity";
            overwrite = true;
            operation = operation + "=" + "Infinity";

        }
        else if(displaytext.contains("NaN")){
            operation = displaytext2;

            displaytext = "NaN";
            overwrite = true;
            operation = operation + "=" + "NaN";


        }
        else
        {
            //for history
            operation = displaytext2;
            //can calculate
            String[] infix = new String[size];
            String tmp ="";
            char[] chrarray = displaytext.toCharArray();
            int incount =0;
            //converts the string to a string[] separated by math functions
            for(int i =0;i<displaytext.length();i++)
            {
                if (rank(Character.toString(chrarray[i]))==0)
                {
                    tmp += Character.toString(chrarray[i]);
                }
                else
                {
                    infix[incount] = tmp;

                    incount += 1;
                    infix[incount] = Character.toString(chrarray[i]);
                    Log.d(TAG, infix[incount]);

                    incount += 1;
                    tmp = "";
                }
            }
            infix[incount] = tmp;
            double ans = calculate(infix);
            displaytext = Double.toString(ans);
            overwrite = true;
            operation = operation + "=" + Double.toString(ans);
        }
        Log.d(TAG, Integer.toString(size));


        maxline = 1;
        declegal =true;
        tv.setText(displaytext);
        while(tv.getLineCount() > maxline)
        {
            float a;
            a = tv.getTextSize();
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,a-25);
            maxline+=1;
        }
        // Add this operation to history
        if (!operation.equals("")) {
            history.add(0, operation);
        }

    }

    public void history(View view) {
        //start history and sends it the history
        Intent i = new Intent(this, HistoryActivity.class);
        i.putExtra("history", history);
        startActivity(i);
    }


}

