package com.dama.service.qrcode.exception;

public class QrNotFoundException extends RuntimeException{

    private static final long serialVersionUID = -2533194229906054487L;

    public QrNotFoundException(String message) {
        super(message);
    }
}
