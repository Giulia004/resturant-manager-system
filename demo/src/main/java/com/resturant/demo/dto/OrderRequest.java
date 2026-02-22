package com.resturant.demo.dto;

import java.util.List;

public record OrderRequest (Long tableId,Long cameriereId,List<OrderItemRequest> items) {
    
}
