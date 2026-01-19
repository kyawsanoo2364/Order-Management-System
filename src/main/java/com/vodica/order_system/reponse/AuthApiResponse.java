package com.vodica.order_system.reponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.vodica.order_system.dto.AuthDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthApiResponse {
   private String status;
   private String message;
   private AuthDataDTO data;
}
