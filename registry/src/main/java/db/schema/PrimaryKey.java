package db.schema;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

// 标识表中主键，提升可读性
@Target(ElementType.FIELD)
public @interface PrimaryKey {
    String fieldName();
}
