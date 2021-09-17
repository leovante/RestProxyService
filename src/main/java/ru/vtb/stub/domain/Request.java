package ru.vtb.stub.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Request {

    @NotBlank
    private String path;
    @NotBlank
    private String method;
    private Map<String, String> headers;
    private Map<String, String> params;
    private String body;
}
