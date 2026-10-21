
package com.library.ui;

import com.library.dao.MemberDAO;
import com.library.model.Member;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AddMemberDialog extends JDialog {

    private JTextField memberCodeField;
    private JTextField nameField;
    private JTextField rollNumberField;
    private JTextField phoneField;
    private JTextField emailField;
    private JComboBox<String> departmentBox;

    private boolean memberSaved = false;

    private final MemberDAO memberDAO;

    public AddMemberDialog(JFrame parent) {
        super(parent, "Add New Member", true);

        memberDAO = new MemberDAO();

        setSize(500, 520);
        setLocationRelativeTo(parent);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(0, 10));
        mainPanel.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel title = new JLabel("Add New Member");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 15));
        formPanel.setBorder(new EmptyBorder(20, 0, 20, 0));

        formPanel.add(new JLabel("Member Code:"));
        memberCodeField = new JTextField();
        formPanel.add(memberCodeField);

        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Roll Number:"));
        rollNumberField = new JTextField();
        formPanel.add(rollNumberField);

        formPanel.add(new JLabel("Department:"));
        departmentBox = new JComboBox<>(new String[]{
            "Computer Science",
            "Information Technology",
            "Mechanical Engineering",
            "Civil Engineering",
            "Electrical Engineering",
            "Electronics Engineering",
            "Other"
        });
        departmentBox.setEditable(true);
        formPanel.add(departmentBox);

        formPanel.add(new JLabel("Phone:"));
        phoneField = new JTextField();
        formPanel.add(phoneField);

        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT));

        JButton cancelButton = new JButton("Cancel");
        JButton saveButton = new JButton("Save Member");

        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        cancelButton.addActionListener(e -> dispose());
        saveButton.addActionListener(e -> saveMember());

        add(mainPanel);
    }

    private void saveMember() {
        String memberCode = memberCodeField.getText().trim();
        String name = nameField.getText().trim();
        String rollNumber = rollNumberField.getText().trim();
        String department =
                departmentBox.getSelectedItem() == null
                ? ""
                : departmentBox.getSelectedItem().toString().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();

        if (memberCode.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Member Code and Name are required.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!phone.isEmpty() && !phone.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(
                    this,
                    "Phone number must contain exactly 10 digits.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!email.isEmpty()
                && !email.matches(
                        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid email address.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Member member = new Member();
        member.setMemberCode(memberCode);
        member.setName(name);
        member.setRollNumber(rollNumber);
        member.setDepartment(department);
        member.setPhone(phone);
        member.setEmail(email);
        member.setStatus("ACTIVE");

        boolean saved = memberDAO.addMember(member);

        if (saved) {
            memberSaved = true;

            JOptionPane.showMessageDialog(
                    this,
                    "Member added successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            dispose();
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Could not save the member. Check the NetBeans "
                    + "Output window. The member code or roll number "
                    + "may already exist.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isMemberSaved() {
        return memberSaved;
    }
}
