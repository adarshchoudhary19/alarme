package com.inheritance.coc.alarmwithpuzzle;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Aliabbas on 31-Dec-17.
 */
public class Maths_Layout extends ConstraintLayout {
    private OnChangeListener mListener = null;
    TextView question_TV;
    EditText answer_ET;
    Button submit_button, reset_button;
    int answer;
    int difficulty = 5;
    private static final String TAG = "Maths_Layout";
    private Context context;

    public Maths_Layout(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public Maths_Layout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void set_difficulty(int difficulty) {
        Log.e(TAG, "set_difficulty: " + (difficulty+1));
        this.difficulty = difficulty;
//        String maths_params[] = randomEquationGenerator.generate(difficulty);
//        String question = maths_params[0];
//        answer = Integer.parseInt(maths_params[1]);
//        question_TV.setText(question);
        gen();
    }

    private void gen() {
        question_TV = (TextView) findViewById(R.id.question_TV);
        answer_ET = (EditText) findViewById(R.id.answer_ET);
        submit_button = (Button) findViewById(R.id.submit_buttton);
        reset_button = (Button) findViewById(R.id.reset_button);

        Log.e(TAG, "set: diff="+(difficulty+1));
        String maths_params[] = randomEquationGenerator.generate(difficulty);
        String question = maths_params[0];
        answer = Integer.parseInt(maths_params[1]);
        question_TV.setText(question);
    }
    private void init() {                       //like onCreate
        //setContentView --> inflater.inflate()
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.maths_layout, this);
        question_TV = (TextView) findViewById(R.id.question_TV);
        answer_ET = (EditText) findViewById(R.id.answer_ET);
        submit_button = (Button) findViewById(R.id.submit_buttton);
        reset_button = (Button) findViewById(R.id.reset_button);

        gen();

        OnClickListener listener = (v) -> {
            if (v.getId() == submit_button.getId()) {
                if (mListener != null) {
                    if (Integer.parseInt(answer_ET.getText().toString()) == answer) {
                        mListener.onChange(true);
                    } else {
                        mListener.onChange(false);
                    }
                }
            } else if (v.getId() == reset_button.getId()) {
                answer_ET.setText("");
            }
        };
        answer_ET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    if (mListener != null) {
                        if (Integer.parseInt(answer_ET.getText().toString()) == answer) {
                            mListener.onChange(true);
                        } else {
                            mListener.onChange(false);
                        }
                    }
                    handled = true;
                }
                return handled;
            }
        });
        submit_button.setOnClickListener(listener);
        reset_button.setOnClickListener(listener);
    }

    public void setOnChangeListener(OnChangeListener listener) {
        mListener = listener;
    }

    public interface OnChangeListener {
        void onChange(boolean correct_ans);
    }
}
