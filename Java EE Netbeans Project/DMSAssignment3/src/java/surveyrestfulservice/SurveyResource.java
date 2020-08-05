/*
 * JAX_RS RESTful resource class for GET requests on survey results/participants/surveyors
 * and POST requests to add to the participant/surveyor collection
 * @author Adrian Fehr
 */
package surveyrestfulservice;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

@Named // so that dependency injection can be used for the EJB
@Path("/survey")
public class SurveyResource {

    @EJB
    private SurveyBean surveyorBean;

    /**
     * Creates a new instance of SurveyResource
     */
    public SurveyResource() {
    }

    //This method gets results of the survey and makes and html doc from the results
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getSurveyResults() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");

        Collection<Participant> allParticipants = surveyorBean.getParticipants();
        buffer.append("<p><strong>Number of Participants: </strong>").append(allParticipants.size()).append("</p>");
        buffer.append("<p><strong>All Answers: </strong></p>");

        //need these variables to find the average survey answers
        int count = 0;
        int[] questionTotals = new int[3];

        //loop to add all answers in html format
        for (Participant participant : allParticipants) {
            int[] answers = participant.getSurveyAnswers();
            buffer.append("<p>{");

            for (int i = 0; i < answers.length; i++) {
                questionTotals[i] += answers[i];
                buffer.append(answers[i]).append(", ");
            }
            buffer.append("}</p>");
            count++;
        }

        //add all average answers in HTML format
        buffer.append("<p><strong>").append("Totals: </strong></p>");
        buffer.append("<p>Question One Average Answer: ").append(questionTotals[0] / count).append("</p>");
        buffer.append("<p>Question Two Average Answer: ").append(questionTotals[1] / count).append("</p>");
        buffer.append("<p>Question Three Average Answer: ").append(questionTotals[2] / count).append("</p>");

        return buffer.toString();
    }

    //method to get all participants and return them in HTML format
    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/participants")
    public String getParticipants() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");

        buffer.append("<p><strong>Participants: </strong></p>");

        Collection<Participant> allParticipants = surveyorBean.getParticipants();

        for (Participant participant : allParticipants) {
            buffer.append("<p>").append(participant.getParticipantName()).append("</p>");
        }
        return buffer.toString();
    }

    //method to get all surveyors and return them in HTML format
    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/surveyors")
    public String getSurveyors() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");

        buffer.append("<p><strong>Surveyors: </strong></p>");

        Collection<String> allSurveyors = surveyorBean.getSurveyors();

        for (String surveyor : allSurveyors) {
            buffer.append("<p>").append(surveyor).append("</p>");
        }
        return buffer.toString();
    }

    //method to get all locations and return them in HTML format, with totals
    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/locations")
    public String getLocations() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");

        buffer.append("<p><strong>Location Frequency: </strong></p>");

        Collection<String> allLocations = surveyorBean.getLocations();
        HashSet<String> uniqueLocations = new HashSet<>(allLocations);
        for (String location : uniqueLocations) {
            int count = 0;

            count = Collections.frequency(allLocations, location.toString());

            buffer.append("<p><strong>").append(location).append(": ").append(count).append("</strong></p>");
        }

        return buffer.toString();
    }

    // method to accept post requests from HTML forms in order to add participants/surveyors
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void addParticipantViaPost(
            MultivaluedMap<String, String> formParams) {

        if (formParams.getFirst("participant") != null && formParams.getFirst("surveyor") != null
                && formParams.getFirst("location") != null) {

            String participant = formParams.getFirst("participant");
            String surveyor = formParams.getFirst("surveyor");
            String location = formParams.getFirst("location");

            surveyorBean.addParticipant(participant, surveyor, location);
        } else if (formParams.getFirst("location") == null) {
            String participant = formParams.getFirst("participant");
            String surveyor = formParams.getFirst("surveyor");
            String location = "None";

            surveyorBean.addParticipant(participant, surveyor, location);
        }

    }
}
