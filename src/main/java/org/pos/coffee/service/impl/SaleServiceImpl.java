package org.pos.coffee.service.impl;

import org.apache.commons.lang.time.DateFormatUtils;
import org.evey.dao.SequenceDao;
import org.evey.service.impl.BaseCrudServiceImpl;
import org.pos.coffee.bean.*;
import org.pos.coffee.bean.helper.ItemUsedHelper;
import org.pos.coffee.bean.helper.OrderExpenseHelper;
import org.pos.coffee.dao.SaleDao;
import org.pos.coffee.service.ItemService;
import org.pos.coffee.service.OrderService;
import org.pos.coffee.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Laurie on 12/14/2015.
 */
@Service("saleService")
public class SaleServiceImpl extends BaseCrudServiceImpl<Sale> implements SaleService {

    @Autowired
    private SaleDao saleDao;

    @Autowired
    private SequenceDao sequenceDao;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ItemService itemService;

    @Override
    @Transactional
    public Double countExpensePerOrder(List<OrderExpenseHelper> orderExpenseHelperList) {
        Double totalExpense = 0D;
        for(OrderExpenseHelper orderExpenseHelper: orderExpenseHelperList){
            Order orderLoaded = orderService.load(orderExpenseHelper.getOrderId());
            orderLoaded.setTotalLineExpense(orderExpenseHelper.getExpense());
            totalExpense += orderExpenseHelper.getExpense();
            orderService.save(orderLoaded);
        }
        return totalExpense;
    }

    @Override
    @Transactional
    public void createSaleAndOrders(Sale sale) {
        sale.setSaleDate(new Date());
        this.save(sale);
        for(Order order: sale.getOrders()){
            order.setSale(sale);
            orderService.save(order);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Sale confirmSaleTransaction(Sale sale) throws Exception{
        this.createSaleAndOrders(sale);
        List<ItemUsedHelper> itemUsed = orderService.countUseItems(sale.getOrders());
        List<OrderExpenseHelper> orderExpenseHelperList = itemService.deductItemInventory(itemUsed);
        sale.setTotalCost(this.countExpensePerOrder(orderExpenseHelperList));
        this.save(sale);
        return sale;
    }

    @Override
    @Transactional
    public String generatePurchaseCode(String key, int increment, int retryCount, int maxRetry) {
        final String datePrefix = DateFormatUtils.format(Calendar.getInstance().getTime(), "MMddyy");
        Long generatedCode = sequenceDao.incrementValue(key+datePrefix, increment, retryCount, maxRetry);
        return datePrefix+String.format("%04d",generatedCode);
    }

}
