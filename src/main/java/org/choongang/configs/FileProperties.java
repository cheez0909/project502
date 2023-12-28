package org.choongang.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "file.upload") // 설정파일의 prefix
public class FileProperties {
    private String url;
    private String path;
}
