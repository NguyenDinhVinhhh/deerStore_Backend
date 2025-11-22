package com.example.quanlycuahang.dto.HoaDon;

import java.util.List;

public class InvoiceResponse {
    private InvoiceResponseHeaderDto hoa_don;
    private List<InvoiceResponseItemDto> items;
    private List<PaymentDto> payment; // Tái sử dụng PaymentDto


    public InvoiceResponseHeaderDto getHoa_don() { return hoa_don; }
    public void setHoa_don(InvoiceResponseHeaderDto hoa_don) { this.hoa_don = hoa_don; }
    public List<InvoiceResponseItemDto> getItems() { return items; }
    public void setItems(List<InvoiceResponseItemDto> items) { this.items = items; }
    public List<PaymentDto> getPayment() { return payment; }
    public void setPayment(List<PaymentDto> payment) { this.payment = payment; }
}