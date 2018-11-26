package com.yunnex.ops.erp.modules.order.exception;

/**
 * 订单物料同步异常
 */
public class OrderMaterialException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public OrderMaterialException() {
        super();
    }

    public OrderMaterialException(String message) {
        super(message);
    }

    public OrderMaterialException(Throwable cause) {
        super(cause);
    }

    public OrderMaterialException(String message, Throwable cause) {
        super(message, cause);
    }
}
