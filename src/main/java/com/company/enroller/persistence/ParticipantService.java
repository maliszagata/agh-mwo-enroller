package com.company.enroller.persistence;

import java.util.Collection;

import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Participant;

@Component("participantService")
public class ParticipantService {

	DatabaseConnector connector;

	public ParticipantService() {
		connector = DatabaseConnector.getInstance();
	}

	public Collection<Participant> getAll() {
		String hql = "FROM Participant";
		Query query = connector.getSession().createQuery(hql);
		return query.list();
	}

	public Participant findByLogin(String login) {
		return (Participant) connector.getSession().get(Participant.class, login);
	}

	public Participant addParticipant(Participant participant) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().save(participant);
		transaction.commit();
		return (Participant) connector.getSession().get(Participant.class, participant.getLogin());
	}

	public Participant deleteParticipant(Participant participant) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().delete(participant);
		transaction.commit();
		return (Participant) connector.getSession().get(Participant.class, participant.getLogin());

	}

	public Participant updateParticipant(Participant participant, String password) {
		participant.setPassword(password);
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().update(participant);
		transaction.commit();
		return (Participant) connector.getSession().get(Participant.class, participant.getLogin());
	}
}
