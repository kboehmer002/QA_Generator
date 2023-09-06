package src.edu.odu.cs411;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Request {
    /**
     * encodeURI(String s) function from
     * http://www.java2s.com/example/java-utility-method/uri-encode/encodeuri-string
     * -s-c3c85.html
     * 
     * Takes text and makes it compatable for the URI HTML Request and returns the
     * produced URI
     * 
     * @param s - string to be encoded
     * @return
     */
    public static String encodeURI(String s) {
        String result;
        try {
            result = URLEncoder.encode(s, "UTF-8").replaceAll("\\+", "%20").replaceAll("\\%21", "!")
                    .replaceAll("\\%27", "'").replaceAll("\\%28", "(").replaceAll("\\%29", ")")
                    .replaceAll("\\%7E", "~");
        } // This exception should never occur.
        catch (Exception e) {
            result = s;
        }
        return result;
    }

    /**
     * sendRequest() function based on code from
     * https://rapidapi.com/DataToBiz/api/prepai-generate-questions
     * 
     * Sends URI to API to generate and return question information from the given
     * data
     * 
     * @param uri_topic       - topic of the data as URI
     * @param uri_string_data - text data as URI
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public String sendRequest(String uri_topic, String uri_string_data) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://prepai-generate-questions.p.rapidapi.com/getQuestions"))
                .header("content-type", "application/x-www-form-urlencoded")
                .header("X-RapidAPI-Key", "6ce629e038mshfe7f4eeb61ff2f9p1e02bejsn507bc48e47b3")
                .header("X-RapidAPI-Host", "prepai-generate-questions.p.rapidapi.com")
                // REPLACE .method WITH PROPER DATA STRINGS
                .method("POST",
                        HttpRequest.BodyPublishers.ofString("topic=" + uri_topic + "&content=" + uri_string_data))
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        return (response.body());
    }

    /**
     * Rephraser Text and Spintax
     * Based on code from
     * https://rapidapi.com/micaelbh/api/rephraser-text-and-spintax
     * 
     * Sends URI to API to rephrase the question return the new phrased question
     * string
     * 
     * @param oldString - String of old question
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public String rephraseQuestion(String oldString) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://rephraser-text-and-spintax.p.rapidapi.com/spintext.php"))
                .header("content-type", "application/x-www-form-urlencoded")
                .header("X-RapidAPI-Key", "6ce629e038mshfe7f4eeb61ff2f9p1e02bejsn507bc48e47b3")
                .header("X-RapidAPI-Host", "rephraser-text-and-spintax.p.rapidapi.com")
                .method("POST", HttpRequest.BodyPublishers.ofString(
                        "text_to_rewrite=" + encodeURI(oldString)))
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        return response.body();
    }
}
