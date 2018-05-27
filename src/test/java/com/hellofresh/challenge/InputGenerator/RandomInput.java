package com.hellofresh.challenge.InputGenerator;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Class that helps generate random input for user's
 * personal information
 */
public class RandomInput {
    final String strLexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    final String numLexicon = "12345674890";
    final java.util.Random rand = new java.util.Random();
    Set<String> random = new HashSet<String>();
    private String[] parts ;

    /**
     * Generates a 128-bit long UUID
     */
    public void generateInputText(){
        String random = UUID.randomUUID().toString();
        parts = random.split("-");
    }

    /**
     * Generates random alphabetic or numeric or alphanumeric
     * values
     * @param min defines minimum value of range
     * @param max defines maximu value of range
     * @param form defines the type of output needed i.e. alphabets only, numeric value only
     * @return returns the output in string format
     */
    public String generateAlphaNumInput(int min, int max, String form){
        String temp;
        StringBuilder builder = new StringBuilder();
        if(form=="numOnly") temp = numLexicon;
        else if(form == "charOnly") temp = strLexicon;
        else temp = strLexicon.concat(numLexicon);

        while(builder.toString().length() == 0) {
            int length = rand.nextInt((max - min) + 1) + min;
            for(int i = 0; i < length; i++) {
                builder.append(temp.charAt(rand.nextInt(temp.length())));
            }
            if(random.contains(builder.toString())) {
                builder = new StringBuilder();
            }
        }
        return builder.toString();
    }

    /**
     * Returns character only value for first name
     * @return first name as string
     */
    public String getRandomFirstName(){
        return generateAlphaNumInput(5, 9, "charOnly");
    }

    /**
     * Returns character only value for last name
     * @return last name as string
     */
    public String getRandomLastName(){
        return generateAlphaNumInput(5, 9, "charOnly");
    }

    /**
     * Returns a mix of alpha-numeric characters as passsword
     * @return password as string
     */
    public String getRandomPassword(){
        return generateAlphaNumInput(10, 12,"AlphaNum");
    }

    /**
     * Returns a validated email ID made up of alpha-numeric characters
     * @return email ID as string
     */
    public String getRandomEmailId(){
        generateInputText();
        String domain = generateAlphaNumInput(8, 12,"charOnly");
        return parts[4]+"@"+domain.substring(0,6)+"."+domain.substring(6);
    }

    /**
     * Returns an alpha-numeric value
     * @return string
     */
    public String getRandomName(){
        generateInputText();
        return parts[0];
    }

    /**
     * Returns a 5-digit postal code generated randomly
     * @return postal code as string
     */
    public String getPostalCode(){
        return generateAlphaNumInput(5, 5, "numOnly");
    }

    /**
     *  Randomly generated 10-digit phone number
     * @return string
     */
    public String getPhoneNumber(){
        return generateAlphaNumInput(10, 10, "numOnly");
    }
    public String getRandomAddress(){
        generateInputText();
        return String.join("", parts);
    }

    /**
     * Returns a no. for index, used by Select value
     * @param size total no. of options in select
     * @return random integer from range of no. of options
     */
    public int getRandomSelectValues(int size){
        return rand.nextInt((size-1)+1)+1;
    }
}
