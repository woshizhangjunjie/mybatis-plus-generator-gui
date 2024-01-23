package com.zzg.mybatis.generator.bridge;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.jcraft.jsch.Session;
import com.zzg.mybatis.generator.controller.PictureProcessStateController;
import com.zzg.mybatis.generator.model.DatabaseConfig;
import com.zzg.mybatis.generator.model.DbType;
import com.zzg.mybatis.generator.model.GeneratorConfig;
import com.zzg.mybatis.generator.plugins.DbRemarksCommentGenerator;
import com.zzg.mybatis.generator.util.ConfigHelper;
import com.zzg.mybatis.generator.util.DbUtil;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.api.ShellCallback;
import org.mybatis.generator.config.*;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;

/**
 * The bridge between GUI and the mybatis generator. All the operation to  mybatis generator should proceed through this
 * class
 * <p>
 * Created by Owen on 6/30/16.
 */
public class MybatisGeneratorBridge {

    private static final Logger _LOG = LoggerFactory.getLogger(MybatisGeneratorBridge.class);

    private GeneratorConfig generatorConfig;

    private DatabaseConfig selectedDatabaseConfig;

    private ProgressCallback progressCallback;

    private List<IgnoredColumn> ignoredColumns;

    private List<ColumnOverride> columnOverrides;

    public MybatisGeneratorBridge() {
    }

    public void setGeneratorConfig(GeneratorConfig generatorConfig) {
        this.generatorConfig = generatorConfig;
    }

    public void setDatabaseConfig(DatabaseConfig databaseConfig) {
        this.selectedDatabaseConfig = databaseConfig;
    }

    public void generate() throws Exception {
        String dbType = selectedDatabaseConfig.getDbType();

        AutoGenerator generator = new AutoGenerator();
        //模块名
        String moduleName = generatorConfig.getModelName();

        // 全局配置
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setOutputDir(generatorConfig.getProjectFolder() + "/src/main/java");
        globalConfig.setAuthor(generatorConfig.getBasePackage() + "." + moduleName);
        globalConfig.setOpen(false);
        globalConfig.setFileOverride(false);
        globalConfig.setSwagger2(true); //实体属性 Swagger2 注解
        generator.setGlobalConfig(globalConfig);

        // 数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        //dataSourceConfig.setUrl(DbUtil.getConnectionUrlWithSchema(selectedDatabaseConfig));
        dataSourceConfig.setUrl(DbUtil.getConnectionUrlWithSchema(selectedDatabaseConfig));
        //dataSourceConfig.setDriverName(DbType.valueOf(dbType).getDriverClass());
        dataSourceConfig.setDriverName(DbType.valueOf(dbType).getDriverClass());
        dataSourceConfig.setUsername(selectedDatabaseConfig.getUsername());
        dataSourceConfig.setPassword(selectedDatabaseConfig.getPassword());
        generator.setDataSource(dataSourceConfig);

        // 包配置
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setModuleName(moduleName);
        packageConfig.setParent(generatorConfig.getBasePackage());
        packageConfig.setEntity(generatorConfig.getModelPackage());
        packageConfig.setMapper(generatorConfig.getDaoPackage());
        generator.setPackageInfo(packageConfig);

        // 配置自定义代码模板
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setXml(XML_MAPPER_TEMPLATE_PATH);
        templateConfig.setMapper(MAPPER_TEMPLATE_PATH);
        templateConfig.setEntity(ENTITY_TEMPLATE_PATH);
        templateConfig.setService(SERVICE_TEMPLATE_PATH);
        templateConfig.setServiceImpl(SERVICE_IMPL_TEMPLATE_PATH);
        templateConfig.setController(CONTROLLER_TEMPLATE_PATH);
        generator.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        //strategy.setSuperEntityClass(generatorConfig.getDomainBasePackage());
        //实体类基础包路径 当修改以下自定义模板时，如修改了实体类基础包的路径，此处也需要修改 todo
        //com.yuntai.web.domain.base.BaseEntity;
        strategy.setSuperEntityClass(generatorConfig.getBasePackage() + "." + moduleName + "." + generatorConfig.getModelPackage() + ".base.BaseEntity");
        strategy.setInclude(generatorConfig.getTableName());
        String generateKeys = generatorConfig.getGenerateKeys();
        String[] split = generateKeys.split(",");
        strategy.setSuperEntityColumns(split);
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(packageConfig.getModuleName() + "_");
        generator.setStrategy(strategy);
        generator.setTemplateEngine(new FreemarkerTemplateEngine());
        //项目模块基础路径
        String basePackageUrl = generatorConfig.getBasePackage().replace(".", "/") + "/" + moduleName + "/" + generatorConfig.getModelPackage();
        //自定义模板
        generator.setCfg(templateConfig(generatorConfig.getProjectFolder() + "/src/main/java/"
                + basePackageUrl));
        generator.execute();
    }


