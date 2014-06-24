package com.example.testdrawer;

public class CalendarRecord {
    private int _id;
    private String accName;
    private String accType;
    private String syncId;
    private String displayName;
    private int accessLevel;
    private String ownerAccount;
    private boolean checked;
    
    public CalendarRecord(int id, String an, String at, String sid,
            String dn, int al, String oa) {
        init(id, an, at, sid, dn, al, oa, false);
    }

    public CalendarRecord(int id, String an, String at, String sid,
            String dn, int al, String oa, boolean ch) {
        init(id, an, at, sid, dn, al, oa, ch);
    }

    private void init(int id, String an, String at, String sid,
            String dn, int al, String oa, boolean ch) {
        _id = id;
        accName = an;
        accType = at;
        syncId = sid;
        displayName = dn;
        accessLevel = al;
        ownerAccount = oa;
        checked = ch;
    }
    
    public String toString() {
        return "id " + _id + " " + accName + " type " + accType
                + " syncId=" + syncId + " " + displayName
                + " accessLevel=" + accessLevel;
    }
    
    public long getId() {
        return _id;
    }
    
    public String getCalendarTitle() {
        return displayName;
    }
    
    public void setCalendarChecked(boolean ch) {
        checked = ch;
    }
    
    public boolean getCalendarChecked() {
        return checked;
    }
}
