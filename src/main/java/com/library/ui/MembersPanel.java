
package com.library.ui;

import com.library.dao.MemberDAO;
import com.library.model.Member;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MembersPanel extends JPanel {

    private JTextField searchField;
    private JTable membersTable;
    private DefaultTableModel tableModel;

    private final MemberDAO memberDAO;

    public MembersPanel() {
        memberDAO = new MemberDAO();

        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(25, 30, 25, 30));

        // HEADER
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Members");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));

        JLabel subtitleLabel =
                new JLabel("Manage students and faculty members");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(Color.GRAY);

        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(subtitleLabel);

        headerPanel.add(titlePanel, BorderLayout.WEST);

        // ADD MEMBER BUTTON
        JButton addMemberButton = new JButton("+ Add Member");
        UIUtils.styleButton(addMemberButton, UIUtils.PRIMARY);
        addMemberButton.setFont(new Font("Arial", Font.BOLD, 14));
        addMemberButton.setForeground(Color.WHITE);
        addMemberButton.setBackground(new Color(52, 152, 219));
        addMemberButton.setFocusPainted(false);

        addMemberButton.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(this);
            JFrame parent = window instanceof JFrame
                    ? (JFrame) window : null;

            AddMemberDialog dialog = new AddMemberDialog(parent);
            dialog.setVisible(true);

            if (dialog.isMemberSaved()) {
                loadMembersFromDatabase();
            }
        });

        headerPanel.add(addMemberButton, BorderLayout.EAST);

        // SEARCH
        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(new EmptyBorder(12, 12, 12, 12));

        searchField = new JTextField();
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createTitledBorder(
                "Search by member code, name, roll number or email"));

        
JButton searchButton = new JButton("Search");
JButton refreshButton = new JButton("Refresh");

JPanel searchButtonsPanel = new JPanel(
        new FlowLayout(FlowLayout.RIGHT, 5, 0));
searchButtonsPanel.setOpaque(false);

searchButtonsPanel.add(searchButton);
searchButtonsPanel.add(refreshButton);

searchPanel.add(searchField, BorderLayout.CENTER);
searchPanel.add(searchButtonsPanel, BorderLayout.EAST);


        
searchButton.addActionListener(e -> searchMembers());
searchField.addActionListener(e -> searchMembers());

refreshButton.addActionListener(e -> {
    searchField.setText("");
    loadMembersFromDatabase();
});


        // TABLE MATCHING THE DATABASE
        String[] columns = {
            "Member ID",
            "Member Code",
            "Name",
            "Roll Number",
            "Department",
            "Phone",
            "Email",
            "Status"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        membersTable = new JTable(tableModel);
        UIUtils.styleTable(membersTable);
        membersTable.setRowHeight(32);
        membersTable.setFont(new Font("Arial", Font.PLAIN, 13));
        membersTable.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 13));
        membersTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(membersTable);

        // ACTION BUTTONS
        JPanel actionPanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setOpaque(false);

        JButton editButton = new JButton("Edit");
        JButton deactivateButton = new JButton("Deactivate");
        JButton deleteButton = new JButton("Delete");

        UIUtils.styleButton(deactivateButton, UIUtils.DANGER);
        UIUtils.styleButton(deleteButton, UIUtils.DANGER);
                
        editButton.setEnabled(false);
        deactivateButton.setEnabled(false);
        deleteButton.setEnabled(false);
        
