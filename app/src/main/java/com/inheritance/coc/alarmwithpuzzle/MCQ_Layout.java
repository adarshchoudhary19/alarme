package com.inheritance.coc.alarmwithpuzzle;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Aliabbas on 31-Dec-17.
 */
public class MCQ_Layout extends ConstraintLayout {
    private OnChangeListener mListener = null;
    private Context context;
    int correct_option_button_id = 0;
    int correct_option_no = 0;
    TextView question_tv;
    int finalCorrect_option_button_id;
    Button opt1_button, opt2_button, opt3_button, opt4_button;

    public MCQ_Layout(Context context) {
        super(context);
        this.context = context;
        init(context);
    }

    public MCQ_Layout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    void setQues(Context context) {
        AlarmDB alarmDB = new AlarmDB(context);
        String mcq_params[] = alarmDB.get_GK_ques();
        String question = mcq_params[0];
        String opt1 = mcq_params[1];
        String opt2 = mcq_params[2];
        String opt3 = mcq_params[3];
        String opt4 = mcq_params[4];
        correct_option_no = Integer.parseInt(mcq_params[5]);
        question_tv.setText(question);
        opt1_button.setText(opt1);
        opt2_button.setText(opt2);
        opt3_button.setText(opt3);
        opt4_button.setText(opt4);

        switch (correct_option_no) {
            case 1:
                correct_option_button_id = R.id.opt1;
                break;
            case 2:
                correct_option_button_id = R.id.opt2;
                break;
            case 3:
                correct_option_button_id = R.id.opt3;
                break;
            case 4:
                correct_option_button_id = R.id.opt4;
                break;
//            default:
//                init(context);  // Check- is this needed??
        }
        finalCorrect_option_button_id = correct_option_button_id;
        OnClickListener listener = (v) -> {
            if (v.getId() == finalCorrect_option_button_id) {
                if (mListener != null) {
                    mListener.onChange(true);
                }
            } else {
                if (mListener != null) {
                    setQues(context);
                    mListener.onChange(false);
                }
            }
        };

        opt1_button.setOnClickListener(listener);
        opt2_button.setOnClickListener(listener);
        opt3_button.setOnClickListener(listener);
        opt4_button.setOnClickListener(listener);
    }
    private void init(Context context) {                       //like onCreate
        //setContentView --> inflater.inflate()
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.mcq_layout, this);
        question_tv = (TextView) findViewById(R.id.question_tv);
        opt1_button = (Button) findViewById(R.id.opt1);
        opt2_button = (Button) findViewById(R.id.opt2);
        opt3_button = (Button) findViewById(R.id.opt3);
        opt4_button = (Button) findViewById(R.id.opt4);

        AlarmDB alarmDB = new AlarmDB(context);
        String mcq_params[] = alarmDB.get_GK_ques();
        String question = mcq_params[0];
        String opt1 = mcq_params[1];
        String opt2 = mcq_params[2];
        String opt3 = mcq_params[3];
        String opt4 = mcq_params[4];
        correct_option_no = Integer.parseInt(mcq_params[5]);
        question_tv.setText(question);
        opt1_button.setText(opt1);
        opt2_button.setText(opt2);
        opt3_button.setText(opt3);
        opt4_button.setText(opt4);

        switch (correct_option_no) {
            case 1:
                correct_option_button_id = R.id.opt1;
                break;
            case 2:
                correct_option_button_id = R.id.opt2;
                break;
            case 3:
                correct_option_button_id = R.id.opt3;
                break;
            case 4:
                correct_option_button_id = R.id.opt4;
                break;
//            default:
//                init(context);  // Check- is this needed??
        }
//        setQues();

        finalCorrect_option_button_id = correct_option_button_id;
        OnClickListener listener = (v) -> {
            if (v.getId() == finalCorrect_option_button_id) {
                if (mListener != null) {
                    mListener.onChange(true);
                }
            } else {
                if (mListener != null) {
                    setQues(context);
                    mListener.onChange(false);
                }
            }
        };

        opt1_button.setOnClickListener(listener);
        opt2_button.setOnClickListener(listener);
        opt3_button.setOnClickListener(listener);
        opt4_button.setOnClickListener(listener);
    }

    public void setOnChangeListener(OnChangeListener listener) {
        mListener = listener;
    }

    public interface OnChangeListener {
        public void onChange(boolean correct_ans);
    }
}

