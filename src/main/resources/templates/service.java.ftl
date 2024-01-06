package ${package.Service};

import com.github.pagehelper.PageInfo;
import ${package.Entity}.${entity};
import ${superServiceClassPackage};
import com.mixchains.ytboot.common.vo.ReturnVO;
import ${author}.dto.${entity}Dto;

import java.util.List;

/**
* @author ${author}
*/
<#if kotlin>
interface ${table.serviceName} : ${superServiceClass}<${entity}>
<#else>
public interface ${table.serviceName} extends ${superServiceClass}<${entity}> {

    /**
    * ${table.comment!}列表
    *
    * @param ${table.entityPath}Dto
    * @return
    */
    ReturnVO<PageInfo<${entity}>> list${entity}(${entity}Dto ${table.entityPath}Dto);

    /**
    * 根据uuid查询${table.comment!}详情
    *
    * @param uuid
    * @return
    */
    ReturnVO<${entity}> select${entity}ByUuid(String uuid);

    /**
    * 新增${table.comment!}
    *
    * @param ${table.entityPath}
    * @return
    */
    ReturnVO save${entity}(${entity} ${table.entityPath});

    /**
    * 修改${table.comment!}
    *
    * @param ${table.entityPath}
    * @return
    */
    ReturnVO update${entity}(${entity} ${table.entityPath});

    /**
    * 删除${table.comment!}
    *
    * @param uuidList
    * @return
    */
    ReturnVO delete${entity}(List<String> uuidList);
}
</#if>
