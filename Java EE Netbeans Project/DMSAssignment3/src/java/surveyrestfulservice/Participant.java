/*
 * Encapsulates the participant of a survey - and the person who surveyed them.
 * Includes the ability to assign random answers to three predefined survey questions.
 * This means I don't have to manually assign survey answer values when I create a participant.
 * This class also includes methods for getting and setting.
 */
package surveyrestfulservice;

import java.util.Random;

/**
 *
 * @author AdrianF
 */
public class Participant {

    private String participantName, surveyor, location;
    private int[] surveyAnswers;
    private String[] surveyQuestions;
    private final String[] defaultQuestions = new String[]{"On a scale of one to five, is the prime minister doing a good job?",
        "On a scale of one to five, is the government doing a good job?",
        "On a scale of one to five, is the opposition doing a good job?"};

    public Participant() {
    }

    public Participant(String participantName, String surveyor, String location) {

        this.surveyQuestions = this.defaultQuestions;

        setParticipantName(participantName);
        setSurveyor(surveyor);

        Random rand = new Random();
        int[] randomAnswers = new int[3];

        for (int i = 0; i < 3; i++) {
            randomAnswers[i] = rand.nextInt(5) + 1;
        }

        setSurveyAnswers(randomAnswers);
        setLocation(location);
    }

    public String getParticipantName() {
        return this.participantName;
    }

    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }

    public String getSurveyor() {
        return this.surveyor;
    }

    public void setSurveyor(String surveyor) {
        this.surveyor = surveyor;
    }

    public int[] getSurveyAnswers() {
        return this.surveyAnswers;
    }

    public void setSurveyAnswers(int[] surveyAnswers) {
        this.surveyAnswers = surveyAnswers;
    }

    public String[] getSurveyQuestions() {
        return this.surveyQuestions;
    }

    public void setSurveyQuestions(String[] surveyQuestions) {
        this.surveyQuestions = surveyQuestions;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
