package com.company.enroller.controllers;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/meetings")
public class MeetingRestController {

    @Autowired
    MeetingService meetingService;
    @Autowired
    ParticipantService participantService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetings() {
        Collection<Meeting> meetings = meetingService.getAll();
        return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getMeeting(@PathVariable("id") long id) {
        Meeting meeting = meetingService.findById(id);
        if (meeting == null) {
            return new ResponseEntity<String>("Meeting " + id + " not found.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> registerMeeting(@RequestBody Meeting meeting) {
        Meeting foundMeeting = meetingService.findById(meeting.getId());
        if (foundMeeting != null) {
            return new ResponseEntity<String>("Unable to create. A meeting with id " + meeting.getId() + " already exist.",
                    HttpStatus.CONFLICT);
        }
        meetingService.addMeeting(meeting);
        return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteMeeting(@RequestBody Meeting meeting) {
        Meeting foundMeeting = meetingService.findById(meeting.getId());
        if (foundMeeting == null) {
            return new ResponseEntity<String>("Unable to delete. Meeting " + meeting.getId() + " not found.",
                    HttpStatus.NOT_FOUND);
        }
        meetingService.deleteMeeting(foundMeeting);
        return new ResponseEntity<Meeting>(foundMeeting, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/participants", method = RequestMethod.PUT)
    public ResponseEntity<?> addParticipant(@PathVariable("id") long id, @RequestBody Participant participant) {

        Participant foundParticipant = participantService.findByLogin(participant.getLogin());

        if (foundParticipant == null) {
            return new ResponseEntity<String>("Unable to update. User " + participant.getLogin()
                    + " not found.",
                    HttpStatus.NOT_FOUND);
        }

        Meeting meeting = meetingService.findById(id);

        List<String> participantsLogins = meeting.getParticipants().stream().map(Participant::getLogin).toList();
        if (participantsLogins.contains(foundParticipant.getLogin())) {
            return new ResponseEntity<String>("Unable to update. User " + participant.getLogin()
                    + " already participate in this meeting.",
                    HttpStatus.CONFLICT);
        }

        meetingService.addParticipant(meeting, participant);
        return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/participants", method = RequestMethod.GET)
    public ResponseEntity<?> getParticipants(@PathVariable("id") long id) {

        Meeting meeting = meetingService.findById(id);

        if (meeting == null) {
            return new ResponseEntity<String>("Meeting " + id + " not found.",
                    HttpStatus.NOT_FOUND);
        }

        Collection<Participant> participants = meeting.getParticipants();

        if (participants.isEmpty()) {
            return new ResponseEntity<String>("Meeting " + meeting.getId()
                    + " has no participants.",
                    HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/participants", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteParticipants(@PathVariable("id") long id, @RequestBody Participant participant) {

        Meeting meeting = meetingService.findById(id);
        Collection<Participant> participants = meeting.getParticipants();

        if (participants.isEmpty()) {
            return new ResponseEntity<String>("Meeting " + meeting.getId()
                    + " has no participants.",
                    HttpStatus.NOT_FOUND);
        }

        Participant foundParticipant = participantService.findByLogin(participant.getLogin());

        meetingService.removeParticipant(meeting, foundParticipant);
        return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateMeeting(@RequestBody Meeting meeting) {

        Meeting foundMeeting = meetingService.findById(meeting.getId());

        if (foundMeeting == null) {
            return new ResponseEntity<String>("Unable to update. Meeting " + meeting.getId() + " not found.",
                    HttpStatus.NOT_FOUND);
        }

        meetingService.updateMeeting(foundMeeting, meeting);
        return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
    }

}
