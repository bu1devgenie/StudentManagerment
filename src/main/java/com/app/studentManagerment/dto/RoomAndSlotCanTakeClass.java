package com.app.studentManagerment.dto;

import com.app.studentManagerment.entity.Room;

public class RoomAndSlotCanTakeClass {
    private long room_id;
    private boolean secondSlotAt2;
    private boolean secondSlotAt4;
    private boolean secondSlotAt6;

    public RoomAndSlotCanTakeClass() {
    }

    public RoomAndSlotCanTakeClass(long room_id, boolean secondSlotAt2, boolean secondSlotAt4, boolean secondSlotAt6) {
        this.room_id = room_id;
        this.secondSlotAt2 = secondSlotAt2;
        this.secondSlotAt4 = secondSlotAt4;
        this.secondSlotAt6 = secondSlotAt6;
    }

    public long getRoom_id() {
        return room_id;
    }

    public void setRoom_id(long room_id) {
        this.room_id = room_id;
    }

    public boolean isSecondSlotAt2() {
        return secondSlotAt2;
    }

    public void setSecondSlotAt2(boolean secondSlotAt2) {
        this.secondSlotAt2 = secondSlotAt2;
    }

    public boolean isSecondSlotAt4() {
        return secondSlotAt4;
    }

    public void setSecondSlotAt4(boolean secondSlotAt4) {
        this.secondSlotAt4 = secondSlotAt4;
    }

    public boolean isSecondSlotAt6() {
        return secondSlotAt6;
    }

    public void setSecondSlotAt6(boolean secondSlotAt6) {
        this.secondSlotAt6 = secondSlotAt6;
    }

    @Override
    public String toString() {
        return "RoomAndSlotCanTakeClass{" +
               "room_id=" + room_id +
               ", secondSlotAt2=" + secondSlotAt2 +
               ", secondSlotAt4=" + secondSlotAt4 +
               ", secondSlotAt6=" + secondSlotAt6 +
               '}';
    }
}
