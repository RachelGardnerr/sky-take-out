package com.sky.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdersRejectionDTO implements Serializable {

    private Long id;

    //订单拒绝原因
    private String rejectionReason;

}
