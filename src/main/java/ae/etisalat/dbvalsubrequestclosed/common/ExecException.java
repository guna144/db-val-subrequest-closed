package ae.etisalat.dbvalsubrequestclosed.common;

public class ExecException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public ExecException(ExecResult p_execRes, Exception p_e) {
		super(p_execRes.getExecLog() + " ########## " + p_e.getMessage(), p_e);
	}

	public ExecException(String arg0) {
		super(arg0);
	}

	public ExecException(Throwable arg0) {
		super(arg0);
	}

	public ExecException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public ExecException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
