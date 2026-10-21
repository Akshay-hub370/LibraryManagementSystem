
package com.library.model;

public class Member {

    private int id;
    private String memberCode;
    private String name;
    private String rollNumber;
    private String department;
    private String phone;
    private String email;
    private String status;

    public Member() {
    }

    public Member(int id, String memberCode, String name,
                  String rollNumber, String department,
                  String phone, String email, String status) {
        this.id = id;
        this.memberCode = memberCode;
        this.name = name;
        this.rollNumber = rollNumber;
        this.department = department;
        this.phone = phone;
        this.email = email;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
