package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class MembersDAO {
	private static MembersDAO instance = null;

	public static MembersDAO getInstance() {
		if (instance == null) {
			instance = new MembersDAO();
		}
		return instance;
	}

	java.util.Date starttijd = new java.util.Date();

	private MembersDAO() {
	}

	private Connection getConnection() throws Exception {
		Context ctx = new InitialContext();
		DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/oracle");
		return ds.getConnection();
	}

	public int insert(String id, String pwd, String name, String launch_date, String nickname, int role, String number,
			String mail, String zip, String address1, String address2) throws Exception {
		String sql = "insert into members values(members_seq.nextval,?,?,?,sysdate,?,?,?,?,?,?,?)";

		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql);) {
			pstat.setString(1, id);
			pstat.setString(2, pwd);
			pstat.setString(3, name);
			//pstat.setString(4, launch_date);
			pstat.setString(4, nickname);
			pstat.setInt(5, role);
			pstat.setString(6, mail);
			pstat.setString(7, number);
			pstat.setString(8, zip);
			pstat.setString(9, address1);
			pstat.setString(10, address2);

			int result = pstat.executeUpdate();
			con.setAutoCommit(false);
			con.close();
			return result;
		}
	}

	public boolean login(String id, String pwd) throws Exception { // 어떤 계정에 대한 실제로 로그인을 시도하는 함수, 인자값으로 ID와 Password를 받아
		// login을 판단함.
		String sql = "SELECT * FROM members WHERE member_id = ? and member_pw = ? "; // 실제로 DB에 입력될 명령어를 SQL 문장으로 만듬.
		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql);) {
			pstat.setString(1, id);
			pstat.setString(2, pwd);
			try (ResultSet rs = pstat.executeQuery();) {
				return rs.next();
			}
		}
	}

	public boolean isIDExist(String id) throws Exception {
		String sql = "select * from members where member_id = ?";

		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql);) {
			pstat.setString(1, id);
			try (ResultSet rs = pstat.executeQuery();) {
				return rs.next();
			}
		}
	}

}
