package com.itexc.dom.domain.projection;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocDayView {
    private Date date;
    private List<DocSessionView> sessions;
}
