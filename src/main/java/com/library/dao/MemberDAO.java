
package com.library.dao;

import com.library.config.DBConnection;
import com.library.model.Member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {

   
    public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();

        String sql = "SELECT id, member_code, name, roll_number, "
                   + "department, phone, email, status "
                   + "FROM members ORDER BY id DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Member member = new Member();

                member.setId(resultSet.getInt("id"));
                member.setMemberCode(
                        resultSet.getString("member_code"));
                member.setName(resultSet.getString("name"));
                member.setRollNumber(
                        resultSet.getString("roll_number"));
                member.setDepartment(
                        resultSet.getString("department"));
                member.setPhone(resultSet.getString("phone"));
                member.setEmail(resultSet.getString("email"));
                member.setStatus(resultSet.getString("status"));

                members.add(member);
            }

        } catch (SQLException e) {
            System.err.println("GET MEMBERS ERROR:");
            e.printStackTrace();
        }

        return members;
    }

    public boolean addMember(Member member) {
        String sql = "INSERT INTO members "
                   + "(member_code, name, roll_number, department, "
                   + "phone, email, status) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, member.getMemberCode());
            statement.setString(2, member.getName());
            statement.setString(3, member.getRollNumber());
            statement.setString(4, member.getDepartment());
            statement.setString(5, member.getPhone());
            statement.setString(6, member.getEmail());
            statement.setString(7, member.getStatus());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("ADD MEMBER ERROR:");
            e.printStackTrace();
            return false;
        }
    }

   
    public boolean updateMember(Member member) {
        String sql = "UPDATE members SET member_code = ?, name = ?, "
                   + "roll_number = ?, department = ?, phone = ?, "
                   + "email = ?, status = ? WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, member.getMemberCode());
            statement.setString(2, member.getName());
            statement.setString(3, member.getRollNumber());
            statement.setString(4, member.getDepartment());
            statement.setString(5, member.getPhone());
            statement.setString(6, member.getEmail());
            statement.setString(7, member.getStatus());
            statement.setInt(8, member.getId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("UPDATE MEMBER ERROR:");
            e.printStackTrace();
            return false;
        }
    }

    
    public boolean deleteMember(int id) {
        String sql = "DELETE FROM members WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("DELETE MEMBER ERROR:");
            e.printStackTrace();
            return false;
        }
    }
}
