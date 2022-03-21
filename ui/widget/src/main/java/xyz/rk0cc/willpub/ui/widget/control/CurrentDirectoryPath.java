package xyz.rk0cc.willpub.ui.widget.control;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Control;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.DirectoryChooser;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2OutlinedAL;
import xyz.rk0cc.willpub.ui.event.DirectoryChangesEventHandler;

import javax.annotation.Nonnull;

import java.io.File;
import java.nio.file.NotDirectoryException;
import java.util.Objects;

import static xyz.rk0cc.willpub.ui.event.DirectoryChangesEvent.*;

/**
 * Member of {@link Control} object that displaying a directory which currently opened.
 *
 * @since 1.0.0
 */
public final class CurrentDirectoryPath extends Control {
    /**
     * Textfield that displaying current opened directory (and does not allow edited by user).
     */
    private final MFXTextField txfCurrentPath = new MFXTextField();
    /**
     * Button that invoke open project directory action.
     */
    private final MFXButton btnOpenDir = new MFXButton();
    /**
     * Button that reset to no project directory state.
     */
    private final MFXButton btnReset = new MFXButton();

    /**
     * Construct {@link CurrentDirectoryPath} controller and ready to added.
     */
    public CurrentDirectoryPath() {
        // Set constant value of buttons
        final double BUTTON_HEIGHT = 45d;
        final int BUTTON_ICON_SIZE = 28;
        final Insets BUTTON_PADDING = new Insets(5d);

        // Container
        HBox container = new HBox();
        container.setMaxWidth(Double.MAX_VALUE);
        container.setAlignment(Pos.CENTER);
        container.setSpacing(10d);
        
        // Current path widget
        HBox.setHgrow(txfCurrentPath, Priority.ALWAYS); // Expand most of space
        txfCurrentPath.setFloatingText("Dart project path:");
        txfCurrentPath.setAllowEdit(false); // Display only
        txfCurrentPath.setPadding(new Insets(7.5d));

        // Open directory button
        // Open directory icon
        FontIcon btnOpenDirIcon = new FontIcon();
        btnOpenDirIcon.setIconCode(Material2OutlinedAL.FOLDER_OPEN);
        btnOpenDirIcon.setIconSize(BUTTON_ICON_SIZE);
        // Open directory button preference
        btnOpenDir.setGraphic(btnOpenDirIcon);
        btnOpenDir.setText("Open...");
        btnOpenDir.setGraphicTextGap(10d);
        btnOpenDir.setAccessibleText("Open directory");
        btnOpenDir.setPrefHeight(BUTTON_HEIGHT);
        btnOpenDir.setMinHeight(USE_PREF_SIZE);
        btnOpenDir.setMaxHeight(Double.MAX_VALUE);
        btnOpenDir.setPadding(BUTTON_PADDING);

        // Handle when button clicked
        btnOpenDir.setOnAction((event) -> {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Select Dart project directory");
            chooser.setInitialDirectory(new File(System.getProperty("user.home")));
            
            // Get selected directory
            File chooseDirectory = chooser.showDialog(CurrentDirectoryPath.super.getScene().getWindow());

            if (Objects.isNull(chooseDirectory)) {
                // Same as cancel
                return;
            } else if (!chooseDirectory.isAbsolute()) {
                // Not absolute path
                CurrentDirectoryPath.super.fireEvent(new DirectoryChangesFailedEvent(
                        chooseDirectory.getPath(),
                        new IllegalArgumentException("Required absolute path when opening new Dart project directory")
                ));

                return;
            } else if (!chooseDirectory.isDirectory()) {
                // Not an existed directory
                CurrentDirectoryPath.super.fireEvent(new DirectoryChangesFailedEvent(
                        chooseDirectory.getPath(),
                        new NotDirectoryException(chooseDirectory.getPath())
                ));

                return;
            }

            // Is a directory
            txfCurrentPath.setText(chooseDirectory.getPath());
            CurrentDirectoryPath.super.fireEvent(new DirectoryChangesAppliedEvent(chooseDirectory));
        });

        // Reset button
        // Reset button icon
        FontIcon btnResetIcon = new FontIcon();
        btnResetIcon.setIconCode(Material2AL.CLOSE);
        btnResetIcon.setIconSize(BUTTON_ICON_SIZE);
        // Prefernce
        btnReset.setGraphic(btnResetIcon);
        btnReset.setPrefSize(BUTTON_HEIGHT, BUTTON_HEIGHT);
        btnReset.setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
        btnReset.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        btnReset.setAccessibleText("Reset");
        btnReset.setPadding(BUTTON_PADDING);

        // Bind reset action
        btnReset.setOnAction((event) -> {
            // Clear current path and fire reset event
            txfCurrentPath.clear();
            CurrentDirectoryPath.super.fireEvent(new DirectoryChangesResetEvent());
        });

        // Wrap up
        container.getChildren().addAll(txfCurrentPath, btnOpenDir, btnReset);
        this.getChildren().add(container);
    }

