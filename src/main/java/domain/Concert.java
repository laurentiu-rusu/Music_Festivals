package domain;

public class Concert {
    private int id;
    private String name;
    private String date;
    private String place;
    private int emptySeats;
    private int takenSeats;

    public Concert(int id, String name, String date, String place, int emptySeats, int takenSeats) {
        this.id = id;
        this.name = name;
        this.date = date;
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

    public String getDate() {
        return date;
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
                ", place='" + place + '\'' +
                ", emptySeats=" + emptySeats +
                ", takenSeats=" + takenSeats +
                '}';
    }
}
