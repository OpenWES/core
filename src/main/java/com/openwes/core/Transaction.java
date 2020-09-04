package com.openwes.core;

/**
 *
 * @author xuanloc0511@gmail.com
 */
public interface Transaction {

    public boolean begin() throws Exception;

    public boolean commit() throws Exception;

    public boolean rollback() throws Exception;

    public boolean end() throws Exception;
}