    /**
     * Get a path of directory that currently opened.
     *
     * @return A {@link String} of directory path.
     */
    @Nonnull
    public String getCurrentPath() {
        return txfCurrentPath.getText();
    }

    /**
     * Get a directory that currently opened.
     *
     * @return {@link File} of the directory.
     */
    @Nonnull
    public File getCurrentPathInFile() {
        return new File(getCurrentPath());
    }

    /**
     * Programmatically click the open button to let user select another directory.
     *
     * @see #resetDirectory()
     */
    public void openDirectory() {
        btnOpenDir.fire();
    }

    /**
     * Programmatically click the reset button to reset to no project state.
     *
     * @see #openDirectory()
     */
    public void resetDirectory() {
        btnReset.fire();
    }

    /**
     * Apply custom implemented {@link DirectoryChangesEventHandler} that to provide a complete action when directory
     * changes.
     * <br/>
     * This helps to implement all {@link xyz.rk0cc.willpub.ui.event.DirectoryChangesEvent} without messing code
     * by multiple call {@link #addEventHandler(EventType, EventHandler)}.
     * 
     * @param handler Implemented handler when the event fired.
     *                
     * @see #addEventHandler(EventType, EventHandler)
     * @see #addDirectoryChangesAppliedHandler(EventHandler)
     * @see #addDirectoryChangesFailedHandler(EventHandler)
     * @see #addDirectoryChangesResetHandler(EventHandler)
     */
    public void addDirectoryChangesHandler(@Nonnull DirectoryChangesEventHandler handler) {
        addEventHandler(DIRECTORY_CHANGES_EVENT_TYPE, handler);
    }

    /**
     * Remove custom implemented {@link DirectoryChangesEventHandler} that to provide a complete action when directory
     * changes.
     * <br/>
     * This helps to implement all {@link xyz.rk0cc.willpub.ui.event.DirectoryChangesEvent} without messing code
     * by multiple call {@link #addEventHandler(EventType, EventHandler)}.
     *
     * @param handler <b>Same</b> implemented handler which
     *                {@linkplain #addDirectoryChangesHandler(DirectoryChangesEventHandler) added} already.
     *
     * @see #removeEventHandler(EventType, EventHandler)
     * @see #removeDirectoryChangesAppliedHandler(EventHandler)
     * @see #removeDirectoryChangesFailedHandler(EventHandler)
     * @see #removeDirectoryChangesResetHandler(EventHandler)
     */
    public void removeDirectoryChangesHandler(@Nonnull DirectoryChangesEventHandler handler) {
        removeEventHandler(DIRECTORY_CHANGES_EVENT_TYPE, handler);
    }

    /**
     * Implement dedicated {@link EventHandler} when {@link DirectoryChangesAppliedEvent} fired.
     *
     * @param handler A handler when the directory changes applied.
     *                
     * @see #addEventHandler(EventType, EventHandler) 
     * @see #addDirectoryChangesHandler(DirectoryChangesEventHandler) 
     */
    public void addDirectoryChangesAppliedHandler(@Nonnull EventHandler<DirectoryChangesAppliedEvent> handler) {
        addEventHandler(DirectoryChangesAppliedEvent.DIRECTORY_CHANGES_APPLIED_EVENT_TYPE, handler);
    }

    /**
     * Remove {@linkplain #addDirectoryChangesAppliedHandler(EventHandler) added} dedicated {@link EventHandler} when
     * {@link DirectoryChangesAppliedEvent} fired.
     *
     * @param handler A handler when the directory changes applied.
     *
     * @see #removeEventHandler(EventType, EventHandler) 
     * @see #removeDirectoryChangesHandler(DirectoryChangesEventHandler) 
     */
    public void removeDirectoryChangesAppliedHandler(@Nonnull EventHandler<DirectoryChangesAppliedEvent> handler) {
        removeEventHandler(DirectoryChangesAppliedEvent.DIRECTORY_CHANGES_APPLIED_EVENT_TYPE, handler);
    }

