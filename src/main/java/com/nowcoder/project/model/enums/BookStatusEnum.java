package com.nowcoder.project.model.enums;

/**
 * Created by nowcoder on 2018/08/04 下午9:38
 */
public enum BookStatusEnum {

  NORMAL(0),  //正常
  DELETE(1),  //删除
  RECOMMENDED(2), //推荐
  ;

  private int value;

  BookStatusEnum(int value){
    this.value = value;
  }

  public int getValue(){
    return value;
  }

}
