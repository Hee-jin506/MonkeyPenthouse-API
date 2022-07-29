package com.monkeypenthouse.core.repository;

import com.monkeypenthouse.core.repository.entity.Ticket;
import org.springframework.data.repository.CrudRepository;

public interface TicketRepository extends CrudRepository<Ticket, Long> {
}
