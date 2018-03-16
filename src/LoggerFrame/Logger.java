package LoggerFrame;

import java.lang.annotation.*;

/**
 * The key annotation required for determining which logger tier to use. Each method may only have one {@link LoggerPolicy}
 * assigned to it. Assigning of this policy is done through this annotation. This annotation may only be applied to a
 * method, and it may only be applied one time. (Not repeatable). The logger annotation declaration is expected to be
 *  with a {@link LoggerPolicy} failure to do this will halt compile.
 *
 *  @author ArcStone Development LLC
 *  @version v1.5
 *  @since v1.5
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Logger {
    /**
     * The Logger policy to be annotated to a given method
     * @return {@link LoggerPolicy}
     */
    LoggerPolicy value();
}
