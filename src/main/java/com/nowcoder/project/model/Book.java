package com.nowcoder.project.model;

/**
 * Created by nowcoder on 2018/08/04 下午3:41
 */
public class Book {

  private int id;

  private String name;

  private String author;

  private String price;

  /**
   * {@link com.nowcoder.project.model.enums.BookStatusEnum}
   */
  private int status;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }
}
