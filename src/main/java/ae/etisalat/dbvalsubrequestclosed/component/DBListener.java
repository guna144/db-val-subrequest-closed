package ae.etisalat.dbvalsubrequestclosed.component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;

import ae.etisalat.dbvalsubrequestclosed.common.ServerProperties;
import ae.etisalat.dbvalsubrequestclosed.db.DBConnection;
import ae.etisalat.dbvalsubrequestclosed.model.LogData;
import io.zeebe.client.ZeebeClient;
import oracle.jdbc.OracleConnection;

public class DBListener {

	private static Logger LOGGER = Logger.getLogger(DBListener.class.getName());
	
	@Autowired
	private ServerProperties serverProperties;
	
	static String LISTENER_TYPE = "DB";
	
    static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static String DB_DATE_FORMAT = "YYYY-MM-DD HH24:MI:SS";
    
    private String name;
    private String env;
    private String expectedValue;
    private String query;
    
    
    public DBListener(String p_name, String p_env, String p_expectedValue, String p_query) {
    	serverProperties = new ServerProperties();
    	name = p_name;
    	env = p_env;
    	expectedValue = p_expectedValue;
    	query = p_query;
    }
    
    
    public void checkSubrequestStatus() throws Exception {

    	  OracleConnection conn = null;
    	  Statement st = null;
    	  ResultSet rs = null;
    	  LogData logData = null;

          try {

                    conn = DBConnection.connect_CBCMRCDL();
                    st = conn.createStatement();

                    Date lastCheckedDate = new Date();
                    LOGGER.info("Check: " + lastCheckedDate);
                    
                    rs = st.executeQuery(query);

                    while (rs.next()) {
    
                          String key = getLogKey(rs);
                          String foundValue = getFoundValue(rs);

                          logData =  getLog(env, key);
                          
          
                          if(logData == null) {
                               logData = new LogData(LISTENER_TYPE, name, env, key, expectedValue, foundValue, lastCheckedDate, 0);
                               if(isExpectedFound(expectedValue, foundValue)) { // if value found as expected mark notification_sent and 
                                    logData.setNotificationSent(1);
                                    triggerNotification("MSG_SUBREQ_CLOSED", getNotificationKey(rs));
                               }
                               addToLog(logData);
                          
                          } else {
                               
                               if(logData.getNotificationSent() == 1) {
                                    continue;
                               }
                          
                               logData.setFoundValue(foundValue);
                               logData.setLastCheckedDate(lastCheckedDate);
                               

                               if(isExpectedFound(expectedValue, foundValue)) {
                                    logData.setNotificationSent(1);
                                    triggerNotification("MSG_SUBREQ_CLOSED", getNotificationKey(rs));
                               }
                               updateLog(logData);
                          }

                    }
               
               rs.close();
               st.close();
               
          } catch (Exception e) {
               throw e;
          } finally {
               if(conn != null) {
            	   conn.close();
               }
          }
    }
    
    
    public String getLogKey(ResultSet p_rs) throws SQLException {
    	return null;
    }
    
    public String getFoundValue(ResultSet p_rs) throws SQLException {
    	return null;
    }
    
    
    public boolean isExpectedFound(String p_expectedValue, String p_foundValue) throws SQLException {
    	return Boolean.FALSE;
    }
    
    public String getNotificationKey(ResultSet p_rs) throws SQLException {
    	return null;
    }
    

    private static int addToLog(LogData p_logData) throws Exception {
    	
	      OracleConnection conn = null;
	      Statement st = null;

          String query = "insert into l_log ( id, type, name, env, key, found_value, expected_value, last_checked_date, NOTIFICATION_SENT) "
          				+ " values ( seq_l_log.nextval, '#type#', '#name#', '#env#', '#key#', '#found_value#', '#expected_value#', to_date('#last_checked_date#', '#date_format#'), #notification_sent# )";
          
          query = query.replaceAll("#type#", p_logData.getType());
          query = query.replaceAll("#name#", p_logData.getName());
          query = query.replaceAll("#env#", p_logData.getEnv());
          query = query.replaceAll("#key#", p_logData.getKey());
          query = query.replaceAll("#found_value#", p_logData.getFoundValue());
          query = query.replaceAll("#expected_value#", p_logData.getExpectedValue());
          query = query.replaceAll("#last_checked_date#", SDF.format(p_logData.getLastCheckedDate()));
          query = query.replaceAll("#date_format#", DB_DATE_FORMAT);
          query = query.replaceAll("#notification_sent#",String.valueOf(p_logData.getNotificationSent()));
          
          LOGGER.info("RECORD ADDED: " + query);

          int cnt = -1;

          try {
              	conn = DBConnection.connect_RITRES();
              	st = conn.createStatement();
                cnt = st.executeUpdate(query);
               
                st.close();

          } catch (Exception e) {
               throw e;
          } finally {
               if(conn != null) {
            	   conn.close();
               }
          }

          return cnt;
    }