    // xml 文件模板
    private static final String XML_MAPPER_TEMPLATE_PATH = "templates/mapper.xml";
    // mapper 文件模板
    private static final String MAPPER_TEMPLATE_PATH = "templates/mapper.java";
    // entity 文件模板
    private static final String ENTITY_TEMPLATE_PATH = "templates/entity.java";
    // service 文件模板
    private static final String SERVICE_TEMPLATE_PATH = "templates/service.java";
    // serviceImpl 文件模板
    private static final String SERVICE_IMPL_TEMPLATE_PATH = "templates/serviceImpl.java";
    // controller 文件模板
    private static final String CONTROLLER_TEMPLATE_PATH = "templates/controller.java";
    // dto 文件模板
    private static final String DTO_TEMPLATE_PATH = "templates/dto.java.ftl";
    //基础分页
    private static final String BASE_PAGE_TEMPLATE_PATH = "templates/basePage.java.ftl";
    //基础响应类
    private static final String BASE_RETURN_VO_TEMPLATE_PATH = "templates/ReturnVo.java.ftl";
    //基础实体类
    private static final String BASE_ENTITY_TEMPLATE_PATH = "templates/baseEntity.java.ftl";
    //基础序列化类
    private static final String BASE_JSON_SERIALIZER_CONFIG_TEMPLATE_PATH = "templates/jsonSerializerConfig.java.ftl";
    //基础断言类
    private static final String BASE_ASSERT_TEMPLATE_PATH = "templates/ytBootAssert.java.ftl";
    //基础异常类
    private static final String BASE_EXCEPTION_TEMPLATE_PATH = "templates/ytBootException.java.ftl";

    /**
     * 自定义生成模板类
     */
    private static InjectionConfig templateConfig(String outPath) {

        InjectionConfig config = new InjectionConfig() {
            @Override
            public void initMap() {

            }
        };

        List<FileOutConfig> files = new ArrayList<FileOutConfig>();
        // 生成分页请求类
        files.add(new FileOutConfig(DTO_TEMPLATE_PATH) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return String.format((outPath + "/dto" + File.separator + "%s" + ".java"), tableInfo.getEntityName() + "Dto");
            }
        });
        // 基础分页类
        files.add(new FileOutConfig(BASE_PAGE_TEMPLATE_PATH) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return String.format((outPath + "/base" + File.separator + "%s" + ".java"), "BasePage");
            }
        });
        // 基础响应类
        files.add(new FileOutConfig(BASE_RETURN_VO_TEMPLATE_PATH) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return String.format((outPath + "/base" + File.separator + "%s" + ".java"), "ReturnVo");
            }
        });
        // 基础实体类
        files.add(new FileOutConfig(BASE_ENTITY_TEMPLATE_PATH) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return String.format((outPath + "/base" + File.separator + "%s" + ".java"), "BaseEntity");
            }
        });
        // 基础序列化类
        files.add(new FileOutConfig(BASE_JSON_SERIALIZER_CONFIG_TEMPLATE_PATH) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return String.format((outPath + "/base" + File.separator + "%s" + ".java"), "JsonSerializerConfig");
            }
        });
        // 基础断言类
        files.add(new FileOutConfig(BASE_ASSERT_TEMPLATE_PATH) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return String.format((outPath + "/base" + File.separator + "%s" + ".java"), "YTBootAssert");
            }
        });
        // 基础异常类
        files.add(new FileOutConfig(BASE_EXCEPTION_TEMPLATE_PATH) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return String.format((outPath + "/base" + File.separator + "%s" + ".java"), "YtBootException");
            }
        });
        config.setFileOutConfigList(files);
        return config;
    }

    private String getMappingXMLFilePath(GeneratorConfig generatorConfig) {
        StringBuilder sb = new StringBuilder();
        sb.append(generatorConfig.getProjectFolder()).append("/");
        sb.append(generatorConfig.getMappingXMLTargetFolder()).append("/");
        String mappingXMLPackage = generatorConfig.getMappingXMLPackage();
        if (StringUtils.isNotEmpty(mappingXMLPackage)) {
            sb.append(mappingXMLPackage.replace(".", "/")).append("/");
        }
        sb.append(generatorConfig.getDomainObjectName()).append("Mapper.xml");
        return sb.toString();
    }

    public void setProgressCallback(ProgressCallback progressCallback) {
        this.progressCallback = progressCallback;
    }

    public void setIgnoredColumns(List<IgnoredColumn> ignoredColumns) {
        this.ignoredColumns = ignoredColumns;
    }

    public void setColumnOverrides(List<ColumnOverride> columnOverrides) {
        this.columnOverrides = columnOverrides;
    }
}
