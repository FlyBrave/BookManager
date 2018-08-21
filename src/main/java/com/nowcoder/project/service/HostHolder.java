package com.nowcoder.project.service;

import com.nowcoder.project.model.User;
import com.nowcoder.project.utils.ConcurrentUtils;
import org.springframework.stereotype.Service;

/**
 * Created by nowcoder on 2018/08/07 下午4:03
 */
@Service
public class HostHolder {

  public User getUser() {
    return ConcurrentUtils.getHost();
  }

  public void setUser(User user) {
    ConcurrentUtils.setHost(user);
  }
}
