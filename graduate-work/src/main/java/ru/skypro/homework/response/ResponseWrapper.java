package ru.skypro.homework.response;
import lombok.Data;

import java.util.Collection;

@Data
public class ResponseWrapper<W> {
    private int count;
    private Collection<W> results;

    public static <W> ResponseWrapper<W> of(Collection<W> results) {
        ResponseWrapper<W> responseWrapper = new ResponseWrapper<>();
        if (results == null) {
            return responseWrapper;
        }
        responseWrapper.results = results;
        responseWrapper.count = results.size();
        return responseWrapper;
    }
}