    /**
     * Implement dedicated {@link EventHandler} when {@link DirectoryChangesFailedEvent} fired.
     *
     * @param handler A handler when the directory changes failed.
     *                
     * @see #addEventHandler(EventType, EventHandler) 
     * @see #addDirectoryChangesHandler(DirectoryChangesEventHandler) 
     */
    public void addDirectoryChangesFailedHandler(@Nonnull EventHandler<DirectoryChangesFailedEvent> handler) {
        addEventHandler(DirectoryChangesFailedEvent.DIRECTORY_CHANGES_FAILED_EVENT_TYPE, handler);
    }

    /**
     * Remove {@linkplain #addDirectoryChangesFailedHandler(EventHandler) added} dedicated {@link EventHandler} when
     * {@link DirectoryChangesFailedEvent} fired.
     *
     * @param handler A handler when the directory changes failed.
     *
     * @see #removeEventHandler(EventType, EventHandler) 
     * @see #removeDirectoryChangesHandler(DirectoryChangesEventHandler) 
     */
    public void removeDirectoryChangesFailedHandler(@Nonnull EventHandler<DirectoryChangesFailedEvent> handler) {
        removeEventHandler(DirectoryChangesFailedEvent.DIRECTORY_CHANGES_FAILED_EVENT_TYPE, handler);
    }

    /**
     * Implement dedicated {@link EventHandler} when {@link DirectoryChangesResetEvent} fired.
     *
     * @param handler A handler when reset to no project directory state.
     *                
     * @see #addEventHandler(EventType, EventHandler) 
     * @see #addDirectoryChangesHandler(DirectoryChangesEventHandler) 
     */
    public void addDirectoryChangesResetHandler(@Nonnull EventHandler<DirectoryChangesResetEvent> handler) {
        addEventHandler(DirectoryChangesResetEvent.DIRECTORY_CHANGES_RESET_EVENT_TYPE, handler);
    }

    /**
     * Remove {@linkplain #addDirectoryChangesResetHandler(EventHandler) added} dedicated {@link EventHandler} when
     * {@link DirectoryChangesResetEvent} fired.
     *
     * @param handler A handler when reset to no project directory state.
     *
     * @see #removeEventHandler(EventType, EventHandler) 
     * @see #removeDirectoryChangesHandler(DirectoryChangesEventHandler) 
     */
    public void removeDirectoryChangesResetHandler(@Nonnull EventHandler<DirectoryChangesResetEvent> handler) {
        removeEventHandler(DirectoryChangesResetEvent.DIRECTORY_CHANGES_RESET_EVENT_TYPE, handler);
    }

    /**
     * Apply custom implemented {@link DirectoryChangesEventHandler} as a filter that to provide a filter 
     * when directory changes.
     * <br/>
     * This helps to implement all {@link xyz.rk0cc.willpub.ui.event.DirectoryChangesEvent} without messing code
     * by multiple call {@link #addEventFilter(EventType, EventHandler)}.
     * 
     * @param handler Implemented filter when the event fired.
     *                
     * @see #addEventFilter(EventType, EventHandler)
     * @see #addDirectoryChangesAppliedFilter(EventHandler)
     * @see #addDirectoryChangesFailedFilter(EventHandler)
     * @see #addDirectoryChangesResetFilter(EventHandler)
     */
    public void addDirectoryChangesFilter(@Nonnull DirectoryChangesEventHandler handler) {
        addEventFilter(DIRECTORY_CHANGES_EVENT_TYPE, handler);
    }

    /**
     * Remove custom implemented {@link DirectoryChangesEventHandler} that to provide a filter when directory
     * changes.
     * <br/>
     * This helps to implement all {@link xyz.rk0cc.willpub.ui.event.DirectoryChangesEvent} without messing code
     * by multiple call {@link #addEventHandler(EventType, EventHandler)}.
     *
     * @param handler <b>Same</b> implemented filter which
     *                {@linkplain #addDirectoryChangesFilter(DirectoryChangesEventHandler) added} already.
     *
     * @see #removeEventFilter(EventType, EventHandler)
     * @see #removeDirectoryChangesAppliedFilter(EventHandler)
     * @see #removeDirectoryChangesFailedFilter(EventHandler)
     * @see #removeDirectoryChangesResetFilter(EventHandler)
     */
    public void removeDirectoryChangesFilter(@Nonnull DirectoryChangesEventHandler handler) {
        removeEventFilter(DIRECTORY_CHANGES_EVENT_TYPE, handler);
    }

