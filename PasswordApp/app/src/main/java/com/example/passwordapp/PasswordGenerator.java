package com.example.passwordapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PasswordGenerator {

    private boolean isCheckBoxLower;
    private boolean isCheckBoxUpper;
    private boolean isCheckBoxNumbers;
    private boolean isCheckBoxSymbols;

    private int length;

    private String pass;

    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER = LOWER.toUpperCase();
    private static final String DIGITS = "0123456789";
    private static final String PUNCTUATION = "!@#$%&*()_+-=[]|,./?><";

    public PasswordGenerator(boolean isCheckBoxLower, boolean isCheckBoxUpper, boolean isCheckBoxNumbers, boolean isCheckBoxSymbols, int length){
        this.isCheckBoxLower = isCheckBoxLower;
        this.isCheckBoxUpper = isCheckBoxUpper;
        this.isCheckBoxNumbers = isCheckBoxNumbers;
        this.isCheckBoxSymbols = isCheckBoxSymbols;

        this.length = length;

        this.pass = generate(length);
    }

    private String generate(int length){

        // Variables.
        StringBuilder password = new StringBuilder(length);
        Random random = new Random(System.nanoTime());

        // Collect the categories to use.
        List<String> charCategories = new ArrayList<>(4);
        if (isCheckBoxLower) {
            charCategories.add(LOWER);
        }
        if (isCheckBoxUpper) {
            charCategories.add(UPPER);
        }
        if (isCheckBoxNumbers) {
            charCategories.add(DIGITS);
        }
        if (isCheckBoxSymbols) {
            charCategories.add(PUNCTUATION);
        }

        // Build the password.
        for (int i = 0; i < length; i++) {
            String charCategory = charCategories.get(random.nextInt(charCategories.size()));
            int position = random.nextInt(charCategory.length());
            password.append(charCategory.charAt(position));
        }

        return new String(password);
    }

    public String getPassword(){
        return this.pass;
    }
}
