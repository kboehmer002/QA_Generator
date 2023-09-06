package src.edu.odu.cs411;

import java.io.PrintStream;
import java.util.Vector;
import java.util.stream.Stream;

public class Question {
    Vector<String> choicesVector;
    String ques;
    String catagory;
    String src;

    /**
     * Initializes the choicesVector as an empty vector
     */
    public Question() {
        this.choicesVector = new Vector<>();
    }

    /**
     * Sets the category to the given string
     * 
     * @param n - String category designation
     */
    public void setCatagory(String n) {
        if (n.contains("1")) {
            this.catagory = "Multiple Choice (Easy)";
        } else if (n.contains("2")) {
            this.catagory = "Multiple Choice (Medium)";
        } else if (n.contains("3")) {
            this.catagory = "Multiple Choice (Hard)";
        } else if (n.contains("4")) {
            this.catagory = "True/False";
        } else if (n.contains("5")) {
            this.catagory = "Descriptive";
        } else if (n.contains("6")) {
            this.catagory = "Fill in the Blank";
        } else {
            this.catagory = "Unidentifiable";
        }
    }

    /**
     * Retrieves the current catagory
     * 
     * @return
     */
    public String getCatagory() {
        return this.catagory;
    }

    /**
     * Stores the "help_text"
     * 
     * @param str - String of source text
     */
    public void setSrc(String str) {
        this.src = str;
    }

    /**
     * Retrives the source text
     * 
     * @return
     */
    public String getSrc() {
        return this.src;
    }

    /**
     * Sets ONLY the actual Question
     * 
     * @param q - String of question text
     */
    public void setQuestion(String q) {
        this.ques = q;
    }

    /**
     * Retrives question text
     * 
     * @return
     */
    public String getQuestion() {
        return this.ques;
    }

    /**
     * Adds a choice option to the choice vector
     * 
     * @param newChoice - String representing the choice to be added
     */
    public void addChoice(String newChoice) {
        this.choicesVector.add(newChoice);
    }

    /**
     * Replaces all prexisting choices with the given ones all at once
     * 
     * @param choices - vector of strings representing each of the possible answer
     *                choices that are to be saved
     */
    public void setAllChoices(Vector<String> choices) {
        this.choicesVector = choices;
    }

    /**
     * Retrieves entire choice vector of strings for the question
     * 
     * @return
     */
    public Vector<String> getAllChoices() {
        return this.choicesVector;
    }

    /**
     * Prints stored question data
     */
    public void displayQuestion() {
        System.out.println("\tQuestion: \t" + this.ques);
        System.out.println("\tCatagory: \t" + this.catagory);
        System.out.println("\tAnswers: ");
        for (int i = 0; i < this.choicesVector.size(); i++) {
            if (i == 0) {
                System.out.println("\t\t\t" + this.choicesVector.get(i));
            } else {
                System.out.println(this.choicesVector.get(i));
            }
        }
        System.out.println("\tSource: " + this.src);
    }
}
