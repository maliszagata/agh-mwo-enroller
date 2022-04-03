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

@RestController
@RequestMapping("/meetings")
public class MeetingRestController {

	@Autowired
	MeetingService meetingService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<?> getMeetings() {
		Collection<Meeting> meetings = meetingService.getAll();
		return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getParticipant(@PathVariable("id") long id) {
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
	public ResponseEntity<?> deleteMeeting(@PathVariable("id") long id, @RequestBody Meeting meeting) {
		Meeting foundMeeting = meetingService.findById(id);
		if (foundMeeting == null) {
			return new ResponseEntity<String>("Unable to delete. Meeting " + id + " not found.",
					HttpStatus.NOT_FOUND);
		}
		meetingService.deleteMeeting(foundMeeting);
		return new ResponseEntity<Meeting>(foundMeeting, HttpStatus.OK);
	}

//	@RequestMapping(value = "", method = RequestMethod.PUT)
//	public ResponseEntity<?> updateParticipant(@RequestBody Participant participant) {
//		Participant foundParticipant = participantService.findByLogin(participant.getLogin());
//		if (foundParticipant == null) {
//			return new ResponseEntity<String>("Unable to update. User " + participant.getLogin() + " not found.",
//					HttpStatus.NOT_FOUND);
//		}
//		participantService.updateParticipant(foundParticipant, participant.getPassword());
//		return new ResponseEntity<Participant>(participant, HttpStatus.OK);
//	}

}
