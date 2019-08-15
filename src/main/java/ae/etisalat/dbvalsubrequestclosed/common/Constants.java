package ae.etisalat.dbvalsubrequestclosed.common;

public class Constants {

//	public static String BROKER = "127.0.0.1:26500";
	
	public static final String JOB_STATUS_CHECK = "status-check";
	
	public static final String JOB_PROVISIONING_SYSTEM_CHECK = "check-provisoing-system";
	
	public static final String JOB_LWM_CHECK = "check-LWM";
	
	public static final String JOB_COMS_CHECK = "check-COMS";
	
	public static final String JOB_SYSTEM_CHECK = "check-SYSTEM";
	
	public static final String JOB_RTF_CHECK = "check-RTF";
	
	public static final String JOB_CMDS_CHECK = "check-CMDS";
	
	public static final String JOB_REQUEST_CREATION_CHECK = "check-request-creation";
	
	public static final String JOB_SUB_REQUEST_CREATION_CHECK = "check-sub-request-creation";
	
	public static final String JOB_NETWORK_PROVISIONING_CHECK = "check-network-provisioning";
	
	public static final String JOB_RESOURCE_ALLOCATION_SIM_CHECK = "check-resource-allocation-sim";
	
	public static final String JOB_RESOURCE_ALLOCATION_IMSI_CHECK = "check-resource-allocation-imsi";
	
	public static final String JOB_SR_REQUEST_CLOSURE_CHECK = "check-sr-request-closure";
	
	public static final String JOB_SR_SUB_REQUEST_CLOSURE_CHECK = "check-sr-sub-request-closure";
	
	public static final String JOB_CBCM_BSCS_MESSAGE_CHECK = "check-CBCM-BSCS-Message";
	
	public static final String JOB_CUSTOMER_CREATED_CHECK = "customer-created";
	
	public static final String JOB_CONTRACT_CREATED_CHECK = "contract-created";
	
	public static final String JOB_DIRECTORY_NUMBER_CHECK = "directory-number";
	
	public static final String JOB_REP_SIM_TYPE_CHECK = "sim-type-check";
	
	public static final String JOB_SIM_IMSI_STATUS_CHECK = "sim-imsi-status-check";
	
	public static final String JOB_INCIDENT = "check-Incident";
	
	public static String QUERY_STATUS_CHECK_1 = "Select * from t_soh_subrequest where rowid = ?"; 
	
	public static String QUERY_STATUS_CHECK = "Select status from t_soh_subrequest where subrequest_id = ?"; 

	public static String QUERY_PROVISIONING_SYSTEM_CHECK = "Select modified_user_id from t_soh_subrequest where subrequest_id = ?";
	
	public static String QUERY_LWM_CHECK = "Select * from test_account bk where bk.account_no = ?"; 

	public static String QUERY_RTF_CHECK = "select * from test_account bk where bk.account_no = ?";

	public static String QUERY_CMDS_CHECK = "select * from test_account bk where bk.account_no = ?";
	
	public static String QUERY_SYSTEM_CHECK = "select * from test_account bk where bk.account_no = ?";

	public static String QUERY_COMS_CHECK =	"select * from coms_api_execution_whitelist bk where bk.account_number = ?"; 

	public static String QUERY_REQUEST_CREATION_CHECK = "select * from t_soh_request where request_id = ?";

	public static String QUERY_SUB_REQUEST_CREATION_CHECK ="select * from t_soh_subrequest where subrequest_id = ?";

	public static String QUERY_NETWORK = "select f.log_id, f.stage_id , f.destination_system, f.transaction_type, f.transaction_description, f.response_description, "
			+ "f.is_success, qq.stage_id, qq.action_id, bb.demand_id, qq.order_information, qq.status, qq.coms_capture_time from COMS_ORDER_STAGE_WORKFLOW f, "
			+ "COMS_ORDER_STAGE qq, COMS_DEMAND bb where f.stage_id = qq.stage_id and qq.sub_request_id = ? and qq.demand_seq_id = bb.demand_seq_id	and f.destination_system in ('LWM','IN','TIBCO')"
			+ "	order by f.log_id, f.stage_id";

	public static String QUERY_RESOURCE_ALLOCATION_CHECK1 ="select * from NUMBERS where MSISDN = ?"; 

	public static String QUERY_RESOURCE_ALLOCATION_CHECK2 ="select * from PRODUCT where SERIAL1 = ?";

	public static String QUERY_SR_REQUEST_CLOSURE_CHECK = "select * from t_soh_request where request_id = ? and Status = '489'";

	public static String QUERY_SR_SUB_REQUEST_CLOSURE_CHECK = "select * from t_soh_subrequest where subrequest_id = ? and Status = '90'";

	public static String QUERY_CBCM_BSCS_MESSAGE_CHECK = "select m.msg_id,m.correlation_id, sr1.account_id,sr1.new_account_id,i.subrequest_id, "
			+ "	pd.target_system, pd.name, decode(m.process_status,0,'TO_BE_PROCESSED',1,'PROCESSED',2,'ERROR',3,'WAITING_FOR_REPLY',4,'BLOCKED') status,"
			+ " m.request_xml, m.response_xml, m.process_error, m.sequence,sr1.sub_request_type_id,sr1.account_number, m.modified_date from t_soh_bscs_interface i,"
			+ "	t_cbcm_bscs_process_definition pd, t_soh_bscs_process_map pm, t_cbcm_bscs_messages m, t_soh_subrequest sr1, t_soh_subrequest sr2 where sr1.subrequest_id = ? and sr2.request_id = sr1.request_id"
			+ " and i.subrequest_id = sr2.subrequest_id and m.interface_id = i.interface_id and m.map_id = pm.process_map_id and pd.process_id = pm.process_id order by m.msg_id";

	public static String QUERY_FOR_PROCESS_CHECK ="  select * from t_soh_sub_request_audit " + 
			"  where sub_request_id in (  select subrequest_id from t_soh_subrequest where modified_date >= ?) " + 
			"  and modified_date >= ? " +
			"  order by sub_request_id asc , modified_date desc ";
}
