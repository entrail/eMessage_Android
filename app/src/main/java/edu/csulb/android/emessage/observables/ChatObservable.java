package edu.csulb.android.emessage.observables;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class ChatObservable extends Observable {

    private static ChatObservable instance = null;

    List<Observer> observers;

    private ChatObservable() {
        observers = new ArrayList<>();
    }

    public static ChatObservable getInstance() {
        Log.d("TAG", "getInstance: ");
        if (instance == null) {
            instance = new ChatObservable();
        }
        return instance;
    }

    @Override
    public void addObserver(Observer o) {
        Log.d("TAG", "addObserver: " + o);
        observers.add(o);
    }

    @Override
    public void notifyObservers() {
        Log.d("TAG", "notifyObservers: ");
        for (Observer observer : observers) {
            Log.d("TAG", "notifyObservers: " + observer);
            if (observer != null)
            observer.update(this, null);
        }
    }

    @Override
    public void deleteObserver(Observer o) {
        Log.d("TAG", "deleteObserver: " + o);
        observers.remove(o);
    }
}
