package org.helmo.gbeditor.views;

import javafx.scene.Parent;
import org.helmo.gbeditor.presenters.Presenter;

public interface View {

    void setPresenter(Presenter p);

    Parent getRoot();

}
