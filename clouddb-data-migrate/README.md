# 数据迁移demo说明

## 概述

​	代码示例示范如何将数据由原传统关系型数据库中迁移至Cloud DB。

​	本文档对示例代码的结构和实现进行说明，作为参考，帮助开发者实现自己的数据迁移目的。

## 名词说明

​	源数据库：数据源头，即当前数据所在的数据库。

​	宿数据库：数据迁移的目标数据库，即Cloud DB。

## 代码路径介绍

​	./config

​	—./agc-apiclient.json	认证凭据，可在下图所示位置下载得到。建议将下载后的文件改名后覆盖当前文件或将内容拷贝至本文件中。也可以选择直接使用下载后的文件，但需要注意修改读取该文件的代码逻辑以匹配正确的文件名称，代码逻辑位于AuthenticateTool.java中。

<img src="image\image-20230111171031616.png" alt="image-20230111171031616" />

​	—./db.properties	源数据库相关配置，需要APP开发者根据实际的数据库类型填写对应url、driver，根据实际配置填写数据库用户的名称和密码。

------

​	./lib	三方包存放路径。若不便线上引入AGC相关SDK或其他依赖的三方软件，可下载至本路径，修改pom.xml文件以本地引用。

​	—./for_local_lib.txt	内容为本地jar包引用方式。

------

​	./src	代码和配置文件路径

​	—./main/java

​	——./com.huawei.agc.clouddb.quickstart

​	———./Application.java	主函数，演示初始化环境、迁移表数据至Cloud DB指定存储区、数据导出至JSON文件等相关业务代码逻辑。IDE导入工程后可直接运行本类执行示例迁移功能。

------

​	———./annotation	自定义注解，用于解析模型对应的源数据库中表名和字段名、Cloud DB侧新定义的表名和字段名。

​	————./Column.java	模型字段注解，标记源宿字段名称

​	————./Table.java	模型类注解，标记源宿表名称和Cloud DB模型类

------

​	———./connector

​	————./DBConfig.java	源数据库配置文件读取类，读取db.properties文件。

​	————./DbConnector	源数据库连接类，提供获取数据库连接的接口。

------

​	———./auth

​	————./AuthenticateTool.java	读取认证凭证信息，初始化认证相关内容。

------

​	———./except	自定义异常类

------

​	———./migrate	数据迁移逻辑

​	————./DataHandler.java	数据处理类，读取源数据库中数据并以自定义模型格式返回、行数统计、模型转换。

​	————./DataMigrate.java	数据迁移逻辑类。提供数据迁移至Cloud DB的接口，数据导出为JSON文件的接口。

------

​	———./progress	进度管理

​	————./IProgressHandler.java	进度输出接口类，可以实现该接口用于自定义进度输出方式。

​	————./ConsoleProgressHandler.java	示例实现，进度输出至控制台

​	————./LoggerProgressHandler.java	示例实现，进度输出至日志文件中

------

​	———./source	源数据库表模型相关内容

​	————./ColumnMapping.java	源宿数据库中表和字段的映射关系对象，该类内容由DataHandler.java的init接口读取模型的注解后自动填充并缓存

​	————./model	源数据库表对应模型

​	—————./AbstractSourceModel.java	源数据库表模型基类，所有具体模型均需要继承该类

​	—————./SourceAllTypeTable.java	表模型示例实现

​	—————./SourceBookInfo.java	表模型示例实现

------

​	———./target   Cloud DB侧导出的表模型相关内容

​	————./model   Cloud DB侧导出的表模型

​	—————./alltypetable.java	Cloud DB侧定义表结构后导出的适配Server SDK的模型类

​	—————./BookInfo.java	Cloud DB侧定义表结构后导出的适配Server SDK的模型类

​	—./main/resource

​	——./log4j.xml	日志配置文件

​	./pom.xml	maven项目构建文件

​	./image	非工程文件，为README.md说明文档引用的图片存放路径

## 源数据库配置文件说明

​	配置文件路径为：./config/db.properties。

​	示例配置如下：

