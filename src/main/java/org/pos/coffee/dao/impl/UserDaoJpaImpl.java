package org.pos.coffee.dao.impl;

import org.evey.dao.impl.BaseEntityDaoJpaImpl;
import org.evey.bean.User;
import org.pos.coffee.dao.UserDao;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;

/**
 * Created by kenji on 12/4/2015.
 */
@Repository("userDao")
public class UserDaoJpaImpl extends BaseEntityDaoJpaImpl<User,Long> implements UserDao {

    @Override
    public User loadUserByUsername(String username) {
        String queryString = "SELECT obj FROM User obj where obj.username = :username";
        Query query = getEntityManager().createQuery(queryString);
        query.setParameter("username", username);
        return (User) query.getSingleResult();
    }

    @Override
    public Boolean checkIfPinIsUnique(String pin) {
        String queryString = "SELECT COUNT(obj.id) FROM User obj where obj.pinDigit = :pin";
        Query query = getEntityManager().createQuery(queryString);
        query.setParameter("pin", pin);

        Long count = (Long) query.getSingleResult();
        if(count!=null &&
                count>0){
            return false;
        }

        return true;
    }

    @Override
    public Boolean checkIfUsernameIsUnique(String username) {
        String queryString = "SELECT COUNT(obj.id) FROM User obj where obj.username = :username";
        Query query = getEntityManager().createQuery(queryString);
        query.setParameter("username", username);

        Long count = (Long) query.getSingleResult();
        if(count!=null &&
                count>0){
            return false;
        }

        return true;
    }
}
