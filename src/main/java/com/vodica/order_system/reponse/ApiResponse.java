package com.vodica.order_system.reponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {
    private String status;
    private String message;
    private Object data;
    private Object errors = null;

    public ApiResponse(String status,String message){
        this.status = status;
        this.message = message;
        this.data = null;
        this.errors = null;
    }


    public ApiResponse(String status,String message,Object data){
        this.status = status;
        this.message =message;
        this.data = data;
        this.errors = null;
    }
}
