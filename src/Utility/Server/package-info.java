/**
 * Provides the {@link Utility.Server.Server} object, which is the bot's server-specific data storage.
 * <br> Classes in this package, besides the {@link Utility.Server.Server} class itself, have package-private constructors
 * to allow the objects to only be built by the {@link Utility.Server.Server} constructor. The classes in this package
 * each contain pieces of information that are required for the {@link Utility.Server.Server} object. All classes in
 * this package implement {@link java.io.Serializable} so that the master {@link Utility.Server.Server} object may be stored
 * in SQL.
 *
 * @author ArcStone Development LLC
 * @version v2.0
 * @since v2.0
 */
package Utility.Server;