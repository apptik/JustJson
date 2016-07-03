package com.produpress.immoweb.model;

import io.apptik.json.wrapper.JsonObjectWrapper;
import io.apptik.json.*;

public class ${wrapperName} extends JsonObjectWrapper {
<#list properties as prop>

    public ${prop.returnType} get${prop.methodName}() {
        return getJson().opt${prop.optMethod}("${prop.origKey}");
    }

</#list>
}