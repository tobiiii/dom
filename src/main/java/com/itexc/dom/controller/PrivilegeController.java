package com.itexc.dom.controller;

import com.itexc.dom.domain.projection.PrivilegeView;
import com.itexc.dom.sevice.PrivilegeService;
import com.itexc.dom.utils.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/privilege")
@Validated
public class PrivilegeController {

    @Autowired
    private PrivilegeService privilegeService;

    @PreAuthorize("hasAnyAuthority('profile.list'," +
                  "'profile.add'," +
                  "'profile.update')")
    @GetMapping(value = "/find_all")
    public JsonResponse findAll() {
        List<PrivilegeView> privileges = privilegeService.getAllPrivileges();
        return JsonResponse.builder().data(privileges)
                .status(JsonResponse.STATUS.SUCCESS).build();
    }
}
