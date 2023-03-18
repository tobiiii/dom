package com.itexc.dom.utils;

import com.itexc.dom.domain.Password;
import com.itexc.dom.domain.Privilege;
import com.itexc.dom.domain.Profile;
import com.itexc.dom.domain.User;
import com.itexc.dom.repository.PrivilegeRepository;
import com.itexc.dom.repository.ProfileRepository;
import com.itexc.dom.repository.SecurityCustomizationRepository;
import com.itexc.dom.repository.UserRepository;
import com.itexc.dom.security.WebSecurityConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@Component
public class CSVHelper {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    PrivilegeRepository privilegeRepository;

    @Autowired
    SecurityCustomizationRepository securityCustomizationRepository;

    @Autowired
    WebSecurityConfig webSecurityConfig;

    private static final String[] PRIVILEGE_HEADER = {"code", "description"};

    private static final String[] PROFILE_HEADER = {"code", "name"};

    private static final String[] PROFILE_PRIVILEGE_HEADER = {"profile", "privilege"};

    private static final String[] USER_HEADER = {"email_address", "first_name", "last_name", "profile", "password"};
    
    public <E extends Privilege> List<E> csvToPrivileges(InputStream inputStream, Class<E> clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        List<E> list = new ArrayList<>();

        try (var fileReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
             var csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader()
                     .withRecordSeparator(',').withIgnoreHeaderCase().withTrim());) {

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            E entity;
            for (CSVRecord csvRecord : csvRecords) {
                entity = clazz.getConstructor().newInstance();

                for (var i = 0; i < PRIVILEGE_HEADER.length; i++) {
                    var stringValue = csvRecord.get(PRIVILEGE_HEADER[i]);
                    if (StringUtils.isNotBlank(stringValue)) {
                        switch (i) {
                            case 0:
                                entity.setCode(stringValue);
                                break;
                            case 1:
                                entity.setDescription(stringValue);
                                break;
                            default:
                                break;
                        }
                    }
                }
                list.add(entity);
            }

            return list;
        } catch (IOException e) {
            log.warn("fail to parse privilege (" + clazz.getSimpleName() + ") CSV file: " + e.getMessage());
        }
        return list;
    }

    public Map<String, List<Privilege>> csvToProfilePrivilege(InputStream inputStream) throws Throwable {

        Map<String, List<Privilege>> map = new HashMap<>();
        try (var fileReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
             var csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader()
                     .withRecordSeparator(',').withIgnoreHeaderCase().withTrim());) {

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            List<Privilege> entity;
            for (CSVRecord csvRecord : csvRecords) {
                var profile = csvRecord.get(PROFILE_PRIVILEGE_HEADER[0]);
                var privilege = csvRecord.get(PROFILE_PRIVILEGE_HEADER[1]);
                Privilege object = (Privilege) privilegeRepository.findByCode(privilege)
                        .orElseThrow(() -> new ObjectNotFoundException(privilege, "Privilege"));
                System.out.println("object.getCode() = " + object.getCode());
                if (map.containsKey(profile)) {
                    entity = map.get(profile);
                    entity.add(object);
                } else {
                    entity = new ArrayList<>() {{
                        add(object);
                    }};
                    map.putIfAbsent(profile, entity);
                }
            }
            return map;
        } catch (IOException e) {
            log.warn("fail to parse profile_privilege CSV file: " + e.getMessage());
        }
        return map;
    }

    public <E extends Profile> List<E> csvToProfile(InputStream inputStream, Map<String, List<Privilege>> map, Class<E> clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        List<E> list = new ArrayList<>();

        try (var fileReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
             var csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader()
                     .withRecordSeparator(',').withIgnoreHeaderCase().withTrim());) {

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            E entity;
            for (CSVRecord csvRecord : csvRecords) {
                entity = clazz.getConstructor().newInstance();

                for (var i = 0; i < PROFILE_HEADER.length; i++) {
                    var stringValue = csvRecord.get(PROFILE_HEADER[i]);
                    if (StringUtils.isNotBlank(stringValue)) {
                        switch (i) {
                            case 0:
                                entity.setCode(stringValue);
                                break;
                            case 1:
                                entity.setName(stringValue);
                                break;
                            default:
                                break;
                        }
                    }
                }

                if (map.containsKey(entity.getCode())) {
                    entity.setPrivileges(map.get(entity.getCode()));
                }
                list.add(entity);
            }

            return list;
        } catch (IOException e) {
            log.warn("fail to parse profile CSV file: " + e.getMessage());
        }
        return list;
    }

    public <E extends User> List<E> csvToUser(InputStream inputStream, Class<E> clazz) {

        List<E> list = new ArrayList<>();

        try (var fileReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
             var csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader()
                     .withRecordSeparator(',').withIgnoreHeaderCase().withTrim());) {

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            E entity;
            for (CSVRecord csvRecord : csvRecords) {
                entity = clazz.getConstructor().newInstance();

                for (var i = 0; i < USER_HEADER.length; i++) {
                    var stringValue = csvRecord.get(USER_HEADER[i]);
                    if (StringUtils.isNotBlank(stringValue)) {
                        switch (i) {
                            case 0:
                                entity.setEmailAddress(stringValue);
                                break;
                            case 1:
                                entity.setFirstName(stringValue);
                                break;
                            case 2:
                                entity.setLastName(stringValue);
                                break;
                            case 3:
                                Profile profile = (Profile) profileRepository.findByCode(stringValue)
                                        .orElseThrow(() -> new ObjectNotFoundException(stringValue, "Profile"));
                                entity.setProfile(profile);
                                break;
                            case 4:
                                Password password = new Password();
                                password.setIsTemporary(Boolean.TRUE);
                                password.setCredential(webSecurityConfig.passwordEncoder().encode(stringValue));
                                entity.setPassword(password);
                                break;
                            default:
                                break;
                        }
                    }
                }

                list.add(entity);
            }

            return list;
        } catch (IOException e) {
            log.warn("fail to parse user (" + clazz.getSimpleName() + ") CSV file: " + e.getMessage());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return list;
    }
}
