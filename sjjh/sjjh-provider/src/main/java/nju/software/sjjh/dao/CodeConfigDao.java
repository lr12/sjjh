package nju.software.sjjh.dao;

import nju.software.sjjh.entity.CodeConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 代码配置项数据访问对象
 * Created by Nekonekod on 2017/4/24.
 */
@Repository
public class CodeConfigDao extends BaseDao<CodeConfig> {


    /**
     * 获取银行
     * @return
     */
    @Cacheable("CODE_BANK")
    public List<CodeConfig> getBanks() {
        return findByProperty("type","银行标识");
    }


    @Override
    @CacheEvict(value = "CODE_BANK",allEntries = true)
    public void save(CodeConfig entity) {
        super.save(entity);
    }
}
