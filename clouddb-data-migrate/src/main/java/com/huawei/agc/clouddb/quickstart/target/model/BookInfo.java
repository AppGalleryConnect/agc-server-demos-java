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

import com.huawei.agconnect.server.clouddb.annotations.DefaultValue;
import com.huawei.agconnect.server.clouddb.annotations.NotNull;
import com.huawei.agconnect.server.clouddb.annotations.PrimaryKeys;
import com.huawei.agconnect.server.clouddb.request.CloudDBZoneObject;
import com.huawei.agconnect.server.clouddb.request.Text;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * Definition of ObjectType BookInfo.
 *
 * @since 2023-01-11
 */
@PrimaryKeys({"id"})
public final class BookInfo extends CloudDBZoneObject {
    private Integer id;

    private String bookName;

    private String author;

    private Double price;

    private String publisher;

    private Date publishTime;

    @NotNull
    @DefaultValue(booleanValue = true)
    private Boolean shadowFlag;

    public BookInfo() {
        super(BookInfo.class);
        this.shadowFlag = true;

    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookName() {
        return bookName;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPrice() {
        return price;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublisher() {
        return publisher;
    }

    @JSONField(format="yyyy-MM-dd HH:mm:ss SSS")
    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    @JSONField(format="yyyy-MM-dd HH:mm:ss SSS")
    public Date getPublishTime() {
        return publishTime;
    }

    public void setShadowFlag(Boolean shadowFlag) {
        this.shadowFlag = shadowFlag;
    }

    public Boolean getShadowFlag() {
        return shadowFlag;
    }

}
