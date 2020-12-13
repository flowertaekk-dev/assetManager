package com.assetManager.server.utils;

import java.net.URI;

public class TestDataUtil {
    // TODO 아 이놈들 대문자로 바꿔야한다... 왜 이렇게 했지..!?
    public static final String USER_ID = "test";
    public static final String SALT = "salt";
    public static final String PASSWORD = "test";
    public static final String EMAIL = "test@email.com";
    public static final String BUSINESS = "testBusiness";

    public static final String LOGIN_CONTROLLER_URL = "/api/v1/login";
    public static final String BUSINESS_CONTROLLER_URL = "/api/v1/business";
    public static final String TABLE_INFO_CONTROLLER_URL = "/api/v1/table";
    public static final String MENU_CONTROLLER_URL = "/api/v1/menu";
    public static final String ACCOUNT_CONTROLLER_URL = "/api/v1/account";
    public static final String UPDATE_USER_CONTROLLER_URL = "/api/v1/updatePassword";
    public static final String DELETE_USER_URL = "/api/v1/deleteUser";
}
