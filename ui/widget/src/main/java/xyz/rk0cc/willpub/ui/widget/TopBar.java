package xyz.rk0cc.willpub.ui.widget;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

/**
 * Extremely basic widget that display context at the top in Will Pub UI.
 * <br/>
 * This provides 3 {@link HBox} as containers of left, center and right side.
 * 
 * @since 1.0.0
 */
public class TopBar extends Region {
    /**
     * Reserved width for both sides' container.
     */
    private static final double SIDE_RESERVE_WIDTH = 50d;
    /**
     * Containers that appeared from different sides.
     */
    private final HBox leftBox = new HBox(),
                       centerBox = new HBox(),
                       rightBox = new HBox();

    /**
     * Construct a top bar with defined width and height of containers.
     */
    public TopBar() {
        // Master container setup
        HBox container = new HBox();
        container.setPrefHeight(50d);
        container.setMinHeight(USE_PREF_SIZE);
        container.setMaxSize(Double.MAX_VALUE, USE_PREF_SIZE);
        container.setPadding(new Insets(5d));
        container.setAlignment(Pos.CENTER_LEFT);
        
        // Left box preference
        leftBox.setMinWidth(SIDE_RESERVE_WIDTH);
        leftBox.setMaxHeight(Double.MAX_VALUE);
        leftBox.setAlignment(Pos.CENTER_LEFT);
        // Right box preference
        rightBox.setMinWidth(SIDE_RESERVE_WIDTH);
        rightBox.setMaxHeight(Double.MAX_VALUE);
        rightBox.setAlignment(Pos.CENTER_RIGHT);
        // Center preference
        HBox.setHgrow(centerBox, Priority.ALWAYS);
        centerBox.setMaxSide(Double.MAX_VALUE, Double.MAX_VALUE);
        centerBox.setAlignment(Pos.CENTER);

        container.getChildren().addAll(leftBox, centerBox, rightBox);
        super.getChildren().add(container);
    }

    /**
     * {@inheritDoc}
     *
     * @return Children from center container.
     */
    @Override
    public ObservableList<Node> getChildren() {
        return centerBox.getChildren();
    }

    /**
     * {@inheritDoc}
     *
     * @return Children from center container.
     */
    @Override
    public ObservableList<Node> getChildrenUnmodifiable() {
        return centerBox.getChildrenUnmodifiable();
    }

    /**
     * Gets a list of children in left container.
     *
     * @return The list of children from left container.
     */
    public final ObservableList<Node> getLeftChildren() {
        return leftBox.getChildren();
    }

    /**
     * Gets a unmodifiable list of children in left container.
     *
     * @return The unmodifiable list of children from left container.
     */
    public final ObservableList<Node> getLeftChildrenUnmodifiable() {
        return leftBox.getChildrenUnmodifiable();
    }

    /**
     * Gets a list of children in right container.
     *
     * @return The list of children from right container.
     */
    public final ObservableList<Node> getRightChildren() {
        return rightBox.getChildren();
    }

    /**
     * Gets a unmodifiable list of children in right container.
     *
     * @return The unmodifiable list of children from right container.
     */
    public final ObservableList<Node> getRightChildrenUnmodifiable() {
        return rightBox.getChildrenUnmodifiable();
    }
}
