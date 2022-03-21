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
    private final MFXTextField txfCurrentPath = new MFXTextField();
    private final MFXButton btnOpenDir = new MFXButton();
    private final MFXButton btnReset = new MFXButton();

    public CurrentDirectoryPath() {
        final double BUTTON_HEIGHT = 45d;
        final int BUTTON_ICON_SIZE = 28;
        final Insets BUTTON_PADDING = new Insets(5d);

        HBox container = new HBox();
        container.setMaxWidth(Double.MAX_VALUE);
        container.setAlignment(Pos.CENTER);
        container.setSpacing(10d);

        HBox.setHgrow(txfCurrentPath, Priority.ALWAYS);
        txfCurrentPath.setFloatingText("Dart project path:");
        txfCurrentPath.setAllowEdit(false);

        FontIcon btnOpenDirIcon = new FontIcon();
        btnOpenDirIcon.setIconCode(Material2OutlinedAL.FOLDER_OPEN);
        btnOpenDirIcon.setIconSize(BUTTON_ICON_SIZE);

        btnOpenDir.setGraphic(btnOpenDirIcon);
        btnOpenDir.setText("Open...");
        btnOpenDir.setGraphicTextGap(10d);
        btnOpenDir.setAccessibleText("Open directory");
        btnOpenDir.setPrefHeight(BUTTON_HEIGHT);
        btnOpenDir.setMinHeight(USE_PREF_SIZE);
        btnOpenDir.setMaxHeight(Double.MAX_VALUE);
        btnOpenDir.setPadding(BUTTON_PADDING);

        btnOpenDir.setOnAction((event) -> {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Select Dart project directory");
            chooser.setInitialDirectory(new File(System.getProperty("user.home")));

            File chooseDirectory = chooser.showDialog(CurrentDirectoryPath.super.getScene().getWindow());

            if (Objects.isNull(chooseDirectory)) {
                return;
            } else if (!chooseDirectory.isAbsolute()) {
                CurrentDirectoryPath.super.fireEvent(new DirectoryChangesFailedEvent(
                        chooseDirectory.getPath(),
                        new IllegalArgumentException("Required absolute path when opening new Dart project directory")
                ));

                return;
            } else if (!chooseDirectory.isDirectory()) {
                CurrentDirectoryPath.super.fireEvent(new DirectoryChangesFailedEvent(
                        chooseDirectory.getPath(),
                        new NotDirectoryException(chooseDirectory.getPath())
                ));

                return;
            }

            txfCurrentPath.setText(chooseDirectory.getPath());
            CurrentDirectoryPath.super.fireEvent(new DirectoryChangesAppliedEvent(chooseDirectory));
        });

        FontIcon btnResetIcon = new FontIcon();
        btnResetIcon.setIconCode(Material2AL.CLOSE);
        btnResetIcon.setIconSize(BUTTON_ICON_SIZE);

        btnReset.setGraphic(btnResetIcon);
        btnReset.setPrefSize(BUTTON_HEIGHT, BUTTON_HEIGHT);
        btnReset.setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
        btnReset.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        btnReset.setAccessibleText("Reset");
        btnReset.setPadding(BUTTON_PADDING);

        btnReset.setOnAction((event) -> {
            txfCurrentPath.clear();

            CurrentDirectoryPath.super.fireEvent(new DirectoryChangesResetEvent());
        });

        container.getChildren().addAll(txfCurrentPath, btnOpenDir, btnReset);
        this.getChildren().add(container);
    }

    @Nonnull
    public String getCurrentPath() {
        return txfCurrentPath.getText();
    }

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
     * Apply custom implemented {@link DirectoryChangesEventHandler} that to provide a complete action when directory
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
     */
    public void removeDirectoryChangesAppliedHandler(@Nonnull EventHandler<DirectoryChangesAppliedEvent> handler) {
        removeEventHandler(DirectoryChangesAppliedEvent.DIRECTORY_CHANGES_APPLIED_EVENT_TYPE, handler);
    }

    public void addDirectoryChangesFailedHandler(@Nonnull EventHandler<DirectoryChangesFailedEvent> handler) {
        addEventHandler(DirectoryChangesFailedEvent.DIRECTORY_CHANGES_FAILED_EVENT_TYPE, handler);
    }

    public void removeDirectoryChangesFailedHandler(@Nonnull EventHandler<DirectoryChangesFailedEvent> handler) {
        removeEventHandler(DirectoryChangesFailedEvent.DIRECTORY_CHANGES_FAILED_EVENT_TYPE, handler);
    }

    public void addDirectoryChangesResetHandler(@Nonnull EventHandler<DirectoryChangesResetEvent> handler) {
        addEventHandler(DirectoryChangesResetEvent.DIRECTORY_CHANGES_RESET_EVENT_TYPE, handler);
    }

    public void removeDirectoryChangesResetHandler(@Nonnull EventHandler<DirectoryChangesResetEvent> handler) {
        removeEventHandler(DirectoryChangesResetEvent.DIRECTORY_CHANGES_RESET_EVENT_TYPE, handler);
    }

    public void addDirectoryChangesFilter(@Nonnull DirectoryChangesEventHandler handler) {
        addEventFilter(DIRECTORY_CHANGES_EVENT_TYPE, handler);
    }

    public void removeDirectoryChangesFilter(@Nonnull DirectoryChangesEventHandler handler) {
        removeEventFilter(DIRECTORY_CHANGES_EVENT_TYPE, handler);
    }

    public void addDirectoryChangesAppliedFilter(@Nonnull EventHandler<DirectoryChangesAppliedEvent> handler) {
        addEventFilter(DirectoryChangesAppliedEvent.DIRECTORY_CHANGES_APPLIED_EVENT_TYPE, handler);
    }

    public void removeDirectoryChangesAppliedFilter(@Nonnull EventHandler<DirectoryChangesAppliedEvent> handler) {
        removeEventFilter(DirectoryChangesAppliedEvent.DIRECTORY_CHANGES_APPLIED_EVENT_TYPE, handler);
    }

    public void addDirectoryChangesFailedFilter(@Nonnull EventHandler<DirectoryChangesFailedEvent> handler) {
        addEventFilter(DirectoryChangesFailedEvent.DIRECTORY_CHANGES_FAILED_EVENT_TYPE, handler);
    }

    public void removeDirectoryChangesFailedFilter(@Nonnull EventHandler<DirectoryChangesFailedEvent> handler) {
        removeEventFilter(DirectoryChangesFailedEvent.DIRECTORY_CHANGES_FAILED_EVENT_TYPE, handler);
    }

    public void addDirectoryChangesResetFilter(@Nonnull EventHandler<DirectoryChangesResetEvent> handler) {
        addEventFilter(DirectoryChangesResetEvent.DIRECTORY_CHANGES_RESET_EVENT_TYPE, handler);
    }

    public void removeDirectoryChangesResetFilter(@Nonnull EventHandler<DirectoryChangesResetEvent> handler) {
        removeEventFilter(DirectoryChangesResetEvent.DIRECTORY_CHANGES_RESET_EVENT_TYPE, handler);
    }
}
