package com.lime.limeEduApi.framework.common.encrypt;

public class EncryptField {
    public static enum SHA512{
        Password
    }
    public static enum AES256{
        Id,
        FirstName,
        MiddleName,
        LastName,
        Phone,
        Email,
        Birth,
        Passport,
        Sender,
        Receiver
    }
}
