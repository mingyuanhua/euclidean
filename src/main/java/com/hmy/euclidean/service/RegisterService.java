package com.hmy.euclidean.service;

import com.hmy.euclidean.dao.RegisterDao;
import com.hmy.euclidean.entity.db.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class RegisterService {
    @Autowired
    private RegisterDao registerDao;

    public boolean register(User user) throws IOException{
        return registerDao.register(User);
    }
}
