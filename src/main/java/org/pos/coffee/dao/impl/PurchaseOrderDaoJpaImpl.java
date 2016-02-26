package org.pos.coffee.dao.impl;

import org.evey.dao.impl.BaseEntityDaoJpaImpl;
import org.pos.coffee.bean.PurchaseOrder;
import org.pos.coffee.dao.PurchaseOrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Laurie on 1/4/2016.
 */
@Repository("purchaseOrderDao")
public class PurchaseOrderDaoJpaImpl extends BaseEntityDaoJpaImpl<PurchaseOrder,Long> implements PurchaseOrderDao {

    @Autowired
    private DataSource dataSource;

    private static final StringBuilder GET_PENDING_PURCHASES = new StringBuilder();

    static {
        GET_PENDING_PURCHASES.append("SELECT PURCHASE_CODE, STATUS FROM purchase where STATUS != 'Received';");
    }

    @Override
    public Map getPendingPurchases() {
        final NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(dataSource);
        final Map<String, Object> params = new HashMap<>();
        SqlRowSet pendingPurchases = template.queryForRowSet(GET_PENDING_PURCHASES.toString(), params);

        Map pendingPurchasesMap = new HashMap();

        while (pendingPurchases.next()) {
            pendingPurchasesMap.put(pendingPurchases.getString("PURCHASE_CODE"), pendingPurchases.getString("STATUS"));
        }

        return pendingPurchasesMap;
    }

}
