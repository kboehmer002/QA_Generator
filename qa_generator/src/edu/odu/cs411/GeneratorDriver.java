package src.edu.odu.cs411;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class GeneratorDriver {
    final PrintStream oldStdout = System.out;

    /**
     * Drives the Question and Answer Generator Program
     * 
     * @param args
     */
    public static void main(String[] args) {
        GeneratorDriver driver = new GeneratorDriver();
        // Prompts p = new Prompts();
        Store storage = new Store();

        if (args.length == 0)
            try {
                final PrintStream oldStdout = System.out;
                // 1. Welcome
                driver.startUp();
                // 2. Request Data
                storage.requestInputDoc();
                // 3. Specify Output Doc
                storage.outputDocName();
                // 4. Generate Questions
                String tempResponse = driver.generateQuestions(storage); /**
                                                                          * FOR FULL
                                                                          * // FUNCTIONAL USE
                                                                          */
                // 5. Store Questions
                storage.intakeQuestionsResponse(tempResponse); /** FOR FULL FUNCTIONAL USE */
                // storage.intakeQuestionsResponse(storage.getFullFileString()); /** FOR EXAMPLE
                // OUTPUT */
                // 6. Present Questions to User
                boolean quit = false;
                String choice = "\0";
                for (int k = 0; k < storage.getTotalQuestions(); k++) {
                    // 6.1 Extract current question and display data
                    Question currQuestion = storage.getAllQuestions().get(k);
                    currQuestion.displayQuestion();
                    // 7. Actions
                    // 7.1 Ask for action
                    System.out.println("Please choose an action to perform:\n"
                            + "\t1 : Output question to designated storage document,\n"
                            + "\t2 : Rephrase the question,\n" + "\t3 : Discard the question, or\n"
                            + "\tq : Quit the program.");
                    // 7.2 Take action input and perform action
                    BufferedReader br2 = new BufferedReader(new InputStreamReader(System.in));
                    choice = br2.readLine();
                    quit = driver.chooseAction(choice, quit, currQuestion,
                            storage.getOutFileStream());
                }

            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        else {
            System.out.print("FAILED TO START");
        }
    }

    /**
     * Gets topic from the user and sends out the HTTP Request to get a response of
     * a single string containing all generated questions and their components
     * 
     * @param storage - all stored information involving the question data
     * @return - response string from HTTP Request
     * @throws IOException
     * @throws InterruptedException
     */
    public String generateQuestions(Store storage) throws IOException, InterruptedException {
        Request r = new Request();
        // 4.1 Request Topic
        storage.requestTopic();
        // 4.2 Submit data string and topic string
        // 4.2.1 Convert to URI / 4.2.2 Submit HTTP Request
        // 4.2.3 Return Q&A Generated
        String tempResponse = storage.convertStrAndRequest();
        // System.out.println("Response: " + tempResponse);
        return tempResponse;
    }

    /**
     * Performs action choices for storage, rephrase, discard, and quit actions
     * 
     * @param choice   - string user input for the action to take
     * @param quit     - boolean defining if the program should quit after the
     *                 function has completed
     * @param question - current question
     * @param outFile  - .txt file for output
     * @return - quit to be updated
     * @throws IOException
     * @throws InterruptedException
     */
    public boolean chooseAction(String choice, boolean quit, Question question, PrintStream outFile)
            throws IOException, InterruptedException {
        Request theReOfTheQuest = new Request();
        switch (choice) {
            case "1":
                // 7.2.1 Store
                // 7.2.1.1 Display question in output document
                System.setOut(outFile);
                question.displayQuestion();
                System.setOut(oldStdout);
                break;
            case "2":
                Question newQ = new Question();
                // 7.2.2 Rephrase
                // 7.2.2.3 Convert to URI and send string to AI to be rephrased
                newQ.setQuestion(theReOfTheQuest.rephraseQuestion(question.getQuestion()));
                // 7.2.2.4 Replace and save?
                System.out.println(
                        "Would you like to \n" + "a: replace the previous question and save this to the file,\n"
                                + "b: save the old question instead, or\n" + "c: discard the question entirely?");
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                String tmp = reader.readLine();
                boolean entry = false;
                while (!entry) {
                    if (tmp == "c") {
                        break;
                    }
                    if ((tmp != "a") || (tmp != "b") || (tmp != "c")) {
                        switch (tmp) {
                            case "a":
                                // 7.2.2.4.1 Save new Question (newQ)
                                question.setQuestion(newQ.getQuestion());
                                System.setOut(outFile);
                                question.displayQuestion();
                                System.setOut(oldStdout);
                                entry = true;
                                break;
                            case "b":
                                // 7.2.2.4.2 Save old Question (question)
                                System.setOut(outFile);
                                question.displayQuestion();
                                System.setOut(oldStdout);
                                entry = true;
                                break;
                            case "c":
                                // 7.2.2.4.3 Discard Question
                                entry = true;
                                break;
                            default:
                                // 7.2.2.4.4 Invalid Entry
                                break;
                        }
                    } else {
                        System.out.println("INVALID ENTRY- Please enter a, b, or c");
                        entry = false;
                    }
                }
                break;
            case "3":
                // 7.2.3 Discard -> do not save question to document and move to next iteration
                break;
            case "q":
                // 7.2.4 QUIT
                // 7.2.4.1 Print quit message
                System.out.println("Quitting question and answer generator program...");
                // 7.2.4.2 Exit loop and close program
                System.exit(1);
            default:
                // 7.2.5 INVALID
                // 7.2.5.1 invalid input message
                System.out.println("INVALID ENTRY: please select 1, 2, 3, or q");
                /** 7.2.5.2 request again -> recursive */
                BufferedReader rd = new BufferedReader(new InputStreamReader(System.in));
                choice = rd.readLine();
                chooseAction(choice, quit, question, outFile);
        }
        return quit = false;
    }

    /**
     * Prints opening Welcome message to the user
     */
    public void startUp() {
        String header = "***********************************\n"
                + "**                               **\n"
                + "** Question and Answer Generator **\n"
                + "**                               **\n"
                + "***********************************\n";
        String welcomeMsg = "Welcome to the Question and Answer Generator!\n";
        System.out.println(header + welcomeMsg);
    }
}
