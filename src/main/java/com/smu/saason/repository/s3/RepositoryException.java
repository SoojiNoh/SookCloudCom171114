package com.smu.saason.repository.s3;

/**
 * Created by mint on 12/04/2017.
 */
public class RepositoryException extends Exception {

    public RepositoryException() {
        super();
    }

    public RepositoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public RepositoryException(String message) {
        super(message);
    }

    public RepositoryException(Throwable cause) {
        super(cause);
    }


}
