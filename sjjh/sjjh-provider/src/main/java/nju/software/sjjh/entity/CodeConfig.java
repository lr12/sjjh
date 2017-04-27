package nju.software.sjjh.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 代码项配置表
 * Created by Nekonekod on 2017/4/24.
 */
@Entity
@Table(name = "CODE_CONFIG")
public class CodeConfig {

    /**
     * 主键
     */
    private Integer id;
    /**
     * 类型
     */
    private String codeType;
    /**
     * 键
     */
    private String codeKey;
    /**
     * 值
     */
    private String codeValue;
    /**
     * 备注
     */
    private String remark;
    /**
     * 备注2
     */
    private String remark2;

    public CodeConfig() {
    }

    public CodeConfig(Integer id, String codeType, String codeKey, String codeValue, String remark, String remark2) {
        this.id = id;
        this.codeType = codeType;
        this.codeKey = codeKey;
        this.codeValue = codeValue;
        this.remark = remark;
        this.remark2 = remark2;
    }

    @Id
    @Column(name = "ID", nullable = false, unique = true)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "CODE_TYPE")
    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }


    @Column(name = "CODE_KEY")
    public String getCodeKey() {
        return codeKey;
    }

    public void setCodeKey(String codeKey) {
        this.codeKey = codeKey;
    }

    @Column(name = "CODE_VALUE")
    public String getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }

    @Column(name = "REMARK")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(name = "REMARK2")
    public String getRemark2() {
        return remark2;
    }

    public void setRemark2(String remark2) {
        this.remark2 = remark2;
    }
}
