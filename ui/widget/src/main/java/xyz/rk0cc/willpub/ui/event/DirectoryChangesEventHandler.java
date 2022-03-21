package xyz.rk0cc.willpub.ui.event;

import javafx.event.EventHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;

/**
 * An {@link EventHandler} that take actions when {@link DirectoryChangesEvent} is fired.
 *
 * @since 1.0.0
 */
public abstract class DirectoryChangesEventHandler implements EventHandler<DirectoryChangesEvent> {
    /**
     * Method that will be called when directory changes is applied.
     *
     * @param newDir New directory that ready to read.
     */
    public abstract void onApplied(@Nonnull File newDir);

    /**
     * Method that will be called when directory changes is failed.
     *
     * @param invalidDirPath A {@link String} of directory path that can not be applied.
     * @param throwable Optionally provided a {@link Throwable} which can be thrown.
     */
    public abstract void onFailed(@Nonnull String invalidDirPath, @Nullable Throwable throwable);

    /**
     * Method that will be called when request to reset to no directory opened state.
     */
    public abstract void onReset();

    /**
     * Implemented method from {@link EventHandler} that make a relay role to invoke {@link #onApplied(File)},
     * {@link #onFailed(String, Throwable)} or {@link #onReset()}.
     *
     * @param event Fired {@link DirectoryChangesEvent}.
     */
    @Override
    public final void handle(@Nonnull DirectoryChangesEvent event) {
        event.invokeHandler(this);
    }
}
