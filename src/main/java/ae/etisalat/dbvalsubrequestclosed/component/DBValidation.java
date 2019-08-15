package ae.etisalat.dbvalsubrequestclosed.component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import ae.etisalat.dbvalsubrequestclosed.common.ExecException;
import ae.etisalat.dbvalsubrequestclosed.common.ExecResult;
import ae.etisalat.dbvalsubrequestclosed.db.DBConnection;
import ae.etisalat.dbvalsubrequestclosed.utility.AccountFormat;

public class DBValidation {

	
	/**
	 * VALIDATION: Network Provisioning Check
	 */
	public static ExecResult network_Provisioning_Check(String p_subRequestId) throws ExecException {
		
		String system = "COMS";
		String name = "Network Provisioning Check";
		String desc = "Validation is used to check the provisioning stages in destination_system ('LWM','IN','TIBCO'), each 1 record need to exist with status 'Y'";
		ExecResult execRes = new ExecResult(system, name, desc);
		
		Connection conn = null;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try {
			
			conn = DBConnection. connect_COMSPD();

			String strQuery = "SELECT F.LOG_ID, F.STAGE_ID , F.DESTINATION_SYSTEM, F.TRANSACTION_TYPE, F.TRANSACTION_DESCRIPTION, F.RESPONSE_DESCRIPTION, " + 
					" F.IS_SUCCESS, QQ.STAGE_ID, QQ.ACTION_ID, BB.DEMAND_ID, QQ.ORDER_INFORMATION, QQ.STATUS, QQ.COMS_CAPTURE_TIME FROM COMS_ORDER_STAGE_WORKFLOW F, " + 
					" COMS_ORDER_STAGE QQ, COMS_DEMAND BB WHERE F.STAGE_ID = QQ.STAGE_ID AND QQ.SUB_REQUEST_ID = '"+p_subRequestId+"' AND QQ.DEMAND_SEQ_ID = BB.DEMAND_SEQ_ID	AND F.DESTINATION_SYSTEM IN ('LWM','IN','TIBCO') " + 
					" ORDER BY F.LOG_ID, F.STAGE_ID";
			
			execRes.appendLog("INFO", "network_Provisioning_Check :: Query :: " + strQuery);
			
			pstmt = conn.prepareStatement(strQuery);			
			rs = pstmt.executeQuery();
			
			
			String logId = null, destinationSystem = null, isSUCCESS = null;
			int cntLWM = 0, cntIN = 0 , cntTIBCO = 0; 
			
			while(rs.next()) {
				logId = rs.getString("LOG_ID");
				destinationSystem = rs.getString("DESTINATION_SYSTEM");
				isSUCCESS =  rs.getString("IS_SUCCESS");
				
				if(isSUCCESS.equalsIgnoreCase("Y")) {
					execRes.appendLog("PASSED", "Rcord LOG_ID: " + logId + "- Status found for " + destinationSystem + " system as expected, expected: 'Y', found: " + isSUCCESS);
				} else {
					execRes.appendLog("PASSED", "Status not found for " + destinationSystem + " system as expected, expected: 'Y', found: " + isSUCCESS);
				}
				
				if(destinationSystem != null && isSUCCESS != null) {
					if(destinationSystem.equals("LWM")) {
						cntLWM += 1;
					}else if(destinationSystem.equals("IN")) {
						cntIN += 1;
					}else if(destinationSystem.equals("TIBCO")) {
						cntTIBCO += 1;
					}
					
				}
			}
			
			rs.close();
			pstmt.close();
			
			if(cntLWM > 0) {
				execRes.appendLog("PASSED", "Record for LWM exist, num of found records: " + cntLWM);
			} else {
				execRes.appendLog("PASSED", "Record for LWM does not exist, num of found records: " + cntLWM);
			}
			if(cntIN > 0) {
				execRes.appendLog("PASSED", "Record for IN exist, num of found records: " + cntLWM);
			} else {
				execRes.appendLog("PASSED", "Record for IN does not exist, num of found records: " + cntLWM);
			}
			if(cntTIBCO > 0) {
				execRes.appendLog("PASSED", "Record for TIBCO exist, num of found records: " + cntLWM);
			} else {
				execRes.appendLog("PASSED", "Record for TIBCO does not exist, num of found records: " + cntLWM);
			}
			
		} catch (Exception e) {
			System.out.println(execRes.toString());
			e.printStackTrace();
			throw new ExecException(execRes, e);
		}finally {
			try {
				if(conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				System.out.println(execRes.toString());
				e.printStackTrace();
				throw new ExecException(execRes, e);
			}
		}
		
		return execRes;
	}
	
	
	/**
	 *  VALIDATION:
	 *  ---------- 
	 *  Resources allocation check, 
	 *  To check if MSISDN done through CSS
	 * 
	 * **/
	public static ExecResult resource_Allocation_IMSI_Check(String p_MSISDN) throws ExecException {
		
		String system = "COMS";
		String name = "Resource Allocation Check";
		String desc = "Validation is used to check if MSISDN done through CSS, 1 record need to exist with status 1";
		ExecResult execRes = new ExecResult(system, name, desc);
		
		String status =  null;
		Connection conn = null;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		
		try {
			
			conn = DBConnection.connect_CBCMPD();

			String strQuery1 = "SELECT * FROM NUMBERS WHERE MSISDN = '" + p_MSISDN + "'";
			execRes.appendLog("INFO", "resource_Allocation_Check IMSI :: Query :: " + strQuery1);

			/******************* Post closure MSISDN Status should be 1 *******************/
			pstmt = conn.prepareStatement(strQuery1);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				status = rs.getString("STATUS");
			}
			if (status != null && status.equals("1")) {
				execRes.appendLog("PASSED", "Status found as expected, found 1");
			} else {
				execRes.appendLog("FAILED", "Status not found as expected, found " + status + ", expected 1");
			}
			
			rs.close();
			pstmt.close();

		} catch (Exception e) {
			System.out.println(execRes.toString());
			e.printStackTrace();
			throw new ExecException(execRes, e);
		}finally {
			try {
				if(conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				System.out.println(execRes.toString());
				e.printStackTrace();
				throw new ExecException(execRes, e);
			}
		}
		
		return execRes;
	}
	
	
	/**
	 *  VALIDATION:
	 *  ---------- 
	 *  Resources allocation check, 
	 *  To check if SIM card allocation done through CSS
	 * 
	 * **/
	public static ExecResult resource_Allocation_SIM_Check(String p_SERIAL1) throws ExecException {
		
		String system = "COMS";
		String name = "Resource Allocation Check";
		String desc = "Validation is used to check if SIM card allocation done through CSS, 1 record need to exist with status 11";
		ExecResult execRes = new ExecResult(system, name, desc);
		
		String status =  null;
		Connection conn = null;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		
		try {
			
			conn = DBConnection.connect_CBCMPD();

			String strQuery2 = "SELECT * FROM PRODUCT WHERE SERIAL1 = '"+p_SERIAL1+"'";
			execRes.appendLog("INFO", "resource_Allocation_Check SIM :: Query :: "+strQuery2);

			/*******************
			 * Post Closure PRODUCT status should be 11
			 *******************/

			pstmt = conn.prepareStatement(strQuery2);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				status = rs.getString("STATUS");
			}

			if (status != null && status.equals("11")) {
				execRes.appendLog("PASSED", "Status found as expected, found 11");
			} else {
				execRes.appendLog("FAILED", "Status not found as expected, found " + status + ", expected 11");
			}

			rs.close();
			pstmt.close();
			
			
		} catch (Exception e) {
			System.out.println(execRes.toString());
			e.printStackTrace();
			throw new ExecException(execRes, e);
		}finally {
			try {
				if(conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				System.out.println(execRes.toString());
				e.printStackTrace();
				throw new ExecException(execRes, e);
			}
		}
		
		return execRes;
	}
	
	
	/**
	 * VALIDATION: srRequest Closure Check
	 */
	public static ExecResult srRequest_Closure_Check(String p_requestId) throws ExecException {
		
		String system = "COMS";
		String name = "Request Closure Check";
		String desc = "Validation is used to check SR Request Closure Check, 1 record need to exist with status 489";
		ExecResult execRes = new ExecResult(system, name, desc);
		
		String status =  null;
		Connection conn = null;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;

		try {
			
			conn = DBConnection.connect_CBCMPD();

			String strQuery = "SELECT * FROM T_SOH_REQUEST WHERE REQUEST_ID = '"+ p_requestId +"' AND STATUS = '489'";
			
			execRes.appendLog("INFO", "srRequest_Closure_Check :: Query :: "+strQuery);
			
			pstmt = conn.prepareStatement(strQuery);			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				status = rs.getString("STATUS");
			}
			
			rs.close();
			pstmt.close();
			
			if(status != null && status.equals("489")) {
				execRes.appendLog("PASSED", "Status found as expected, found 489");
			} else {
				execRes.appendLog("FAILED", "Status not found as expected, found " + status + ", expected 489");
			}
			
		} catch (Exception e) {
			System.out.println(execRes.toString());
			e.printStackTrace();
			throw new ExecException(execRes, e);
		}finally {
			try {
				if(conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				System.out.println(execRes.toString());
				e.printStackTrace();
				throw new ExecException(execRes, e);
			}
		}
		
		return execRes;
	}
	
	
	/**
	 * VALIDATION: srSub Request Closure Check
	 */
	public static ExecResult srSub_Request_Closure_Check(String p_subRequestId) throws ExecException {
		
		String system = "COMS";
		String name = "Sub Request Closure Check";
		String desc = "Validation is used to check SR Sub Request Closure Check, 1 record need to exist with status 90";
		ExecResult execRes = new ExecResult(system, name, desc);
		
		String status =  null;
		Connection conn = null;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;

		try {
			
			conn = DBConnection.connect_CBCMPD();

			String strQuery = "select * from t_soh_subrequest where subrequest_id = '"+ p_subRequestId +"' AND STATUS = '90'";
			
			execRes.appendLog("INFO", "srSub_Request_Closure_Check :: Query :: "+strQuery);
			
			pstmt = conn.prepareStatement(strQuery);			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				status = rs.getString("STATUS");
			}
			
			rs.close();
			pstmt.close();
			
			if(status != null && status.equals("90")) {
				execRes.appendLog("PASSED", "Status found as expected, found 90");
			} else {
				execRes.appendLog("FAILED", "Status not found as expected, found " + status + ", expected 90");
			}
			
		} catch (Exception e) {
			System.out.println(execRes.toString());
			e.printStackTrace();
			throw new ExecException(execRes, e);
		}finally {
			try {
				if(conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				System.out.println(execRes.toString());
				e.printStackTrace();
				throw new ExecException(execRes, e);
			}
		}
		
		return execRes;
	}
	
	
	/**
	 * VALIDATION: CBCM BSCS Message Check
	 */
	public static ExecResult cbcm_BSCS_Message_Check(String p_subRequestId) throws ExecException {
		String system = "BSCS";
		String name = "CBCM BSCS Message Check";
		String desc = "Validation is used to check BSCS processing, 3 records need to exist with status PROCESSED";
		ExecResult execRes = new ExecResult(system, name, desc);
		
		String status =  null;
		Connection conn = null;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;

		try {
			
			conn = DBConnection.connect_CBCMPD();

			String strQuery = "SELECT M.MSG_ID,M.CORRELATION_ID, SR1.ACCOUNT_ID,SR1.NEW_ACCOUNT_ID,I.SUBREQUEST_ID, PD.TARGET_SYSTEM, PD.NAME, " +
						" DECODE(M.PROCESS_STATUS,0,'TO_BE_PROCESSED',1,'PROCESSED',2,'ERROR',3,'WAITING_FOR_REPLY',4,'BLOCKED') STATUS, " + 
						" M.REQUEST_XML, M.RESPONSE_XML, M.PROCESS_ERROR, M.SEQUENCE,SR1.SUB_REQUEST_TYPE_ID,SR1.ACCOUNT_NUMBER, M.MODIFIED_DATE FROM T_SOH_BSCS_INTERFACE I, " + 
						" T_CBCM_BSCS_PROCESS_DEFINITION PD, T_SOH_BSCS_PROCESS_MAP PM, T_CBCM_BSCS_MESSAGES M, T_SOH_SUBREQUEST SR1, T_SOH_SUBREQUEST SR2 "+
						" WHERE SR1.SUBREQUEST_ID = '"+ p_subRequestId +"' AND SR2.REQUEST_ID = SR1.REQUEST_ID AND I.SUBREQUEST_ID = SR2.SUBREQUEST_ID AND "+
						" M.INTERFACE_ID = I.INTERFACE_ID AND M.MAP_ID = PM.PROCESS_MAP_ID AND PD.PROCESS_ID = PM.PROCESS_ID ORDER BY M.MSG_ID ";
			
			execRes.appendLog("INFO", "srSub_Request_Closure_Check :: Query :: "+strQuery);
			
			pstmt = conn.prepareStatement(strQuery);			
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			while(rs.next()) {
				cnt ++;
				status = rs.getString("STATUS");
				String msgId = rs.getString("MSG_ID");
				
				if(status != null && status.equals("PROCESSED")) {
					execRes.appendLog("PASSED", "MSG ID: " + msgId + ". Status found as expected, found PROCESSED");
				} else {
					execRes.appendLog("FAILED", "MSG ID: " + msgId + ". Status not found as expected, found " + status + ", expected PROCESSED");
				}
			}
			
			rs.close();
			pstmt.close();
			
			if(cnt > 0) {
				if(cnt == 3) {
					execRes.appendLog("PASSED", "Expected number of records: 3, found: " + cnt);
				} else {
					execRes.appendLog("FAILED", "Expected number of records: 3, found: " + cnt);
				}
			} else {
				execRes.appendLog("FAILED", "No records found!");
			}
			
			
		} catch (Exception e) {
			System.out.println(execRes.toString());
			e.printStackTrace();
			throw new ExecException(execRes, e);
		}finally {
			try {
				if(conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				System.out.println(execRes.toString());
				e.printStackTrace();
				throw new ExecException(execRes, e);
			}
		}
		
		return execRes;
	}
	
	/**
	 * VALIDATION: Customer Creation Check
	 */
	public static ExecResult customerCreationCheck(String p_accountId) throws ExecException {
		String system = "BSCS";
		String name = "Customer Creation Check";
		String desc = "Validation is used to check if customer is created in BSCS, if yes, return customer id";
		ExecResult execRes = new ExecResult(system, name, desc);
		
		String customerId =  null;
		Connection conn = null;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;

		try {
			
			conn = DBConnection.connect_CBCMPD();

			String strQuery = "SELECT CUSTOMER_ID, CUSTOMER_ID_HIGH, CSACTIVATED, CSLEVEL FROM S_CUSTOMER_ALL WHERE CUSTNUM = '"+p_accountId+"' ";
			
			execRes.appendLog("INFO", "customerCreationCheck :: Query :: "+strQuery);
			
			pstmt = conn.prepareStatement(strQuery);			
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			while(rs.next()) {
				cnt ++;
				customerId = rs.getString("CUSTOMER_ID");
				
				if(customerId == null ) {
					execRes.appendLog("FAILED", "CUSTOMER ID: " + customerId + " No custumer found in CUSTOMER_ALL table for account id: "+p_accountId);
				} else {
					execRes.appendLog("PASSED", "CUSTOMER ID: " + customerId + " Custumer found in CUSTOMER_ALL table for account id: "+p_accountId);
				}
			}
			
			rs.close();
			pstmt.close();
			
			if(cnt > 1) {
				execRes.appendLog("FAILED", "Multiple rows found in CUSTOMER_ALL table for account id: " + p_accountId);
			} 			
			
		} catch (Exception e) {
			System.out.println(execRes.toString());
			e.printStackTrace();
			throw new ExecException(execRes, e);
		}finally {
			try {
				if(conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				System.out.println(execRes.toString());
				e.printStackTrace();
				throw new ExecException(execRes, e);
			}
		}
		
		return execRes;
	}
	
	
	/**
	 * VALIDATION: Contract Creation Check
	 */
	public static ExecResult contractCreationCheck(String p_accountId) throws ExecException {
		String system = "BSCS";
		String name = "Contract Creation Check";
		String desc = "Validation is used to check if customer is created in BSCS, and contract status is 'a', if yes, return tmcode";
		ExecResult execRes = new ExecResult(system, name, desc);
		
		String status =  null;
		String tmCODE =  null;
		Connection conn = null;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;

		try {
			
			conn = DBConnection.connect_CBCMPD();

			String strQuery = "SELECT CO_ID, CO_CODE, CH_STATUS, TMCODE FROM S_CONTRACT_ALL WHERE CO_CODE= 'SA-"+p_accountId+"' ";
			
			execRes.appendLog("INFO", "contractCreationCheck :: Query :: "+strQuery);
			
			pstmt = conn.prepareStatement(strQuery);			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				status = rs.getString("CH_STATUS");
				tmCODE = rs.getString("TMCODE");
				
				if(status != null && !status.equals("a")) {
					execRes.appendLog("FAILED", "Contract with co_code: SA-: " + p_accountId + " found in BSCS but it is not active, contract status: "+status);
				} else {
					execRes.appendLog("PASSED", "Contract with co_code: SA-: " + p_accountId + " found in BSCS, contract status: "+status);
				}
			}
			
			rs.close();
			pstmt.close();
			
			if(tmCODE == null) {
				execRes.appendLog("FAILED", "Contract with co_code: SA-" + p_accountId + "not found in BSCS");
			} 			
			
		} catch (Exception e) {
			System.out.println(execRes.toString());
			e.printStackTrace();
			throw new ExecException(execRes, e);
		}finally {
			try {
				if(conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				System.out.println(execRes.toString());
				e.printStackTrace();
				throw new ExecException(execRes, e);
			}
		}
		
		return execRes;
	}
	
	/**
	 * VALIDATION: Directory number
	 */
	public static ExecResult directoryNumberCheck(String p_account_no, String p_subRequestId) throws ExecException {
		String system = "BSCS";
		String name = "Directory Number Check";
		String desc = "Validation is used to check direcory number for contract";
		ExecResult execRes = new ExecResult(system, name, desc);
		
		String status =  null;
		Connection conn = null;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;

		try {
			
			conn = DBConnection.connect_CBCMPD();

			String accountNum = AccountFormat.getLong(p_account_no);
			
			String strQuery2 = "select dn.dn_id, dn.dn_num, dn.dn_status, dn.dn_status_mod_date, sr.created_date from DIRECTORY_NUMBER@custrep_bscssby.etisalat.corp.ae dn , t_soh_subrequest sr " + 
			"where dn.dn_num = '" + accountNum + "' and sr.subrequest_id = '" + p_subRequestId + "'";
			execRes.appendLog("INFO", "Directory number :: Query :: "+strQuery2);
			
			pstmt = conn.prepareStatement(strQuery2);			
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			while(rs.next()) {
				cnt ++;
				status = rs.getString("DN_STATUS");
				Date directoryDate = rs.getDate("DN_STATUS_MOD_DATE");
				Date subreqDate = rs.getDate("CREATED_DATE");
				
				if(status.equals("a")) {
					execRes.appendLog("PASSED", "Found status is as expected, expected 'a', found: " +  status);
				} else {
					execRes.appendLog("FAILED", "Found status is not as expected, expected 'a', found: " +  status);
				}
				
				if(directoryDate.compareTo(subreqDate) > 0) {
					execRes.appendLog("PASSED", "Directory modification date is same as subrequest creation date. Subrequest creation date: " + subreqDate + ", directory modification date " +  directoryDate);
				} else {
					execRes.appendLog("PASSED", "Directory modification date is not same as subrequest creation date. Subrequest creation date: " + subreqDate + ", directory modification date " +  directoryDate);
				}
			}
			
			rs.close();
			pstmt.close();
			
			if(cnt > 0) {
				execRes.appendLog("PASSED", "Number of records found: " + cnt);
			}else {
				execRes.appendLog("FAILED", "Record not found!");
			}
			
		} catch (Exception e) {
			System.out.println(execRes.toString());
			e.printStackTrace();
			throw new ExecException(execRes, e);
		}finally {
			try {
				if(conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				System.out.println(execRes.toString());
				e.printStackTrace();
				throw new ExecException(execRes, e);
			}
		}
		
		return execRes;
	}
	

	/**
	 * VALIDATION: REP SIM Type Check
	 */
	public static ExecResult simTypCheck(String p_subRequestId) throws ExecException {
		String system = "General";
		String name = "REP SIM Type Check";
		String desc = "Validation is used to check the type of SIM card replacement";
		ExecResult execRes = new ExecResult(system, name, desc);
		
		Connection conn = null;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;

		try {
			
			conn = DBConnection.connect_CBCMPD();

			String strQuery = "SELECT * FROM T_SOH_GSM_CLONE_SIM_DTLS WHERE SUBREQUEST_ID = '" + p_subRequestId + "'";
			
			execRes.appendLog("INFO", "REP SIM Card Check :: Query :: "+strQuery);
			
			pstmt = conn.prepareStatement(strQuery);			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				execRes.appendLog("PASSED", "Record found ");
			} else {
				execRes.appendLog("FAILED", "Record not found, this is a normal SIM card replacement");
			}

			rs.close();
			pstmt.close();
			

		} catch (Exception e) {
			System.out.println(execRes.toString());
			e.printStackTrace();
			throw new ExecException(execRes, e);
		}finally {
			try {
				if(conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				System.out.println(execRes.toString());
				e.printStackTrace();
				throw new ExecException(execRes, e);
			}
		}
		
		return execRes;
	}
	
	/**
	 * VALIDATION: Check status of SIM and IMSI in BSC
	 */
	public static ExecResult simAndIMSIStatusCheck(String p_accountId, String p_subRequestId) throws ExecException {
		String system = "BSCS";
		String name = "Check status of SIM and IMSI in BSCS";
		String desc = "Validation is used to check the status of the SIM and IMSI in BSCS";
		ExecResult execRes = new ExecResult(system, name, desc);
		
		String status = null;
		String deActivDate = null;
		String sim = null;
		String iccid = null;
		Connection conn = null;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		PreparedStatement pstmt1 = null; 
		ResultSet rs1 = null;

		try {
			
			conn = DBConnection.connect_CBCMPD();

			String strQuery = "SELECT CO_ID,CD_SM_NUM SIM,(SELECT PORT_NUM FROM PORT@DM_CUST_BSCSPRD.ETISALAT.CORP.AE  WHERE PORT_ID=CD.PORT_ID) IMSI,CD_STATUS, "
							+ " CD.CD_ACTIV_DATE,CD_DEACTIV_DATE FROM CONTR_DEVICES@DM_CUST_BSCSPRD.ETISALAT.CORP.AE CD " 
							+ " WHERE CO_ID IN(SELECT CO_ID FROM CONTRACT_ALL@DM_CUST_BSCSPRD.ETISALAT.CORP.AE WHERE CO_CODE='SA-"+ p_accountId +"') ORDER BY CD_ACTIV_DATE";
			
			execRes.appendLog("INFO", "Check status of SIM and IMSI in BSCS :: Query :: "+strQuery);
			
			pstmt = conn.prepareStatement(strQuery);			
			rs = pstmt.executeQuery();
			
			
			int countO = 0;
			int countRWithoutDeacDate = 0;
			while(rs.next()) {
				
				status = rs.getString("CD_STATUS");
				deActivDate = rs.getString("CD_DEACTIV_DATE");
				sim = rs.getString("SIM");
			
				if(status != null && status.equals("O")) {
					countO ++;
					if(deActivDate == null) {
						execRes.appendLog("FAILED", "Found record with status O and null deactivation date");
					} else {
						execRes.appendLog("PASSED", "Found record with status O and filled deactivation date");
					}
					
				} else if(status != null && status.equals("R")) {
					if(deActivDate == null) {
						countRWithoutDeacDate ++;
					
						strQuery = "SELECT ICCID_NO FROM T_SOH_GSM_RESOURCE_POOL WHERE (EFFECTIVE_TILL_DATE IS NULL OR EFFECTIVE_TILL_DATE > SYSDATE) AND SUBREQUEST_ID = '"+p_subRequestId+"'";
						
						execRes.appendLog("INFO", "Fetch serial number :: Query :: "+strQuery);
						
						pstmt1 = conn.prepareStatement(strQuery);			
						rs1 = pstmt1.executeQuery();
						
						while(rs1.next()) {
							iccid = rs1.getString("ICCID_NO");
							
							if(sim != null && iccid != null && iccid.equals(sim)) {
								execRes.appendLog("PASSED", "Reactivated SIM number is equal to new SIM number. Reactivated SIM: "+sim +", new SIM: "+iccid);
							} else {
								execRes.appendLog("FAILED", "Reactivated SIM number is not equal to new SIM number. Reactivated SIM: "+sim +", new SIM: "+iccid);
							}
						}
					}

				} else {
					execRes.appendLog("FAILED", "Record found with different status than O and R, found status: "+status);
				}
			}
			
			if(countO == 1) {
				execRes.appendLog("PASSED", countO + " records found with status O");
			}else {
				execRes.appendLog("FAILED", countO + " records found with status O");
			}

			if(countRWithoutDeacDate == 1) {
				execRes.appendLog("PASSED", countRWithoutDeacDate + " record found with status R without deactivation date");
			} else {
				execRes.appendLog("FAILED", countRWithoutDeacDate + " records found with status R without deactivation date");
			}
			
			rs.close();
			pstmt.close();

			rs1.close();
			pstmt1.close();

		} catch (Exception e) {
			System.out.println(execRes.toString());
			e.printStackTrace();
			throw new ExecException(execRes, e);
		}finally {
			try {
				if(conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				System.out.println(execRes.toString());
				e.printStackTrace();
				throw new ExecException(execRes, e);
			}
		}
		
		return execRes;
	}
}
