package ae.etisalat.dbvalsubrequestclosed.listener;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import ae.etisalat.dbvalsubrequestclosed.common.Constants;
import ae.etisalat.dbvalsubrequestclosed.common.ServerProperties;
import ae.etisalat.dbvalsubrequestclosed.component.DBListener;
import ae.etisalat.dbvalsubrequestclosed.model.LogData;
import io.zeebe.client.ZeebeClient;

@Component
public class SubrequestClosed extends DBListener {
	
	private static Logger LOGGER = Logger.getLogger(SubrequestClosed.class.getName());
	
	@Autowired
	private ServerProperties serverProperties;
	
	
	private static String NAME = "Subrequest closed";
    private static String ENV = "RC";
    private static String EXPECTED_VALUE = "90";
    private static String QUERY = "SELECT SUBREQUEST_ID, STATUS, CREATED_DATE, MODIFIED_DATE "
      								+ "FROM T_SOH_SUBREQUEST "
      								+ "WHERE MODIFIED_DATE > SYSDATE - INTERVAL '10' SECOND "
      								+ "ORDER BY SUBREQUEST_ID DESC";

	public SubrequestClosed() {
		super(NAME, ENV, EXPECTED_VALUE, QUERY);
	}

	@Override
    public String getKey(ResultSet p_rs) throws SQLException {
    	return "T_SOH_SUBREQUEST-STATUS-" + EXPECTED_VALUE + "-" + p_rs.getInt("SUBREQUEST_ID");
    }
    
	@Override
    public String getFoundValue(ResultSet p_rs) throws SQLException {
    	return p_rs.getString("STATUS");
    }
	
	@Override
    public boolean isExpectedFound(String p_expectedValue, String p_foundValue) throws SQLException {
    	return p_foundValue.equals(p_expectedValue);
    }
	
	@Override
    public void triggerNotification(LogData p_logData) {
        LOGGER.info("Notification: " + p_logData.toString());
    	SubReq_Closed_Event(p_logData.getKey());
    }
    
	@Scheduled(fixedRate=5000)
    public void checkSubrequestStatus() throws Exception {
		super.checkSubrequestStatus();
	}
	
	
	private void SubReq_Closed_Event(String subRequestId){
		
		final ZeebeClient client = ZeebeClient.newClientBuilder()
	            // change the contact point if needed
	            .brokerContactPoint(serverProperties.getBroker())
	            .build();
	
	        System.out.println("Connected...... Status 90");
	        client.newPublishMessageCommand()
		    		.messageName("MSG_SUBREQ_CLOSED")
		    		.correlationKey(subRequestId) 
		    		.timeToLive(Duration.ofMinutes(1))
		    		.send()
		    		.join();
	
	        System.out.println("Message sent - Name: " + "MSG_SUBREQ_CLOSED" + ", Key: " + subRequestId);
	        client.close();
	        System.out.println("Closed.");
	}
}
