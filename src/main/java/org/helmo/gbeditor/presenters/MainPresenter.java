package org.helmo.gbeditor.presenters;

import org.helmo.gbeditor.repo.Repo;
import org.helmo.gbeditor.repo.dbRepo;
import org.helmo.gbeditor.views.*;

public class MainPresenter implements Presenter {

    private ContainerView cv;
    private String fname;
    private String lname;

    public MainPresenter(ContainerView v){
        this.cv = v;
    }

    public String getFname(){ return fname; }
    public String getLname(){return lname;}

    public void setNames(String fname, String lname){
        cv.setHeader(fname, lname);
        this.fname = fname;
        this.lname = lname;
    }

    @Override
    public void goTo(Views name) {
        Repo rep = new dbRepo("ip:port");
        View v;
        Presenter p;

        switch(name.ordinal()){
            case 1: //CREATION
                v = new CreationView();
                p = new CreationPresenter((CreationView) v, this, rep);
                break;
            case 2: //EDITION
                v = new EditionView();
                p = new EditionPresenter((EditionView) v, this, rep);
                break;
            default:
                v = new StartView();
                p = new StartPresenter((StartView) v, this, rep);
                break;
        }

        cv.changeView(v, p);

    }

}
