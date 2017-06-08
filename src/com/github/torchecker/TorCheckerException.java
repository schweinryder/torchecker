package com.github.torchecker;

public class TorCheckerException extends Exception {

	private static final long serialVersionUID = 7262244080057642788L;

    public TorCheckerException(String message) {
        super(message);
    }

    public TorCheckerException(Throwable cause) {
        super(cause);
    }
    
    public TorCheckerException(String message, Throwable cause) {
        super(message, cause);
    }

}
