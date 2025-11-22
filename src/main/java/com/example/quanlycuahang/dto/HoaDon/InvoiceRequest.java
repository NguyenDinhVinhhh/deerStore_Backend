package com.example.quanlycuahang.dto.HoaDon;

import java.util.List;

// DTO tổng hợp dữ liệu từ client
public class InvoiceRequest {
    private InvoiceHeaderDto hoa_don;
    private List<InvoiceItemDto> items;
    private List<PaymentDto> payment;


    public InvoiceHeaderDto getHoa_don() { return hoa_don; }
    public void setHoa_don(InvoiceHeaderDto hoa_don) { this.hoa_don = hoa_don; }
    public List<InvoiceItemDto> getItems() { return items; }
    public void setItems(List<InvoiceItemDto> items) { this.items = items; }
    public List<PaymentDto> getPayment() { return payment; }
    public void setPayment(List<PaymentDto> payment) { this.payment = payment; }
}