package com.kaishengit.service;

import com.github.pagehelper.PageInfo;
import com.kaishengit.entity.Admin;
import com.kaishengit.entity.Customer;
import com.kaishengit.service.exception.ServiceException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by User on 2017/11/10.
 */
public interface CustomerService {


    PageInfo<Customer> findMyCustomer(Integer pageNo, Integer userId);

    public List<String> findAllSources();

    public List<String> findAllBusiness();

    void saveCustomer(Customer customer);

    Customer findById(Integer id);

    void updateCustomer(Customer customer);

    void publicCustomer(Customer customer);

    /**
     * 获取当前用户的客户列表
     * @param userId 当前登录用户id
     * @return
     */
    List<Customer> findAllCustomerMy(Integer userId);
    /**
     * 导出客户列表为csv格式
     * @param outputStream
     * @param admin 当前登录用户
     * @throws IOException
     */
    void exportCsvFileToOutputStream(OutputStream outputStream, Admin admin) throws IOException;

    /**
     * 导出客户列表为xls格式
     * @param outputStream
     * @param admin 当前登录用户
     * @throws IOException
     */
    void exportXlsFileToOutputStream(OutputStream outputStream, Admin admin) throws IOException;

    /**
     * 获取公海客户
     * @return 公海客户集合
     */
    List<Customer> findAllPublicCustomer();

    /**
     * 公客私有化
     * @param id 公客id
     * @param admin
     */
    void privateCustomer(Integer id, Admin admin);

    /**
     * 对于客户的删除操作
     * @param customer
     */
    void deleteCustomer(Customer customer)throws ServiceException;
}
