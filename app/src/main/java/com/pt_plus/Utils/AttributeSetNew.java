package com.pt_plus.Utils;



public interface AttributeSetNew {
    int getAttributeCount();

    default String getAttributeNamespace(int index) {
        throw new RuntimeException("Stub!");
    }

    String getAttributeName(int var1);

    String getAttributeValue(int var1);

    String getAttributeValue(String var1, String var2);

    String getPositionDescription();

    int getAttributeNameResource(int var1);

    int getAttributeListValue(String var1, String var2, String[] var3, int var4);

    boolean getAttributeBooleanValue(String var1, String var2, boolean var3);

    int getAttributeResourceValue(String var1, String var2, int var3);

    int getAttributeIntValue(String var1, String var2, int var3);

    int getAttributeUnsignedIntValue(String var1, String var2, int var3);

    float getAttributeFloatValue(String var1, String var2, float var3);

    int getAttributeListValue(int var1, String[] var2, int var3);

    boolean getAttributeBooleanValue(int var1, boolean var2);

    int getAttributeResourceValue(int var1, int var2);

    int getAttributeIntValue(int var1, int var2);

    int getAttributeUnsignedIntValue(int var1, int var2);

    float getAttributeFloatValue(int var1, float var2);

    String getIdAttribute();

    String getClassAttribute();

    int getIdAttributeResourceValue(int var1);

    int getStyleAttribute();
}

