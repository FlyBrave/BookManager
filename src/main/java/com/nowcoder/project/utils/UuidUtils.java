package com.nowcoder.project.utils;

import java.util.UUID;

/**
 * Created by nowcoder on 2018/08/07 下午2:38
 */
public class UuidUtils {

  public static String next(){
    return UUID.randomUUID().toString().replace("-","a");
  }

}
