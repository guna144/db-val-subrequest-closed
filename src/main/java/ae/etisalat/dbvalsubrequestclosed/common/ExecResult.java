/**
 * 
 */
package ae.etisalat.dbvalsubrequestclosed.common;

import java.util.Date;

/**
 * @author LSA-AS2444DXBDB05
 *
 */
public class ExecResult {
	
	private String system;
	
	private String name;
	
	private String desc;
	
	private String execStatus;
	
	private String execLog;
		
	private Date execDate;
	
	private Object value;
	
	
	public ExecResult() {
		
	}
	
	public ExecResult(String p_system, String p_name, String p_desc) {
		system = p_system;
		name = p_name;
		desc = p_desc;
	}
	
	public void appendLog(String p_execStatus, String p_execLog) {
		
		if(execStatus == "FAILED" || p_execStatus == "FAILED") {
			execStatus = "FAILED";
		} else if (execStatus == "PASSED" || p_execStatus == "PASSED") {
			execStatus = "PASSED"; 
		} else {
			execStatus = p_execStatus;
		}
		if(execLog == null) {
			execLog = "<p> ########## SYSTEM: <br>" + system + "</p> ########## NAME: " + name + "\n ########## DESC: " + desc;
		} 
		execLog += "\n ########## " + p_execStatus + " - " + p_execLog;
		
		
	}
	
	public boolean isFailed() {
		if(execStatus == null || execStatus == "FAILED") {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getExecStatus() {
		return execStatus;
	}

	public void setExecStatus(String execStatus) {
		this.execStatus = execStatus;
	}

	public String getExecLog() {
		return execLog;
	}

	public void setExecLog(String execLog) {
		this.execLog = execLog;
	}

	public Date getExecDate() {
		return execDate;
	}

	public void setExecDate(Date execDate) {
		this.execDate = execDate;
	}
	

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "ExecResult [system=" + system + ", name=" + name + ", desc=" + desc + ", execStatus=" + execStatus
				+ ", execLog=" + execLog + ", execDate=" + execDate + ", value=" + value + "]";
	}

}
