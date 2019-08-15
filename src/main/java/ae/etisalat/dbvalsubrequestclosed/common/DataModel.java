/**
 * 
 */
package ae.etisalat.dbvalsubrequestclosed.common;

/**
 * @author LSA-AS2444DXBDB05
 *
 */
public class DataModel {
	
	
	private String subRequestId;
	
	private String status;
	
	private String auditCode;
	

	public String getAuditCode() {
		return auditCode;
	}

	public void setAuditCode(String auditCode) {
		this.auditCode = auditCode;
	}

	public String getSubRequestId() {
		return subRequestId;
	}

	public void setSubRequestId(String subRequestId) {
		this.subRequestId = subRequestId;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	
}