editButton.addActionListener(e -> {
    int selectedRow = membersTable.getSelectedRow();

    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(
                this,
                "Please select a member to edit.",
                "No Member Selected",
                JOptionPane.WARNING_MESSAGE);
        return;
    }

    int modelRow = membersTable.convertRowIndexToModel(selectedRow);

    int memberId = Integer.parseInt(
            tableModel.getValueAt(modelRow, 0).toString());

    // Get the latest member data from MySQL.
    Member selectedMember = null;

    for (Member member : memberDAO.getAllMembers()) {
        if (member.getId() == memberId) {
            selectedMember = member;
            break;
        }
    }

    if (selectedMember == null) {
        JOptionPane.showMessageDialog(
                this,
                "Member not found in the database.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        loadMembersFromDatabase();
        return;
    }

    Window window = SwingUtilities.getWindowAncestor(this);
    JFrame parent = window instanceof JFrame ? (JFrame) window : null;

    EditMemberDialog dialog =
            new EditMemberDialog(parent, selectedMember);

    dialog.setVisible(true);

    if (dialog.isMemberUpdated()) {
        loadMembersFromDatabase();
    }
});

        actionPanel.add(editButton);
        actionPanel.add(deactivateButton);
        actionPanel.add(deleteButton);
        
        
membersTable.getSelectionModel().addListSelectionListener(e -> {
    boolean selected = membersTable.getSelectedRow() != -1;

    editButton.setEnabled(selected);
    deactivateButton.setEnabled(selected);
    deleteButton.setEnabled(selected);
});


       
deleteButton.addActionListener(e -> {
    int selectedRow = membersTable.getSelectedRow();

    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(
                this,
                "Please select a member to delete.",
                "No Member Selected",
                JOptionPane.WARNING_MESSAGE);
        return;
    }

    int modelRow = membersTable.convertRowIndexToModel(selectedRow);

    int memberId = Integer.parseInt(
            tableModel.getValueAt(modelRow, 0).toString());

    String memberName = tableModel.getValueAt(modelRow, 2).toString();

    int choice = JOptionPane.showConfirmDialog(
            this,
            "Permanently delete member \"" + memberName
                    + "\" (ID: " + memberId + ")?\n"
                    + "This action cannot be undone.",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

    if (choice != JOptionPane.YES_OPTION) {
        return;
    }

    boolean deleted = memberDAO.deleteMember(memberId);

    if (deleted) {
        JOptionPane.showMessageDialog(
                this,
                "Member deleted successfully.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

        loadMembersFromDatabase();
    } else {
        JOptionPane.showMessageDialog(
                this,
                "Could not delete the member. Check the NetBeans Output "
                        + "window for the database error. The member may "
                        + "be referenced by another database record.",
                "Delete Failed",
                JOptionPane.ERROR_MESSAGE);
    }
});


        // MAIN LAYOUT
        JPanel centerPanel = new JPanel(new BorderLayout(0, 15));
        centerPanel.setOpaque(false);
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(actionPanel, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        // LOAD REAL DATABASE RECORDS
        loadMembersFromDatabase();
    }

    private void loadMembersFromDatabase() {
        tableModel.setRowCount(0);

        List<Member> members = memberDAO.getAllMembers();

        for (Member member : members) {
            tableModel.addRow(new Object[]{
                member.getId(),
                member.getMemberCode(),
                member.getName(),
                member.getRollNumber(),
                member.getDepartment(),
                member.getPhone(),
                member.getEmail(),
                member.getStatus()
            });
        }
    }

    
private void searchMembers() {
    String query = searchField.getText().trim().toLowerCase();

    membersTable.clearSelection();

    if (query.isEmpty()) {
        loadMembersFromDatabase();
        return;
    }

    boolean found = false;

    for (int i = 0; i < tableModel.getRowCount(); i++) {
        // Columns: ID, Code, Name, Roll Number,
        // Department, Phone, Email, Status
        for (int col = 0; col <= 6; col++) {
            Object value = tableModel.getValueAt(i, col);

            if (value != null
                    && value.toString().toLowerCase().contains(query)) {

                membersTable.setRowSelectionInterval(i, i);
                membersTable.scrollRectToVisible(
                        membersTable.getCellRect(i, 0, true));

                found = true;
                break;
            }
        }

        if (found) {
            break;
        }
    }

    if (!found) {
        JOptionPane.showMessageDialog(
                this,
                "No member found matching: " + query,
                "Search Results",
                JOptionPane.INFORMATION_MESSAGE);
    }
}

}
