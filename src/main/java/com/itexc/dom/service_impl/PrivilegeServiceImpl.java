package com.itexc.dom.service_impl;

import com.itexc.dom.domain.Privilege;
import com.itexc.dom.domain.enums.ERROR_CODE;
import com.itexc.dom.domain.projection.PrivilegeView;
import com.itexc.dom.exceptions.ValidationException;
import com.itexc.dom.repository.PrivilegeRepository;
import com.itexc.dom.sevice.PrivilegeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PrivilegeServiceImpl implements PrivilegeService {

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Override
    public List<PrivilegeView> getAllPrivileges() {
        return privilegeRepository.findAll();
    }

    @Override
    public Collection<Privilege> getPrivilegesFromIdList(List<Long> privileges)
            throws ValidationException {
        Collection<Privilege> result = privilegeRepository.findByIdIn(privileges);
        if (result.size() != privileges.size()) throw new ValidationException(ERROR_CODE.INEXISTANT_PRIVILEGE);
        return result;
    }

    @Override
    public List<PrivilegeView> fromPrivilegesListToPrivilegesViewList(Collection<Privilege> privileges ) {
        List<Privilege> allPrivileges = privilegeRepository.findAll();
        List<PrivilegeView> result = privileges.stream()
                .map(item -> new PrivilegeView(item, true)).collect(Collectors.toList());
        List<PrivilegeView> nonSelected = allPrivileges.stream()
                .map(item -> new PrivilegeView(item, false))
                .filter(item -> !result.contains(item))
                .collect(Collectors.toList());
        result.addAll(nonSelected);
        return result;
    }
}
