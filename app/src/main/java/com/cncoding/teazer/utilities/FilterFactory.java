package com.cncoding.teazer.utilities;

import android.text.InputFilter;
import android.text.Spanned;

/**
 *
 * Created by Prem $ on 12/3/2017.
 */

public class FilterFactory {

    private static final String validUsername = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890@._";
    private static final String validName = "ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijklmnopqrstuvwxyz";
    private static final String validEmail = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@._";
    private static final String validPhoneNumber = "1234567890";
    private static final String validPassword = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890~`!@#$%^*()_-+={}|[]:\";'/?&\\";

    public static InputFilter usernameFilter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dStart, int dEnd) {

            if (source != null && !validUsername.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    public static InputFilter nameFilter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dStart, int dEnd) {

            try {
                if (end > 0 && source != null && !validName.contains(String.valueOf(source.charAt(end - 1)))) {
                    return "";
                } else return null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    };

    public static InputFilter emailFilter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dStart, int dEnd) {

            if (source != null && !validEmail.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    public static InputFilter numberFilter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dStart, int dEnd) {

            if (source != null && !validPhoneNumber.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    public static InputFilter passwordFilter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dStart, int dEnd) {

            if (source != null && !validPassword.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };
}
