package com.xunmeng.codura.setting.component.annotation;

import com.xunmeng.codura.constants.enums.ComponentType;
import com.xunmeng.codura.constants.enums.Type;

import java.lang.annotation.*;

@Documented
@Inherited
@Target({ElementType.FIELD})  //可以在字段、枚举的常量、方法
@Retention(RetentionPolicy.RUNTIME)
public @interface UIComponent {
    String label() default "";
    ComponentType componentType() default ComponentType.JTEXTFILED;
    Class<? extends Type> tabs() default Type.class;
    boolean disabled() default false;
    int order() default 0;
}
