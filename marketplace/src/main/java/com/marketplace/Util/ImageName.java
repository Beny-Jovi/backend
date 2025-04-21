package com.marketplace.Util;

import org.apache.commons.lang3.RandomStringUtils;

public class ImageName {
    
    public static String customLogoImage(String storeId) {
        @SuppressWarnings("deprecation")
        String generatedString = RandomStringUtils.randomAlphanumeric(10);
        return storeId + "_profile_" + generatedString;
    }

    public static String getImageFileExtensions(String fileName) {
        if (fileName == null) {
            return null;
        }
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex >= 0) {
            return fileName.substring(dotIndex + 1);
        }
        return "";
    }

}
