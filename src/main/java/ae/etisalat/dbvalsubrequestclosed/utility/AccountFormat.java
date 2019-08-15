package ae.etisalat.dbvalsubrequestclosed.utility;

public class AccountFormat {

	public static String getShort(String p_acc_num) {
		if(p_acc_num.substring(0,1).equals("0")) {
            return p_acc_num;
       }
       if(p_acc_num.substring(0,3).equals("971")) {
            return "0" + p_acc_num.substring(3);
       }
       return "0" + p_acc_num;
	}
	
	public static String getLong(String p_acc_num) {
		if(p_acc_num.substring(0,3).equals("971")) {
            return p_acc_num;
       }
       if(p_acc_num.substring(0,1).equals("0")) {
            return "971" + p_acc_num.substring(1);
       }
       return "971" + p_acc_num;

	}
	
	public static String getWithout0(String p_acc_num) {
		if(p_acc_num.substring(0,3).equals("971")) {
            return p_acc_num.substring(3);
       }
       if(p_acc_num.substring(0,1).equals("0")) {
            return p_acc_num.substring(1);
       }
       return p_acc_num;

	}
	
}
