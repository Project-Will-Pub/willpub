package xyz.rk0cc.willpub.ui.event;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;

/**
 * An {@link Event} that listening directory changes.
 * <br/>
 * This event has 3 subclasses: {@link DirectoryChangesAppliedEvent}, {@link DirectoryChangesFailedEvent} and
 * {@link DirectoryChangesResetEvent}. Which will be fired depending on given directory ({@link File} object) is valid
 * to applied. Therefore, call {@link DirectoryChangesEventHandler} that
 * {@linkplain javafx.scene.Node#addEventHandler(EventType, EventHandler) attached} in {@link javafx.scene.Node}
 * already.
 *
 * @since 1.0.0
 */
public abstract sealed class DirectoryChangesEvent extends Event {
    /**
     * Enumerated {@link DirectoryChangesEvent}'s {@link EventType} name.
     *
     * @since 1.0.0
     */
    public enum DirectoryChangesEventTypeName {
        /**
         * Enumerated value for {@link DirectoryChangesAppliedEvent}'s {@link EventType} name.
         */
        APPLIED,
        /**
         * Enumerated value for {@link DirectoryChangesFailedEvent}'s {@link EventType} name.
         */
        FAILED,
        /**
         * Enumerated value for {@link DirectoryChangesResetEvent}'s {@link EventType} name.
         */
        RESET;

        /**
         * Non-enumerated field that uses to be {@linkplain DirectoryChangesEvent the abstracted event}'s
         * {@link EventType} name.
         */
        public static final String MASTER_NAME = "DIRECTORY_CHANGES";

        /**
         * A {@link String} uses for naming subclasses of {@link DirectoryChangesEvent}'s {@link EventType} name.
         *
         * @return {@link EventType} name, which is <code>CHANGES_{@linkplain #name() (value's name)}</code>.
         */
        @Nonnull
        public String getEventTypeName() {
            return "CHANGES_" + name();
        }
    }

    /**
     * A master {@link EventType} that representing {@link DirectoryChangesEvent}.
     */
    public static final EventType<DirectoryChangesEvent> DIRECTORY_CHANGES_EVENT_TYPE
            = new EventType<>(DirectoryChangesEventTypeName.MASTER_NAME);

    /**
     * Constructor of {@link DirectoryChangesEvent}.
     * <br/>
     * This constructor must be private that the parameter should not use {@link EventType} in reality. Therefore,
     * to identify inherited {@link DirectoryChangesEvent}'s type.
     *
     * @param eventType {@link EventType} which come from subclasses.
     */
    private DirectoryChangesEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }

    /**
     * Specify which method in the {@linkplain DirectoryChangesEventHandler handler} will be called when invoking
     * {@link DirectoryChangesEventHandler#handle(DirectoryChangesEvent)}.
     *
     * @param handler {@linkplain DirectoryChangesEventHandler Handler} which request to handle.
     */
    abstract void invokeHandler(@Nonnull DirectoryChangesEventHandler handler);

    /**
     * Subclass of {@link DirectoryChangesEvent} that the directory changes is applied and ready to read context.
     *
     * @since 1.0.0
     */
    public static final class DirectoryChangesAppliedEvent extends DirectoryChangesEvent {
        /**
         * {@link EventType} uses for {@link DirectoryChangesAppliedEvent}.
         */
        public static final EventType<DirectoryChangesAppliedEvent> DIRECTORY_CHANGES_APPLIED_EVENT_TYPE
                = new EventType<>(DIRECTORY_CHANGES_EVENT_TYPE, DirectoryChangesEventTypeName.APPLIED.getEventTypeName());

        /**
         * The {@link File} object that selected to be opened.
         */
        private final File newDir;

        /**
         * Construct an event that the directory is applied successfully.
         *
         * @param newDir New directory's {@link File} object.
         */
        public DirectoryChangesAppliedEvent(@Nonnull File newDir) {
            super(DIRECTORY_CHANGES_APPLIED_EVENT_TYPE);
            this.newDir = newDir;
        }

        @Override
        void invokeHandler(@Nonnull DirectoryChangesEventHandler handler) {
            handler.onApplied(newDir);
        }

        public File getNewDir() {
            return newDir;
        }
    }

    /**
     * Subclass of event that the directory can not be applied with some reason.
     * <br/>
     * Note: It does not invoke after {@linkplain DirectoryChangesAppliedEvent applied} already.
     *
     * @since 1.0.0
     */
    public static final class DirectoryChangesFailedEvent extends DirectoryChangesEvent {
        /**
         * {@link EventType} uses for {@link DirectoryChangesFailedEvent}.
         */
        public static final EventType<DirectoryChangesFailedEvent> DIRECTORY_CHANGES_FAILED_EVENT_TYPE
                = new EventType<>(DIRECTORY_CHANGES_EVENT_TYPE, DirectoryChangesEventTypeName.FAILED.getEventTypeName());

        /**
         * The {@link String} of the applied directory path that can not be applied.
         */
        private final String invalidDirPath;

        /**
         * (Optional) {@link Throwable} object which threw when applying directory.
         */
        private final Throwable throwable;

        /**
         * Construct an event when the given directory unable to apply.
         *
         * @param invalidDirPath A {@link String} which going to apply.
         * @param throwable {@link Throwable} object throw when applying directory.
         */
        public DirectoryChangesFailedEvent(@Nonnull String invalidDirPath, @Nullable Throwable throwable) {
            super(DIRECTORY_CHANGES_FAILED_EVENT_TYPE);
            this.invalidDirPath = invalidDirPath;
            this.throwable = throwable;
        }

        /**
         * Construct an event when the given directory unable to apply.
         *
         * @param invalidDirPath A {@link String} which going to apply.
         */
        public DirectoryChangesFailedEvent(@Nonnull String invalidDirPath) {
            this(invalidDirPath, null);
        }

        @Override
        void invokeHandler(@Nonnull DirectoryChangesEventHandler handler) {
            handler.onFailed(invalidDirPath, throwable);
        }

        public String getInvalidDirPath() {
            return invalidDirPath;
        }

        public Throwable getThrowable() {
            return throwable;
        }
    }

    /**
     * Subclass of event that reset to no directory opened state.
     *
     * @since 1.0.0
     */
    public static final class DirectoryChangesResetEvent extends DirectoryChangesEvent {
        /**
         * {@link EventType} uses for {@link DirectoryChangesResetEvent}.
         */
        public static final EventType<DirectoryChangesResetEvent> DIRECTORY_CHANGES_RESET_EVENT_TYPE
                = new EventType<>(DIRECTORY_CHANGES_EVENT_TYPE, DirectoryChangesEventTypeName.RESET.getEventTypeName());

        /**
         * Construct an event that reset to no project state.
         */
        public DirectoryChangesResetEvent() {
            super(DIRECTORY_CHANGES_RESET_EVENT_TYPE);
        }

        @Override
        void invokeHandler(@Nonnull DirectoryChangesEventHandler handler) {
            handler.onReset();
        }
    }
}
