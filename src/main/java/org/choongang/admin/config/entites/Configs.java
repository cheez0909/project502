package org.choongang.admin.config.entites;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Data;

/**
 * code 와 값만
 */
@Entity
@Data
public class Configs {

    @Id @Column(length = 60)
    private String code;
    @Lob // large Object
    private String data;
}
