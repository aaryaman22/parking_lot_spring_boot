package com.example.PARKING_LOT_SYSTEM.Responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;


@Getter
@Setter
@AllArgsConstructor
public class Response<T> {
    T returnObject;
    HttpStatus httpStatus;
    public Response(T returnObject){
        this.returnObject = returnObject;
        this.httpStatus = HttpStatus.OK;
    }
}
