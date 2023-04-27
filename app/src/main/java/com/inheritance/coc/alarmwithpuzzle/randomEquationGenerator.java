package com.inheritance.coc.alarmwithpuzzle;

import java.util.Random;

public class randomEquationGenerator {
    private static float solve(float no1, char sym, float no2) {
        if (sym == '+') {
            return no1 + no2;
        } else if (sym == '-') {
            return no1 - no2;
        } else if (sym == '*') {
            return no1 * no2;
        } else if (sym == '/') {
            return no1 / no2;
        } else {
            return 0;
        }
    }

    private static int[] generate_options(int ans) {
        int answer = ans;
        int options[] = {answer, answer, answer, answer, 0};  // 4 options + correct_option_num
        int num_digits = 1;
        while (ans / 10 != 0) {
            num_digits++;
            ans = ans / 10;
        }
        Random rand = new Random();
        int factor = (int) Math.pow(10, num_digits - 1);
        int corr_option = rand.nextInt(4);  // 0-3
        options[corr_option] = answer;
        options[4] = corr_option;
        for (int i = 0; i < 4; i++) {
            if (i != corr_option) {
                while (options[i] == answer)  // there may be duplicate wrong answers
                {
                    options[i] = rand.nextInt(9 * factor) + factor;
                }
            }
        }
        return options;  // 4 options + correct_option_num
    }

    static String[] generate_with_opts(int difficulty) {
        /*
        Left to Right
        Approximations
        Only integers
        DIFFICULTY Levels:
        1 - 2 single digit +-
        2 - 3 single digit +-
        3 - 1 single 1 double digit +-
        4 - 2 double digit +-
        5 - 2 single digit /*
        6 - 3 single digit /*
        7 - 1 single 1 double digit /*
        // 8 - 2 double digit /*
        */
        int num1, num2, num3 = 0;
        char sym1, sym2 = 0;
        String question;
        int options[] = new int[5];  // 4 options + correct_option_num
        String opts[] = new String[5];  // as string, for returning
        int answer;
        char symbols[] = {'+', '-', '*', '/'};
        Random rand = new Random();
        if (difficulty == 1 || difficulty == 2 || difficulty == 3 || difficulty == 5 || difficulty == 6 || difficulty == 7) {
            num1 = rand.nextInt(9) + 1;  // 1 to 9
        } else  // 4, 8
        {
            num1 = rand.nextInt(90) + 10;  // 10 to 99
        }
        if (difficulty == 1 || difficulty == 2 || difficulty == 5 || difficulty == 6) {
            num2 = rand.nextInt(9) + 1;  // 1 to 9
        } else  // 3, 4, 7, 8
        {
            num2 = rand.nextInt(90) + 10;  // 10 to 99
        }
        if (difficulty == 2 || difficulty == 6) {
            num3 = rand.nextInt(9) + 1;  // 10 to 99
        }
        if (difficulty == 1 || difficulty == 2 || difficulty == 3 || difficulty == 4) {
            sym1 = symbols[rand.nextInt(2)];
        } else {
            sym1 = symbols[rand.nextInt(2) + 2];
        }
        if (difficulty == 2) {
            sym2 = symbols[rand.nextInt(2)];
        } else if (difficulty == 6) {
            sym2 = symbols[rand.nextInt(2) + 2];
        }
        if (difficulty != 2 && difficulty != 6) {
            question = num1 + " " + sym1 + " " + num2 + " = ?";
            answer = (int) solve(num1, sym1, num2);
        } else {
            question = num1 + " " + sym1 + " " + num2 + " " + sym2 + " " + num3 + " = ?";
            float ans = solve(num1, sym1, num2);
            answer = (int) solve(ans, sym2, num3);
        }
        options = generate_options(answer);
        for (int i = 0; i < options.length; i++) {
            opts[i] = String.valueOf(options[i]);
        }
        return new String[]{question, opts[0], opts[1], opts[2], opts[3], opts[4]};
    }

    static String[] generate(int difficulty) {
        difficulty++; //coz while storing, difficulty is starting from 0, not 1
        /*
        Left to Right
        Approximations
        Only integers
        DIFFICULTY Levels:
        1 - 2 single digit +-
        2 - 3 single digit +-
        3 - 1 single 1 double digit +-
        4 - 2 double digit +-
        5 - 2 single digit /*
        6 - 3 single digit /*
        7 - 1 single 1 double digit /*
        8 - 2 double digit /*
        */
        int num1, num2, num3 = 0;
        char sym1, sym2 = 0;
        String question;
        int options[] = new int[5];  // 4 options + correct_option_num
        String opts[] = new String[5];  // as string, for returning
        int answer;
        char symbols[] = {'+', '-', '*', '/'};
        Random rand = new Random();
        difficulty = (difficulty > 7) ? 7 : difficulty;
        if (difficulty == 1 || difficulty == 2 || difficulty == 3 || difficulty == 5 || difficulty == 6 || difficulty == 7) {
            num1 = rand.nextInt(9) + 1;  // 1 to 9
        } else  // 4, 8
        {
            num1 = rand.nextInt(90) + 10;  // 10 to 99
        }
        if (difficulty == 1 || difficulty == 2 || difficulty == 5 || difficulty == 6) {
            num2 = rand.nextInt(9) + 1;  // 1 to 9
        } else  // 3, 4, 7, 8
        {
            num2 = rand.nextInt(90) + 10;  // 10 to 99
        }
        if (difficulty == 2 || difficulty == 6) {
            num3 = rand.nextInt(9) + 1;  // 10 to 99
        }
        if (difficulty == 1 || difficulty == 2 || difficulty == 3 || difficulty == 4) {
            sym1 = symbols[rand.nextInt(2)];
        } else {
            sym1 = symbols[rand.nextInt(2) + 2];
        }
        if (difficulty == 2) {
            sym2 = symbols[rand.nextInt(2)];
        } else if (difficulty == 6) {
            sym2 = symbols[rand.nextInt(2) + 2];
        }
        if (difficulty != 2 && difficulty != 6) {
            question = num1 + " " + sym1 + " " + num2 + " = ?";
            answer = Math.round(solve(num1, sym1, num2));
        } else {
            question = num1 + " " + sym1 + " " + num2 + " " + sym2 + " " + num3 + " = ?";
            float ans = solve(num1, sym1, num2);
            answer = (int) solve(ans, sym2, num3);
        }
        return new String[]{question, String.valueOf(answer)};
    }
}
