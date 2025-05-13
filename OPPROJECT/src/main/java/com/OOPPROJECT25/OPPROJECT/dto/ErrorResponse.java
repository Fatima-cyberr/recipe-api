package com.OOPPROJECT25.OPPROJECT.dto; // for documentation part


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;




/**
 *
 *
 * @see org.springframework.http.ResponseEntity
 * @see org.springframework.web.bind.annotation.ExceptionHandler
 */
@Getter
@Setter
public class ErrorResponse {

    private LocalDateTime timestamp; // if an error occurred during the request response process it returns
                                   // it returns the time where the error occurred
    /**
     *  @see org.springframework.http.HttpStatus
     */
    private int status; // number of the error (short description)

    private String error;// readable explanation about the error (kenet binary mfrud returned as a paragraph)

    private String message;

    private String path;//if there is an error in the path provided
}