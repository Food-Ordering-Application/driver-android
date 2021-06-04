package com.foa.driver.model;

import com.foa.driver.model.enums.InvoiceStatus;
import com.google.gson.annotations.SerializedName;

public class Invoice {
    @SerializedName("status")
    private InvoiceStatus status;
    @SerializedName("payment")
    private Payment payment;
}
