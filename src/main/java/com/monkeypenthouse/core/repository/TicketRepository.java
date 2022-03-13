package com.monkeypenthouse.core.repository;

import com.monkeypenthouse.core.entity.Ticket;
import org.springframework.data.repository.CrudRepository;

public interface TicketRepository extends CrudRepository<Ticket, Long> {
}
