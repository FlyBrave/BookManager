package com.nowcoder.project.service;

import com.nowcoder.project.dao.TicketDAO;
import com.nowcoder.project.model.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by nowcoder on 2018/08/07 下午2:52
 */
@Service
public class TicketService {

  @Autowired
  private TicketDAO ticketDAO;

  public void addTicket(Ticket t){
    ticketDAO.addTicket(t);
  }

  public Ticket getTicket(int uid){
    return ticketDAO.selectByUserId(uid);
  }

  public Ticket getTicket(String t){
    return ticketDAO.selectByTicket(t);
  }

  public void deleteTicket(int tid){
    ticketDAO.deleteTicketById(tid);
  }

  public void deleteTicket(String t){
    ticketDAO.deleteTicket(t);
  }
}