```properties
# mysql
source.db.url=jdbc:mysql://localhost:3306/test?characterEncoding=GBK
source.db.driver=com.mysql.cj.jdbc.Driver
source.db.user=root
source.db.password=root
# postgresql
#source.db.url=jdbc:postgresql://localhost:5432/postgres?currentSchema=test&socketTimeout=300
#source.db.driver=org.postgresql.Driver
#source.db.user=postgres
#source.db.password=postgres
# oracle
#source.db.url=jdbc:oracle:thin:@localhost:1521:orcl
#source.db.driver=oracle.jdbc.driver.OracleDriver
#source.db.user=user
#source.db.password=password
# sql server
#source.db.url=jdbc:sqlserver://localhost:1433;DatabaseName=test;
#source.db.driver=com.microsoft.sqlserver.jdbc.SQLServerDriver
#source.db.user=user
#source.db.password=password
```

​	示例文件中依次列举了mysql、postgresql、oracle、sql server等常用数据库的配置举例，请根据实际情况选择和修改配置内容。

​	**注意：**若使用postgresql数据库时，请在url中指定参数currentSchema，或在正式的代码中在SQL语句上拼接表所属的schema，否则可能出现表不存在的问题。

## 数据库驱动包

​	由于不同的数据库需要对应的数据库驱动包，所以需要根据实际情况选择相关驱动包。

​	本示例通过maven引入对应三方包，pom.xml配置中引入方式如下：

```xml
    <dependencies>
        ...
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.30</version>
        </dependency>
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <version>42.5.1</version>
        </dependency>
        <dependency>
            <groupId>com.oracle.database.jdbc</groupId>
            <artifactId>ojdbc10</artifactId>
            <version>19.17.0.0</version>
        </dependency>
        <dependency>
            <groupId>com.microsoft.sqlserver</groupId>
            <artifactId>sqljdbc4</artifactId>
            <version>4.0</version>
        </dependency>
        ...
    </dependencies>
```

​	如上，依次列举了mysql、postgresql、oracle和sql server的引入方式，版本号仅做参考，可以根据实际需要选择合适的版本。

​	由于oracle和sql server为收费数据库，故可能另有相应的数据库驱动程序，请自行根据实际需要设置，上述配置仅做参考。

## Cloud DB软件包引入

​	通过maven引入Cloud DB相关软件包，pom.xml文件引入方式如下：

```xml
    <dependencies>
        ...
        <dependency>
            <groupId>com.huawei.agconnect.server</groupId>
            <artifactId>agconnect-auth</artifactId>
            <version>1.2.7.300</version>
        </dependency>
        <dependency>
            <groupId>com.huawei.agconnect.server</groupId>
            <artifactId>agconnect-database-server-sdk</artifactId>
            <version>1.0.13.300</version>
        </dependency>
        ...
    </dependencies>
```

​	上述软件包版本仅作参考，请根据实际情况选择修改。

​	AGC相关SDK包会间接依赖部分开源软件，示例pom.xml文件中已按需添加相关依赖，若使用过程中仍旧出现软件包缺失的情况，请按需添加依赖或修改合适的版本。若使用三方镜像仓库下载com.huawei.agconnect.server失败，请使用pom.xml文件中的maven仓库地址。

## 表结构模型类注解详细说明

​	表结构模型类主要以源数据库表结构为参考编写，建议统一存放路径为：com.huawei.agc.clouddb.quickstart.source.model。

​	模型必须继承基类AbstractSourceModel，否则会出现编译问题。

​	demo提供CloudDBQuickStart_1.json文件，该文件可直接用于Cloud DB侧导入生成对象类型（表），如下模型以此表结构为参考进行说明。

​	模型如下：

```java
package com.huawei.agc.clouddb.quickstart.source.model;

import com.huawei.agc.clouddb.quickstart.annotation.Column;
import com.huawei.agc.clouddb.quickstart.annotation.Table;
import com.huawei.agc.clouddb.quickstart.target.model.BookInfo;

import java.util.Date;

@Table(source = "book", target = "BookInfo", targetClass = BookInfo.class)
public class SourceBookInfo extends AbstractSourceModel {
    @Column(source = "id", target = "id", isPrimaryKey = true)
    private Integer id;

    @Column(source = "book_name", target = "bookName")
    private String bookName;

    @Column(source = "author", target = "author")
    private String author;

    @Column(source = "price", target = "price")
    private Double price;

    @Column(source = "publisher", target = "publisher")
    private String publisher;

    @Column(source = "publish_time", target = "publishTime")
    private Date publishTime;

    @Column(source = "shadow_flag", target = "shadowFlag")
    private Boolean shadowFlag;
}
```