    private static int updateLog(LogData p_logData) throws Exception {
    	

	      OracleConnection conn = null;
	      Statement st = null;

          String query = "update l_log set "
        		  				+ " type = '#type#', "
                                + " name = '#name#', "
                                + " env = '#env#', "
                                + " key = '#key#', "      
                                + " expected_value = '#expected_value#', "
                                + " found_value = '#found_value#', "
                                + " last_checked_date = to_date( '#last_checked_date#', '#date_format#' ), "
                                + " NOTIFICATION_SENT = #notification_sent# "
                                + " where "
                                   + " env = '#env#' "
                               + " and key = '#key#'  ";
                          
          query = query.replaceAll("#type#", p_logData.getType());
          query = query.replaceAll("#name#", p_logData.getName());
          query = query.replaceAll("#env#", p_logData.getEnv());
          query = query.replaceAll("#key#", p_logData.getKey());
          query = query.replaceAll("#found_value#", p_logData.getFoundValue());
          query = query.replaceAll("#expected_value#", p_logData.getExpectedValue());
          query = query.replaceAll("#last_checked_date#", SDF.format(p_logData.getLastCheckedDate()));
          query = query.replaceAll("#date_format#", DB_DATE_FORMAT);
          query = query.replaceAll("#notification_sent#", String.valueOf(p_logData.getNotificationSent()));
          
          LOGGER.info("RECORD UPDATED: " + query);

          int cnt = -1;

          try {

            	conn = DBConnection.connect_RITRES();
            	st = conn.createStatement();
                cnt = st.executeUpdate(query);
                
               st.close();
          } catch (Exception e) {
               throw e;
          } finally {
               if(conn != null) {
            	   conn.close();
               }
          }

          return cnt;
    }


    private static LogData getLog(String p_env, String p_key) throws Exception {
          boolean found = Boolean.FALSE;
          LogData result = null;
          OracleConnection conn = null;
          Statement st = null;
          ResultSet rs = null;

          String query = "select * from l_log where env = '"+ p_env+"' and key = '"+p_key+"'";

//          LOGGER.info("LOG QUERY: " + query);


          try {

              conn = DBConnection.connect_RITRES();
              st = conn.createStatement();

              rs = st.executeQuery(query);

               while (rs.next()) {
                    found = Boolean.TRUE;
                    result = new LogData();
                    result.setType(rs.getString("TYPE"));
                    result.setName(rs.getString("NAME"));
                    result.setEnv(rs.getString("ENV"));
                    result.setKey(rs.getString("KEY"));
                    result.setFoundValue(rs.getString("FOUND_VALUE"));
                    result.setExpectedValue(rs.getString("EXPECTED_VALUE"));
                    result.setLastCheckedDate(rs.getDate("LAST_CHECKED_DATE"));
                    result.setNotificationSent(rs.getInt("NOTIFICATION_SENT"));
               }
               
               
               rs.close();
               st.close();

               if(!found) {
                    //LOGGER.info("Record not found in L_LOG table");
               } else {
                    //LOGGER.info("Record found in L_LOG table: " + result);
               }
          } catch (Exception e) {
               throw e;
          } finally {
               if(conn != null) {
            	   conn.close();
               }
          }

          return result;
    }
    
    
    public void triggerNotification(String p_messageName, String p_correlationKey) {
    	
    	SubReq_Opened_Event(p_correlationKey);
    	
    	final ZeebeClient client = ZeebeClient.newClientBuilder()
	            .brokerContactPoint(serverProperties.getBroker())
	            .build();
	
	        System.out.println("serverProperties.getBroker()  : " +serverProperties.getBroker()+ " Connected...... Status 90");
	        client.newPublishMessageCommand()
		    		.messageName(p_messageName)
		    		.correlationKey(p_correlationKey) 
		    		.timeToLive(Duration.ofMinutes(1))
		    		.send()
		    		.join();
	
	        System.out.println("Message sent - Name: " + p_messageName + ", Key: " + p_correlationKey);
	        client.close();
	        System.out.println("Closed.");
    }
    
    
    private void SubReq_Opened_Event(String subRequestId){
    	
    	final ZeebeClient client = ZeebeClient.newClientBuilder()
	            // change the contact point if needed
    			.brokerContactPoint(serverProperties.getBroker())
	            .build();

    	    System.out.println("serverProperties.getBroker()  : " +serverProperties.getBroker()+ " Connected...... Status 87 - new instance creation");
            client.newPublishMessageCommand()
            		.messageName("MSG_SUBREQ_OPENED_GSM_POSTPAID")
            		.correlationKey("")
            		.variables("{\"P_SUBREQ_ID\": "+subRequestId+" }")
            		.send()
            		.join();

            System.out.println("Message sent - Name: " + "MSG_SUBREQ_OPENED_GSM_POSTPAID" + ", Key: " + subRequestId);
	        client.close();
	        System.out.println("Closed.");
    }

}
