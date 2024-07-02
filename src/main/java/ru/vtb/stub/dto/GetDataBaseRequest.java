package ru.vtb.stub.dto;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.http.annotation.QueryValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.vtb.stub.validate.Method;
import ru.vtb.stub.validate.Path;
import ru.vtb.stub.validate.Team;

@Introspected
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetDataBaseRequest {

    @Team
    @QueryValue
    private String team;
    @Path
    @QueryValue
    private String path;
    @Method
    @QueryValue
    private String method;

}
