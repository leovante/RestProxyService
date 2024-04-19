package ru.vtb.stub.dto;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.vtb.stub.validate.Method;
import ru.vtb.stub.validate.Path;
import ru.vtb.stub.validate.Team;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetDataBaseRequest {

    @Parameter(name = "team", example = "team1")
    @Team
    private String team;
    @Parameter(name = "path", example = "/path/example")
    @Path
    private String path;
    @Parameter(name = "method", example = "GET")
    @Method
    private String method;

}
