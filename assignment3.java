import java.sql.*;
import java.util.Scanner;

public class assignment3 {
    String url = "jdbc:postgresql://localhost:5432/assignment3";
    String user = "postgres";
    String password = "2002zeynep";

    // 1) READ. Retrieves and displays all records from the students table.
    public void getAllStudents() {
        String SQL = "SELECT student_id, first_name, last_name, email, enrollment_date " + "FROM students ORDER BY student_id";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement psm = connection.prepareStatement(SQL);
             ResultSet rs = psm.executeQuery()) {

            System.out.println("\n-- getAllStudents() --");
            System.out.printf("%-4s  %-12s %-12s %-26s %-10s%n",
                    "ID", "First Name", "Last Name", "Email", "Enrollment Date");
            while (rs.next()) {
                System.out.printf("%-4d  %-12s %-12s %-26s %-10s%n",
                        rs.getInt("student_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getDate("enrollment_date"));
            }
        } catch (SQLException ex) {
            System.out.println("getAllStudents error: " + ex.getMessage());
        }
    }

    // 2- CREATE. Inserting a new student record into the students table.
    public void addStudent(String first, String last, String email, Date enrollDate) {
        String SQL = "INSERT INTO students(first_name,last_name,email,enrollment_date) VALUES (?,?,?,?)";
        try (Connection c = DriverManager.getConnection(url, user, password);
             PreparedStatement psm = c.prepareStatement(SQL)) {

            psm.setString(1, first);
            psm.setString(2, last);
            psm.setString(3, email);
            psm.setDate(4, enrollDate);
            psm.executeUpdate();
            System.out.println("Student added successfully :).");

        } catch (SQLException ex) {
            System.out.println("oops addStudent error: " + ex.getMessage());
        }
    }

    // 3 - UPDATE. Updates the email address for a student with the specified student_id.
    public void updateStudentEmail(int studentId, String newEmail) {
        String SQL = "UPDATE students SET email=? WHERE student_id=?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement psm = conn.prepareStatement(SQL)) {

            psm.setString(1, newEmail);
            psm.setInt(2, studentId);
            psm.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    // 3 - DELETE. Deletes the record of the student with the specified student_id.
    public void deleteStudent(int studentId) {
        String SQL = "DELETE FROM students WHERE student_id=?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement psm = conn.prepareStatement(SQL)) {

            psm.setInt(1, studentId);
            psm.executeUpdate();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void main(String[] args) {
        assignment3 as3 = new assignment3();
        Scanner scanner = new Scanner(System.in);

        // 1- showing the current rows
        as3.getAllStudents();

        // 2 - add a student
        System.out.print("\nWould you like to add a new student? (yes/no): ");
        if (scanner.nextLine().equalsIgnoreCase("yes")) {
            as3.addStudent("Ruby", "Kaleli", "ruby.kaleli@example.com",
                    Date.valueOf("2025-11-01"));
            as3.getAllStudents();
        }

        // 3 - update the student's email
        System.out.print("\nUpdate a student's email? (yes/no): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
            Integer id = readId(scanner, "Enter student_id to update: ");
            if (id != null) {
                System.out.print("Enter new email: ");
                String newEmail = scanner.nextLine().trim();
                if (!newEmail.isEmpty()) {
                    as3.updateStudentEmail(id, newEmail);
                    as3.getAllStudents();
                } else {
                    System.out.println("No email provided. Skipping update.");
                }
            }
        }

        // 4 - delete a student
        System.out.print("\nDelete a student? (yes/no): ");
        if (scanner.nextLine().equalsIgnoreCase("yes")) {
            Integer id = readId(scanner, "Enter student_id to delete: ");
            if (id != null) {
                as3.deleteStudent(id);
                as3.getAllStudents();
            }
        } scanner.close();
    }

    // helper Function - reads a student ID
    private static Integer readId(Scanner sc, String what) {
        System.out.print(what);
        String line = sc.nextLine().trim();
        if (line.isEmpty()) return null;
        try {
            return Integer.parseInt(line);
        } catch (NumberFormatException e) {
            System.out.println("Invalid id. Skipping.");
            return null;
        }
        }
    }


