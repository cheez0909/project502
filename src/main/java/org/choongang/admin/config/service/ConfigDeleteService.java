package org.choongang.admin.config.service;

import lombok.RequiredArgsConstructor;
import org.choongang.admin.config.entites.Configs;
import org.choongang.admin.config.repositories.ConfigsRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConfigDeleteService {
    private final ConfigsRepository configsRepository;

    public void delete(String code){
        Configs configs = configsRepository.findById(code).orElse(null);
        if(configs == null){
            return;
        }
        configsRepository.delete(configs);
        configsRepository.flush(); // flush를 해야 삭제로 바뀜
    }
}