1. 使用Table注解模型类，参数source值为源数据库中表名，target值为Cloud DB中定义的表名，targetClass值为Cloud DB侧导出的模型类。
   该源模型类对类名没有要求，可以根据可读性或代码规范自定义类名。不过由于targetClass的存在，源宿模型的类名可能比较相近，建议统一增加Source前缀，用于区分，便于代码阅读。

2. 使用Column注解当前类成员变量，成员变量与表字段一一对应。
   
source值为源数据库表中字段的名称；
   
   target值为Cloud DB中定义的字段名称；
   
   isPrimaryKey值表示该字段是否为主键字段，若是联合主键则需要在每个字段上添加注解，非主键字段可以不用填写。
   
   字段变量命名没有要求，可基于可读性或代码规范适当修改。
   
3. 当前类所有字段的类型请使用封装类型定义，避免从数据库中读取数据时出现类型转换错误。

   bytea类型字段使用byte[]声明。

4. 定义模型字段时请注意Java数据类型与数据库中数据类型的对应关系，避免出现数据转换错误。

   常用数据类型对应关系可参考如下：

   |      Java数据类型       |                数据库数据类型                 |
   | :---------------------: | :-------------------------------------------: |
   |         Boolean         |                      bit                      |
   |         Boolean         |                     bool                      |
   | Boolean, Short, Integer |                    tinyint                    |
   |     Short, Integer      | smallint, mediumint, int, integer, int2, int4 |
   |          Long           |                 bigint, int8                  |
   |          Float          |                 float, float4                 |
   |         Double          |                float8, double                 |
   |  java.math.BigDecimal   |                    decimal                    |
   |      java.sql.Date      |                     date                      |
   |         String          |                 char, varchar                 |
   |         byte[]          |                     bytea                     |
   |         byte[]          |               binary, varbinary               |
   |         byte[]          |     tinyblob, blob, mediumblob, longblob      |
   |         String          |     tinytext, mediumtext, text, longtext      |

5. DataHandler类的私有方法getFieldValue用于将数据从ResultSet中提取出来，有些类型需要特别处理，如byte[]、com.huawei.agconnect.server.clouddb.request.Text、java.util.Date等类型。若出现类型转换问题，可参考以下方式处理：

   ```java
       private static Object getFieldValue(ResultSet resultSet, String fieldName, Class<?> valueType) throws SQLException {
           if (valueType == byte[].class) {
               return resultSet.getBytes(fieldName);
           } else if (valueType == Text.class) {
               return new Text(resultSet.getString(fieldName));
           } else if (valueType == Date.class) {
               return new Date(resultSet.getDate(fieldName).getTime());
           }
           return resultSet.getObject(fieldName, valueType);
       }
   ```

6. 请注意模型类中表名和字段名称大小写均需要与数据库中定义保持一致，否则可能出现表或字段不存在的问题。

## Cloud DB模型类说明

​	在Cloud DB侧下载对象类型（表）模型类，下载步骤如下：

1. <img src="image\image-20230111165732281.png" alt="image-20230111165732281" />

2. 开发者输入包名，选择确定后导出文件。示例中包名为：com.huawei.agc.cluddb.quickstart.target.model。

   <img src="image\image-20230111171343794.png" alt="image-20230111171343794" style="zoom:67%;" />

3. 将下载后的对应模型类导入至工程对应的包路径。不建议对模型类做任何代码逻辑上的变更，以免修改错误导致后续业务异常。

## 源宿表字段映射关系类说明

​	ColumnMapping.java，该类为demo实现中内部缓存源宿模型映射关系的类，开发者可作为参考使用。

​	sourceField为字段在源模型对象中定义的Field对象；targetField为字段对应到Cloud DB模型中定义的Field对象。

​	sourceColumnName和targetColumnName分别为源宿表中定义的字段名称。

