package ${package.Mapper};

import ${package.Entity}.${entity};
import ${superMapperClassPackage};
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* @author ${author}
*/
<#if kotlin>
interface ${table.mapperName} : ${superMapperClass}<${entity}>
<#else>
public interface ${table.mapperName} extends ${superMapperClass}<${entity}> {

    /**
    * 根据uuid查询返回实体类
    *
    * @param uuid
    * @return
    */
    @Select("SELECT * FROM ${table.name} WHERE uuid=<#noparse>#{</#noparse>uuid} AND deleted = 0")
    ${entity} selectByUuid(@Param("uuid") String uuid);

}
</#if>
