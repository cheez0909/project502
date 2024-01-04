package org.choongang.admin.config.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.choongang.admin.config.entites.Configs;
import org.choongang.admin.config.repositories.ConfigsRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConfigInfoService {

    private final ConfigsRepository repository;

    /**
     * 널값을 허용하도록 -> 유연하게
     * 널일때 직접 설정
     */
    public <T> Optional<T> get(String code, Class<T> clazz){

        return get(code, clazz, null);
    }
    public <T> Optional<T> get(String code, TypeReference<T> typeReference){

        return get(code, null, typeReference);
    }

    // 리스트나 맵, 중첩된 객체 일 때
    public <T> Optional<T> get(String code, Class<T> clazz, TypeReference typeReference) {
        Configs config = repository.findById(code).orElse(null);
        if(config == null || !StringUtils.hasText(config.getData())){
            return Optional.ofNullable(null);
        }
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        String jsonString = config.getData();
        try {
            T data = null;
            if(clazz==null){ // TypeReference로 처리 : 맵이나 복합적인 구조일땐 이것을 쓰고
                data=om.readValue(jsonString, new TypeReference<T>() {
                });
            } else { // Class 처리 : 그냥 문자열 일때...?
                data = om.readValue(jsonString, clazz);
            }
            return Optional.ofNullable(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Optional.ofNullable(null);
        }
    }
}