    /**
     * Implement dedicated {@link EventHandler} as a filter when {@link DirectoryChangesAppliedEvent} fired.
     *
     * @param handler A filter when the directory changes applied.
     *                
     * @see #addEventFilter(EventType, EventHandler) 
     * @see #addDirectoryChangesFilter(DirectoryChangesEventHandler) 
     */
    public void addDirectoryChangesAppliedFilter(@Nonnull EventHandler<DirectoryChangesAppliedEvent> handler) {
        addEventFilter(DirectoryChangesAppliedEvent.DIRECTORY_CHANGES_APPLIED_EVENT_TYPE, handler);
    }

    /**
     * Remove {@linkplain #addDirectoryChangesAppliedFilter(EventHandler) added} dedicated {@link EventHandler} when
     * {@link DirectoryChangesAppliedEvent} fired.
     *
     * @param handler A handler when the directory changes applied.
     *
     * @see #removeEventFilter(EventType, EventHandler) 
     * @see #removeDirectoryChangesFilter(DirectoryChangesEventHandler) 
     */
    public void removeDirectoryChangesAppliedFilter(@Nonnull EventHandler<DirectoryChangesAppliedEvent> handler) {
        removeEventFilter(DirectoryChangesAppliedEvent.DIRECTORY_CHANGES_APPLIED_EVENT_TYPE, handler);
    }

    /**
     * Implement dedicated {@link EventHandler} as a filter when {@link DirectoryChangesFailedEvent} fired.
     *
     * @param handler A filter when the directory changes failed.
     *                
     * @see #addEventFilter(EventType, EventHandler) 
     * @see #addDirectoryChangesFilter(DirectoryChangesEventHandler) 
     */
    public void addDirectoryChangesFailedFilter(@Nonnull EventHandler<DirectoryChangesFailedEvent> handler) {
        addEventFilter(DirectoryChangesFailedEvent.DIRECTORY_CHANGES_FAILED_EVENT_TYPE, handler);
    }

    /**
     * Remove {@linkplain #addDirectoryChangesFailedFilter(EventHandler) added} dedicated {@link EventHandler} when
     * {@link DirectoryChangesFailedEvent} fired.
     *
     * @param handler A handler when the directory changes failed.
     *
     * @see #removeEventFilter(EventType, EventHandler) 
     * @see #removeDirectoryChangesFilter(DirectoryChangesEventHandler) 
     */
    public void removeDirectoryChangesFailedFilter(@Nonnull EventHandler<DirectoryChangesFailedEvent> handler) {
        removeEventFilter(DirectoryChangesFailedEvent.DIRECTORY_CHANGES_FAILED_EVENT_TYPE, handler);
    }

    /**
     * Implement dedicated {@link EventHandler} as a filter when {@link DirectoryChangesResetEvent} fired.
     *
     * @param handler A filter when reset to no project directory state.
     *                
     * @see #addEventFilter(EventType, EventHandler) 
     * @see #addDirectoryChangesFilter(DirectoryChangesEventHandler) 
     */
    public void addDirectoryChangesResetFilter(@Nonnull EventHandler<DirectoryChangesResetEvent> handler) {
        addEventFilter(DirectoryChangesResetEvent.DIRECTORY_CHANGES_RESET_EVENT_TYPE, handler);
    }

    /**
     * Remove {@linkplain #addDirectoryChangesResetFilter(EventHandler) added} dedicated {@link EventHandler} when
     * {@link DirectoryChangesResetEvent} fired.
     *
     * @param handler A filter when reset to no project directory state.
     *
     * @see #removeEventFilter(EventType, EventHandler) 
     * @see #removeDirectoryChangesFilter(DirectoryChangesEventHandler) 
     */
    public void removeDirectoryChangesResetFilter(@Nonnull EventHandler<DirectoryChangesResetEvent> handler) {
        removeEventFilter(DirectoryChangesResetEvent.DIRECTORY_CHANGES_RESET_EVENT_TYPE, handler);
    }
}
