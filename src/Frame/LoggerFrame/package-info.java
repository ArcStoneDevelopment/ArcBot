/**
 * Provides all resources necessary for the bot's three-level logging system. When put to proper use, the logging system
 * provided by this custom annotation-based framework is incredibly powerful and provides a single access point for logging
 * on three layers: (1) Direct console output. (2) Text file output. (3) Log channel on the designated server. Furthermore,
 * this framework provides combinations. Such that when an annotation is set for file output, console output also occurs.
 * And when an annotation is set for log channel output, both file and console outputs also occur. This system makes it
 * easy for the bot to have a direct, simple and verbose logging system.
 *
 * <br><br> <strong>How To Use:</strong>
 * <p>
 *     Using the logger framework is very simple! Two lines of code are necessary. An example of these lines at work can
 *     be seen below. Above the method to be logged, you have to declare the annotation {@code @Logger()} and inside the
 *     parenthesis, you have to declare a logger policy from {@link Frame.LoggerFrame.LoggerPolicy}. Once you've done that, all
 *     you have to do is call the logger method from {@link Frame.LoggerFrame.LoggerCore}.
 *     <pre>
 *     {@literal @}{@code Logger (LoggerPolicy.File)
 *     public void myMethod(int parameterOne, String parameterTwo) {
 *         //Other Code Here
 *         LoggerCore.log(new Object(){}.getClass().getEnclosingMethod(), true, "Log This Message!");
 *     }
 *    }</pre>
 * <br><br> <strong>Method Inference:</strong>
 * <p>
 *     For most cases, the logger framework can infer the calling method. This is a convenience addition in version 2.0 to
 *     make using this framework even easier! To understand when you can (and when you can't) use the method inference
 *     features of this framework, please read the {@link Frame.LoggerFrame.LoggerCore} documentation. If you determine
 *     that you can use method inference, you can use the following form:
 *     <pre>
 *     {@literal @}{@code Logger (LoggerPolicy.File)
 *     public void myMethod(int parameterOne, String parameterTwo) {
 *         //Other Code Here
 *         LoggerCore.log(true, "Log This Message!");
 *     }
 *    }</pre>
 * @author ArcStone Development LLC
 * @version v2.0
 * @since v1.5
 */
package Frame.LoggerFrame;