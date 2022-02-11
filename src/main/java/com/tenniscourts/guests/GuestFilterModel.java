package com.tenniscourts.guests;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

@Getter
@Setter
public class GuestFilterModel {

    private Integer page = 0;
    private Integer size = 10;
    private String nameLike;

    public Integer getPage() {
        return page == null ? 0 : page;
    }

    public Integer getSize() {
        return size == null ? 10 : size;
    }
}