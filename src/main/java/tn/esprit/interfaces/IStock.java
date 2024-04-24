package tn.esprit.interfaces;

import java.util.List;

public interface IStock<S> {
    void add(S s);
    void update(S s);
    void delete(S s);
    List<S> getAll();
    S getOne(int id);
}
