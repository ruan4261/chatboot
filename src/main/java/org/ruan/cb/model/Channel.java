package org.ruan.cb.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Channel {

    private Integer id;
    private String name;
    private Integer online;
    private String notice;

}