​	代码如下：

```java
package com.huawei.agc.clouddb.quickstart.source;

import java.lang.reflect.Field;

/**
 * represent the relationships of source column and target column
 */
public class ColumnMapping {
    private Field sourceField;

    private Field targetField;

    private String sourceName;

    private String targetName;
	
    ...
}
```

## 数据迁移逻辑说明

​	Application.java，demo示范迁移功能的入口主函数，代码逻辑如下：

```java
        // assign zone name in Cloud DB
        String cloudDBZoneName = "functionTestDbA";
        CloudDBZone cloudDBZone;
        try {
            AuthenticateTool.init();
            AGCClient agcClient = AGCClient.getInstance(CLOUDDB_CN.getClientName());
            AGConnectCloudDB cloudDB = AGConnectCloudDB.getInstance(agcClient);

            CloudDBZoneConfig cloudDBZoneConfig = new CloudDBZoneConfig(cloudDBZoneName);
            cloudDBZone = cloudDB.openCloudDBZone(cloudDBZoneConfig);
        } catch (AGCException ex) {
            ex.printStackTrace();
            return;
        }

        // assign the tables, and do migrate or export data files
        List<Class<? extends AbstractSourceModel>> classes = new ArrayList<>();
        classes.add(SourceBookInfo.class);

        for (Class<? extends AbstractSourceModel> clazz : classes) {
            try {
                Class<? extends CloudDBZoneObject> targetClazz = clazz.getAnnotation(Table.class).targetClass();
                DataHandler<? extends AbstractSourceModel, ? extends CloudDBZoneObject> dataHandler =
                    new DataHandler<>(clazz, targetClazz);
                dataHandler.init();

                DataMigrate<? extends AbstractSourceModel, ? extends CloudDBZoneObject> dataMigrate =
                    new DataMigrate<>(dataHandler, new LoggerProgressHandler());

                // option1: migrate data with target object
                dataMigrate.migrateAsTargetObject(cloudDBZone);

                // option2: migrate data with generic object
                dataMigrate.migrateAsGenericObject(cloudDBZone);

                // option3: export data to json files
                dataMigrate.export(cloudDBZoneName);
            } catch (ServiceException ex) {
                ex.printStackTrace();
            }
        }
```

​	代码步骤说明：

1. 调用AuthenticateTool.init()函数初始化认证凭据配置文件，用于后续与Cloud DB交互请求的认证。

2. 初始化客户端，创建与Cloud DB的连接；指定Cloud DB侧目标存储区名称并初始化CloudDBZone实例。

3. 变量classes保存要迁移的表对应的模型类，可以包含多个模型类。

4. for循环用于以串行方式迁移各张表的数据，开发者也可以根据需要改为多线程执行方式。

5. 使用模型类实例化DataHandler，并调用init()接口读取模型注解信息，缓存源宿数据库表的映射关系。

6. 使用DataHandler实例对象和进度处理实现类实例化DataMigrate对象。

7. 调用DataMigrate类提供的三种示例接口实现迁移或数据导出逻辑，选择其中一种即可。

   ​	migrateAsTargetObject()接口：

   ​	将数据从源数据库读取到Cloud DB模型对象中，即target.model包路径下的CloudDBZoneObject子类对象，调用CloudDBZone.executeUpsert()接口将数据存入Cloud DB。

   ------

   ​	migrateAsGenericObject()接口：

   ​	将数据从源数据库中读取到Cloud DB提供的泛化对象CloudDBZoneGenericObject中，调用CloudDBZone.executeUpsert()接口将数据存入Cloud DB。
   
------
   
​	export()接口：
   
​	用于将源数据库中数据读取并保存到JSON文件中，该文件可以在Cloud DB的Portal页面直接作为输入文件，将数据导入。
   
​	由于使用JSON文件导入数据时对单文件中的数据条数有限制，所以export()接口导出的文件将依照数据条数限制分别导出到多个文件中，文件名称中增加序号用作区分。
   
​	JSON文件名称以Cloud DB侧目标存储区、对象类型名称、序号和下划线拼接组成，如：functionTestDbA_BookInfo_0.json。
   
