/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2022-2023. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.huawei.agc.clouddb.quickstart.target.model;

import com.huawei.agconnect.server.clouddb.annotations.PrimaryKeys;
import com.huawei.agconnect.server.clouddb.request.CloudDBZoneObject;
import com.huawei.agconnect.server.clouddb.request.Text;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Definition of ObjectType alltypetable.
 *
 * @since 2023-01-09
 */
@PrimaryKeys({"ID"})
public final class alltypetable extends CloudDBZoneObject {
    @JSONField(name = "ID")
    private Integer ID;

    @JSONField(name = "IntegerT")
    private Integer IntegerT;

    @JSONField(name = "DoubleT")
    private Double DoubleT;

    @JSONField(name = "FloatT")
    private Float FloatT;

    @JSONField(name = "ShortT")
    private Short ShortT;

    @JSONField(name = "LongT")
    private Long LongT;

    @JSONField(name = "StringT")
    private String StringT;

    @JSONField(name = "BooleanT")
    private Boolean BooleanT;

    @JSONField(name = "ByteArrayT")
    private byte[] ByteArrayT;

    private Text TextT;

    public alltypetable() {
        super(alltypetable.class);
    }

    @JSONField(name = "ID")
    public void setID(Integer ID) {
        this.ID = ID;
    }

    @JSONField(name = "ID")
    public Integer getID() {
        return ID;
    }

    @JSONField(name = "IntegerT")
    public void setIntegerT(Integer IntegerT) {
        this.IntegerT = IntegerT;
    }

    @JSONField(name = "IntegerT")
    public Integer getIntegerT() {
        return IntegerT;
    }

    @JSONField(name = "DoubleT")
    public void setDoubleT(Double DoubleT) {
        this.DoubleT = DoubleT;
    }

    @JSONField(name = "DoubleT")
    public Double getDoubleT() {
        return DoubleT;
    }

    @JSONField(name = "FloatT")
    public void setFloatT(Float FloatT) {
        this.FloatT = FloatT;
    }

    @JSONField(name = "FloatT")
    public Float getFloatT() {
        return FloatT;
    }

    @JSONField(name = "ShortT")
    public void setShortT(Short ShortT) {
        this.ShortT = ShortT;
    }

    @JSONField(name = "ShortT")
    public Short getShortT() {
        return ShortT;
    }

    @JSONField(name = "LongT")
    public void setLongT(Long LongT) {
        this.LongT = LongT;
    }

    @JSONField(name = "LongT")
    public Long getLongT() {
        return LongT;
    }

    @JSONField(name = "StringT")
    public void setStringT(String StringT) {
        this.StringT = StringT;
    }

    @JSONField(name = "StringT")
    public String getStringT() {
        return StringT;
    }

    @JSONField(name = "BooleanT")
    public void setBooleanT(Boolean BooleanT) {
        this.BooleanT = BooleanT;
    }

    @JSONField(name = "BooleanT")
    public Boolean getBooleanT() {
        return BooleanT;
    }

    @JSONField(name = "ByteArrayT")
    public void setByteArrayT(byte[] ByteArrayT) {
        this.ByteArrayT = ByteArrayT;
    }

    @JSONField(name = "ByteArrayT")
    public byte[] getByteArrayT() {
        return ByteArrayT;
    }

    @JSONField(name = "TextT", deserializeUsing = Text.TextValueReader.class)
    public void setTextT(Text TextT) {
        this.TextT = TextT;
    }

    @JSONField(name = "TextT", serializeUsing = Text.TextValueWriter.class)
    public Text getTextT() {
        return TextT;
    }
}
