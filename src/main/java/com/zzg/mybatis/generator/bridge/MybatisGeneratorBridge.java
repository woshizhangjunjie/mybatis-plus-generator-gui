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
        strategy.setSuperEntityClass(generatorConfig.getDomainBasePackage());
        strategy.setInclude(generatorConfig.getTableName());
        String generateKeys = generatorConfig.getGenerateKeys();
        String[] split = generateKeys.split(",");
        strategy.setSuperEntityColumns(split);
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(packageConfig.getModuleName() + "_");
        generator.setStrategy(strategy);
        generator.setTemplateEngine(new FreemarkerTemplateEngine());

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
