package domains;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class Concert implements Serializable {
    private int id;
    private String name;
    private LocalDate date;
    private LocalTime time;
    private String place;
    private int emptySeats;
    private int takenSeats;

    public Concert(int id, String name, LocalDate date, LocalTime time, String place, int takenSeats, int emptySeats) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.place = place;
        this.emptySeats = emptySeats;
        this.takenSeats = takenSeats;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getPlace() {
        return place;
    }

    public int getEmptySeats() {
        return emptySeats;
    }

    public int getTakenSeats() {
        return takenSeats;
    }

    @Override
    public String toString() {
        return "Concert{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", time=" + time +
                ", place='" + place + '\'' +
                ", emptySeats=" + emptySeats +
                ", takenSeats=" + takenSeats +
                '}';
    }
}
