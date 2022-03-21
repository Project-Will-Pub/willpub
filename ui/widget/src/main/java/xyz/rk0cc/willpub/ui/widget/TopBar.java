package xyz.rk0cc.willpub.ui.widget;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class TopBar extends Region {
    private static final double SIDE_RESERVE_WIDTH = 50d;
    private final HBox leftBox = new HBox(),
                       centerBox = new HBox(),
                       rightBox = new HBox();

    public TopBar() {
        HBox container = new HBox();
        container.setPrefHeight(50d);
        container.setMinHeight(USE_PREF_SIZE);
        container.setMaxSize(Double.MAX_VALUE, USE_PREF_SIZE);
        container.setPadding(new Insets(7.5d));
        container.setAlignment(Pos.CENTER_LEFT);

        leftBox.setMinWidth(SIDE_RESERVE_WIDTH);
        leftBox.setMaxHeight(Double.MAX_VALUE);
        leftBox.setAlignment(Pos.CENTER_LEFT);
        rightBox.setMinWidth(SIDE_RESERVE_WIDTH);
        rightBox.setMaxHeight(Double.MAX_VALUE);
        rightBox.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(centerBox, Priority.ALWAYS);
        centerBox.setMaxHeight(Double.MAX_VALUE);
        centerBox.setAlignment(Pos.CENTER);

        container.getChildren().addAll(leftBox, centerBox, rightBox);
        super.getChildren().add(container);
    }

    @Override
    public ObservableList<Node> getChildren() {
        return centerBox.getChildren();
    }

    @Override
    public ObservableList<Node> getChildrenUnmodifiable() {
        return centerBox.getChildrenUnmodifiable();
    }

    public final ObservableList<Node> getLeft() {
        return leftBox.getChildren();
    }

    public final ObservableList<Node> getLeftUnmodifiable() {
        return leftBox.getChildrenUnmodifiable();
    }

    public final ObservableList<Node> getRight() {
        return rightBox.getChildren();
    }

    public final ObservableList<Node> getRightUnmodifiable() {
        return rightBox.getChildrenUnmodifiable();
    }
}
