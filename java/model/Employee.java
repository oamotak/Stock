package model;

import java.io.Serializable;

public class Employee implements Serializable {
 private String name;
 private String pass;
 private String id;
 private String type;
 public Employee() {}
 public Employee(String id,String pass,String name,String type) {this.id=id;this.pass=pass;this.name=name;this.type=type;}
 public Employee(String id,String pass,String name) {this.id=id;this.pass=pass;this.name=name;}
 public String getName() {return this.name;}
 public String getId() {return this.id;}
 public String getType() {return this.type;}
}
