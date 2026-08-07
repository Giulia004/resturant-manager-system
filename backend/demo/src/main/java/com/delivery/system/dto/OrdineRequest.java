package com.delivery.system.dto;

import java.util.List;

public record OrdineRequest(Integer numeroTavolo, List<OrdineItemRequest> righe) {
    
}
