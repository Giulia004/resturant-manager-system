package com.delivery.system.dto.request;

import java.util.List;

public record OrdineRequest(Integer numeroTavolo, List<OrdineItemRequest> righe) {
    
}
