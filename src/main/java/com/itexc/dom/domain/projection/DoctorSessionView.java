package com.itexc.dom.domain.projection;

import com.itexc.dom.domain.DoctorSession;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorSessionView {

    private String code;
    private String start;
    private String end;
    private boolean reserved;

    public DoctorSessionView(DoctorSession session , boolean reserved) {
        this.code = session.getCode();
        this.start = session.getStart();
        this.end = session.getEndTime();
        this.reserved = reserved;
    }
}
