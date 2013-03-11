package org.example.service;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * @author bastengao
 * @date 13-3-11 下午11:12
 */
@Service
@Scope("prototype")
public class MoneyServiceImpl implements MoneyService {
    @Override
    public void giveMeMoney(int amountIWant) {
        System.out.println("give you money: " + amountIWant);
    }
}
