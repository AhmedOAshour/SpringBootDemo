package com.vodafone.SpringBootDemo.errorhandlling;

import org.springframework.http.HttpStatus;

public class DuplicateEntryException extends APIException{
    public DuplicateEntryException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
    }
}
