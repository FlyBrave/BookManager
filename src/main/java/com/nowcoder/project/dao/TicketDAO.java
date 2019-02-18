package com.nowcoder.project.dao;


import com.nowcoder.project.model.Ticket;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * Created by nowcoder on 2018/08/04 下午3:44
 */
@Mapper
public interface TicketDAO {

  String table_name = " ticket ";
  String insert_field = " user_id, ticket, expired_at ";
  String select_field = " id, " + insert_field;

  @Insert({"insert into", table_name, "(", insert_field,
      ") values (#{userId},#{ticket},#{expiredAt})"})
  int addTicket(Ticket ticket);

  @Select({"select", select_field, "from", table_name, "where user_id=#{uid}"})
  Ticket selectByUserId(int uid);

  @Select({"select", select_field, "from", table_name, "where ticket=#{t}"})
  Ticket selectByTicket(String t);

  @Delete({"delete from", table_name, " where id=#{tid}"})
  void deleteTicketById(int tid);

  @Delete({"delete from", table_name, " where ticket=#{t}"})
  void deleteTicket(String t);

}
