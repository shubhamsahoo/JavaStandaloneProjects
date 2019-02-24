package com.nit.jdbc;
/**Job registration application
 * version since:1.0
 * Author:Team-S
 * Date-24/02/2019
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/*SQL> create table job_registration(sno number(4) primary key,
		  2  name varchar2(15),
		  3  email varchar2(35) not null,
		  4  mob number(10),
		  5  addr varchar2(30),
		  6  dob date,
		  7  qualification varchar2(10),
		  8  passoutyr number(4),
		  9  mark number(3),
		 10  currloc varchar2(20),
		 11  experience float(22));

		Table created.

		SQL> create sequence job_reg_no start with 1 increment by 1 ;

		Sequence created.*/

public class JobRegistraionApp {
	private static final String JOB_REG_INSERT = "INSERT INTO JOB_REGISTRATION VALUES(JOB_REG_NO.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String JOB_REG_SELECT = "SELECT SNO FROM JOB_REGISTRATION WHERE EMAIL = ?";
	@SuppressWarnings("unused")
	public static void register() {
		String name = null, email = null, dob = null, addr = null, qualifiaction = null, currLoc = null, option = null;;
		long mob = 0, milliSecound = 0;
		int passOutYear = 0, mark = 0, result = 0;
		float  yearOfExp = -1;
		Pattern p = null;
		Matcher m = null;
		SimpleDateFormat sdf = null;
		java.util.Date ddob = null;
		java.sql.Date sdob = null;
		//read inputs
		try (Scanner sc = new Scanner(System.in)) {
			System.out.print("Enter Candidate name::");
			name = sc.nextLine();
			while(email==null) {
				System.out.print("Enter email::");
				email = sc.next();
				p = Pattern.compile("[a-zA-Z0-9][a-zA-Z0-9_.]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+");
				m = p.matcher(email.trim());
				if (m.find() && m.group().equals(email))
					break;
				else {
					System.out.println("Invalid email enter a valid email");
					email = null;
				}
			}
			while(mob==0) {
				System.out.print("Enter Mob No::");
				mob = sc.nextLong();
				p = Pattern.compile("(0|91)?[7-9][0-9]{9}");
				m = p.matcher(mob+"");
				if (m.find() && m.group().equals(mob+"")) 
					break;
				else {
					System.out.println("Invalid Mob no enter a valid Mob no");
					mob = 0;
				}
			}
			System.out.print("Address::");
			sc.nextLine();
			addr = sc.nextLine();
			System.out.print("Enter DOB(dd-mm-yyyy)::");
			dob = sc.next();
			System.out.print("Qualification::");
			sc.nextLine();
			qualifiaction = sc.nextLine();
			System.out.print("Year of passed out::");
			passOutYear = sc.nextInt();
			System.out.print("Enter total percentage of mark::");
			mark = sc.nextInt();
			System.out.print("Enter current location::");
			sc.nextLine();
			currLoc = sc.nextLine();
			while (yearOfExp == -1) {
				System.out.print("Year of experience(0-5)::");
				yearOfExp = sc.nextFloat();
				if (yearOfExp >= 0 && yearOfExp <= 5)
					break;
				else {
					System.out.println("Experience Should ne in between 0 to 5");
					yearOfExp = -1;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		//load and register JDBC driver
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}
		catch (ClassNotFoundException cnf) {
			cnf.printStackTrace();
		}
		//convert date values
		sdf = new SimpleDateFormat("dd-MM-yyyy");
		try {
			ddob = sdf.parse(dob);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		milliSecound = ddob.getTime();
		sdob = new java.sql.Date(milliSecound);
		//establish the connection
		try (Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "scott", "tiger")) {
			//create PreparedStatement object
			try (PreparedStatement ps = con.prepareStatement(JOB_REG_INSERT)) {
				//set values to query param
				ps.setString(1, name);
				ps.setString(2, email);
				ps.setLong(3, mob);
				ps.setString(4, addr);
				ps.setDate(5, sdob);
				ps.setString(6, qualifiaction);
				ps.setInt(7, passOutYear);
				ps.setInt(8, mark);
				ps.setString(9, currLoc);
				ps.setFloat(10, yearOfExp);
				//execute Query
				result = ps.executeUpdate();
				if (result == 1) {
					System.out.println("Registration succeded");
					try (PreparedStatement ps1 = con.prepareStatement(JOB_REG_SELECT)) {
						ps1.setString(1, email);
						try (ResultSet rs = ps1.executeQuery()) {
							while (rs.next()) {
								System.out.println("Your Registration Number is::"+rs.getInt(1));
							}//while
						}//try
					}//try
				}//try
			}//try
		}//try
		catch (SQLException se) {
			se.printStackTrace();
		}
	}//main
}//class