​	文件输出在程序运行根路径下的output子路径下。

## 数据迁移进度

​	由于迁移数据通常是个比较耗时的过程，所以通常需要输出进度信息告知当前进展。

​	示例demo提供IProgressHandler接口类，实现该接口自定义进度输出方式，如示例ConsoleProgressHandler.java和LoggerProgressHandler.java实现，开发者也可以自定义更适合的进度输出方式。

​	为便于计算迁移进度，DataHandler类提供如下接口：

​		count()接口：查询对应表中数据总行数；

​		select(Connection, long, long)接口：用于分批查询数据，避免数据量太大时造成内存问题；

​		select(Connection)接口：查询当前表全量数据。

​	开发者请根据需要选择上述接口，用于实现数据查询以及进度管理。

​	示例代码通过分批查询的方式，批次迁移数据。根据当前表的数据总行数和已处理数据行数计算输出每张表的迁移进度。

## 运行结果

完成如上述认证文件替换、源数据库配置修改、模型注解和迁移逻辑变更后，可直接运行主函数Application.java尝试迁移功能。

如下展示迁移成功的效果（为了便于展示进度信息和导出多个批次JSON文件的效果，已将DataMigrate.java文件中将批次大小改成了1）。

源数据库（PostgreSQL）侧数据如下：

<img src="image\image-20230113122337924.png" alt="image-20230113122337924" />

迁移成功后，Cloud DB侧数据：

<img src="image\image-20230113122819277.png" alt="image-20230113122819277" />

导出JSON文件：

<img src="image\image-20230113123029119.png" alt="image-20230113123029119" />

JSON文件（functionTestDbA_BookInfo_0.json）内容（已格式化）如下：

```json
{
	"cloudDBZoneName": "functionTestDbA",
	"objectTypeName": "BookInfo",
	"objects": [{
		"publishTime": 1672416000000,
		"author": "罗贯中",
		"price": 33.3,
		"shadowFlag": true,
		"publisher": "人民教育出版社",
		"id": 1,
		"bookName": "三国演义"
	}]
}
```

日志文件中进度信息如下（三块进度信息，分别对应两个迁移接口和一个导出接口）：

```
2023-01-13 12:25:42.571 [main] [INFO] [LoggerProgressHandler.java:32] progress: 0% | start to process table[book], total size is: 4
2023-01-13 12:25:43.930 [main] [INFO] [LoggerProgressHandler.java:32] progress: 25% | processing...
2023-01-13 12:25:43.974 [main] [INFO] [LoggerProgressHandler.java:32] progress: 50% | processing...
2023-01-13 12:25:44.040 [main] [INFO] [LoggerProgressHandler.java:32] progress: 75% | processing...
2023-01-13 12:25:44.075 [main] [INFO] [LoggerProgressHandler.java:32] progress: 100% | process table[book] finished.

2023-01-13 12:25:44.113 [main] [INFO] [LoggerProgressHandler.java:32] progress: 0% | start to process table[book], total size is: 4
2023-01-13 12:25:44.166 [main] [INFO] [LoggerProgressHandler.java:32] progress: 25% | processing...
2023-01-13 12:25:44.209 [main] [INFO] [LoggerProgressHandler.java:32] progress: 50% | processing...
2023-01-13 12:25:44.244 [main] [INFO] [LoggerProgressHandler.java:32] progress: 75% | processing...
2023-01-13 12:25:44.278 [main] [INFO] [LoggerProgressHandler.java:32] progress: 100% | process table[book] finished.

2023-01-13 12:25:44.316 [main] [INFO] [LoggerProgressHandler.java:32] progress: 0% | start to process table[book], total size is: 4
2023-01-13 12:25:44.318 [main] [INFO] [LoggerProgressHandler.java:32] progress: 25% | processing...
2023-01-13 12:25:44.319 [main] [INFO] [LoggerProgressHandler.java:32] progress: 50% | processing...
2023-01-13 12:25:44.320 [main] [INFO] [LoggerProgressHandler.java:32] progress: 75% | processing...
2023-01-13 12:25:44.321 [main] [INFO] [LoggerProgressHandler.java:32] progress: 100% | process table[book] finished.
```