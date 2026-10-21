
package com.library.ui;

import com.library.dao.MemberDAO;
import com.library.model.Member;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class EditMemberDialog extends JDialog {

    private JTextField memberCodeField;
    private JTextField nameField;
    private JTextField rollNumberField;
    private JTextField phoneField;
    private JTextField emailField;
    private JComboBox<String> departmentBox;
    private JComboBox<String> statusBox;

    private final MemberDAO memberDAO;
    private final Member member;
    private boolean memberUpdated = false;

    public EditMemberDialog(JFrame parent, Member member) {
        super(parent, "Edit Member", true);

        this.member = member;
        this.memberDAO = new MemberDAO();

        setSize(500, 570);
        setLocationRelativeTo(parent);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(0, 10));
        mainPanel.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel title = new JLabel("Edit Member");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 15));
        formPanel.setBorder(new EmptyBorder(20, 0, 20, 0));

        formPanel.add(new JLabel("Member Code:"));
        memberCodeField = new JTextField(member.getMemberCode());
        formPanel.add(memberCodeField);

        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField(member.getName());
        formPanel.add(nameField);

        formPanel.add(new JLabel("Roll Number:"));
        rollNumberField = new JTextField(
                member.getRollNumber() == null
                        ? "" : member.getRollNumber());
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
        departmentBox.setSelectedItem(member.getDepartment() == null
                ? "" : member.getDepartment());
        formPanel.add(departmentBox);

        formPanel.add(new JLabel("Phone:"));
        phoneField = new JTextField(
                member.getPhone() == null ? "" : member.getPhone());
        formPanel.add(phoneField);

        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField(
                member.getEmail() == null ? "" : member.getEmail());
        formPanel.add(emailField);

        formPanel.add(new JLabel("Status:"));
        statusBox = new JComboBox<>(
                new String[]{"ACTIVE", "INACTIVE"});
        statusBox.setSelectedItem(
                member.getStatus() == null
                        ? "ACTIVE" : member.getStatus().toUpperCase());
        formPanel.add(statusBox);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT));

        JButton cancelButton = new JButton("Cancel");
        JButton saveButton = new JButton("Save Changes");

        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        cancelButton.addActionListener(e -> dispose());
        saveButton.addActionListener(e -> saveChanges());

        add(mainPanel);
    }

    private void saveChanges() {
        String memberCode = memberCodeField.getText().trim();
        String name = nameField.getText().trim();
        String rollNumber = rollNumberField.getText().trim();

        String department = departmentBox.getSelectedItem() == null
                ? ""
                : departmentBox.getSelectedItem().toString().trim();

        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String status = statusBox.getSelectedItem().toString();

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

        member.setMemberCode(memberCode);
        member.setName(name);
        member.setRollNumber(rollNumber);
        member.setDepartment(department);
        member.setPhone(phone);
        member.setEmail(email);
        member.setStatus(status);

        if (memberDAO.updateMember(member)) {
            memberUpdated = true;

            JOptionPane.showMessageDialog(
                    this,
                    "Member updated successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            dispose();
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Could not update the member. Check the NetBeans "
                    + "Output window. The member code or roll number "
                    + "may already exist.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isMemberUpdated() {
        return memberUpdated;
    }
}
