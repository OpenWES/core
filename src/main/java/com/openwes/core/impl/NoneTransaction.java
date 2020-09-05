package com.openwes.core.impl;

import com.openwes.core.Transaction;
import com.openwes.core.annotation.Implementation;

/**
 *
 * @author xuanloc0511@gmail.com
 */
@Implementation(source = Transaction.class)
public class NoneTransaction implements Transaction {

    @Override
    public boolean begin() throws Exception {
        /**
         * DO NOTHING
         */
        return true;
    }

    @Override
    public boolean commit() throws Exception {
        /**
         * DO NOTHING
         */
        return true;
    }

    @Override
    public boolean rollback() throws Exception {
        /**
         * DO NOTHING
         */
        return true;
    }

    @Override
    public boolean end() throws Exception {
        /**
         * DO NOTHING
         */
        return true;
    }

}
