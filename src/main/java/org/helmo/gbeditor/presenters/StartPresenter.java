package org.helmo.gbeditor.presenters;

import org.helmo.gbeditor.exceptions.DataBaseException;
import org.helmo.gbeditor.models.Editor;
import org.helmo.gbeditor.repo.Repo;
import org.helmo.gbeditor.repo.dbRepo;
import org.helmo.gbeditor.views.StartView;
import org.helmo.gbeditor.views.Views;


public class StartPresenter implements Presenter{

    private MainPresenter mainPresenter;
    private Repo repository;
    private StartView view;


    public StartPresenter(StartView v, MainPresenter mp, Repo repo){
        this.view = v;
        this.mainPresenter = mp;
        this.repository = repo;
    }

    /**
     * Set the author's firstname and lastname
     */
    public void clickOnConnect(String fname, String lname){

        try {
            if (repository instanceof dbRepo) {
                ((dbRepo) repository).addAuthor(new Editor(fname, lname));
            }
            mainPresenter.setNames(fname, lname);
            goTo(Views.EDITION);
        } catch (DataBaseException dbe){
            view.setStatus(dbe.getMessage());
        }

    }

    @Override
    public void goTo(Views name) {
        mainPresenter.goTo(name);
    }
}
