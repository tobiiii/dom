package com.itexc.dom.sevice;

import com.itexc.dom.domain.Privilege;
import com.itexc.dom.domain.projection.PrivilegeView;
import com.itexc.dom.exceptions.ValidationException;

import java.util.Collection;
import java.util.List;

public interface PrivilegeService {

    List<PrivilegeView> getAllPrivileges();

    Collection<Privilege> getPrivilegesFromIdList(List<Long> privileges) throws ValidationException;

    List<PrivilegeView> fromPrivilegesListToPrivilegesViewList(Collection<Privilege> privileges);
}
