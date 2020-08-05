/*
 * Singleton EJB to encapsulate a survey
 */
package surveyrestfulservice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;

@Singleton
public class SurveyBean {

    private List<Participant> participants; // list of all participants

    @PostConstruct
    public void initialiseParticipantCollection() {
        // convenience list to avoid using any database
        this.participants = new ArrayList<>();
        addParticipant("Roger Waters", "Verity Lang","None");
        addParticipant("Trevor Whittman", "Verity Lang","None");
        addParticipant("Conor McGregor", "Verity Lang","None");
        addParticipant("Bagger Vance", "Justine Diamond", "Auckland");
        addParticipant("Cub Swanson", "Justine Diamond","Auckland");
        addParticipant("Reginald Allen", "Justine Diamond", "Auckland");
        addParticipant("Portia Pottey", "Justine Diamond","Auckland");
        addParticipant("Callum Ingrate", "Verity Lang", "None");
        addParticipant("Fronte Mann", "Michael Vicks","Whangarei");
        addParticipant("Popp Stahr", "Michael Vicks","Whangarei");
        addParticipant("Mike Munchkin", "Michael Vicks","Whangarei");
        addParticipant("Ray Lazenby", "Verity Lang","None");
    }

    public void addParticipant(String participantName, String surveyor, String location) {

            participants.add(new Participant(participantName, surveyor, location));

    }

    public boolean removeParticipant(String participantName, String surveyor) {
        for (Participant p : participants) {
            if (p.getParticipantName().equals(participantName)
                    && p.getSurveyor().equals(surveyor)) {
                participants.remove(p);
                return true;
            }
        }
        return false; // participant not found in collection
    }

    public Collection<Participant> getParticipants() {
        return this.participants;
    }

    public Collection<String> getSurveyors() {
        Collection<String> surveyors = new ArrayList<String>();
        for (Participant p : this.participants) {
            if (!surveyors.contains(p.getSurveyor())) {
                surveyors.add(p.getSurveyor());
            }
        }
        return surveyors;
    }
    
    public Collection<String> getLocations() {
        Collection<String> locations = new ArrayList<String>();
        for (Participant p : this.participants) {
            if (!"".equals(p.getLocation())) {
                locations.add(p.getLocation());
            }
        }
        return locations;
    }
    

    //method to get all the participants that each surveyor has interviewed
    public Collection<Participant> getParticipantsForSurveyor(String surveyor) {
        Collection<Participant> surveyorsParticipants = new ArrayList<Participant>();
        for (Participant p : participants) {
            if (p.getSurveyor().equals(surveyor)) {
                surveyorsParticipants.add(p);
            }
        }
        return surveyorsParticipants;
    }
}
