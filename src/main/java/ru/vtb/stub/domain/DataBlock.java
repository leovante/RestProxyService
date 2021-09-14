package ru.vtb.stub.domain;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataBlock {

    private Integer status;
    private List<Header> headers;
    private JsonNode body;
}
