package src.edu.odu.cs411;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.Buffer;
import java.util.Scanner;
import java.util.Vector;
import src.edu.odu.cs411.Request;

public class Store {
    String PLACEHOLDER_STRING = " ";
    String filePath;
    String topic;
    String fullFileString;
    String URI_String;
    String URI_Topic;
    File outDoc;
    PrintStream outFilestream;
    Vector<Question> allQuestions = new Vector<Question>();

    /**
     * Requests and reads in input data from user to store as a single string
     * 
     * @throws IOException
     */
    public void requestInputDoc() throws IOException {
        // 2.1 Take Input Doc
        // 2.1.2 get input data file name
        System.out.println("Please provide the input file data for question generation... ");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        // 2.1.3 Reading data using readLine
        this.filePath = reader.readLine();
        if (!this.filePath.contains(".txt")) {
            System.out.println("INVALID DOCUMENT TYPE: documents must be .txt files");
            requestInputDoc();
        } else {
            // Tell user file name given

            BufferedReader br = null;
            try {
                // 2.1.4 Convert to File
                File file = new File(this.filePath);
                System.out.println("Given file is: " + file.getAbsolutePath());
                if (!file.exists()) {
                    System.out.println("ERROR FILE DOES NOT EXIST");
                    requestInputDoc();
                } else {
                    // 2.2 Read *data* to String
                    br = new BufferedReader(new FileReader(file));
                    String line = null;

                    while ((line = br.readLine()) != null) {
                        this.fullFileString = this.fullFileString + line + "\n";
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Requests and reads in input from user to store as the Topic of the data being
     * transformed into questions
     * 
     * @throws IOException
     */
    public void requestTopic() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("What is the topic of the data?");
        this.topic = reader.readLine();
    }

    /**
     * Requests an output document name from the user and stores it with that name
     * 
     * @throws FileNotFoundException
     */
    public void outputDocName() throws FileNotFoundException {
        // System.out.print("What would you like to name your output document? ");
        // String docOutName = reader.readLine();
        String docOutName = "testOutput.txt";

        // try (Scanner reader = new Scanner(System.in)) {
        // docOutName = reader.nextLine(); // Read user input
        // }

        if (docOutName.contains(".txt")) {
            this.outDoc = new File(docOutName);
            System.out.println(
                    "From now on " + this.outDoc.getAbsolutePath()
                            + " will be your console for final question output.");
            this.outFilestream = new PrintStream(this.outDoc);
        } else {
            System.out.println("INVALID DOCUMENT TYPE: documents must be .txt files");
            outputDocName();
        }

    }

    /**
     * Takes string of data and topic, converts it to URI, and sends out the HTTP
     * request for question generation. It returns the single string of Q&A
     * generation
     * 
     * @return String
     * @throws IOException
     * @throws InterruptedException
     */
    public String convertStrAndRequest() throws IOException, InterruptedException {
        Request r = new Request();
        this.URI_String = Request.encodeURI(fullFileString);
        this.URI_Topic = Request.encodeURI(topic);
        // 4.3 Returns Q&A Generated
        return r.sendRequest(URI_Topic, URI_String);
    }

    /**
     * Takes the API's question response and stores it as individual questions with
     * all of its separated data
     * 
     * @param response - a single String containing the question generation API's
     *                 entire response
     */
    public void intakeQuestionsResponse(String response) {
        // 5.1 Split into individual questions
        Vector<String> splitQuestionsVect = new Vector<String>();
        splitQuestionsVect = separateQuestions(response);

        // 5.2 Remove non-question segments
        Vector<String> splitNoJunk = new Vector<String>();
        splitNoJunk = removeJunk(splitQuestionsVect);

        // 5.3 Separate Components and store question
        for (int i = 0; i < splitNoJunk.size(); i++) {
            this.allQuestions.add(disassembleQuestion(splitNoJunk.get(i)));
        }
    }

    /**
     * Appends question to the vector containing all the generated questions
     * 
     * @param q - question to be appended
     */
    public void addQuestion(Question q) {
        this.allQuestions.add(q);
    }

    /**
     * Stores entire vector of questions in place of the current set of questions
     * 
     * @param q - vector of questions to replace the old ones with
     */
    public void setAllQuestions(Vector<Question> q) {
        this.allQuestions = q;
    }

    /**
     * Returns entire vector of stored questions
     * 
     * @return Vector<Question>
     */
    public Vector<Question> getAllQuestions() {
        return this.allQuestions;
    }

    /**
     * Deletes a question from the compiled questions based on position
     * 
     * @param index - position of the question to be deleted in the vector
     */
    public void removeQuestion(int index) {
        this.allQuestions.remove(index);
    }

    /**
     * Returns the size of the compiled questions vector
     * 
     * @return int
     */
    public int getTotalQuestions() {
        return allQuestions.size();
    }

    /**
     * Retrieves output location
     * 
     * @return PrintStream
     */
    public PrintStream getOutFileStream() {
        return this.outFilestream;
    }

    /**
     * Stores entire input data file
     * 
     * @param str - string to be stored
     */
    public void setFullFileString(String str) {
        this.fullFileString = str;
    }

    /**
     * Returns String containing all fo the input data file
     * 
     * @return String
     */
    public String getFullFileString() {
        return this.fullFileString;
    }

    /**
     * Separates string of all questions into individual questions based on the
     * position of {}'s
     * 
     * @param response - string of all questions and misc data
     * @return Vector<String>
     */
    public Vector<String> separateQuestions(String response) {
        Vector<String> spV = new Vector<String>();
        String[] spQ = response.split("[{}]+");
        for (int i = 0; i < spQ.length; i++) {
            if (spQ[i] != null) {
                spV.add(spQ[i]);
            }
        }
        return spV;
    }

    /**
     * Removes unnecessary elements found between question elements and returns it
     * without the junk between the question strings
     * 
     * @param oldVec - vector of questions and junk elements
     * @return Vector<String>
     */
    public Vector<String> removeJunk(Vector<String> oldVec) {
        Vector<String> newVec = new Vector<String>(); // PLACEHOLDER FOR CODE @todo
        for (int i = 0; i < oldVec.size(); i++) {
            if (oldVec.get(i).contains("\"topic\":")) {
                newVec.add(oldVec.get(i));
            } // otherwise ignore and we get rid of all the junk
        }
        // System.out.println("oldVec.size() = " + oldVec.size() + " & newVec.size() = "
        // + newVec.size());
        return newVec;
    }

    /**
     * Sorts and stores components
     * 
     * @param old
     * @return Question
     */
    public Question disassembleQuestion(String old) {
        Question quest = new Question();
        // 5.3.1 Split Into Lines of Components
        Vector<String> strs = new Vector<String>();
        String[] spQ = old.split("[\n]+");
        // System.out.println("Size question split = " + spQ.length);
        // remove random new lines
        for (int i = 0; i < spQ.length; i++) {
            if (!spQ[i].matches("\\s*")) {
                strs.add(spQ[i]);
            }
        }

        // 5.3.2 Split Components off by FIRST ':'
        Vector<String> v = new Vector<String>();
        for (int i = 0; i < strs.size(); i++) {
            v = spltBySemicolon(strs.get(strs.size() - 1));
            quest.setSrc(v.get(1));
            if (strs.get(i).contains("\":")) {
                v = spltBySemicolon(strs.get(i));
                if (v.get(0).contains("\"topic\"")) {
                    // discarded- not valuable information
                } else if (v.get(0).contains("\"category_type\"")) {
                    quest.setCatagory(v.get(1));
                } else if (v.get(0).contains("\"question\"")) {
                    quest.setQuestion(v.get(1));
                } else if (v.get(0).contains("\"options\"")) {
                    // OPTIONS IS SPECIAL BECUASE OF THE FOLLOWING LINES
                    quest.addChoice(v.get(1)); // first option
                    i++; // move to next line of text
                    while ((i + 1) < strs.size()) { // goes up until help text
                        quest.addChoice(strs.get(i)); // store the line and continue for each option
                        i++;
                    }
                }
            }
        }
        return quest;
    }

    /**
     * Splits string into TWO elements of a vector with a semicolon as the delimiter
     * separating the type of question data to the actual data to be stored later
     * 
     * @param str - string with semicolon
     * @return Vector<String>
     */
    public Vector<String> spltBySemicolon(String str) {
        String[] afterSplit = str.split("[:]", 2);
        Vector<String> strV = new Vector<String>();
        for (int i = 0; i < afterSplit.length; i++) {
            strV.add(afterSplit[i]);
        }
        return strV;
    }

    public String getUserInput() {
        String str = "";
        try (Scanner reader = new Scanner(System.in)) {
            str = reader.nextLine(); // Read user input
        }

        return str;
    }
}
