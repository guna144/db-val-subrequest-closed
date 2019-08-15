package ae.etisalat.dbvalsubrequestclosed.model;

import java.util.Date;

public class LogData {

	String type;
	String name;
    String env;
    String key;
    String expectedValue;
    String foundValue;
    Date lastCheckedDate;
    int notificationSent;

    public LogData() {
    }
    
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEnv() {
		return env;
	}

	public void setEnv(String env) {
		this.env = env;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getExpectedValue() {
		return expectedValue;
	}

	public void setExpectedValue(String expectedValue) {
		this.expectedValue = expectedValue;
	}

	public String getFoundValue() {
		return foundValue;
	}

	public void setFoundValue(String foundValue) {
		this.foundValue = foundValue;
	}

	public Date getLastCheckedDate() {
		return lastCheckedDate;
	}

	public void setLastCheckedDate(Date lastCheckedDate) {
		this.lastCheckedDate = lastCheckedDate;
	}

    public int getNotificationSent() {
		return notificationSent;
	}

	public void setNotificationSent(int notificationSent) {
		this.notificationSent = notificationSent;
	}

	public LogData(String p_type, String p_name, String p_env, String p_key, String p_expectedValue, String p_foundValue, Date p_lastCheckedDate, int p_triggered) {
        type = p_type;
		name = p_name;
        env = p_env;
        key = p_key;
        expectedValue = p_expectedValue;
        foundValue = p_foundValue;
        lastCheckedDate = p_lastCheckedDate;
        notificationSent = p_triggered;
   }

	@Override
	public String toString() {
		return "LogData [type=" + type + ", name=" + name + ", env=" + env + ", key=" + key + ", expectedValue="
				+ expectedValue + ", foundValue=" + foundValue + ", lastCheckedDate=" + lastCheckedDate
				+ ", notificationSent=" + notificationSent + "]";
	}

}
