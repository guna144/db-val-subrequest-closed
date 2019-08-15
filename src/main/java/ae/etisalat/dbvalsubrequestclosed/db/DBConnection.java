/**
 * 
 */
package ae.etisalat.dbvalsubrequestclosed.db;

import java.sql.SQLException;
import java.util.Properties;
import oracle.jdbc.driver.OracleConnection;
import oracle.jdbc.driver.OracleDriver;

/**
 * @author gpalani
 *
 */
public class DBConnection {

	static final String USERNAME_CMDSRC = "Cmdsrc";
	static final String PASSWORD_CMDSRC = "windows1";
	static final String URL_CMDSRC = "jdbc:oracle:thin:@AU1190:1521:CMDSRC";
	
	static final String USERNAME_CBCMRCDL = "cbcm_customer";
	static final String PASSWORD_CBCMRCDL = "cbcm54321";
	static final String URL_CBCMRCDL = "jdbc:oracle:thin:@AU941:1527:CBCMRCDL";
	
	static final String USERNAME_COMSQA = "comsuat";
	static final String PASSWORD_COMSQA = "comsuat";
	static final String URL_COMSQA = "jdbc:oracle:thin:@au429:1521:COMSQA";
	
	static final String USERNAME_CBCMPD = "CBCM_CUSTOMER";
	static final String PASSWORD_CBCMPD = "cbcm54321";
	static final String URL_CBCMPD = "jdbc:oracle:thin:@au1372.etisalat.corp.ae:1521:CBCMPD";
	
	static final String RITRES_DEV_USERNAME = "rit_results";
	static final String RITRES_DEV_PASSWORD = "rit_results";
	static final String URL_RITRES = "jdbc:oracle:thin:@AS753-DX-BDB-05.etisalat.corp.ae:1521:RITRES";
	/**
	 * @param args
	 */
//	public static void main(String[] args) {
//		DBConnection oracleDCN = new DBConnection();
//		try {
//			oracleDCN.run();
//		}catch(Exception ex) {
//			ex.printStackTrace();
//		}
//	}
	
	
//	private void run() throws Exception {
//		OracleConnection conn1 = connect_CMDSRC();
//		OracleConnection conn2 = connect_CBCMRCDL();
//		OracleConnection conn3 = connect_COMSQA();
////		Properties prop = new Properties();
////		prop.setProperty(OracleConnection.DCN_NOTIFY_ROWIDS, "true");
//		System.out.println("Connected 1 : "+conn1);
//		System.out.println("Connected 2 : "+conn2);
//		System.out.println("Connected 3 : "+conn3);
//	}
	public static OracleConnection connect_CMDSRC() throws SQLException {
		OracleDriver dr = new OracleDriver();
		Properties prop = new Properties();
		prop.setProperty("user", DBConnection.USERNAME_CMDSRC);
		prop.setProperty("password", DBConnection.PASSWORD_CMDSRC);
		return (OracleConnection) dr.connect(DBConnection.URL_CMDSRC, prop);
	}
	
	public static OracleConnection connect_CBCMPD() throws SQLException {
		OracleDriver dr = new OracleDriver();
		Properties prop = new Properties();
		prop.setProperty("user", DBConnection.USERNAME_CBCMPD);
		prop.setProperty("password", DBConnection.PASSWORD_CBCMPD);
		return (OracleConnection) dr.connect(DBConnection.URL_CBCMPD, prop);
	}

	
	public static OracleConnection connect_CBCMRCDL() throws SQLException {
		OracleDriver dr = new OracleDriver();
		Properties prop = new Properties();
		prop.setProperty("user", DBConnection.USERNAME_CBCMRCDL);
		prop.setProperty("password", DBConnection.PASSWORD_CBCMRCDL);
		return (OracleConnection) dr.connect(DBConnection.URL_CBCMRCDL, prop);
	}
	
	public static OracleConnection connect_COMSQA() throws SQLException {
		OracleDriver dr = new OracleDriver();
		Properties prop = new Properties();
		prop.setProperty("user", DBConnection.USERNAME_COMSQA);
		prop.setProperty("password", DBConnection.PASSWORD_COMSQA);
		return (OracleConnection) dr.connect(DBConnection.URL_COMSQA, prop);
	}
	
	public static OracleConnection connect_RITRES() throws SQLException {
		OracleDriver dr = new OracleDriver();
		Properties prop = new Properties();
		prop.setProperty("user", DBConnection.RITRES_DEV_USERNAME);
		prop.setProperty("password", DBConnection.RITRES_DEV_PASSWORD);
		return (OracleConnection) dr.connect(DBConnection.URL_RITRES, prop);
	}
	

}
