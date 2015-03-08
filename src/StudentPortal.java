import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.sql.*; // JDBC stuff.
import java.io.*;  // Reading user input.

public class StudentPortal
{
    /* This is the driving engine of the program. It parses the
     * command-line arguments and calls the appropriate methods in
     * the other classes.
     *
     * You should edit this file in two ways:
     * 	1) 	Insert your database username and password (no @medic1!)
     *		in the proper places.
     *	2)	Implement the three functions getInformation, registerStudent
     *		and unregisterStudent.
     */
    public static void main(String[] args)
    {
        if (args.length == 1) {
            try {
                DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
                String url = "jdbc:oracle:thin:@db.student.chalmers.se:1521/kingu.ita.chalmers.se";
                String userName = "vtda357_020"; // Your username goes here!
                String password = "x8130d09"; // Your password goes here!
                Connection conn = DriverManager.getConnection(url,userName,password);

                //String student = args[0]; // This is the identifier for the student.
                BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
                String student = input.readLine();
                System.out.println("Welcome!");
                while(true) {
                    System.out.println("Please choose a mode of operation:");
                    System.out.print("? > ");
                    String mode = input.readLine();
                     if ((new String("information")).startsWith(mode.toLowerCase())) {
						/* Information mode */
                        getInformation(conn, student);
                    } else if ((new String("register")).startsWith(mode.toLowerCase())) {
						/* Register student mode */
                        System.out.print("Register for what course? > ");
                        String course = input.readLine();
                        registerStudent(conn, student, course);
                    } else if ((new String("unregister")).startsWith(mode.toLowerCase())) {
						/* Unregister student mode */
                        System.out.print("Unregister from what course? > ");
                        String course = input.readLine();
                        unregisterStudent(conn, student, course);
                    } else if ((new String("quit")).startsWith(mode.toLowerCase())) {
                        System.out.println("Goodbye!");
                        break;
                    } else {
                        System.out.println("Unknown argument, please choose either " +
                                "information, register, unregister or quit!");
                        continue;
                    }
                }
                conn.close();
            } catch (SQLException e) {
                System.err.println(e);
                System.exit(2);
            } catch (IOException e) {
                System.err.println(e);
                System.exit(2);
            }
        } else {
            System.err.println("Wrong number of arguments");
            System.exit(3);
        }
    }

    static void getInformation(Connection conn, String student)
    {
        try {
            Statement statement = conn.createStatement();
            ResultSet studentInformation = statement.executeQuery("SELECT * FROM StudentsFollowing WHERE id = " + student);
            studentInformation.next();

            StringBuilder builder = new StringBuilder();
            builder.append("Information for student " + student + "\n")
                    .append("------------------------------------------------------------\n")
                    .append("Name: " + studentInformation.getString("name") + "\n")
                    .append("Line: " + studentInformation.getString("programme") + "\n")
                    .append("Branch: " + studentInformation.getString("branch") + "\n\n");


            ResultSet finishedCourses = statement.executeQuery("SELECT * FROM FinishedCourses WHERE id = " + student);
            builder.append("Read courses (name (code), credits: grade):\n");
            while(finishedCourses.next()) {
                builder.append("\t" + finishedCourses.getString("coursename") + " (" + finishedCourses.getString("course") + "), "
                        + finishedCourses.getString("credit") + "p: " + finishedCourses.getString("grade") + "\n");
            }

            ResultSet registration = statement.executeQuery("SELECT * FROM Registrations WHERE student = " + student);
            builder.append("\nRegistered courses (name (code): status):\n");
            while(registration.next()) {
                String status = registration.getString("status");
                String code = registration.getString("course");
                builder.append("\t" + registration.getString("coursename") + " (" + code + "): " + status);


                if(status.equals("waiting")) {
                    ResultSet QueuePos = statement.executeQuery("SELECT priority FROM CourseQueuePosition WHERE student = " + student + " AND course = '" + code + "'");
                    QueuePos.next();
                    builder.append(" as nr " + QueuePos.getString("priority"));
                }
                builder.append("\n");
            }

            ResultSet pathToGraduation = statement.executeQuery("SELECT * FROM PathToGraduation WHERE id = " + student);
            pathToGraduation.next();
            builder.append("\nSeminar courses taken: " + pathToGraduation.getString("seminarcount") + "\n")
                    .append("Math credits taken: " + pathToGraduation.getString("mathcredit") + "\n")
                    .append("Research credits taken: " + pathToGraduation.getString("passedcredits") + "\n")
                    .append("Fullfils the requirements for graduation: " + pathToGraduation.getString("cangraduate") + "\n");

            System.out.println(builder);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void registerStudent(Connection conn, String student, String course)
    {
        System.out.println(student);
        System.out.println(course);
        try {
            Statement myStmt = conn.createStatement();
            myStmt.executeUpdate("INSERT INTO Registrations(course, student) VALUES ('ABC123', 12)");
            //myStmt.executeUpdate("INSERT INTO Registrations(course, student) VALUES('" + course + "'," + student + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }


        // Your implementation here
    }

    static void unregisterStudent(Connection conn, String student, String course)
    {
        // Your implementation here
    }
}