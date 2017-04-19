package nju.software.sjjh.dao;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.connection.ConnectionProvider;
import org.hibernate.engine.SessionFactoryImplementor;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

/**
 * Dao公共类
 * Created by Nekonekod on 2017/4/17.
 */
@Slf4j
public abstract class BaseDao<T> extends HibernateDaoSupport {


    /**
     * 保存dataobject
     * @param entity
     */
    public void save(T entity) {
        getHibernateTemplate().save(entity);
    }

    /**
     * 根据id获取
     *
     * @param id 主键
     * @return
     */
    public T get(Serializable id) {
        try {
            return getHibernateTemplate().get(this.getGenerics(), id);
        } catch (Exception e) {
            log.error("find by id failed:class:" + getGenerics() + ",id:" + id.toString());
        }
        return null;
    }

    /**
     * 删除
     * @param entity
     */
    public void delete(T entity){
        getHibernateTemplate().delete(entity);
    }

    /**
     * 更新
     * @param entity
     */
    public void update(T entity){
        getHibernateTemplate().update(entity);
    }

    /**
     * 根据属性名称和属性值查找
     *
     * @param name  属性名
     * @param value 属性值
     * @return
     */
    public List<T> findByProperty(String name, Object value) {
        String doname = this.getGenerics().getSimpleName();
        try {
            String hql = "from " + doname + " where " + name + " = ? ";
            return getHibernateTemplate().find(hql, value);
        } catch (Exception e) {
            log.error("find by property failed:class:" + doname + ",property name:" + name + ",value:" + value,e);
        }
        return Collections.<T>emptyList();
    }

    /**
     * 封装好的手写resultset取数据
     *
     * @param sql
     * @param callback {@link BaseDao.DealResultSetCallback}
     */
    protected void dealResultSet(String sql, DealResultSetCallback callback) {
        ConnectionProvider cp = null;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            cp = ((SessionFactoryImplementor) this.getSessionFactory()).getConnectionProvider();
            connection = cp.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            callback.dealResultSet(resultSet);
        } catch (SQLException e) {
            log.error("Wrong sql:" + sql, e);
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
                if (statement != null)
                    statement.close();
                if (cp != null)
                    cp.closeConnection(connection);
            } catch (SQLException e) {
                log.error("关闭数据库连接出错。", e);
            }
        }
    }



    /**
     * 注入SessionFactory
     *
     * @param sessionFactory
     */
    @Resource
    public void setMySessioinFactory(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }

    /**
     * 获取泛型类型
     *
     * @return
     */
    protected Class<T> getGenerics() {
        Type type = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        Class<T> clazz = (Class<T>) type;
        return clazz;
    }

    /**
     * 手动使用resultset时使用的回调接口
     *
     * @See {@link BaseDao#dealResultSet}
     */
    protected interface DealResultSetCallback {
        void dealResultSet(ResultSet resultSet) throws SQLException;
    }



}
