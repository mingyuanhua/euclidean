package com.hmy.euclidean.service;

import com.hmy.euclidean.dao.RegisterDao;
import com.hmy.euclidean.entity.db.User;
import com.hmy.euclidean.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class RegisterService {
    @Autowired
    private RegisterDao registerDao;

    public boolean register(User user) throws IOException{
        user.setPassword(Util.encryptPassword(user.getUserId(), user.getPassword()));
        return registerDao.register(user);
    }
}
