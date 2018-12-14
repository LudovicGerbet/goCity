package epitech.project.gerbet_l.gocity;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Event implements Serializable {

    private String id;
    private User creator;
    private String title;
    private String date;
    private String start;
    private String end;
    private List<User> players;

    public Event() {
        this.id = null;
        this.creator = null;
        this.title = null;
        this.date = null;
        this.start = null;
        this.end = null;
        this.players = null;
    }

    public Event(String id, User creator, String title, String date, String start, String end) {
        this.id = id;
        this.creator = creator;
        this.title = title;
        this.date = date;
        this.start = start;
        this.end = end;
        this.players = null;
    }

    /*
    ** GET FUNCTIONS
    */

    public String getId() {
        return id;
    }

    public User getCreator() {
        return creator;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public List<User> getPlayers() {
        return players;
    }

    /*
    ** SET FUNCTIONS
    */

    public void setId(String id) {
        this.id = id;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public void setPlayers(List<User> players) {
        this.players = players;
    }

    public void addPlayer(User player) {
        if (this.players == null) {
            this.players = new ArrayList<User>();
        }
        this.players.add(player);
    }
}
