package com.kaishengit.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kaishengit.entity.*;
import com.kaishengit.mapper.AdminMapper;
import com.kaishengit.mapper.CustomerMapper;
import com.kaishengit.mapper.SaleChanceMapper;
import com.kaishengit.service.CustomerService;
import com.kaishengit.service.exception.ServiceException;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 2017/11/10.
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private SaleChanceMapper saleChanceMapper;



    private Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Value("#{'${customer.sources}'.split(',')}")
    private List<String> customerSources;

    @Value("#{'${customer.business}'.split(',')}")
    private List<String> customersBusiness;

    @Override
    public List<String> findAllSources(){
        return customerSources;
    }

    @Override
    public List<String> findAllBusiness(){
        return customersBusiness;
    }

    @Override
    public void saveCustomer(Customer customer) {
        customerMapper.insertSelective(customer);
    }

    @Override
    public Customer findById(Integer id) {
        return customerMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateCustomer(Customer customer) {
        customer.setUpdatetime(new Date());
        customerMapper.updateByPrimaryKeySelective(customer);
    }

    /**
     * 把客户放入公客池
     * @param customer
     */
    @Override
    public void publicCustomer(Customer customer) {

        Admin admin = adminMapper.selectByPrimaryKey(customer.getUserId());
        customer.setReminder(customer.getReminder() + "  " + admin.getUsername() + "于" + new Date()+ "把客户"+ customer.getCusName() + "放入公海");
        customer.setUserId(0);
        customer.setUpdatetime(new Date());
        customerMapper.updateByPrimaryKeySelective(customer);
    }

    /**
     * 查询当前登录用户的所有客户列表
     * @param userId 当前登陆对象的唯一标识
     * @return 客户列表
     */
    @Override
    public List<Customer> findAllCustomerMy(Integer userId) {

        CustomerExample customerExample = new CustomerExample();
        customerExample.createCriteria().andUserIdEqualTo(userId);
        return customerMapper.selectByExample(customerExample);
    }

    /**
     * 导出cvs文件
     * @param outputStream
     * @param admin
     * @throws IOException
     */
    @Override
    public void exportCsvFileToOutputStream(OutputStream outputStream, Admin admin) throws IOException {
        CustomerExample customerExample = new CustomerExample();
        customerExample.createCriteria().andUserIdEqualTo(admin.getId());
        List<Customer> customerList = customerMapper.selectByExample(customerExample);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("姓名").append(",").append("手机号")
                .append(",").append("性别").append(",").append("地址").append("\r\n");
        for(Customer customer : customerList){
            stringBuilder.append(customer.getCusName()).append(",")
                    .append(customer.getMobile()).append(",")
                    .append(customer.getSex()).append(",")
                    .append(customer.getAdress())
                    .append("\r\n");
        }
        IOUtils.write(stringBuilder.toString(),outputStream,"GBK");
        outputStream.flush();
        outputStream.close();

    }

    /**
     * 导出xls文件
     * @param outputStream
     * @param admin
     * @throws IOException
     */
    @Override
    public void exportXlsFileToOutputStream(OutputStream outputStream, Admin admin)throws IOException {

        CustomerExample customerExample = new CustomerExample();
        customerExample.createCriteria().andUserIdEqualTo(admin.getId());
        List<Customer> customerList = customerMapper.selectByExample(customerExample);

        //创建工作表
        Workbook workbook = new HSSFWorkbook();
        //创建sheet
        Sheet sheet = workbook.createSheet();
        //创建行
        Row row = sheet.createRow(0);
        //创建单元格
        Cell cell = row.createCell(0);
        cell.setCellValue("姓名");
        row.createCell(1).setCellValue("性别");
        row.createCell(2).setCellValue("地址");
        row.createCell(3).setCellValue("手机号码");

        for(int i = 0; i < customerList.size(); i++){
            Customer customer = customerList.get(i);

            Row tableRow = sheet.createRow(i + 1);
            tableRow.createCell(0).setCellValue(customer.getCusName());
            tableRow.createCell(1).setCellValue(customer.getSex());
            tableRow.createCell(2).setCellValue(customer.getAdress());
            tableRow.createCell(3).setCellValue(customer.getMobile());
        }

        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();


    }

    /**
     * 获取公海客户
     *
     * @return 公海客户集合
     */
    @Override
    public List<Customer> findAllPublicCustomer() {
        CustomerExample customerExample = new CustomerExample();
        customerExample.createCriteria().andUserIdEqualTo(0);
        return customerMapper.selectByExample(customerExample);

    }

    /**
     * 公客私有化
     * @param id    公客id
     * @param admin
     */
    @Override
    public void privateCustomer(Integer id, Admin admin) {
        Customer customer = customerMapper.selectByPrimaryKey(id);
        if(customer.getUserId().equals(0)){
           customer.setUserId(admin.getId());
           customer.setUpdatetime(new Date());
           customer.setReminder(customer.getReminder() + "   " + new Date().toString() + admin.getUsername() + "把客户私有化");
           customerMapper.updateByPrimaryKeySelective(customer);
        }else if (customer.getUserId().equals(admin.getId()) || !customer.getUserId().equals(0)){
            throw new ServiceException("该客户不属于公海客户或不存在");
        }
    }

    /**
     * 对于客户的删除操作
     *
     * @param customer
     */
    @Override
    public void deleteCustomer(Customer customer) throws ServiceException{

        SaleChanceExample saleChanceExample = new SaleChanceExample();
        saleChanceExample.createCriteria().andCustomerIdEqualTo(customer.getId());
        List<SaleChance> saleChanceList = saleChanceMapper.selectByExample(saleChanceExample);
        if(!saleChanceList.isEmpty()){
            throw new ServiceException("该客户存在销售机会");
        }
        customerMapper.deleteByPrimaryKey(customer.getId());
    }

    /**
     * 获取当前账户的所有客户的分页对象
     * @param pageNo 页数
     * @param userId 当前登陆对象的唯一标识
     * @return
     */
    @Override
    public PageInfo<Customer> findMyCustomer(Integer pageNo,Integer userId) {

        PageHelper.startPage(pageNo,10);
        CustomerExample customerExample = new CustomerExample();
        customerExample.createCriteria().andUserIdEqualTo(userId);

        List<Customer> customerList = customerMapper.selectByExample(customerExample);
        return new PageInfo<>(customerList);
    }
}
